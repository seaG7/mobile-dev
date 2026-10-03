package ru.mirea.danilov.retrofitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.Holder> {
    private static final String[] STILLS = {
            "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg",
            "https://cdn.myanimelist.net/images/anime/1171/109222l.jpg",
            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx126403-BfVSRzWUtVFW.png",
            "https://cdn.myanimelist.net/images/anime/1806/126216l.jpg",
            "https://cdn.myanimelist.net/images/anime/10/47347l.jpg"
    };

    private final List<Todo> items = new ArrayList<>();
    private final ApiService api;

    public TodoAdapter(ApiService api) {
        this.api = api;
    }

    public void setItems(List<Todo> next) {
        items.clear();
        if (next != null) {
            items.addAll(next);
        }
        notifyDataSetChanged();
    }

    static String stillFor(int id) {
        int index = Math.floorMod(id, STILLS.length);
        return STILLS[index];
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Todo todo = items.get(position);
        holder.title.setText(todo.getTitle());
        holder.done.setOnCheckedChangeListener(null);
        holder.done.setChecked(todo.isCompleted());
        Picasso.get()
                .load(stillFor(todo.getId()))
                .resize(240, 240)
                .centerCrop()
                .placeholder(R.drawable.frame_still)
                .error(R.drawable.frame_still)
                .into(holder.still);
        bindCheck(holder, todo);
    }

    private void bindCheck(Holder holder, Todo todo) {
        holder.done.setOnCheckedChangeListener(null);
        holder.done.setChecked(todo.isCompleted());
        holder.done.setOnCheckedChangeListener((button, checked) -> {
            if (!button.isPressed()) {
                return;
            }
            boolean previous = todo.isCompleted();
            todo.setCompleted(checked);
            api.updateTodo(todo.getId(), todo).enqueue(new Callback<Todo>() {
                @Override
                public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                    if (!response.isSuccessful()) {
                        todo.setCompleted(previous);
                        bindCheck(holder, todo);
                        Toast.makeText(holder.itemView.getContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                    todo.setCompleted(previous);
                    bindCheck(holder, todo);
                    Toast.makeText(holder.itemView.getContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView still;
        final TextView title;
        final CheckBox done;

        Holder(View itemView) {
            super(itemView);
            still = itemView.findViewById(R.id.imageStill);
            title = itemView.findViewById(R.id.textTitle);
            done = itemView.findViewById(R.id.checkDone);
        }
    }
}
