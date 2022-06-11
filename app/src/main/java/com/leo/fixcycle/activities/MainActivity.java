package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leo.fixcycle.R;
import com.leo.fixcycle.databinding.ActivityMainBinding;
import com.leo.fixcycle.fragments.BookServiceFragment;
import com.leo.fixcycle.fragments.HomeFragment;
import com.leo.fixcycle.fragments.MyMotorcycleFragment;
import com.leo.fixcycle.fragments.OrdersFragment;
import com.leo.fixcycle.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        String fragmentName = "home";
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            fragmentName = extras.getString("fragmentName");
        }

        switch (fragmentName) {
            case "home":
                replaceFragment(new HomeFragment());
                bottomNavigationView.setSelectedItemId(R.id.home_nav);
                break;
            case "orders":
                replaceFragment(new OrdersFragment());
                bottomNavigationView.setSelectedItemId(R.id.orders_nav);
                break;
            case "bookService":
                replaceFragment(new BookServiceFragment());
                bottomNavigationView.setSelectedItemId(R.id.book_service_nav);
                break;
            case "myMotorcycle":
                replaceFragment(new MyMotorcycleFragment());
                bottomNavigationView.setSelectedItemId(R.id.my_moto_nav);
                break;
            default:
                replaceFragment(new ProfileFragment());
                bottomNavigationView.setSelectedItemId(R.id.profile_nav);
                break;
        }

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home_nav) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.orders_nav) {
                replaceFragment(new OrdersFragment());
            } else if (itemId == R.id.book_service_nav) {
                replaceFragment(new BookServiceFragment());
            } else if (itemId == R.id.my_moto_nav) {
                replaceFragment(new MyMotorcycleFragment());
            } else {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}