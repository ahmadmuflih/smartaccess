package com.a4nesia.baso.smartaccess.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a4nesia.baso.smartaccess.R;
import com.a4nesia.baso.smartaccess.models.Privilege;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Privilege> privileges;
    OnItemClickListener onItemClickListener;

    public RoomAdapter(Context context, ArrayList<Privilege> privileges, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.privileges = privileges;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View row=inflater.inflate(R.layout.view_privilege, parent, false);
        Holder holder = new Holder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Holder hold = new Holder(holder.itemView);
        final Privilege privilege = getItem(position);
        hold.txtRoomId.setText(privilege.getRoom().getCode());
        hold.txtRoomName.setText(privilege.getRoom().getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(privilege);
            }
        });
    }

    @Override
    public int getItemCount() {
        return privileges.size();
    }
    public Privilege getItem(int position){
        return privileges.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(Privilege privilege);
    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView txtRoomId, txtRoomName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            txtRoomId= itemView.findViewById(R.id.room_id);
            txtRoomName= itemView.findViewById(R.id.room_name);
        }
    }
}
