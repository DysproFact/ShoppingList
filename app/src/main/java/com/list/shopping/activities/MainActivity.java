package com.list.shopping.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.list.shopping.R;
import com.list.shopping.database.DatabaseHelper;
import com.list.shopping.database.User;
import com.list.shopping.fragments.GroceryFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GroceryFragment.OnFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fabMenu)
    FloatingActionButton fabMenu;
    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;
    @BindView(R.id.fabRemove)
    FloatingActionButton fabRemove;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private static final String TAG = MainActivity.class.getName();
    private boolean isFABOpen = false;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        db = DatabaseHelper.getInstance(this);

        // Gestion du fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, new GroceryFragment());
        ft.commit();
    }

    @OnClick(R.id.fabMenu)
    public void fabListener(View view) {
        if (!isFABOpen) {
            isFABOpen = true;
            fabAdd.animate().translationY(-getResources().getDimension(R.dimen.anim_fab_subMenu));
            fabRemove.animate().translationX(-getResources().getDimension(R.dimen.anim_fab_subMenu));
            fabMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_close_white_24px));
        } else {
            isFABOpen = false;
            fabAdd.animate().translationY(0);
            fabRemove.animate().translationX(0);
            fabMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_white_24px));
        }
    }

    @OnClick(R.id.fabAdd)
    public void fabAddListener(FloatingActionButton fabSubMenu) {
        Toast.makeText(this, "Ajouter", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.fabRemove)
    public void fabRemoveListener(FloatingActionButton fabSubMenu) {
        listUsers();
        Toast.makeText(this, "Supprimer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void listUsers(){
        List<User> T_users = db.getAllUsers();

        for (User user :T_users){
            Log.d(TAG, "login : " + user.login + " || lastName" + user.lastName);
        }
    }
}
