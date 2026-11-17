package ru.mirea.danilov.anishot.presentation.details;

import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.presentation.AuthActivity;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.Motion;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.DetailsViewModel;

public class DetailsFragment extends Fragment {
    private int pendingScore = 8;

    public static DetailsFragment newInstance(int animeId) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putInt("animeId", animeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int id = requireArguments().getInt("animeId", 1);
        DetailsViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(this)).get(DetailsViewModel.class);
        View notice = view.findViewById(R.id.cardNotice);
        ImageView art = view.findViewById(R.id.imageNotice);
        TextView status = view.findViewById(R.id.textStatus);
        MaterialButton openAuth = view.findViewById(R.id.buttonOpenAuth);
        MaterialButton add = view.findViewById(R.id.buttonAdd);
        View cardManage = view.findViewById(R.id.cardManage);
        MaterialButton statusWatching = view.findViewById(R.id.statusWatching);
        MaterialButton statusDone = view.findViewById(R.id.statusDone);
        MaterialButton statusPlan = view.findViewById(R.id.statusPlan);
        MaterialButton scoreMinus = view.findViewById(R.id.buttonScoreMinus);
        MaterialButton scorePlus = view.findViewById(R.id.buttonScorePlus);
        TextView myScore = view.findViewById(R.id.textMyScore);
        MaterialButton rate = view.findViewById(R.id.buttonRate);
        MaterialButton remove = view.findViewById(R.id.buttonRemove);

        Motion.press(add);
        View back = view.findViewById(R.id.buttonBack);
        int baseTop = ((ViewGroup.MarginLayoutParams) back.getLayoutParams()).topMargin;
        ViewCompat.setOnApplyWindowInsetsListener(back, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = baseTop + bars.top;
            v.setLayoutParams(params);
            return insets;
        });
        back.setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());
        openAuth.setOnClickListener(v -> startActivity(new Intent(requireContext(), AuthActivity.class)));
        add.setOnClickListener(v -> viewModel.saveCurrent());

        statusWatching.setOnClickListener(v -> viewModel.setStatus("Смотрю"));
        statusDone.setOnClickListener(v -> viewModel.setStatus("Просмотрено"));
        statusPlan.setOnClickListener(v -> viewModel.setStatus("В планах"));

        scoreMinus.setOnClickListener(v -> {
            pendingScore = Math.max(1, pendingScore - 1);
            myScore.setText("Оценка: " + pendingScore + "/10");
        });
        scorePlus.setOnClickListener(v -> {
            pendingScore = Math.min(10, pendingScore + 1);
            myScore.setText("Оценка: " + pendingScore + "/10");
        });
        rate.setOnClickListener(v -> viewModel.rate(pendingScore));
        remove.setOnClickListener(v -> viewModel.remove());

        viewModel.anime().observe(getViewLifecycleOwner(), anime -> {
            if (anime == null) {
                getParentFragmentManager().popBackStack();
                return;
            }
            bind(view, anime);
        });

        viewModel.save().observe(getViewLifecycleOwner(), save -> {
            if (save == null) {
                return;
            }
            notice.setVisibility(View.VISIBLE);
            Motion.rise(notice);
            if (save.guest) {
                art.setVisibility(View.VISIBLE);
                openAuth.setVisibility(View.VISIBLE);
                status.setText("Войдите, чтобы сохранить тайтл в список.");
            } else {
                art.setVisibility(View.GONE);
                openAuth.setVisibility(View.GONE);
                status.setText(save.message);
            }
        });

        viewModel.entryState().observe(getViewLifecycleOwner(), entry -> {
            if (entry == null) {
                return;
            }
            if (!entry.guest && entry.inList) {
                add.setText("В списке");
                add.setEnabled(false);
                cardManage.setVisibility(View.VISIBLE);
                paintChoice(statusWatching, "Смотрю".equals(entry.status));
                paintChoice(statusDone, "Просмотрено".equals(entry.status));
                paintChoice(statusPlan, "В планах".equals(entry.status));
                pendingScore = entry.score > 0 ? entry.score : 8;
                myScore.setText(entry.score > 0
                        ? "Оценка: " + entry.score + "/10"
                        : "Выбор: " + pendingScore + "/10");
            } else {
                add.setText("В список    ›››");
                add.setEnabled(true);
                cardManage.setVisibility(View.GONE);
            }
        });

        viewModel.open(id);
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

    private void bind(View view, Anime anime) {
        ImageBinder.load(view.findViewById(R.id.imagePoster), anime.getImageUrl());
        ((TextView) view.findViewById(R.id.textTitle)).setText(anime.getTitle());
        ((TextView) view.findViewById(R.id.textOriginal)).setText(anime.getOriginalTitle());
        ((TextView) view.findViewById(R.id.textMeta)).setText(anime.getMeta());
        ((TextView) view.findViewById(R.id.textDescription)).setText(anime.getDescription());
        ((TextView) view.findViewById(R.id.textScore)).setText(String.valueOf(anime.getAverageScore()));
    }
}
