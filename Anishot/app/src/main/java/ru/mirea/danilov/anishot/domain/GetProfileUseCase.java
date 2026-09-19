package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

/** Просмотреть профиль */
public class GetProfileUseCase {
    private AuthRepository authRepository;

    public GetProfileUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public User execute() {
        return authRepository.getProfile();
    }
}
