package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AnimeClipsDatabase
import com.example.data.AnimeClipsRepository
import com.example.ui.components.AppHeader
import com.example.ui.components.AppNavigationDrawer
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.AnimeDetailScreen
import com.example.ui.screens.AnimeLibraryScreen
import com.example.ui.screens.BlogScreen
import com.example.ui.screens.ClipPlayerScreen
import com.example.ui.screens.CommunityScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.EpisodeScreen
import com.example.ui.screens.EventsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PerksScreen
import com.example.ui.screens.PostDetailScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SeasonScreen
import com.example.ui.theme.BackgroundNearBlack
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AnimeClipsViewModel
import com.example.viewmodel.AnimeClipsViewModelFactory
import com.example.viewmodel.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AnimeClipsDatabase.getInstance(applicationContext)
        val repository = AnimeClipsRepository(database, CoroutineScope(Dispatchers.IO))
        val viewModelFactory = AnimeClipsViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AnimeClipsViewModel::class.java]

        setContent {
            MyApplicationTheme {
                AnimeClipsApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AnimeClipsApp(viewModel: AnimeClipsViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsStateWithLifecycle()
    val activeDownloadsCount by viewModel.activeDownloadsCount.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    // Hardware/System back button handling
    BackHandler(enabled = isDrawerOpen || currentScreen !is Screen.Home) {
        if (isDrawerOpen) {
            viewModel.closeDrawer()
        } else {
            viewModel.navigateBack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
    ) {
        Scaffold(
            topBar = {
                AppHeader(
                    activeDownloadsCount = activeDownloadsCount,
                    onLogoClick = { viewModel.navigateTo(Screen.Home) },
                    onSearchClick = { viewModel.navigateTo(Screen.Search) },
                    onDownloadsClick = { viewModel.navigateTo(Screen.Downloads) },
                    onMenuClick = { viewModel.openDrawer() }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = BackgroundNearBlack
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (val screen = currentScreen) {
                    is Screen.Home -> HomeScreen(viewModel = viewModel)
                    is Screen.AnimeLibrary -> AnimeLibraryScreen(viewModel = viewModel)
                    is Screen.AnimeDetail -> AnimeDetailScreen(animeId = screen.animeId, viewModel = viewModel)
                    is Screen.SeasonDetail -> SeasonScreen(animeId = screen.animeId, seasonId = screen.seasonId, viewModel = viewModel)
                    is Screen.EpisodeDetail -> EpisodeScreen(animeId = screen.animeId, seasonId = screen.seasonId, episodeId = screen.episodeId, viewModel = viewModel)
                    is Screen.ClipPlayer -> ClipPlayerScreen(clipId = screen.clipId, viewModel = viewModel)
                    is Screen.Community -> CommunityScreen(viewModel = viewModel)
                    is Screen.PostDetail -> PostDetailScreen(postId = screen.postId, viewModel = viewModel)
                    is Screen.Events -> EventsScreen(viewModel = viewModel)
                    is Screen.EventDetail -> EventsScreen(viewModel = viewModel)
                    is Screen.Blog -> BlogScreen(viewModel = viewModel)
                    is Screen.BlogDetail -> BlogScreen(viewModel = viewModel)
                    is Screen.Perks -> PerksScreen(viewModel = viewModel)
                    is Screen.Downloads -> DownloadsScreen(viewModel = viewModel)
                    is Screen.Search -> SearchScreen(viewModel = viewModel)
                    is Screen.AdminPanel -> AdminScreen(viewModel = viewModel)
                }
            }
        }

        // Global Navigation Drawer overlay
        AppNavigationDrawer(
            isOpen = isDrawerOpen,
            currentScreen = currentScreen,
            onNavigate = { viewModel.navigateTo(it) },
            onClose = { viewModel.closeDrawer() }
        )
    }
}
