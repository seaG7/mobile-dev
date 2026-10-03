package ru.mirea.danilov.navigationdrawerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.danilov.navigationdrawerapp.databinding.FragmentMarksBinding;

public class MarksFragment extends Fragment {
    private FragmentMarksBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarksBinding.inflate(inflater, container, false);
        binding.textLine1.setText("12:04 — ворота в «Фрирен»");
        binding.textLine2.setText("Опенинг «Унесённых призраками»");
        binding.textLine3.setText("Письмо в «Вайолет Эвергарден»");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
