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

import ru.mirea.danilov.anishot.AnishotApp;
import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.domain.GetMyListUseCase;
import ru.mirea.danilov.anishot.domain.GetProfileUseCase;
import ru.mirea.danilov.anishot.domain.LogoutUseCase;
import ru.mirea.danilov.anishot.domain.models.User;
import ru.mirea.danilov.anishot.presentation.AuthActivity;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AnishotApp app = (AnishotApp) requireActivity().getApplication();
        User user = new GetProfileUseCase(app.authRepository()).execute();
        TextView name = view.findViewById(R.id.textName);
        TextView mail = view.findViewById(R.id.textMail);
        TextView date = view.findViewById(R.id.textDate);
        TextView badge = view.findViewById(R.id.textBadge);
        TextView letter = view.findViewById(R.id.textAvatarLetter);
        TextView listCount = view.findViewById(R.id.textListCount);
        Button auth = view.findViewById(R.id.buttonAuth);
        Button logout = view.findViewById(R.id.buttonLogout);

        String nick = nickname(user);
        name.setText(nick);
        letter.setText(nick.substring(0, 1).toUpperCase());
        int saved = new GetMyListUseCase(app.listRepository(), app.authRepository()).execute().size();
        listCount.setText(String.valueOf(saved));

        if (user.isGuest()) {
            mail.setText("Без аккаунта");
            date.setText("—");
            badge.setText("Гость");
            logout.setVisibility(View.GONE);
            auth.setOnClickListener(v -> startActivity(new Intent(requireContext(), AuthActivity.class)));
        } else {
            mail.setText(user.getLogin());
            date.setText(user.getLastLoginAt());
            badge.setText("Коллекционер");
            auth.setVisibility(View.GONE);
            logout.setOnClickListener(v -> {
                new LogoutUseCase(app.authRepository()).execute();
                startActivity(new Intent(requireContext(), AuthActivity.class));
                requireActivity().finish();
            });
        }
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
