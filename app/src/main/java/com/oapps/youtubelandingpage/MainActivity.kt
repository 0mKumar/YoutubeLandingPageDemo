package com.oapps.youtubelandingpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.oapps.youtubelandingpage.ui.theme.YoutubeLandingPageTheme

data class TabData(val text: String, val icon: ImageVector)

//fun createNavController(context: Context): NavController{
//    return createNavController(context).apply {
//        for (navigator in navigators) {
//            navigatorProvider.addNavigator(navigator)
//        }
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val navController = createNavController(this)


        setContent {
            val tabs = remember {
                listOf(
                    TabData("Home", Icons.Default.Home),
                    TabData("Account", Icons.Default.Person),
                    TabData("Video", Icons.Default.FavoriteBorder),
                )
            }

            val navController = rememberNavController()

            YoutubeLandingPageTheme {
                var selectedTabIndex by remember { mutableStateOf(0) }
                Scaffold(
                    topBar = {
                        Column {
                            TopAppBar {
                                Text("Home")
                            }
                            TabRow(selectedTabIndex = selectedTabIndex) {
                                for (i in tabs.indices) {
                                    YoutubeTab(
                                        text = tabs[i].text,
                                        icon = tabs[i].icon,
                                        selected = i == selectedTabIndex
                                    ) {
                                        selectedTabIndex = i
                                        navController.navigate(tabs[i].text.lowercase())
                                    }
                                }
                            }
                        }
                    },
                    drawerContent = {
                        for (i in 0..5) {
                            OutlinedButton(onClick = { /*TODO*/ }) {
                                Text("Button 1")
                            }
                        }
                    }
                ) {
                    NavHost(navController = navController, "home") {
                        composable("home") {
                            selectedTabIndex = 0
                            HomeScreen(navController)
                        }
                        composable("account") {
                            selectedTabIndex = 1
                            Text("This is Account")
                        }
                        composable("video/{videoId}", arguments = listOf(navArgument("videoId") { type = NavType.IntType })) {
                            selectedTabIndex = 2
                            val id = it.arguments?.getInt("videoId")
                            if(id != null){
                                Text("This is Video $id")
                            }else{
                                Text("Video Id not passed")
                            }
                        }
                    }
//                    navController.addOnDestinationChangedListener { controller, destination, arguments ->
//                        when(destination.route){
//                            "home" -> selectedTabIndex = 0
//                            "account" -> selectedTabIndex = 1
//                            "library" -> selectedTabIndex = 2
//                        }
//                    }
                }

            }
        }
    }

    @Composable
    private fun YoutubeTab(
        text: String,
        icon: ImageVector,
        selected: Boolean,
        onClick: () -> Unit
    ) {
        Tab(
            selected = selected,
            onClick = onClick,
            text = {
                Text(text)
            },
            icon = {
                Icon(icon, text)
            }
        )
    }
}

data class VideoData(val title: String, val views: String, val channel: String)

val listOfVideo =
    (0..100).map { VideoData("Title of video $it", "${(1..11000).random()} views", "XYZ$it") }

@Composable
fun HomeScreen(navController: NavController) {
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(listOfVideo) { index, video ->
            VideoItem("https://picsum.photos/200/300", video.title, video.channel, video.views){
                navController.navigate("video/$index")
            }
        }
    }
}


@Composable
fun VideoItem(imageUrl: String, title: String, channel: String, views: String, onClick: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 8.dp)
            .height(100.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Box {
            Image(
                painter = rememberImagePainter(data = imageUrl, builder = {
                    scale(Scale.FILL)
                }),
                contentScale = ContentScale.FillBounds,
                contentDescription = "video",
                modifier = Modifier
                    .aspectRatio(16f / 9f)
                    .background(Color.Red)
            )
            Text(
                "5:00",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(0.5f), RoundedCornerShape(4.dp)),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.h6)
            Text(channel, style = MaterialTheme.typography.subtitle2)
            Text(views, style = MaterialTheme.typography.subtitle2)
        }
    }
}

