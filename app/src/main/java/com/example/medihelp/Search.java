package com.example.medihelp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by abc on 27-02-2016.
 */
public class Search extends AppCompatActivity {

    SearchView searchView;
    ArrayList<Data> info, searchResults;
    Toolbar toolbar;
    RecyclerView hospital_list;
    RecyclerViewAdapterForHospitals rvAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchResults = new ArrayList<>();
        info = new ArrayList<>();
        info=Hospitals.info;
        
        searchView = (SearchView) findViewById(R.id.search_view);
        hospital_list = (RecyclerView) findViewById(R.id.hospitals_list);

        searchView.setIconified(false);
        searchView.setBackgroundColor(Color.WHITE);
        int id=searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void startSearch(String query) {
        query=query.toLowerCase();
        for (int i = 0; i < info.size(); i++) {
            String address = info.get(i).Hospitalname;
            address=address.toLowerCase();
            Log.e("flag",address+","+query);
            if(address.contains(query)){
                searchResults.add(info.get(i));
            }
        }
        populate();
    }

    private void populate() {
        rvAdapter = new RecyclerViewAdapterForHospitals(this, searchResults);
        hospital_list.setAdapter(rvAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        hospital_list.setLayoutManager(linearLayoutManager);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
