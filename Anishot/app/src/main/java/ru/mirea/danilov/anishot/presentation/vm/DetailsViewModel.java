package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.danilov.anishot.domain.GetAnimeDetailsUseCase;
import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.RateAnimeUseCase;
import ru.mirea.danilov.anishot.domain.RemoveFromMyListUseCase;
import ru.mirea.danilov.anishot.domain.SaveToMyListUseCase;
import ru.mirea.danilov.anishot.domain.UpdateListStatusUseCase;
import ru.mirea.danilov.anishot.domain.models.Anime;
import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class DetailsViewModel extends ViewModel {
    private static final String TAG = "DetailsViewModel";

    public static final class Save {
        public final boolean guest;
        public final boolean saved;
        public final String message;

        Save(boolean guest, boolean saved, String message) {
            this.guest = guest;
            this.saved = saved;
            this.message = message;
        }
    }

    public static final class EntryState {
        public final boolean guest;
        public final boolean inList;
        public final String status;
        public final int score;

        EntryState(boolean guest, boolean inList, String status, int score) {
            this.guest = guest;
            this.inList = inList;
            this.status = status;
            this.score = score;
        }
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final GetAnimeDetailsUseCase detailsUseCase;
    private final SaveToMyListUseCase saveUseCase;
    private final GetMyListUseCase listUseCase;
    private final RateAnimeUseCase rateUseCase;
    private final UpdateListStatusUseCase statusUseCase;
    private final RemoveFromMyListUseCase removeUseCase;
    private final AuthRepository authRepository;

    private final MutableLiveData<Anime> anime = new MutableLiveData<>();
    private final MutableLiveData<Save> save = new MutableLiveData<>();
    private final MutableLiveData<EntryState> entryState = new MutableLiveData<>();

    public DetailsViewModel(GetAnimeDetailsUseCase detailsUseCase,
                            SaveToMyListUseCase saveUseCase,
                            GetMyListUseCase listUseCase,
                            RateAnimeUseCase rateUseCase,
                            UpdateListStatusUseCase statusUseCase,
                            RemoveFromMyListUseCase removeUseCase,
                            AuthRepository authRepository) {
        this.detailsUseCase = detailsUseCase;
        this.saveUseCase = saveUseCase;
        this.listUseCase = listUseCase;
        this.rateUseCase = rateUseCase;
        this.statusUseCase = statusUseCase;
        this.removeUseCase = removeUseCase;
        this.authRepository = authRepository;
        Log.d(TAG, "DetailsViewModel created");
    }

    public LiveData<Anime> anime() {
        return anime;
    }

    public LiveData<Save> save() {
        return save;
    }

    public LiveData<EntryState> entryState() {
        return entryState;
    }

    public void open(int id) {
        executor.execute(() -> {
            anime.postValue(detailsUseCase.execute(id));
            postEntryState(id);
        });
    }

    public void saveCurrent() {
        Anime current = anime.getValue();
        if (current == null) {
            return;
        }
        boolean guest = authRepository.getProfile().isGuest();
        boolean saved = saveUseCase.execute(new ListEntry(
                current.getId(),
                current.getTitle(),
                "Смотрю",
                0,
                current.getImageUrl()
        ));
        save.setValue(new Save(guest, saved, guest ? "" : (saved ? "Теперь в вашем списке." : "Не удалось сохранить.")));
        postEntryState(current.getId());
    }

    public void setStatus(String status) {
        Anime current = anime.getValue();
        if (current == null) {
            return;
        }
        boolean ok = statusUseCase.execute(current.getId(), status);
        if (ok) {
            save.setValue(new Save(false, true, "Статус: " + status));
            postEntryState(current.getId());
        }
    }

    public void rate(int score) {
        Anime current = anime.getValue();
        if (current == null) {
            return;
        }
        boolean ok = rateUseCase.execute(current.getId(), score);
        if (ok) {
            save.setValue(new Save(false, true, "Оценка сохранена: " + score + "/10"));
            postEntryState(current.getId());
        }
    }

    public void remove() {
        Anime current = anime.getValue();
        if (current == null) {
            return;
        }
        boolean ok = removeUseCase.execute(current.getId());
        if (ok) {
            save.setValue(new Save(false, false, "Удалено из списка."));
            postEntryState(current.getId());
        }
    }

    private void postEntryState(int id) {
        boolean guest = authRepository.getProfile().isGuest();
        if (!guest) {
            for (ListEntry entry : listUseCase.execute()) {
                if (entry.getAnimeId() == id) {
                    entryState.postValue(new EntryState(false, true, entry.getStatus(), entry.getScore()));
                    return;
                }
            }
        }
        entryState.postValue(new EntryState(guest, false, "Смотрю", 0));
    }

    @Override
    protected void onCleared() {
        executor.shutdownNow();
        Log.d(TAG, "DetailsViewModel cleared");
        super.onCleared();
    }
}
