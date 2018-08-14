package unikom.skripsi.angga.masyarakat.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import unikom.skripsi.angga.masyarakat.Fragment.HistoryFragment;
import unikom.skripsi.angga.masyarakat.Fragment.HomeFragment;
import unikom.skripsi.angga.masyarakat.Fragment.SungaiFragment;
import unikom.skripsi.angga.masyarakat.R;

public class UtamaActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private Toolbar toolbar;
    private int position = 0;
    private SungaiFragment sungaiFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    getSupportActionBar().setTitle("Home");
                    position = 0;
                    invalidateOptionsMenu();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new SungaiFragment();
                    getSupportActionBar().setTitle("Sungai");
                    position = 1;
                    invalidateOptionsMenu();
                    break;
                case R.id.navigation_notifications:
                    fragment = new HistoryFragment();
                    getSupportActionBar().setTitle("History");
                    position = 2;
                    invalidateOptionsMenu();
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            position = 0;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
        }
        fragmentManager = getSupportFragmentManager();
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        if (position == 1){
            menuItem.setVisible(true);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
            searchView.setQueryHint("Cari sungai");
            sungaiFragment = (SungaiFragment)fragment;
            search(searchView);
        }else {
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sungaiFragment.searchSungai(newText);
                return true;
            }
        });
    }
}
