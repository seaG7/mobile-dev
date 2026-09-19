package ru.mirea.danilov.anishot.data.repository;

import android.content.Context;

import ru.mirea.danilov.anishot.domain.models.FrameMask;
import ru.mirea.danilov.anishot.domain.repository.FrameMaskRepository;

public class FrameMaskRepositoryImpl implements FrameMaskRepository {
    public FrameMaskRepositoryImpl(Context context) {
    }

    @Override
    public FrameMask buildMask() {
        return new FrameMask("person", true);
    }
}
