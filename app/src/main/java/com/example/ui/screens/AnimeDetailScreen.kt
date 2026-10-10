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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.AnimeEntity
import com.example.data.SeasonEntity
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
fun AnimeDetailScreen(
    animeId: String,
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    val animeState by viewModel.getAnime(animeId).collectAsStateWithLifecycle(initialValue = null)
    val seasons by viewModel.getSeasons(animeId).collectAsStateWithLifecycle(initialValue = emptyList())

    if (animeState == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundNearBlack),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading anime details...", color = TextSecondary)
        }
        return
    }

    val anime = animeState!!

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("anime_detail_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Backdrop Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                AsyncImage(
                    model = anime.bannerUrl.ifEmpty { anime.coverUrl },
                    contentDescription = anime.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.8f),
                                    BackgroundNearBlack
                                )
                            )
                        )
                )

                // Top Back Button & Breadcrumbs
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceCharcoal.copy(alpha = 0.8f))
                            .testTag("anime_detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "CLIPS / ${anime.slug.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandOrange
                    )
                }

                // Title info on bottom of hero
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TechBadge(text = "${anime.releaseYear}", isAccent = true)
                        Spacer(modifier = Modifier.width(8.dp))
                        TechBadge(text = "${seasons.size} SEASONS", isCyan = true)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = anime.title,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black),
                        color = TextPrimary
                    )

                    if (anime.altTitle.isNotEmpty()) {
                        Text(
                            text = anime.altTitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Synopsis & Genres
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "SYNOPSIS",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandOrange
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = anime.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "GENRES: ",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    Text(
                        text = anime.genres,
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PUBLISHED SEASONS (${seasons.size})",
                        style = MaterialTheme.typography.labelLarge,
                        color = BrandOrange
                    )
                    Text(
                        text = "Select season to browse episodes & clips",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }

        // Seasons List
        if (seasons.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceCharcoal)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No published seasons found for this anime.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
        } else {
            items(seasons) { season ->
                SeasonCardItem(
                    season = season,
                    animeTitle = anime.title,
                    onClick = {
                        viewModel.navigateTo(Screen.SeasonDetail(anime.id, season.id))
                    },
                    onDownloadFullSeason = {
                        viewModel.downloadSeasonPackage(season, anime.title, 12, 24)
                    },
                    onDownloadAllEpisodes = {
                        viewModel.showMessage("Queued all episode packages for Season ${season.seasonNumber}")
                    },
                    onDownloadAllClips = {
                        viewModel.showMessage("Queued all raw clips for Season ${season.seasonNumber}")
                    },
                    onViewDetails = {
                        viewModel.navigateTo(Screen.SeasonDetail(anime.id, season.id))
                    },
                    onCopyLink = {
                        viewModel.showMessage("Season link copied: /clips/${anime.slug}/season-${season.seasonNumber}/")
                    }
                )
            }
        }
    }
}

@Composable
fun SeasonCardItem(
    season: SeasonEntity,
    animeTitle: String,
    onClick: () -> Unit,
    onDownloadFullSeason: () -> Unit,
    onDownloadAllEpisodes: () -> Unit,
    onDownloadAllClips: () -> Unit,
    onViewDetails: () -> Unit,
    onCopyLink: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("season_card_${season.seasonNumber}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceHighlight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S${season.seasonNumber}",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        ),
                        color = BrandOrange
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = season.seasonTitle,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    if (season.description.isNotEmpty()) {
                        Text(
                            text = season.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        TechBadge(text = "4K / 1080p AVAILABLE", isCyan = true)
                    }
                }
            }

            SeasonActionDropdown(
                seasonNumber = season.seasonNumber,
                seasonTitle = season.seasonTitle,
                onDownloadFullSeason = onDownloadFullSeason,
                onDownloadAllEpisodes = onDownloadAllEpisodes,
                onDownloadAllClips = onDownloadAllClips,
                onViewSeasonDetails = onViewDetails,
                onCopySeasonLink = onCopyLink
            )
        }
    }
}
