package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.danilov.anishot.domain.GetAnimeCatalogUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;

public class CatalogViewModel extends ViewModel {
    private static final String TAG = "CatalogViewModel";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final GetAnimeCatalogUseCase catalogUseCase;
    private final MutableLiveData<List<Anime>> catalog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> fallback = new MutableLiveData<>(false);

    public CatalogViewModel(GetAnimeCatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
        Log.d(TAG, "CatalogViewModel created");
        executor.execute(() -> {
            List<Anime> loaded = catalogUseCase.execute();
            catalog.postValue(loaded == null ? new ArrayList<>() : loaded);
            fallback.postValue(catalogUseCase.fellBack());
        });
    }

    public LiveData<List<Anime>> catalog() {
        return catalog;
    }

    public LiveData<Boolean> fallback() {
        return fallback;
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        Log.d(TAG, "CatalogViewModel cleared");
        super.onCleared();
    }
}
