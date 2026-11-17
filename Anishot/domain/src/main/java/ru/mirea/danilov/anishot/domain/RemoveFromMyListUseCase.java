package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

/** Удалить тайтл из списка */
public class RemoveFromMyListUseCase {
    private final ListRepository listRepository;
    private final AuthRepository authRepository;

    public RemoveFromMyListUseCase(ListRepository listRepository, AuthRepository authRepository) {
        this.listRepository = listRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(int animeId) {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return false;
        }
        return listRepository.removeFromList(animeId);
    }
}
