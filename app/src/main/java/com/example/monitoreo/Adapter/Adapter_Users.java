package com.example.monitoreo.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.monitoreo.R;
import com.example.monitoreo.SignUp;
import com.example.monitoreo.data.model.User;

import java.util.ArrayList;

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

        public ViewHolderUsers(@NonNull View itemView) {
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
                    int i = getAdapterPosition();

                }
            });
        }
    }
}
