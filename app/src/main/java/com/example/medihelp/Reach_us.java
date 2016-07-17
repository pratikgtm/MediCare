package com.example.medihelp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Reach_us extends Fragment {
    String myPlan="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about, container, false);
        TextView about = (TextView) view.findViewById(R.id.textView_about);
        TextView share = (TextView) view.findViewById(R.id.share);
        ImageView share_button = (ImageView) view.findViewById(R.id.share_button);
        Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Light.otf");
        about.setTypeface(customFont);
        share.setTypeface(customFont);
        myPlan="Hey, check out this App \"MediCare\".";
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ShareActionProvider mshare = (ShareActionProvider) v.getActionProvider();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, myPlan);
                startActivity(i);
            }
        });
        return view;
    }

}
