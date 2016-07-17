package com.example.medihelp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Hospitals extends Fragment {

    RecyclerView hospital_list;
    RecyclerViewAdapterForHospitals rvAdapter;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    public static ArrayList<Data> info, info_local;
    SharedPreferences sharedpreferences;
    ProgressDialog dia = null;
    CheckBox publi, privat, hos, collg, clinic, ayurv, ayush, allo, homeo, neuro, unani, yoga;
    Button apply;
    Dialog dialog;
    Boolean[] checkbox_status;
    FloatingActionButton FAB, reload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.hospitals, container, false);
        hospital_list = (RecyclerView) view.findViewById(R.id.hospitals_list);
        FAB = (FloatingActionButton) view.findViewById(R.id.FAB);
        reload = (FloatingActionButton) view.findViewById(R.id.reload);

        info = new ArrayList<>();
        info_local = new ArrayList<>();
        volleySingleton = VolleySingleton.getInstance();
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String district_name = sharedpreferences.getString("district", "NA");
        startJsonRequest(district_name);

        dialog = new Dialog(getContext());
        checkbox_status = new Boolean[12];

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter);
        publi = (CheckBox) dialog.findViewById(R.id.publi);
        privat = (CheckBox) dialog.findViewById(R.id.privat);
        hos = (CheckBox) dialog.findViewById(R.id.hos);
        collg = (CheckBox) dialog.findViewById(R.id.med);
        clinic = (CheckBox) dialog.findViewById(R.id.clinic);
        ayurv = (CheckBox) dialog.findViewById(R.id.ayurv);
        ayush = (CheckBox) dialog.findViewById(R.id.ayush);
        homeo = (CheckBox) dialog.findViewById(R.id.homeo);
        neuro = (CheckBox) dialog.findViewById(R.id.neuro);
        unani = (CheckBox) dialog.findViewById(R.id.unani);
        allo = (CheckBox) dialog.findViewById(R.id.allo);
        yoga = (CheckBox) dialog.findViewById(R.id.yoga);
        apply = (Button) dialog.findViewById(R.id.ok);

        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getContext(), Maps.class);
                //i.putExtra("data",info_local);
                //startActivity(i);
                GPSTracker gps = new GPSTracker(getContext());
                if (isOnline()) {
                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(latitude, longitude, 1);
                            String gpscity = null;
                            if (addresses.size() > 0)
                                gpscity = addresses.get(0).getLocality();
                            String city = gpscity;
                            Toast.makeText(getContext(), "cordinates :" + latitude + "," + longitude + "city :" + city, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        gps.showSettingsAlert();
                    }
                }

            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startJsonRequest(district_name);
            }
        });
        return view;
    }

    private void populate(ArrayList<Data> info) {
        reload.setVisibility(View.GONE);
        rvAdapter = new RecyclerViewAdapterForHospitals(getContext(), info);
        hospital_list.setAdapter(rvAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        hospital_list.setLayoutManager(linearLayoutManager);
        dia.cancel();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void startJsonRequest(String district) {
        district = district.replaceAll(" ", "%20");
        dia = new ProgressDialog(getContext());
        dia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dia.setMessage("Loading Hospitals...");
        dia.setIndeterminate(true);
        dia.setCancelable(false);
        dia.show();
        requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, UrlEndpoints.getUrlCurrent(district), (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJsonResponse(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error" + error.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("flag", error+"");
                dia.dismiss();
                reload.setVisibility(View.VISIBLE);
            }

        });
        requestQueue.add(request);
    }

    public void parseJsonResponse(JSONObject response) {
        if (response == null || response.length() == 0) {
            return;
        }
        try {
            JSONArray records = response.getJSONArray("records");
            for (int i = 0; i < records.length(); i++) {
                Data data = new Data();
                JSONObject object = records.getJSONObject(i);
                String Hospitalname = object.getString("Hospitalname");
                String HospitalCategory = object.getString("HospitalCategory");
                String Hostipalcaretype = object.getString("Hostipalcaretype");
                String SystemsOfMedicine = object.getString("SystemsOfMedicine");
                String AddressFirstLine = object.getString("AddressFirstLine");
                String State = object.getString("State");
                String District = object.getString("District");
                String Pincode = object.getString("Pincode");
                String Telephone = object.getString("Telephone");
                String Mobilenumber = object.getString("Mobilenumber");
                String Emergencynum = object.getString("Emergencynum");
                String Ambulancephoneno = object.getString("Ambulancephoneno");
                String Tollfree = object.getString("Tollfree");
                String Helpline = object.getString("Helpline");
                String Hospitalfax = object.getString("Hospitalfax");
                String Hospitalprimaryemailid = object.getString("Hospitalprimaryemailid");
                String Website = object.getString("Website");
                String Googlemapcorridinatelati = object.getString("Googlemapcorridinatelati");
                String Googlemapcorridinatelongi = object.getString("Googlemapcorridinatelongi");
                String Facilities = object.getString("Facilities");
                String Totalnumofbeds = object.getString("Totalnumofbeds");
                String speciality = object.getString("Specialties");

                data.Hospitalname = Hospitalname;
                data.HospitalCategory = HospitalCategory;
                data.Hostipalcaretype = Hostipalcaretype;
                data.SystemsOfMedicine = SystemsOfMedicine;
                data.AddressFirstLine = AddressFirstLine;
                data.State = State;
                data.District = District;
                data.Pincode = Pincode;
                data.Telephone = Telephone;
                data.Mobilenumber = Mobilenumber;
                data.Emergencynum = Emergencynum;
                data.Ambulancephoneno = Ambulancephoneno;
                data.Tollfree = Tollfree;
                data.Helpline = Helpline;
                data.Hospitalfax = Hospitalfax;
                data.Hospitalprimaryemailid = Hospitalprimaryemailid;
                data.Website = Website;
                data.Googlemapcorridinatelati = Googlemapcorridinatelati;
                data.Googlemapcorridinatelongi = Googlemapcorridinatelongi;
                data.Facilities = Facilities;
                data.Totalnumofbeds = Totalnumofbeds;
                data.Specialties = speciality;
                info.add(data);
                info_local.add(data);
            }
            populate(info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            startActivity(new Intent(getActivity(), Search.class));
        }
        if (id == R.id.filter) {
            dialog.show();
            sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            checkbox_status[0] = sharedpreferences.getBoolean("publi", true);
            checkbox_status[1] = sharedpreferences.getBoolean("privat", true);
            checkbox_status[2] = sharedpreferences.getBoolean("hos", true);
            checkbox_status[3] = sharedpreferences.getBoolean("collg", true);
            checkbox_status[4] = sharedpreferences.getBoolean("clinic", true);
            checkbox_status[5] = sharedpreferences.getBoolean("ayurv", true);
            checkbox_status[6] = sharedpreferences.getBoolean("ayush", true);
            checkbox_status[7] = sharedpreferences.getBoolean("homeo", true);
            checkbox_status[8] = sharedpreferences.getBoolean("neuro", true);
            checkbox_status[9] = sharedpreferences.getBoolean("unani", true);
            checkbox_status[10] = sharedpreferences.getBoolean("allo", true);
            checkbox_status[11] = sharedpreferences.getBoolean("yoga", true);

            publi.setChecked(checkbox_status[0]);
            privat.setChecked(checkbox_status[1]);
            hos.setChecked(checkbox_status[2]);
            collg.setChecked(checkbox_status[3]);
            clinic.setChecked(checkbox_status[4]);
            ayurv.setChecked(checkbox_status[5]);
            ayush.setChecked(checkbox_status[6]);
            homeo.setChecked(checkbox_status[7]);
            neuro.setChecked(checkbox_status[8]);
            unani.setChecked(checkbox_status[9]);
            allo.setChecked(checkbox_status[10]);
            yoga.setChecked(checkbox_status[11]);

            publi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[0] = !checkbox_status[0];
                }
            });
            privat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[1] = !checkbox_status[1];
                }
            });
            hos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[2] = !checkbox_status[2];
                }
            });
            collg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[3] = !checkbox_status[3];
                }
            });
            clinic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[4] = !checkbox_status[4];
                }
            });
            ayurv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[5] = !checkbox_status[5];
                }
            });
            ayush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[6] = !checkbox_status[6];
                }
            });
            homeo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[7] = !checkbox_status[7];
                }
            });
            neuro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[8] = !checkbox_status[8];
                }
            });
            unani.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[9] = !checkbox_status[9];
                }
            });
            allo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[10] = !checkbox_status[10];
                }
            });
            yoga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkbox_status[11] = !checkbox_status[11];
                }
            });
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor e = sharedpreferences.edit();
                    e.putBoolean("publi", checkbox_status[0]);
                    e.putBoolean("privat", checkbox_status[1]);
                    e.putBoolean("hos", checkbox_status[2]);
                    e.putBoolean("collg", checkbox_status[3]);
                    e.putBoolean("clinic", checkbox_status[4]);
                    e.putBoolean("ayurv", checkbox_status[5]);
                    e.putBoolean("ayush", checkbox_status[6]);
                    e.putBoolean("homeo", checkbox_status[7]);
                    e.putBoolean("neuro", checkbox_status[8]);
                    e.putBoolean("unani", checkbox_status[9]);
                    e.putBoolean("allo", checkbox_status[10]);
                    e.putBoolean("yoga", checkbox_status[11]);
                    e.commit();
                    dialog.dismiss();
                    populateAgain();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateAgain() {
        info_local.clear();
        for (int i = 0; i < info.size(); i++) {
            if (checkbox_status[0] == false && info.get(i).HospitalCategory.toLowerCase().contains("public"))
                continue;
            if (checkbox_status[1] == false && info.get(i).HospitalCategory.toLowerCase().contains("private"))
                continue;
            if (checkbox_status[2] == false && info.get(i).Hostipalcaretype.toLowerCase().contains("hospital"))
                continue;
            if (checkbox_status[3] == false && info.get(i).Hostipalcaretype.toLowerCase().contains("college"))
                continue;
            if (checkbox_status[4] == false && info.get(i).Hostipalcaretype.toLowerCase().contains("clinic"))
                continue;
            if (checkbox_status[5] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("ayurveda"))
                continue;
            if (checkbox_status[6] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("ayush"))
                continue;
            if (checkbox_status[7] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("homeopathy"))
                continue;
            if (checkbox_status[8] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("naturopathy"))
                continue;
            if (checkbox_status[9] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("unani"))
                continue;
            if (checkbox_status[10] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("allopathic"))
                continue;
            if (checkbox_status[11] == false && info.get(i).SystemsOfMedicine.toLowerCase().contains("yoga"))
                continue;
            info_local.add(info.get(i));
        }
        populate(info_local);
    }

    @Override
    public void onDestroy() {
        SharedPreferences.Editor e = sharedpreferences.edit();
        e.putBoolean("publi", true);
        e.putBoolean("privat", true);
        e.putBoolean("hos", true);
        e.putBoolean("collg", true);
        e.putBoolean("clinic", true);
        e.putBoolean("ayurv", true);
        e.putBoolean("ayush", true);
        e.putBoolean("homeo", true);
        e.putBoolean("neuro", true);
        e.putBoolean("unani", true);
        e.putBoolean("allo", true);
        e.putBoolean("yoga", true);
        e.commit();
        super.onDestroy();
    }
}
