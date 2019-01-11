package ir.aihelp.helplibrary;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends Fragment  {


    public SaveFragment() {

    }
    EditText email_edt;
    Button email_btn;
    Button skip_btn;

    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);

        email_btn = view.findViewById(R.id.email_btn);
        email_edt = view.findViewById(R.id.email_edt);
        skip_btn = view.findViewById(R.id.aiskip_btn);


        sharedPreferences = getActivity().getSharedPreferences("AIHelp",Context.MODE_PRIVATE);
        if(sharedPreferences.contains("isComplete")){
            GoChat();
        }

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isComplete",true);
                editor.apply();
                GoChat();
            }
        });
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_edt.getText().toString().trim().length() > 0){
                    ServerInterface serverInterface = new ServerInterface(getActivity());
                    HashMap<String,String> params = new HashMap<>();
                    params.put("deviceid",Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
                    params.put("email",email_edt.getText().toString());
                    serverInterface.Post("/api/setEmail", params, new ServerInterface.responseListeneer() {
                        @Override
                        public void OnResponse(String content) {
                            Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
                            try{
                                JSONObject object = new JSONObject(content);
                                if(object.getString("status").equals("ok")){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isComplete",true);
                                    editor.apply();
                                    GoChat();
                                    Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException jsx){
                                Toast.makeText(getActivity(), "خطایی رخ داده است لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else
                    Toast.makeText(getActivity(), "لطفا ایمیل را وارد کنید", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void GoChat(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chat_frame, new Chat());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
