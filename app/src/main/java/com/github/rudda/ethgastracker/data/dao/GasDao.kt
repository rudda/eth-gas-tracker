package com.github.ethgastracker.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.ethgastracker.data.Gas

@Dao
interface GasDao {
    @Query("Select * from gas ORDER by last_updated DESC limit :limit")
    fun getCurrentGasPrice(limit: Int): LiveData<Gas>

    @Query("Select * from gas ORDER by last_updated DESC limit 1")
    fun getCurrentGasPrice(): Gas

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGwei( newGwei : Gas.Gwei) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGas(newGas : Gas): Long

    @Query("Select * from gwei where gwei_id in (:ids)")
    fun getGweis(ids: Array<Long>): LiveData<List<Gas.Gwei>>

    @Query("Select * from gwei where gwei_id = :id")
    fun getGwei(id: Long): Gas.Gwei

}