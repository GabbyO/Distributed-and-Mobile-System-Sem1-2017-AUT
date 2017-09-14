package android.dms.aut.ac.nz.testapptracking;

/**
 * Created by Gabby on 15/05/2017.
 */

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser
{
    /**
     * USE JSONObject and get a list of lists - points, containing latitude and longitude
     * DIRECTION ROUTES
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject)
    {
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray JSONRoutes = null;
        JSONArray JSONLegs = null;
        JSONArray JSONSteps = null;
        JSONObject JSONDistance = null;
        JSONObject JSONDuration = null;

        try
        {
            JSONRoutes = jObject.getJSONArray("routes");

            //Get routes from JSON
            for (int i = 0; i < JSONRoutes.length(); i++)
            {
                JSONLegs = ((JSONObject) JSONRoutes.get(i)).getJSONArray("legs");

                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                //Get legs from JSON
                for (int j = 0; j < JSONLegs.length(); j++) {

                    //Get distance from JSON
                    JSONDistance = ((JSONObject) JSONLegs.get(j)).getJSONObject("distance");
                    HashMap<String, String> hmDistance = new HashMap<String, String>();
                    hmDistance.put("distance", JSONDistance.getString("text"));

                    //Get duration from JSON
                    JSONDuration = ((JSONObject) JSONLegs.get(j)).getJSONObject("duration");
                    HashMap<String, String> hmDuration = new HashMap<String, String>();
                    hmDuration.put("duration", JSONDuration.getString("text"));

                    //Add distance/duration to path list
                    path.add(hmDistance);
                    path.add(hmDuration);

                    JSONSteps = ((JSONObject) JSONLegs.get(j)).getJSONArray("steps");

                    //Get steps from JSON
                    for (int k = 0; k < JSONSteps.length(); k++)
                    {
                        String polyline = "";

                        polyline = (String) ((JSONObject) ((JSONObject) JSONSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        //Add points to a list
                        for (int l = 0; l < list.size(); l++)
                        {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                }
                routes.add(path);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }

    /**
     * A method to decode polyline points (draw paths)
     * FROM: jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    public static List<LatLng> decodePoly(String encoded)
    {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}