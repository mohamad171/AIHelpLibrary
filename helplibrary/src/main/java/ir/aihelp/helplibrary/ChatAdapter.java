package ir.aihelp.helplibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mohamad on 10/30/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    List<ChatModel> chatModelList = new ArrayList<>();
    Context context;
    Profile pro;

    public ChatAdapter(Context context) {

        this.context = context;

    }
    public void setData(List<ChatModel> chatModelList){
        this.chatModelList = chatModelList;

    }


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_card_layout,parent,false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
    final ChatModel chatModel = chatModelList.get(position);
        if (chatModel.getUsername().equals(pro.username)){
            holder.mainchatlayout.setGravity(Gravity.RIGHT);
            holder.mainchatlayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }else{
            holder.mainchatlayout.setGravity(Gravity.LEFT);
            holder.mainchatlayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        holder.messagetext.setText(chatModel.getChattext());
        holder.datetext.setText(chatModel.getDatetext());


    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView messagetext;
        TextView datetext;
        LinearLayout mainchatlayout;
        public ChatViewHolder(View itemView) {
            super(itemView);
            messagetext = (TextView) itemView.findViewById(R.id.chattext);
            datetext = (TextView) itemView.findViewById(R.id.chatdatetxt);
            mainchatlayout = (LinearLayout) itemView.findViewById(R.id.chatlayout);
        }
    }
}
