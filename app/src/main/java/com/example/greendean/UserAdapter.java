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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private ArrayList<User> mUsers;

    public UserAdapter(ArrayList<User> users) {
        mUsers = users;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        if (user.getUserMsg().isSystem()){//系统消息
            holder.mLinearLayoutSystem.setVisibility(View.VISIBLE);
            holder.mRelativeLayoutReceive.setVisibility(View.GONE);
            holder.mRelativeLayoutSend.setVisibility(View.GONE);
            holder.mTextViewSystemText.setText(user.getUserMsg().getContent());
        }else{
            if (user.getUserMsg().isSend()){//是发送消息
                holder.mLinearLayoutSystem.setVisibility(View.GONE);
                holder.mRelativeLayoutReceive.setVisibility(View.GONE);
                holder.mRelativeLayoutSend.setVisibility(View.VISIBLE);

                holder.mImageViewSend.setImageResource(user.getUserImg());
                holder.mTextViewNameSend.setText(user.getUserName());
                holder.mTextViewContentSend.setText(user.getUserMsg().getContent());
            }else { //接收的消息
                holder.mLinearLayoutSystem.setVisibility(View.GONE);
                holder.mRelativeLayoutReceive.setVisibility(View.VISIBLE);
                holder.mRelativeLayoutSend.setVisibility(View.GONE);

                holder.mImageViewReceive.setImageResource(user.getUserImg());
                holder.mTextViewNameReceive.setText(user.getUserName());
                holder.mTextViewContentReceive.setText(user.getUserMsg().getContent());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //绑定所有视图
        RelativeLayout mRelativeLayoutSend,mRelativeLayoutReceive;
        LinearLayout mLinearLayoutSystem;
        ImageView mImageViewReceive,mImageViewSend;
        TextView mTextViewNameReceive,mTextViewNameSend,mTextViewContentReceive,mTextViewContentSend,mTextViewSystemText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRelativeLayoutReceive = itemView.findViewById(R.id.receive_relative);
            mRelativeLayoutSend = itemView.findViewById(R.id.send_relative);
            mLinearLayoutSystem = itemView.findViewById(R.id.system_layout);

            mImageViewReceive = itemView.findViewById(R.id.image_receive);
            mImageViewSend = itemView.findViewById(R.id.image_send);

            mTextViewNameReceive = itemView.findViewById(R.id.text_name_receive);
            mTextViewNameSend = itemView.findViewById(R.id.text_name_send);
            mTextViewContentReceive = itemView.findViewById(R.id.text_content_receive);
            mTextViewContentSend = itemView.findViewById(R.id.text_content_send);
            mTextViewSystemText = itemView.findViewById(R.id.system_text);
        }
    }
}
