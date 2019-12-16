package com.example.monitoreo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.MainActivity;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Readings;
import com.example.monitoreo.data.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Adapter_Readings extends RecyclerView.Adapter<Adapter_Readings.ViewHolderReadings> implements View.OnClickListener{

    ArrayList<Readings> listReadings;
    ArrayList<User> listUsers;

    private View.OnClickListener listener;

    public Adapter_Readings(ArrayList<Readings> listReadings, ArrayList<User> listUsers) {
        this.listReadings = listReadings;
        this.listUsers = listUsers;
    }

    @NonNull
    @Override
    public Adapter_Readings.ViewHolderReadings onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_readings, null, false);
        view.setOnClickListener(this);
        return new ViewHolderReadings(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Readings.ViewHolderReadings holder, int position) {
        if (Integer.valueOf(listUsers.get(position).getId()) == 0){
            holder.item_list_name_person_reading.setText("Automatico");
        }else {
            if(MainActivity.userID == 2) {
                holder.item_list_name_person_reading.setText(listUsers.get(position).getName());
            } else {
                if (Integer.valueOf(listUsers.get(position).getId()) == MainActivity.userID){
                    holder.item_list_name_person_reading.setText("Realizada por el usuario actual.");
                } else {
                    holder.item_list_name_person_reading.setText("Realizada por otro usuario.");
                }
            }
        }

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);

        String inputDate = listReadings.get(position).getTime();
        try {
            Date date = inputFormat.parse(inputDate);
            String outputDate = outputFormat.format(date);
            holder.item_list_time_reading.setText(outputDate);
        } catch (ParseException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listReadings.size();
    }

    public class ViewHolderReadings extends RecyclerView.ViewHolder {

        TextView item_list_name_person_reading, item_list_time_reading;
        public ViewHolderReadings(@NonNull View itemView) {
            super(itemView);

            //Text View
            item_list_name_person_reading = itemView.findViewById(R.id.item_list_name_person_reading);
            item_list_time_reading = itemView.findViewById(R.id.item_list_time_reading);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }
}
