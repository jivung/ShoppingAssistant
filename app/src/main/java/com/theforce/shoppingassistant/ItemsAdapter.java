package com.theforce.shoppingassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Micke on 2016-05-19.
 */
public class ItemsAdapter extends ArrayAdapter<Item>{

    ArrayList<Item> shoppingList = null;
    Context context;

    public ItemsAdapter(Context context, ArrayList<Item> shoppingList) {
        super(context,  R.layout.list_item, shoppingList);
        this.context = context;
        this.shoppingList = shoppingList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data list_item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemCategory = (TextView) convertView.findViewById(R.id.itemCategory);
        // Populate the data into the template view using the data object
        itemName.setText(item.getName());
        itemCategory.setText(item.getCategory());
        // Return the completed view to render on screen
        return convertView;
    }

}
