package com.example.sportsballistics.utils

import com.example.sportsballistics.data.local.AthletesModel

class DummyContent {
    companion object {
        fun loadDummyContentForAthletes(): ArrayList<AthletesModel> {
            val list = ArrayList<AthletesModel>()
            list.add(AthletesModel(0, "70", "Coachabilty", "7", "35"))
            list.add(AthletesModel(1, "60", "Shooting Mechanics", "8", "45"))
            list.add(AthletesModel(2, "80", "Overall Strengths", "9", "55"))
            list.add(AthletesModel(3, "50", "Dribbling", "6", "25"))
            return list
        }
    }
}