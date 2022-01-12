package com.khoirullatif.livedatawithapiapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _restaurant = MutableLiveData<Restaurant>()
    private val _listReviews = MutableLiveData<List<CustomerReviewsItem>>()
    private val _isLoading = MutableLiveData<Boolean>()

    val restaurant: LiveData<Restaurant> = _restaurant
    val listReviewsItem: LiveData<List<CustomerReviewsItem>> = _listReviews
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
        private const val RESTAURANT_ID = "ateyf7m737ekfw1e867"
    }

    init {
        findRestaurant()
    }

    private fun findRestaurant() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _restaurant.value = response.body()?.restaurant
                    _listReviews.value = response.body()?.restaurant?.customerReviews
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}