package ru.mirea.danilov.anishot.data.storage.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "list_entries")
public class ListEntryEntity {
    @PrimaryKey
    public int animeId;
    @NonNull
    public String title = "";
    @NonNull
    public String status = "Смотрю";
    public int score;
    @NonNull
    public String posterUrl = "";

    public ListEntryEntity(int animeId, @NonNull String title, @NonNull String status, int score, @NonNull String posterUrl) {
        this.animeId = animeId;
        this.title = title;
        this.status = status;
        this.score = score;
        this.posterUrl = posterUrl;
    }
}
