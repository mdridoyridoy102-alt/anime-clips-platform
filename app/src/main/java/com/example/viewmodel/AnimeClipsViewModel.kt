package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AnimeClipsRepository
import com.example.data.AnimeEntity
import com.example.data.BlogPostEntity
import com.example.data.ClipEntity
import com.example.data.CommunityCommentEntity
import com.example.data.CommunityPostEntity
import com.example.data.DownloadItemEntity
import com.example.data.EpisodeEntity
import com.example.data.EventEntity
import com.example.data.FeaturedArtworkEntity
import com.example.data.SeasonEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    data object Home : Screen()
    data object AnimeLibrary : Screen()
    data class AnimeDetail(val animeId: String) : Screen()
    data class SeasonDetail(val animeId: String, val seasonId: String) : Screen()
    data class EpisodeDetail(val animeId: String, val seasonId: String, val episodeId: String) : Screen()
    data class ClipPlayer(val clipId: String) : Screen()
    data object Community : Screen()
    data class PostDetail(val postId: String) : Screen()
    data object Events : Screen()
    data class EventDetail(val eventId: String) : Screen()
    data object Blog : Screen()
    data class BlogDetail(val blogId: String) : Screen()
    data object Perks : Screen()
    data object Downloads : Screen()
    data object Search : Screen()
    data object AdminPanel : Screen()
}

enum class SortOrder {
    RECENTLY_ADDED,
    A_TO_Z,
    Z_TO_A,
    RELEASE_YEAR
}

class AnimeClipsViewModel(
    private val repository: AnimeClipsRepository
) : ViewModel() {

    // Screen navigation stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val currentScreen: StateFlow<Screen> = _screenStack.combine(MutableStateFlow(Unit)) { stack, _ ->
        stack.lastOrNull() ?: Screen.Home
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Screen.Home)

    // Drawer state
    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen: StateFlow<Boolean> = _isDrawerOpen.asStateFlow()

    // Global Snackbar notification
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Detailed Resource dialog on Home Page
    private val _selectedLatestUpload = MutableStateFlow<ClipEntity?>(null)
    val selectedLatestUpload: StateFlow<ClipEntity?> = _selectedLatestUpload.asStateFlow()

    // Data streams from repository
    val allAnime = repository.allPublishedAnime.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val latestClips = repository.latestClips.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val featuredArtworks = repository.featuredArtworks.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val communityPosts = repository.communityPosts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val events = repository.events.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val blogPosts = repository.blogPosts.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val downloads = repository.downloads.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active downloading count
    val activeDownloadsCount = downloads.combine(MutableStateFlow(Unit)) { list, _ ->
        list.count { it.status == "DOWNLOADING" }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Library Filtering & Search
    private val _librarySearch = MutableStateFlow("")
    val librarySearch: StateFlow<String> = _librarySearch.asStateFlow()

    private val _selectedGenre = MutableStateFlow<String?>(null)
    val selectedGenre: StateFlow<String?> = _selectedGenre.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.RECENTLY_ADDED)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    // Filtered anime list
    val filteredAnime = combine(allAnime, _librarySearch, _selectedGenre, _sortOrder) { animeList, query, genre, sort ->
        var list = animeList
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.altTitle.lowercase().contains(q) ||
                it.genres.lowercase().contains(q)
            }
        }
        if (!genre.isNullOrBlank()) {
            list = list.filter { it.genres.contains(genre, ignoreCase = true) }
        }
        when (sort) {
            SortOrder.A_TO_Z -> list.sortedBy { it.title }
            SortOrder.Z_TO_A -> list.sortedByDescending { it.title }
            SortOrder.RELEASE_YEAR -> list.sortedByDescending { it.releaseYear }
            SortOrder.RECENTLY_ADDED -> list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Global Search
    private val _globalSearchQuery = MutableStateFlow("")
    val globalSearchQuery: StateFlow<String> = _globalSearchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    // Navigation methods
    fun navigateTo(screen: Screen) {
        _isDrawerOpen.value = false
        val current = _screenStack.value
        if (current.lastOrNull() != screen) {
            _screenStack.value = current + screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        return if (current.size > 1) {
            _screenStack.value = current.dropLast(1)
            true
        } else {
            false
        }
    }

    fun openDrawer() { _isDrawerOpen.value = true }
    fun closeDrawer() { _isDrawerOpen.value = false }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    fun setLatestUploadDetails(clip: ClipEntity?) {
        _selectedLatestUpload.value = clip
    }

    fun setLibrarySearch(query: String) { _librarySearch.value = query }
    fun setGenreFilter(genre: String?) { _selectedGenre.value = genre }
    fun setSortOrder(order: SortOrder) { _sortOrder.value = order }
    fun setGlobalSearchQuery(q: String) { _globalSearchQuery.value = q }

    // Repository accessors for sub-screens
    fun getAnime(id: String) = repository.getAnimeById(id)
    fun getSeasons(animeId: String) = repository.getSeasonsForAnime(animeId)
    fun getSeason(id: String) = repository.getSeasonById(id)
    fun getEpisodes(seasonId: String) = repository.getEpisodesForSeason(seasonId)
    fun getEpisode(id: String) = repository.getEpisodeById(id)
    fun getClipsForEpisode(episodeId: String) = repository.getClipsForEpisode(episodeId)
    fun getClipsForSeason(seasonId: String) = repository.getClipsForSeason(seasonId)
    fun getClip(id: String) = repository.getClipById(id)
    fun getComments(postId: String) = repository.getCommentsForPost(postId)

    // Download triggers
    fun downloadClip(clip: ClipEntity, qualityLabel: String) {
        repository.startClipDownload(clip, qualityLabel)
        showMessage("Download started: ${clip.title} ($qualityLabel)")
    }

    fun downloadEpisodePackage(episode: EpisodeEntity, animeTitle: String, clipCount: Int) {
        repository.startEpisodeDownload(episode, animeTitle, clipCount)
        showMessage("Generating Episode ${episode.episodeNumber} download package...")
    }

    fun downloadSeasonPackage(season: SeasonEntity, animeTitle: String, epCount: Int, clipCount: Int) {
        repository.startSeasonDownload(season, animeTitle, epCount, clipCount)
        showMessage("Queued full season archive for ${season.seasonTitle} ($epCount episodes)")
    }

    fun clearAllDownloads() {
        viewModelScope.launch { repository.clearDownloads() }
    }

    fun removeDownload(id: String) {
        viewModelScope.launch { repository.removeDownload(id) }
    }

    // Community Actions
    fun createPost(title: String, category: String, content: String, tags: String) {
        viewModelScope.launch {
            repository.createCommunityPost(title, category, content, tags)
            showMessage("Community post published successfully!")
        }
    }

    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            repository.addComment(postId, content)
            showMessage("Comment posted!")
        }
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            repository.likePost(postId)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId)
            showMessage("Post removed.")
        }
    }

    // Admin App Actions
    fun adminAddAnime(anime: AnimeEntity) {
        viewModelScope.launch {
            repository.adminAddAnime(anime)
            showMessage("Anime '${anime.title}' added to catalog.")
        }
    }

    fun adminDeleteAnime(id: String) {
        viewModelScope.launch {
            repository.adminDeleteAnime(id)
            showMessage("Anime deleted.")
        }
    }

    fun adminAddSeason(season: SeasonEntity) {
        viewModelScope.launch {
            repository.adminAddSeason(season)
            showMessage("Season ${season.seasonNumber} added.")
        }
    }

    fun adminAddEpisode(episode: EpisodeEntity) {
        viewModelScope.launch {
            repository.adminAddEpisode(episode)
            showMessage("Episode ${episode.episodeNumber} created.")
        }
    }

    fun adminAddClip(clip: ClipEntity) {
        viewModelScope.launch {
            repository.adminAddClip(clip)
            showMessage("Clip '${clip.title}' published.")
        }
    }

    fun adminAddArtwork(artwork: FeaturedArtworkEntity) {
        viewModelScope.launch {
            repository.adminAddArtwork(artwork)
            showMessage("Featured Artwork added.")
        }
    }

    fun adminAddEvent(event: EventEntity) {
        viewModelScope.launch {
            repository.adminAddEvent(event)
            showMessage("Event '${event.title}' scheduled.")
        }
    }

    fun adminAddBlogPost(blog: BlogPostEntity) {
        viewModelScope.launch {
            repository.adminAddBlogPost(blog)
            showMessage("Article '${blog.title}' published.")
        }
    }
}

class AnimeClipsViewModelFactory(
    private val repository: AnimeClipsRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnimeClipsViewModel(repository) as T
    }
}
