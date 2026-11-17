package ru.mirea.danilov.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudioListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StudioViewModel model = new ViewModelProvider(requireActivity()).get(StudioViewModel.class);
        List<Studio> studios = shelf();
        RecyclerView list = view.findViewById(R.id.studioList);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        list.setAdapter(new Adapter(studios, model));
        if (model.selected().getValue() == null && !studios.isEmpty()) {
            model.select(studios.get(0));
        }
    }

    private List<Studio> shelf() {
        List<Studio> studios = new ArrayList<>();
        studios.add(new Studio("Madhouse", "Токио, 1972", "«Фрирен», «Охотник х Охотник», «Паприка»."));
        studios.add(new Studio("MAPPA", "Токио, 2011", "«Магическая битва», «Человек-бензопила»."));
        studios.add(new Studio("Studio Ghibli", "Токио, 1985", "«Унесённые призраками», «Принцесса Мононокэ»."));
        studios.add(new Studio("Kyoto Animation", "Удзи, 1985", "«Вайолет Эвергарден», «Класс превосходства»."));
        studios.add(new Studio("Bones", "Токио, 1998", "«Стальной алхимик», «Моя геройская академия»."));
        studios.add(new Studio("Wit Studio", "Токио, 2012", "Ранние сезоны «Атаки титанов»."));
        studios.add(new Studio("Ufotable", "Токио, 2000", "«Судьба», «Клинок, рассекающий демонов»."));
        studios.add(new Studio("Science SARU", "Токио, 2013", "«Одержимые духами», «Дьявол тоже может плакать»."));
        return studios;
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private final List<Studio> items;
        private final StudioViewModel model;

        Adapter(List<Studio> items, StudioViewModel model) {
            this.items = items;
            this.model = model;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studio, parent, false);
            return new Holder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Studio studio = items.get(position);
            holder.name.setText(studio.name);
            holder.itemView.setOnClickListener(v -> model.select(studio));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class Holder extends RecyclerView.ViewHolder {
            final TextView name;

            Holder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.textName);
            }
        }
    }
}
