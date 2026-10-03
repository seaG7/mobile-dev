package ru.mirea.danilov.anishot.domain.repository;

import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;

public interface SceneRepository {
    Frame selectFrameFromVideo();

    SceneMatch identifyByFrame();

    SceneMatch getPreview();

    boolean saveScene(SceneMatch match);
}
