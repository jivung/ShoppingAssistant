package com.theforce.shoppingassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Micke on 2016-05-19.
 */
public class ItemsAdapter extends ArrayAdapter<Item> implements Filterable{

    ArrayList<Item> shoppingList = null;
    ArrayList<Item> origShoppingList = null;
    ItemFilter itemFilter;
    Context context;

    public ItemsAdapter(Context context, ArrayList<Item> shoppingList) {
        super(context,  R.layout.list_item, shoppingList);
        this.context = context;
        this.shoppingList = shoppingList;
        this.origShoppingList = shoppingList;
    }

    public int getCount() {
        return shoppingList.size();
    }

    public Item getItem(int position) {
        return shoppingList.get(position);
    }

    public long getItemId(int position) {
        return shoppingList.get(position).hashCode();
    }

    public ArrayList<Item> getFilteredList(){
        return shoppingList;
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

    @Override
    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            // We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
               // No filter implemented we return all the list
                results.values = origShoppingList;
                results.count = origShoppingList.size();
            } else {
                // We perform filtering operation
                ArrayList<Item> nShoppingList = new ArrayList<>();
                for (Item p : shoppingList) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
                        nShoppingList.add(p);
                    }
                }
                results.values = nShoppingList;
                results.count = nShoppingList.size();
            }
            return results;
        }
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Now we have to inform the adapter about the new list filtered
            if (results.count == 0) {
                notifyDataSetInvalidated();
            } else {
                shoppingList = (ArrayList<Item>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}
