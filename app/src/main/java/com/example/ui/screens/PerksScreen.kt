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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TechBadge
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentGold
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

@Composable
fun PerksScreen(
    viewModel: AnimeClipsViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundNearBlack)
            .testTag("perks_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCharcoal)
                    .padding(16.dp)
            ) {
                Text(
                    text = "COMMUNITY PERKS & RECOGNITION",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = TextPrimary
                )
                Text(
                    text = "Free open access for all editors, with special privileges for active community contributors",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Tier 1: Free Community Access
        item {
            PerkCard(
                title = "Standard Free Access",
                subtitle = "Unlimited access for all AMV editors & visitors",
                badge = "DEFAULT ACCESS",
                badgeColor = AccentGreen,
                features = listOf(
                    "Browse all published anime, seasons & episodes",
                    "Download 1080p and 4K raw video clips",
                    "Download Alight Motion & After Effects XML presets",
                    "Participate in public community discussions & comment threads",
                    "Register for community AMV challenges & speed edits"
                ),
                buttonText = "Current Tier (Active)",
                isCurrent = true,
                onClick = {}
            )
        }

        // Tier 2: Community Contributor
        item {
            PerkCard(
                title = "Verified Contributor",
                subtitle = "Earned through sharing presets, tutorials & high-quality clips",
                badge = "COMMUNITY EARNED",
                badgeColor = BrandOrange,
                features = listOf(
                    "Verified Creator badge on community posts & comments",
                    "Priority server download queues for high-volume 4K season packs",
                    "Direct clip request queue with fast track review",
                    "Access to VIP Discord creator role and internal feedback lounge",
                    "Eligibility for monthly creator showcase features"
                ),
                buttonText = "Join Contributor Program",
                isCurrent = false,
                onClick = { viewModel.showMessage("Contributor guidelines sent to your inbox.") }
            )
        }

        // Tier 3: Supporter & Server Booster
        item {
            PerkCard(
                title = "Server Supporter",
                subtitle = "Helping power high-bandwidth 4K video storage and server infrastructure",
                badge = "PATRON & SUPPORTER",
                badgeColor = AccentGold,
                features = listOf(
                    "Supporter role on Discord & Community Hub",
                    "Early access to newly ingested raw anime series archives",
                    "Beta tester access for new streaming and compression features",
                    "Exclusive monthly AMV editing asset packs & custom LUT collections"
                ),
                buttonText = "Learn How To Support",
                isCurrent = false,
                onClick = { viewModel.showMessage("Thank you! Visit animeclips.online/support for options.") }
            )
        }
    }
}

@Composable
fun PerkCard(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    features: List<String>,
    buttonText: String,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isCurrent) BrandOrange.copy(alpha = 0.5f) else BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TechBadge(text = badge, isAccent = isCurrent)
                if (isCurrent) {
                    TechBadge(text = "ACTIVE", isGreen = true)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            features.forEach { feat ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Check, null, tint = BrandOrange, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = feat,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCurrent) SurfaceHighlight else BrandOrange,
                    contentColor = if (isCurrent) TextSecondary else Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
