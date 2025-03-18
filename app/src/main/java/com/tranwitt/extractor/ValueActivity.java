package com.tranwitt.extractor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tranwitt.extractor.internal.JsonAdapter;
import com.tranwitt.extractor.internal.JsonEntry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ValueActivity extends AppCompatActivity {

    TextView textViewHeader;
    ListView listViewValues;
    JsonAdapter jsonAdapter;
    Gson gson = new Gson();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_value);

        textViewHeader = findViewById(R.id.textViewHeader);
        listViewValues = findViewById(R.id.listViewValues);

        // Retrieve data
        String gsonList = getIntent().getStringExtra("selected_items");
        String selectedKey = getIntent().getStringExtra("selected_key");
        
        Type listType = new TypeToken<ArrayList<JsonEntry>>() {}.getType();
        List<JsonEntry> entryList = gson.fromJson(gsonList, listType);

        // Set header text
        textViewHeader.setText("Items under ListId: " + selectedKey);

        // Create adapter
        jsonAdapter = new JsonAdapter(this, entryList);
        listViewValues.setAdapter(jsonAdapter);

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());
    }
}