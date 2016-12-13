package com.lyl.haza.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lyl on 2016/11/16.
 * </P>
 */
public class NewsBean implements Parcelable{

    /**
     * title : 一富豪送了条狗狗给老人，老人得知后很生气过了几天致电感谢富豪
     * date : 2016-11-16 04:37
     * author_name : 养条好狗
     * thumbnail_pic_s : http://04.imgmini.eastday.com/mobile/20161116/20161116043733_e9dc8e1d0f954013f1dd680ff22720a9_1_mwpm_03200403.jpeg
     * thumbnail_pic_s02 : http://04.imgmini.eastday.com/mobile/20161116/20161116043733_e9dc8e1d0f954013f1dd680ff22720a9_1_mwpl_05500201.jpeg
     * thumbnail_pic_s03 : http://04.imgmini.eastday.com/mobile/20161116/20161116043733_e9dc8e1d0f954013f1dd680ff22720a9_1_mwpl_05500201.jpeg
     * url : http://mini.eastday.com/mobile/161116043733504.html?qid=juheshuju
     * uniquekey : 161116043733504
     * type : 头条
     * realtype : 社会
     */

    private String title;
    private String date;
    private String author_name;
    private String thumbnail_pic_s;
    private String thumbnail_pic_s02;
    private String thumbnail_pic_s03;
    private String url;
    private String uniquekey;
    private String type;
    private String realtype;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getThumbnail_pic_s02() {
        return thumbnail_pic_s02;
    }

    public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }

    public String getThumbnail_pic_s03() {
        return thumbnail_pic_s03;
    }

    public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRealtype() {
        return realtype;
    }

    public void setRealtype(String realtype) {
        this.realtype = realtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.author_name);
        dest.writeString(this.thumbnail_pic_s);
        dest.writeString(this.thumbnail_pic_s02);
        dest.writeString(this.thumbnail_pic_s03);
        dest.writeString(this.url);
        dest.writeString(this.uniquekey);
        dest.writeString(this.type);
        dest.writeString(this.realtype);
    }

    public NewsBean() {
    }

    protected NewsBean(Parcel in) {
        this.title = in.readString();
        this.date = in.readString();
        this.author_name = in.readString();
        this.thumbnail_pic_s = in.readString();
        this.thumbnail_pic_s02 = in.readString();
        this.thumbnail_pic_s03 = in.readString();
        this.url = in.readString();
        this.uniquekey = in.readString();
        this.type = in.readString();
        this.realtype = in.readString();
    }

    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel source) {
            return new NewsBean(source);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };
}
