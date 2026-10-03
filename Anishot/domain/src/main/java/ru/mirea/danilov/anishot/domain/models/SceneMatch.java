package ru.mirea.danilov.anishot.domain.models;

public class SceneMatch {
    private int anilistId;
    private String title;
    private int episode;
    private double at;
    private double similarity;
    private String imageUrl;
    private String videoUrl;

    public SceneMatch(int anilistId, String title, int episode, double at,
                      double similarity, String imageUrl, String videoUrl) {
        this.anilistId = anilistId;
        this.title = title;
        this.episode = episode;
        this.at = at;
        this.similarity = similarity;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
    }

    public int getAnilistId() {
        return anilistId;
    }

    public String getTitle() {
        return title;
    }

    public int getEpisode() {
        return episode;
    }

    public double getAt() {
        return at;
    }

    public double getSimilarity() {
        return similarity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
