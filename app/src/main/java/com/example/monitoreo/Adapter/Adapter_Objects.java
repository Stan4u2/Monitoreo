package com.example.monitoreo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Element;
import com.example.monitoreo.data.model.Section;

import java.util.ArrayList;

public class Adapter_Objects extends RecyclerView.Adapter<Adapter_Objects.ViewHolderObjects> implements View.OnClickListener {

    ArrayList<Element> listElement;
    ArrayList<Area> listArea;
    ArrayList<Section> listSection;

    private View.OnClickListener listener;

    public Adapter_Objects(ArrayList<Element> listElement, ArrayList<Area> listArea, ArrayList<Section> listSection){
        this.listElement = listElement;
        this.listArea = listArea;
        this.listSection = listSection;
    }

    @NonNull
    @Override
    public Adapter_Objects.ViewHolderObjects onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_objects, null, false);

        view.setOnClickListener(this);

        return new ViewHolderObjects(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Objects.ViewHolderObjects holder, int position) {
        String Area = "", Section = "";
        for(int i = 0; i<listArea.size(); i++){
            if(listArea.get(i).getId() == listElement.get(position).getAreaID()){
                Area = listArea.get(i).getName();
            }
        }
        for(int i = 0; i<listSection.size(); i++){
            System.out.println("No");
            if(listSection.get(i).getId() == listElement.get(position).getSectionID()){
                Section = listSection.get(i).getName();
            }
        }

        holder.item_list_element.setText(listElement.get(position).getDescriptor());
        holder.item_list_area.setText(Area);
        holder.item_list_section.setText(Section);

        if(listElement.get(position).getState()){
            //Set icon if its active
            //holder.purchasePaidList.setImageResource(R.drawable.ic_pagado);
        }else{
            //Set icon if its inactive
            //holder.purchasePaidList.setImageResource(R.drawable.ic_pagado);
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("Size: " + listElement.size());
        return listElement.size();
    }

    public class ViewHolderObjects extends RecyclerView.ViewHolder {
        TextView item_list_element, item_list_area, item_list_section;
        ImageView ElementState;
        public ViewHolderObjects(@NonNull View itemView) {
            super(itemView);
            //Text View
            item_list_element = itemView.findViewById(R.id.item_list_element);
            item_list_area = itemView.findViewById(R.id.item_list_area);
            item_list_section = itemView.findViewById(R.id.item_list_section);
            //ImageView
            ElementState = itemView.findViewById(R.id.ElementState);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }
}
