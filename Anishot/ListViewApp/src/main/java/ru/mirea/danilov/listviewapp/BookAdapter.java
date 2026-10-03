package ru.mirea.danilov.listviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private final List<Book> books;
    private final LayoutInflater inflater;

    public BookAdapter(LayoutInflater inflater, List<Book> books) {
        this.inflater = inflater;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_book, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        Book book = getItem(position);
        holder.title.setText(book.title);
        holder.author.setText(book.author);
        return convertView;
    }

    private static class Holder {
        final TextView title;
        final TextView author;

        Holder(View view) {
            title = view.findViewById(R.id.textTitle);
            author = view.findViewById(R.id.textAuthor);
        }
    }
}
