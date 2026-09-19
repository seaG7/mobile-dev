package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

public class ListRepositoryImpl implements ListRepository {
    private final List<ListEntry> entries;

    public ListRepositoryImpl(Context context) {
        entries = new ArrayList<>();
        entries.add(new ListEntry(21416, "Gochuumon wa Usagi Desu ka??", "watching", 9));
        entries.add(new ListEntry(1, "Cowboy Bebop", "completed", 10));
    }

    @Override
    public boolean saveToList(ListEntry entry) {
        entries.add(entry);
        return true;
    }

    @Override
    public boolean rate(int animeId, int score) {
        for (int i = 0; i < entries.size(); i++) {
            ListEntry old = entries.get(i);
            if (old.getAnimeId() == animeId) {
                entries.set(i, new ListEntry(old.getAnimeId(), old.getTitle(), old.getStatus(), score));
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ListEntry> getMyList() {
        return entries;
    }
}
