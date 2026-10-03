package ru.mirea.danilov.anishot.domain.models;

public class Anime {
    private int id;
    private String title;
    private String originalTitle;
    private String subtitle;
    private String imageUrl;
    private String posterColor;
    private String meta;
    private String description;
    private int averageScore;

    public Anime(int id, String title, String originalTitle, String subtitle,
                 String imageUrl, String posterColor, String meta, String description,
                 int averageScore) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.posterColor = posterColor;
        this.meta = meta;
        this.description = description;
        this.averageScore = averageScore;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPosterColor() {
        return posterColor;
    }

    public String getMeta() {
        return meta;
    }

    public String getDescription() {
        return description;
    }

    public int getAverageScore() {
        return averageScore;
    }
}
