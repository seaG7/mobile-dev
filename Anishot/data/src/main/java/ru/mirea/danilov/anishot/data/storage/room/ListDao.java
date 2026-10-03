package ru.mirea.danilov.anishot.data.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListDao {
    @Query("SELECT * FROM list_entries")
    List<ListEntryEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(ListEntryEntity entity);

    @Query("UPDATE list_entries SET score = :score WHERE animeId = :animeId")
    int updateScore(int animeId, int score);
}
