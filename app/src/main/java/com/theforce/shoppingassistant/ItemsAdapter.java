package com.theforce.shoppingassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
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
    int layout;

    public ItemsAdapter(Context context, int layout, ArrayList<Item> shoppingList) {
        super(context,  layout, shoppingList);
        this.context = context;
        this.shoppingList = shoppingList;
        this.origShoppingList = shoppingList;
        this.layout = layout;
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
            convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
        }
        // Lookup view for data population
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        TextView itemCategory = (TextView) convertView.findViewById(R.id.itemCategory);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        // Populate the data into the template view using the data object
        itemName.setText(item.getName());
        itemCategory.setText(item.getCategory());
        if(checkBox != null){
            checkBox.setChecked(item.isChecked());
        }
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

    public void itemChecked (ListView listView, Item currentItem) {

        final int size = shoppingList.size();

        final ArrayList<Item> clonedList = new ArrayList<Item>(shoppingList.size());
        for (Item dog : shoppingList) {
            clonedList.add(new Item(dog));
        }

        for(int i=0; i<size; i++){

            final View view = listView.getChildAt(i);
            int duration = 300;
            int direction = -(view.getHeight());
            final int yo = i;

            Animation animation = new TranslateAnimation(0, 0, 0, direction);
            animation.setDuration(duration);
            animation.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart (Animation animation) {}
                public void onAnimationRepeat (Animation animation) {}
                public void onAnimationEnd (Animation animation) {
                    view.clearAnimation();
                    remove(shoppingList.get(yo));
                    if(yo<size-1) {
                        Item newItem = clonedList.get(yo+1);
                        insert(newItem, yo);
                    }
                }
            });
            view.startAnimation(animation);
        }



    }

}
