package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.data.storage.room.AppDatabase;
import ru.mirea.danilov.anishot.data.storage.room.ListDao;
import ru.mirea.danilov.anishot.data.storage.room.ListEntryEntity;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

public class ListRepositoryImpl implements ListRepository {
    private final ListDao listDao;

    public ListRepositoryImpl(Context context) {
        this.listDao = AppDatabase.getInstance(context).listDao();
    }

    @Override
    public boolean saveToList(ListEntry entry) {
        listDao.upsert(mapToStorage(entry));
        return true;
    }

    @Override
    public boolean rate(int animeId, int score) {
        return listDao.updateScore(animeId, score) > 0;
    }

    @Override
    public List<ListEntry> getMyList() {
        List<ListEntry> result = new ArrayList<>();
        for (ListEntryEntity entity : listDao.getAll()) {
            result.add(mapToDomain(entity));
        }
        return result;
    }

    private ListEntryEntity mapToStorage(ListEntry entry) {
        return new ListEntryEntity(entry.getAnimeId(), entry.getTitle(), entry.getStatus(), entry.getScore());
    }

    private ListEntry mapToDomain(ListEntryEntity entity) {
        return new ListEntry(entity.animeId, entity.title, entity.status, entity.score);
    }
}
