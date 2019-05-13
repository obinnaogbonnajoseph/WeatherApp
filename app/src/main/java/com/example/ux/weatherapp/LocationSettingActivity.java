package com.example.ux.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ux.weatherapp.data.SunshinePreferences;
import com.example.ux.weatherapp.search.City;
import com.example.ux.weatherapp.search.SearchAdapter;
import com.example.ux.weatherapp.sync.SunshineSyncUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationSettingActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, SearchAdapter.SearchAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private List<City> cities = new ArrayList<>();
    private SearchAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setting);

        mRecyclerView = findViewById(R.id.recyclerview_city);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator_city);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager);

        /* add divider to recycler view */
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        mSearchAdapter = new SearchAdapter(this, this, cities);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mSearchAdapter);
        showLoading();
        loadCities();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_location_key))) {
            // we've changed the location
            // Wipe out any potential PlacePicker latlng values so that we can use this text entry.
            SunshinePreferences.resetLocationCoordinates(getApplicationContext());
            SunshineSyncUtils.startImmediateSync(getApplicationContext());
        }
    }

    @Override
    public void onClick(String cityId, String cityName) {
        SunshinePreferences.setPrefCityId(this, cityId, cityName);
        SunshineSyncUtils.startImmediateSync(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadCities() {
        // load cities from JSON
        AsyncTask<Void, Void, List<City>> loadJsonTask = new AsyncTask<Void, Void, List<City>>() {
            @Override
            protected List<City> doInBackground(Void... voids) {
                String json;
                List<City> cityList = new ArrayList<>();
                try {
                    InputStream is = getApplicationContext().getAssets().open("city.list.json");
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    json = new String(buffer, "UTF-8");

                    JSONArray cityArrayList = new JSONArray(json);
                    for(int i = 0; i < cityArrayList.length(); i++) {
                        JSONObject cityObject = cityArrayList.getJSONObject(i);
                        City city = new City();
                        city.setCountry(cityObject.getString("country"));
                        city.setId(cityObject.getLong("id"));
                        city.setName(cityObject.getString("name"));
                        cityList.add(city);
                    }
                } catch (IOException | JSONException ex) {
                    ex.printStackTrace();
                    return null;
                }
                return cityList;
            }

            @Override
            protected void onPostExecute(List<City> cityList) {
                super.onPostExecute(cityList);
                cities.addAll(cityList);
                Collections.sort(cities, (d1, d2) -> d1.getName().compareTo(d2.getName()));
                mSearchAdapter.notifyDataSetChanged();
                showCityView();
            }
        };
        loadJsonTask.execute();
    }

    /**
     * This method will make the loading indicator visible and hide the weather View and error
     * message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the View for the weather data visible and hide the error message and
     * loading indicator.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't need to check whether
     * each view is currently visible or invisible.
     */
    private void showCityView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
