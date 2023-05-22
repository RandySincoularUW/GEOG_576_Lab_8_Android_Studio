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

        String str = null;

        System.out.println("*** AsyncHttpRequest: in doInBackground() ...");

        try {

            System.out.println("*** AsyncHttpRequest: in doInBackground()  HttpGet request() ...");

            // 15-May-23 add
            URL flightObject = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) flightObject.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent",USER_AGENT);
            int responseCode = con.getResponseCode();

            System.out.println("*** AsynchHttpRequest.java doInBackground() GET Response Code: " + responseCode);

            System.out.println("*** AsyncHttpRequest: in doInBackground()  Executing request() ...");

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


            System.out.println("*** AsyncHttpRequest: in doInBackground() length: " + str.length() + " result: " + str );


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

        if (flightArray == null) {
            System.out.println("*** AsyncHttpRequest: in doInBackground() .returning 'flightArray' array 'flightArray is NULL!' ...");
        }
        else {
            System.out.println("*** AsyncHttpRequest: in doInBackground() .returning flightArray array flightArray is Not Null!' ...");
        }


        return flightArray;

    }  // end doinBackground()


    @Override
    // ------------------------------------------------------------
    // This method is called after doInBackground method completes
    // ------------------------------------------------------------

    protected void onPostExecute(JSONArray jsonArray) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (mMap != null) {
            mMap.clear();
        }

        System.out.println("*** AsyncHttpRequest: in onPostExecute() ...");

        if (jsonArray == null) {
            System.out.println("*** AsyncHttpRequest: in onPostExecute() jsonArray is NULL ...");
        }
        else {
            System.out.println("*** AsyncHttpRequest: in onPostExecute() jsonArray is Not Null ... +++++++++");
        }

        if (jsonArray != null) {

            // 16-May-23 Don't need this
            System.out.println("*** AsyncHttpRequest: in onPostExecute() calling filterFlights ...");

            System.out.println("*** AsyncHttpRequest: in onPostExecute() looping through flights. Length: " + jsonArray.length());

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

     public void setGoogleMap(GoogleMap mMap) {
        System.out.println("*** AsyncHttpRequest: in setGoogleMap() ...");

        this.mMap = mMap;
    }

    public void setPutData(HashMap<String, String> mData) {
        System.out.println("*** AsyncHttpRequest: in setPutData() ...");
        this.mData = mData;
    }

} // end class