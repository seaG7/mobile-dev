package ru.mirea.danilov.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NoteFragment extends Fragment {
    public static final String REQUEST = "requestKey";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText edit = view.findViewById(R.id.editNote);
        view.findViewById(R.id.buttonSend).setOnClickListener(v -> {
            String text = edit.getText().toString().trim();
            if (text.isEmpty()) {
                text = getString(R.string.empty_note);
            }
            Bundle bundle = new Bundle();
            bundle.putString("key", text);
            getParentFragmentManager().setFragmentResult(REQUEST, bundle);
            new NoteSheet().show(getParentFragmentManager(), "note");
        });
    }
}
