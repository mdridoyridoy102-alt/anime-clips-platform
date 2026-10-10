package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import com.example.data.ClipEntity
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGreen
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

@Composable
fun TechBadge(
    text: String,
    isAccent: Boolean = false,
    isCyan: Boolean = false,
    isGreen: Boolean = false,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isAccent -> BrandOrange.copy(alpha = 0.15f)
        isCyan -> AccentCyan.copy(alpha = 0.15f)
        isGreen -> AccentGreen.copy(alpha = 0.15f)
        else -> SurfaceHighlight
    }
    val textColor = when {
        isAccent -> BrandOrange
        isCyan -> AccentCyan
        isGreen -> AccentGreen
        else -> TextSecondary
    }
    val borderColor = when {
        isAccent -> BrandOrange.copy(alpha = 0.5f)
        isCyan -> AccentCyan.copy(alpha = 0.5f)
        isGreen -> AccentGreen.copy(alpha = 0.5f)
        else -> BorderSubtle
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .border(0.75.dp, borderColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                letterSpacing = 0.5.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )
    }
}

@Composable
fun SeasonActionDropdown(
    seasonNumber: Int,
    seasonTitle: String,
    onDownloadFullSeason: () -> Unit,
    onDownloadAllEpisodes: () -> Unit,
    onDownloadAllClips: () -> Unit,
    onViewSeasonDetails: () -> Unit,
    onCopySeasonLink: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.testTag("season_action_menu_$seasonNumber")
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Season Options",
                tint = TextSecondary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(SurfaceCharcoalElevated)
                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FolderZip, null, tint = BrandOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download Full Season", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                onClick = {
                    expanded = false
                    onDownloadFullSeason()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Archive, null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download All Episodes", color = TextPrimary)
                    }
                },
                onClick = {
                    expanded = false
                    onDownloadAllEpisodes()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Videocam, null, tint = AccentGreen, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download All Clips", color = TextPrimary)
                    }
                },
                onClick = {
                    expanded = false
                    onDownloadAllClips()
                }
            )
            HorizontalDivider(color = BorderSubtle)
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Season Details", color = TextPrimary)
                    }
                },
                onClick = {
                    expanded = false
                    onViewSeasonDetails()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ContentCopy, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy Season Link", color = TextSecondary)
                    }
                },
                onClick = {
                    expanded = false
                    onCopySeasonLink()
                }
            )
        }
    }
}

@Composable
fun EpisodeActionDropdown(
    episodeNumber: Int,
    onDownloadPackage: () -> Unit,
    onDownloadAllClips: () -> Unit,
    onViewEpisode: () -> Unit,
    onCopyLink: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.testTag("episode_action_menu_$episodeNumber")
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Episode Options",
                tint = TextSecondary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(SurfaceCharcoalElevated)
                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Archive, null, tint = BrandOrange, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download Episode Package", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                onClick = {
                    expanded = false
                    onDownloadPackage()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Videocam, null, tint = AccentCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download All Episode Clips", color = TextPrimary)
                    }
                },
                onClick = {
                    expanded = false
                    onDownloadAllClips()
                }
            )
            HorizontalDivider(color = BorderSubtle)
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayCircle, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Episode", color = TextPrimary)
                    }
                },
                onClick = {
                    expanded = false
                    onViewEpisode()
                }
            )
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ContentCopy, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy Episode Link", color = TextSecondary)
                    }
                },
                onClick = {
                    expanded = false
                    onCopyLink()
                }
            )
        }
    }
}

@Composable
fun LatestUploadDetailsDialog(
    clip: ClipEntity,
    animeTitle: String,
    onDismiss: () -> Unit,
    onOpenClip: () -> Unit,
    onDownload: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SurfaceCharcoalElevated,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TechBadge(text = if (clip.is4k) "GENUINE 4K RAW" else "1080p FHD", isAccent = clip.is4k)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = clip.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$animeTitle • Uploaded ${clip.uploadDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "TECHNICAL SPECIFICATIONS",
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandOrange
                )

                Spacer(modifier = Modifier.height(10.dp))

                TechnicalSpecRow("Resolution", "${clip.width} x ${clip.height} (${clip.resolutionLabel})")
                TechnicalSpecRow("Frame Rate", "${clip.frameRate} FPS")
                TechnicalSpecRow("Codec", clip.codec)
                TechnicalSpecRow("Container", clip.format)
                TechnicalSpecRow("File Size", "${clip.fileSizeMb} MB")
                TechnicalSpecRow("Duration", "${clip.durationSeconds}s")
                TechnicalSpecRow("XML Presets", if (clip.xmlPresetUrl.isNotEmpty()) "Available (AE/Premiere)" else "None")

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onOpenClip,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                    ) {
                        Text("Open Player")
                    }

                    Button(
                        onClick = onDownload,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                    ) {
                        Icon(Icons.Default.Download, null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TechnicalSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            color = TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            ),
            color = TextPrimary
        )
    }
}
