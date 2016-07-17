package com.example.medihelp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    String FIRSTTIME;
    SharedPreferences sharedpreferences;
    ListView mDrawerList;
    LinearLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mNavItems.add(new NavItem("Address", R.drawable.address));
        mNavItems.add(new NavItem("Hospitals",  R.drawable.list));
        mNavItems.add(new NavItem("About",  R.drawable.about));
        mNavItems.add(new NavItem("Reach us",  R.drawable.reach_us));

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        FIRSTTIME = sharedpreferences.getString("first", "true");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerPane = (LinearLayout) findViewById(R.id.Main_navigation);
        mDrawerList = (ListView) findViewById(R.id.menu);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        if (FIRSTTIME.equals("true")) {
            fragment = new Addres();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
        }else{
            fragment = new Hospitals();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
        }

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (FIRSTTIME.equals("true")) {
                    mDrawerLayout.openDrawer(mDrawerPane);
                }
            }
        });
        t.start();

    }

    private void selectItemFromDrawer(int position) {
        switch (position) {
            case 0:
                fragment = new Addres();
                break;
            case 1:
                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String state_name = sharedpreferences.getString("state", "NA");
                String district_name = sharedpreferences.getString("district", "NA");
                if(state_name.equals("NA") && district_name.equals("NA")){
                    Toast.makeText(getApplicationContext(),"First select state and city",Toast.LENGTH_SHORT).show();
                }else {
                    fragment = new Hospitals();
                }
                break;
            case 2:
                fragment = new Reach_us();
                break;
            case 3:
                fragment = new About();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //--------------------------------------------------------------------
    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }

    //---------------------------------------------------------------------

    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.main_navigation_item, null);
            } else {
                view = convertView;
            }

            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            TextView menu_name = (TextView) view.findViewById(R.id.menu_name);
            menu_name.setText(mNavItems.get(position).mTitle);
            icon.setImageResource(mNavItems.get(position).mIcon);
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Home.this, 2).setTitle("MediCare").
                setMessage("Do you want to exit?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
