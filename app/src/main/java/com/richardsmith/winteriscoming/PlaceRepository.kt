package com.richardsmith.winteriscoming

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.content.Context

/**
 * Created by richardsmith on 2017-10-12.
 */

class PlaceRepository(context: Context) {

    private val placeDao: PlaceDao

    init {
        val dataBase = Room
                .databaseBuilder(context, PlaceDatabase::class.java, "places.db")
                .build()
        placeDao = dataBase.placeDao()

    }

    val allPlaces: LiveData<List<Place>> = placeDao.loadAll()

    fun loadPlace(id: Int): LiveData<Place> {
        return placeDao.loadPlace(id)
    }

    fun savePlace(place: Place) {
        placeDao.savePlace(place)
    }

    fun removePlace(place: Place) {
        placeDao.removePlace(place)
    }
}