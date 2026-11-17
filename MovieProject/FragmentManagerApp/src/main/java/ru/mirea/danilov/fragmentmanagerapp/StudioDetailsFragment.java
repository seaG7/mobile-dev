package ru.mirea.danilov.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class StudioDetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studio_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.textName);
        TextView place = view.findViewById(R.id.textPlace);
        TextView about = view.findViewById(R.id.textAbout);
        StudioViewModel model = new ViewModelProvider(requireActivity()).get(StudioViewModel.class);
        model.selected().observe(getViewLifecycleOwner(), studio -> {
            if (studio == null) {
                return;
            }
            name.setText(studio.name);
            place.setText(studio.place);
            about.setText(studio.about);
        });
    }
}
