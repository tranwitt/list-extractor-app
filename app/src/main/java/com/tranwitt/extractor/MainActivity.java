package com.tranwitt.extractor;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.tranwitt.extractor.internal.DataHandler;
import com.tranwitt.extractor.internal.JsonEntry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ListView listView;
        List<String> keyList;
        List<String> keyListCopy;
        ArrayAdapter<String> adapter;
        Gson gson = new Gson();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.listView);
        DataHandler handler = new DataHandler("https://fetch-hiring.s3.amazonaws.com/hiring.json");
        keyList = new ArrayList<>(handler.getData().keySet());
        keyListCopy = new ArrayList<>(keyList);
        if (!keyListCopy.isEmpty()) {
            keyListCopy.replaceAll(s -> "ListId: " + s);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keyListCopy);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedKey = keyList.get(position);
            ArrayList<JsonEntry> selectedItems = handler.getData().get(selectedKey);

            String gsonList = gson.toJson(selectedItems);

            // Open new activity and pass the list of objects
            Intent intent = new Intent(MainActivity.this, ValueActivity.class);
            intent.putExtra("selected_items", gsonList); // Pass list as Serializable
            intent.putExtra("selected_key", selectedKey); // Pass key as well
            startActivity(intent);
        });
    }
}