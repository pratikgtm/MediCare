package com.example.medihelp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapterForHospitals extends RecyclerView.Adapter<RecyclerViewAdapterForHospitals.MyViewHolder> {

    private LayoutInflater inflater;
    ArrayList<Data> data = new ArrayList<>();
    Data current;
    private Context context;
    public RecyclerViewAdapterForHospitals(Context context, ArrayList<Data> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.hospital_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        current = new Data();
        current=data.get(position);
        holder.name.setText(current.Hospitalname);
        holder.address.setText(current.AddressFirstLine+",\n"+current.District+" - "+current.Pincode+",\n"+current.State+".");
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Bold.otf" );
        holder.name.setTypeface(customFont);
        holder.address.setTypeface(customFont);

        if(current.Hostipalcaretype.equals("Hospital")){
            holder.careType.setImageResource(R.drawable.hospital);
            holder.subtitle.setText("Hospital");
        }else if(current.Hostipalcaretype.equals("Clinic")){
            holder.careType.setImageResource(R.drawable.clinic);
            holder.subtitle.setText("Clinic");
        }else {
            holder.careType.setImageResource(R.drawable.college);
            holder.subtitle.setText("College");
        }
        holder.subtitle.setTypeface(customFont);

        if(current.HospitalCategory.equals("Public")){
            holder.type.setImageResource(R.drawable.public_type);
        }else {
            holder.type.setImageResource(R.drawable.private_type);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView careType,type;
        TextView name,address,subtitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getPosition();
                    Intent i = new Intent(context,ViewHospital.class);
                    i.putExtra("data", data.get(pos));
                    context.startActivity(i);
                }
            });
            name = (TextView) itemView.findViewById(R.id.name);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            address = (TextView) itemView.findViewById(R.id.address);
            careType = (ImageView) itemView.findViewById(R.id.caretype);
            type = (ImageView) itemView.findViewById(R.id.type);
        }
    }
}