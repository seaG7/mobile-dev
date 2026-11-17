package ru.mirea.danilov.anishot.data.repository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mirea.danilov.anishot.data.network.AniListResponse;
import ru.mirea.danilov.anishot.data.network.AniListService;
import ru.mirea.danilov.anishot.data.network.KitsuPage;
import ru.mirea.danilov.anishot.data.network.KitsuService;
import ru.mirea.danilov.anishot.data.network.NetworkApi;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;

public class AnimeRepositoryImpl implements AnimeRepository {
    private static final String BASE_URL = "https://kitsu.io/api/edge/";
    private static final String ANILIST_URL = "https://graphql.anilist.co/";

    private final NetworkApi networkApi;
    private final KitsuService kitsu;
    private final AniListService aniList;
    private final Map<Integer, Anime> detailsCache = new ConcurrentHashMap<>();
    private List<Anime> remote;
    private boolean fromStub;

    public AnimeRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(12, TimeUnit.SECONDS)
                .build();
        kitsu = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KitsuService.class);
        aniList = new Retrofit.Builder()
                .baseUrl(ANILIST_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AniListService.class);
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
                    for (Anime item : loaded) {
                        detailsCache.put(item.getId(), item);
                    }
                    return remote;
                }
            }
        } catch (Exception ignored) {
        }
        fromStub = true;
        return readStub();
    }

    @Override
    public boolean catalogFromStub() {
        return fromStub;
    }

    @Override
    public Anime getDetails(int id) {
        Anime cached = detailsCache.get(id);
        if (cached != null) {
            return cached;
        }
        Anime found = find(getCatalog(), id);
        if (found != null) {
            detailsCache.put(id, found);
            return found;
        }
        Anime stub = find(readStub(), id);
        if (stub != null) {
            detailsCache.put(id, stub);
            return stub;
        }
        try {
            Response<AniListResponse> response = aniList.mediaById(new AniListService.Request(id)).execute();
            if (response.isSuccessful() && response.body() != null) {
                Anime resolved = response.body().toAnime();
                if (resolved != null) {
                    detailsCache.put(id, resolved);
                    return resolved;
                }
            }
        } catch (Exception ignored) {
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
