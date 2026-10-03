package ru.mirea.danilov.anishot;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.FirebaseApp;

import ru.mirea.danilov.anishot.data.firebase.FirebaseAuthDataSource;
import ru.mirea.danilov.anishot.data.network.NetworkApi;
import ru.mirea.danilov.anishot.data.repository.AnimeRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.AuthRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.FrameMaskRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.ListRepositoryImpl;
import ru.mirea.danilov.anishot.data.repository.SceneRepositoryImpl;
import ru.mirea.danilov.anishot.data.storage.client.sharedprefs.SharedPrefClientStorage;
import ru.mirea.danilov.anishot.domain.repository.AnimeRepository;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.FrameMaskRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;
import ru.mirea.danilov.anishot.domain.repository.SceneRepository;

public class AnishotApp extends Application {
    private AuthRepository authRepository;
    private AnimeRepository animeRepository;
    private SceneRepository sceneRepository;
    private ListRepository listRepository;
    private ListRepositoryImpl listStore;
    private FrameMaskRepository frameMaskRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception ignored) {
        }
        authRepository = new AuthRepositoryImpl(
                new FirebaseAuthDataSource(),
                new SharedPrefClientStorage(this)
        );
        NetworkApi networkApi = new NetworkApi();
        animeRepository = new AnimeRepositoryImpl(networkApi);
        sceneRepository = new SceneRepositoryImpl(networkApi);
        listStore = new ListRepositoryImpl(this);
        listRepository = listStore;
        frameMaskRepository = new FrameMaskRepositoryImpl();
    }

    public AuthRepository authRepository() {
        return authRepository;
    }

    public AnimeRepository animeRepository() {
        return animeRepository;
    }

    public SceneRepository sceneRepository() {
        return sceneRepository;
    }

    public ListRepository listRepository() {
        return listRepository;
    }

    public FrameMaskRepository frameMaskRepository() {
        return frameMaskRepository;
    }

    public LiveData<Long> listPulse() {
        return listStore.pulse();
    }
}
