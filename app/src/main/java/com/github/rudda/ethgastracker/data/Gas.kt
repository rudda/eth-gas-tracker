package com.github.ethgastracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity( tableName = "gas")
data class Gas( @PrimaryKey(autoGenerate = true) val gas_id : Long,
                var low_gwei_id :  Long,
                var med_gwei_id :  Long,
                var high_gwei_id : Long,
                var last_updated : String){

    @Entity
    data class Gwei( @PrimaryKey(autoGenerate = true) val gwei_id : Long,
                                 var price: Int,
                                 var speed : GweiSpeed,
                                 var fee : Int )

    enum class GweiSpeed() {
        FAST, AVERAGE, SAFE_LOW, UNKNOWN,
    }
}
