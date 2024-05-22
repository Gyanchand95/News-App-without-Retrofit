package com.mynewsapp.app_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.mynewsapp.R
import com.mynewsapp.model.Article
import com.mynewsapp.network.ApiConstant
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiCallScreen() {
    var apiState by remember { mutableStateOf<ApiCallState>(ApiCallState.Loading) } //define the api state for get the response of the API
    val coroutineScope = rememberCoroutineScope() // Api calling instead of main UI thread

    //calling api in this block
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            apiState = ApiCallState.Loading
            val result = makeApiCall(ApiConstant.BASE_URL)
            apiState = result.fold(
                onSuccess = { ApiCallState.Success(it) },
                onFailure = { ApiCallState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    //UI screen according to API response showing the UI like at the loading time showing loader
    // if getting any error then showing error message in text
    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            TopAppBar(
                title = { Text("News App") }
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
                        //for Api loading state show the Progress Indicator
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ApiCallState.Success -> {
                        //In api success redirect on the list UI
                        val newsResponse = (apiState as ApiCallState.Success).data
                        NewsList(articles = newsResponse.articles)
                    }
                    is ApiCallState.Error -> {
                        //If getting error then simple add the error message in text view
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
    //Using for the vertical list
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.background(Color.White)
    ) {
        //items like the adapter given the item directly set the value of the item in next UI
        items(articles) { article ->
            NewsItem(article)
        }
    }
}


//News Item is the single item of the list
//We can create the single class or function for the Textview and other components
@Composable
fun NewsItem(article: Article) {
    var expanded by remember{
        mutableStateOf(false)
    }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
                )
            )
            .clickable {
                expanded = !expanded
            }
    ) {
        Column {
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
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily(Font(R.font.batang_regular)),
                modifier = Modifier.padding(10.dp)
            )
            article.description?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.batang_regular)),
                    maxLines = if(!expanded) 3 else 50,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .fillMaxHeight()

                )
            }
        }

    }

}