package ru.mirea.danilov.bottomnavigationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.danilov.bottomnavigationapp.databinding.FragmentViewerBinding;

public class ViewerFragment extends Fragment {
    private FragmentViewerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewerBinding.inflate(inflater, container, false);
        binding.textTitle.setText("Зритель");
        binding.textName.setText("Михаил");
        binding.textPlace.setText("Место у окна");
        binding.textNote.setText("Без спойлеров до титров");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
