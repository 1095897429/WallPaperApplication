package com.ngbj.wallpaper.bean.greenBeanDao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ngbj.wallpaper.bean.entityBean.HistoryBean;

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

    /** =========================================================================== */


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
        qb.orderDesc(HistoryBeanDao.Properties.ClickTime);
        List<HistoryBean> list = qb.list();
        return list;
    }


}
