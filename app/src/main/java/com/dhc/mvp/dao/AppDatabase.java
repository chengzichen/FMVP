package com.dhc.mvp.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dhc.mvp.modle.bean.GankItemBean;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午3:36
 * @description：数据库管理
 */

@Database(entities = {GankItemBean.class}, version = 1)//对应映射实体类,修改了字段必需升级版本号
public abstract class AppDatabase extends RoomDatabase {

    public abstract GankDao gankDao();

}
