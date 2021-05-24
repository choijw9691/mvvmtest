package com.didimstory.mvvmtest.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.didimstory.mvvmtest.data.api.FIRST_PAGE
import com.didimstory.mvvmtest.data.api.TheMovieDBInterface
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//DataSource: 데이터를 로딩하는 객체입니다. 로컬 또는 Backend의 데이터를 가져오는 역할입니다.
//PageKeyedDataSource: 페이지 기반의 아이템을 로딩하는 DataSource

class MovieDataSource(
    private val apiService: TheMovieDBInterface,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, com.didimstory.mvvmtest.data.vo.Movie>() {
    private var page = FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, com.didimstory.mvvmtest.data.vo.Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(page).subscribeOn(Schedulers.io()).subscribe({

                callback.onResult(it.movies, null, page + 1)
                networkState.postValue(NetworkState.LOADED)
            }, {
                networkState.postValue(NetworkState.ERROR)
                Log.e("MovieDataSource", it.message.toString())
            })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, com.didimstory.mvvmtest.data.vo.Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovie(params.key).subscribeOn(Schedulers.io()).subscribe({
if (it.totalPages >= params.key){

    callback.onResult(it.movies,  params.key + 1)
    networkState.postValue(NetworkState.LOADED)
}else{
networkState.postValue(NetworkState.ENDOFLIST)
}

            }, {
                networkState.postValue(NetworkState.ERROR)
            })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, com.didimstory.mvvmtest.data.vo.Movie>) {



    }


}