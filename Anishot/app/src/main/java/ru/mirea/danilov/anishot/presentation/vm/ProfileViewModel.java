package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.GetProfileUseCase;
import ru.mirea.danilov.anishot.domain.LogoutUseCase;
import ru.mirea.danilov.anishot.domain.models.User;

public class ProfileViewModel extends ViewModel {
    private static final String TAG = "ProfileViewModel";

    public static final class State {
        public final User user;
        public final int saved;

        State(User user, int saved) {
            this.user = user;
            this.saved = saved;
        }
    }

    private final GetProfileUseCase profileUseCase;
    private final GetMyListUseCase listUseCase;
    private final LogoutUseCase logoutUseCase;
    private final MutableLiveData<State> state = new MutableLiveData<>();
    private final MutableLiveData<Step> navigation = new MutableLiveData<>();

    public ProfileViewModel(GetProfileUseCase profileUseCase,
                            GetMyListUseCase listUseCase,
                            LogoutUseCase logoutUseCase) {
        this.profileUseCase = profileUseCase;
        this.listUseCase = listUseCase;
        this.logoutUseCase = logoutUseCase;
        Log.d(TAG, "ProfileViewModel created");
        refresh();
    }

    public LiveData<State> state() {
        return state;
    }

    public LiveData<Step> navigation() {
        return navigation;
    }

    public void logout() {
        logoutUseCase.execute();
        navigation.setValue(new Step(Step.Where.AUTH));
    }

    public void refresh() {
        User user = profileUseCase.execute();
        state.setValue(new State(user, listUseCase.execute().size()));
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "ProfileViewModel cleared");
        super.onCleared();
    }
}
