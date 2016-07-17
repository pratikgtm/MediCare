package com.example.medihelp;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends Fragment {
	 
	@Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.reach_us, container, false);
		TextView dev1 = (TextView) view.findViewById(R.id.developer1);
		TextView dev2 = (TextView) view.findViewById(R.id.developer2);
		ImageView fb1 = (ImageView) view.findViewById(R.id.fb1);
		ImageView fb2 = (ImageView) view.findViewById(R.id.fb2);
		ImageView mail1 = (ImageView) view.findViewById(R.id.mail1);
		ImageView mail2 = (ImageView) view.findViewById(R.id.mail2);
		Typeface customFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/Quicksand-Light.otf");
		dev1.setTypeface(customFont);
		dev2.setTypeface(customFont);

		fb1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url="https://www.facebook.com/arunim.chopra";
				try {
					getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href="+url));
					startActivity(i);
				} catch (Exception e) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(i);
				}
			}
		});
		fb2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url="https://www.facebook.com/pratik.goutam";
				try {
					getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href="+url));
					startActivity(i);
				} catch (Exception e) {
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(i);
				}
			}
		});

		mail1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String[]addr={"arunim29@gmail.com"};
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL,addr);
				intent.putExtra(Intent.EXTRA_SUBJECT, "About MediCare");
				startActivity(Intent.createChooser(intent, "Send Email"));
			}
		});
		mail2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String[]addr={"pratikgtm@gmail.com"};
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, addr);
				intent.putExtra(Intent.EXTRA_SUBJECT, "About MediCare");
				startActivity(Intent.createChooser(intent, "Send Email"));
			}
		});
		return view;
	 }

}
