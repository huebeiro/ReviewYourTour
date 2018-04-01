package com.huebeiro.reviewyourtour.view

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.huebeiro.reviewyourtour.MainApp
import com.huebeiro.reviewyourtour.R
import com.huebeiro.reviewyourtour.databinding.AddreviewBinding
import com.huebeiro.reviewyourtour.model.Review
import com.huebeiro.reviewyourtour.request.AddReviewRequest
import com.huebeiro.reviewyourtour.viewmodel.AddReviewViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author: Adilson Ribeiro
 * Date: 30/03/18
 */

class AddReviewActivity : AppCompatActivity() {

    private var binding: AddreviewBinding? = null
    private var viewModel = AddReviewViewModel()
    private val TAG = "ReviewsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.addreview)
        binding!!.viewModel = viewModel
        binding!!.view = this

    }

    fun cancelClick(){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun okClick(){
        val review = Review(
                rating = binding!!.ratingBar.rating.toString(),
                title = binding!!.txtTitle.text.toString(),
                message = binding!!.txtMessage.text.toString(),
                author = binding!!.txtName.text.toString() + " - " + binding!!.txtCountry.text.toString(),
                foreignLanguage = false,
                date = SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(Date()),
                dateUnformatted = Any(),
                languageCode = Locale.getDefault().language,
                travelerType = "friends", //should be based on account info
                reviewerName = binding!!.txtName.text.toString(),
                reviewerCountry = binding!!.txtCountry.text.toString()
        )
        var error = false
        if(review.reviewerName.isEmpty()){
            error = true
            binding!!.txtName.setError(resources.getString(R.string.addreview_error_fields),null)
        }
        if(review.reviewerCountry.isEmpty()){
            error = true
            binding!!.txtCountry.setError(resources.getString(R.string.addreview_error_fields),null)
        }
        if(error){
            Snackbar.make(binding!!.mainView, R.string.addreview_error_fields, Snackbar.LENGTH_LONG).show()
        } else {
            val request = AddReviewRequest(
                    review,
                    responseListener = Response.Listener { response ->
                        onResponse(response)
                    },
                    errorListener = Response.ErrorListener { volleyError ->
                        onError(volleyError)
                    }
            )
            MainApp.mainRequestQueue.add(request)
        }
    }

    fun onResponse(response: Review){
        Log.d(TAG, "onResponse - Review: " + response)
        Thread(Runnable {
            MainApp.mainDatabase.reviewDao().insertAll(
                    response
            )

            setResult(Activity.RESULT_OK)
            finish()
        }).start()
    }

    fun onError(volleyError: VolleyError){
        Log.e(TAG, "Error while processing the request", volleyError)
        Snackbar.make(binding!!.mainView, R.string.addreview_error_network, Snackbar.LENGTH_LONG).show()

    }

}
