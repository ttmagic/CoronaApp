package com.ttmagic.corona.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class StatsWorldResponse(
    val Data: List<StatsWorld>
)

@Entity
data class StatsWorld(
    val ActiveCase: String,
    val CasesHn: String,
    val CasesVn: String,
    val CasesWorld: String,
    val CountryOther: String,
    val DeadHn: String,
    val DeadVn: String,
    val DeadWorld: String,
    @PrimaryKey
    val Id: String,
    val NewCases: String,
    val NewDeaths: String,
    val RecoveredHn: String,
    val RecoveredVn: String,
    val RecoveredWorld: String,
    val SeriosCritical: String,
    val TotalCases: String,
    val TotalDeadths: String,
    val TotalRecovered: String
) : com.base.mvvm.Entity() {
    override val uniqueId: Any
        get() = Id
}


data class Summary(val world: Info, val vn: Info)
data class Info(val cases: String, val dead: String, val recovered: String)

//        {
//            "CountryOther": "China",
//            "TotalCases": "80967",
//            "NewCases": "39",
//            "TotalDeadths": "3248",
//            "NewDeaths": "3",
//            "ActiveCase": "6569",
//            "TotalRecovered": "71150",
//            "SeriosCritical": "2136",
//            "CasesWorld": "255796",           //Summary: Case World
//            "CasesVn": "91",                  //Summary: Case VN
//            "DeadWorld": "10495",             //Summary: Dead World
//            "DeadVn": "0",                    //Summary: Dead VN
//            "RecoveredWorld": "89918",        //Summary: Recovered Word
//            "RecoveredVn": "17",              //Summary: Recovered VN
//            "CasesHn": "26",
//            "DeadHn": "0",
//            "RecoveredHn": "",
//            "Id": "c62ae5c2-6faf-493e-913f-325679b0f773",
//        }
