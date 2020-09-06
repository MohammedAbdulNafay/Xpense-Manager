package com.example.xpensemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //Fragments ...

    DashboardFragment dashboardFragment;
    IncomeFragment incomeFragment;
    ExpenseFragment expenseFragment;

    // Firebase

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Xpense Manager");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottomNavigationbar);
        frameLayout = findViewById(R.id.main_frame);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.naView);
        navigationView.setNavigationItemSelectedListener(this);

        dashboardFragment = new DashboardFragment();
        incomeFragment = new IncomeFragment();
        expenseFragment = new ExpenseFragment();

        //By default we show dashboard
        setFragment(dashboardFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch(menuItem.getItemId())
                {

                    case R.id.dashboard:

                        setFragment(dashboardFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);

                        return true;

                    case R.id.income:

                        setFragment(incomeFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.income_color);

                        return true;

                    case R.id.expense:

                        setFragment(expenseFragment);
                        bottomNavigationView.setItemBackgroundResource(R.color.expense_color);

                        return true;

                    default:

                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment)
    {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        displaySelectedListener(menuItem.getItemId());
        return false;
    }

    public void displaySelectedListener(int itemId)
    {
        Fragment fragment = null;

        switch (itemId)
        {
            case R.id.dashboard:

                fragment = new DashboardFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.dashboard_color);
                break;

            case R.id.income:

                fragment = new IncomeFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.income_color);
                break;

            case R.id.expense:

                fragment = new ExpenseFragment();
                bottomNavigationView.setItemBackgroundResource(R.color.expense_color);
                break;

            case R.id.logout:

                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        if (fragment != null)
        {

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, fragment);
            fragmentTransaction.commit();


        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

    }
}