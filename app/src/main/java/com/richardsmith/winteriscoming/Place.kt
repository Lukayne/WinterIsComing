package com.richardsmith.winteriscoming

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by richardsmith on 2017-10-11.
 */

@Entity(tableName = "place")
data class Place(@PrimaryKey(autoGenerate = true)val id: Int, val city: String)

@Dao
interface PlaceDao {
    @Query("SELECT id, city FROM place")
    fun loadAll(): LiveData<List<Place>>

    @Query("SELECT id, city FROM place WHERE id = :id")
    fun loadPlace(id: Int) : LiveData<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun savePlace(place: Place)

    @Delete
    fun removePlace(place: Place)
}

@Database(entities = arrayOf(Place::class), version = 1)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}

class PlaceViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG = "PlaceViewModel"
    }


    private val executor: Executor = Executors.newCachedThreadPool()
    private val repository: PlaceRepository = PlaceRepository(application)

    var allPlaces: LiveData<List<Place>>

    init {
        allPlaces = repository.allPlaces
    }

    override fun onCleared() {
        super.onCleared()
        (executor as ExecutorService).shutdown()
    }

    fun loadPlace(id: Int): MediatorLiveData<Place> {
        val mediatorLiveData = MediatorLiveData<Place>()

        mediatorLiveData.addSource(repository.loadPlace(id), Observer {
            mediatorLiveData.postValue(it)
        })

        return mediatorLiveData
    }

    fun savePlace(place: Place) {
        executor.execute {
            repository.savePlace(place)
        }
    }
    fun removePlace(place: Place) {
        executor.execute {
            repository.removePlace(place)
        }
    }

}
