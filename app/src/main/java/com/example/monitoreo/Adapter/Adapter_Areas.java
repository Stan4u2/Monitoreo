package com.example.monitoreo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;

import java.util.ArrayList;

public class Adapter_Areas extends RecyclerView.Adapter<Adapter_Areas.ViewHolderAreas> implements View.OnClickListener {

    ArrayList<Area> listArea;

    private View.OnClickListener listener;

    public Adapter_Areas(ArrayList<Area> listArea) {
        this.listArea = listArea;
    }

    @NonNull
    @Override
    public Adapter_Areas.ViewHolderAreas onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_areas, null, false);

        view.setOnClickListener(this);

        return new ViewHolderAreas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Areas.ViewHolderAreas holder, int position) {
        holder.item_list_area.setText(listArea.get(position).getName());

        if(listArea.get(position).getState()){
            //Set background if its active
            holder.AreaState.setBackgroundResource(R.drawable.ic_ative);
        } else {
            //Set background if its inactive
            holder.AreaState.setBackgroundResource(R.drawable.ic_inactive);
        }
    }

    @Override
    public int getItemCount() {
        return listArea.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class ViewHolderAreas extends RecyclerView.ViewHolder {
        TextView item_list_area;
        ImageView AreaState;
        ImageButton EditButton, DeleteButton;

        public ViewHolderAreas(@NonNull View itemView) {
            super(itemView);
            //Text View
            item_list_area = itemView.findViewById(R.id.item_list_area);
            //ImageView
            AreaState = itemView.findViewById(R.id.AreaState);
            //ImageButton
            EditButton = itemView.findViewById(R.id.EditButton);
            DeleteButton = itemView.findViewById(R.id.DeleteButton);

            EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    Area area = listArea.get(i);
                }
            });

            DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();

                }
            });
        }
    }
}
