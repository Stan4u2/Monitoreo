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

import com.example.monitoreo.AddSection;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.MainMenu;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Sections extends RecyclerView.Adapter<Adapter_Sections.ViewHolderSections> implements View.OnClickListener {

    ArrayList<Section> listSection;
    ArrayList<Area> listArea;

    private View.OnClickListener listener;

    public Adapter_Sections(ArrayList<Section> listSection, ArrayList<Area> listArea) {
        this.listSection = listSection;
        this.listArea = listArea;
    }

    @NonNull
    @Override
    public Adapter_Sections.ViewHolderSections onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_sections, null, false);

        view.setOnClickListener(this);

        return new ViewHolderSections(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Sections.ViewHolderSections holder, int position) {
        holder.item_list_section.setText(listSection.get(position).getName());
        holder.item_list_area.setText(listArea.get(position).getName());

        if (listSection.get(position).getState()) {
            //Set background if its active
            holder.SectionState.setBackgroundResource(R.drawable.ic_ative);
        } else {
            //Set background if its inactive
            holder.SectionState.setBackgroundResource(R.drawable.ic_inactive);
        }
    }

    @Override
    public int getItemCount() {
        return listSection.size();
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

    public class ViewHolderSections extends RecyclerView.ViewHolder {
        TextView item_list_section, item_list_area;
        ImageView SectionState;
        ImageButton EditButton, DeleteButton;

        public ViewHolderSections(@NonNull final View itemView) {
            super(itemView);
            //TextView
            item_list_section = itemView.findViewById(R.id.item_list_section);
            item_list_area = itemView.findViewById(R.id.item_list_area);
            //ImageView
            SectionState = itemView.findViewById(R.id.SectionState);
            //ImageButton
            EditButton = itemView.findViewById(R.id.EditButton);
            DeleteButton = itemView.findViewById(R.id.DeleteButton);

            EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = getAdapterPosition();
                    Section section = listSection.get(i);
                    Area area = listArea.get(i);

                    Intent intent = new Intent(v.getContext(), AddSection.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("section", section);
                    bundle.putSerializable("area", area);
                    bundle.putSerializable("action", "modify");
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = getAdapterPosition();
                    APIService mAPIService = APIUtils.getAPIService();
                    Call<Section> call = mAPIService.countElementsInSection(MainActivity.tokenAuth, listSection.get(i).getId());

                    call.enqueue(new Callback<Section>() {
                        @Override
                        public void onResponse(Call<Section> call, Response<Section> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getCount() > 0){
                                    showAlert();
                                } else {
                                    deleteSectionAlert(i);
                                }
                            } else {
                                Toast.makeText(itemView.getContext(), "Error al eliminar la sección", Toast.LENGTH_LONG).show();
                                Log.e("Count Elements in Section", "onFailure: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Section> call, Throwable t) {
                            Log.e("Count Elements in Section", "onFailure: " + t.getMessage());
                        }
                    });
                }
            });
        }

        public void deleteSection(int i) {
            APIService mAPIService = APIUtils.getAPIService();
            Call<ResponseBody> call = mAPIService.deleteSection(MainActivity.tokenAuth, listSection.get(i).getId());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(itemView.getContext(), "Secciòn Eliminada", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(itemView.getContext(), MainMenu.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("window", "sections");
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Error al eliminar la secciòn", Toast.LENGTH_LONG).show();
                        Log.e("Delete Section", "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Error al eliminar la secciòn", Toast.LENGTH_LONG).show();
                    Log.e("Delete Section", "onFailure: " + t.getMessage());
                }
            });
        }

        public void showAlert() {
            AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
            alertDialog.setTitle("Sección En Uso");
            alertDialog.setMessage("Sección esta siendo usada, elimine todas relaciones.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setIcon(android.R.drawable.ic_dialog_info);
            alertDialog.show();
        }

        public void deleteSectionAlert(final int pos) {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Eliminar Sección")
                    .setMessage("Esta seguro de eliminar esta sección?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteSection(pos);
                        }
                    })
                    .setNegativeButton("NO", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }
}
