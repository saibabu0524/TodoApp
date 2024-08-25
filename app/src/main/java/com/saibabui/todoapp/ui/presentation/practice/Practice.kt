package com.saibabui.todoapp.ui.presentation.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saibabui.todoapp.R

@Composable
fun CustomLazyColumn(modifier: Modifier = Modifier, listOfItems: List<String>) {
    val scrollState = rememberScrollState()
    scrollState.interactionSource.interactions
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
    ) {

        Image(
            painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Android logo",
            contentScale = ContentScale.Fit,
            // Reduce scrolling rate by half.
            modifier = Modifier.parallaxLayoutModifier(scrollState, 2)
        )

        Text(
            text = "kljasd kajaskd flaskdf askdfjsafa askjasl dl;k faskfjas;kf  kjsa kljasd kajaskd flaskdf askdfjsafa askjasl dl;k faskfjas;kf  kjsa kljasd kajaskd flaskdf askdfjsafa askjasl dl;k faskfjas;kf  kjsa",
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 8.dp),

            )
    }

}

fun Modifier.parallaxLayoutModifier(scrollState: ScrollState, rate: Int) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        val height = if (rate > 0) scrollState.value / rate else scrollState.value
        layout(placeable.width, placeable.height) {
            placeable.place(0, height)
        }
    }


@Preview
@Composable
private fun Preview() {
    CustomLazyColumn(listOfItems = listOf("a", "b", "c"))
}