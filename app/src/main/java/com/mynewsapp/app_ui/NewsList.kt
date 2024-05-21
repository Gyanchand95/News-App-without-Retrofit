package com.mynewsapp.app_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mynewsapp.network.ApiCallState
import com.mynewsapp.network.makeApiCall
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.mynewsapp.model.Article
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCallScreen() {
    var apiState by remember { mutableStateOf<ApiCallState>(ApiCallState.Loading) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            apiState = ApiCallState.Loading
            val result = makeApiCall("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")
            apiState = result.fold(
                onSuccess = { ApiCallState.Success(it) },
                onFailure = { ApiCallState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("API Call Example") }
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (apiState) {
                    is ApiCallState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ApiCallState.Success -> {
                        val newsResponse = (apiState as ApiCallState.Success).data
                        NewsList(articles = newsResponse.articles)
                    }
                    is ApiCallState.Error -> {
                        Text(
                            text = (apiState as ApiCallState.Error).error,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun NewsList(articles: List<Article>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(articles) { article ->
            NewsItem(article)
        }
    }
}

@Composable
fun NewsItem(article: Article) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        article.urlToImage?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = article.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        article.description?.let {
            Text(
                text = it,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

    }
}