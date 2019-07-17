package com.franciscoolivero.android.roomerapp.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.franciscoolivero.android.roomerapp.R;
import com.franciscoolivero.android.roomerapp.Results.ResultsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileAdapter extends ArrayAdapter<Profile> {

    private Context mContext;
    private ResultsFragment resultsFragment;


    public ProfileAdapter(@NonNull Context context, @NonNull ArrayList<Profile> objects, ResultsFragment resultsFragment) {
        super(context, 0, objects);
        this.mContext = context;
        this.resultsFragment = resultsFragment;
        Log.v("mContext of adap is", mContext.toString());
    }

    @Override
    public void remove(@Nullable Profile object) {
        super.remove(object);
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


        ImageButton buttonAddLike = listItemView.findViewById(R.id.button_add_list_item);


        buttonAddLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("onClick Profile Adapter", "onClick Triggered");
                if (resultsFragment.insertLike(currentProfile.getmToken())) {
                    Log.v("onClick adp", "insertLike was called");
                    remove(currentProfile);
                    notifyDataSetChanged();
                }

            }
        });


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
}
