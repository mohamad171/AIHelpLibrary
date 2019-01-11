package ir.aihelp.helplibrary;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ServerInterface serverInterface;

    @Override
    public void onBackPressed() {
        fragmentManager.popBackStack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aihelp_activity_main);
        setTheme(R.style.AppTheme_NoActionAIHelp);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.chat_frame,new MainFragment());
        fragmentTransaction.commit();

    }

}
