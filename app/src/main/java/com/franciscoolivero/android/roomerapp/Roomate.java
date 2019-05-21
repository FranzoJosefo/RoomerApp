package com.franciscoolivero.android.roomerapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class Roomate implements Parcelable {

    /**
     * Roomate Token
     */
    private String mToken;

    /**
     * Roomate Profile Data
     */
    private Profile mProfile;
    /**
     * Roomate Filter Data
     */
    private Filter mFilter;
    /**
     * List of the Id's Tokens for each user liked by the roomate
     * Initialize as null in case the roomate has no Likes done (JSON may not return this variable).
     */
    private ArrayList<String> mLikedTokens;
    /**
     * List of the Id's Tokens for each user that matched with the roomate
     * Initialize as null in case the roomate has no Matches done (JSON may not return this variable).
     */
    private ArrayList<String> mMatchesTokens;



//
//    /**
//     * Constructs a new {@link Roomate} object.
//     * @param mTitle      is the tittle of the Roomate.
//     * @param mAuthors      is the author of the Roomate.
//     * @param mInfoLink         is the Google Roomates website of the Roomate.
//     * @param mListPrice  is the List Price of the book.
//     * @param mCurrencyCode is the currency code for the book sale.
//     * @param mRating is the average rating of the book.
//     *
//     */


    public Roomate(String mToken, Profile mProfile, Filter mFilter, @Nullable ArrayList<String> mLikedTokens, @Nullable ArrayList<String> mMatchesTokens) {
        this.mToken = mToken;
        this.mProfile = mProfile;
        this.mFilter = mFilter;
        this.mLikedTokens = mLikedTokens;
        this.mMatchesTokens = mMatchesTokens;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public Profile getmProfile() {
        return mProfile;
    }

    public void setmProfile(Profile mProfile) {
        this.mProfile = mProfile;
    }

    public Filter getmFilter() {
        return mFilter;
    }

    public void setmFilter(Filter mFilter) {
        this.mFilter = mFilter;
    }

    public List<String> getmLikedTokens() {
        return mLikedTokens;
    }

    public void setmLikedTokens(ArrayList<String> mLikedTokens) {
        this.mLikedTokens = mLikedTokens;
    }

    public List<String> getmMatchesTokens() {
        return mMatchesTokens;
    }

    public void setmMatchesTokens(ArrayList<String> mMatchesTokens) {
        this.mMatchesTokens = mMatchesTokens;
    }

    public boolean hasLikes(){
        return mLikedTokens != null;
    }

    public boolean hasMatches(){
        return mMatchesTokens != null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mToken);
        dest.writeStringList(this.mLikedTokens);
        dest.writeStringList(this.mMatchesTokens);
        dest.writeParcelable(this.mFilter, flags);
        dest.writeParcelable(this.mProfile, flags);
    }

    protected Roomate(Parcel in) {
        this.mToken = in.readString();
        this.mLikedTokens = in.createStringArrayList();
        this.mMatchesTokens = in.createStringArrayList();
        this.mFilter = in.readParcelable(getClass().getClassLoader());
        this.mProfile = in.readParcelable(getClass().getClassLoader());
    }

    public static final Parcelable.Creator<Roomate> CREATOR = new Parcelable.Creator<Roomate>() {
        @Override
        public Roomate createFromParcel(Parcel source) {
            return new Roomate(source);
        }

        @Override
        public Roomate[] newArray(int size) {
            return new Roomate[size];
        }
    };
}
