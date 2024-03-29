package com.chenyangqi.composedemo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chenyangqi.composedemo.ui.theme.Teal200

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultPreview()
        }
    }

    @Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFFFFFFFF)
    @Composable
    fun DefaultPreview() {
        MaterialTheme() {
            val typography = MaterialTheme.typography
            Column(Modifier.padding(20.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.header),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(4.dp))
                        .clickable {
                            Toast
                                .makeText(this@MainActivity, "click imageview", Toast.LENGTH_SHORT)
                                .show()
                        },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "A day wandering through the sandhills " +
                            "in Shark Fin Cove, and a few of the " +
                            "sights I saw",
                    style = typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text("Davenport, California", style = typography.body2)
                Text("December 2018", style = typography.body2)

                //嵌套其他的Composable
                ArtistCard()

                //weight使用
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1.5F)
                            .background(Color.Red),
                        text = "1.5 weight"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(3F)
                            .background(Color.Gray),
                        text = " 3 weight"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(4.5F)
                            .background(Color.Green),
                        text = " 4.5 weight"
                    )
                }
            }
        }
    }

    @Composable()
    fun ArtistCard() {
        Row(
            modifier = Modifier
                .background(Teal200)
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(35.dp))
                    .height(70.dp)
                    .width(70.dp)
                    .clickable {
                        Toast
                            .makeText(this@MainActivity, "click avatar", Toast.LENGTH_SHORT)
                            .show()
                    }
            )
            Column() {
                Text(
                    "Alfred Sisley",
                    modifier = Modifier.paddingFromBaseline(30.dp)
                )
                Text(
                    "3 minute age",
                    modifier = Modifier.offset(x = 30.dp)
                )
            }
            Box(
                modifier = Modifier.size(width = 70.dp, height = 70.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "",
                    Modifier
                        .background(Color.Black)
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
                Text(
                    text = "",
                    Modifier
                        .background(Color.Blue)
                        .height(50.dp)
                        .width(50.dp)
                )
            }
        }
    }
}
