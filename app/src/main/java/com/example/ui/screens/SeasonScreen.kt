package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.EpisodeEntity
import com.example.ui.components.EpisodeActionDropdown
import com.example.ui.components.SeasonActionDropdown
import com.example.ui.components.TechBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BackgroundNearBlack
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandOrange
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
fun SeasonScreen(
    animeId: String,
    seasonId: String,
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    val animeState by viewModel.getAnime(animeId).collectAsStateWithLifecycle(initialValue = null)
    val seasonState by viewModel.getSeason(seasonId).collectAsStateWithLifecycle(initialValue = null)
    val episodes by viewModel.getEpisodes(seasonId).collectAsStateWithLifecycle(initialValue = emptyList())
    val seasonClips by viewModel.getClipsForSeason(seasonId).collectAsStateWithLifecycle(initialValue = emptyList())

    if (animeState == null || seasonState == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundNearBlack),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading season...", color = TextSecondary)
        }
        return
    }

    val anime = animeState!!
    val season = seasonState!!

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("season_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Top Bar & Breadcrumbs
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCharcoal)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateBack() },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceHighlight)
                                .testTag("season_back_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${anime.title.uppercase()} / SEASON ${season.seasonNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandOrange
                            )
                            Text(
                                text = season.seasonTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                    }

                    SeasonActionDropdown(
                        seasonNumber = season.seasonNumber,
                        seasonTitle = season.seasonTitle,
                        onDownloadFullSeason = {
                            viewModel.downloadSeasonPackage(season, anime.title, episodes.size, seasonClips.size)
                        },
                        onDownloadAllEpisodes = {
                            viewModel.showMessage("Queued all ${episodes.size} episodes")
                        },
                        onDownloadAllClips = {
                            viewModel.showMessage("Queued all ${seasonClips.size} clips")
                        },
                        onViewSeasonDetails = {},
                        onCopySeasonLink = {
                            viewModel.showMessage("Season link copied")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Season description and quick stats
                if (season.description.isNotEmpty()) {
                    Text(
                        text = season.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TechBadge(text = "${episodes.size} EPISODES", isAccent = true)
                    TechBadge(text = "${seasonClips.size} RAW CLIPS", isCyan = true)
                    TechBadge(text = "4K 60FPS READY")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Direct "Download Full Season" Button
                Button(
                    onClick = {
                        viewModel.downloadSeasonPackage(season, anime.title, episodes.size, seasonClips.size)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("download_full_season_cta"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Download, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Full Season Package (Hierarchical ZIP)",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // Section Title: Published Episodes
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "PUBLISHED EPISODES IN THIS SEASON",
                    style = MaterialTheme.typography.labelLarge,
                    color = BrandOrange
                )
                Text(
                    text = "Select an episode to view clips and technical cut metadata",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        if (episodes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCharcoal)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No episodes currently uploaded for this season.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        } else {
            items(episodes) { ep ->
                EpisodeCardItem(
                    episode = ep,
                    animeTitle = anime.title,
                    onClick = {
                        viewModel.navigateTo(Screen.EpisodeDetail(anime.id, season.id, ep.id))
                    },
                    onDownloadPackage = {
                        viewModel.downloadEpisodePackage(ep, anime.title, 2)
                    },
                    onDownloadAllClips = {
                        viewModel.showMessage("Queued clips for Episode ${ep.episodeNumber}")
                    },
                    onViewEpisode = {
                        viewModel.navigateTo(Screen.EpisodeDetail(anime.id, season.id, ep.id))
                    },
                    onCopyLink = {
                        viewModel.showMessage("Episode link copied: /clips/${anime.slug}/season-${season.seasonNumber}/episode-${ep.episodeNumber}/")
                    }
                )
            }
        }
    }
}

@Composable
fun EpisodeCardItem(
    episode: EpisodeEntity,
    animeTitle: String,
    onClick: () -> Unit,
    onDownloadPackage: () -> Unit,
    onDownloadAllClips: () -> Unit,
    onViewEpisode: () -> Unit,
    onCopyLink: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("episode_card_${episode.episodeNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceHighlight)
            ) {
                if (episode.thumbnailUrl.isNotEmpty()) {
                    AsyncImage(
                        model = episode.thumbnailUrl,
                        contentDescription = episode.episodeTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Ep Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(4.dp)
                ) {
                    TechBadge(text = "EP ${episode.episodeNumber}", isAccent = true)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = episode.episodeTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary,
                    maxLines = 1
                )
                if (episode.description.isNotEmpty()) {
                    Text(
                        text = episode.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    TechBadge(text = "4K AVAILABLE", isCyan = true)
                }
            }

            EpisodeActionDropdown(
                episodeNumber = episode.episodeNumber,
                onDownloadPackage = onDownloadPackage,
                onDownloadAllClips = onDownloadAllClips,
                onViewEpisode = onViewEpisode,
                onCopyLink = onCopyLink
            )
        }
    }
}
