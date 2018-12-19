package com.ngbj.wallpaper.bean.entityBean;

/***
 * banner详情页
 */
public class BannerDetailBean {

        private String id;
        private String title;
        private String img_url;
        private String comment;
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

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
        public String getImg_url() {
            return img_url;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
        public String getComment() {
            return comment;
        }
}
