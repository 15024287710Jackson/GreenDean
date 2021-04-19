package com.example.greendean;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder>{

    private ArrayList<User> mUsers;

    public ChooseAdapter(ArrayList<User> users) {
        mUsers = users;
    }

    @NonNull
    @Override
    public ChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_touxiang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.touxiang.setImageResource(user.getUserImg());
        holder.mingzi.setText(user.getUserName());


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //绑定

        ImageView touxiang;
        TextView mingzi;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            touxiang = itemView.findViewById(R.id.touxiang);
            mingzi = itemView.findViewById(R.id.mingzi);
        }
    }
}
