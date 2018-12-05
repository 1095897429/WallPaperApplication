package com.ngbj.wallpaper.bean.entityBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HistoryBean {
    @Id
    private Long id;
    private String historyName;
    private String clickTime;//点击时间


    @Generated(hash = 1584943740)
    public HistoryBean(Long id, String historyName, String clickTime) {
        this.id = id;
        this.historyName = historyName;
        this.clickTime = clickTime;
    }

    @Generated(hash = 48590348)
    public HistoryBean() {
    }

    public HistoryBean(String historyName){
        this.historyName = historyName;
    }

    public HistoryBean(String historyName,String clickTime){
        this.historyName = historyName;
        this.clickTime = clickTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistoryName() {
        return historyName;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
    }
}
