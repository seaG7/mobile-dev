package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;

public class AnimeRepositoryImpl implements AnimeRepository {
    private final List<Anime> catalog;

    public AnimeRepositoryImpl(Context context) {
        catalog = new ArrayList<>();
        catalog.add(new Anime(
                21416,
                "Gochuumon wa Usagi Desu ka??",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/b21416-Uz6b7CKnKz9K.jpg",
                77
        ));
        catalog.add(new Anime(
                1,
                "Cowboy Bebop",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx1-CXtrrkMpJ8Zq.png",
                86
        ));
        catalog.add(new Anime(
                16498,
                "Shingeki no Kyojin",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx16498-73IhOXpJZiMF.jpg",
                85
        ));
        catalog.add(new Anime(
                21,
                "One Piece",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/nx21-tXMN3Y20AIKV.jpg",
                82
        ));
        catalog.add(new Anime(
                1535,
                "Death Note",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/medium/bx1535-lawCwhzhihED.jpg",
                84
        ));
    }

    @Override
    public List<Anime> getCatalog() {
        return catalog;
    }

    @Override
    public Anime getDetails(int id) {
        for (Anime anime : catalog) {
            if (anime.getId() == id) {
                return anime;
            }
        }
        return catalog.get(0);
    }
}
