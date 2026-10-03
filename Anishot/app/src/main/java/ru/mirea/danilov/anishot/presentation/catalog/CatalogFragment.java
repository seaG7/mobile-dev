package ru.mirea.danilov.anishot.presentation.catalog;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.GetAnimeCatalogUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.details.DetailsActivity;

public class CatalogFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recyclerCatalog);
        recycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        AnishotApp app = (AnishotApp) requireActivity().getApplication();
        List<Anime> catalog = new GetAnimeCatalogUseCase(app.animeRepository()).execute();
        TextView count = view.findViewById(R.id.textCount);
        count.setText(String.valueOf(catalog.size()));
        recycler.setAdapter(new PosterAdapter(catalog, anime -> {
            Intent intent = new Intent(requireContext(), DetailsActivity.class);
            intent.putExtra("animeId", anime.getId());
            startActivity(intent);
        }));
    }

    private static class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.Holder> {
        interface Listener {
            void onClick(Anime anime);
        }

        private final List<Anime> items;
        private final Listener listener;

        PosterAdapter(List<Anime> items, Listener listener) {
            this.items = items == null ? new ArrayList<>() : items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Anime anime = items.get(position);
            holder.title.setText(anime.getTitle());
            holder.score.setText(String.valueOf(anime.getAverageScore()));
            holder.meta.setText(anime.getMeta());
            ImageBinder.load(holder.poster, anime.getImageUrl());
            holder.itemView.setOnClickListener(v -> listener.onClick(anime));
            if (holder.itemView.getTag() == null) {
                holder.itemView.setTag(Boolean.TRUE);
                holder.itemView.setAlpha(0f);
                holder.itemView.setTranslationY(24f);
                holder.itemView.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setStartDelay(Math.min(position, 5) * 45L)
                        .setDuration(360)
                        .start();
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final ImageView poster;
            final TextView title;
            final TextView meta;
            final TextView score;

            Holder(View itemView) {
                super(itemView);
                poster = itemView.findViewById(R.id.imagePoster);
                title = itemView.findViewById(R.id.textTitle);
                meta = itemView.findViewById(R.id.textMeta);
                score = itemView.findViewById(R.id.textScore);
            }
        }
    }
}
