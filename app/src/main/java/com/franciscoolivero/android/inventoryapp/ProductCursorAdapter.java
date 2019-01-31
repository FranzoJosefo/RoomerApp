package com.franciscoolivero.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.franciscoolivero.android.inventoryapp.data.ProductContract.ProductEntry;

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
        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        byte[] pictureByteArray = cursor.getBlob(cursor.getColumnIndexOrThrow("picture"));
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureByteArray, 0, pictureByteArray.length);
        if (bitmap != null) {
            holder.picture.setImageBitmap(bitmap);
        }

        ImageButton buttonSale = view.findViewById(R.id.button_sale);

        int currentId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));

        Uri mCurrentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, currentId);


        buttonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.valueOf(holder.quantity.getText().toString());
                long affectedRowOrId;

                if (quantity != 0) {
                    quantity -= 1;
                }

                String strQuantity = String.valueOf(quantity);

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();

                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, strQuantity);

                affectedRowOrId = context.getContentResolver().update(mCurrentProductUri, values, null, null);
                notifyDataSetChanged();

                if (affectedRowOrId == 0) {
                    Toast toast = Toast.makeText(view.getContext(), "Error selling product", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        holder.name.setText(name);
        holder.model.setText(model);
        String priceString = String.format("%.2f", price);
        holder.price.setText(priceString);

        String quantityStr = String.valueOf(quantity);

        if (quantity == 0) {
            holder.quantity.setText(context.getResources().getString(R.string.out_of_stock_label_list_item));
            holder.buttonSale.setVisibility(View.GONE);
            holder.inStockLabel.setVisibility(View.GONE);
        } else {
            holder.buttonSale.setVisibility(View.VISIBLE);
            holder.inStockLabel.setVisibility(View.VISIBLE);
            holder.quantity.setText(quantityStr);
        }

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
        @BindView(R.id.image_list_item)
        ImageView picture;
        @BindView(R.id.button_sale)
        ImageButton buttonSale;
        @BindView(R.id.in_stock_label)
        TextView inStockLabel;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}