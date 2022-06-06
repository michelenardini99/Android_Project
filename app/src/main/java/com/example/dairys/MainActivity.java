package com.example.dairys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairys.Database.AppDatabase;
import com.example.dairys.Database.Food;
import com.example.dairys.Database.User;
import com.example.dairys.Fragment.DreamDiaryFragment;
import com.example.dairys.Fragment.HomeFragment;
import com.example.dairys.Fragment.SettingsFragment;
import com.example.dairys.Fragment.StatisticsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static int REQUEST_CODE_PHOTO = 1;

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

        getUserLoggedData();

        goToHomeFragment();

        SettingsFragment.setLocale(this);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_closed);

        drawerLayout.setDrawerListener(toggle);

        View headerView = navigationView.getHeaderView(0);
        ImageView img = headerView.findViewById(R.id.imageWallpaperDrawer);
        if(!readWallpaper(this).isEmpty()){
            img.setImageURI(Uri.parse(readWallpaper(this)));
        }
        toolbar.setTitle(R.string.home);
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
                        toolbar.setTitle(R.string.home);
                        fragment = new HomeFragment();
                        break;
                    case R.id.statistics:
                        toolbar.setTitle(R.string.statistics);
                        fragment = new StatisticsFragment();
                        break;
                    case R.id.dream_diary:
                        toolbar.setTitle(R.string.dream_diary);
                        fragment = new DreamDiaryFragment();
                        break;
                    case R.id.settings:
                        toolbar.setTitle(R.string.settings);
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
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case R.id.targets:
                startActivity(new Intent(this, TargetsActivity.class));
                drawerLayout.closeDrawers();
                break;
            case R.id.share:
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                String body = "Download this App";
                String sub = "http://play.tipiaceresse.com";
                sendIntent.putExtra(Intent.EXTRA_TEXT, body);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sub);
                startActivity(Intent.createChooser(sendIntent, "Share using"));
                drawerLayout.closeDrawers();
                break;
            case R.id.wallpaper_drawer:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
                break;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CODE_PHOTO){
                Uri selectedImage = data.getData();
                View headerView = navigationView.getHeaderView(0);
                ImageView img = headerView.findViewById(R.id.imageWallpaperDrawer);
                img.setImageURI(selectedImage);
                writeWallpaper(selectedImage.toString());
            }
        }
    }

    public void getUserLoggedData(){
        List<User> user = db.userDao().userLogged();
        if(user.size() != 0){
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.username);
            navUsername.setText(user.get(0).getUsername());
            if (user.get(0).getImageProfile() != null){
                CircleImageView imageProfile = headerView.findViewById(R.id.image_user);
                imageProfile.setImageURI(Uri.parse(user.get(0).getImageProfile()));
            }
        }
    }

    private void writeWallpaper(String image){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("drawer_wallpaper", image);
        myEdit.commit();
    }

    public String readWallpaper(Context context){
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        return sh.getString("drawer_wallpaper", "");
    }


}