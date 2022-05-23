package com.example.dairys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Fragment.DreamDiaryFragment;
import com.example.dairys.Fragment.HomeFragment;
import com.example.dairys.Fragment.SettingsFragment;
import com.example.dairys.Fragment.StatisticsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private BottomNavigationView bottomNavigationView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_drawer);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        db = AppDatabase.getInstance(MainActivity.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.username);
            String value = extras.getString("username");
            navUsername.setText(value);
        }

        goToHomeFragment();


        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_closed);

        drawerLayout.setDrawerListener(toggle);
    }

    private void goToHomeFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                Fragment fragment = null;
                switch(item.getItemId()){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.statistics:
                        fragment = new StatisticsFragment();
                        break;
                    case R.id.dream_diary:
                        fragment = new DreamDiaryFragment();
                        break;
                    case R.id.settings:
                        fragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        };


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "Click profile button", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.dream_board:
                Toast.makeText(getApplicationContext(), "Click dream board button", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
            case R.id.favorites:
                Toast.makeText(getApplicationContext(), "Click favorites button", Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawers();
                break;
        }

        return true;
    }


}