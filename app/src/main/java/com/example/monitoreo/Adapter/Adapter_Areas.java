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

import com.example.monitoreo.AddArea;
import com.example.monitoreo.MainActivity;
import com.example.monitoreo.MainMenu;
import com.example.monitoreo.R;
import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (listArea.get(position).getState()) {
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

    public void setOnClickListener(View.OnClickListener listener) {
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

        public ViewHolderAreas(@NonNull final View itemView) {
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

                    Intent intent = new Intent(v.getContext(), AddArea.class);
                    Bundle bundle = new Bundle();
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
                    Call<Area> call = mAPIService.countElementsInArea(MainActivity.tokenAuth, listArea.get(i).getId());

                    call.enqueue(new Callback<Area>() {
                        @Override
                        public void onResponse(Call<Area> call, Response<Area> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getCount() > 0) {
                                    showAlert();
                                } else {
                                    checkIfSectionsExistsArea(i);
                                }
                            } else {
                                Toast.makeText(itemView.getContext(), "Error al eliminar la area", Toast.LENGTH_LONG).show();
                                Log.e("Count Elements in Area", "onFailure: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Area> call, Throwable t) {
                            Log.e("Count Elements in Area", "onFailure: " + t.getMessage());
                        }
                    });
                }
            });
        }

        public void checkIfSectionsExistsArea (final int i){
            APIService mAPIService = APIUtils.getAPIService();
            Call<Area> call = mAPIService.countSectionsInArea(MainActivity.tokenAuth, listArea.get(i).getId());

            call.enqueue(new Callback<Area>() {
                @Override
                public void onResponse(Call<Area> call, Response<Area> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCount() > 0) {
                            showAlert();
                        } else {
                            deleteAreaAlert(i);
                        }
                    } else {
                        Toast.makeText(itemView.getContext(), "Error al eliminar la area", Toast.LENGTH_LONG).show();
                        Log.e("Count Sections in Area", "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Area> call, Throwable t) {
                    Log.e("Count Sections in Area", "onFailure: " + t.getMessage());
                }
            });
        }

        public void deleteArea(int i) {
            APIService mAPIService = APIUtils.getAPIService();
            Call<ResponseBody> call = mAPIService.deleteArea(MainActivity.tokenAuth, listArea.get(i).getId());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(itemView.getContext(), "Area Eliminada", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(itemView.getContext(), MainMenu.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("window", "areas");
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Error al eliminar la area", Toast.LENGTH_LONG).show();
                        Log.e("Delete Area", "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Error al eliminar la area", Toast.LENGTH_LONG).show();
                    Log.e("Delete Area", "onFailure: " + t.getMessage());
                }
            });
        }

        public void showAlert() {
            AlertDialog alertDialog = new AlertDialog.Builder(itemView.getContext()).create();
            alertDialog.setTitle("Area En Uso");
            alertDialog.setMessage("Area esta siendo usada, elimine todas relaciones.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
            alertDialog.show();
        }

        public void deleteAreaAlert(final int pos) {
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle("Eliminar Area")
                    .setMessage("Esta seguro de eliminar esta area?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteArea(pos);
                        }
                    })
                    .setNegativeButton("NO", null)
                    .setIcon(R.drawable.ic_error_black_24dp)
                    .show();
        }
    }
}
