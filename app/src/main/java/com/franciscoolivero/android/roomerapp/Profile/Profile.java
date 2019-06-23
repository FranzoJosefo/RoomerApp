package com.franciscoolivero.android.roomerapp.Profile;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Profile implements Parcelable {
    /**
     * Name of the roomate
     */
    private String mName;
    /**
     * Last name of the roomate
     */
    private String mLastName;
    /**
     * Gender of the roomate
     */
    private String mGender;
    /**
     * DNI of the roomate
     */
    private int mDni;
    /**
     * Phone of the roomate
     */
    private int mPhone;
    /**
     * Area Code of the roomate
     */
    private int mAreaCode;
    /**
     * Age of the roomate
     */
    private int mAge;
    /**
     * Profile Picture of the roomate Id?
     */
    private String mPicture = null;
    /**
     * Roomate's description.
     */
    private String mDescription = null;

    /**
     * Constructs a new {@link Profile} object.
     * @param mName      Name of the roomate
     * @param mLastName      Last name of the roomate
     * @param mAge         Age of the roomate
     * @param mGender  Gender of the roomate
     * @param mDni Dni of the roomate
     * @param mAreaCode AreaCode of the roomate
     * @param mPhone Phone of the roomate
     * @param mPicture Picture of the roomate
     * @param mDescription Description of the roomate
     */

    public Profile(String mName, String mLastName, String mGender, int mDni, int mPhone, int mAreaCode, int mAge, @Nullable String mPicture, @Nullable String mDescription) {
        this.mName = mName;
        this.mLastName = mLastName;
        this.mGender = mGender;
        this.mDni = mDni;
        this.mPhone = mPhone;
        this.mAreaCode = mAreaCode;
        this.mAge = mAge;
        this.mPicture = mPicture;
        this.mDescription = mDescription;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public int getmDni() {
        return mDni;
    }

    public void setmDni(int mDni) {
        this.mDni = mDni;
    }

    public int getmPhone() {
        return mPhone;
    }

    public void setmPhone(int mPhone) {
        this.mPhone = mPhone;
    }

    public int getmAreaCode() {
        return mAreaCode;
    }

    public void setmAreaCode(int mAreaCode) {
        this.mAreaCode = mAreaCode;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public String getmPicture() {
        return mPicture;
    }

    public void setmPicture(String mPicture) {
        this.mPicture = mPicture;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean hasDescription(){
        return mDescription != null;
    }

    public boolean hasImage(){
        return mPicture != null;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDescription);
        dest.writeString(this.mName);
        dest.writeString(this.mLastName);
        dest.writeString(this.mGender);
        dest.writeString(this.mPicture);
        dest.writeInt(this.mAge);
        dest.writeInt(this.mDni);
        dest.writeInt(this.mAreaCode);
        dest.writeInt(this.mPhone);
    }

    protected Profile(Parcel in) {
        this.mDescription = in.readString();
        this.mName = in.readString();
        this.mLastName = in.readString();
        this.mGender = in.readString();
        this.mPicture = in.readString();
        this.mAge = in.readInt();
        this.mDni = in.readInt();
        this.mAreaCode = in.readInt();
        this.mPhone = in.readInt();
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
