package ru.mirea.danilov.anishot.presentation;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import ru.mirea.danilov.anishot.R;

public class HomeActivity extends AppCompatActivity {
    private View nav;
    private NavController navController;
    private static final int[] NAV = {
            R.id.nav_search,
            R.id.nav_catalog,
            R.id.nav_list,
            R.id.nav_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        View nav = findViewById(R.id.bottomNav);
        this.nav = nav;
        int baseMargin = nav.getLayoutParams() instanceof ViewGroup.MarginLayoutParams
                ? ((ViewGroup.MarginLayoutParams) nav.getLayoutParams()).bottomMargin
                : 0;
        ViewCompat.setOnApplyWindowInsetsListener(nav, (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.bottomMargin = baseMargin + bars.bottom;
            view.setLayoutParams(params);
            return insets;
        });
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        navController = host.getNavController();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean card = destination.getId() == R.id.details;
            this.nav.setVisibility(card ? View.GONE : View.VISIBLE);
            for (int item : NAV) {
                findViewById(item).setSelected(item == destination.getId());
            }
        });
        View.OnClickListener open = view -> openTab(view.getId());
        for (int id : NAV) {
            findViewById(id).setOnClickListener(open);
        }
    }

    public void openDetails(int animeId) {
        Bundle args = new Bundle();
        args.putInt("animeId", animeId);
        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.rise_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.rise_in)
                .setPopExitAnim(R.anim.fade_out)
                .build();
        navController.navigate(R.id.details, args, options);
    }

    private void openTab(int id) {
        NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(R.id.nav_search, false, true)
                .build();
        navController.navigate(id, null, options);
    }
}
