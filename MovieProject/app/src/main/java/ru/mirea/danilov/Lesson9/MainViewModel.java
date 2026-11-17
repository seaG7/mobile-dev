package ru.mirea.danilov.Lesson9;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.danilov.Lesson9.domain.models.Movie;
import ru.mirea.danilov.Lesson9.domain.repository.MovieRepository;
import ru.mirea.danilov.Lesson9.domain.usecases.GetFavoriteFilmUseCase;
import ru.mirea.danilov.Lesson9.domain.usecases.SaveMovieToFavoriteUseCase;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";

    private final SaveMovieToFavoriteUseCase saveMovie;
    private final GetFavoriteFilmUseCase readMovie;
    private final MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        saveMovie = new SaveMovieToFavoriteUseCase(movieRepository);
        readMovie = new GetFavoriteFilmUseCase(movieRepository);
        Log.d(TAG, "MainViewModel created");
    }

    public LiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setText(Movie movie) {
        boolean saved = saveMovie.execute(movie);
        favoriteMovie.setValue(Boolean.toString(saved));
    }

    public void getText() {
        Movie movie = readMovie.execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s", movie.getName()));
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "MainViewModel cleared");
        super.onCleared();
    }
}
