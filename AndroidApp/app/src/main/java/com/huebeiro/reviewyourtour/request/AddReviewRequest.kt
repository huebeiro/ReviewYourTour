package com.huebeiro.reviewyourtour.request

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.huebeiro.reviewyourtour.model.Review
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Author: Adilson Ribeiro
 * Date: 31/03/18
 */

/**
 * The address to the Mock post server, needs to be updated to new server address.
 */
val mockServerAddress = "192.168.2.6:8080"

class AddReviewRequest(
        private val review: Review,
        private val responseListener: Response.Listener<Review>,
        errorListener: Response.ErrorListener
) : Request<Review>(
        Method.POST,
        "http://$mockServerAddress/reviews", //URL to the mock server
        errorListener
){
    private val gson = Gson()

    override fun getBodyContentType(): String {
        return "application/json; charset=utf-8"
    }

    override fun getBody(): ByteArray? {
        val json = gson.toJson(review)
        return if (json != null)
            json.toByteArray(Charset.forName("utf-8"))
        else
            null
    }

    override fun deliverResponse(response: Review) = responseListener.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<Review> {
        return try {
            val json = String(
                    response?.data ?: ByteArray(0),
                    Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                    gson.fromJson(json, Review::class.java),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}