package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * 极光推送下来的实体
 */
public class JpushExtras implements Parcelable {
    private String type;
    private String param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.param);
    }

    public JpushExtras() {
    }

    protected JpushExtras(Parcel in) {
        this.type = in.readString();
        this.param = in.readString();
    }

    public static final Parcelable.Creator<JpushExtras> CREATOR = new Parcelable.Creator<JpushExtras>() {
        @Override
        public JpushExtras createFromParcel(Parcel source) {
            return new JpushExtras(source);
        }

        @Override
        public JpushExtras[] newArray(int size) {
            return new JpushExtras[size];
        }
    };
}
