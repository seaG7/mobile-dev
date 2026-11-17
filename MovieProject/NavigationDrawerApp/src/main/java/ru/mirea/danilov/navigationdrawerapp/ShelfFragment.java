package ru.mirea.danilov.navigationdrawerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.danilov.navigationdrawerapp.databinding.FragmentShelfBinding;

public class ShelfFragment extends Fragment {
    private FragmentShelfBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShelfBinding.inflate(inflater, container, false);
        binding.textLead.setText("Три тайтла ждут повторного просмотра.");
        binding.textLine1.setText("Фрирен");
        binding.textLine2.setText("Унесённые призраками");
        binding.textLine3.setText("Вайолет Эвергарден");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
