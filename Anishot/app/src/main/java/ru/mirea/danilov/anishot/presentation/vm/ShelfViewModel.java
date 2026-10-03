package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.danilov.anishot.domain.GetAnimeCatalogUseCase;
import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class ShelfViewModel extends ViewModel {
    private static final String TAG = "ShelfViewModel";

    public static final class Row {
        public final ListEntry entry;
        public final String poster;

        Row(ListEntry entry, String poster) {
            this.entry = entry;
            this.poster = poster;
        }
    }

    public static final class State {
        public final boolean guest;
        public final List<Row> rows;

        State(boolean guest, List<Row> rows) {
            this.guest = guest;
            this.rows = rows;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final GetMyListUseCase listUseCase;
    private final AuthRepository authRepository;
    private final MutableLiveData<List<Anime>> catalog = new MutableLiveData<>();
    private final MediatorLiveData<State> state = new MediatorLiveData<>();
    private List<Anime> latestCatalog = new ArrayList<>();

    public ShelfViewModel(GetAnimeCatalogUseCase catalogUseCase,
                          GetMyListUseCase listUseCase,
                          AuthRepository authRepository,
                          LiveData<Long> roomPulse) {
        this.listUseCase = listUseCase;
        this.authRepository = authRepository;
        Log.d(TAG, "ShelfViewModel created");
        state.addSource(catalog, items -> {
            latestCatalog = items == null ? new ArrayList<>() : items;
            publish();
        });
        state.addSource(roomPulse, tick -> publish());
        executor.execute(() -> {
            List<Anime> loaded = catalogUseCase.execute();
            catalog.postValue(loaded == null ? new ArrayList<>() : loaded);
        });
    }

    public LiveData<State> state() {
        return state;
    }

    private void publish() {
        boolean guest = authRepository.getProfile().isGuest();
        List<ListEntry> entries = listUseCase.execute();
        Map<Integer, String> posters = new HashMap<>();
        for (Anime anime : latestCatalog) {
            posters.put(anime.getId(), anime.getImageUrl());
        }
        List<Row> rows = new ArrayList<>();
        for (ListEntry entry : entries) {
            rows.add(new Row(entry, posters.get(entry.getAnimeId())));
        }
        state.setValue(new State(guest, rows));
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        Log.d(TAG, "ShelfViewModel cleared");
        super.onCleared();
    }
}
