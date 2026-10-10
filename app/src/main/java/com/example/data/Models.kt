package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class AnimeEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val title: String,
    val altTitle: String = "",
    val description: String,
    val genres: String, // comma-separated e.g. "Action, Supernatural, Shonen"
    val releaseYear: Int,
    val coverUrl: String,
    val bannerUrl: String,
    val isFeatured: Boolean = false,
    val published: Boolean = true
)

@Entity(tableName = "seasons")
data class SeasonEntity(
    @PrimaryKey val id: String,
    val animeId: String,
    val seasonNumber: Int,
    val seasonTitle: String,
    val coverUrl: String = "",
    val description: String = "",
    val published: Boolean = true
)

@Entity(tableName = "episodes")
data class EpisodeEntity(
    @PrimaryKey val id: String,
    val seasonId: String,
    val animeId: String,
    val episodeNumber: Int,
    val episodeTitle: String,
    val description: String = "",
    val thumbnailUrl: String = "",
    val published: Boolean = true
)

@Entity(tableName = "clips")
data class ClipEntity(
    @PrimaryKey val id: String,
    val episodeId: String,
    val seasonId: String,
    val animeId: String,
    val title: String,
    val durationSeconds: Int,
    val resolutionLabel: String, // e.g., "3840x2160 (4K UHD)", "1920x1080 (FHD)"
    val width: Int,
    val height: Int,
    val frameRate: Int = 60,
    val codec: String = "HEVC / H.265", // ProRes 422 HQ, HEVC, H.264
    val format: String = "MP4",
    val fileSizeMb: Double,
    val downloadUrl: String = "",
    val xmlPresetUrl: String = "",
    val is4k: Boolean = false,
    val viewCount: Int = 0,
    val published: Boolean = true,
    val uploadDate: String = "2026-10-09"
)

@Entity(tableName = "featured_artworks")
data class FeaturedArtworkEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String = "",
    val artworkUrl: String,
    val releaseInfo: String = "",
    val altText: String = "",
    val displayOrder: Int = 0
)

@Entity(tableName = "community_posts")
data class CommunityPostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorAvatar: String = "",
    val category: String, // General Discussion, Anime Discussion, AMV Editing, Clip Requests, XML & Presets
    val title: String,
    val content: String,
    val tags: String = "", // e.g. "Twixtor, Premiere, 4K"
    val imageUrl: String = "",
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)

@Entity(tableName = "community_comments")
data class CommunityCommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val authorName: String,
    val authorAvatar: String = "",
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val likeCount: Int = 0
)

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val coverUrl: String,
    val eventType: String, // AMV Contest, Speed Edit Challenge, Clip Release
    val startDate: String,
    val endDate: String,
    val status: String = "ONGOING", // UPCOMING, ONGOING, PAST
    val rules: String = "",
    val registrationLink: String = "",
    val reward: String = "",
    val participantCount: Int = 0
)

@Entity(tableName = "blog_posts")
data class BlogPostEntity(
    @PrimaryKey val id: String,
    val slug: String,
    val title: String,
    val category: String,
    val excerpt: String,
    val content: String,
    val coverUrl: String,
    val authorName: String,
    val publishDate: String,
    val readTimeMinutes: Int = 5
)

@Entity(tableName = "downloads")
data class DownloadItemEntity(
    @PrimaryKey val id: String,
    val resourceType: String, // "CLIP", "EPISODE_PKG", "SEASON_PKG"
    val title: String,
    val subtitle: String,
    val fileSizeMb: Double,
    val progressPercent: Int = 0,
    val status: String = "QUEUED", // QUEUED, DOWNLOADING, COMPLETED, FAILED
    val speed: String = "0 MB/s",
    val filePath: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
