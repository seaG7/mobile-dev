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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.details.DetailsActivity;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.CatalogViewModel;

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
        TextView count = view.findViewById(R.id.textCount);
        PosterAdapter adapter = new PosterAdapter(anime -> {
            Intent intent = new Intent(requireContext(), DetailsActivity.class);
            intent.putExtra("animeId", anime.getId());
            startActivity(intent);
        });
        recycler.setAdapter(adapter);
        CatalogViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(this)).get(CatalogViewModel.class);
        viewModel.catalog().observe(getViewLifecycleOwner(), items -> {
            List<Anime> safe = items == null ? new ArrayList<>() : items;
            count.setText(String.valueOf(safe.size()));
            adapter.setItems(safe);
        });
    }

    private static class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.Holder> {
        interface Listener {
            void onClick(Anime anime);
        }

        private final List<Anime> items = new ArrayList<>();
        private final Listener listener;

        PosterAdapter(Listener listener) {
            this.listener = listener;
        }

        void setItems(List<Anime> next) {
            items.clear();
            if (next != null) {
                items.addAll(next);
            }
            notifyDataSetChanged();
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
