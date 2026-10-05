package ru.mirea.danilov.Lesson9.data.storage;

import ru.mirea.danilov.Lesson9.data.storage.models.Movie;

public interface MovieStorage {
    Movie get();

    boolean save(Movie movie);
}
