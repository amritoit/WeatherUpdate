package com.example.amritendu.weatherupdate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by amritendu on 30/3/15.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    public void okClicked(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
        String[] forecastArray = {
                "Today-sunny-88/63",
                "Tomorrow-foggy-70/46",
                "Wednesday-cloudy-72/63",
                "Thursday-rainy-64/51",
                "Friday-foggy-70/46",
                "Saturday-sunny-76/68",
                "Saturday-testing-sunny-76/68"
        };
        List<String> weekForecast = Arrays.asList(forecastArray);

        ArrayAdapter mForecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast_dup,
                R.id.list_item_forecast_textview_dup,
                weekForecast);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast_dup);
        listView.setAdapter(mForecastAdapter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("amrit_success", "successfully returned root view ");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecastArray = {
                "Today-sunny-88/63",
                "Tomorrow-foggy-70/46",
                "Wednesday-cloudy-72/63",
                "Thursday-rainy-64/51",
                "Friday-foggy-70/46",
                "Saturday-sunny-76/68",
        };
        List<String> weekForecast = Arrays.asList(forecastArray);

        ArrayAdapter mForecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.lilst_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);


        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);



        return rootView;
    }

    class FetchWeatherTask extends AsyncTask{

        private final String LOG_TAG= FetchWeatherTask.class.getSimpleName();

        @Override
        protected Object doInBackground(Object[] params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                Log.i(LOG_TAG, "successfully returned root view ");


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

}