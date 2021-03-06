package com.franciscoolivero.android.roomerapp.Matches;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {


    private String mName;
    private String mLastName;
    private int mAge;
    private String mImageUrl = null;
    private String mMail;
    private String mPhone;
    private int mAreaCode;


//
//    /**
//     * Constructs a new {@link Match} object.
//     * @param mTitle      is the tittle of the Match.
//     * @param mAuthors      is the author of the Match.
//     * @param mInfoLink         is the Google Matchs website of the Match.
//     * @param mListPrice  is the List Price of the profile.
//     * @param mCurrencyCode is the currency code for the profile sale.
//     * @param mRating is the average rating of the profile.
//     *
//     */


    public Match(String mName, String mLastName, int mAge, String mImageUrl, String mMail, String mPhone, int mAreaCode) {
        this.mName = mName;
        this.mLastName = mLastName;
        this.mAge = mAge;
        this.mImageUrl = mImageUrl;
        this.mMail = mMail;
        this.mPhone = mPhone;
        this.mAreaCode = mAreaCode;
    }

    public String getmLastName() {
        return mLastName;
    }

    public String getmName() {
        return mName;
    }

    public int getmAge() {
        return mAge;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getmMail() {
        return mMail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public int getmAreaCode() {
        return mAreaCode;
    }

    public boolean hasImage() {
        return mImageUrl != null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mLastName);
        dest.writeInt(this.mAge);
        dest.writeString(this.mImageUrl);
        dest.writeString(this.mPhone);
        dest.writeString(this.mMail);
        dest.writeInt(this.mAreaCode);
    }

    protected Match(Parcel in) {
        this.mName = in.readString();
        this.mLastName = in.readString();
        this.mMail = in.readString();
        this.mAge = in.readInt();
        this.mImageUrl = in.readString();
        this.mPhone = in.readString();
        this.mAreaCode = in.readInt();
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel source) {
            return new Match(source);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
}
