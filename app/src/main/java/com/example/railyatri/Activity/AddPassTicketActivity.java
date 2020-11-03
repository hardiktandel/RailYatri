package com.example.railyatri.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.railyatri.Adapter.ViewPagerAdapter;
import com.example.railyatri.Fragment.FragmentAddCollagePass;
import com.example.railyatri.Fragment.FragmentAddNormalPass;
import com.example.railyatri.Fragment.FragmentAddTicket;
import com.example.railyatri.R;
import com.example.railyatri.Utils.SharedPref;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class AddPassTicketActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Activity activity;
    private SharedPref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pass_ticket);

        activity = this;
        pref = new SharedPref(activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Book Ticket & Pass");

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new FragmentAddTicket(), "Ticket");
        if (pref.getTYPE().equalsIgnoreCase("student")) {
            adapter.addFragment(new FragmentAddCollagePass(), "Pass");
        } else {
            adapter.addFragment(new FragmentAddNormalPass(), "Pass");
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }
}
