package android.dms.aut.ac.nz.testapptracking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity
{
    Button clickBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickBegin = (Button) findViewById(R.id.beginButton);

        clickBegin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                    if (isNetworkAvailable(MainActivity.this))
                    {
                        beginStartActivity();
                    }

                    if (!isNetworkAvailable(MainActivity.this))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder
                                .setMessage("No internet connection on your device. Would you like to enable it?")
                                .setTitle("No Internet Connection")
                                .setCancelable(false)
                                .setPositiveButton(" Enable Internet ",
                                        new DialogInterface.OnClickListener()
                                        {

                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                Intent dialogIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(dialogIntent);
                                            }
                                        });

                        builder.setNegativeButton(" Cancel ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
            }
        });
    }

    public void clickExit(View v)
    {
        finish();
        MainActivity.this.finish();
        finish();
    }

    public void beginStartActivity()
    {
        finish();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you wish to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                MainActivity.this.finish();
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isNetworkAvailable(Activity activity)
    {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
