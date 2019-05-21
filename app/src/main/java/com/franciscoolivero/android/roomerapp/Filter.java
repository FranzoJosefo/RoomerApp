package com.franciscoolivero.android.roomerapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Filter implements Parcelable {

    private String mHood;
    private int mMinMoney;
    private int mMaxMoney;
    private int mMinAge;
    private int mMaxAge;
    private int mGender;


    /**
     * Constructs a new {@link Profile} object.
     * @param mHood      Neighbourhood selected by the user.
     * @param mMinMoney      Min Money selected by the user.
     * @param mMaxMoney  Max Money selected by the user.
     * @param mMinAge Min Age selected by the user.
     * @param mMaxAge Max Age selecetd by the user.
     * @param mGender Gender selected by the user.
     */

    public Filter(@NonNull String mHood, @NonNull int mMinMoney, @NonNull int mMaxMoney, @NonNull int mMinAge, @NonNull int mMaxAge, @NonNull int mGender) {
        this.mHood = mHood;
        this.mMinMoney = mMinMoney;
        this.mMaxMoney = mMaxMoney;
        this.mMinAge = mMinAge;
        this.mMaxAge = mMaxAge;
        this.mGender = mGender;
    }

    public String getmHood() {
        return mHood;
    }

    public void setmHood(String mHood) {
        this.mHood = mHood;
    }

    public int getmMinMoney() {
        return mMinMoney;
    }

    public void setmMinMoney(int mMinMoney) {
        this.mMinMoney = mMinMoney;
    }

    public int getmMaxMoney() {
        return mMaxMoney;
    }

    public void setmMaxMoney(int mMaxMoney) {
        this.mMaxMoney = mMaxMoney;
    }

    public int getmMinAge() {
        return mMinAge;
    }

    public void setmMinAge(int mMinAge) {
        this.mMinAge = mMinAge;
    }

    public int getmMaxAge() {
        return mMaxAge;
    }

    public void setmMaxAge(int mMaxAge) {
        this.mMaxAge = mMaxAge;
    }

    public int getmGender() {
        return mGender;
    }

    public void setmGender(int mGender) {
        this.mGender = mGender;
    }


//    public boolean hasAuthor(){
//        return mAuthors != null;
//    }
//
//    public boolean isSaleable(){
//        return mListPrice != null;
//    }
//
//
//    public boolean hasRating(){
//        return !mRating.equals("");
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mHood);
        dest.writeInt(this.mMinMoney);
        dest.writeInt(this.mMaxMoney);
        dest.writeInt(this.mMinAge);
        dest.writeInt(this.mMaxAge);
        dest.writeInt(this.mGender);
    }

    protected Filter(Parcel in) {
        this.mHood = in.readString();
        this.mMinMoney = in.readInt();
        this.mMaxMoney = in.readInt();
        this.mMinAge = in.readInt();
        this.mMaxAge = in.readInt();
        this.mGender = in.readInt();
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel source) {
            return new Profile(source);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
