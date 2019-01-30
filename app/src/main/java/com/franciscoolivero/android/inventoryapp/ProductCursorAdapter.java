package com.franciscoolivero.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = new ViewHolder(view);
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String model = cursor.getString(cursor.getColumnIndexOrThrow("model"));
        float price = cursor.getFloat(cursor.getColumnIndexOrThrow("price"));
        String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
        holder.name.setText(name);
        holder.model.setText(model);
        String priceString = String.format("%.2f", price);
        holder.price.setText(priceString);
        quantity = context.getResources().getString(R.string.in_stock_label_list_item)+quantity;
        holder.quantity.setText(quantity);

    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.model)
        TextView model;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.list_item_parent_view)
        LinearLayout parentView;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}