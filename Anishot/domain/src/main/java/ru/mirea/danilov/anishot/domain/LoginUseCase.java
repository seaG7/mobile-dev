package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.repository.AuthCallback;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class LoginUseCase {
    private AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthCallback callback) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            callback.onError("Укажите почту");
            return;
        }
        if (password == null || password.length() < 6) {
            callback.onError("Пароль от 6 символов");
            return;
        }
        authRepository.login(email, password, callback);
    }
}
