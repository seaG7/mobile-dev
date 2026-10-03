package ru.mirea.danilov.anishot.presentation.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        Motion.press(add);
        view.findViewById(R.id.buttonBack).setOnClickListener(v ->
                Navigation.findNavController(v).popBackStack());
        openAuth.setOnClickListener(v -> startActivity(new Intent(requireContext(), AuthActivity.class)));
        add.setOnClickListener(v -> viewModel.saveCurrent());
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
            } else if (save.saved) {
                art.setVisibility(View.GONE);
                openAuth.setVisibility(View.GONE);
                status.setText("Теперь в вашем списке.");
                add.setText("В списке");
                add.setEnabled(false);
            } else {
                art.setVisibility(View.GONE);
                openAuth.setVisibility(View.GONE);
                status.setText("Не удалось сохранить.");
            }
        });
        viewModel.open(id);
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
