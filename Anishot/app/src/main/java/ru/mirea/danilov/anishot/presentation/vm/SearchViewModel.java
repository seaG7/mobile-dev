package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.danilov.anishot.domain.BuildFrameMaskUseCase;
import ru.mirea.danilov.anishot.domain.GetScenePreviewUseCase;
import ru.mirea.danilov.anishot.domain.IdentifyAnimeByFrameUseCase;
import ru.mirea.danilov.anishot.domain.SaveFoundSceneUseCase;
import ru.mirea.danilov.anishot.domain.SelectFrameFromVideoUseCase;
import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class SearchViewModel extends ViewModel {
    private static final String TAG = "SearchViewModel";

    public static final class Hit {
        public final SceneMatch match;
        public final FrameMask mask;

        Hit(SceneMatch match, FrameMask mask) {
            this.match = match;
            this.mask = mask;
        }
    }

    public static final class SceneSave {
        public final boolean guest;
        public final boolean saved;

        SceneSave(boolean guest, boolean saved) {
            this.guest = guest;
            this.saved = saved;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final IdentifyAnimeByFrameUseCase identifyUseCase;
    private final BuildFrameMaskUseCase maskUseCase;
    private final SelectFrameFromVideoUseCase selectFrameUseCase;
    private final GetScenePreviewUseCase previewUseCase;
    private final SaveFoundSceneUseCase saveSceneUseCase;
    private final AuthRepository authRepository;

    private final MutableLiveData<Hit> hit = new MutableLiveData<>();
    private final MutableLiveData<String> videoFramePath = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<SceneSave> sceneSave = new MutableLiveData<>();

    public SearchViewModel(IdentifyAnimeByFrameUseCase identifyUseCase,
                           BuildFrameMaskUseCase maskUseCase,
                           SelectFrameFromVideoUseCase selectFrameUseCase,
                           GetScenePreviewUseCase previewUseCase,
                           SaveFoundSceneUseCase saveSceneUseCase,
                           AuthRepository authRepository) {
        this.identifyUseCase = identifyUseCase;
        this.maskUseCase = maskUseCase;
        this.selectFrameUseCase = selectFrameUseCase;
        this.previewUseCase = previewUseCase;
        this.saveSceneUseCase = saveSceneUseCase;
        this.authRepository = authRepository;
        Log.d(TAG, "SearchViewModel created");
    }

    public LiveData<Hit> hit() {
        return hit;
    }

    public LiveData<String> videoFramePath() {
        return videoFramePath;
    }

    public LiveData<Boolean> loading() {
        return loading;
    }

    public LiveData<SceneSave> sceneSave() {
        return sceneSave;
    }

    public void extractVideoFrame(String videoUri) {
        loading.setValue(true);
        executor.execute(() -> {
            Frame frame = selectFrameUseCase.execute(videoUri);
            videoFramePath.postValue(frame == null ? "" : frame.getPath());
            loading.postValue(false);
        });
    }

    public void find(String imagePath) {
        loading.setValue(true);
        sceneSave.setValue(null);
        executor.execute(() -> {
            FrameMask mask = maskUseCase.execute(imagePath);
            SceneMatch match = identifyUseCase.execute(imagePath);
            hit.postValue(new Hit(match, mask));
            loading.postValue(false);
        });
    }

    public SceneMatch preview() {
        return previewUseCase.execute();
    }

    public void saveCurrentScene() {
        Hit current = hit.getValue();
        SceneMatch match = current != null && current.match != null ? current.match : previewUseCase.execute();
        boolean guest = authRepository.getProfile().isGuest();
        boolean saved = saveSceneUseCase.execute(match);
        sceneSave.setValue(new SceneSave(guest, saved));
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        Log.d(TAG, "SearchViewModel cleared");
        super.onCleared();
    }
}
