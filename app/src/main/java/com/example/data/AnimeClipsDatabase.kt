package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AnimeEntity::class,
        SeasonEntity::class,
        EpisodeEntity::class,
        ClipEntity::class,
        FeaturedArtworkEntity::class,
        CommunityPostEntity::class,
        CommunityCommentEntity::class,
        EventEntity::class,
        BlogPostEntity::class,
        DownloadItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AnimeClipsDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
    abstract fun seasonDao(): SeasonDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun clipDao(): ClipDao
    abstract fun featuredArtworkDao(): FeaturedArtworkDao
    abstract fun communityDao(): CommunityDao
    abstract fun eventDao(): EventDao
    abstract fun blogDao(): BlogDao
    abstract fun downloadDao(): DownloadDao

    companion object {
        @Volatile
        private var INSTANCE: AnimeClipsDatabase? = null

        fun getInstance(context: Context): AnimeClipsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AnimeClipsDatabase::class.java,
                    "anime_clips_platform.db"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateInitialData(database)
                }
            }
        }
    }
}

suspend fun populateInitialData(database: AnimeClipsDatabase) {
    // 1. Featured Anime
    val animeList = listOf(
        AnimeEntity(
            id = "jjk",
            slug = "jujutsu-kaisen",
            title = "Jujutsu Kaisen",
            altTitle = "呪術廻戦 (Sorcery Fight)",
            description = "A high-octane dark fantasy where high school student Yuji Itadori swallows a cursed talisman and becomes host to Ryomen Sukuna, navigating battles against deadly curses.",
            genres = "Action, Supernatural, Shonen, Dark Fantasy",
            releaseYear = 2023,
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80",
            isFeatured = true,
            published = true
        ),
        AnimeEntity(
            id = "demon-slayer",
            slug = "demon-slayer-kimetsu-no-yaiba",
            title = "Demon Slayer: Kimetsu no Yaiba",
            altTitle = "鬼滅の刃 (Blade of Demon Destruction)",
            description = "Tanjiro Kamado embarks on a perilous quest to become a Demon Slayer, avenge his slaughtered family, and find a cure to turn his demonized sister Nezuko back into a human.",
            genres = "Action, Historical, Supernatural, Shonen",
            releaseYear = 2024,
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop&q=80",
            isFeatured = true,
            published = true
        ),
        AnimeEntity(
            id = "cyberpunk",
            slug = "cyberpunk-edgerunners",
            title = "Cyberpunk: Edgerunners",
            altTitle = "サイバーパンク エッジランナーズ",
            description = "A street kid trying to survive in Night City—a technology and body modification obsessed city of the future. Having everything to lose, he chooses to stay alive by becoming an edgerunner.",
            genres = "Sci-Fi, Cyberpunk, Action, Psychological",
            releaseYear = 2022,
            coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200&auto=format&fit=crop&q=80",
            isFeatured = true,
            published = true
        ),
        AnimeEntity(
            id = "aot",
            slug = "attack-on-titan",
            title = "Attack on Titan",
            altTitle = "進撃の巨人 (Shingeki no Kyojin)",
            description = "After his hometown is destroyed and his mother is killed, young Eren Jaeger vows to cleanse the earth of the giant humanoid Titans that have brought humanity to the brink of extinction.",
            genres = "Action, Military, Mystery, Shonen",
            releaseYear = 2023,
            coverUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80",
            isFeatured = true,
            published = true
        ),
        AnimeEntity(
            id = "chainsaw-man",
            slug = "chainsaw-man",
            title = "Chainsaw Man",
            altTitle = "チェンソーマン",
            description = "Denji is a teenage boy living with a Chainsaw Devil named Pochita. Due to the debt his father left behind, he is living a rock bottom life until a bloody betrayal transforms him into Chainsaw Man.",
            genres = "Action, Supernatural, Gore, Shonen",
            releaseYear = 2022,
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
            isFeatured = false,
            published = true
        ),
        AnimeEntity(
            id = "frieren",
            slug = "frieren-beyond-journeys-end",
            title = "Frieren: Beyond Journey's End",
            altTitle = "葬送のフリーレン (Sousou no Frieren)",
            description = "An elf mage named Frieren and her courageous fellow adventurers have defeated the Demon King and brought peace to the land. But as an elf who lives for thousands of years, she reflects on the fleeting lives of her human companions.",
            genres = "Adventure, Drama, Fantasy, Slice of Life",
            releaseYear = 2024,
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop&q=80",
            isFeatured = false,
            published = true
        )
    )
    database.animeDao().insertAnimeList(animeList)

    // 2. Seasons
    val seasons = listOf(
        SeasonEntity(
            id = "jjk-s1",
            animeId = "jjk",
            seasonNumber = 1,
            seasonTitle = "Season 1: Cursed Womb & Goodwill Event",
            description = "Introduction of Yuji, Megumi, Nobara, and Satoru Gojo confronting special grade curses.",
            published = true
        ),
        SeasonEntity(
            id = "jjk-s2",
            animeId = "jjk",
            seasonNumber = 2,
            seasonTitle = "Season 2: Hidden Inventory & Shibuya Incident",
            description = "Gojo's past friendship with Geto and the catastrophic Halloween siege in Shibuya.",
            published = true
        ),
        SeasonEntity(
            id = "ds-s2",
            animeId = "demon-slayer",
            seasonNumber = 2,
            seasonTitle = "Entertainment District Arc",
            description = "Sound Hashira Tengen Uzui recruits Tanjiro and team to hunt Upper Rank Six in the Yoshiwara district.",
            published = true
        ),
        SeasonEntity(
            id = "ds-s3",
            animeId = "demon-slayer",
            seasonNumber = 3,
            seasonTitle = "Swordsmith Village Arc",
            description = "Tanjiro visits the hidden village of swordsmiths and battles Upper Ranks Four and Five alongside Muichiro and Mitsuri.",
            published = true
        ),
        SeasonEntity(
            id = "cp-s1",
            animeId = "cyberpunk",
            seasonNumber = 1,
            seasonTitle = "Complete Mini-Series",
            description = "The 10-episode adrenaline rush through chrome, neural implants, and tragedy in Night City.",
            published = true
        ),
        SeasonEntity(
            id = "aot-s4",
            animeId = "aot",
            seasonNumber = 4,
            seasonTitle = "The Final Season",
            description = "The battle for Marley and the devastating activation of the Rumbling by Eren Jaeger.",
            published = true
        )
    )
    database.seasonDao().insertSeasons(seasons)

    // 3. Episodes
    val episodes = listOf(
        // JJK S2
        EpisodeEntity(
            id = "jjk-s2-ep09",
            seasonId = "jjk-s2",
            animeId = "jjk",
            episodeNumber = 9,
            episodeTitle = "Shibuya Incident - Gate, Open",
            description = "Satoru Gojo faces the cursed spirits in the B5F subway platform in a historic 0.2 second Unlimited Void.",
            thumbnailUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80",
            published = true
        ),
        EpisodeEntity(
            id = "jjk-s2-ep16",
            seasonId = "jjk-s2",
            animeId = "jjk",
            episodeNumber = 16,
            episodeTitle = "Thunderclap",
            description = "Sukuna awakens at fifteen fingers and battles the fire curse Jogo across the devastated skyline of Shibuya.",
            thumbnailUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80",
            published = true
        ),
        EpisodeEntity(
            id = "jjk-s2-ep17",
            seasonId = "jjk-s2",
            animeId = "jjk",
            episodeNumber = 17,
            episodeTitle = "Thunderclap, Part 2",
            description = "The legendary clash between Ryomen Sukuna and Eight-Handled Sword Divergent Sila Divine General Mahoraga.",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            published = true
        ),
        // Demon Slayer S2
        EpisodeEntity(
            id = "ds-s2-ep10",
            seasonId = "ds-s2",
            animeId = "demon-slayer",
            episodeNumber = 10,
            episodeTitle = "Never Give Up",
            description = "Tengen Uzui with his Musical Score technique and Tanjiro unleash their limits against Gyutaro.",
            thumbnailUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80",
            published = true
        ),
        // Cyberpunk S1
        EpisodeEntity(
            id = "cp-s1-ep06",
            seasonId = "cp-s1",
            animeId = "cyberpunk",
            episodeNumber = 6,
            episodeTitle = "Girl on Fire",
            description = "Maine experiences rapid cyberpsychosis while David uses his Sandevistan to rescue the crew.",
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
            published = true
        ),
        EpisodeEntity(
            id = "cp-s1-ep10",
            seasonId = "cp-s1",
            animeId = "cyberpunk",
            episodeNumber = 10,
            episodeTitle = "My Moon My Man",
            description = "David Martinez clad in the cyber-skeleton makes his final ascent at Arasaka Tower confronting Adam Smasher.",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            published = true
        )
    )
    database.episodeDao().insertEpisodes(episodes)

    // 4. Clips with accurate technical metadata
    val clips = listOf(
        ClipEntity(
            id = "clip-jjk-01",
            episodeId = "jjk-s2-ep17",
            seasonId = "jjk-s2",
            animeId = "jjk",
            title = "Sukuna Malevolent Shrine Domain Expansion Sequence",
            durationSeconds = 74,
            resolutionLabel = "3840x2160 (Native 4K)",
            width = 3840,
            height = 2160,
            frameRate = 60,
            codec = "ProRes 422 HQ",
            format = "MOV / MP4",
            fileSizeMb = 1420.5,
            downloadUrl = "https://storage.animeclips.online/raw/jjk_s2_ep17_shrine_4k.mp4",
            xmlPresetUrl = "https://storage.animeclips.online/presets/jjk_shrine_ae_flow.xml",
            is4k = true,
            viewCount = 38240,
            published = true,
            uploadDate = "2026-10-09"
        ),
        ClipEntity(
            id = "clip-jjk-02",
            episodeId = "jjk-s2-ep17",
            seasonId = "jjk-s2",
            animeId = "jjk",
            title = "Mahoraga Wheel Spin & Regeneration Impact",
            durationSeconds = 48,
            resolutionLabel = "3840x2160 (Native 4K)",
            width = 3840,
            height = 2160,
            frameRate = 60,
            codec = "HEVC / H.265",
            format = "MP4",
            fileSizeMb = 580.0,
            downloadUrl = "https://storage.animeclips.online/raw/jjk_mahoraga_impact_4k.mp4",
            xmlPresetUrl = "https://storage.animeclips.online/presets/impact_shake_aftereffects.xml",
            is4k = true,
            viewCount = 24190,
            published = true,
            uploadDate = "2026-10-08"
        ),
        ClipEntity(
            id = "clip-jjk-03",
            episodeId = "jjk-s2-ep09",
            seasonId = "jjk-s2",
            animeId = "jjk",
            title = "Satoru Gojo 0.2s Infinite Void Subway Clean Raw",
            durationSeconds = 62,
            resolutionLabel = "1920x1080 (FHD 60FPS)",
            width = 1920,
            height = 1080,
            frameRate = 60,
            codec = "H.264 / High 10",
            format = "MP4",
            fileSizeMb = 295.4,
            downloadUrl = "https://storage.animeclips.online/raw/jjk_gojo_subway_1080p.mp4",
            xmlPresetUrl = "https://storage.animeclips.online/presets/gojo_glow_twistor.xml",
            is4k = false,
            viewCount = 49100,
            published = true,
            uploadDate = "2026-10-07"
        ),
        ClipEntity(
            id = "clip-ds-01",
            episodeId = "ds-s2-ep10",
            seasonId = "ds-s2",
            animeId = "demon-slayer",
            title = "Tengen Musical Score Climax vs Gyutaro (60FPS Interpolated)",
            durationSeconds = 85,
            resolutionLabel = "3840x2160 (Native 4K)",
            width = 3840,
            height = 2160,
            frameRate = 60,
            codec = "HEVC / H.265 10-bit",
            format = "MP4",
            fileSizeMb = 1180.0,
            downloadUrl = "https://storage.animeclips.online/raw/ds_tengen_gyutaro_4k60.mp4",
            xmlPresetUrl = "https://storage.animeclips.online/presets/ufotable_fire_sparks.xml",
            is4k = true,
            viewCount = 67300,
            published = true,
            uploadDate = "2026-10-06"
        ),
        ClipEntity(
            id = "clip-cp-01",
            episodeId = "cp-s1-ep10",
            seasonId = "cp-s1",
            animeId = "cyberpunk",
            title = "David Sandevistan Cyber-Skeleton Speed Blitz",
            durationSeconds = 54,
            resolutionLabel = "3840x2160 (Native 4K)",
            width = 3840,
            height = 2160,
            frameRate = 60,
            codec = "ProRes 422 HQ",
            format = "MOV",
            fileSizeMb = 940.2,
            downloadUrl = "https://storage.animeclips.online/raw/edgerunners_david_sande_4k.mov",
            xmlPresetUrl = "https://storage.animeclips.online/presets/chromatic_aberration_glitch.xml",
            is4k = true,
            viewCount = 52180,
            published = true,
            uploadDate = "2026-10-05"
        ),
        ClipEntity(
            id = "clip-cp-02",
            episodeId = "cp-s1-ep06",
            seasonId = "cp-s1",
            animeId = "cyberpunk",
            title = "Lucy Moonwalk Braindance Sequence (Clean Background)",
            durationSeconds = 42,
            resolutionLabel = "1920x1080 (FHD)",
            width = 1920,
            height = 1080,
            frameRate = 24,
            codec = "H.264",
            format = "MP4",
            fileSizeMb = 180.0,
            downloadUrl = "https://storage.animeclips.online/raw/edgerunners_lucy_moon_1080p.mp4",
            xmlPresetUrl = "https://storage.animeclips.online/presets/ambient_dust_particles.xml",
            is4k = false,
            viewCount = 31400,
            published = true,
            uploadDate = "2026-10-04"
        )
    )
    database.clipDao().insertClips(clips)

    // 5. Featured Artworks Gallery
    val artworks = listOf(
        FeaturedArtworkEntity(
            id = "art-1",
            title = "Jujutsu Kaisen: Shibuya Arc Official Visual",
            description = "High dynamic range promotional banner showcasing Gojo Satoru and the cursed spirits.",
            artworkUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1000&auto=format&fit=crop&q=80",
            releaseInfo = "MAPPA Studio / 2023",
            altText = "JJK Shibuya key visual",
            displayOrder = 1
        ),
        FeaturedArtworkEntity(
            id = "art-2",
            title = "Demon Slayer: Infinity Castle Countdown",
            description = "Ufotable panoramic theatrical key visual featuring all Nine Hashira.",
            artworkUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=1000&auto=format&fit=crop&q=80",
            releaseInfo = "Ufotable / 2024",
            altText = "Demon Slayer Hashira artwork",
            displayOrder = 2
        ),
        FeaturedArtworkEntity(
            id = "art-3",
            title = "Cyberpunk: Edgerunners Moon Concept Art",
            description = "Studio TRIGGER neon aesthetic concept art for Night City and the Moon.",
            artworkUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1000&auto=format&fit=crop&q=80",
            releaseInfo = "Studio TRIGGER / CDPR",
            altText = "Edgerunners moon banner",
            displayOrder = 3
        )
    )
    database.featuredArtworkDao().insertArtworks(artworks)

    // 6. Community Posts
    val posts = listOf(
        CommunityPostEntity(
            id = "post-1",
            authorName = "KuroEditor",
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            category = "AMV Editing",
            title = "Best Twixtor Pro & Flow Settings for 60FPS Anime Cuts",
            content = "Hey creators! When dealing with native 24fps anime raws from the library, avoid ghosting by setting Motion Vectors to 'High Quality' and Frame Method to 'Blend'. Also check out the XML preset in the presets channel!",
            tags = "Twixtor, AfterEffects, 60FPS, Flow",
            imageUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80",
            likeCount = 142,
            commentCount = 19,
            timestamp = System.currentTimeMillis() - 7200000,
            isPinned = true
        ),
        CommunityPostEntity(
            id = "post-2",
            authorName = "NeonShifter",
            authorAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
            category = "Clip Requests",
            title = "Request: Frieren vs Clone Zoltraak 4K Raw in ProRes",
            content = "Can the admins upload the clean episode 26 fight scene without subtitles in ProRes 422? Need it for the upcoming AMV tournament!",
            tags = "Frieren, 4K, ProRes, RawRequest",
            likeCount = 89,
            commentCount = 7,
            timestamp = System.currentTimeMillis() - 14400000,
            isPinned = false
        ),
        CommunityPostEntity(
            id = "post-3",
            authorName = "SyncMaster",
            authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
            category = "XML & Presets",
            title = "Shared: Alight Motion & Premiere CC Impact Shake Pack v3",
            content = "Just dropped a 12-preset bundle tuned specifically for swords clashing and explosion impacts. Links work seamlessly on desktop and mobile editor.",
            tags = "Premiere, AlightMotion, ShakePack, XML",
            likeCount = 215,
            commentCount = 34,
            timestamp = System.currentTimeMillis() - 86400000,
            isPinned = false
        )
    )
    database.communityDao().insertPosts(posts)

    // Comments for post 1
    val comments = listOf(
        CommunityCommentEntity(
            id = "com-1",
            postId = "post-1",
            authorName = "VFX_Dan",
            content = "This solved the stuttering on my Sukuna edit instantly! Legend.",
            timestamp = System.currentTimeMillis() - 3600000,
            likeCount = 14
        ),
        CommunityCommentEntity(
            id = "post-1-c2",
            postId = "post-1",
            authorName = "SoraCuts",
            content = "Does this work in DaVinci Resolve Optical Flow too? Yes, set Enhanced Better mode.",
            timestamp = System.currentTimeMillis() - 1800000,
            likeCount = 8
        )
    )
    database.communityDao().insertComments(comments)

    // 7. Events
    val events = listOf(
        EventEntity(
            id = "event-1",
            title = "2026 Neon Cyberpunk AMV Grand Prix",
            description = "Create a high-energy edit utilizing any clips from Cyberpunk: Edgerunners, Akira, or Ghost in the Shell. Max length: 90 seconds.",
            coverUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&auto=format&fit=crop&q=80",
            eventType = "AMV Editing Contest",
            startDate = "2026-10-15",
            endDate = "2026-11-01",
            status = "UPCOMING",
            rules = "1. Original audio track allowed.\n2. Must use genuine raw clips without watermarks.\n3. Native 60fps exports only.",
            registrationLink = "https://animeclips.online/events/cyberpunk-gp",
            reward = "$1,500 Prize Pool + Verified Creator Badge",
            participantCount = 184
        ),
        EventEntity(
            id = "event-2",
            title = "48-Hour Speed Edit Challenge: Battle Shonen",
            description = "A rapid-fire contest where all participants receive a surprise clip pack and audio track on kick-off day.",
            coverUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=800&auto=format&fit=crop&q=80",
            eventType = "Speed Challenge",
            startDate = "2026-10-08",
            endDate = "2026-10-10",
            status = "ONGOING",
            rules = "Submit before countdown hits 0. Only tools: Premiere, AE, or Davinci.",
            registrationLink = "https://animeclips.online/events/speed-48h",
            reward = "VIP 4K Storage Access + Lifetime Perks",
            participantCount = 312
        )
    )
    database.eventDao().insertEvents(events)

    // 8. Blog Posts
    val blogPosts = listOf(
        BlogPostEntity(
            id = "blog-1",
            slug = "optimal-4k-amv-export-settings",
            title = "Optimal 4K AMV Export Settings for YouTube & TikTok (2026)",
            category = "Video Editing Tutorials",
            excerpt = "Prevent compression artifacts and banding when uploading high-motion anime edits to social video platforms.",
            content = "Exporting high-framerate anime clips requires understanding bitrate envelopes. Anime lines often suffer from macroblocking during fast whip pans. By targeting HEVC Constant Rate Factor (CRF) 14 with a keyframe distance of 24 frames, you retain razor-sharp line art without exceeding upload size caps...",
            coverUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&auto=format&fit=crop&q=80",
            authorName = "Alex Rivera (Chief Audio-Visual Engineer)",
            publishDate = "2026-10-04",
            readTimeMinutes = 6
        ),
        BlogPostEntity(
            id = "blog-2",
            slug = "color-grading-anime-raws-davinci",
            title = "Mastering Anime Color Grading in DaVinci Resolve",
            category = "Color Science",
            excerpt = "How to transform flat anime television broadcasts into cinematic, moody masterworks with node-based LUTs.",
            content = "Modern anime releases are balanced for standard Rec.709 displays. However, high-end AMVs leverage cinematic teal-and-orange color contrast, halation filters, and film grain emulation. In this guide, we break down our custom power grades for Jujutsu Kaisen and Cyberpunk clips...",
            coverUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=800&auto=format&fit=crop&q=80",
            authorName = "Maya Chen (Lead Colorist)",
            publishDate = "2026-09-28",
            readTimeMinutes = 8
        )
    )
    database.blogDao().insertBlogPosts(blogPosts)
}
