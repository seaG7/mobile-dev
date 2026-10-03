package ru.mirea.danilov.anishot.presentation.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.GetAnimeCatalogUseCase;
import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.presentation.ImageBinder;

public class MyListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AnishotApp app = (AnishotApp) requireActivity().getApplication();
        TextView empty = view.findViewById(R.id.textEmpty);
        View groupEmpty = view.findViewById(R.id.groupEmpty);
        ImageView imageEmpty = view.findViewById(R.id.imageEmpty);
        RecyclerView recycler = view.findViewById(R.id.recyclerList);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        User user = app.authRepository().getProfile();
        Map<Integer, String> posters = new HashMap<>();
        for (Anime anime : new GetAnimeCatalogUseCase(app.animeRepository()).execute()) {
            posters.put(anime.getId(), anime.getImageUrl());
        }
        List<ListEntry> items = user.isGuest()
                ? new ArrayList<>()
                : new GetMyListUseCase(app.listRepository(), app.authRepository()).execute();
        if (user.isGuest() || items.isEmpty()) {
            groupEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
            imageEmpty.setImageResource(user.isGuest() ? R.drawable.art_guest : R.drawable.art_onb_list);
            empty.setText(user.isGuest()
                    ? "Каталог открыт. Чтобы сохранять тайтлы, войдите."
                    : "Здесь появятся тайтлы из карточки.");
            return;
        }
        groupEmpty.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
        recycler.setAdapter(new Adapter(items, posters));
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private final List<ListEntry> items;
        private final Map<Integer, String> posters;

        Adapter(List<ListEntry> items, Map<Integer, String> posters) {
            this.items = items == null ? new ArrayList<>() : items;
            this.posters = posters;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            ListEntry entry = items.get(position);
            holder.title.setText(entry.getTitle());
            holder.meta.setText("Смотрю");
            holder.score.setText(entry.getScore() > 0 ? String.valueOf(entry.getScore()) : "—");
            ImageBinder.load(holder.poster, posters.get(entry.getAnimeId()));
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
