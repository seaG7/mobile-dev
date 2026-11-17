package ru.mirea.danilov.fragmentmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudioViewModel extends ViewModel {
    private final MutableLiveData<Studio> selected = new MutableLiveData<>();

    public void select(Studio studio) {
        selected.setValue(studio);
    }

    public LiveData<Studio> selected() {
        return selected;
    }
}
