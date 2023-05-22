package com.example.mobile_empty_views_activity;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class AsyncHttpRequest extends AsyncTask<String, Void, JSONArray> {
    private HashMap<String, String> mData;
    private GoogleMap mMap;
    // private MapFilters mapFilters;

    // 15-May-23
    private static final String USER_AGENT = "Mozilla/5.0";

    @Override

    // -------------------------------------------------
    // When this method completes, onPostExecute is run
    // -------------------------------------------------

    // ++++++++++++ 15-May-23 to get around the deprecated http client code +++++++++++

    // -------------------------------------------------
    // When this method completes, onPostExecute is run
    // -------------------------------------------------

    protected JSONArray doInBackground(String... params) {

        JSONArray arr = null;
        JSONArray flightArray = null;  // 13-May-23

        // HttpClient client = new DefaultHttpClient();

        // add 13-May-23 to deal with exception and the variable 'str' not being in scope
        String str = null;

        System.out.println("*** AsyncHttpRequest: in doInBackground() ...");

        try {
            // 15-May-23 comment out
            // HttpRequestBase req;


            System.out.println("*** AsyncHttpRequest: in doInBackground()  HttpGet request() ...");

            // 15-May-23 comment out
            // req = new HttpGet(params[0]); // in this case, params[0] is URL

            // 15-May-23 add
            URL flightObject = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) flightObject.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent",USER_AGENT);
            int responseCode = con.getResponseCode();

            System.out.println("*** AsynchHttpRequest.java doInBackground() GET Response Code: " + responseCode);

            System.out.println("*** AsyncHttpRequest: in doInBackground()  Executing request() ...");

            // 15-May-23 comment out
            // HttpResponse response = client.execute(req);

            // 15-May-23 comment out
            // byte[] result = EntityUtils.toByteArray(response.getEntity());

            // 15-May-23
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Print Result
                System.out.println("*** AsyncHttpRequest.java response: " + response.toString());

                // copy of string response
                str = String.valueOf(response);

            }
            else {
                System.out.println("*** AsyncHttpRequest.java response: POST Request did not work");

            }
            //* Result of HttpRequest
            //* ---------------------
            // 13-May-23
            // String str = new String(result, "UTF-8");


            // 15-May-23 comment out
            // str = new String(result, "UTF-8");

            System.out.println("*** AsyncHttpRequest: in doInBackground() length: " + str.length() + " result: " + str );

            // Convert Request String to an Array 'arr'
            // arr = new JSONArray(str);


        } catch (UnsupportedEncodingException e) {
            System.out.println("*** AsyncHttpRequest: in doInBackground() ERROR with unsupportedencoding ... ....");

            android.util.Log.v("INFO", e.toString());
        } catch (Exception e) {
            System.out.println("*** AsyncHttpRequest: in doInBackground(). Error/Exception  ....");

            android.util.Log.v("INFO", e.toString());
        }

        // +++++++++++++++++++++++++++++++++++++++
        // Code Goes Here after hitting Exception
        // +++++++++++++++++++++++++++++++++++++++

        System.out.println("*** +++++++++++++++ AsyncHttpRequest: doInBackground() Testing Result as JSONObject ++++++++++++++++");

        try {
            JSONObject flightObject = new JSONObject(str);
            Iterator<String> keys = flightObject.keys();

            if (flightObject == null) {
                System.out.println("++++ AsyncHttRequest: flightObject: is NULL ");

            } else {
                System.out.println("++++ AsyncHttRequest: flightObject: is Not Null");

            }

            // 13-May-23 Get 'response' portion of request that is returned
            // and convert it to an array
            flightArray = (JSONArray) flightObject.get("response");


        } catch (Exception e) {
            System.out.println("*** AsyncHttpRequest: in doInBackground(). Error/Exception: " + e);

            android.util.Log.v("INFO", e.toString());

        }

        System.out.println("*** AsyncHttpRequest: in doInBackground(). Ready to return 'array2' array ....");

        if (flightArray == null) {
            System.out.println("*** AsyncHttpRequest: in doInBackground() .returning 'flightArray' array 'flightArray is NULL!' ...");
        }
        else {
            System.out.println("*** AsyncHttpRequest: in doInBackground() .returning flightArray array flightArray is Not Null!' ...");
        }


        // Return 'arr' to onPostExecute()
        // return arr;
        return flightArray;

    }  // end doinBackground()


    // ++++++++++++ 15-May-23 Original. Need to get around the deprecated code ++++++++
    // Changed the name to: doInBackground2


    @Override
    // ------------------------------------------------------------
    // This method is called after doInBackground method completes
    // ------------------------------------------------------------

    // 16-May-23 Modified to not use the filter methods
    //

    protected void onPostExecute(JSONArray jsonArray) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (mMap != null) {
            mMap.clear();
        }

        System.out.println("*** AsyncHttpRequest: in onPostExecute() ...");

        // filterSightings changed to: filterFlights

        if (jsonArray == null) {
            System.out.println("*** AsyncHttpRequest: in onPostExecute() jsonArray is NULL ...");
        }
        else {
            System.out.println("*** AsyncHttpRequest: in onPostExecute() jsonArray is Not Null ... +++++++++");
        }

        if (jsonArray != null) {

            // 16-May-23 Don't need this
            System.out.println("*** AsyncHttpRequest: in onPostExecute() calling filterFlights ...");

            // JSONArray filteredFlights = mapFilters.filterFlights(jsonArray);

            System.out.println("*** AsyncHttpRequest: in onPostExecute() looping through flights. Length: " + jsonArray.length());

            // 16-May-23
            // changed filteredFlights to: jsonArray

            for (int i = 0 ; i < jsonArray.length(); i++) {
                try {
                    JSONObject flight = jsonArray.getJSONObject(i);
                    Double lng = Double.parseDouble(flight.getString("lng"));
                    Double lat = Double.parseDouble(flight.getString("lat"));

                    System.out.println("*** AsyncHttpRequest: in onPostExecute(). lng: " + lng + " lat: " + lat);

                    LatLng latlng = new LatLng(lat, lng);
                    builder.include(latlng);

                    // Flight Details
                    String regNumber = flight.getString("reg_number");
                    String airline = flight.getString("airline_icao");
                    String speed = flight.getString("speed");
                    String aircraftType = flight.getString("aircraft_icao");

                    System.out.println("*** AsyncHttpRequest: in onPostExecute() adding markers to map ...");

                    android.util.Log.v("** AsyncHttpRequest()"," onPostExecute() lat/lng: " + latlng.toString() + " reg_number: " + regNumber);


                    //* Add Marker to Map

                    mMap.addMarker(new MarkerOptions().position(latlng)
                            .title(flight.getString("reg_number"))
                            .snippet(
                                    flight.getString("aircraft_icao") + " is "
                                            + flight.getString("status") + " from "
                                            + flight.getString("dep_iata") + " to "
                                            + flight.getString("arr_iata") + " at speed of "
                                            + flight.getString("speed")
                            )
                    );

                } catch (JSONException e) {
                    android.util.Log.v("INFO", e.toString());
                }
            }

            // Set Map Extent for Zooming to Active Flights
            if (jsonArray.length() > 0) {
                LatLngBounds bounds = builder.build();

                int padding = 500; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.moveCamera(cu);
            }
        }

    } //end onPostExecute()

    // ----------------- end 16-may-23 changes


    // 16-May-23 Don't need to call the filter methods
    /*
    protected void onPostExecute2(JSONArray jsonArray) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (mMap != null) {
            mMap.clear();
        }

        System.out.println("*** AsyncHttpRequest: in onPostExecute() ...");

        // filterSightings changed to: filterFlights

        if (jsonArray == null) {
            System.out.println("*** AsyncHttpRequest: in onPostExecute() jsonArray is NULL ...");
        }
        else {
            System.out.println("*** AsyncHttpRequest: in onPostExecute() jsonArray is Not Null ... +++++++++");
        }

        if (jsonArray != null) {

            // 16-May-23 Don't need this
            System.out.println("*** AsyncHttpRequest: in onPostExecute() calling filterFlights ...");

            // JSONArray filteredFlights = mapFilters.filterFlights(jsonArray);

            System.out.println("*** AsyncHttpRequest: in onPostExecute() looping through flights. Length: " + filteredFlights.length());

            for (int i = 0 ; i < filteredFlights.length(); i++) {
                try {
                    JSONObject flight = filteredFlights.getJSONObject(i);
                    Double lng = Double.parseDouble(flight.getString("lng"));
                    Double lat = Double.parseDouble(flight.getString("lat"));

                    System.out.println("*** AsyncHttpRequest: in onPostExecute(). lng: " + lng + " lat: " + lat);

                    LatLng latlng = new LatLng(lat, lng);
                    builder.include(latlng);

                    // Flight Details
                    String regNumber = flight.getString("reg_number");
                    String airline = flight.getString("airline_icao");
                    String speed = flight.getString("speed");
                    String aircraftType = flight.getString("aircraft_icao");

                    System.out.println("*** AsyncHttpRequest: in onPostExecute() adding markers to map ...");

                    android.util.Log.v("** AsyncHttpRequest()"," onPostExecute() lat/lng: " + latlng.toString() + " reg_number: " + regNumber);


                    //* Add Marker to Map

                    /*
                    mMap.addMarker(new MarkerOptions().position(latlng)
                                    .title(aircraftType.toUpperCase())
                                    // Icon
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.damage_icon))
                                    .snippet("Aircraft Speed: " + speed));
                    */
/*
                    mMap.addMarker(new MarkerOptions().position(latlng)
                            .title(flight.getString("reg_number"))
                            .snippet(
                                    flight.getString("aircraft_icao") + " is "
                                            + flight.getString("status") + " from "
                                            + flight.getString("dep_iata") + " to "
                                            + flight.getString("arr_iata") + " at speed of "
                                            + flight.getString("speed")
                            )
                    );

                } catch (JSONException e) {
                    android.util.Log.v("INFO", e.toString());
                }
            }

            // 16-May-23 Don't need this
            // mapFilters.updateOptions(jsonArray);
        }

    } //end onPostExecute()
*/

    public void setGoogleMap(GoogleMap mMap) {
        System.out.println("*** AsyncHttpRequest: in setGoogleMap() ...");

        this.mMap = mMap;
    }

    public void setPutData(HashMap<String, String> mData) {
        System.out.println("*** AsyncHttpRequest: in setPutData() ...");
        this.mData = mData;
    }

    /* 16-May-23 Don't need this
    public void setMapFilters(MapFilters mapFilters) {
        System.out.println("*** AsyncHttpRequest: in setMapFilters() ...");

        this.mapFilters = mapFilters;
    }
*/
} // end class