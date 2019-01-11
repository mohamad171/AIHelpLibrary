package ir.aihelp.library;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ir.aihelp.helplibrary.AIConnect;


public class MainActivity extends AppCompatActivity {

    Button go_chat;

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go_chat = findViewById(R.id.go_chat);
        final AIConnect aiConnect = new AIConnect(MainActivity.this);
        aiConnect.AIinit("1234","Test");
        go_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aiConnect.AIopenChat();
            }
        });

    }

}
