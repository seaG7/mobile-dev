package ru.mirea.danilov.bottomnavigationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.danilov.bottomnavigationapp.databinding.FragmentTimingBinding;

public class TimingFragment extends Fragment {
    private FragmentTimingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTimingBinding.inflate(inflater, container, false);
        binding.textTitle.setText("Тайминг");
        binding.textLine1.setText("Заставка — 1:28");
        binding.textLine2.setText("Нужный кадр — 12:04");
        binding.textLine3.setText("Титры — 23:11");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
