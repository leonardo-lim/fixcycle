package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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
        replaceFragment(new HomeFragment());

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