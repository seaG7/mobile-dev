package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {
    private User currentUser;

    public AuthRepositoryImpl(Context context) {
        this.currentUser = new User(0, "guest", true);
    }

    @Override
    public boolean login(String login, String password) {
        currentUser = new User(1, login, false);
        return true;
    }

    @Override
    public boolean register(String login, String password) {
        currentUser = new User(2, login, false);
        return true;
    }

    @Override
    public User getProfile() {
        return currentUser;
    }

    @Override
    public void logout() {
        currentUser = new User(0, "guest", true);
    }
}
