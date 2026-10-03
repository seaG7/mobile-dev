package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.danilov.anishot.domain.RegisterUseCase;
import ru.mirea.danilov.anishot.domain.repository.AuthCallback;

public class RegisterViewModel extends ViewModel {
    private static final String TAG = "RegisterViewModel";
    private final RegisterUseCase registerUseCase;
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Step> navigation = new MutableLiveData<>();

    public RegisterViewModel(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
        Log.d(TAG, "RegisterViewModel created");
    }

    public LiveData<String> error() {
        return error;
    }

    public LiveData<Step> navigation() {
        return navigation;
    }

    public void register(String email, String password, String repeat) {
        error.setValue("");
        registerUseCase.execute(email, password, repeat, new AuthCallback() {
            @Override
            public void onSuccess() {
                navigation.setValue(new Step(Step.Where.HOME));
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
            }
        });
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "RegisterViewModel cleared");
        super.onCleared();
    }
}
