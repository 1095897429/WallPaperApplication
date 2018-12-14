package com.ngbj.wallpaper.bean.entityBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/***
 * 首页的总数据
 */
public class IndexBean implements Parcelable {



    private List<HotSearch> hotSearch;
    private List<Banner> banner;
    private List<Navigation> navigation;
    private List<AdBean> recommend;

    public void setHotSearch(List<HotSearch> hotSearch) {
        this.hotSearch = hotSearch;
    }

    public List<HotSearch> getHotSearch() {
        return hotSearch;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setNavigation(List<Navigation> navigation) {
        this.navigation = navigation;
    }

    public List<Navigation> getNavigation() {
        return navigation;
    }

    public List<AdBean> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<AdBean> recommend) {
        this.recommend = recommend;
    }

    /** 热门搜索 */
    public static class HotSearch implements Parcelable {
        private String id;
        private String title;
        private String link;
        private String img_url;
        private String begin_time;
        private String end_time;
        private String type;
        private String show_position;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setShow_position(String show_position) {
            this.show_position = show_position;
        }

        public String getShow_position() {
            return show_position;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.link);
            dest.writeString(this.img_url);
            dest.writeString(this.begin_time);
            dest.writeString(this.end_time);
            dest.writeString(this.type);
            dest.writeString(this.show_position);
        }

        public HotSearch() {
        }

        protected HotSearch(Parcel in) {
            this.id = in.readString();
            this.title = in.readString();
            this.link = in.readString();
            this.img_url = in.readString();
            this.begin_time = in.readString();
            this.end_time = in.readString();
            this.type = in.readString();
            this.show_position = in.readString();
        }

        public static final Parcelable.Creator<HotSearch> CREATOR = new Parcelable.Creator<HotSearch>() {
            @Override
            public HotSearch createFromParcel(Parcel source) {
                return new HotSearch(source);
            }

            @Override
            public HotSearch[] newArray(int size) {
                return new HotSearch[size];
            }
        };
    }

    /** banner */
    public static class Banner implements Parcelable {
        private String id;
        private String title;
        private String link;
        private String img_url;
        private String begin_time;
        private String end_time;
        private String type;
        private String show_position;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setShow_position(String show_position) {
            this.show_position = show_position;
        }

        public String getShow_position() {
            return show_position;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.link);
            dest.writeString(this.img_url);
            dest.writeString(this.begin_time);
            dest.writeString(this.end_time);
            dest.writeString(this.type);
            dest.writeString(this.show_position);
        }

        public Banner() {
        }

        protected Banner(Parcel in) {
            this.id = in.readString();
            this.title = in.readString();
            this.link = in.readString();
            this.img_url = in.readString();
            this.begin_time = in.readString();
            this.end_time = in.readString();
            this.type = in.readString();
            this.show_position = in.readString();
        }

        public static final Parcelable.Creator<Banner> CREATOR = new Parcelable.Creator<Banner>() {
            @Override
            public Banner createFromParcel(Parcel source) {
                return new Banner(source);
            }

            @Override
            public Banner[] newArray(int size) {
                return new Banner[size];
            }
        };
    }

    /** navigation */
    public static class Navigation implements Parcelable {
        private String id;
        private String title;
        private String link;
        private String img_url;
        private String begin_time;
        private String end_time;
        private String type;
        private String show_position;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setShow_position(String show_position) {
            this.show_position = show_position;
        }

        public String getShow_position() {
            return show_position;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.link);
            dest.writeString(this.img_url);
            dest.writeString(this.begin_time);
            dest.writeString(this.end_time);
            dest.writeString(this.type);
            dest.writeString(this.show_position);
        }

        public Navigation() {
        }

        protected Navigation(Parcel in) {
            this.id = in.readString();
            this.title = in.readString();
            this.link = in.readString();
            this.img_url = in.readString();
            this.begin_time = in.readString();
            this.end_time = in.readString();
            this.type = in.readString();
            this.show_position = in.readString();
        }

        public static final Parcelable.Creator<Navigation> CREATOR = new Parcelable.Creator<Navigation>() {
            @Override
            public Navigation createFromParcel(Parcel source) {
                return new Navigation(source);
            }

            @Override
            public Navigation[] newArray(int size) {
                return new Navigation[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.hotSearch);
        dest.writeTypedList(this.banner);
        dest.writeTypedList(this.navigation);
        dest.writeTypedList(this.recommend);
    }

    public IndexBean() {
    }

    protected IndexBean(Parcel in) {
        this.hotSearch = in.createTypedArrayList(HotSearch.CREATOR);
        this.banner = in.createTypedArrayList(Banner.CREATOR);
        this.navigation = in.createTypedArrayList(Navigation.CREATOR);
        this.recommend = in.createTypedArrayList(AdBean.CREATOR);
    }

    public static final Parcelable.Creator<IndexBean> CREATOR = new Parcelable.Creator<IndexBean>() {
        @Override
        public IndexBean createFromParcel(Parcel source) {
            return new IndexBean(source);
        }

        @Override
        public IndexBean[] newArray(int size) {
            return new IndexBean[size];
        }
    };
}
