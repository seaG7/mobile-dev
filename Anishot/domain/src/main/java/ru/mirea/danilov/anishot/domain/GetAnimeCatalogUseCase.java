package ru.mirea.danilov.anishot.domain;

import java.util.List;

import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;

/** Просмотреть каталог аниме */
public class GetAnimeCatalogUseCase {
    private AnimeRepository animeRepository;

    public GetAnimeCatalogUseCase(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public List<Anime> execute() {
        return animeRepository.getCatalog();
    }
}
