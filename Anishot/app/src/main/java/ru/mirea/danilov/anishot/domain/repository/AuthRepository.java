package ru.mirea.danilov.anishot.domain.repository;

import ru.mirea.danilov.anishot.domain.models.User;

public interface AuthRepository {
    boolean login(String login, String password);

    boolean register(String login, String password);

    User getProfile();

    void logout();
}
