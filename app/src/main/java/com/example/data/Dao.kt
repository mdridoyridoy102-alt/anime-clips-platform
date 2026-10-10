package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime WHERE published = 1 ORDER BY releaseYear DESC")
    fun getAllPublishedAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime ORDER BY releaseYear DESC")
    fun getAllAnimeAdmin(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime WHERE id = :id LIMIT 1")
    fun getAnimeById(id: String): Flow<AnimeEntity?>

    @Query("SELECT * FROM anime WHERE slug = :slug LIMIT 1")
    fun getAnimeBySlug(slug: String): Flow<AnimeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(animeList: List<AnimeEntity>)

    @Update
    suspend fun updateAnime(anime: AnimeEntity)

    @Query("DELETE FROM anime WHERE id = :id")
    suspend fun deleteAnimeById(id: String)
}

@Dao
interface SeasonDao {
    @Query("SELECT * FROM seasons WHERE animeId = :animeId AND published = 1 ORDER BY seasonNumber ASC")
    fun getSeasonsForAnime(animeId: String): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM seasons WHERE animeId = :animeId ORDER BY seasonNumber ASC")
    fun getAllSeasonsForAnimeAdmin(animeId: String): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM seasons WHERE id = :id LIMIT 1")
    fun getSeasonById(id: String): Flow<SeasonEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeason(season: SeasonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeasons(seasons: List<SeasonEntity>)

    @Query("DELETE FROM seasons WHERE id = :id")
    suspend fun deleteSeasonById(id: String)
}

@Dao
interface EpisodeDao {
    @Query("SELECT * FROM episodes WHERE seasonId = :seasonId AND published = 1 ORDER BY episodeNumber ASC")
    fun getEpisodesForSeason(seasonId: String): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE animeId = :animeId AND published = 1 ORDER BY episodeNumber ASC")
    fun getEpisodesForAnime(animeId: String): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE id = :id LIMIT 1")
    fun getEpisodeById(id: String): Flow<EpisodeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: EpisodeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeEntity>)

    @Query("DELETE FROM episodes WHERE id = :id")
    suspend fun deleteEpisodeById(id: String)
}

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips WHERE episodeId = :episodeId AND published = 1 ORDER BY id ASC")
    fun getClipsForEpisode(episodeId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE seasonId = :seasonId AND published = 1 ORDER BY id ASC")
    fun getClipsForSeason(seasonId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE animeId = :animeId AND published = 1 ORDER BY id ASC")
    fun getClipsForAnime(animeId: String): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE published = 1 ORDER BY id DESC LIMIT 20")
    fun getLatestClips(): Flow<List<ClipEntity>>

    @Query("SELECT * FROM clips WHERE id = :id LIMIT 1")
    fun getClipById(id: String): Flow<ClipEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClip(clip: ClipEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClips(clips: List<ClipEntity>)

    @Query("DELETE FROM clips WHERE id = :id")
    suspend fun deleteClipById(id: String)
}

@Dao
interface FeaturedArtworkDao {
    @Query("SELECT * FROM featured_artworks ORDER BY displayOrder ASC")
    fun getAllFeaturedArtworks(): Flow<List<FeaturedArtworkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artwork: FeaturedArtworkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtworks(artworks: List<FeaturedArtworkEntity>)

    @Query("DELETE FROM featured_artworks WHERE id = :id")
    suspend fun deleteArtworkById(id: String)
}

@Dao
interface CommunityDao {
    @Query("SELECT * FROM community_posts ORDER BY isPinned DESC, timestamp DESC")
    fun getAllPosts(): Flow<List<CommunityPostEntity>>

    @Query("SELECT * FROM community_posts WHERE category = :category ORDER BY timestamp DESC")
    fun getPostsByCategory(category: String): Flow<List<CommunityPostEntity>>

    @Query("SELECT * FROM community_posts WHERE id = :id LIMIT 1")
    fun getPostById(id: String): Flow<CommunityPostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: CommunityPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<CommunityPostEntity>)

    @Query("DELETE FROM community_posts WHERE id = :id")
    suspend fun deletePostById(id: String)

    @Query("UPDATE community_posts SET likeCount = likeCount + 1 WHERE id = :id")
    suspend fun incrementPostLikes(id: String)

    @Query("SELECT * FROM community_comments WHERE postId = :postId ORDER BY timestamp ASC")
    fun getCommentsForPost(postId: String): Flow<List<CommunityCommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommunityCommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommunityCommentEntity>)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startDate ASC")
    fun getAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    fun getEventById(id: String): Flow<EventEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: String)
}

@Dao
interface BlogDao {
    @Query("SELECT * FROM blog_posts ORDER BY publishDate DESC")
    fun getAllBlogPosts(): Flow<List<BlogPostEntity>>

    @Query("SELECT * FROM blog_posts WHERE id = :id LIMIT 1")
    fun getBlogPostById(id: String): Flow<BlogPostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogPost(blogPost: BlogPostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogPosts(blogPosts: List<BlogPostEntity>)

    @Query("DELETE FROM blog_posts WHERE id = :id")
    suspend fun deleteBlogPostById(id: String)
}

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloads ORDER BY timestamp DESC")
    fun getAllDownloads(): Flow<List<DownloadItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadItemEntity)

    @Update
    suspend fun updateDownload(download: DownloadItemEntity)

    @Query("DELETE FROM downloads WHERE id = :id")
    suspend fun deleteDownloadById(id: String)

    @Query("DELETE FROM downloads")
    suspend fun clearAllDownloads()
}
