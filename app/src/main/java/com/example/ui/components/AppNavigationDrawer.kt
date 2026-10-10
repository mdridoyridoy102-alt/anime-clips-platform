package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BrandOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCharcoal
import com.example.ui.theme.SurfaceCharcoalElevated
import com.example.ui.theme.SurfaceHighlight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.Screen

@Composable
fun AppNavigationDrawer(
    isOpen: Boolean,
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onClose: () -> Unit
) {
    if (isOpen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .clickable(onClick = onClose)
                .testTag("drawer_backdrop")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(320.dp)
                    .align(Alignment.CenterEnd)
                    .background(SurfaceCharcoalElevated)
                    .clickable(enabled = false) {} // Consume clicks inside drawer
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Drawer Header with Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BrandOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "NAVIGATION",
                                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                                color = BrandOrange
                            )
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceHighlight)
                                .testTag("drawer_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "MAIN DIRECTORY",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // THE 5 MAIN MENU ITEMS IN EXACT REQUIRED ORDER:
                    // 1. Home
                    DrawerMenuItem(
                        title = "1. Home",
                        icon = Icons.Default.Home,
                        isSelected = currentScreen is Screen.Home,
                        tag = "drawer_item_home",
                        onClick = { onNavigate(Screen.Home) }
                    )

                    // 2. Anime Clips
                    DrawerMenuItem(
                        title = "2. Anime Clips",
                        icon = Icons.Default.Movie,
                        isSelected = currentScreen is Screen.AnimeLibrary,
                        tag = "drawer_item_anime_clips",
                        onClick = { onNavigate(Screen.AnimeLibrary) }
                    )

                    // 3. Events
                    DrawerMenuItem(
                        title = "3. Events",
                        icon = Icons.Default.Event,
                        isSelected = currentScreen is Screen.Events,
                        tag = "drawer_item_events",
                        onClick = { onNavigate(Screen.Events) }
                    )

                    // 4. Blog
                    DrawerMenuItem(
                        title = "4. Blog",
                        icon = Icons.Default.Article,
                        isSelected = currentScreen is Screen.Blog,
                        tag = "drawer_item_blog",
                        onClick = { onNavigate(Screen.Blog) }
                    )

                    // 5. Perks
                    DrawerMenuItem(
                        title = "5. Perks",
                        icon = Icons.Default.CardGiftcard,
                        isSelected = currentScreen is Screen.Perks,
                        tag = "drawer_item_perks",
                        onClick = { onNavigate(Screen.Perks) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "SHORTCUTS",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Community shortcut
                    DrawerMenuItem(
                        title = "Community Hub",
                        icon = Icons.Default.Forum,
                        isSelected = currentScreen is Screen.Community,
                        tag = "drawer_item_community",
                        onClick = { onNavigate(Screen.Community) }
                    )

                    // Search shortcut
                    DrawerMenuItem(
                        title = "Search Platform",
                        icon = Icons.Default.Search,
                        isSelected = currentScreen is Screen.Search,
                        tag = "drawer_item_search",
                        onClick = { onNavigate(Screen.Search) }
                    )

                    // Downloads shortcut
                    DrawerMenuItem(
                        title = "Download Queue",
                        icon = Icons.Default.Download,
                        isSelected = currentScreen is Screen.Downloads,
                        tag = "drawer_item_downloads",
                        onClick = { onNavigate(Screen.Downloads) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = BorderSubtle)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ADMINISTRATION",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Android Admin App Integration
                    DrawerMenuItem(
                        title = "Admin App Portal",
                        icon = Icons.Default.AdminPanelSettings,
                        isSelected = currentScreen is Screen.AdminPanel,
                        tag = "drawer_item_admin",
                        isAccent = true,
                        onClick = { onNavigate(Screen.AdminPanel) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Social Links
                    Text(
                        text = "CONNECT WITH COMMUNITY",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SocialBadge("Discord")
                        SocialBadge("X (Twitter)")
                        SocialBadge("GitHub")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "v2.6.4 • animeclips.online",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    tag: String,
    isAccent: Boolean = false,
    onClick: () -> Unit
) {
    val bg = if (isSelected) BrandOrange.copy(alpha = 0.15f) else Color.Transparent
    val borderCol = if (isSelected) BrandOrange else Color.Transparent
    val textCol = if (isSelected) BrandOrange else if (isAccent) BrandOrange else TextPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected || isAccent) BrandOrange else TextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = textCol
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(BrandOrange)
            )
        }
    }
}

@Composable
fun SocialBadge(name: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceHighlight)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = TextSecondary
        )
    }
}
