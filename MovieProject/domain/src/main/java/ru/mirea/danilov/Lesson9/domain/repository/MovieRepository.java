package ru.mirea.danilov.Lesson9.domain.repository;

import ru.mirea.danilov.Lesson9.domain.models.Movie;

public interface MovieRepository {
    boolean saveMovie(Movie movie);

    Movie getMovie();
}
