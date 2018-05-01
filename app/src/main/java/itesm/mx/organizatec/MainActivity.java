package itesm.mx.organizatec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_doc_practice);

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.isChecked()) {
                getSupportActionBar().setTitle(item.getTitle());
            }
        }

        MaterialPagerFragment fragment = new MaterialPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MaterialPagerFragment.MATERIAL_TYPE, getMaterialType(R.id.nav_doc_practice));

        fragment.setArguments(bundle);

        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager().beginTransaction().add(R.id.content_frame, fragment).commit();

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_doc_practice || id == R.id.nav_doc_theory) {
            MaterialPagerFragment fragment = new MaterialPagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(MaterialPagerFragment.MATERIAL_TYPE, getMaterialType(id));
            fragment.setArguments(bundle);

            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

            item.setChecked(true);

        } else if (id == R.id.nav_info_teacher) {
            Intent intent = new Intent(this, TeacherInformationActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getMaterialType (Integer navDrawerElementId) {
        String materialType = "";

        if (navDrawerElementId == R.id.nav_doc_practice) {
            materialType = "Practice";
        }

        if (navDrawerElementId == R.id.nav_doc_theory) {
            materialType = "Theory";
        }

        return materialType;
    }
}
