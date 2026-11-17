package ru.mirea.danilov.anishot.domain.repository;

import java.util.List;

import ru.mirea.danilov.anishot.domain.models.ListEntry;

public interface ListRepository {
    boolean saveToList(ListEntry entry);

    boolean rate(int animeId, int score);

    boolean updateStatus(int animeId, String status);

    boolean removeFromList(int animeId);

    List<ListEntry> getMyList();
}
