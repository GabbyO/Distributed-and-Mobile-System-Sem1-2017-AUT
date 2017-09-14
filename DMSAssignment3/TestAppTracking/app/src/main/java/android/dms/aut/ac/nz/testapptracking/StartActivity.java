package android.dms.aut.ac.nz.testapptracking;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StartActivity extends AppCompatActivity implements LocationListener
{
    boolean checkifListempty;
    String bestProvider;
    LocationManager locationManager;
    Location location;
    Handler handler;
    Geocoder geocoder;

    Location locationA = new Location("point A");
    Location locationB = new Location("point B");

    int seconds, minutes, millSec;
    long millsecTime, startTime, timeBuffer, updateTime, disTimer = 0L;
    double latitude, longitude;

    static CharSequence time, distRunnable;
    private static String formattedDate;
    static double distance;

    TextView timeTextView, distanceTextView;
    ListView timedistListView;
    Button start, pause, stop, end;

    String[] listDisTimeArray = new String[]{};
    List<String> listStrArrayListTimDis;
    ArrayAdapter<String> adapter;

    ClipData myClip;
    ClipboardManager clipboard;

    public static String getFormattedDate() {
        return formattedDate;
    }

    public static void setFormattedDate(String formattedDate)
    {
        StartActivity.formattedDate = formattedDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        this.checkifListempty = false;
        end = (Button) findViewById(R.id.endActivityButton);
        start = (Button) findViewById(R.id.startTimerButton);
        pause = (Button) findViewById(R.id.pauseTimerButton);
        stop = (Button) findViewById(R.id.stopTimerButton);
        timedistListView = (ListView) findViewById(R.id.timedistListView);

        distanceTextView = (TextView) findViewById(R.id.distanceCounterTextView);
        timeTextView = (TextView) findViewById(R.id.timerCounterTextView);

        handler = new Handler();
        listStrArrayListTimDis = new ArrayList<String>(Arrays.asList(listDisTimeArray));

        adapter = new ArrayAdapter<String>(StartActivity.this,
                android.R.layout.simple_list_item_1,
                listStrArrayListTimDis);

        timedistListView.setAdapter(adapter);
        stop.setEnabled(false);
        pause.setEnabled(false);

        handler.removeCallbacks(runnableDistance, 0);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                handler.postDelayed(runnableDistance, 0);

                start.setEnabled(false);
                pause.setEnabled(true);
                end.setEnabled(false);
                stop.setEnabled(true);

                setDistanceTextView();
                setTimeTextView();
                setLocationA();

                adapter.notifyDataSetChanged();
                checkifListempty = true;
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeBuffer += millsecTime;
                handler.removeCallbacks(runnable);

                pause.setEnabled(false);
                start.setEnabled(true);
                end.setEnabled(false);
                stop.setEnabled(true);

                setTimeTextView();
                adapter.notifyDataSetChanged();
                checkifListempty = true;
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setEnabled(false);
                pause.setEnabled(false);
                stop.setEnabled(false);
                end.setEnabled(true);
                listStrArrayListTimDis.add("TIME: " + timeTextView.getText().toString());
                handler.removeCallbacks(runnable);
                handler.removeCallbacks(runnableDistance);
                //handler.removeCallbacks(runnableDistanceClick, 1);
                adapter.notifyDataSetChanged();
                setLocationB();
                setDistanceAtoB();

                listStrArrayListTimDis.add("DISTANCE: " + getDistanceAtoB());
                distanceTextView.setText("" + getDistanceAtoB());

                clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                //ENABLE to copy the records
                timedistListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                        builder.setMessage("Do you wish to copy the record?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                String text = listStrArrayListTimDis.get(0) + "\n" + listStrArrayListTimDis.get(1);
                                myClip = ClipData.newPlainText("text", text);
                                clipboard.setPrimaryClip(myClip);
                            }
                        });

                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    }
                });
                checkifListempty = true;
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);

        bestProvider = locationManager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            location = locationManager.getLastKnownLocation(bestProvider);
            locationA = locationManager.getLastKnownLocation(bestProvider);
            locationB = locationManager.getLastKnownLocation(bestProvider);

        } else {
            Toast.makeText(this, "PLEASE ENABLE THE PERMISSION", Toast.LENGTH_LONG).show();
        }

        if (location != null && locationA != null && locationB != null)
        {
            onLocationChanged(location);
            onLocationChanged(locationA);
            onLocationChanged(locationB);
        }

        locationManager.requestLocationUpdates(bestProvider, 500, 0, this);

        Calendar calendar = Calendar.getInstance();
        System.out.println("Current time =&gt; " + calendar.getTime());

        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a");
        setFormattedDate(df.format(calendar.getTime()));
    }

    public void setLocationA()
    {
        locationA.setLatitude(latitude);
        locationA.setLongitude(longitude);
    }

    public void setLocationB() {
        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);
    }

    //A calculation - in meters (assume distance)
    public void setDistanceAtoB()
    {
        distance = locationA.distanceTo(locationB);
    }

    public static double getDistanceAtoB()
    {
        return distance;
    }

    private void setDistanceTextView()
    {
        distRunnable = distanceTextView.getText();
    }

    public static CharSequence getDistanceTextView()
    {
        return distRunnable;
    }

    private void setTimeTextView() {
        time = timeTextView.getText();
    }

    public static CharSequence getTimeTextView() {
        return time;
    }

    public void clickAddress(View view)
    {
        Toast.makeText(this, "Latitude:" + latitude +"\nLongitude:" + longitude,
                Toast.LENGTH_SHORT).show();
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            millsecTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuffer + millsecTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            millSec = (int) (updateTime % 1000);
            timeTextView.setText("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", millSec));
            handler.postDelayed(this, 0);
        }
    };

    public Runnable runnableDistance = new Runnable()
    {
        public void run()
        {
            double radius = (long) 6371e1;

            double R = radius;
            double φ1 = locationA.getLatitude();
            double λ1 = locationA.getLongitude();
            double φ2 = locationB.getLatitude();
            double λ2 = locationB.getLongitude();
            double Δφ = φ2 - φ1;
            double Δλ = λ2 - λ1;

            double a = (Math.sin(Δφ / 2) * Math.sin(Δφ / 2)
                    + Math.cos(φ1) * Math.cos(φ2)
                    * Math.sin(Δλ / 2) * Math.sin(Δλ / 2));
            double c = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
            double d = R * c;

            disTimer += d;

            handler.postDelayed(this, 1000);
            distanceTextView.setText(getDistanceTextView());
        }
    };

    public void clickMap(View v)
    {
        if(checkifListempty) {
            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
            builder.setMessage("WARNING: You will lost your time and distance records. " +
                    "Do you still want go to Map activity?");
            builder.setCancelable(true);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    goMapActivity();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            goMapActivity();
        }

    }
    private void goMapActivity()
    {
        startActivity(new Intent(this, MapsActivity.class));
        finish();
    }

    public void clickEndTimer(View v)
    {
        startActivity(new Intent(this, EndActivity.class));
        adapter.notifyDataSetChanged();
        setTimeTextView();
        setDistanceTextView();
        setDistanceAtoB();
        distanceTextView.setText("" + getDistanceAtoB());
        finish();
    }

    public void clickSMS(View v)
    {
        if(checkifListempty)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
            builder.setMessage("WARNING: You will lost your time and distance records. " +
                    "Do you still want go to SMS activity?");
            builder.setCancelable(true);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    goSMSActivity();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            goSMSActivity();
        }
    }

    public void goSMSActivity()
    {
        startActivity(new Intent(this, SMSFolder.class));
        finish();
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
        builder.setMessage("Do you wish to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int id)
            {
                StartActivity.this.finish();
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onLocationChanged(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "provider enable " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "provider disable " + provider,
                Toast.LENGTH_SHORT).show();
    }


    public void clickCam(View v)
    {
        if(checkifListempty)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
            builder.setMessage("WARNING: You will lost your time and distance records. " +
                    "Do you still want go to Camera activity?");
            builder.setCancelable(true);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    goCameraActivity();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            goCameraActivity();
        }
    }

    public void goCameraActivity()
    {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    public void clickLocation(View view)
    {
        List<Address> addressList = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try
        {
            addressList = geocoder.getFromLocation(latitude, longitude, 1000);
        } catch (IOException e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < addressList.get(0).getMaxAddressLineIndex(); i++)
        {
            stringBuilder.append(addressList.get(0).getAddressLine(i)).append("\n");
        }

        String city = addressList.get(0).getLocality();
        String state = addressList.get(0).getAdminArea();
        String country = addressList.get(0).getCountryName();
        String postalCode = addressList.get(0).getPostalCode();
        String knownName = addressList.get(0).getFeatureName();

        Toast.makeText(this, "ADDRESS: " + stringBuilder + "\n" + "CITY: " + city + "\n" + "STATE: " + state + "\n"
                + "COUNTRY: " + country + "\n" + "POSTAL CODE: " + postalCode + "\n" + "KNOWN NAME: " +
                knownName, Toast.LENGTH_LONG).show();
    }
}
