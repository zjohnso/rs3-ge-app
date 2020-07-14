package com.example.projectscape.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.projectscape.Fragments.AllItemsFragment;
import com.example.projectscape.Fragments.BestiaryFragment;
import com.example.projectscape.Fragments.FavItemsFragment;
import com.example.projectscape.Fragments.ProfileFragment;
import com.example.projectscape.R;
import com.example.projectscape.Utility.APIHandler;
import com.example.projectscape.Utility.DataHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bn;
    private Toolbar tb;
    private ProgressBar loading;
    private ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = findViewById(R.id.loading);
        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIHandler.getItemDetails(v.getContext());
                refresh.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
            }
        });
        if (DataHandler.getMarketItems(this)) {
            loading.setVisibility(View.VISIBLE);
            APIHandler.getItemVolumes(this);
        } else {
            loading.setVisibility(View.VISIBLE);
            APIHandler.getItemDetails(this);
        }
        APIHandler.getMonsterSimpleDetails();
        if (!DataHandler.getPlayerDetails(this)) {
            APIHandler.getCharDetails(this);
        }
        bn = findViewById(R.id.nav_view);
        bn.getMenu().findItem(R.id.market).setChecked(true);
        tb = findViewById(R.id.toolBar1);
        tb.setTitle("GE Item Lookup");
        DataHandler.getFavorites(this);
        //APIHandler.getFavoritePrice(this);
    }

    public void hideLoading() {
        loading.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
    }

    public void startApp() {
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment f = null;
                switch (menuItem.getItemId()) {
                    case R.id.market:
                        f = new AllItemsFragment();
                        tb.setTitle("GE Item Lookup");
                        break;
                    case R.id.favorites:
                        f = new FavItemsFragment();
                        tb.setTitle("GE Favorite Items");
                        break;
                    case R.id.bestiary:
                        f = new BestiaryFragment();
                        tb.setTitle("Bestiary");
                        break;
                    case R.id.character:
                        f = new ProfileFragment();
                        tb.setTitle("Profile");
                        refresh.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                APIHandler.getCharDetails(v.getContext());
                                refresh.setVisibility(View.GONE);
                                loading.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                }
                return loadFragment(f);
            }
        });
        loadFragment(new AllItemsFragment());
        hideLoading();
    }

    private boolean loadFragment(Fragment f) {
        if (f != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
            return true;
        }
        return false;
    }

}
