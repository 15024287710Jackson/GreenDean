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

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder>implements View.OnClickListener{

    private ArrayList<User> mUsers;
    private RecyclerView recyclerView;
    private OnItemClickListener onItemClickListener;



    public ChooseAdapter(ArrayList<User> users) {
        mUsers = users;
    }



    @NonNull
    @Override
    public ChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_touxiang,parent,false);
        view.setOnClickListener(this);
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {
        //根据RecyclerView获得当前View的位置
        int position = recyclerView.getChildAdapterPosition(view);
        //程序执行到此，会去执行具体实现的onItemClick()方法
        if (onItemClickListener!=null){
            onItemClickListener.onItemClick(recyclerView,view,position,mUsers.get(position));
        }
    }

    public interface OnItemClickListener{
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onItemClick(RecyclerView parent,View view, int position, User user);
    }


    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView= recyclerView;
    }
    /**
     *   将RecycleView从Adapter解除
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
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
