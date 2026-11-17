package ru.mirea.danilov.anishot.data.storage.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SceneDao {
    @Query("SELECT * FROM saved_scenes")
    List<SavedSceneEntity> getAll();

    @Query("SELECT * FROM saved_scenes")
    LiveData<List<SavedSceneEntity>> observeAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsert(SavedSceneEntity entity);
}
