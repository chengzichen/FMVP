package com.dhc.library.data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dhc.library.data.cache.ICache;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * 创建者     邓浩宸
 * 创建时间   2018/3/27 16:38
 * 描述	      room数据库帮助类
 */

public class DBHelper {

    @Inject
    ICache iCache;
    public Context context;

    public DBHelper(Context context) {
        //Map used to store db
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <S extends RoomDatabase> S getApi(Class<S> serviceClass, String dbName) {
        if (iCache.contains(dbName)) {
            return (S) iCache.get(dbName);
        } else {
            Object obj = Room.databaseBuilder(context, serviceClass, dbName).build();
            iCache.put(dbName, obj);
            return (S) obj;
        }
    }

}
