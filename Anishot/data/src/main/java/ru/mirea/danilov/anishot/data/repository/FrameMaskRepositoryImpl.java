package ru.mirea.danilov.anishot.data.repository;

import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.repository.FrameMaskRepository;

public class FrameMaskRepositoryImpl implements FrameMaskRepository {
    @Override
    public FrameMask buildMask() {
        return new FrameMask("person", true);
    }
}
