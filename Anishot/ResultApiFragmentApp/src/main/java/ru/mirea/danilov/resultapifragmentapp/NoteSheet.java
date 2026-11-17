package ru.mirea.danilov.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class NoteSheet extends BottomSheetDialogFragment {
    private static final String STATE_RECEIVED = "state_received";
    private String received = "";
    private TextView note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            received = savedInstanceState.getString(STATE_RECEIVED, "");
        }
        getParentFragmentManager().setFragmentResultListener(NoteFragment.REQUEST, this, (key, bundle) -> {
            received = bundle.getString("key", "");
            if (note != null) {
                note.setText(received);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        note = view.findViewById(R.id.textNote);
        note.setText(received);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_RECEIVED, received);
    }
}
