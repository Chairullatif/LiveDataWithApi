package com.khoirullatif.livedatawithapiapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    //di bawah ini contoh encapsulation pada LiveData
    // bisa dinamakan backing property
    //dimana nilai mutable tidak dapat diubah dari luar kelas viewModel

    private val _restaurant = MutableLiveData<Restaurant>()
    private val _listReviews = MutableLiveData<List<CustomerReviewsItem>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _snackbarText = MutableLiveData<String>()

    val restaurant: LiveData<Restaurant> = _restaurant
    val listReviewsItem: LiveData<List<CustomerReviewsItem>> = _listReviews
    val isLoading: LiveData<Boolean> = _isLoading
    val snackbarText: LiveData<String> = _snackbarText

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

    fun postReview(review: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Choi", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(
                call: Call<PostReviewResponse>,
                response: Response<PostReviewResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _listReviews.value = response.body()?.customerReviews
                    _snackbarText.value = response.body()?.message
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}", )
                }
            }

            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}