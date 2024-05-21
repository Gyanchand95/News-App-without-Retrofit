package com.mynewsapp.network

import com.mynewsapp.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.Json


suspend fun makeApiCall(urlString: String): Result<NewsResponse> {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val result = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    result.append(line)
                }

                reader.close()
                inputStream.close()

                val json = result.toString()
                val newsResponse = Json.decodeFromString<NewsResponse>(json)
                Result.success(newsResponse)
            } else {
                Result.failure(Exception("Error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}