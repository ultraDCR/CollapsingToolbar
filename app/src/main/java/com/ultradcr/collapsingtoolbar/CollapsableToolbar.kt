package com.ultradcr.collapsingtoolbar



import android.util.Log
import android.widget.Toolbar
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.roundToInt


@Composable
fun CollapsableToolbar() {

// here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn
    val toolbarHeight = 148.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
// our offset to collapse toolbar
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val height = animateFloatAsState(targetValue = toolbarOffsetHeightPx.value)

    // now, let's create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {

                if(abs(available.y) == 0f){
                    if(toolbarOffsetHeightPx.value < -(toolbarHeightPx/2)){
                        toolbarOffsetHeightPx.value = -toolbarHeightPx
                    }else{
                        toolbarOffsetHeightPx.value = 0f
                    }
                }

                return Velocity.Zero
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
// attach as a parent to the nested scroll system
            .nestedScroll(nestedScrollConnection)

    ) {
        TopAppBar(title = {Text("reerer")})
        Box(
            modifier = Modifier
            .height(with(LocalDensity.current){ toolbarHeight - abs(height.value).toDp() })){
            TopAppBar(
                modifier = Modifier.fillMaxHeight(),
//                    .height(with(LocalDensity.current){ toolbarHeight - abs(height.value).toDp() }),
//                .offset { IntOffset(x = 0, y = height.value.roundToInt()) },
                title = { Text("toolbar offset is ${toolbarOffsetHeightPx.value}") }
            )

        }

        // our list with build in nested scroll support that will notify us about its scroll
        LazyColumn(contentPadding = PaddingValues(0.dp),) {
            items(100) { index ->
                Text("I'm item $index", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp))
            }
        }

    }
}


@Composable
fun CollapsableToolbarContainer(
    collapsableToolbar : @Composable () -> Unit,
    toolbarHeight: Dp,
    content: @Composable () -> Unit
) {


// here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn

    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
// our offset to collapse toolbar
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val height = animateFloatAsState(targetValue = toolbarOffsetHeightPx.value)

    // now, let's create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {

                if(abs(available.y) == 0f){
                    if(toolbarOffsetHeightPx.value < -(toolbarHeightPx/2)){
                        toolbarOffsetHeightPx.value = -toolbarHeightPx
                    }else{
                        toolbarOffsetHeightPx.value = 0f
                    }
                }

                return Velocity.Zero
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            // attach as a parent to the nested scroll system
            .nestedScroll(nestedScrollConnection)

    ) {
        Box(
            modifier = Modifier
                .height(with(LocalDensity.current){ toolbarHeight - abs(height.value).toDp() })){
            collapsableToolbar()
        }
        // our list with build in nested scroll support that will notify us about its scroll
        content()

    }
}