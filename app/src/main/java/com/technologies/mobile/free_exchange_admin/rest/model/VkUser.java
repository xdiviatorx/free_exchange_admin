package com.technologies.mobile.free_exchange_admin.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 29.12.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class VkUser implements Parcelable{

    public VkUser(){}

    @JsonProperty("id")
    long id;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("photo_100")
    String photo100Url;

    protected VkUser(Parcel in) {
        id = in.readLong();
        firstName = in.readString();
        lastName = in.readString();
        photo100Url = in.readString();
    }

    public static final Creator<VkUser> CREATOR = new Creator<VkUser>() {
        @Override
        public VkUser createFromParcel(Parcel in) {
            return new VkUser(in);
        }

        @Override
        public VkUser[] newArray(int size) {
            return new VkUser[size];
        }
    };

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoto100Url() {
        return photo100Url;
    }

    public String getFullName(){
        return getFirstName() + " " + getLastName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(photo100Url);
    }
}
