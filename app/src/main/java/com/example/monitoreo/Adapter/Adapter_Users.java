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

import com.example.monitoreo.MainActivity;
import com.example.monitoreo.MainMenu;
import com.example.monitoreo.R;
import com.example.monitoreo.SignUp;
import com.example.monitoreo.data.model.User;
import com.example.monitoreo.data.remote.APIService;
import com.example.monitoreo.data.remote.APIUtils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_Users extends RecyclerView.Adapter<Adapter_Users.ViewHolderUsers> implements View.OnClickListener {

    ArrayList<User> listUser;

    private View.OnClickListener listener;

    public Adapter_Users(ArrayList<User> listUser) {
        this.listUser = listUser;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Adapter_Users.ViewHolderUsers onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_users, null, false);
        view.setOnClickListener(this);
        return new ViewHolderUsers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Users.ViewHolderUsers holder, int position) {
        holder.item_list_name.setText(listUser.get(position).getName());
        holder.item_list_user.setText(listUser.get(position).getUsername());
        if (listUser.get(position).getAdmin()){
            holder.item_list_role.setText("Administrador");
        } else {
            holder.item_list_role.setText("Normal");
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class ViewHolderUsers extends RecyclerView.ViewHolder {
        TextView item_list_name, item_list_user, item_list_role;
        ImageView UserState;
        ImageButton EditButton, DeleteButton;

        public ViewHolderUsers(@NonNull final View itemView) {
            super(itemView);
            //TextView
            item_list_name = itemView.findViewById(R.id.item_list_name);
            item_list_user = itemView.findViewById(R.id.item_list_user);
            item_list_role = itemView.findViewById(R.id.item_list_role);
            //ImageView
            UserState = itemView.findViewById(R.id.UserState);
            //ImageButton
            EditButton = itemView.findViewById(R.id.EditButton);
            DeleteButton = itemView.findViewById(R.id.DeleteButton);

            EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = getAdapterPosition();
                    User user = listUser.get(i);

                    Intent intent = new Intent(v.getContext(), SignUp.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    bundle.putSerializable("action", "modify");
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            DeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int i = getAdapterPosition();
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Eliminar Usuario")
                            .setMessage("Esta seguro de eliminar este usuario?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteUser(i);
                                }
                            })
                            .setNegativeButton("NO", null)
                            .setIcon(R.drawable.ic_error_black_24dp)
                            .show();
                }
            });
        }

        private void deleteUser(final int pos){
            APIService mAPIService = APIUtils.getAPIService();
            Call<ResponseBody> call = mAPIService.deleteUser(MainActivity.tokenAuth, Integer.valueOf(listUser.get(pos).getId()));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(itemView.getContext(), "Usuario Eliminado", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(itemView.getContext(), MainMenu.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("window", "users");
                        intent.putExtras(bundle);
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Error al eliminar el usuario", Toast.LENGTH_LONG).show();
                        Log.e("Delete User", "onFailure: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
}
