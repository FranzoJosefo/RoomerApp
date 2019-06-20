package com.franciscoolivero.android.roomerapp.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.franciscoolivero.android.roomerapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProfileAdapter extends ArrayAdapter<Profile> {

    public ProfileAdapter(@NonNull Context context, @NonNull ArrayList<Profile> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        final Profile currentProfile = getItem(position);
//        ViewHolder holder = new ViewHolder(listItemView);
//
//        holder.tittle.setText(currentProfile.getmTitle());

        //TODO LOGIC FOR PROFILE CARD.
//        if (currentProfile.hasAuthor()) {
//            holder.author.setVisibility(View.VISIBLE);
//
//            String authorString = TextUtils.join(", ", currentProfile.getmAuthors());
//            holder.author.setText(authorString);
//        } else {
//            holder.author.setVisibility(View.GONE);
//        }
//
//        if (currentProfile.hasImage()) {
//            Picasso.with(getContext()).load(currentProfile.getmImage()).into(holder.profileImage);
//        } else {
//            //TODO
//        }
//
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
        return listItemView;
    }

//    static class ViewHolder {
//        @BindView(R.id.text_tittle)
//        TextView tittle;
//        @BindView(R.id.text_author)
//        TextView author;
//        @BindView(R.id.text_rating)
//        TextView rating;
//        @BindView(R.id.text_price_amount)
//        TextView amount;
//        @BindView(R.id.text_currency)
//        TextView currencyCode;
//        @BindView(R.id.image_profile)
//        ImageView profileImage;
//        @BindView(R.id.image_star)
//        ImageView starRatingImage;

//        @BindView(R.id.button_buy)
//        Button buttonBuy;

//        private ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }

//    private void openWebPage(Profile profile) {
//        //Uri profileUri = Uri.parse(profile.getmInfoLink()); Replace
//        Intent intent = new Intent(Intent.ACTION_VIEW, profileUri);
//        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
//            getContext().startActivity(intent);
//        }
//    }
}
