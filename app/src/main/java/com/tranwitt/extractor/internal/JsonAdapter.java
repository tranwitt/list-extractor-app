package com.tranwitt.extractor.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tranwitt.extractor.R;

import java.util.List;

public class JsonAdapter extends ArrayAdapter<JsonEntry> {
    /**
     * A custom adapter class to accomodate the complex elements of JsonEntry
     * @param context Context for the view adapter
     * @param entry List of JsonEntry objects
     */
    public JsonAdapter(Context context, List<JsonEntry> entry) {
        super(context, 0, entry);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the item for this position
        JsonEntry entry = getItem(position);

        // Inflate the layout if it's not already created
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_entry,
                    parent, false);
        }

        // Find TextViews in our custom layout
        TextView textTitle = convertView.findViewById(R.id.textTitle);
        TextView textSubtitle = convertView.findViewById(R.id.textSubtitle);

        // Set text values
        assert entry != null;
        textTitle.setText("Name: " + entry.getName());
        textSubtitle.setText("Id: " + entry.getId());
        return convertView;
    }
}