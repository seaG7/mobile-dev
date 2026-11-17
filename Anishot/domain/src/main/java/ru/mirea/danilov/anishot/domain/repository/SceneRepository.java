package ru.mirea.danilov.anishot.domain.repository;

import java.util.List;

import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;

public interface SceneRepository {
    Frame selectFrameFromVideo();

    Frame selectFrameFromVideo(String videoUri);

    SceneMatch identifyByFrame();

    SceneMatch identifyByFrame(String imagePath);

    SceneMatch getPreview();

    boolean saveScene(SceneMatch match);

    List<SceneMatch> getSavedScenes();
}
