package com.franciscoolivero.android.roomerapp.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.franciscoolivero.android.roomerapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileAdapter extends ArrayAdapter<Profile> {

    public ProfileAdapter(@NonNull Context context, @NonNull ArrayList<Profile> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.result_list_item, parent, false);
        }
        final Profile currentProfile = getItem(position);
        ViewHolder holder = new ViewHolder(listItemView);

        //TODO LOGIC FOR PROFILE CARD.

        //Get and update the name and full name
        String userfullName = currentProfile.getmName() + " " + currentProfile.getmLastName();
        holder.nameTextView.setText(userfullName);

        //Get and update the user age
        holder.ageTextView.setText(String.valueOf(currentProfile.getmAge()));

        //If there is an image, get it using picasso and set it to the ImageView
        if (currentProfile.hasImage()) {
            Picasso.get().load(currentProfile.getmPicture()).into(holder.profileImageView);
        } else {
            //TODO
        }

//        if (currentProfile.isSaleable()) {
//            holder.amount.setVisibility(View.VISIBLE);
//            holder.currencyCode.setVisibility(View.VISIBLE);
//            holder.amount.setText(currentProfile.getmListPrice());
//            holder.currencyCode.setText(currentProfile.getmCurrencyCode());
//        } else {
//            holder.amount.setVisibility(View.GONE);
//            holder.currencyCode.setVisibility(View.GONE);
//        }
//
//        if (currentProfile.hasRating()) {
//            holder.rating.setVisibility(View.VISIBLE);
//            holder.starRatingImage.setVisibility(View.VISIBLE);
//            holder.starRatingImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.round_star_rate_black_18));
//            holder.rating.setText(currentProfile.getmRating());
//        } else {
//            holder.rating.setVisibility(View.GONE);
//            holder.starRatingImage.setVisibility(View.GONE);
//        }


//        holder.buttonBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openWebPage(currentProfile);
//            }
//        });



        //TODO - Add more Complex Logic for button pressing and disabling each card.
//        buttonSale.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int quantity = Integer.valueOf(holder.quantity.getText().toString());
//                long affectedRowOrId;
//
//                if (quantity != 0) {
//                    quantity -= 1;
//                }
//
//                String strQuantity = String.valueOf(quantity);
//
//                // Create a new map of values, where column names are the keys
//                ContentValues values = new ContentValues();
//
//                values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, strQuantity);
//
//                affectedRowOrId = context.getContentResolver().update(mCurrentProductUri, values, null, null);
//                notifyDataSetChanged();
//
//                if (affectedRowOrId == 0) {
//                    Toast toast = Toast.makeText(view.getContext(), "Error selling product", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });
        return listItemView;
    }

    static class ViewHolder {
        @BindView(R.id.image_card_view)
        CardView imageCardViewContainer;
        @BindView(R.id.image_list_item)
        ImageView profileImageView;
        @BindView(R.id.name_list_item)
        TextView nameTextView;
        @BindView(R.id.age_list_item)
        TextView ageTextView;
        @BindView(R.id.button_add_list_item)
        ImageButton addLikeButton;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

//    private void openWebPage(Profile profile) {
//        //Uri profileUri = Uri.parse(profile.getmInfoLink()); Replace
//        Intent intent = new Intent(Intent.ACTION_VIEW, profileUri);
//        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
//            getContext().startActivity(intent);
//        }
//    }
}
