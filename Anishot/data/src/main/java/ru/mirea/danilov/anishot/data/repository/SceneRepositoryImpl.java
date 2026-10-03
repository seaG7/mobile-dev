package ru.mirea.danilov.anishot.data.repository;

import org.json.JSONObject;

import ru.mirea.danilov.anishot.data.network.NetworkApi;
import ru.mirea.danilov.anishot.domain.models.Frame;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

public class SceneRepositoryImpl implements SceneRepository {
    private final NetworkApi networkApi;

    public SceneRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public Frame selectFrameFromVideo() {
        return new Frame("video-frame");
    }

    @Override
    public SceneMatch identifyByFrame() {
        return parseScene();
    }

    @Override
    public SceneMatch getPreview() {
        return parseScene();
    }

    @Override
    public boolean saveScene(SceneMatch match) {
        return match != null;
    }

    private SceneMatch parseScene() {
        try {
            JSONObject object = new JSONObject(networkApi.getSceneJson());
            return new SceneMatch(
                    object.getInt("anilistId"),
                    object.getString("title"),
                    object.getInt("episode"),
                    object.getDouble("at"),
                    object.getDouble("similarity"),
                    object.optString("imageUrl", ""),
                    object.optString("videoUrl", "")
            );
        } catch (Exception exception) {
            return new SceneMatch(1, "Фрирен", 14, 724.0, 0.974,
                    "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg", "");
        }
    }
}
