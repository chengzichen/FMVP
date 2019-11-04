package com.dhc.library.data

import android.content.Context

import androidx.room.Room
import androidx.room.RoomDatabase

import com.dhc.library.data.cache.ICache

import java.util.HashMap

import javax.inject.Inject

/**
 * 创建者     邓浩宸
 * 创建时间   2018/3/27 16:38
 * 描述	      room数据库帮助类
 */

class DBHelper(var context: Context, internal var iCache: ICache)//Map used to store db
{

    fun <S : RoomDatabase> getApi(serviceClass: Class<S>, dbName: String): S {
        if (iCache.contains(dbName)) {
            return iCache[dbName] as S
        } else {
            val obj = Room.databaseBuilder(context, serviceClass, dbName).build()
            iCache.put(dbName, obj)
            return obj
        }
    }

}
