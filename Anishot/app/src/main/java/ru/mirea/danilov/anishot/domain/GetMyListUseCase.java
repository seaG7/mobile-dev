package ru.mirea.danilov.anishot.domain;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.danilov.anishot.domain.models.ListEntry;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.domain.repository.AuthRepository;
import ru.mirea.danilov.anishot.domain.repository.ListRepository;

/** Просмотреть мой список */
public class GetMyListUseCase {
    private ListRepository listRepository;
    private AuthRepository authRepository;

    public GetMyListUseCase(ListRepository listRepository, AuthRepository authRepository) {
        this.listRepository = listRepository;
        this.authRepository = authRepository;
    }

    public List<ListEntry> execute() {
        User user = authRepository.getProfile();
        if (user.isGuest()) {
            return new ArrayList<>();
        }
        return listRepository.getMyList();
    }
}
