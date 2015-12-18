package com.alphalabz.familyapp.Activities;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.alphalabz.familyapp.Fragments.BlankFragment;
import com.alphalabz.familyapp.Fragments.ProfileFragment;
import com.alphalabz.familyapp.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MainActivity extends AppCompatActivity {

    private Drawer result = null;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Account header for the Google Material Drawer
        //Responsive to multiple accounts
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.header)
                .withTextColorRes(R.color.md_white_1000)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        //Create the drawer
        result = new DrawerBuilder(this)
                //this layout have to contain child layouts
                .withRootView(R.id.drawer_container)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_android),
                        new PrimaryDrawerItem().withName("Profile").withIcon(FontAwesome.Icon.faw_android),
                        new PrimaryDrawerItem().withName("Item 3").withIcon(FontAwesome.Icon.faw_android),
                        new PrimaryDrawerItem().withName("Item 4").withIcon(FontAwesome.Icon.faw_android),
                        new PrimaryDrawerItem().withName("Item 5").withIcon(FontAwesome.Icon.faw_android)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int i, IDrawerItem drawerItem) {

                        if (drawerItem != null && drawerItem instanceof Nameable) {
                            String name = ((Nameable) drawerItem).getName().getText(MainActivity.this);
                            getSupportActionBar().setTitle(name);
                            Fragment fragment;
                            switch (i) {
                                case 1:
                                    fragment = new BlankFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;
                                case 2:
                                    fragment = new ProfileFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;

                                default:
                                    fragment = new BlankFragment();
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                    break;

                            }
                        }

                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {



                    }
                })
                .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();



    }

}
