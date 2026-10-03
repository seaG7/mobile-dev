package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.danilov.anishot.domain.GetAnimeDetailsUseCase;
import ru.mirea.danilov.anishot.domain.SaveToMyListUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";

    public static final class Save {
        public final boolean guest;
        public final boolean saved;

        Save(boolean guest, boolean saved) {
            this.guest = guest;
            this.saved = saved;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final GetAnimeDetailsUseCase detailsUseCase;
    private final SaveToMyListUseCase saveUseCase;
    private final AuthRepository authRepository;
    private final MutableLiveData<Anime> anime = new MutableLiveData<>();
    private final MutableLiveData<Save> save = new MutableLiveData<>();

    public DetailsViewModel(GetAnimeDetailsUseCase detailsUseCase,
                            SaveToMyListUseCase saveUseCase,
                            AuthRepository authRepository) {
        this.detailsUseCase = detailsUseCase;
        this.saveUseCase = saveUseCase;
        this.authRepository = authRepository;
        Log.d(TAG, "DetailsViewModel created");
    }

    public LiveData<Anime> anime() {
        return anime;
    }

    public LiveData<Save> save() {
        return save;
    }

    public void open(int id) {
        executor.execute(() -> anime.postValue(detailsUseCase.execute(id)));
    }

    public void saveCurrent() {
        Anime current = anime.getValue();
        if (current == null) {
            return;
        }
        boolean guest = authRepository.getProfile().isGuest();
        boolean saved = saveUseCase.execute(new ListEntry(current.getId(), current.getTitle(), "watching", 0));
        save.setValue(new Save(guest, saved));
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        Log.d(TAG, "DetailsViewModel cleared");
        super.onCleared();
    }
}
