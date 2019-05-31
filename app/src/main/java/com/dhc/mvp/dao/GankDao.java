package com.dhc.mvp.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.dhc.mvp.modle.bean.GankItemBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午3:42
 * @description：TODO 请描述该类职责
 */
@Dao
public interface GankDao {

    @Query("SELECT*FROM gankitem")
    Flowable<List<GankItemBean>> getAll();

    @Query("SELECT * FROM gankitem WHERE id IN (:id)")
    Flowable<List<GankItemBean>> loadAllByIds(String[] id);

    @Insert
    void insertAll(GankItemBean... users);

    @Delete
    void delete(GankItemBean user);
}
