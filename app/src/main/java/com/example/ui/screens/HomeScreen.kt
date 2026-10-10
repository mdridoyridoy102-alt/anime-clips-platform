package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.AnimeEntity
import com.example.data.ClipEntity
import com.example.data.FeaturedArtworkEntity
import com.example.ui.components.LatestUploadDetailsDialog
import com.example.ui.components.TechBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BackgroundNearBlack
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.BrandOrangeDark
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCharcoal
import com.example.ui.theme.SurfaceCharcoalElevated
import com.example.ui.theme.SurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AnimeClipsViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    val allAnime by viewModel.allAnime.collectAsStateWithLifecycle()
    val latestClips by viewModel.latestClips.collectAsStateWithLifecycle()
    val artworks by viewModel.featuredArtworks.collectAsStateWithLifecycle()
    val communityPosts by viewModel.communityPosts.collectAsStateWithLifecycle()
    val selectedDetailClip by viewModel.selectedLatestUpload.collectAsStateWithLifecycle()

    // Dialog for Latest Upload Details
    selectedDetailClip?.let { clip ->
        val parentAnime = allAnime.find { it.id == clip.animeId }?.title ?: "Anime Resource"
        LatestUploadDetailsDialog(
            clip = clip,
            animeTitle = parentAnime,
            onDismiss = { viewModel.setLatestUploadDetails(null) },
            onOpenClip = {
                viewModel.setLatestUploadDetails(null)
                viewModel.navigateTo(Screen.ClipPlayer(clip.id))
            },
            onDownload = {
                viewModel.setLatestUploadDetails(null)
                viewModel.downloadClip(clip, clip.resolutionLabel)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 3.1 Hero Section
        item {
            HeroSection(
                onBrowseClips = { viewModel.navigateTo(Screen.AnimeLibrary) },
                onJoinCommunity = { viewModel.navigateTo(Screen.Community) }
            )
        }

        // 3.2 Featured Anime Artwork Gallery
        if (artworks.isNotEmpty()) {
            item {
                FeaturedArtworkGallerySection(
                    artworks = artworks,
                    onBrowseAll = { viewModel.navigateTo(Screen.AnimeLibrary) }
                )
            }
        }

        // 3.3 Latest Uploads Section
        item {
            LatestUploadsHeader()
        }

        if (latestClips.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCharcoal)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No clips published yet. Upload via the Admin App.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        } else {
            items(latestClips) { clip ->
                val parentAnime = allAnime.find { it.id == clip.animeId }?.title ?: "Anime Resource"
                LatestUploadCard(
                    clip = clip,
                    animeTitle = parentAnime,
                    onOpen = { viewModel.navigateTo(Screen.ClipPlayer(clip.id)) },
                    onDetails = { viewModel.setLatestUploadDetails(clip) },
                    onDownload = { viewModel.downloadClip(clip, clip.resolutionLabel) }
                )
            }
        }

        // 3.5 Community Preview
        item {
            CommunityPreviewSection(
                postCount = communityPosts.size,
                topPostTitle = communityPosts.firstOrNull()?.title ?: "Twixtor 60FPS Guide for Anime Clips",
                topPostAuthor = communityPosts.firstOrNull()?.authorName ?: "KuroEditor",
                onJoinCommunity = { viewModel.navigateTo(Screen.Community) }
            )
        }

        // 3.6 Home Page Footer
        item {
            HomePageFooter(
                onNavigate = { viewModel.navigateTo(it) }
            )
        }
    }
}

@Composable
fun HeroSection(
    onBrowseClips: () -> Unit,
    onJoinCommunity: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceCharcoalElevated,
                        SurfaceCard,
                        BackgroundNearBlack
                    )
                )
            )
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TechBadge(text = "VERSION 2.6", isAccent = true)
                Spacer(modifier = Modifier.width(8.dp))
                TechBadge(text = "4K RAW ARCHIVE", isCyan = true)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "ULTRA-HIGH BITRATE ANIME CLIPS & EDITING RESOURCES",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 32.sp
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Uncompressed 4K 60FPS anime cuts, full-season packages, raw footage, and XML presets curated specifically for AMV and edit creators.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBrowseClips,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_browse_clips_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Movie, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Browse Clips",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onJoinCommunity,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("hero_join_community_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Forum, null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Community",
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedArtworkGallerySection(
    artworks: List<FeaturedArtworkEntity>,
    onBrowseAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FEATURED ARTWORK GALLERY",
                    style = MaterialTheme.typography.labelLarge,
                    color = BrandOrange
                )
                Text(
                    text = "Official studio visuals & seasonal promotional banners",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(artworks) { item ->
                ArtworkCard(artwork = item)
            }
        }
    }
}

@Composable
fun ArtworkCard(artwork: FeaturedArtworkEntity) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(180.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = artwork.artworkUrl,
                contentDescription = artwork.altText,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                if (artwork.releaseInfo.isNotEmpty()) {
                    TechBadge(text = artwork.releaseInfo, isCyan = true)
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = artwork.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (artwork.description.isNotEmpty()) {
                    Text(
                        text = artwork.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun LatestUploadsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Videocam,
            contentDescription = null,
            tint = BrandOrange,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "LATEST PUBLISHED UPLOADS",
                style = MaterialTheme.typography.labelLarge,
                color = BrandOrange
            )
            Text(
                text = "Recently added clips, episodes & XML packages",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun LatestUploadCard(
    clip: ClipEntity,
    animeTitle: String,
    onOpen: () -> Unit,
    onDetails: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onOpen)
            .testTag("latest_upload_card_${clip.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TechBadge(
                        text = if (clip.is4k) "4K UHD" else "1080p",
                        isAccent = clip.is4k,
                        isCyan = !clip.is4k
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TechBadge(text = "${clip.frameRate} FPS")
                    if (clip.xmlPresetUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        TechBadge(text = "XML", isGreen = true)
                    }
                }

                Text(
                    text = "${clip.fileSizeMb} MB",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = clip.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$animeTitle • Codec: ${clip.codec}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderSubtle)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = onDetails,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium)
                    ) {
                        Icon(Icons.Default.Info, null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tech Specs", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = onDownload,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BrandOrange.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.Download, null, tint = BrandOrange, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Download", style = MaterialTheme.typography.labelSmall, color = BrandOrange)
                    }
                }

                Button(
                    onClick = onOpen,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceHighlight),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Open", color = TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, null, tint = TextPrimary, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
fun CommunityPreviewSection(
    postCount: Int,
    topPostTitle: String,
    topPostAuthor: String,
    onJoinCommunity: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceCharcoalElevated)
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Forum, null, tint = AccentCyan, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "COMMUNITY HUB",
                        style = MaterialTheme.typography.labelLarge,
                        color = AccentCyan
                    )
                }

                TechBadge(text = "14,800+ EDITORS", isGreen = true)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Connect with AMV editors, request raw clips, share Alight Motion/After Effects XML presets, and participate in challenges.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Highlighted post preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceCard)
                    .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "PINNED TOPIC",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandOrange
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = topPostTitle,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "by $topPostAuthor • 142 Likes • 19 Replies",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onJoinCommunity,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("community_preview_join_button"),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCyan),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Explore Community Discussions",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HomePageFooter(
    onNavigate: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        HorizontalDivider(color = BorderSubtle)
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(BrandOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, null, tint = Color.Black, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ANIMExCLIPS PLATFORM",
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "animeclips.online is a community repository for anime editors and creators providing high-fidelity raw cuts, XML presets, and AMV resources.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quick footer navigation links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.clickable { onNavigate(Screen.AnimeLibrary) }
            )
            Text(
                text = "Events",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.clickable { onNavigate(Screen.Events) }
            )
            Text(
                text = "Tutorials",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.clickable { onNavigate(Screen.Blog) }
            )
            Text(
                text = "Perks",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                modifier = Modifier.clickable { onNavigate(Screen.Perks) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "DMCA & COPYRIGHT POLICY: All clips are provided strictly for transformative fair-use editing and educational AMV production. All rights belong to their respective studios.",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
            color = TextMuted
        )
    }
}
