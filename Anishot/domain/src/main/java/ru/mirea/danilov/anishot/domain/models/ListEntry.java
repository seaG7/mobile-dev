package ru.mirea.danilov.anishot.domain.models;

public class ListEntry {
    private int animeId;
    private String title;
    private String status;
    private int score;

    public ListEntry(int animeId, String title, String status, int score) {
        this.animeId = animeId;
        this.title = title;
        this.status = status;
        this.score = score;
    }

    public int getAnimeId() {
        return animeId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }
}
