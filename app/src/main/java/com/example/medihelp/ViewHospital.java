package com.example.medihelp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by abc on 27-02-2016.
 */
public class ViewHospital extends AppCompatActivity {
    Toolbar toolbar;
    Data data;
    TextView name, caretype_name, address, med_type, special, facility, mobile, email, website;
    ImageView caretype, type;
    String Details_Str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_hospital);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        data = new Data();
        data = (Data) (getIntent().getSerializableExtra("data"));
        initialize();
        populate(data);
    }

    private void initialize() {
        name = (TextView) findViewById(R.id.name);
        caretype_name = (TextView) findViewById(R.id.caretype_name);
        address = (TextView) findViewById(R.id.address);
        med_type = (TextView) findViewById(R.id.med_type);
        special = (TextView) findViewById(R.id.special);
        facility = (TextView) findViewById(R.id.facility);
        mobile = (TextView) findViewById(R.id.mobile);
        email = (TextView) findViewById(R.id.email);
        website = (TextView) findViewById(R.id.website);

        caretype = (ImageView) findViewById(R.id.caretype);
        type = (ImageView) findViewById(R.id.type);
    }

    private void populate(Data data) {
        name.setText(data.Hospitalname);
        caretype_name.setText(data.Hostipalcaretype);
        address.setText(data.AddressFirstLine + ",\n" + data.District + " - " + data.Pincode + ",\n" + data.State + ".");
        med_type.setText(data.SystemsOfMedicine);
        special.setText(data.Specialties);
        facility.setText(data.Facilities);
        mobile.setText(data.Mobilenumber + "\n" + data.Telephone);
        email.setText(data.Hospitalprimaryemailid);
        website.setText(data.Website);

        if (data.Hostipalcaretype.toLowerCase().equals("hospital")) {
            caretype.setImageResource(R.drawable.hospital);
        } else if (data.Hostipalcaretype.toLowerCase().equals("clinic")) {
            caretype.setImageResource(R.drawable.clinic);
        } else {
            caretype.setImageResource(R.drawable.college);
        }

        if (data.HospitalCategory.toLowerCase().equals("public")) {
            type.setImageResource(R.drawable.public_type);
        } else {
            type.setImageResource(R.drawable.private_type);
        }

        Details_Str="Name : "+name.getText()+",\n"+
                "Caretype : "+caretype_name.getText()+",\n"+
                "Address : "+address.getText()+".\n\n"+
                "Medicine types : "+med_type.getText()+".\n"+
                "Specialities : "+special.getText()+".\n"+
                "Facility : "+facility.getText()+".\n\n"+
                "Mobile : "+mobile.getText()+"\n"+
                "Emal : "+email.getText()+"\n"+
                "Website : "+website.getText();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        if (item.getItemId() == R.id.share) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, Details_Str);
            startActivity(share);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
