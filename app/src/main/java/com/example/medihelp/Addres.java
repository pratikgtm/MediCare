package com.example.medihelp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Addres extends Fragment {
    Spinner state, district;
    String state_name, district_name;
    SharedPreferences sharedpreferences;
    String[] state_list = InputAddress.STATES;
    String[][] district_list = InputAddress.DISTRICTS;
    int state_pos = 0;
    int district_pos = 0;
    LinearLayout district_layout;
    ArrayAdapter<String> adapter, adapter2;
    Button OK;
    String FIRSTTIME = "true";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.addres, container, false);
        state = (Spinner) view.findViewById(R.id.user_state);
        district = (Spinner) view.findViewById(R.id.user_district);
        district_layout = (LinearLayout) view.findViewById(R.id.district_layout);
        OK = (Button) view.findViewById(R.id.OK);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        state_name = sharedpreferences.getString("state", "NA");
        district_name = sharedpreferences.getString("district", "NA");
        FIRSTTIME = sharedpreferences.getString("first", "true");

        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.address_spinner_item, state_list);
        state.setAdapter(adapter);
        state.setSelection(location(state_name));
        if (FIRSTTIME.equals("false")) {
            district_layout.setVisibility(View.VISIBLE);
            adapter2 = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.address_spinner_item, district_list[state_pos]);
            district.setAdapter(adapter2);
            district.setSelection(location2(district_name));
        }
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                state_pos = pos;
                if (pos!=0) {
                    district_layout.setVisibility(View.VISIBLE);
                    adapter2 = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.address_spinner_item, district_list[state_pos]);
                    district.setAdapter(adapter2);
                    district.setSelection(location2(district_name));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                district_pos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state_pos != 0 && district_pos != 0) {
                    SharedPreferences.Editor e = sharedpreferences.edit();
                    e.putString("state", state_list[state_pos]);
                    e.putString("district", district_list[state_pos][district_pos]);
                    e.putString("first", "false");
                    e.commit();
                    Toast.makeText(getContext(),"Location saved !",Toast.LENGTH_SHORT).show();
                    Fragment fragment = new Hospitals();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.mainContent, fragment).commit();
                }else{
                    Toast.makeText(getContext(),"First select State and District !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private int location2(String district_name) {
        int pos = 0;
        if (district_name.equals("NA")) return 0;
        for (int i = 0; i < district_list[state_pos].length; i++) {
            if (district_name.equals(district_list[state_pos][i])) pos = i;
        }
        district_pos=pos;
        return pos;
    }

    private int location(String state_name) {
        int pos = 0;
        if (state_name.equals("NA")) return 0;
        for (int i = 0; i < state_list.length; i++) {
            if (state_name.equals(state_list[i])) pos = i;
        }
        state_pos=pos;
        return pos;
    }
}
