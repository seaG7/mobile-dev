package ru.mirea.danilov.anishot.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.presentation.AuthActivity;
import ru.mirea.danilov.anishot.presentation.vm.AnishotFactory;
import ru.mirea.danilov.anishot.presentation.vm.ProfileViewModel;
import ru.mirea.danilov.anishot.presentation.vm.Step;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView name = view.findViewById(R.id.textName);
        TextView mail = view.findViewById(R.id.textMail);
        TextView date = view.findViewById(R.id.textDate);
        TextView badge = view.findViewById(R.id.textBadge);
        TextView letter = view.findViewById(R.id.textAvatarLetter);
        TextView listCount = view.findViewById(R.id.textListCount);
        Button auth = view.findViewById(R.id.buttonAuth);
        Button logout = view.findViewById(R.id.buttonLogout);

        ProfileViewModel viewModel = new ViewModelProvider(this, AnishotFactory.from(this)).get(ProfileViewModel.class);
        viewModel.state().observe(getViewLifecycleOwner(), state -> {
            if (state == null || state.user == null) {
                return;
            }
            User user = state.user;
            String nick = nickname(user);
            name.setText(nick);
            letter.setText(nick.substring(0, 1).toUpperCase());
            listCount.setText(String.valueOf(state.saved));
            if (user.isGuest()) {
                mail.setText("Без аккаунта");
                date.setText("—");
                badge.setText("Гость");
                logout.setVisibility(View.GONE);
                auth.setVisibility(View.VISIBLE);
            } else {
                mail.setText(user.getLogin());
                date.setText(user.getLastLoginAt());
                badge.setText("Коллекционер");
                auth.setVisibility(View.GONE);
                logout.setVisibility(View.VISIBLE);
            }
        });
        auth.setOnClickListener(v -> openAuth());
        logout.setOnClickListener(v -> viewModel.logout());
        viewModel.navigation().observe(getViewLifecycleOwner(), step -> {
            if (step == null || step.take() != Step.Where.AUTH) {
                return;
            }
            openAuth();
            requireActivity().finish();
        });
    }

    private void openAuth() {
        startActivity(new Intent(requireContext(), AuthActivity.class));
    }

    private static String nickname(User user) {
        if (user.isGuest()) {
            return "Гость";
        }
        String login = user.getLogin() == null ? "" : user.getLogin();
        int at = login.indexOf('@');
        String raw = at > 0 ? login.substring(0, at) : login;
        if (raw.isEmpty()) {
            return "Пользователь";
        }
        return Character.toUpperCase(raw.charAt(0)) + raw.substring(1);
    }
}
