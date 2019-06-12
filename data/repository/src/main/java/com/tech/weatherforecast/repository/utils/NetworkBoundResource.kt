package com.tech.weatherforecast.repository.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun build(): NetworkBoundResource<ResultType, RequestType> {
        val dbResult = loadFromDb()
        if (shouldFetch(dbResult.value)) {
            fetchFromNetwork()
        } else {
            Log.d(NetworkBoundResource::class.java.name, "Return data from local database")
            setValue(Resource.success(dbResult.value))
        }

        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    // ---

    private fun fetchFromNetwork() {
        Log.d(NetworkBoundResource::class.java.name, "Fetch data from network")

        disposables.add(createCallAsync()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                saveCallResult(processResponse(it))
                setValue(Resource.success(loadFromDb().value))
            }, { error ->
                setValue(Resource.error(error, null))
            }))

        Log.e(NetworkBoundResource::class.java.name, "Data fetched from network")
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        Log.d(NetworkBoundResource::class.java.name, "Resource: "+newValue)
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @WorkerThread
    protected abstract fun saveCallResult(item: ResultType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCallAsync(): Flowable<RequestType>
}