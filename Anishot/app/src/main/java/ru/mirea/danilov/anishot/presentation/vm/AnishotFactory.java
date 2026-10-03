package ru.mirea.danilov.anishot.presentation.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.domain.BuildFrameMaskUseCase;
import ru.mirea.danilov.anishot.domain.GetAnimeCatalogUseCase;
import ru.mirea.danilov.anishot.domain.GetAnimeDetailsUseCase;
import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.GetProfileUseCase;
import ru.mirea.danilov.anishot.domain.IdentifyAnimeByFrameUseCase;
import ru.mirea.danilov.anishot.domain.LoginUseCase;
import ru.mirea.danilov.anishot.domain.LogoutUseCase;
import ru.mirea.danilov.anishot.domain.RegisterUseCase;
import ru.mirea.danilov.anishot.domain.SaveToMyListUseCase;

public class AnishotFactory implements ViewModelProvider.Factory {
    private final AnishotApp app;

    public AnishotFactory(AnishotApp app) {
        this.app = app;
    }

    public static AnishotFactory from(Fragment fragment) {
        Application application = fragment.requireActivity().getApplication();
        return new AnishotFactory((AnishotApp) application);
    }

    public static AnishotFactory from(Application application) {
        return new AnishotFactory((AnishotApp) application);
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == AuthViewModel.class) {
            return (T) new AuthViewModel(
                    new LoginUseCase(app.authRepository()),
                    app.authRepository()
            );
        }
        if (modelClass == RegisterViewModel.class) {
            return (T) new RegisterViewModel(new RegisterUseCase(app.authRepository()));
        }
        if (modelClass == CatalogViewModel.class) {
            return (T) new CatalogViewModel(new GetAnimeCatalogUseCase(app.animeRepository()));
        }
        if (modelClass == ShelfViewModel.class) {
            return (T) new ShelfViewModel(
                    new GetAnimeCatalogUseCase(app.animeRepository()),
                    new GetMyListUseCase(app.listRepository(), app.authRepository()),
                    app.authRepository(),
                    app.listPulse()
            );
        }
        if (modelClass == SearchViewModel.class) {
            return (T) new SearchViewModel(
                    new IdentifyAnimeByFrameUseCase(app.sceneRepository()),
                    new BuildFrameMaskUseCase(app.frameMaskRepository())
            );
        }
        if (modelClass == DetailsViewModel.class) {
            return (T) new DetailsViewModel(
                    new GetAnimeDetailsUseCase(app.animeRepository()),
                    new SaveToMyListUseCase(app.listRepository(), app.authRepository()),
                    app.authRepository()
            );
        }
        if (modelClass == ProfileViewModel.class) {
            return (T) new ProfileViewModel(
                    new GetProfileUseCase(app.authRepository()),
                    new GetMyListUseCase(app.listRepository(), app.authRepository()),
                    new LogoutUseCase(app.authRepository())
            );
        }
        throw new IllegalArgumentException(modelClass.getName());
    }
}
