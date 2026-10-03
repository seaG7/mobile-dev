package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.repository.AuthCallback;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class RegisterUseCase {
    private AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, String repeat, AuthCallback callback) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            callback.onError("Укажите почту");
            return;
        }
        if (password == null || password.length() < 6) {
            callback.onError("Пароль от 6 символов");
            return;
        }
        if (!password.equals(repeat)) {
            callback.onError("Пароли не совпадают");
            return;
        }
        authRepository.register(email, password, callback);
    }
}
