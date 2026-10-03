package ru.mirea.danilov.anishot.domain.repository;

public interface AuthCallback {
    void onSuccess();

    void onError(String message);
}
