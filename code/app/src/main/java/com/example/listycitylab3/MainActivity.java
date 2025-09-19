package com.example.listycitylab3;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private ListView cityList;
    private CityArrayAdapter cityAdapter;

    private boolean editDialogOpen = false;   // guard to prevent re-opening

    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCity(int position, City city) {
        dataList.set(position, city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogClosed() {
        editDialogOpen = false;               // dialog finished (Save or Cancel)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cities = { "Edmonton", "Vancouver", "Toronto" };
        String[] provinces = { "AB", "BC", "ON" };

        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // Open EDIT dialog on row tap (guarded so it won't immediately re-open)
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            if (editDialogOpen) return;
            editDialogOpen = true;
            City c = dataList.get(position);
            AddCityFragment.newInstance(c.getName(), c.getProvince(), position)
                    .show(getSupportFragmentManager(), "Edit City");
        });

        // ADD new city via FAB
        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v ->
                AddCityFragment.newInstance(null, null, -1)
                        .show(getSupportFragmentManager(), "Add City")
        );
    }
}