package com.franciscoolivero.android.roomerapp.Filters;

import android.os.Parcel;
import android.os.Parcelable;

import com.franciscoolivero.android.roomerapp.Profile.Profile;

import androidx.annotation.NonNull;

public class Filter implements Parcelable {

    private String mToken;
    private String mHood;
    private int mMinMoney;
    private int mMaxMoney;
    private int mMinAge;
    private int mMaxAge;
    private String mGender;


    /**
     * Constructs a new {@link Profile} object.
     * @param mToken
     * @param mHood      Neighbourhood selected by the user.
     * @param mMaxMoney  Max Money selected by the user.
     * @param mMinAge Min Age selected by the user.
     * @param mMaxAge Max Age selecetd by the user.
     * @param mGender Gender selected by the user.
     * @param mMinMoney      Min Money selected by the user.
     */

    public Filter(String mToken, @NonNull String mHood, @NonNull int mMaxMoney, @NonNull int mMinAge, @NonNull int mMaxAge, @NonNull String mGender, @NonNull int mMinMoney) {
        this.mToken = mToken;
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


    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
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

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
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
        dest.writeString(this.mToken);
        dest.writeString(this.mHood);
        dest.writeInt(this.mMinMoney);
        dest.writeInt(this.mMaxMoney);
        dest.writeInt(this.mMinAge);
        dest.writeInt(this.mMaxAge);
        dest.writeString(this.mGender);
    }

    protected Filter(Parcel in) {
        this.mToken = in.readString();
        this.mHood = in.readString();
        this.mMinMoney = in.readInt();
        this.mMaxMoney = in.readInt();
        this.mMinAge = in.readInt();
        this.mMaxAge = in.readInt();
        this.mGender = in.readString();
    }

    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
