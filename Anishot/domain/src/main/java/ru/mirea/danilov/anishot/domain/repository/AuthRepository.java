package ru.mirea.danilov.anishot.domain.repository;

import ru.mirea.danilov.anishot.domain.models.User;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);

    void register(String email, String password, AuthCallback callback);

    User getProfile();

    void logout();

    void continueAsGuest();
}
