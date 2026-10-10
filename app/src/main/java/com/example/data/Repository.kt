package com.example.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class AnimeClipsRepository(
    private val database: AnimeClipsDatabase,
    private val scope: CoroutineScope
) {
    private val animeDao = database.animeDao()
    private val seasonDao = database.seasonDao()
    private val episodeDao = database.episodeDao()
    private val clipDao = database.clipDao()
    private val featuredArtworkDao = database.featuredArtworkDao()
    private val communityDao = database.communityDao()
    private val eventDao = database.eventDao()
    private val blogDao = database.blogDao()
    private val downloadDao = database.downloadDao()

    // Read flows
    val allPublishedAnime: Flow<List<AnimeEntity>> = animeDao.getAllPublishedAnime()
    val allAnimeAdmin: Flow<List<AnimeEntity>> = animeDao.getAllAnimeAdmin()
    val latestClips: Flow<List<ClipEntity>> = clipDao.getLatestClips()
    val featuredArtworks: Flow<List<FeaturedArtworkEntity>> = featuredArtworkDao.getAllFeaturedArtworks()
    val communityPosts: Flow<List<CommunityPostEntity>> = communityDao.getAllPosts()
    val events: Flow<List<EventEntity>> = eventDao.getAllEvents()
    val blogPosts: Flow<List<BlogPostEntity>> = blogDao.getAllBlogPosts()
    val downloads: Flow<List<DownloadItemEntity>> = downloadDao.getAllDownloads()

    fun getAnimeById(id: String): Flow<AnimeEntity?> = animeDao.getAnimeById(id)
    fun getSeasonsForAnime(animeId: String): Flow<List<SeasonEntity>> = seasonDao.getSeasonsForAnime(animeId)
    fun getSeasonById(id: String): Flow<SeasonEntity?> = seasonDao.getSeasonById(id)
    fun getEpisodesForSeason(seasonId: String): Flow<List<EpisodeEntity>> = episodeDao.getEpisodesForSeason(seasonId)
    fun getEpisodeById(id: String): Flow<EpisodeEntity?> = episodeDao.getEpisodeById(id)
    fun getClipsForEpisode(episodeId: String): Flow<List<ClipEntity>> = clipDao.getClipsForEpisode(episodeId)
    fun getClipsForSeason(seasonId: String): Flow<List<ClipEntity>> = clipDao.getClipsForSeason(seasonId)
    fun getClipById(id: String): Flow<ClipEntity?> = clipDao.getClipById(id)
    fun getCommentsForPost(postId: String): Flow<List<CommunityCommentEntity>> = communityDao.getCommentsForPost(postId)

    // Ensure database has initial data on startup if empty
    suspend fun checkAndSeedInitialData() {
        val count = animeDao.getAllPublishedAnime().first().size
        if (count == 0) {
            populateInitialData(database)
        }
    }

    // Community interactions
    suspend fun createCommunityPost(
        title: String,
        category: String,
        content: String,
        tags: String,
        authorName: String = "You"
    ) {
        val post = CommunityPostEntity(
            id = "post-" + UUID.randomUUID().toString().take(8),
            authorName = authorName,
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            category = category,
            title = title,
            content = content,
            tags = tags,
            timestamp = System.currentTimeMillis()
        )
        communityDao.insertPost(post)
    }

    suspend fun addComment(postId: String, content: String, authorName: String = "You") {
        val comment = CommunityCommentEntity(
            id = "comment-" + UUID.randomUUID().toString().take(8),
            postId = postId,
            authorName = authorName,
            content = content,
            timestamp = System.currentTimeMillis()
        )
        communityDao.insertComment(comment)
    }

    suspend fun likePost(postId: String) {
        communityDao.incrementPostLikes(postId)
    }

    suspend fun deletePost(postId: String) {
        communityDao.deletePostById(postId)
    }

    // Download Manager operations
    fun startClipDownload(clip: ClipEntity, qualityLabel: String) {
        scope.launch(Dispatchers.IO) {
            val downloadId = "dl-" + UUID.randomUUID().toString().take(8)
            val initialItem = DownloadItemEntity(
                id = downloadId,
                resourceType = "CLIP",
                title = clip.title,
                subtitle = "$qualityLabel • ${clip.codec} • ${clip.fileSizeMb} MB",
                fileSizeMb = clip.fileSizeMb,
                progressPercent = 0,
                status = "DOWNLOADING",
                speed = "14.2 MB/s",
                filePath = "/storage/emulated/0/AnimeClips/${clip.title.replace(" ", "_")}.mp4"
            )
            downloadDao.insertDownload(initialItem)

            // Simulate realistic progressive network download
            val steps = 10
            for (i in 1..steps) {
                delay(400)
                val percent = i * 10
                val speedVal = (12.0 + (i % 4) * 2.5).toString().take(4) + " MB/s"
                val updated = initialItem.copy(
                    progressPercent = percent,
                    speed = if (percent == 100) "Completed" else speedVal,
                    status = if (percent == 100) "COMPLETED" else "DOWNLOADING"
                )
                downloadDao.updateDownload(updated)
            }
        }
    }

    fun startEpisodeDownload(episode: EpisodeEntity, animeTitle: String, clipCount: Int) {
        scope.launch(Dispatchers.IO) {
            val downloadId = "dl-pkg-" + UUID.randomUUID().toString().take(8)
            val totalSize = (clipCount * 450.0).coerceAtLeast(650.0)
            val initialItem = DownloadItemEntity(
                id = downloadId,
                resourceType = "EPISODE_PKG",
                title = "$animeTitle - Ep ${episode.episodeNumber}: ${episode.episodeTitle}",
                subtitle = "Episode Package ($clipCount clips + XML presets) • ${totalSize.toInt()} MB",
                fileSizeMb = totalSize,
                progressPercent = 0,
                status = "DOWNLOADING",
                speed = "22.5 MB/s",
                filePath = "/storage/emulated/0/AnimeClips/$animeTitle/Ep_${episode.episodeNumber}_Package.zip"
            )
            downloadDao.insertDownload(initialItem)

            val steps = 10
            for (i in 1..steps) {
                delay(500)
                val percent = i * 10
                val speedVal = (18.0 + (i % 3) * 3.1).toString().take(4) + " MB/s"
                val updated = initialItem.copy(
                    progressPercent = percent,
                    speed = if (percent == 100) "Completed" else speedVal,
                    status = if (percent == 100) "COMPLETED" else "DOWNLOADING"
                )
                downloadDao.updateDownload(updated)
            }
        }
    }

    fun startSeasonDownload(season: SeasonEntity, animeTitle: String, episodeCount: Int, clipCount: Int) {
        scope.launch(Dispatchers.IO) {
            val downloadId = "dl-season-" + UUID.randomUUID().toString().take(8)
            val totalSize = (clipCount * 520.0).coerceAtLeast(1800.0)
            val initialItem = DownloadItemEntity(
                id = downloadId,
                resourceType = "SEASON_PKG",
                title = "$animeTitle - ${season.seasonTitle}",
                subtitle = "Hierarchical Season Archive ($episodeCount Episodes, $clipCount Clips) • ${totalSize.toInt()} MB",
                fileSizeMb = totalSize,
                progressPercent = 0,
                status = "DOWNLOADING",
                speed = "28.4 MB/s",
                filePath = "/storage/emulated/0/AnimeClips/$animeTitle/${season.seasonTitle.replace(" ", "_")}_Archive.zip"
            )
            downloadDao.insertDownload(initialItem)

            val steps = 10
            for (i in 1..steps) {
                delay(600)
                val percent = i * 10
                val speedVal = (25.0 + (i % 5) * 2.2).toString().take(4) + " MB/s"
                val updated = initialItem.copy(
                    progressPercent = percent,
                    speed = if (percent == 100) "Completed" else speedVal,
                    status = if (percent == 100) "COMPLETED" else "DOWNLOADING"
                )
                downloadDao.updateDownload(updated)
            }
        }
    }

    suspend fun clearDownloads() {
        downloadDao.clearAllDownloads()
    }

    suspend fun removeDownload(id: String) {
        downloadDao.deleteDownloadById(id)
    }

    // Admin operations for the Android Admin App Integration
    suspend fun adminAddAnime(anime: AnimeEntity) = animeDao.insertAnime(anime)
    suspend fun adminDeleteAnime(id: String) = animeDao.deleteAnimeById(id)
    suspend fun adminAddSeason(season: SeasonEntity) = seasonDao.insertSeason(season)
    suspend fun adminDeleteSeason(id: String) = seasonDao.deleteSeasonById(id)
    suspend fun adminAddEpisode(episode: EpisodeEntity) = episodeDao.insertEpisode(episode)
    suspend fun adminDeleteEpisode(id: String) = episodeDao.deleteEpisodeById(id)
    suspend fun adminAddClip(clip: ClipEntity) = clipDao.insertClip(clip)
    suspend fun adminDeleteClip(id: String) = clipDao.deleteClipById(id)
    suspend fun adminAddArtwork(artwork: FeaturedArtworkEntity) = featuredArtworkDao.insertArtwork(artwork)
    suspend fun adminDeleteArtwork(id: String) = featuredArtworkDao.deleteArtworkById(id)
    suspend fun adminAddEvent(event: EventEntity) = eventDao.insertEvent(event)
    suspend fun adminAddBlogPost(blogPost: BlogPostEntity) = blogDao.insertBlogPost(blogPost)
}
