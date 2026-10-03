package ru.mirea.danilov.anishot.domain.repository;

import java.util.List;

import ru.mirea.danilov.anishot.domain.models.Anime;

public interface AnimeRepository {
    List<Anime> getCatalog();

    boolean catalogFromStub();

    Anime getDetails(int id);
}
