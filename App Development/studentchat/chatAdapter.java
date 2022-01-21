package com.biswa1045.studentchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ChatViewAdapter> {
    List<chatData> list;
    Context context;
    int bat=1;
    public chatAdapter(List<chatData> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public ChatViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item,parent,false);
        return new ChatViewAdapter(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewAdapter holder, int position) {

        chatData currentItem = list.get(position);

        holder.name.setText(currentItem.getName());
        holder.msg.setText(currentItem.getMsg());
        holder.date.setText(currentItem.getDate());



    }

    @Override
    public int getItemCount() {

           return list.size();

    }

    public class ChatViewAdapter extends RecyclerView.ViewHolder {

TextView name,msg,date;

        public ChatViewAdapter(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.chat_name);
            msg=itemView.findViewById(R.id.chat_msg);
            date=itemView.findViewById(R.id.chat_date);

        }
    }

}
