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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.TechBadge
import com.example.ui.theme.AccentCyan
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

data class SearchResultItem(
    val id: String,
    val type: String, // "ANIME", "CLIP", "COMMUNITY", "EVENT", "BLOG"
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val targetScreen: Screen
)

@Composable
fun SearchScreen(
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    val query by viewModel.globalSearchQuery.collectAsStateWithLifecycle()
    val allAnime by viewModel.allAnime.collectAsStateWithLifecycle()
    val clips by viewModel.latestClips.collectAsStateWithLifecycle()
    val posts by viewModel.communityPosts.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle()
    val blogs by viewModel.blogPosts.collectAsStateWithLifecycle()

    var activeCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Anime", "Clips", "Community", "Events", "Guides")

    // Compile dynamic search results across all platform entities
    val rawResults = remember(query, allAnime, clips, posts, events, blogs) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) emptyList() else {
            val list = mutableListOf<SearchResultItem>()

            allAnime.filter { it.title.lowercase().contains(q) || it.altTitle.lowercase().contains(q) || it.genres.lowercase().contains(q) }
                .forEach {
                    list.add(SearchResultItem(it.id, "ANIME", it.title, "Anime Series • ${it.genres}", Icons.Default.Movie, Screen.AnimeDetail(it.id)))
                }

            clips.filter { it.title.lowercase().contains(q) || it.resolutionLabel.lowercase().contains(q) || it.codec.lowercase().contains(q) }
                .forEach {
                    list.add(SearchResultItem(it.id, "CLIP", it.title, "Raw Clip • ${it.resolutionLabel} • ${it.fileSizeMb}MB", Icons.Default.Videocam, Screen.ClipPlayer(it.id)))
                }

            posts.filter { it.title.lowercase().contains(q) || it.content.lowercase().contains(q) || it.tags.lowercase().contains(q) }
                .forEach {
                    list.add(SearchResultItem(it.id, "COMMUNITY", it.title, "Community Topic • ${it.category}", Icons.Default.Forum, Screen.PostDetail(it.id)))
                }

            events.filter { it.title.lowercase().contains(q) || it.description.lowercase().contains(q) }
                .forEach {
                    list.add(SearchResultItem(it.id, "EVENT", it.title, "Contest / Event • ${it.eventType}", Icons.Default.Event, Screen.Events))
                }

            blogs.filter { it.title.lowercase().contains(q) || it.content.lowercase().contains(q) }
                .forEach {
                    list.add(SearchResultItem(it.id, "BLOG", it.title, "Guide • ${it.category}", Icons.Default.Article, Screen.Blog))
                }

            list
        }
    }

    val filteredResults = when (activeCategory) {
        "Anime" -> rawResults.filter { it.type == "ANIME" }
        "Clips" -> rawResults.filter { it.type == "CLIP" }
        "Community" -> rawResults.filter { it.type == "COMMUNITY" }
        "Events" -> rawResults.filter { it.type == "EVENT" }
        "Guides" -> rawResults.filter { it.type == "BLOG" }
        else -> rawResults
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("search_screen")
    ) {
        // Search Input Top Header
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

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = query,
                    onValueChange = { viewModel.setGlobalSearchQuery(it) },
                    placeholder = { Text("Search entire platform...", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = BrandOrange) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setGlobalSearchQuery("") }) {
                                Icon(Icons.Default.Clear, "Clear", tint = TextSecondary)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedBorderColor = BrandOrange,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Filter Pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSel = activeCategory == cat
                    FilterChip(
                        selected = isSel,
                        onClick = { activeCategory = cat },
                        label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
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

        // Search Results List
        if (query.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Search, null, tint = TextMuted, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Global Repository Search",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Type an anime title, clip, 4K quality, XML pack, or contest keyword.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
        } else if (filteredResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No results found for '$query' in $activeCategory",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "FOUND ${filteredResults.size} MATCHING RESULTS",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrandOrange
                    )
                }

                items(filteredResults) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(item.targetScreen) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceHighlight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(item.icon, null, tint = BrandOrange, modifier = Modifier.size(20.dp))
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    TechBadge(text = item.type, isAccent = item.type == "CLIP", isCyan = item.type == "ANIME")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary,
                                        maxLines = 1
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
