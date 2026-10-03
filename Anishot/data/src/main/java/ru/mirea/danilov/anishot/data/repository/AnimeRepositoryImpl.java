package ru.mirea.danilov.anishot.data.repository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.danilov.anishot.data.network.KitsuPage;
import ru.mirea.danilov.anishot.data.network.KitsuService;
import ru.mirea.danilov.anishot.data.network.NetworkApi;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;

public class AnimeRepositoryImpl implements AnimeRepository {
    private static final String BASE_URL = "https://kitsu.io/api/edge/";

    private final NetworkApi networkApi;
    private final KitsuService kitsu;
    private List<Anime> remote;
    private boolean fromStub;

    public AnimeRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        kitsu = retrofit.create(KitsuService.class);
    }

    @Override
    public synchronized List<Anime> getCatalog() {
        if (remote != null) {
            return remote;
        }
        try {
            Response<KitsuPage> response = kitsu.popular(12, "-userCount").execute();
            if (response.isSuccessful() && response.body() != null) {
                List<Anime> loaded = response.body().toAnime();
                if (!loaded.isEmpty()) {
                    fromStub = false;
                    remote = loaded;
                    return remote;
                }
            }
        } catch (Exception ignored) {
        }
        fromStub = true;
        remote = readStub();
        return remote;
    }

    @Override
    public boolean catalogFromStub() {
        return fromStub;
    }

    @Override
    public Anime getDetails(int id) {
        Anime found = find(getCatalog(), id);
        if (found != null) {
            return found;
        }
        Anime stub = find(readStub(), id);
        if (stub != null) {
            return stub;
        }
        List<Anime> catalog = getCatalog();
        return catalog.isEmpty() ? null : catalog.get(0);
    }

    private static Anime find(List<Anime> catalog, int id) {
        for (Anime anime : catalog) {
            if (anime.getId() == id) {
                return anime;
            }
        }
        return null;
    }

    private List<Anime> readStub() {
        List<Anime> catalog = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(networkApi.getCatalogJson());
            for (int i = 0; i < array.length(); i++) {
                catalog.add(mapStub(array.getJSONObject(i)));
            }
        } catch (Exception ignored) {
        }
        return catalog;
    }

    private Anime mapStub(JSONObject object) throws Exception {
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
