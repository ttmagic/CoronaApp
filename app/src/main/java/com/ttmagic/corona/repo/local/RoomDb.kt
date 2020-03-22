package com.ttmagic.corona.repo.local

import androidx.room.*
import com.ttmagic.corona.App
import com.ttmagic.corona.model.Converters
import com.ttmagic.corona.model.Patient
import com.ttmagic.corona.model.StatsVn
import com.ttmagic.corona.model.StatsWorld
import com.ttmagic.corona.util.Const

@Dao
interface PatientDao {
    @Query("SELECT * FROM patient")
    suspend fun getAll(): List<Patient>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(list: List<Patient>)

}


@Dao
interface StatsVnDao {
    @Query("SELECT * FROM StatsVn")
    suspend fun getAll(): List<StatsVn>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(list: List<StatsVn>)

}

@Dao
interface StatsWorldDao {
    @Query("SELECT * FROM StatsWorld")
    suspend fun getAll(): List<StatsWorld>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(list: List<StatsWorld>)

}


@Database(entities = [Patient::class, StatsVn::class, StatsWorld::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): PatientDao
    abstract fun statsVnDao(): StatsVnDao
    abstract fun statsWorldDao(): StatsWorldDao
}


val localDb by lazy {
    Room.databaseBuilder(App.context, AppDatabase::class.java, Const.DB_NAME)
        .allowMainThreadQueries()
        .build()
}