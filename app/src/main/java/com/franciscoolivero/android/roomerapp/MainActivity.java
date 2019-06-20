package com.franciscoolivero.android.roomerapp;

import android.os.Bundle;
import android.view.MenuItem;

import com.franciscoolivero.android.roomerapp.Matches.MatchesFragment;
import com.franciscoolivero.android.roomerapp.Filters.FiltersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_nav_bar);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Busquedas");
        loadFragment(new ResultsFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_busquedas:
                    toolbar.setTitle("Busquedas");
                    fragment = new ResultsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_filtros:
                    toolbar.setTitle("Filtros");
                    fragment = new FiltersFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_matches:
                    toolbar.setTitle("Matches");
                    fragment = new MatchesFragment();
                    loadFragment(fragment);
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
