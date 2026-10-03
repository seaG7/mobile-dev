package ru.mirea.danilov.anishot.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.domain.models.Anime;

public class KitsuPage {
    private static final String[] COLORS = {
            "#4A3A78", "#3A2458", "#1E4A6E", "#8A4A18", "#5A2018", "#2E4A3A"
    };

    @SerializedName("data")
    List<Row> data;

    public List<Anime> toAnime() {
        List<Anime> catalog = new ArrayList<>();
        if (data == null) {
            return catalog;
        }
        int index = 0;
        for (Row row : data) {
            Anime anime = row == null ? null : row.toAnime(COLORS[index % COLORS.length]);
            if (anime != null) {
                catalog.add(anime);
                index++;
            }
        }
        return catalog;
    }

    static class Row {
        @SerializedName("id")
        String id;
        @SerializedName("attributes")
        Attr attributes;

        Anime toAnime(String color) {
            if (attributes == null || id == null) {
                return null;
            }
            int parsed;
            try {
                parsed = Integer.parseInt(id);
            } catch (NumberFormatException exception) {
                return null;
            }
            if (parsed >= 1 && parsed <= 5) {
                parsed += 1_000_000;
            }
            String title = attributes.displayTitle();
            String original = attributes.titles == null || attributes.titles.ja == null ? "" : attributes.titles.ja;
            String romaji = attributes.canonicalTitle == null ? "" : attributes.canonicalTitle;
            String subtitle = romaji.equals(title) ? "" : romaji;
            return new Anime(
                    parsed,
                    title,
                    original,
                    subtitle,
                    attributes.poster(),
                    color,
                    attributes.meta(),
                    attributes.lead(),
                    attributes.score()
            );
        }
    }

    static class Attr {
        @SerializedName("canonicalTitle")
        String canonicalTitle;
        @SerializedName("synopsis")
        String synopsis;
        @SerializedName("episodeCount")
        Integer episodeCount;
        @SerializedName("averageRating")
        String averageRating;
        @SerializedName("startDate")
        String startDate;
        @SerializedName("subtype")
        String subtype;
        @SerializedName("titles")
        Titles titles;
        @SerializedName("posterImage")
        Poster posterImage;

        String displayTitle() {
            if (titles != null && titles.en != null && !titles.en.isEmpty()) {
                return titles.en;
            }
            return canonicalTitle == null ? "" : canonicalTitle;
        }

        String poster() {
            if (posterImage == null) {
                return "";
            }
            if (posterImage.medium != null && !posterImage.medium.isEmpty()) {
                return posterImage.medium;
            }
            return posterImage.small == null ? "" : posterImage.small;
        }

        String meta() {
            String year = startDate != null && startDate.length() >= 4 ? startDate.substring(0, 4) : "";
            String kind = subtype == null ? "" : subtype;
            String episodes = episodeCount == null ? "" : episodeCount + " серий";
            StringBuilder line = new StringBuilder();
            append(line, year);
            append(line, kind);
            append(line, episodes);
            return line.toString();
        }

        String lead() {
            if (synopsis == null) {
                return "";
            }
            String text = synopsis.trim();
            int breakAt = text.indexOf("\n\n");
            if (breakAt > 0) {
                text = text.substring(0, breakAt).trim();
            }
            if (text.length() > 340) {
                int end = text.lastIndexOf(' ', 340);
                if (end < 180) {
                    end = 340;
                }
                text = text.substring(0, end).trim() + "…";
            }
            return text;
        }

        int score() {
            if (averageRating == null || averageRating.isEmpty()) {
                return 0;
            }
            try {
                return (int) Math.round(Double.parseDouble(averageRating));
            } catch (NumberFormatException exception) {
                return 0;
            }
        }

        private static void append(StringBuilder line, String part) {
            if (part == null || part.isEmpty()) {
                return;
            }
            if (line.length() > 0) {
                line.append(" · ");
            }
            line.append(part);
        }
    }

    static class Titles {
        @SerializedName("en")
        String en;
        @SerializedName("ja_jp")
        String ja;
    }

    static class Poster {
        @SerializedName("medium")
        String medium;
        @SerializedName("small")
        String small;
    }
}
