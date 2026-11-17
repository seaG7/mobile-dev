package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.danilov.anishot.data.network.NetworkApi;
import ru.mirea.danilov.anishot.data.network.TraceMoeResponse;
import ru.mirea.danilov.anishot.data.network.TraceMoeService;
import ru.mirea.danilov.anishot.data.storage.room.AppDatabase;
import ru.mirea.danilov.anishot.data.storage.room.SavedSceneEntity;
import ru.mirea.danilov.anishot.data.storage.room.SceneDao;
import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

public class SceneRepositoryImpl implements SceneRepository {
    private static final String TAG = "SceneRepository";
    private static final String TRACE_MOE_URL = "https://api.trace.moe/";

    private final Context appContext;
    private final NetworkApi networkApi;
    private final SceneDao sceneDao;
    private final TraceMoeService traceMoe;
    private SceneMatch lastPreview;

    public SceneRepositoryImpl(Context context, NetworkApi networkApi) {
        this.appContext = context.getApplicationContext();
        this.networkApi = networkApi;
        this.sceneDao = AppDatabase.getInstance(this.appContext).sceneDao();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        this.traceMoe = new Retrofit.Builder()
                .baseUrl(TRACE_MOE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TraceMoeService.class);
    }

    @Override
    public Frame selectFrameFromVideo() {
        return new Frame("video-frame");
    }

    @Override
    public Frame selectFrameFromVideo(String videoUri) {
        if (videoUri == null || videoUri.isEmpty()) {
            return selectFrameFromVideo();
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (videoUri.startsWith("content://") || videoUri.startsWith("file://")) {
                retriever.setDataSource(appContext, Uri.parse(videoUri));
            } else {
                retriever.setDataSource(videoUri);
            }
            long durationMs = 0L;
            String durStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            if (durStr != null) {
                durationMs = Long.parseLong(durStr);
            }
            long targetUs = durationMs > 400 ? durationMs * 250L : -1L;
            Bitmap frame = retriever.getFrameAtTime(targetUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if (frame == null) {
                frame = retriever.getFrameAtTime(-1L);
            }
            if (frame == null) {
                return selectFrameFromVideo();
            }
            File out = saveScaledJpeg(frame, "video_frame.jpg");
            frame.recycle();
            return new Frame(out == null ? "" : out.getAbsolutePath());
        } catch (Exception e) {
            Log.w(TAG, "Video frame extraction failed: " + e.getMessage());
            return selectFrameFromVideo();
        } finally {
            try {
                retriever.release();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public SceneMatch identifyByFrame() {
        SceneMatch stub = parseStub();
        lastPreview = stub;
        return stub;
    }

    @Override
    public SceneMatch identifyByFrame(String imagePath) {
        File uploadFile = prepareUploadFile(imagePath);
        if (uploadFile != null && uploadFile.exists()) {
            try {
                RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), uploadFile);
                MultipartBody.Part part = MultipartBody.Part.createFormData("image", uploadFile.getName(), body);
                Response<TraceMoeResponse> response = traceMoe.search(part).execute();
                if (response.isSuccessful() && response.body() != null) {
                    SceneMatch live = response.body().firstMatch();
                    if (live != null) {
                        lastPreview = live;
                        return live;
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "trace.moe search failed, falling back to stub: " + e.getMessage());
            }
        }
        return identifyByFrame();
    }

    @Override
    public SceneMatch getPreview() {
        return lastPreview != null ? lastPreview : parseStub();
    }

    @Override
    public boolean saveScene(SceneMatch match) {
        if (match == null) {
            return false;
        }
        sceneDao.upsert(new SavedSceneEntity(
                match.getAnilistId(),
                safe(match.getTitle()),
                match.getEpisode(),
                match.getAt(),
                match.getSimilarity(),
                safe(match.getImageUrl()),
                safe(match.getVideoUrl())
        ));
        return true;
    }

    @Override
    public List<SceneMatch> getSavedScenes() {
        List<SceneMatch> out = new ArrayList<>();
        for (SavedSceneEntity entity : sceneDao.getAll()) {
            out.add(new SceneMatch(
                    entity.anilistId,
                    entity.title,
                    entity.episode,
                    entity.at,
                    entity.similarity,
                    entity.imageUrl,
                    entity.videoUrl
            ));
        }
        return out;
    }

    private File prepareUploadFile(String imagePath) {
        if (imagePath == null || imagePath.isEmpty() || "demo".equals(imagePath) || "video-frame".equals(imagePath)) {
            return null;
        }
        Bitmap bitmap = decodeBitmap(imagePath);
        if (bitmap == null) {
            File direct = new File(imagePath);
            return direct.exists() ? direct : null;
        }
        try {
            return saveScaledJpeg(bitmap, "query_frame.jpg");
        } finally {
            bitmap.recycle();
        }
    }

    private Bitmap decodeBitmap(String imagePath) {
        try {
            if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                try (InputStream is = appContext.getContentResolver().openInputStream(Uri.parse(imagePath))) {
                    return is == null ? null : BitmapFactory.decodeStream(is);
                }
            }
            return BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {
            return null;
        }
    }

    private File saveScaledJpeg(Bitmap source, String filename) {
        try {
            int maxDim = 960;
            int w = source.getWidth();
            int h = source.getHeight();
            Bitmap scaled = source;
            if (w > maxDim || h > maxDim) {
                float scale = (float) maxDim / Math.max(w, h);
                scaled = Bitmap.createScaledBitmap(source, Math.max(1, Math.round(w * scale)), Math.max(1, Math.round(h * scale)), true);
            }
            File out = new File(appContext.getCacheDir(), filename);
            try (FileOutputStream fos = new FileOutputStream(out)) {
                scaled.compress(Bitmap.CompressFormat.JPEG, 88, fos);
            }
            if (scaled != source) {
                scaled.recycle();
            }
            return out;
        } catch (Exception e) {
            return null;
        }
    }

    private SceneMatch parseStub() {
        try {
            JSONObject object = new JSONObject(networkApi.getSceneJson());
            return new SceneMatch(
                    object.getInt("anilistId"),
                    object.getString("title"),
                    object.getInt("episode"),
                    object.getDouble("at"),
                    object.getDouble("similarity"),
                    object.optString("imageUrl", ""),
                    object.optString("videoUrl", "")
            );
        } catch (Exception exception) {
            return new SceneMatch(1, "Фрирен", 14, 724.0, 0.974,
                    "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg", "");
        }
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
