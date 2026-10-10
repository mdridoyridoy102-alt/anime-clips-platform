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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ClipEntity
import com.example.ui.components.TechBadge
import com.example.ui.components.TechnicalSpecRow
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.BackgroundNearBlack
import com.example.ui.theme.BorderMedium
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
import kotlinx.coroutines.delay

@Composable
fun ClipPlayerScreen(
    clipId: String,
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    val clipState by viewModel.getClip(clipId).collectAsStateWithLifecycle(initialValue = null)
    val latestClips by viewModel.latestClips.collectAsStateWithLifecycle()
    val allAnime by viewModel.allAnime.collectAsStateWithLifecycle()

    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0.24f) }
    var isLooping by remember { mutableStateOf(true) }
    var selectedQuality by remember { mutableStateOf("4K 2160p (Native)") }

    // Simulated playback progress
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100)
            progress += 0.005f
            if (progress >= 1f) {
                if (isLooping) progress = 0f else isPlaying = false
            }
        }
    }

    if (clipState == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundNearBlack),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading clip...", color = TextSecondary)
        }
        return
    }

    val clip = clipState!!
    val anime = allAnime.find { it.id == clip.animeId }
    val animeTitle = anime?.title ?: "Anime Resource"

    // Real available qualities based on clip technical specs
    val availableQualities = if (clip.is4k) {
        listOf("4K 2160p (Native)", "1440p (QHD)", "1080p (FHD)", "720p (HD)")
    } else {
        listOf("1080p (Native FHD)", "720p (HD)")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("clip_player_screen"),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Back Navigation Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCharcoal)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceHighlight)
                        .testTag("clip_player_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary, modifier = Modifier.size(16.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "$animeTitle / RAW PLAYER",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandOrange
                )
            }
        }

        // Video Player Viewport
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color.Black)
            ) {
                // Interactive player viewport simulation
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(SurfaceHighlight, Color.Black)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(BrandOrange.copy(alpha = 0.9f))
                                .clickable { isPlaying = !isPlaying }
                                .testTag("player_play_pause_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.Black,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        TechBadge(
                            text = if (clip.is4k) "STREAMING 4K 60FPS" else "STREAMING 1080p 60FPS",
                            isAccent = true
                        )
                    }
                }

                // Top right badges
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                ) {
                    TechBadge(text = "${clip.frameRate} FPS", isCyan = true)
                }

                // Player Bottom Controls Bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    // Scrub timeline
                    Slider(
                        value = progress,
                        onValueChange = { progress = it },
                        colors = SliderDefaults.colors(
                            thumbColor = BrandOrange,
                            activeTrackColor = BrandOrange,
                            inactiveTrackColor = Color.DarkGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val currentSec = (progress * clip.durationSeconds).toInt()
                            val totalSec = clip.durationSeconds
                            Text(
                                text = String.format("%02d:%02d / %02d:%02d", currentSec / 60, currentSec % 60, totalSec / 60, totalSec % 60),
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                                color = TextPrimary
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { isLooping = !isLooping },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Repeat,
                                    contentDescription = "Loop Clip",
                                    tint = if (isLooping) BrandOrange else TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            IconButton(
                                onClick = { viewModel.showMessage("Audio volume: 100%") },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, "Audio", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                            IconButton(
                                onClick = { viewModel.showMessage("Fullscreen toggle") },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Fullscreen, "Fullscreen", tint = TextSecondary, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Title and actions
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = clip.title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$animeTitle • Uploaded ${clip.uploadDate} • ${clip.viewCount} Views",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quality Selector (Real available versions)
                Text(
                    text = "SELECT DOWNLOAD QUALITY (ACTUAL RESOLUTIONS)",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandOrange
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(availableQualities) { q ->
                        val isSel = selectedQuality.startsWith(q.take(4))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSel) BrandOrange else SurfaceHighlight)
                                .border(1.dp, if (isSel) BrandOrange else BorderSubtle, RoundedCornerShape(6.dp))
                                .clickable { selectedQuality = q }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = q,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isSel) Color.Black else TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons: Download Clip & XML Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.downloadClip(clip, selectedQuality)
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("clip_download_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Download, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Download Clip",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (clip.xmlPresetUrl.isNotEmpty()) {
                        Button(
                            onClick = {
                                viewModel.showMessage("XML Presets downloaded: /storage/AnimeClips/Presets/flow.xml")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("clip_xml_download_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "XML Presets",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.showMessage("Clip link copied to clipboard")
                        },
                        modifier = Modifier.size(44.dp),
                        contentPadding = PaddingValues(0.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, "Copy", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Technical Metadata Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "TECHNICAL METADATA",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandOrange
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TechnicalSpecRow("Resolution", "${clip.width} x ${clip.height} (${clip.resolutionLabel})")
                    TechnicalSpecRow("Frame Rate", "${clip.frameRate} FPS")
                    TechnicalSpecRow("Video Codec", clip.codec)
                    TechnicalSpecRow("Container Format", clip.format)
                    TechnicalSpecRow("File Size", "${clip.fileSizeMb} MB")
                    TechnicalSpecRow("Duration", "${clip.durationSeconds} Seconds")
                    TechnicalSpecRow("Color Space", "BT.709 10-bit Raw")
                    TechnicalSpecRow("XML Inclusion", if (clip.xmlPresetUrl.isNotEmpty()) "Available (AE/Premiere/Alight)" else "None")
                }
            }
        }

        // Related Clips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "RELATED ANIME RAW CLIPS",
                    style = MaterialTheme.typography.labelLarge,
                    color = BrandOrange
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val related = latestClips.filter { it.id != clip.id }
                    items(related) { rel ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .clickable { viewModel.navigateTo(Screen.ClipPlayer(rel.id)) },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                TechBadge(text = if (rel.is4k) "4K" else "1080p", isAccent = rel.is4k)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = rel.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary,
                                    maxLines = 2
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${rel.fileSizeMb} MB • ${rel.frameRate} FPS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
