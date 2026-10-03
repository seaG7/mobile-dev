package ru.mirea.danilov.anishot.presentation.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.danilov.anishot.domain.LoginUseCase;
import ru.mirea.danilov.anishot.domain.repository.AuthCallback;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";
    private final LoginUseCase loginUseCase;
    private final AuthRepository authRepository;
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Step> navigation = new MutableLiveData<>();

    public AuthViewModel(LoginUseCase loginUseCase, AuthRepository authRepository) {
        this.loginUseCase = loginUseCase;
        this.authRepository = authRepository;
        Log.d(TAG, "AuthViewModel created");
    }

    public LiveData<String> error() {
        return error;
    }

    public LiveData<Step> navigation() {
        return navigation;
    }

    public void login(String email, String password) {
        error.setValue("");
        loginUseCase.execute(email, password, new AuthCallback() {
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

    public void continueAsGuest() {
        authRepository.continueAsGuest();
        navigation.setValue(new Step(Step.Where.HOME));
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "AuthViewModel cleared");
        super.onCleared();
    }
}
