package com.ngbj.wallpaper.bean.greenBeanDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ngbj.wallpaper.bean.entityBean.AdBean;
import com.ngbj.wallpaper.bean.entityBean.DownBean;
import com.ngbj.wallpaper.bean.entityBean.HistoryBean;
import com.ngbj.wallpaper.bean.entityBean.LoginBean;
import com.ngbj.wallpaper.bean.entityBean.TestBean;
import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/***
 * 全局只有一个
 */
public class DBManager {

    private final static String dbName = "wallpaper_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /** ========================= 历史记录  开始================================================== */


    /** 插入一条历史记录 */
    public void insertHistrory(HistoryBean historyBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao userDao = daoSession.getHistoryBeanDao();
        userDao.insert(historyBean);
    }


    /** 插入历史记录集合 */
    public void insertHistoryList(List<HistoryBean> historyBeanList) {
        if (historyBeanList == null || historyBeanList.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao historyBeanDao = daoSession.getHistoryBeanDao();
        historyBeanDao.insertInTx(historyBeanList);
    }

    /** 更新一条浏览广告 */
    public void updateHistory(HistoryBean historyBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao historyBeanDao = daoSession.getHistoryBeanDao();
        historyBeanDao.update(historyBean);
    }



    /** 删除一条记录 */
    public void deleteHistory(HistoryBean historyBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao historyBeanDao = daoSession.getHistoryBeanDao();
        historyBeanDao.delete(historyBean);
    }


    /** 删除所有历史记录 */
    public void deleteAllHistoryBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao historyDataDao = daoSession.getHistoryBeanDao();
        historyDataDao.deleteAll();
    }

    /** 查询一条历史记录 */
    public HistoryBean queryHistory(String historyName) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao userDao = daoSession.getHistoryBeanDao();
        HistoryBean historyBean = userDao.queryBuilder().where(HistoryBeanDao.Properties.HistoryName.eq(historyName)).unique();
        return historyBean;
    }

    /** 查询所有历史记录 降序 */
    public List<HistoryBean> queryHistoryList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao userDao = daoSession.getHistoryBeanDao();
        QueryBuilder<HistoryBean> qb = userDao.queryBuilder().orderDesc(HistoryBeanDao.Properties.ClickTime);
        List<HistoryBean> list = qb.list();
        return list;
    }


    /** 查询一定数量历史列表 */
    public List<HistoryBean> queryHistoryLimitList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        HistoryBeanDao userDao = daoSession.getHistoryBeanDao();
        QueryBuilder<HistoryBean> qb = userDao.queryBuilder();
        qb.orderDesc(HistoryBeanDao.Properties.ClickTime).limit(5);
        List<HistoryBean> list = qb.list();
        return list;
    }

    /** ========================= 历史记录  结束================================================== */




    /** ========================= 壁纸数据  开始================================================== */

    /** 插入一条壁纸记录 */
    public void insertWallpagerBean(WallpagerBean wallpagerBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        userDao.insert(wallpagerBean);
    }


    /** 插入壁纸记录集合 */
    public void inserWallpagerBeanList(List<WallpagerBean> wallpagerBeanList) {
        if (wallpagerBeanList == null || wallpagerBeanList.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao wallpagerBeanDao = daoSession.getWallpagerBeanDao();
        wallpagerBeanDao.insertInTx(wallpagerBeanList);
    }

    /** 更新一条壁纸 */
    public void updateWallpagerBean(WallpagerBean wallpagerBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao wallpagerBeanDao = daoSession.getWallpagerBeanDao();
        wallpagerBeanDao.update(wallpagerBean);
    }



    /** 删除一条壁纸 */
    public void deleteWallpagerBean(WallpagerBean wallpagerBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao historyBeanDao = daoSession.getWallpagerBeanDao();
        historyBeanDao.delete(wallpagerBean);
    }


    /** 删除所有壁纸记录 */
    public void deleteAllWallpagerBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao historyDataDao = daoSession.getWallpagerBeanDao();
        historyDataDao.deleteAll();
    }

    /** 查询一条壁纸记录 */
    public WallpagerBean queryWallpagerBean(String wallpaperId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        WallpagerBean wallpagerBean = userDao.queryBuilder().where(WallpagerBeanDao.Properties.Wallpager_id.eq(wallpaperId)).unique();
        return wallpagerBean;
    }

    /** 查询所有壁纸记录 降序 */
    public List<WallpagerBean> queryWallpagerBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        QueryBuilder<WallpagerBean> qb = userDao.queryBuilder();
        List<WallpagerBean> list = qb.list();
        return list;
    }


    /** 查询一定数量壁纸列表 */
    public List<WallpagerBean> queryWallpagerBeanLimitList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        QueryBuilder<WallpagerBean> qb = userDao.queryBuilder();
        List<WallpagerBean> list = qb.list();
        return list;
    }

    /** ========================= 壁纸数据  结束================================================== */


    /** 根据条件查询壁纸记录 */
    public List<WallpagerBean> queryDifferWPId(String wallpaperId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        List<WallpagerBean> wallpagerBeanList = userDao.queryBuilder().where(WallpagerBeanDao.Properties.Wallpager_id.eq(wallpaperId)).list();
        return wallpagerBeanList;
    }


    /** 根据条件查询壁纸记录 */
    public List<WallpagerBean> queryDifferCome(String fromWhere) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        List<WallpagerBean> wallpagerBeanList = userDao.queryBuilder().where(WallpagerBeanDao.Properties.FromWhere.eq(fromWhere)).list();
        return wallpagerBeanList;
    }


    /** 根据条件查询一条壁纸记录 */
    public WallpagerBean queryWallpager(String wallpaperId,String fromWhere) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WallpagerBeanDao userDao = daoSession.getWallpagerBeanDao();
        WallpagerBean wallpagerBean = userDao.queryBuilder()
                .where(WallpagerBeanDao.Properties.Wallpager_id.eq(wallpaperId),
                        WallpagerBeanDao.Properties.FromWhere.eq(fromWhere)).unique();
        return wallpagerBean;
    }



    /** 根据条件删除所有壁纸记录 */
    public void deleteWallpagerBeanList(String fromWhere) {
        List<WallpagerBean> list = queryDifferCome(fromWhere);
        for (WallpagerBean bean : list) {
            deleteWallpagerBean(bean);
        }
    }


    /** ========================= 下载  开始================================================== */

    /** 插入一条下载壁纸记录 */
    public void insertDownBean(DownBean downBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownBeanDao userDao = daoSession.getDownBeanDao();
        userDao.insert(downBean);
    }

    /** 更新一条下载壁纸记录 */
    public void updateDownBean(DownBean downBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownBeanDao downBeanDao = daoSession.getDownBeanDao();
        downBeanDao.update(downBean);
    }


    /** 查询一条下载壁纸记录 */
    public DownBean queryDownBean() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DownBeanDao userDao = daoSession.getDownBeanDao();
        DownBean downBean = userDao.queryBuilder().unique();
        return downBean;
    }

    /** ========================= 下载  结束================================================== */



    /** ========================= 登录  开始================================================== */

    /** 插入一条用户记录 */
    public void insertLoginBean(LoginBean loginBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LoginBeanDao userDao = daoSession.getLoginBeanDao();
        userDao.insert(loginBean);
    }

    /** 更新一条用户记录 */
    public void updateLoginBean(LoginBean loginBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LoginBeanDao loginBeanDao = daoSession.getLoginBeanDao();
        loginBeanDao.update(loginBean);
    }


    /** 查询一条用户记录 */
    public LoginBean queryLoginBean() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LoginBeanDao loginBeanDao  = daoSession.getLoginBeanDao();
        LoginBean loginBean = loginBeanDao.queryBuilder().unique();
        return loginBean;
    }


    /** 删除用户记录 */
    public void deleteAllLoginBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LoginBeanDao loginBeanDao = daoSession.getLoginBeanDao();
        loginBeanDao.deleteAll();
    }


    /** ========================= 登录  结束================================================== */

    /** =========================   开始================================================== */

    /** 插入一条用户记录 */
    public void insertTestBean(TestBean testBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        TestBeanDao userDao = daoSession.getTestBeanDao();
        userDao.insert(testBean);
    }

//    /** 更新一条用户记录 */
//    public void updateLoginBean(LoginBean loginBean) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        LoginBeanDao loginBeanDao = daoSession.getLoginBeanDao();
//        loginBeanDao.update(loginBean);
//    }


    /** 查询一条用户记录 */
    public TestBean queryTestBean() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        TestBeanDao testBeanDao  = daoSession.getTestBeanDao();
        TestBean testBean = testBeanDao.queryBuilder().unique();
        return testBean;
    }


//    /** 删除用户记录 */
    public void deleteAllTestBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        TestBeanDao loginBeanDao = daoSession.getTestBeanDao();
        loginBeanDao.deleteAll();
    }


    /** ========================= 登录  结束================================================== */



}
