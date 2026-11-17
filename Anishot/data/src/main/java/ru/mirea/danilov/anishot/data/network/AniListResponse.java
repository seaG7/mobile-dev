package ru.mirea.danilov.anishot.data.network;

import com.google.gson.annotations.SerializedName;

import ru.mirea.danilov.anishot.domain.models.Anime;

public class AniListResponse {
    @SerializedName("data")
    Data data;

    public Anime toAnime() {
        if (data == null || data.media == null) {
            return null;
        }
        Media m = data.media;
        String display = "";
        String original = "";
        String subtitle = "";
        if (m.title != null) {
            display = notEmpty(m.title.english, notEmpty(m.title.romaji, m.title.nativeTitle));
            original = m.title.nativeTitle == null ? "" : m.title.nativeTitle;
            if (m.title.romaji != null && !m.title.romaji.equals(display)) {
                subtitle = m.title.romaji;
            }
        }
        String poster = "";
        if (m.coverImage != null) {
            poster = notEmpty(m.coverImage.large, m.coverImage.medium);
        }
        StringBuilder meta = new StringBuilder();
        if (m.seasonYear != null) {
            meta.append(m.seasonYear);
        }
        if (m.format != null && !m.format.isEmpty()) {
            if (meta.length() > 0) meta.append(" · ");
            meta.append(m.format);
        }
        if (m.episodes != null) {
            if (meta.length() > 0) meta.append(" · ");
            meta.append(m.episodes).append(" серий");
        }
        String cleanDesc = m.description == null ? "" : m.description.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
        if (cleanDesc.length() > 360) {
            cleanDesc = cleanDesc.substring(0, 357).trim() + "…";
        }
        return new Anime(
                m.id,
                display,
                original,
                subtitle,
                poster,
                "#4A3A78",
                meta.toString(),
                cleanDesc,
                m.averageScore == null ? 0 : m.averageScore
        );
    }

    private static String notEmpty(String first, String fallback) {
        if (first != null && !first.isEmpty()) {
            return first;
        }
        return fallback == null ? "" : fallback;
    }

    static class Data {
        @SerializedName("Media")
        Media media;
    }

    static class Media {
        @SerializedName("id")
        int id;
        @SerializedName("title")
        Title title;
        @SerializedName("averageScore")
        Integer averageScore;
        @SerializedName("seasonYear")
        Integer seasonYear;
        @SerializedName("episodes")
        Integer episodes;
        @SerializedName("format")
        String format;
        @SerializedName("coverImage")
        Cover coverImage;
        @SerializedName("description")
        String description;
    }

    static class Title {
        @SerializedName("romaji")
        String romaji;
        @SerializedName("english")
        String english;
        @SerializedName("native")
        String nativeTitle;
    }

    static class Cover {
        @SerializedName("large")
        String large;
        @SerializedName("medium")
        String medium;
    }
}
