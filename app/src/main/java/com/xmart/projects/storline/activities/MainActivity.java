package com.xmart.projects.storline.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.xmart.projects.storline.R;
import com.xmart.projects.storline.activities.fragments.CartFragment;
import com.xmart.projects.storline.activities.fragments.CategoryProductFragment;
import com.xmart.projects.storline.activities.fragments.ProductsFragment;
import com.xmart.projects.storline.activities.fragments.SalesFragment;
import com.xmart.projects.storline.activities.products.CategoriesActivity;
import com.xmart.projects.storline.data.UserDriver;

import static android.support.design.widget.NavigationView.*;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private static final int CATEGORY_CODE = 123;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView tvUserName, tvUserMail;
    private ImageView imgUser;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        tvUserName = (TextView) header.findViewById(R.id.tvUserName);
        tvUserMail = (TextView) header.findViewById(R.id.tvUserMail);
        imgUser = (ImageView) header.findViewById(R.id.imgUser);

        fragmentManager.beginTransaction().replace(R.id.fragmentContent, new CategoryProductFragment()).commit();

    }


    @Override
    protected void onStart() {
        super.onStart();

        updateUI(mAuth.getCurrentUser());
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchMenuItem.getActionView();
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sign_out:
                signOutApp();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //Item selecteds

        switch (id) {
            case R.id.nav_home:
                fragmentManager.beginTransaction().replace(R.id.fragmentContent, new CategoryProductFragment()).commit();
                break;
            case R.id.nav_categories:
                Intent categoriesIntent = new Intent(this, CategoriesActivity.class);
                startActivityForResult(categoriesIntent, CATEGORY_CODE);
                break;
            case R.id.nav_cart:
                fragmentManager.beginTransaction().replace(R.id.fragmentContent, new CartFragment()).commit();
                break;
            case R.id.nav_sales:
                fragmentManager.beginTransaction().replace(R.id.fragmentContent, new SalesFragment()).commit();
                break;
            case R.id.nav_products:
                fragmentManager.beginTransaction().replace(R.id.fragmentContent, new ProductsFragment()).commit();
                break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case CATEGORY_CODE:

                    String category = data.getExtras().getString("category");
                    Bundle bundle = new Bundle();
                    bundle.putString("category", category);

                    CategoryProductFragment cpf = new CategoryProductFragment();
                    cpf.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.fragmentContent, cpf).commitAllowingStateLoss();

                    break;

            }

        }

    }


    private void signOutApp() {
        mAuth.signOut();
        updateUI(mAuth.getCurrentUser());
    }


    private void updateUI(FirebaseUser currentUser) {

        if (currentUser == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else  {
            refreshUser();
        }
    }


    private void refreshUser() {
        if (mAuth != null) {
            FirebaseUser user = mAuth.getCurrentUser();

            UserDriver ud = new UserDriver(this, "Main");
            ud.tvUser = tvUserName;
            ud.tvMail = tvUserMail;
            ud.imgUser = imgUser;
            ud.navAdmin = navigationView.getMenu().findItem(R.id.nav_products);

            ud.getOneUser(user.getUid());

            ud.refreshToken(FirebaseInstanceId.getInstance().getToken());
        }
    }

}
