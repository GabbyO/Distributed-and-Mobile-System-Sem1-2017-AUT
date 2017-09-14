package android.dms.aut.ac.nz.testapptracking;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.dms.aut.ac.nz.testapptracking.StartActivity.getTimeTextView;

public class EndActivity extends AppCompatActivity implements LocationListener
{

    TextView endtimerTextView, startTimeFormat, endTimeFormat, distanceTV, dateTextView;
    Location location;
    double latitude, longitude;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        endtimerTextView = (TextView) findViewById(R.id.endtimerTextView);
        endtimerTextView.setText(getTimeTextView());
        endTimeFormat = (TextView) findViewById(R.id.endTimeTV);
        startTimeFormat = (TextView) findViewById(R.id.startTimeTV);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        Calendar calendar = Calendar.getInstance();
        System.out.println("Current time =&gt; " +calendar.getTime());

        SimpleDateFormat dateDayYear = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a");

        String formattedDate = df.format(calendar.getTime());
        String formattedDay = dateDayYear.format(calendar.getTime());

        endTimeFormat.setText(formattedDate);
        startTimeFormat.setText(StartActivity.getFormattedDate());
        dateTextView.setText(formattedDay);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        criteria.setBearingRequired(true);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(bestProvider);
        } else {
            Toast.makeText(this, "PLEASE ENABLE THE PERMISSIONS", Toast.LENGTH_LONG).show();
        }

        if (location != null)
        {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);

        distanceTV = (TextView) findViewById(R.id.distanceTV);
        distanceTV.setText("" + StartActivity.getDistanceAtoB());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String resultTime = data.getStringExtra("getTimeTextView");
                String resultDistance = data.getStringExtra("getDistanceAtoB");
            }
        }
    }

    public void clickBack(View v)
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void clickExit(View v)
    {
        finish();
        EndActivity.this.finish();
        finish();
    }

    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EndActivity.this);
        builder.setMessage("Do you want to exit?");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
                EndActivity.this.finish();
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

    public void clickLocation(View view)
    {
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1000);
        } catch (IOException e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
            sb.append(addresses.get(0).getAddressLine(i)).append("\n");
        }

        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        Toast.makeText(this, "ADDRESS: " + sb + "\n" + "CITY: " + city + "\n" + "STATE: " + state + "\n"
                + "COUNTRY: " + country + "\n" + "POSTAL CODE: " + postalCode + "\n" + "KNOWN NAME: " +
                    knownName, Toast.LENGTH_LONG).show();
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
    public void onProviderEnabled(String provider) {}
    @Override
    public void onProviderDisabled(String provider) {}
}
