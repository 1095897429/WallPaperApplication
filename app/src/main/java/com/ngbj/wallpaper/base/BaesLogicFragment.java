package com.ngbj.wallpaper.base;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ngbj.wallpaper.adapter.index.RecomendAdapter;
import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.ApiAdBean;
import com.ngbj.wallpaper.bean.entityBean.MulAdBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;
import com.ngbj.wallpaper.utils.common.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/***
 * 统一点击逻辑事件的基类
 */
public abstract class BaesLogicFragment<T extends BaseContract.BasePresenter> extends BaseFragment<T>{

    protected RecomendAdapter recomendAdapter;
    protected GridLayoutManager gridLayoutManager;
//    protected List<MulAdBean> recommendList = new ArrayList<>();
//    protected AdBean mAdBean;


    @Override
    protected void initEvent() {

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
                wallpagerBean.setWallpager_id(adBean.getId());
                wallpagerBean.setNickname(adBean.getNickname());
                wallpagerBean.setTitle(adBean.getTitle());
                wallpagerBean.setHead_img(adBean.getHead_img());
                wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                wallpagerBean.setImg_url(adBean.getImg_url());
                wallpagerBean.setLink(adBean.getLink());//TODO 新增的
                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(apiAdBean.getType());
                wallpagerBean.setImg_url(apiAdBean.getImgUrl());
                wallpagerBean.setLink(apiAdBean.getLink());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }


}
