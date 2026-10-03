package ru.mirea.danilov.recyclerviewapp;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {
    public interface Listener {
        void onClick(Event event);
    }

    private final List<Event> items = new ArrayList<>();
    private final Listener listener;

    public EventAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<Event> next) {
        items.clear();
        if (next != null) {
            items.addAll(next);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Event event = items.get(position);
        holder.year.setText(String.valueOf(event.year));
        holder.title.setText(event.title);
        holder.body.setText(event.text);
        holder.still.setColorFilter(event.color, PorterDuff.Mode.SRC_IN);
        holder.itemView.setOnClickListener(v -> listener.onClick(event));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView still;
        final TextView year;
        final TextView title;
        final TextView body;

        Holder(View itemView) {
            super(itemView);
            still = itemView.findViewById(R.id.imageStill);
            year = itemView.findViewById(R.id.textYear);
            title = itemView.findViewById(R.id.textTitle);
            body = itemView.findViewById(R.id.textBody);
        }
    }
}
