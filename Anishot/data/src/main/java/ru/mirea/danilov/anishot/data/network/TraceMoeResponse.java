package ru.mirea.danilov.anishot.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.mirea.danilov.anishot.domain.models.SceneMatch;

public class TraceMoeResponse {
    @SerializedName("result")
    List<Hit> result;

    public SceneMatch firstMatch() {
        if (result == null || result.isEmpty()) {
            return null;
        }
        Hit best = result.get(0);
        if (best == null) {
            return null;
        }
        int id = best.anilist != null ? best.anilist.id : 1;
        String title = best.anilist != null ? best.anilist.displayTitle() : "";
        if (title.isEmpty()) {
            title = best.filename == null ? "Неизвестное аниме" : best.filename;
        }
        int episode = best.episode != null && best.episode > 0 ? best.episode : 1;
        return new SceneMatch(
                id,
                title,
                episode,
                best.from,
                best.similarity,
                best.image == null ? "" : best.image,
                best.video == null ? "" : best.video
        );
    }

    static class Hit {
        @SerializedName("anilist")
        Anilist anilist;
        @SerializedName("filename")
        String filename;
        @SerializedName("episode")
        Integer episode;
        @SerializedName("from")
        double from;
        @SerializedName("similarity")
        double similarity;
        @SerializedName("image")
        String image;
        @SerializedName("video")
        String video;
    }

    static class Anilist {
        @SerializedName("id")
        int id;
        @SerializedName("title")
        Title title;

        String displayTitle() {
            if (title == null) {
                return "";
            }
            if (title.english != null && !title.english.isEmpty()) {
                return title.english;
            }
            if (title.romaji != null && !title.romaji.isEmpty()) {
                return title.romaji;
            }
            return title.nativeTitle == null ? "" : title.nativeTitle;
        }
    }

    static class Title {
        @SerializedName("english")
        String english;
        @SerializedName("romaji")
        String romaji;
        @SerializedName("native")
        String nativeTitle;
    }
}
