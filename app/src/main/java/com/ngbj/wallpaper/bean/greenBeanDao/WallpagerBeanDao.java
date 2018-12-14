package com.ngbj.wallpaper.bean.greenBeanDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.ngbj.wallpaper.bean.entityBean.WallpagerBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WALLPAGER_BEAN".
*/
public class WallpagerBeanDao extends AbstractDao<WallpagerBean, Long> {

    public static final String TABLENAME = "WALLPAGER_BEAN";

    /**
     * Properties of entity WallpagerBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Type = new Property(1, String.class, "type", false, "TYPE");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Movie_url = new Property(3, String.class, "movie_url", false, "MOVIE_URL");
        public final static Property Wallpager_id = new Property(4, String.class, "wallpager_id", false, "WALLPAGER_ID");
        public final static Property Nickname = new Property(5, String.class, "nickname", false, "NICKNAME");
        public final static Property Head_img = new Property(6, String.class, "head_img", false, "HEAD_IMG");
        public final static Property Thumb_img_url = new Property(7, String.class, "thumb_img_url", false, "THUMB_IMG_URL");
        public final static Property Is_collected = new Property(8, String.class, "is_collected", false, "IS_COLLECTED");
        public final static Property Category_id = new Property(9, String.class, "category_id", false, "CATEGORY_ID");
        public final static Property Category_name = new Property(10, String.class, "category_name", false, "CATEGORY_NAME");
        public final static Property Img_url = new Property(11, String.class, "img_url", false, "IMG_URL");
    };


    public WallpagerBeanDao(DaoConfig config) {
        super(config);
    }
    
    public WallpagerBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WALLPAGER_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TYPE\" TEXT," + // 1: type
                "\"TITLE\" TEXT," + // 2: title
                "\"MOVIE_URL\" TEXT," + // 3: movie_url
                "\"WALLPAGER_ID\" TEXT," + // 4: wallpager_id
                "\"NICKNAME\" TEXT," + // 5: nickname
                "\"HEAD_IMG\" TEXT," + // 6: head_img
                "\"THUMB_IMG_URL\" TEXT," + // 7: thumb_img_url
                "\"IS_COLLECTED\" TEXT," + // 8: is_collected
                "\"CATEGORY_ID\" TEXT," + // 9: category_id
                "\"CATEGORY_NAME\" TEXT," + // 10: category_name
                "\"IMG_URL\" TEXT);"); // 11: img_url
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WALLPAGER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WallpagerBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(2, type);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String movie_url = entity.getMovie_url();
        if (movie_url != null) {
            stmt.bindString(4, movie_url);
        }
 
        String wallpager_id = entity.getWallpager_id();
        if (wallpager_id != null) {
            stmt.bindString(5, wallpager_id);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(6, nickname);
        }
 
        String head_img = entity.getHead_img();
        if (head_img != null) {
            stmt.bindString(7, head_img);
        }
 
        String thumb_img_url = entity.getThumb_img_url();
        if (thumb_img_url != null) {
            stmt.bindString(8, thumb_img_url);
        }
 
        String is_collected = entity.getIs_collected();
        if (is_collected != null) {
            stmt.bindString(9, is_collected);
        }
 
        String category_id = entity.getCategory_id();
        if (category_id != null) {
            stmt.bindString(10, category_id);
        }
 
        String category_name = entity.getCategory_name();
        if (category_name != null) {
            stmt.bindString(11, category_name);
        }
 
        String img_url = entity.getImg_url();
        if (img_url != null) {
            stmt.bindString(12, img_url);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WallpagerBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(2, type);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String movie_url = entity.getMovie_url();
        if (movie_url != null) {
            stmt.bindString(4, movie_url);
        }
 
        String wallpager_id = entity.getWallpager_id();
        if (wallpager_id != null) {
            stmt.bindString(5, wallpager_id);
        }
 
        String nickname = entity.getNickname();
        if (nickname != null) {
            stmt.bindString(6, nickname);
        }
 
        String head_img = entity.getHead_img();
        if (head_img != null) {
            stmt.bindString(7, head_img);
        }
 
        String thumb_img_url = entity.getThumb_img_url();
        if (thumb_img_url != null) {
            stmt.bindString(8, thumb_img_url);
        }
 
        String is_collected = entity.getIs_collected();
        if (is_collected != null) {
            stmt.bindString(9, is_collected);
        }
 
        String category_id = entity.getCategory_id();
        if (category_id != null) {
            stmt.bindString(10, category_id);
        }
 
        String category_name = entity.getCategory_name();
        if (category_name != null) {
            stmt.bindString(11, category_name);
        }
 
        String img_url = entity.getImg_url();
        if (img_url != null) {
            stmt.bindString(12, img_url);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public WallpagerBean readEntity(Cursor cursor, int offset) {
        WallpagerBean entity = new WallpagerBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // type
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // movie_url
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // wallpager_id
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // nickname
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // head_img
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // thumb_img_url
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // is_collected
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // category_id
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // category_name
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // img_url
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WallpagerBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMovie_url(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWallpager_id(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNickname(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setHead_img(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setThumb_img_url(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIs_collected(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCategory_id(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCategory_name(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setImg_url(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(WallpagerBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(WallpagerBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}