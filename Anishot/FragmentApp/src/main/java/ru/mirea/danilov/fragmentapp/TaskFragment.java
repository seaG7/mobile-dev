package ru.mirea.danilov.fragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    private final List<String> titles = new ArrayList<>();
    private final List<Boolean> done = new ArrayList<>();
    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int number = requireArguments().getInt("my_number_student");
        ((TextView) view.findViewById(R.id.textNumber)).setText("Номер по списку: " + number);
        if (titles.isEmpty()) {
            add("Досмотреть 14 серию «Фрирен»", true);
            add("Выписать таймкод кадра", false);
            add("Открыть карточку и сверить студию", false);
            add("Собрать полку на семестр", false);
            add("Пересмотреть «Унесённых призраками»", false);
        }
        RecyclerView list = view.findViewById(R.id.taskList);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TaskAdapter();
        list.setAdapter(adapter);
        EditText edit = view.findViewById(R.id.editTask);
        Button add = view.findViewById(R.id.buttonAdd);
        add.setOnClickListener(v -> {
            String text = edit.getText().toString().trim();
            if (text.isEmpty()) {
                return;
            }
            add(text, false);
            edit.setText("");
            adapter.notifyItemInserted(titles.size() - 1);
        });
    }

    private void add(String title, boolean finished) {
        titles.add(title);
        done.add(finished);
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {
        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new Holder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.title.setText(titles.get(position));
            holder.done.setOnCheckedChangeListener(null);
            holder.done.setChecked(done.get(position));
            holder.done.setOnCheckedChangeListener((button, checked) -> {
                int index = holder.getBindingAdapterPosition();
                if (index != RecyclerView.NO_POSITION) {
                    done.set(index, checked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            final CheckBox done;
            final TextView title;

            Holder(View itemView) {
                super(itemView);
                done = itemView.findViewById(R.id.checkDone);
                title = itemView.findViewById(R.id.textTitle);
            }
        }
    }
}
