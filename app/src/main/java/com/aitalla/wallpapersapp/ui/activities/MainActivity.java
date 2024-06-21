package com.aitalla.wallpapersapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;


import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.adapters.PageAdapter;
import com.aitalla.wallpapersapp.utilities.Utilities;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDrawerLayout();
        Utilities.setStatusBrandNavigationBarColorPrimary(MainActivity.this);
        Utilities.initSdk(this);

    }


    private void setupDrawerLayout() {

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);

        //attach tabs to tablayout
        new TabLayoutMediator(tabLayout,viewPager,((tab,position)-> tab.setText(Utilities.getTabs()[position]))).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                //Utilities.toast(MainActivity.this,"Page Scrolled by " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
              //  Utilities.toast(MainActivity.this,"Page Selected : " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
              //  Utilities.toast(MainActivity.this,"Page Scroll Sate : " + state);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
          //  Utilities.toast(MainActivity.this, "Home Button");
        } else if (id == R.id.nav_download) {
            startActivity(new Intent(this,DownloadsActivity.class));
        } else if (id == R.id.nav_rate_us) {
            //Utilities.toast(MainActivity.this, "Rate Us Button");
        }  else if (id == R.id.nav_privacy_policy) {
            startActivity(new Intent(this,PrivacyPolicyActivity.class));
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}