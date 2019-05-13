package com.example.ux.weatherapp.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ux.weatherapp.R;

import java.util.List;

/**
 * {@link SearchAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    /* The context we use to utility methods, app resources and layout inflaters */
    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private SearchAdapterOnClickHandler mClickHandler;

    /* List of Cities */
    private List<City> mCities;


    /**
     * The interface that receives onClick messages.
     */
    public interface SearchAdapterOnClickHandler {
        void onClick(String cityId);
    }


    /**
     * Creates a SearchAdapter.
     *
     * @param context      Used to talk to the UI and app resources
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public SearchAdapter(@NonNull Context context, SearchAdapterOnClickHandler clickHandler, List<City> cities) {
        mContext = context;
        mClickHandler = clickHandler;
        mCities = cities;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_list_item, viewGroup, false);

        view.setFocusable(true);

        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchAdapterViewHolder searchAdapterViewHolder, int position) {
        City city = mCities.get(position);
        String cityName = city.getName() + ", " + city.getCountry();
        searchAdapterViewHolder.locationNameView.setText(cityName);
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }



    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class SearchAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView locationNameView;

        SearchAdapterViewHolder(View view) {
            super(view);
            locationNameView = view.findViewById(R.id.location_name);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click. We fetch the date that has been
         * selected, and then call the onClick handler registered with this adapter, passing that
         * date.
         *
         * @param v the View that was clicked
         */
        @Override
        public void onClick(View v) {
            // implement what happens on click
            int adapterPosition = getAdapterPosition();
            City city = mCities.get(adapterPosition);
            String cityId = city.getId()+"";
            mClickHandler.onClick(cityId);
        }
    }
}
