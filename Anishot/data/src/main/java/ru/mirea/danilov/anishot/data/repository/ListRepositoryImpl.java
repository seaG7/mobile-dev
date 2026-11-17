package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.data.storage.room.AppDatabase;
import ru.mirea.danilov.anishot.data.storage.room.ListDao;
import ru.mirea.danilov.anishot.data.storage.room.ListEntryEntity;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

public class ListRepositoryImpl implements ListRepository {
    private final ListDao listDao;
    private final MediatorLiveData<Long> pulse = new MediatorLiveData<>();

    public ListRepositoryImpl(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.listDao = db.listDao();
        pulse.addSource(listDao.observeAll(), rows -> pulse.setValue(System.currentTimeMillis()));
        pulse.addSource(db.sceneDao().observeAll(), rows -> pulse.setValue(System.currentTimeMillis()));
    }

    public LiveData<Long> pulse() {
        return pulse;
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
    public boolean updateStatus(int animeId, String status) {
        return listDao.updateStatus(animeId, status) > 0;
    }

    @Override
    public boolean removeFromList(int animeId) {
        return listDao.deleteById(animeId) > 0;
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
        String poster = entry.getPosterUrl() == null ? "" : entry.getPosterUrl();
        return new ListEntryEntity(entry.getAnimeId(), entry.getTitle(), entry.getStatus(), entry.getScore(), poster);
    }

    private ListEntry mapToDomain(ListEntryEntity entity) {
        return new ListEntry(entity.animeId, entity.title, entity.status, entity.score, entity.posterUrl);
    }
}
