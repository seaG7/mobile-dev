package ru.mirea.danilov.Lesson9;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.danilov.Lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.danilov.Lesson9.data.storage.MovieStorage;
import ru.mirea.danilov.Lesson9.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.danilov.Lesson9.domain.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context appContext;

    public ViewModelFactory(Context context) {
        appContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == MainViewModel.class) {
            MovieStorage storage = new SharedPrefMovieStorage(appContext);
            MovieRepository repository = new MovieRepositoryImpl(storage);
            return (T) new MainViewModel(repository);
        }
        throw new IllegalArgumentException(modelClass.getName());
    }
}
