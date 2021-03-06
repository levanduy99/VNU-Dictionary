package com.example.vnuepttest176;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vnuepttest176.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MenuItem menuSetting;
    Toolbar toolbar;

    DBHelper dbHelper;

    DictionaryFragment dictionaryFragment;
    BookmarkFragment bookmarkFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dictionaryFragment = new DictionaryFragment();
        bookmarkFragment = BookmarkFragment.getNewInstance (dbHelper);
        goToFragment(dictionaryFragment,true);

        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this,"dic_type");
                int dicType = id == null? R.id.action_eng_vn:Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value,dbHelper,dicType),false);
            }
        });
        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this,"dic_type");
                int dicType = id == null? R.id.action_eng_vn:Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value,dbHelper,dicType),false);
            }
        });

        EditText edit_search = findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dictionaryFragment.filterValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(R.id.action_settings);

        String id = Global.getState(this,"dic_type");

        if(id != null){
            onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
        }
        else {
            ArrayList<String> source = dbHelper.getWord(R.id.action_eng_vn);
            dictionaryFragment.resetDataSource(source);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if(R.id.action_settings == id)
//            return true;
        int id = item.getItemId();
        if(R.id.action_settings == id) {
            return true;
        }

        Global.saveState(this,"dic_type",String.valueOf(id));
        ArrayList<String> source = dbHelper.getWord(id);

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_eng_vn){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dictionaryFragment.resetDataSource(source);
                menuSetting.setIcon(getDrawable(R.drawable.ic_e));
            }
        }
        else if(id==R.id.action_vn_eng){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dictionaryFragment.resetDataSource(source);
                menuSetting.setIcon(getDrawable(R.drawable.ic_v));
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatemenWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_bookmark){
            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if(!activeFragment.equals(BookmarkFragment.class.getSimpleName())){
                goToFragment(bookmarkFragment,false);
            }
        }
        if(id == R.id.nav_help){
            Intent intent = new Intent(MainActivity.this,SelectActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void goToFragment(Fragment fragment,boolean isTop){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container,fragment);
        if(!isTop)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
        //android.app.Fragment activeFragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if(activeFragment.equals(BookmarkFragment.class.getSimpleName())){
            menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle("Bookmark");
        }else{
            menuSetting.setVisible(true);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.VISIBLE);
            toolbar.setTitle("");
        }
        return true;
    }

}