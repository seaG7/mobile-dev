package ru.mirea.danilov.anishot.presentation.search;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private String source = "none";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView previewImage = view.findViewById(R.id.imagePreview);
        TextView preview = view.findViewById(R.id.textPreview);
        View cardResult = view.findViewById(R.id.cardResult);
        ImageView resultImage = view.findViewById(R.id.imageResult);
        TextView resultTitle = view.findViewById(R.id.textResultTitle);
        TextView result = view.findViewById(R.id.textResult);
        TextView similarity = view.findViewById(R.id.textSimilarity);
        TextView similarityLabel = view.findViewById(R.id.textSimilarityLabel);
        TextView maskChip = view.findViewById(R.id.textMask);
        TextView open = view.findViewById(R.id.textOpen);
        MaterialButton photo = view.findViewById(R.id.buttonPhoto);
        MaterialButton video = view.findViewById(R.id.buttonVideo);
        View find = view.findViewById(R.id.buttonFind);
        Motion.press(find);
        SearchViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(this)).get(SearchViewModel.class);
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
            maskChip.setText("маска  ·  " + (hit.mask == null ? "" : hit.mask.getLabel()));
            open.setVisibility(View.VISIBLE);
            ImageBinder.load(resultImage, match.getImageUrl());
            cardResult.setOnClickListener(card ->
                    ((HomeActivity) requireActivity()).openDetails(match.getAnilistId()));
        });

        View.OnClickListener pick = clicked -> {
            source = clicked.getId() == R.id.buttonPhoto ? "photo" : "video";
            paintChoice(photo, "photo".equals(source));
            paintChoice(video, "video".equals(source));
            preview.setText("photo".equals(source) ? "Пробный кадр" : "Кадр 12:04");
            ImageBinder.load(previewImage, "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg");
        };
        photo.setOnClickListener(pick);
        video.setOnClickListener(pick);
        find.setOnClickListener(v -> {
            if ("none".equals(source)) {
                showCard(cardResult);
                resultImage.setImageResource(R.drawable.art_guest);
                resultTitle.setText("Кадр не выбран");
                result.setText("Сначала нажмите «Фото» или «Кадр из видео».");
                similarity.setText("");
                similarityLabel.setVisibility(View.GONE);
                maskChip.setVisibility(View.GONE);
                open.setVisibility(View.GONE);
                cardResult.setOnClickListener(null);
                return;
            }
            viewModel.find();
        });
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
