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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.presentation.ImageBinder;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.ShelfViewModel;

public class MyListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView empty = view.findViewById(R.id.textEmpty);
        View groupEmpty = view.findViewById(R.id.groupEmpty);
        ImageView imageEmpty = view.findViewById(R.id.imageEmpty);
        RecyclerView recycler = view.findViewById(R.id.recyclerList);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        Adapter adapter = new Adapter();
        recycler.setAdapter(adapter);
        ShelfViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(this)).get(ShelfViewModel.class);
        viewModel.state().observe(getViewLifecycleOwner(), state -> {
            if (state == null) {
                return;
            }
            if (state.guest || state.rows.isEmpty()) {
                groupEmpty.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
                imageEmpty.setImageResource(state.guest ? R.drawable.art_guest : R.drawable.art_onb_list);
                empty.setText(state.guest
                        ? "Каталог открыт. Чтобы сохранять тайтлы, войдите."
                        : "Здесь появятся тайтлы из карточки.");
                return;
            }
            groupEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
            adapter.setItems(state.rows);
        });
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private final List<ShelfViewModel.Row> items = new ArrayList<>();

        void setItems(List<ShelfViewModel.Row> next) {
            items.clear();
            if (next != null) {
                items.addAll(next);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            ShelfViewModel.Row row = items.get(position);
            holder.title.setText(row.entry.getTitle());
            holder.meta.setText("Смотрю");
            holder.score.setText(row.entry.getScore() > 0 ? String.valueOf(row.entry.getScore()) : "—");
            ImageBinder.load(holder.poster, row.poster);
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
