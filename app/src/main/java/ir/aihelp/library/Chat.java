package ir.aihelp.library;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {


    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }
        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("Errorrrrrrr", "IndexOutOfBoundsException in RecyclerView happens");
            }
        }
    }
    EditText messagetext;
    ImageView btnsend;
    RecyclerView chatrecyclerview;
    Socket socket;
    ChatAdapter adapter;
    List<ChatModel> chatModels = new ArrayList<>();
    Profile pro = new Profile();
    ConnectionChecker connectionChecker;
    SpinKitView chatspin_kit;
    boolean isFill = false;
    TextView tracking_text;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        btnsend = (ImageView) view.findViewById(R.id.btnsend);
        messagetext = (EditText) view.findViewById(R.id.editText);
        chatrecyclerview = (RecyclerView) view.findViewById(R.id.chat_recycler);
        chatspin_kit = (SpinKitView) view.findViewById(R.id.chatspin_kit);
        tracking_text = view.findViewById(R.id.tracking_text);
        final WrapContentLinearLayoutManager wrapLinearLayout = new WrapContentLinearLayoutManager(getActivity());
        chatrecyclerview.setLayoutManager(wrapLinearLayout);
        adapter = new ChatAdapter(getActivity());
        chatrecyclerview.setAdapter(adapter);
        try{
            socket = IO.socket("http://192.168.1.100:4000");
        }catch (URISyntaxException ex){
            Log.wtf("errrrrrr",ex.toString());
        }
        socket.emit("chats");
        messagetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = messagetext.getText().toString();
                if (msg.trim().length() > 0){
                    isFill = true;

                    btnsend.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_send));
                }else{
                    isFill = false;
                    btnsend.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_file));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFill){
                    try{
                        JSONObject obj = new JSONObject();
                        obj.put("message", messagetext.getText().toString());
                        obj.put("username", "mohamad171");
                        socket.emit("message",obj );
                        messagetext.setText("");
                    }catch (JSONException ex){

                    }
                }

            }
        });

        socket.on("client_message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                try {
                    final JSONObject obj = (JSONObject) args[0];
                    chatModels.add(new ChatModel(obj.getString("username"),obj.getString("message"),"12:32"));
                    adapter.setData(chatModels);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemInserted(chatModels.size());
                            chatrecyclerview.scrollToPosition(chatModels.size() - 1);
                        }
                    });
                } catch (JSONException ex) {
                    Log.wtf("errrrrr", ex.toString());
                }

            }
        });

        socket.on("getchats", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {

                try {
                    final JSONObject obj = (JSONObject) args[0];
                    Log.wtf("errrrrr",obj.getString("data"));
                    JSONArray arr = new JSONArray(obj.getString("data"));
                    for(int i = 0 ; i < arr.length();i++){
                        JSONObject c = arr.getJSONObject(i);
                        chatModels.add(new ChatModel(c.getString("from"),c.getString("message"),"14:22"));
                        adapter.setData(chatModels);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatspin_kit.setVisibility(View.GONE);
                            adapter.notifyItemInserted(chatModels.size());
                            chatrecyclerview.scrollToPosition(chatModels.size() - 1);
                        }
                    });
                } catch (JSONException ex) {
                    Log.wtf("errrrrr", ex.toString());
                }

            }
        });
                if (!socket.connected()){
                    socket.connect();
                    try{
                        JSONObject object = new JSONObject();
                        object.put("deviceid","device");

                        socket.emit("config", object, new Ack() {
                            @Override
                            public void call(final Object... args) {

                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try{
                                                    JSONObject obj = new JSONObject(String.valueOf(args[0]));
                                                    if(obj.getString("status").equals("ok")){
                                                        tracking_text.setText("شماره پیگری: "+obj.getString("trackingCode"));
                                                        chatspin_kit.setVisibility(View.GONE);
                                                    }

                                                }catch (JSONException jsexcp){

                                                }


                                            }
                                        });



                            }
                        });

                    }catch (JSONException jex){

                    }


                }else{
                    Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
                }




        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        connectionChecker = new ConnectionChecker();
        getActivity().registerReceiver(connectionChecker,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(connectionChecker);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        socket.emit("userdisconnect");
        socket.disconnect();

    }
    public class ConnectionChecker extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
         //   messagetext.setEnabled(false);
           // btnsend.setEnabled(false);
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if(isConnected){
                messagetext.setEnabled(true);
                 btnsend.setEnabled(true);
            }else{
                messagetext.setEnabled(false);
                 btnsend.setEnabled(false);
            }
        }
    }
}
