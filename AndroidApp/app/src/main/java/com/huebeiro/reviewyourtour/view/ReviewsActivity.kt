package com.huebeiro.reviewyourtour.view

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.huebeiro.reviewyourtour.BR
import com.huebeiro.reviewyourtour.MainApp
import com.huebeiro.reviewyourtour.R
import com.huebeiro.reviewyourtour.databinding.ReviewsBinding
import com.huebeiro.reviewyourtour.model.ReviewsResponse
import com.huebeiro.reviewyourtour.recyclerview.adapter.ClickHandler
import com.huebeiro.reviewyourtour.recyclerview.adapter.binder.ItemBinder
import com.huebeiro.reviewyourtour.recyclerview.adapter.binder.ItemBinderBase
import com.huebeiro.reviewyourtour.request.ReviewsRequest
import com.huebeiro.reviewyourtour.viewmodel.ReviewViewModel
import com.huebeiro.reviewyourtour.viewmodel.ReviewsViewModel
import java.util.*

/**
 * Author: Adilson Ribeiro
 * Date: 30/03/18
 */

class ReviewsActivity : AppCompatActivity() {

    private var binding: ReviewsBinding? = null
    private var viewModel = ReviewsViewModel()
    private val TAG = "ReviewsActivity"
    private val addReviewRequest = 4123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding the view
        binding = DataBindingUtil.setContentView(this, R.layout.reviews)
        binding!!.viewModel = viewModel
        binding!!.view = this
        binding!!.listReviews.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL,false)
        binding!!.listReviews.addItemDecoration(
                DividerItemDecoration(this,  LinearLayoutManager.VERTICAL)
        )

        //Make a initial request
        requestReviews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == addReviewRequest){
            if(resultCode == Activity.RESULT_OK){
                refreshList(true)
            }
        }
    }

    //API methods
    fun requestReviews(count: Int = 100){
        viewModel.loading.set(true)
        val request = ReviewsRequest(
                count = count,
                responseListener = Response.Listener { t: ReviewsResponse ->
                    onResponse(t)
                },
                errorListener = Response.ErrorListener { volleyError ->
                    onError(volleyError)
                }
        )
        MainApp.mainRequestQueue.add(request)
    }

    fun onResponse(response: ReviewsResponse){
        Log.d(TAG, "onResponse - Total Reviews: " + response.data.size)
        Thread(Runnable {
            MainApp.mainDatabase.reviewDao().deleteAll()
            MainApp.mainDatabase.reviewDao().insertAll(
                    * response.data.toTypedArray()
            )
            refreshList()
        }).start()
    }

    fun onError(volleyError: VolleyError){
        Log.e(TAG, "Error while processing the request", volleyError)
        Snackbar.make(binding!!.mainView, R.string.reviews_activity_error, Snackbar.LENGTH_LONG).show()
        refreshList() //If there is something wrong only shows persisted data
    }

    fun onFiltersChanged(){
        refreshList()
    }

    //View methods
    fun refreshList(scrollList: Boolean = false){
        Thread(Runnable {
            val reviews =
                    if (binding!!.checkboxLanguage.isChecked)
                        MainApp.mainDatabase.reviewDao().getReviewsByLanguage(
                                Locale.getDefault().language
                        )
                    else
                        MainApp.mainDatabase.reviewDao().all
            val modelReviews = ArrayList<ReviewViewModel>()
            for(review in reviews){
                modelReviews.add(ReviewViewModel(review))
            }
            //passing all items at once to the view
            Handler(Looper.getMainLooper()).post {
                viewModel.reviews.clear()
                viewModel.reviews.addAll(modelReviews)
                viewModel.loading.set(false)
                if(scrollList) //Scrolling to inserted Review
                    binding!!.listReviews.scrollToPosition(modelReviews.size -1)
            }
        }).start()
    }

    fun swipeRefresh(){
        requestReviews()
    }

    fun addReview(){
        startActivityForResult(Intent(
                this,
                AddReviewActivity::class.java
        ), addReviewRequest)
    }

    //RecyclerView functions
    fun itemViewBinder(): ItemBinder<ReviewViewModel> {
        return ItemBinderBase<ReviewViewModel>(BR.review, R.layout.row_review)
    }

    fun clickHandler() : ClickHandler<ReviewViewModel>{
        return object : ClickHandler<ReviewViewModel>{
            override fun onClick(viewModel: ReviewViewModel?) {
                if(viewModel != null){
                    Log.i(TAG, "Object Click: " + viewModel.review.title)
                }
            }
        }
    }
}
