package com.ngbj.wallpaper.base;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.constant.AppConstant;
import com.ngbj.wallpaper.module.app.WebViewActivity;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/***
 * 统一点击逻辑事件的基类
 */
public abstract class BaesLogicActivity<T extends BaseContract.BasePresenter> extends BaseActivity<T>{

    protected RecomendAdapter recomendAdapter;
    protected GridLayoutManager gridLayoutManager;
    protected List<MulAdBean> recommendList = new ArrayList<>();
    protected  ArrayList<WallpagerBean> temps = new ArrayList<>();//传递给明细界面的数据



    /** ---------------- 公共方法  ------------------  */

    // 临时数据 数据库数据
    protected void commonDtaLogin(String fromWhere,boolean isMore,List<MulAdBean> list) {

        if(!isMore){
            temps.clear();//临时数据清空
            MyApplication.getDbManager().deleteWallpagerBeanList(fromWhere);//删除某来源
        }


        List<WallpagerBean> wTemps = transformDataToWallpaper(fromWhere,list);
        temps.addAll(wTemps);

        for (WallpagerBean wallpagerBean:wTemps) {
            MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);//新增
        }

    }


    protected List<WallpagerBean> transformDataToWallpaper(String fromWhere,List<MulAdBean> recommendList) {
        WallpagerBean wallpagerBean;
        AdBean adBean ;
        ApiAdBean apiAdBean;
        List<WallpagerBean> tempList = new ArrayList<>();
        for (MulAdBean bean: recommendList) {
            if(bean.getItemType() == MulAdBean.TYPE_ONE){
                adBean = bean.adBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(adBean.getType());
                wallpagerBean.setFromWhere(fromWhere);
                wallpagerBean.setIs_collected(adBean.getIs_collected());
                wallpagerBean.setMovie_url(adBean.getMovie_url());
                wallpagerBean.setNickname(adBean.getNickname());
                wallpagerBean.setTitle(adBean.getTitle());
                wallpagerBean.setHead_img(adBean.getHead_img());
                wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                wallpagerBean.setImg_url(adBean.getImg_url());

                if(adBean.getType().equals(AppConstant.COMMON_WP)){
                    wallpagerBean.setWallpager_id(adBean.getId());
                }else{
                    wallpagerBean.setWallpager_id(adBean.getAd_id());
                    wallpagerBean.setLink(adBean.getLink());//TODO 新增的
                }

                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setFromWhere(fromWhere);
                wallpagerBean.setWallpager_id(apiAdBean.getAd_id());
                wallpagerBean.setType(apiAdBean.getType());
                wallpagerBean.setImg_url(apiAdBean.getImgUrl());
                wallpagerBean.setLink(apiAdBean.getLink());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }


//////////////////下面是老的


    // 临时数据 数据库数据
    protected void commonDtaLogin(boolean isMore,List<MulAdBean> list) {

        if(!isMore){
            temps.clear();//临时数据清空
            MyApplication.getDbManager().deleteAllWallpagerBean();//删除
        }


        List<WallpagerBean> wTemps = transformDataToWallpaper(list);
        temps.addAll(wTemps);

        for (WallpagerBean wallpagerBean:wTemps) {
            MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);//新增
        }

    }



    protected List<WallpagerBean> transformDataToWallpaper(List<MulAdBean> recommendList) {
        WallpagerBean wallpagerBean;
        AdBean adBean ;
        ApiAdBean apiAdBean;
        List<WallpagerBean> tempList = new ArrayList<>();
        for (MulAdBean bean: recommendList) {
            if(bean.getItemType() == MulAdBean.TYPE_ONE){
                adBean = bean.adBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(adBean.getType());
                wallpagerBean.setIs_collected(adBean.getIs_collected());
                wallpagerBean.setMovie_url(adBean.getMovie_url());

                if(adBean.getType().equals(AppConstant.COMMON_WP)){
                    wallpagerBean.setWallpager_id(adBean.getId());
                }else{
                    wallpagerBean.setWallpager_id(adBean.getAd_id());
                    wallpagerBean.setLink(adBean.getLink());//TODO 新增的
                }


                wallpagerBean.setNickname(adBean.getNickname());
                wallpagerBean.setTitle(adBean.getTitle());
                wallpagerBean.setHead_img(adBean.getHead_img());
                wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){

                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setWallpager_id(apiAdBean.getAd_id());
                wallpagerBean.setType(apiAdBean.getType());
                wallpagerBean.setImg_url(apiAdBean.getImgUrl());
                wallpagerBean.setLink(apiAdBean.getLink());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }



}
