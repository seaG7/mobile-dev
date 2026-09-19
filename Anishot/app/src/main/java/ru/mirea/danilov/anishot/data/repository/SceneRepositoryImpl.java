package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

public class SceneRepositoryImpl implements SceneRepository {
    private final SceneMatch sample;

    public SceneRepositoryImpl(Context context) {
        sample = new SceneMatch(
                21416,
                "Gochuumon wa Usagi Desu ka??",
                1,
                279.83,
                0.992,
                "https://api.trace.moe/image/dummy.jpg",
                "https://api.trace.moe/video/dummy.mp4"
        );
    }

    @Override
    public Frame selectFrameFromVideo() {
        return new Frame("/storage/emulated/0/Download/frame.jpg");
    }

    @Override
    public SceneMatch identifyByFrame() {
        return sample;
    }

    @Override
    public SceneMatch getPreview() {
        return sample;
    }

    @Override
    public boolean saveScene(SceneMatch match) {
        return match != null;
    }
}
