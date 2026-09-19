package ru.mirea.danilov.anishot.domain.models;

public class Anime {
    private int id;
    private String title;
    private String imageUrl;
    private int averageScore;

    public Anime(int id, String title, String imageUrl, int averageScore) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.averageScore = averageScore;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getAverageScore() {
        return averageScore;
    }
}
