package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

/** Выбрать кадр из видео */
public class SelectFrameFromVideoUseCase {
    private SceneRepository sceneRepository;

    public SelectFrameFromVideoUseCase(SceneRepository sceneRepository) {
        this.sceneRepository = sceneRepository;
    }

    public Frame execute() {
        return sceneRepository.selectFrameFromVideo();
    }
}
