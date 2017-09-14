package android.dms.aut.ac.nz.testapptracking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.dms.aut.ac.nz.testapptracking.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    MenuInflater inflater;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    LatLng latLng, latLng1;
    Geocoder geocoder;
    Marker marker;
    String urlTopass1, urlTopassWalk;
    Polyline line;
    URLDownlanderDisDur URLDownlanderDisDur;

    Button enterAddress;
    Button walkButton, driveButton;
    TextView durdisTextViewMAP;
    EditText addressTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        mapFragment.getMapAsync(this);

        if (mapFragment == null)
        {
            Toast.makeText(this, "Google Maps is not available",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
                mMap.setIndoorEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setTiltGesturesEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        walkButton = (Button) findViewById(R.id.walkButton);
        driveButton = (Button) findViewById(R.id.driveButton);
        durdisTextViewMAP = (TextView) findViewById(R.id.durdisTextViewMAP);

        enterAddress = (Button) findViewById(R.id.enterAddress);
        addressTextEdit = (EditText) findViewById(R.id.addressTextEdit);

        walkButton.setEnabled(true);
        driveButton.setEnabled(false);

        geocoder = new Geocoder(this, Locale.getDefault());

        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walkButton.setEnabled(false);
                driveButton.setEnabled(true);
            }
        });

        driveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walkButton.setEnabled(true);
                driveButton.setEnabled(false);
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng point)
            {
                //save current location
                latLng1 = point;

                List<Address> addresses = new ArrayList<>();

                try
                {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                    {
                        stringBuilder.append(address.getAddressLine(i) + "\n");
                    }

                    Toast.makeText(MapsActivity.this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                }

                //remove previously placed Marker
                if (marker != null)
                {
                    marker.remove();
                }

                //place marker where user just clicked
                marker = mMap.addMarker(new MarkerOptions().position(point).title("Touch Marker")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                if (!walkButton.isEnabled())
                {
                        urlTopassWalk = makeURLWalking(latLng.latitude,
                                latLng.longitude, latLng1.latitude,
                                latLng1.longitude);

                        new connectAsyncTask(urlTopassWalk).execute();

                        URLDownlanderDisDur = new URLDownlanderDisDur();
                        String url = makeURLWalking(latLng.latitude,
                                latLng.longitude, latLng1.latitude,
                                latLng1.longitude);

                        URLDownlanderDisDur.execute(url);

                } else if (walkButton.isEnabled())
                {
                        urlTopass1 = makeURLDriving(latLng.latitude,
                                latLng.longitude, latLng1.latitude,
                                latLng1.longitude);

                        new connectAsyncTask(urlTopass1).execute();

                        URLDownlanderDisDur = new URLDownlanderDisDur();
                        String url = makeURLDriving(latLng.latitude,
                                latLng.longitude, latLng1.latitude,
                                latLng1.longitude);

                        URLDownlanderDisDur.execute(url);
                }

                Double distanceL1L2 = SphericalUtil.computeDistanceBetween(latLng, latLng1);

                List<Address> disdurAddresses = new ArrayList<>();

                try
                {
                    disdurAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MapsActivity.this, "DISTANCE FROM: " + disdurAddresses.get(0).getAddressLine(0)
                        + " - TO - " + addresses.get(0).getAddressLine(0) +
                        ":\n" + distanceL1L2 + " (METERS)", Toast.LENGTH_LONG).show();
            }
        });

        enterAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marker != null)
                {
                    mMap.clear();
                    marker.remove();
                }

                setAddressFinder();
            }
        });
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            // action with ID action_refresh was selected
            case R.id.SAT:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            // action with ID action_settings was selected
            case R.id.HYB:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            case R.id.NOR:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setBuildingsEnabled(true);
                break;
            // action with ID action_settings was selected
            case R.id.TER:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case R.id.SHOWTRAFFIC:
                mMap.setTrafficEnabled(true);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (mCurrLocationMarker != null)
        {
            mMap.clear();
        }

        //Place current location marker
        latLng = new LatLng(latitude, longitude);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        mLocationRequest.getPriority();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);

        //stop location updates
        if (mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // The permission was granted. Do contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        if (mGoogleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }

                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "The permission was denied.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void setAddressFinder()
    {
        try
        {
            List<Address> addresses = geocoder.getFromLocationName(addressTextEdit.getText().toString(), 5);

            if (addresses.size() > 0)
            {
                Double lat = (double) (addresses.get(0).getLatitude());
                Double lon = (double) (addresses.get(0).getLongitude());

                Log.d("lat-long", "" + lat + "......." + lon);

                latLng1 = new LatLng(lat, lon);

                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng1)
                        .title(addresses.get(0).getAddressLine(0))
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.amu_bubble_mask)));

                String URLgetPos = makeURLDriving(mCurrLocationMarker.getPosition().latitude,
                        mCurrLocationMarker.getPosition().longitude, marker.getPosition().latitude,
                        marker.getPosition().longitude);

                new connectAsyncTask(URLgetPos).execute();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(marker.getPosition())
                        .zoom(18).bearing(90)
                        .tilt(30).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        cameraPosition));

                addressTextEdit.setText("");
                URLDownlanderDisDur = new URLDownlanderDisDur();

                if (!walkButton.isEnabled() && driveButton.isEnabled())
                {
                    String url = makeURLWalking(latLng.latitude,
                            latLng.longitude, latLng1.latitude,
                            latLng1.longitude);
                    URLDownlanderDisDur.execute(url);
                }

                if (!driveButton.isEnabled() && walkButton.isEnabled())
                {
                    String url = makeURLDriving(latLng.latitude,
                            latLng.longitude, latLng1.latitude,
                            latLng1.longitude);
                    URLDownlanderDisDur.execute(url);
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),
                    "ADDRESS failed, cannot find this ADDRESS! Please try again!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            AlertDialog.Builder adb = new AlertDialog.Builder(MapsActivity.this);
            adb.setTitle("Google Map");
            adb.setMessage("Please Provide the Proper Place");
            adb.setPositiveButton("Close", null);
            adb.show();
        }

        String addressDisplay;
        addressDisplay = addressTextEdit.getText().toString();

        if (enterAddress.isEnabled())
        {
            Toast.makeText(this, "Address typed: " + addressDisplay + "!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        onClick(findViewById(View.generateViewId()));
    }

    public void onClick(View v)
    {
        finish();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    public String makeURLDriving(double sourcelat, double sourcelog, double destlat,
                                 double destlog)
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        return urlString.toString();
    }

    public String makeURLWalking(double sourcelat, double sourcelog, double destlat,
                                 double destlog)
    {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString.append(Double.toString(sourcelog));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=walking&alternatives=true");
        return urlString.toString();
    }

    private class connectAsyncTask extends AsyncTask<Void, Void, String>
    {
        String url;
        connectAsyncTask(String urlPass)
        {
            url = urlPass;
            Log.e("test", " :---- 000 " + url);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if (result != null)
            {
                drawPath(result);
            }
        }
    }

    public class JSONParser
    {
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";

        // constructor
        public JSONParser() {}

        public String getJSONFromUrl(String url)
        {
            // Making HTTP request
            try
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try
            {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                json = sb.toString();
                is.close();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return json;
        }
    }

    public void drawPath(String result)
    {
        try
        {
            if (line != null)
            {
                mMap.clear();
                marker.remove();
            }

            mMap.addMarker(new MarkerOptions().position(latLng).icon(
                    BitmapDescriptorFactory.defaultMarker()));

            marker = mMap
                    .addMarker(new MarkerOptions().position(latLng1).icon(
                            BitmapDescriptorFactory
                                    .fromResource(R.drawable.amu_bubble_mask))
                            .title(geocoder.getFromLocation(latLng1.latitude, latLng1.longitude, 1).toString()));

            try
            {
                final JSONObject json = new JSONObject(result);

                Log.e("test", "json  :p==  " + json);
                JSONArray routeArray = json.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                Log.e("test", "routes :==  " + routes);

                JSONArray newTempARr = routes.getJSONArray("legs");
                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
                Log.e("test", "newDisTimeOb :==  " + newDisTimeOb);

                JSONObject overviewPolylines = routes
                        .getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = DirectionsJSONParser.decodePoly(encodedString);

                PolylineOptions options = new PolylineOptions().width(10)
                        .color(Color.BLUE).geodesic(true);

                for (int z = 0; z < list.size(); z++)
                {
                    LatLng point = list.get(z);
                    options.add(point);
                }

                line = mMap.addPolyline(options);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * To download JSON data using HTTP-Url-Connection
     */
    private String downloadUrl(String stringUrl) throws IOException
    {
        String s = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try
        {
            URL url = new URL(stringUrl);

            //Create the HTTP URL connection to connect with url
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            // Reading input stream from url
            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }

            s = stringBuffer.toString();
            bufferedReader.close();

        } catch (Exception e) {
        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return s;
    }

    private class URLDownlanderDisDur extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... url)
        {
            // To store url from web service
            String urlS = "";

            try
            {
                urlS = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return urlS;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            JSONParserAsyncTask JSONParserAsyncTask = new JSONParserAsyncTask();
            JSONParserAsyncTask.execute(result);
        }
    }


    //JSONPARSER
    private class JSONParserAsyncTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>
    {
        JSONObject jsonObject;

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {
            List<List<HashMap<String, String>>> routes = null;

            try
            {
                jsonObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        //TO GET DISTANCE/DURATION
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result)
        {
            ArrayList<LatLng> points = null;
            String distance = "";
            String duration = "";

            if (result.size() < 1)
            {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++)
            {
                points = new ArrayList<LatLng>();
                List<HashMap<String, String>> path = result.get(i);

                for (int d = 0; d < path.size(); d++)
                {
                    HashMap<String, String> point = path.get(d);

                    if (d == 0)
                    {
                        distance = (String) point.get("distance");
                        continue;
                    } else if (d == 1) {
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

            }
            durdisTextViewMAP.setText("DISTANCE: " + distance + "\nDURATION: " + duration);
        }
    }
}
