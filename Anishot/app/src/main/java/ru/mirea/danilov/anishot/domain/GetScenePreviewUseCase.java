package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

/** Показать превью кадра и клипа */
public class GetScenePreviewUseCase {
    private SceneRepository sceneRepository;

    public GetScenePreviewUseCase(SceneRepository sceneRepository) {
        this.sceneRepository = sceneRepository;
    }

    public SceneMatch execute() {
        return sceneRepository.getPreview();
    }
}
