package com.example.lenovo.pickaplace;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class getNearbyPlaces extends AsyncTask<Object,String ,String>{

    GoogleMap mMap;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data,name_restaurant,rating,vicinity;

    @Override
    protected String doInBackground(Object... param) {

        mMap = (GoogleMap)param[0];
        url = (String)param[1];

        try {
            URL myURL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)myURL.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line = "";
            stringBuilder = new StringBuilder();

            while((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();

        }
        catch(Exception e)
        {

        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        try{
            Log.d("MyApp", s);
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultArray = parentObject.getJSONArray("results");

            for(int i=0;i<resultArray.length();i++)
            {
                JSONObject jsonObject = resultArray.getJSONObject(i);
                JSONObject locationObject = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObject.getString("lat");
                String longitude = locationObject.getString("lng");

                JSONObject nameObject = resultArray.getJSONObject(i);

                name_restaurant = nameObject.getString("name");
                vicinity = nameObject.getString("vicinity");
                rating = nameObject.getString("rating");

                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));

                MarkerOptions markerOptions = new MarkerOptions();
                //markerOptions.snippet("\nPlace :"+vicinity+"\nRating : "+rating+"/5");
                markerOptions.snippet("Rating : "+rating+"/5");
                markerOptions.title(name_restaurant);
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);
            }
        }
        catch(Exception e)
        {
        }
    }
}
