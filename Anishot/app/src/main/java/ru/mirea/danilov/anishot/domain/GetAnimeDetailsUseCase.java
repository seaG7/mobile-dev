package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;

/** Открыть карточку аниме */
public class GetAnimeDetailsUseCase {
    private AnimeRepository animeRepository;

    public GetAnimeDetailsUseCase(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public Anime execute(int id) {
        return animeRepository.getDetails(id);
    }
}
