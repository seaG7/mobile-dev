package ru.mirea.danilov.anishot.presentation;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import ru.mirea.danilov.anishot.R;
import ru.mirea.danilov.anishot.presentation.catalog.CatalogFragment;
import ru.mirea.danilov.anishot.presentation.details.DetailsFragment;
import ru.mirea.danilov.anishot.presentation.list.MyListFragment;
import ru.mirea.danilov.anishot.presentation.profile.ProfileFragment;
import ru.mirea.danilov.anishot.presentation.search.SearchFragment;

public class HomeActivity extends AppCompatActivity {
    private View nav;
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
        View.OnClickListener open = view -> show(view.getId());
        for (int id : NAV) {
            findViewById(id).setOnClickListener(open);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this::syncDock);
        syncDock();
        if (savedInstanceState == null) {
            show(R.id.nav_search);
        }
    }

    public void openDetails(int animeId) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.rise_in, R.anim.fade_out, R.anim.rise_in, R.anim.fade_out)
                .replace(R.id.fragmentContainer, DetailsFragment.newInstance(animeId))
                .addToBackStack("card")
                .commit();
    }

    private void syncDock() {
        boolean card = getSupportFragmentManager().getBackStackEntryCount() > 0;
        nav.setVisibility(card ? View.GONE : View.VISIBLE);
    }

    private void show(int id) {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        for (int item : NAV) {
            findViewById(item).setSelected(item == id);
        }
        Fragment fragment;
        if (id == R.id.nav_catalog) {
            fragment = new CatalogFragment();
        } else if (id == R.id.nav_list) {
            fragment = new MyListFragment();
        } else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
        } else {
            fragment = new SearchFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.rise_in, R.anim.fade_out)
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
