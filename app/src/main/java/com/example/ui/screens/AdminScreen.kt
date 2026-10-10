package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AnimeEntity
import com.example.data.ClipEntity
import com.example.data.EpisodeEntity
import com.example.data.SeasonEntity
import com.example.ui.components.TechBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGreen
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
import java.util.UUID

@Composable
fun AdminScreen(
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    var adminTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Add Anime", "Add Clip", "Manage Catalog", "Community Moderation")

    val allAnime by viewModel.allAnime.collectAsStateWithLifecycle()
    val allPosts by viewModel.communityPosts.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("admin_screen")
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceCharcoal)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceHighlight)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextPrimary, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AdminPanelSettings, null, tint = BrandOrange, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "ANDROID ADMIN APP PORTAL",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                            color = BrandOrange
                        )
                        Text(
                            text = "Connected to shared AnimeClips platform database",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Navigation Tabs
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tabs.indices.toList()) { index ->
                    val isSel = adminTab == index
                    FilterChip(
                        selected = isSel,
                        onClick = { adminTab = index },
                        label = { Text(tabs[index], style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandOrange,
                            selectedLabelColor = Color.Black,
                            containerColor = SurfaceHighlight,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSel,
                            borderColor = if (isSel) BrandOrange else BorderSubtle
                        )
                    )
                }
            }
        }

        // Tab Content
        when (adminTab) {
            0 -> AddAnimeForm(onAddAnime = { viewModel.adminAddAnime(it) })
            1 -> AddClipForm(animeList = allAnime, onAddClip = { viewModel.adminAddClip(it) })
            2 -> ManageCatalogList(animeList = allAnime, onDeleteAnime = { viewModel.adminDeleteAnime(it) })
            3 -> CommunityModerationList(posts = allPosts, onDeletePost = { viewModel.deletePost(it) })
        }
    }
}

@Composable
fun AddAnimeForm(
    onAddAnime: (AnimeEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var altTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var genres by remember { mutableStateOf("Action, Shonen, Supernatural") }
    var year by remember { mutableStateOf("2026") }
    var coverUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "PUBLISH NEW ANIME TITLE",
                style = MaterialTheme.typography.labelLarge,
                color = BrandOrange
            )
            Text(
                text = "Add a title to the public anime clips library",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        item {
            AdminTextField(value = title, onValueChange = { title = it }, label = "Anime Title (e.g. Solo Leveling)")
        }
        item {
            AdminTextField(value = altTitle, onValueChange = { altTitle = it }, label = "Japanese / Romaji Title")
        }
        item {
            AdminTextField(value = description, onValueChange = { description = it }, label = "Synopsis / Description", singleLine = false)
        }
        item {
            AdminTextField(value = genres, onValueChange = { genres = it }, label = "Genres (comma separated)")
        }
        item {
            AdminTextField(value = year, onValueChange = { year = it }, label = "Release Year")
        }
        item {
            AdminTextField(value = coverUrl, onValueChange = { coverUrl = it }, label = "Cover Poster Image URL")
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val slug = title.lowercase().replace(" ", "-").replace(":", "")
                        val entity = AnimeEntity(
                            id = "anime-" + UUID.randomUUID().toString().take(6),
                            slug = slug,
                            title = title,
                            altTitle = altTitle,
                            description = description,
                            genres = genres,
                            releaseYear = year.toIntOrNull() ?: 2026,
                            coverUrl = coverUrl,
                            bannerUrl = coverUrl,
                            isFeatured = true,
                            published = true
                        )
                        onAddAnime(entity)
                        title = ""
                        altTitle = ""
                        description = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Publish Anime To Shared Backend", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AddClipForm(
    animeList: List<AnimeEntity>,
    onAddClip: (ClipEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedAnimeId by remember { mutableStateOf(animeList.firstOrNull()?.id ?: "jjk") }
    var is4k by remember { mutableStateOf(true) }
    var resolution by remember { mutableStateOf("3840x2160 (Native 4K)") }
    var frameRate by remember { mutableStateOf("60") }
    var codec by remember { mutableStateOf("ProRes 422 HQ") }
    var duration by remember { mutableStateOf("45") }
    var fileSize by remember { mutableStateOf("680.0") }
    var xmlPresetUrl by remember { mutableStateOf("https://storage.animeclips.online/presets/pack.xml") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "UPLOAD RAW ANIME CLIP",
                style = MaterialTheme.typography.labelLarge,
                color = BrandOrange
            )
            Text(
                text = "Attach verified technical specs and XML presets to a clip",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        item {
            AdminTextField(value = title, onValueChange = { title = it }, label = "Clip Title (e.g. Hollow Purple 4K 60FPS)")
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Genuine Native 4K Master", color = TextPrimary)
                Switch(
                    checked = is4k,
                    onCheckedChange = {
                        is4k = it
                        resolution = if (it) "3840x2160 (Native 4K)" else "1920x1080 (FHD)"
                    },
                    colors = SwitchDefaults.colors(checkedThumbColor = BrandOrange, checkedTrackColor = SurfaceHighlight)
                )
            }
        }

        item {
            AdminTextField(value = resolution, onValueChange = { resolution = it }, label = "Resolution Label")
        }
        item {
            AdminTextField(value = frameRate, onValueChange = { frameRate = it }, label = "Frame Rate (FPS)")
        }
        item {
            AdminTextField(value = codec, onValueChange = { codec = it }, label = "Codec (e.g. ProRes 422 HQ, HEVC)")
        }
        item {
            AdminTextField(value = duration, onValueChange = { duration = it }, label = "Duration (Seconds)")
        }
        item {
            AdminTextField(value = fileSize, onValueChange = { fileSize = it }, label = "File Size (MB)")
        }
        item {
            AdminTextField(value = xmlPresetUrl, onValueChange = { xmlPresetUrl = it }, label = "XML Preset URL (Optional)")
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val clip = ClipEntity(
                            id = "clip-" + UUID.randomUUID().toString().take(6),
                            episodeId = "jjk-s2-ep17",
                            seasonId = "jjk-s2",
                            animeId = selectedAnimeId,
                            title = title,
                            durationSeconds = duration.toIntOrNull() ?: 45,
                            resolutionLabel = resolution,
                            width = if (is4k) 3840 else 1920,
                            height = if (is4k) 2160 else 1080,
                            frameRate = frameRate.toIntOrNull() ?: 60,
                            codec = codec,
                            format = "MP4",
                            fileSizeMb = fileSize.toDoubleOrNull() ?: 500.0,
                            downloadUrl = "https://storage.animeclips.online/raw/clip.mp4",
                            xmlPresetUrl = xmlPresetUrl,
                            is4k = is4k,
                            viewCount = 0,
                            published = true,
                            uploadDate = "2026-10-10"
                        )
                        onAddClip(clip)
                        title = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Publish Clip with Technical Metadata", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ManageCatalogList(
    animeList: List<AnimeEntity>,
    onDeleteAnime: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "CURRENT CATALOG (${animeList.size} TITLES)",
                style = MaterialTheme.typography.labelLarge,
                color = BrandOrange
            )
        }

        items(animeList) { anime ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(anime.title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text("${anime.releaseYear} • ${anime.genres}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    IconButton(onClick = { onDeleteAnime(anime.id) }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityModerationList(
    posts: List<com.example.data.CommunityPostEntity>,
    onDeletePost: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "MODERATION QUEUE (${posts.size} POSTS)",
                style = MaterialTheme.typography.labelLarge,
                color = BrandOrange
            )
        }

        items(posts) { post ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(post.title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                        Text("Author: ${post.authorName} • Category: ${post.category}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    }
                    IconButton(onClick = { onDeletePost(post.id) }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

@Composable
fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = TextSecondary) },
        singleLine = singleLine,
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedBorderColor = BrandOrange,
            unfocusedBorderColor = BorderSubtle
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
