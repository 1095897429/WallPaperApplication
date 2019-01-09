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



    /** ---------------- 公共方法  ------------------  */
    /** page == 1 表示初始化请求  其他情况是加载更多*/
    protected void insertToSql(int page, final List<MulAdBean> recommendList, final String fromWhere){

        if(1 == page){
            MyApplication.getDbManager().deleteWallpagerBeanList(fromWhere);

        }

        //TODO 线程加入到数据库中 -- 先删除，后添加
        new Thread(new Runnable() {
            @Override
            public void run() {
                WallpagerBean wallpagerBean;
                AdBean adBean ;
                ApiAdBean apiAdBean;
                for (MulAdBean bean: recommendList) {
                    if(bean.getItemType() == MulAdBean.TYPE_ONE){
                        adBean = bean.adBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setFromWhere(fromWhere);
                        wallpagerBean.setType(adBean.getType());
                        wallpagerBean.setIs_collected(adBean.getIs_collected());
                        wallpagerBean.setMovie_url(adBean.getMovie_url());
                        wallpagerBean.setWallpager_id(adBean.getId());
                        wallpagerBean.setNickname(adBean.getNickname());
                        wallpagerBean.setTitle(adBean.getTitle());
                        wallpagerBean.setHead_img(adBean.getHead_img());
                        wallpagerBean.setThumb_img_url(adBean.getThumb_img_url());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                        apiAdBean = bean.apiAdBean;
                        wallpagerBean = new WallpagerBean();
                        wallpagerBean.setFromWhere(fromWhere);
                        wallpagerBean.setType(apiAdBean.getType());
                        MyApplication.getDbManager().insertWallpagerBean(wallpagerBean);
                    }
                }
            }
        }).start();
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
                tempList.add(wallpagerBean);
            }else if(bean.getItemType() == MulAdBean.TYPE_TWO){
                apiAdBean = bean.apiAdBean;
                wallpagerBean = new WallpagerBean();
                wallpagerBean.setType(apiAdBean.getType());
                tempList.add(wallpagerBean);
            }
        }
        return tempList;
    }



}
