package com.lyl.haza.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lyl on 2016/11/17.
 * </P>
 */
public class TabBean implements Parcelable{

    private String type;
    private String title;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.title);
    }

    public TabBean() {
    }

    protected TabBean(Parcel in) {
        this.type = in.readString();
        this.title = in.readString();
    }

    public static final Creator<TabBean> CREATOR = new Creator<TabBean>() {
        @Override
        public TabBean createFromParcel(Parcel source) {
            return new TabBean(source);
        }

        @Override
        public TabBean[] newArray(int size) {
            return new TabBean[size];
        }
    };
}
