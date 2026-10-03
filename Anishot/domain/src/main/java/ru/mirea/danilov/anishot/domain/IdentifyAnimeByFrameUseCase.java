package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

/** Найти аниме по кадру */
public class IdentifyAnimeByFrameUseCase {
    private SceneRepository sceneRepository;

    public IdentifyAnimeByFrameUseCase(SceneRepository sceneRepository) {
        this.sceneRepository = sceneRepository;
    }

    public SceneMatch execute() {
        return sceneRepository.identifyByFrame();
    }
}
