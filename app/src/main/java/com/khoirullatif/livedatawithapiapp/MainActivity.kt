package com.khoirullatif.livedatawithapiapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.khoirullatif.livedatawithapiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        supportActionBar?.hide()

        mainViewModel.restaurant.observe(this, {
            activityMainBinding.tvTitle.text = it.name
            activityMainBinding.tvDescription.text = it.description
            Glide.with(this)
                .load("https://restaurant-api.dicoding.dev/images/large/${it.pictureId}")
                .into(activityMainBinding.ivPicture)
        })

        mainViewModel.listReviewsItem.observe(this, { consumerReviews ->
            val listReview = consumerReviews.map {
                "${it.review}\n- ${it.name}"
            }

            activityMainBinding.lvReview.adapter =
                ArrayAdapter(this, R.layout.item_review, listReview)

            activityMainBinding.edReview.setText("")
        })

        mainViewModel.isLoading.observe(this, {
            activityMainBinding.progressBar.visibility =
                if (it) View.VISIBLE else View.GONE
        })
    }
}