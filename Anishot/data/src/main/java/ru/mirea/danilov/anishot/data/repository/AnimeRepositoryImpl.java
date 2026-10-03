package ru.mirea.danilov.anishot.data.repository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.data.network.NetworkApi;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;

public class AnimeRepositoryImpl implements AnimeRepository {
    private final NetworkApi networkApi;

    public AnimeRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Anime> getCatalog() {
        List<Anime> catalog = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(networkApi.getCatalogJson());
            for (int i = 0; i < array.length(); i++) {
                catalog.add(mapToDomain(array.getJSONObject(i)));
            }
        } catch (Exception ignored) {
        }
        return catalog;
    }

    @Override
    public Anime getDetails(int id) {
        for (Anime anime : getCatalog()) {
            if (anime.getId() == id) {
                return anime;
            }
        }
        List<Anime> catalog = getCatalog();
        return catalog.isEmpty() ? null : catalog.get(0);
    }

    private Anime mapToDomain(JSONObject object) throws Exception {
        return new Anime(
                object.getInt("id"),
                object.getString("title"),
                object.optString("original", ""),
                object.optString("subtitle", ""),
                object.optString("image", ""),
                object.optString("color", "#333333"),
                object.optString("meta", ""),
                object.optString("description", ""),
                object.optInt("score", 0)
        );
    }
}
