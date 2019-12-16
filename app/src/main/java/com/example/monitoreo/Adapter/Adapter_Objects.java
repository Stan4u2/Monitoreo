package com.example.monitoreo.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.AddElement;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.MainMenu;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Element;
import com.example.monitoreo.data.model.ReadingsDetails;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Objects extends RecyclerView.Adapter<Adapter_Objects.ViewHolderObjects> implements View.OnClickListener {

    ArrayList<Element> listElement;
    ArrayList<Area> listArea;
    ArrayList<Section> listSection;
    ArrayList<ReadingsDetails> listReadings;

    private View.OnClickListener listener;

    public Adapter_Objects(ArrayList<Element> listElement, ArrayList<Area> listArea, ArrayList<Section> listSection, ArrayList<ReadingsDetails> listReadings) {
        this.listElement = listElement;
        this.listArea = listArea;
        this.listSection = listSection;
        this.listReadings = listReadings;
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

        holder.item_list_element.setText(listElement.get(position).getDescriptor());
        holder.item_list_area.setText(listArea.get(position).getName());
        holder.item_list_section.setText(listSection.get(position).getName());
        if(listReadings!= null){
            if (listReadings.get(position).getRead()){
                holder.item_list_reading.setText("Si");
            } else {
                holder.item_list_reading.setText("No");
            }

        }

        if (listElement.get(position).getState()) {
            //Set background if its active
            holder.ElementState.setBackgroundResource(R.drawable.ic_ative);
        } else {
            //Set background if its inactive
            holder.ElementState.setBackgroundResource(R.drawable.ic_inactive);
        }
    }

    @Override
    public int getItemCount() {
        return listElement.size();
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

    public class ViewHolderObjects extends RecyclerView.ViewHolder {
        TextView item_list_element, item_list_area, item_list_section, item_list_reading;
        ImageView ElementState;
        ImageButton EditButton, DeleteButton;

        public ViewHolderObjects(@NonNull final View itemView) {
            super(itemView);
            //Text View
            item_list_element = itemView.findViewById(R.id.item_list_element);
            item_list_area = itemView.findViewById(R.id.item_list_area);
            item_list_section = itemView.findViewById(R.id.item_list_section);
            item_list_reading = itemView.findViewById(R.id.item_list_reading);
            //ImageView
            ElementState = itemView.findViewById(R.id.ElementState);
            //Image Button
            EditButton = itemView.findViewById(R.id.EditButton);
            DeleteButton = itemView.findViewById(R.id.DeleteButton);

            DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    deleteAreaAlert(i);

                }
            });

            EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    Element element = listElement.get(i);
                    Area area = listArea.get(i);
                    Section section = listSection.get(i);

                    Intent intent = new Intent(v.getContext(), AddElement.class);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("element", element);
                    bundle.putSerializable("area", area);
                    bundle.putSerializable("section", section);
                    bundle.putSerializable("action", "modify");

                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void deleteAreaAlert(final int pos) {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Eliminar Elemento")
                    .setMessage("Esta seguro de eliminar este elemento?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteElement(pos);
                        }
                    })
                    .setNegativeButton("NO", null)
                    .setIcon(R.drawable.ic_error_black_24dp)
                    .show();
        }

        public void deleteElement(int i) {
            APIService mAPIService = APIUtils.getAPIService();
            Call<ResponseBody> call = mAPIService.deleteElement(MainActivity.tokenAuth, listElement.get(i).getId());

            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(itemView.getContext(), "Elemento Eliminado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(itemView.getContext(), MainMenu.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("window", "objects");
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);

                    } else if (!response.isSuccessful()) {
                        Toast.makeText(itemView.getContext(), "Error al eliminar elemento", Toast.LENGTH_LONG).show();
                        Log.e("Delete Element", "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Delete Element", "onFailure: " + t.getMessage());
                }
            });
        }
    }
}
