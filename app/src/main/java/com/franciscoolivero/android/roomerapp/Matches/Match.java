package com.franciscoolivero.android.roomerapp.Matches;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable {


    private String mName;
    private int mAge;
    private int mImageResourceID;
    private String mMail;
    private String mPhone;
    private String mAreaCode;



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


    public Match(String mName, int mAge, int mImageResourceID, String mMail, String mPhone, String mAreaCode) {
        this.mName = mName;
        this.mAge = mAge;
        this.mImageResourceID = mImageResourceID;
        this.mMail = mMail;
        this.mPhone = mPhone;
        this.mAreaCode = mAreaCode;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public int getmImageResourceID() {
        return mImageResourceID;
    }

    public void setmImageResourceID(int mImageResourceID) {
        this.mImageResourceID = mImageResourceID;
    }

    public String getmMail() {
        return mMail;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmAreaCode() {
        return mAreaCode;
    }

    public void setmAreaCode(String mAreaCode) {
        this.mAreaCode = mAreaCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeInt(this.mAge);
        dest.writeInt(this.mImageResourceID);
        dest.writeString(this.mPhone);
        dest.writeString(this.mMail);
        dest.writeString(this.mAreaCode);
    }

    protected Match(Parcel in) {
        this.mName = in.readString();
        this.mMail = in.readString();
        this.mAge = in.readInt();
        this.mImageResourceID = in.readInt();
        this.mPhone = in.readString();
        this.mAreaCode = in.readString();
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
