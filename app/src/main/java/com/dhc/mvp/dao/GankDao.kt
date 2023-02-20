package com.dhc.mvp.dao

import androidx.room.Dao
import com.dhc.mvp.modle.bean.GankItemBean
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * @creator：denghc(desoce)
 * @updateTime：2018/8/23 下午3:42
 * @description：TODO 请描述该类职责
 */
@Dao
interface GankDao {

    @Query("SELECT*FROM gankitem")
    suspend  fun   all(): List<GankItemBean?>

    @Query("SELECT * FROM gankitem WHERE id IN (:id)")
    suspend fun loadAllByIds(id: Array<String>): List<GankItemBean>

    @Insert
    suspend fun insertAll(vararg users: GankItemBean)

    @Delete
    suspend  fun delete(user: GankItemBean?)
}