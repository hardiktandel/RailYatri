package com.example.railyatri.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.railyatri.R;
import com.example.railyatri.Utils.Const;
import com.example.railyatri.Utils.SharedPref;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btn_search;
    private TextView tv_user_name;
    private CircleImageView img_user;
    private ImageButton img_btn_swap;
    private Toolbar toolbar;
    private AutoCompleteTextView ac_tv_from_station;
    private AutoCompleteTextView ac_tv_to_station;
    private LinearLayout nav_header;
    private Activity activity;
    private SharedPref pref;
    private String from_station;
    private String to_station;
    private ArrayList<String> keys = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("E-Rail");

        activity = this;
        ac_tv_from_station = findViewById(R.id.ac_tv_from_station);
        ac_tv_to_station = findViewById(R.id.ac_tv_to_station);
        btn_search = findViewById(R.id.btn_search);
        img_btn_swap = findViewById(R.id.img_btn_swap);

        pref = new SharedPref(HomeActivity.this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        img_user = navigationView.getHeaderView(0).findViewById(R.id.img_user);
        nav_header = navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        tv_user_name = navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        if (pref.getFirstName().equals("Guest User")) {
            navigationView.getMenu().findItem(R.id.nav_log_out).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_food_order).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_shop).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_coolie).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_booked_coolie).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_pass).setVisible(false);
            tv_user_name.setText(pref.getFirstName());
        } else {
            navigationView.getMenu().findItem(R.id.nav_log_out).setVisible(true);
            setNavHeaderDetails();
        }

        switch (pref.getTYPE().toLowerCase()) {
            case "user":
            case "student":
                navigationView.getMenu().findItem(R.id.nav_shop).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_coolie).setVisible(false);
                break;
            case "coolie":
                navigationView.getMenu().findItem(R.id.nav_shop).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_food_order).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_booked_coolie).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_pass).setVisible(false);
                break;
            case "shop":
                navigationView.getMenu().findItem(R.id.nav_food_order).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_coolie).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_booked_coolie).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_pass).setVisible(false);
                break;
        }

        nav_header.setOnClickListener(v -> {
            if (tv_user_name.getText().equals("Guest User")) {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                return;
            }
            Intent i = new Intent(this, ViewProfileActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, img_user, "userImg");
            startActivityForResult(i, 100, options.toBundle());
        });

        station();

        btn_search.setOnClickListener(v -> {

            from_station = ac_tv_from_station.getText().toString().trim();
            to_station = ac_tv_to_station.getText().toString().trim();

            if (TextUtils.isEmpty(from_station)) {
                ac_tv_from_station.setError("Please Enter Station Name");
                ac_tv_from_station.requestFocus();
            } else if (TextUtils.isEmpty(to_station)) {
                ac_tv_to_station.setError("Please Enter Station Name");
                ac_tv_to_station.requestFocus();
            } else if (from_station.equals(to_station)) {
                Toast.makeText(activity, "Please select different station", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(activity, TrainListActivity.class);
                String[] from = ac_tv_from_station.getText().toString().split("-");
                String[] to = ac_tv_to_station.getText().toString().split("-");
                i.putExtra("ToCode", to[1]);
                i.putExtra("To", to[0]);
                i.putExtra("FromCode", from[1]);
                i.putExtra("From", from[0]);
                startActivity(i);
            }
        });
        img_btn_swap.setOnClickListener(v -> {

            RotateAnimation rotateAnim = new RotateAnimation(0, 180, (float) v.getWidth() / 2, (float) v.getHeight() / 2);
            rotateAnim.setDuration(300);
            rotateAnim.setFillAfter(true);
            v.startAnimation(rotateAnim);

            String fromStation = ac_tv_from_station.getText().toString();
            String toStation = ac_tv_to_station.getText().toString();
            ac_tv_from_station.setText(toStation);
            ac_tv_from_station.clearFocus();
            ac_tv_to_station.setText(fromStation);
            ac_tv_to_station.clearFocus();
            ac_tv_from_station.dismissDropDown();
            ac_tv_to_station.dismissDropDown();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setNavHeaderDetails() {
        tv_user_name.setText(pref.getFirstName() + " " + pref.getLastName());
        if (pref.getPhoto().equals("")) {
            img_user.setImageResource(R.drawable.ic_account_circle_200dp);
        } else {
            Picasso.get().load(Const.URL + pref.getPhoto())
                    .error(R.drawable.ic_account_circle_200dp)
                    .into(img_user);
        }
    }

    private void station() {
        try {
            InputStream databaseInputStream = getResources().openRawResource(R.raw.station);
            BufferedReader r = new BufferedReader(new InputStreamReader(databaseInputStream));
            String line;
            while ((line = r.readLine()) != null) {
                keys.add(line);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keys);
            ac_tv_from_station.setAdapter(adapter);
            ac_tv_from_station.setThreshold(1);
            ac_tv_to_station.setAdapter(adapter);
            ac_tv_to_station.setThreshold(1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_log_out:
                pref.LogOut();
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                removeShortcuts();
                Toast.makeText(HomeActivity.this, "Successfully Logout!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_shop:
                Intent foodIntent = new Intent(HomeActivity.this, FoodShopActivity.class);
                startActivity(foodIntent);
                break;
            case R.id.nav_food_order:
                Intent foodOrderIntent = new Intent(HomeActivity.this, FoodOrderActivity.class);
                startActivity(foodOrderIntent);
                break;
            case R.id.nav_coolie:
                Intent coolieIntent = new Intent(HomeActivity.this, CoolieActivity.class);
                startActivity(coolieIntent);
                break;
            case R.id.nav_booked_coolie:
                Intent bookCoolieIntent = new Intent(HomeActivity.this, BookedCoolieActivity.class);
                startActivity(bookCoolieIntent);
                break;
            case R.id.nav_pass:
                Intent passTicketIntent = new Intent(HomeActivity.this, PassTicketActivity.class);
                startActivity(passTicketIntent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            Objects.requireNonNull(shortcutManager).removeAllDynamicShortcuts();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                setNavHeaderDetails();
            }
        }
    }
}
