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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ClipEntity
import com.example.ui.components.EpisodeActionDropdown
import com.example.ui.components.TechBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BackgroundNearBlack
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCharcoal
import com.example.ui.theme.SurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AnimeClipsViewModel
import com.example.viewmodel.Screen

@Composable
fun EpisodeScreen(
    animeId: String,
    seasonId: String,
    episodeId: String,
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    val animeState by viewModel.getAnime(animeId).collectAsStateWithLifecycle(initialValue = null)
    val seasonState by viewModel.getSeason(seasonId).collectAsStateWithLifecycle(initialValue = null)
    val episodeState by viewModel.getEpisode(episodeId).collectAsStateWithLifecycle(initialValue = null)
    val clips by viewModel.getClipsForEpisode(episodeId).collectAsStateWithLifecycle(initialValue = emptyList())

    if (animeState == null || seasonState == null || episodeState == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundNearBlack),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading episode details...", color = TextSecondary)
        }
        return
    }

    val anime = animeState!!
    val season = seasonState!!
    val episode = episodeState!!

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("episode_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Episode Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCharcoal)
                    .padding(16.dp)
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
                                .testTag("episode_back_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${anime.title.uppercase()} / S${season.seasonNumber} / EP ${episode.episodeNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BrandOrange
                            )
                            Text(
                                text = episode.episodeTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                    }

                    EpisodeActionDropdown(
                        episodeNumber = episode.episodeNumber,
                        onDownloadPackage = {
                            viewModel.downloadEpisodePackage(episode, anime.title, clips.size)
                        },
                        onDownloadAllClips = {
                            viewModel.showMessage("Queued all ${clips.size} clips for download")
                        },
                        onViewEpisode = {},
                        onCopyLink = {
                            viewModel.showMessage("Episode link copied")
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (episode.description.isNotEmpty()) {
                    Text(
                        text = episode.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TechBadge(text = "${clips.size} RAW CLIPS", isAccent = true)
                    TechBadge(text = "4K / 1080p AVAILABLE", isCyan = true)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.downloadEpisodePackage(episode, anime.title, clips.size)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("download_episode_package_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Download, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Episode Package (${clips.size} Clips + XML)",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Section: Published Clips in Episode
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "AVAILABLE CLIPS IN THIS EPISODE",
                    style = MaterialTheme.typography.labelLarge,
                    color = BrandOrange
                )
                Text(
                    text = "Click to inspect technical specs or stream in the player",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        if (clips.isEmpty()) {
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
                        text = "No individual clips uploaded for this episode yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }
        } else {
            items(clips) { clip ->
                EpisodeClipCardItem(
                    clip = clip,
                    onClick = {
                        viewModel.navigateTo(Screen.ClipPlayer(clip.id))
                    },
                    onDownload = {
                        viewModel.downloadClip(clip, clip.resolutionLabel)
                    }
                )
            }
        }
    }
}

@Composable
fun EpisodeClipCardItem(
    clip: ClipEntity,
    onClick: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("episode_clip_card_${clip.id}"),
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
                        text = if (clip.is4k) "4K UHD" else "1080p FHD",
                        isAccent = clip.is4k,
                        isCyan = !clip.is4k
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    TechBadge(text = "${clip.frameRate} FPS")
                    if (clip.xmlPresetUrl.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        TechBadge(text = "XML PRESET", isGreen = true)
                    }
                }

                Text(
                    text = "${clip.durationSeconds}s • ${clip.fileSizeMb} MB",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = clip.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Format: ${clip.format} • Codec: ${clip.codec} • ${clip.width}x${clip.height}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDownload,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                ) {
                    Icon(Icons.Default.Download, null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Download", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
