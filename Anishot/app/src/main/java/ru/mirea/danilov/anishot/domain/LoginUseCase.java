package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

/** Войти */
public class LoginUseCase {
    private AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean execute(String login, String password) {
        if (login == null || login.isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        return authRepository.login(login, password);
    }
}
