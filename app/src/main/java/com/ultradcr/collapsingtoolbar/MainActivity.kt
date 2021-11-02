package com.ultradcr.collapsingtoolbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ultradcr.collapsingtoolbar.ui.theme.CollapsingToolbarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    CollapsableToolbar()

                    CollapsableToolbarContainer(
                        collapsableToolbar = {
                            TopAppBar(modifier = Modifier.fillMaxHeight()) {
                                Text(text = "Text")
                            }
                        },
                        toolbarHeight = 140.dp,
                    ){
                        LazyColumn(contentPadding = PaddingValues(0.dp),) {
                            items(100) { index ->
                                Text("I'm item $index", modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CollapsingToolbarTheme {
        Greeting("Android")
    }
}