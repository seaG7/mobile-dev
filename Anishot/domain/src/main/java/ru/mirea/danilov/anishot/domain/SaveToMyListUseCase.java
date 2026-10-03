package ru.mirea.danilov.anishot.domain;

import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

/** Сохранить в мой список */
public class SaveToMyListUseCase {
    private ListRepository listRepository;
    private AuthRepository authRepository;

    public SaveToMyListUseCase(ListRepository listRepository, AuthRepository authRepository) {
        this.listRepository = listRepository;
        this.authRepository = authRepository;
    }

    public boolean execute(ListEntry entry) {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return false;
        }
        if (entry.getTitle().isEmpty()) {
            return false;
        }
        return listRepository.saveToList(entry);
    }
}
