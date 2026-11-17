package ru.mirea.danilov.bottomnavigationapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.mirea.danilov.bottomnavigationapp.databinding.FragmentFrameBinding;

public class FrameFragment extends Fragment {
    private FragmentFrameBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFrameBinding.inflate(inflater, container, false);
        binding.textKicker.setText("Сегодня");
        binding.textTitle.setText("Фрирен у ворот");
        binding.textBody.setText("14 серия. Свет уже тёплый, герои стоят перед дорогой. Этот кадр и есть повод собраться.");
        binding.textWhen.setText("Воскресенье, 19:00");
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
