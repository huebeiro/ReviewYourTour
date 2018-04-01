package com.huebeiro.reviewyourtour.request

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.huebeiro.reviewyourtour.model.ReviewsResponse
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Author: Adilson Ribeiro
 * Date: 31/03/18
 */

class ReviewsRequest(
        count: Int = 5,
        private val responseListener: Response.Listener<ReviewsResponse>,
        errorListener: Response.ErrorListener
) : Request<ReviewsResponse>(
        Method.GET,
        "https://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json?" +
                "count=$count&" +
                "page=0&" +
                "rating=0&" +
                "type=&" +
                "sortBy=date_of_review&" +
                "direction=DESC",
        errorListener
){

    override fun deliverResponse(response: ReviewsResponse) = responseListener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<ReviewsResponse> {
        return try {
            val json = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                    Gson().fromJson(json, ReviewsResponse::class.java),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}