package ru.mirea.danilov.anishot.data.storage.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_scenes")
public class SavedSceneEntity {
    @PrimaryKey
    public int anilistId;
    @NonNull
    public String title = "";
    public int episode;
    public double at;
    public double similarity;
    @NonNull
    public String imageUrl = "";
    @NonNull
    public String videoUrl = "";

    public SavedSceneEntity(int anilistId, @NonNull String title, int episode, double at,
                            double similarity, @NonNull String imageUrl, @NonNull String videoUrl) {
        this.anilistId = anilistId;
        this.title = title;
        this.episode = episode;
        this.at = at;
        this.similarity = similarity;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }
}
