package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.danilov.anishot.domain.BuildFrameMaskUseCase;
import ru.mirea.danilov.anishot.domain.IdentifyAnimeByFrameUseCase;
import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.models.SceneMatch;

public class SearchViewModel extends ViewModel {
    private static final String TAG = "SearchViewModel";

    public static final class Hit {
        public final SceneMatch match;
        public final FrameMask mask;

        Hit(SceneMatch match, FrameMask mask) {
            this.match = match;
            this.mask = mask;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final IdentifyAnimeByFrameUseCase identifyUseCase;
    private final BuildFrameMaskUseCase maskUseCase;
    private final MutableLiveData<Hit> hit = new MutableLiveData<>();

    public SearchViewModel(IdentifyAnimeByFrameUseCase identifyUseCase, BuildFrameMaskUseCase maskUseCase) {
        this.identifyUseCase = identifyUseCase;
        this.maskUseCase = maskUseCase;
        Log.d(TAG, "SearchViewModel created");
    }

    public LiveData<Hit> hit() {
        return hit;
    }

    public void find() {
        executor.execute(() -> hit.postValue(new Hit(identifyUseCase.execute(), maskUseCase.execute())));
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        Log.d(TAG, "SearchViewModel cleared");
        super.onCleared();
    }
}
