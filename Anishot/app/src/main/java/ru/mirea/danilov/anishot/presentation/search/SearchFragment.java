package ru.mirea.danilov.anishot.presentation.search;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.presentation.HomeActivity;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.Motion;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.SearchViewModel;

public class SearchFragment extends Fragment {
    private static final String STATE_SOURCE = "state_source";
    private static final String STATE_PATH = "state_path";
    private static final String DEMO_URL = "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg";

    private String source = "none";
    private String selectedPath = "";

    private SearchViewModel viewModel;
    private ImageView previewImage;
    private TextView preview;
    private MaterialButton photo;
    private MaterialButton video;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickPhoto =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri == null) {
                    return;
                }
                applySelection("photo", uri.toString(), "Фото выбрано");
            });

    private final ActivityResultLauncher<PickVisualMediaRequest> pickVideo =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri == null || viewModel == null) {
                    return;
                }
                viewModel.extractVideoFrame(uri.toString());
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ScrollView scroll = view.findViewById(R.id.scrollSearch);
        previewImage = view.findViewById(R.id.imagePreview);
        preview = view.findViewById(R.id.textPreview);
        TextView demo = view.findViewById(R.id.textDemo);
        View cardResult = view.findViewById(R.id.cardResult);
        ImageView resultImage = view.findViewById(R.id.imageResult);
        TextView resultTitle = view.findViewById(R.id.textResultTitle);
        TextView result = view.findViewById(R.id.textResult);
        TextView similarity = view.findViewById(R.id.textSimilarity);
        TextView similarityLabel = view.findViewById(R.id.textSimilarityLabel);
        TextView maskChip = view.findViewById(R.id.textMask);
        TextView open = view.findViewById(R.id.textOpen);
        View groupActions = view.findViewById(R.id.groupSceneActions);
        TextView buttonClip = view.findViewById(R.id.buttonClip);
        TextView buttonSaveScene = view.findViewById(R.id.buttonSaveScene);
        photo = view.findViewById(R.id.buttonPhoto);
        video = view.findViewById(R.id.buttonVideo);
        MaterialButton find = view.findViewById(R.id.buttonFind);
        Motion.press(find);

        viewModel = new ViewModelProvider(this, AnishotFactory.from(this)).get(SearchViewModel.class);

        if (savedInstanceState != null) {
            source = savedInstanceState.getString(STATE_SOURCE, "none");
            selectedPath = savedInstanceState.getString(STATE_PATH, "");
            if (!"none".equals(source)) {
                String label = "photo".equals(source) ? "Фото выбрано"
                        : ("video".equals(source) ? "Кадр из видео" : "Пробный кадр");
                applySelection(source, selectedPath, label);
            }
        }

        viewModel.loading().observe(getViewLifecycleOwner(), busy -> {
            boolean running = Boolean.TRUE.equals(busy);
            find.setEnabled(!running);
            find.setText(running ? "Ищу сцену…" : "Найти    ›››");
        });

        viewModel.videoFramePath().observe(getViewLifecycleOwner(), framePath -> {
            if (framePath == null || framePath.isEmpty()) {
                return;
            }
            applySelection("video", framePath, "Кадр из видео");
        });

        viewModel.hit().observe(getViewLifecycleOwner(), hit -> {
            if (hit == null || hit.match == null) {
                return;
            }
            SceneMatch match = hit.match;
            int percent = (int) Math.round(match.getSimilarity() * 100);
            int seconds = (int) match.getAt();
            String stamp = String.format(Locale.US, "%d:%02d", seconds / 60, seconds % 60);
            showCard(cardResult);
            resultTitle.setText(match.getTitle());
            result.setText("Серия " + match.getEpisode() + "   ·   " + stamp);
            similarity.setText(percent + "%");
            similarityLabel.setVisibility(View.VISIBLE);
            maskChip.setVisibility(View.VISIBLE);
            String maskLabel = hit.mask == null ? "frame" : hit.mask.getLabel();
            maskChip.setText("TFLite  ·  " + maskLabel);
            open.setVisibility(View.VISIBLE);
            groupActions.setVisibility(View.VISIBLE);
            buttonSaveScene.setText("Сохранить сцену");
            ImageBinder.load(resultImage, match.getImageUrl());
            cardResult.setOnClickListener(card ->
                    ((HomeActivity) requireActivity()).openDetails(match.getAnilistId()));
            scroll.post(() -> scroll.smoothScrollTo(0, cardResult.getBottom()));
        });

        viewModel.sceneSave().observe(getViewLifecycleOwner(), save -> {
            if (save == null) {
                return;
            }
            if (save.guest) {
                Toast.makeText(requireContext(), "Войдите, чтобы сохранить сцену.", Toast.LENGTH_SHORT).show();
            } else if (save.saved) {
                buttonSaveScene.setText("Сцена сохранена");
                Toast.makeText(requireContext(), "Сцена сохранена в дневник.", Toast.LENGTH_SHORT).show();
            }
        });

        photo.setOnClickListener(v -> pickPhoto.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        ));

        video.setOnClickListener(v -> pickVideo.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                        .build()
        ));

        demo.setOnClickListener(v -> applySelection("demo", "demo", "Пробный кадр"));

        buttonClip.setOnClickListener(v -> showClipPreview(viewModel.preview()));
        buttonSaveScene.setOnClickListener(v -> viewModel.saveCurrentScene());

        find.setOnClickListener(v -> {
            if ("none".equals(source)) {
                showCard(cardResult);
                resultImage.setImageResource(R.drawable.art_guest);
                resultTitle.setText("Кадр не выбран");
                result.setText("Выберите «Фото», «Кадр из видео» или «Пробный кадр».");
                similarity.setText("");
                similarityLabel.setVisibility(View.GONE);
                maskChip.setVisibility(View.GONE);
                open.setVisibility(View.GONE);
                groupActions.setVisibility(View.GONE);
                cardResult.setOnClickListener(null);
                return;
            }
            viewModel.find(selectedPath);
        });
    }

    private void applySelection(String nextSource, String path, String label) {
        source = nextSource;
        selectedPath = path == null ? "" : path;
        paintChoice(photo, "photo".equals(source));
        paintChoice(video, "video".equals(source));
        preview.setText(label);
        String previewTarget = ("demo".equals(selectedPath) || selectedPath.isEmpty()) ? DEMO_URL : selectedPath;
        ImageBinder.load(previewImage, previewTarget);
    }

    private void showClipPreview(SceneMatch match) {
        if (match == null || match.getVideoUrl() == null || match.getVideoUrl().isEmpty()) {
            Toast.makeText(requireContext(), "Превью-клип доступен при поиске онлайн.", Toast.LENGTH_SHORT).show();
            return;
        }
        FrameLayout box = new FrameLayout(requireContext());
        int pad = dp(12);
        box.setPadding(pad, pad, pad, pad);
        VideoView videoView = new VideoView(requireContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(220)
        );
        videoView.setLayoutParams(params);
        MediaController controller = new MediaController(requireContext());
        controller.setAnchorView(videoView);
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(match.getVideoUrl()));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoView.start();
        });
        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(requireContext(), "Не удалось воспроизвести клип.", Toast.LENGTH_SHORT).show();
            return true;
        });
        box.addView(videoView);
        new AlertDialog.Builder(requireContext())
                .setTitle(match.getTitle() + " · серия " + match.getEpisode())
                .setView(box)
                .setPositiveButton("Закрыть", (d, w) -> videoView.stopPlayback())
                .setOnDismissListener(d -> videoView.stopPlayback())
                .show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SOURCE, source);
        outState.putString(STATE_PATH, selectedPath);
    }

    private void showCard(View card) {
        card.setVisibility(View.VISIBLE);
        Motion.rise(card);
    }

    private void paintChoice(MaterialButton button, boolean selected) {
        int background = ContextCompat.getColor(requireContext(), selected ? R.color.ink : R.color.surface);
        int text = ContextCompat.getColor(requireContext(), selected ? R.color.white : R.color.ink);
        button.setBackgroundTintList(ColorStateList.valueOf(background));
        button.setTextColor(text);
        button.setStrokeWidth(selected ? 0 : dp(1));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
