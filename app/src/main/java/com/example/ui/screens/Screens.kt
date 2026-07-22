package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.database.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BurritoViewModel
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.LiveOrderStatus
import kotlinx.coroutines.launch

// ==========================================
// 1. HOME SCREEN
// ==========================================
@Composable
fun HomeScreenContent(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier,
    onNavigateToAccountSub: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Bento Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "PROPER BRITISH STREET FOOD",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = YorkshireGreen,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Yorkshire ",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = GravyBronze
                        )
                    )
                    Text(
                        text = "Burrito",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = YorkshireGold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ready for a proper roast?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = GravyBronze.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier.testTag("home_welcome_title")
                )
            }

            // User Profile / Account Trigger
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(GravyBronze)
                    .border(2.dp, YorkshireGold, CircleShape)
                    .clickable { viewModel.navigateTo(AppScreen.ACCOUNT) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Roast of the Week / Promo Banner
        ScrollingPromotionsPanel()

        Spacer(modifier = Modifier.height(16.dp))

        // Bento Grid Actions Section Title
        Text(
            text = "Eee, what's your fancy?",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = GravyBronze
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // Custom Bento Grid
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Row 1: Best Seller Card (The Beef Sunday Wrap) - col-span-2, height ~200.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GravyBronze)
                    .clickable { viewModel.navigateTo(AppScreen.ORDER) }
                    .testTag("order_now_card")
            ) {
                // Background subtle green glow (radial highlight effect)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = YorkshireGreen.copy(alpha = 0.25f),
                        radius = 160.dp.toPx(),
                        center = androidx.compose.ui.geometry.Offset(size.width, size.height)
                    )
                }

                // "Best Seller" badge top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(YorkshireGold, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "BEST SELLER",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = GravyBronze,
                            letterSpacing = 1.sp
                        )
                    )
                }

                // Content bottom left & Action Button
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "The Beef\nSunday Wrap",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = Color.White,
                            lineHeight = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Slow-braised beef, roasties & gravy",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = YorkshireGold
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.ORDER) },
                        colors = ButtonDefaults.buttonColors(containerColor = YorkshireGold, contentColor = GravyBronze),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ORDER NOW",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }

            // Row 2: Your Points (left) & Nearest Location (right) - height ~160.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Your Points Card
                val loyaltyProfile by viewModel.loyaltyProfile.collectAsState()
                val points = loyaltyProfile?.points ?: 1240
                val pointsUntilNext = if (points % 100 == 0) 100 else 100 - (points % 100)

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .clickable { viewModel.navigateTo(AppScreen.REWARDS) }
                        .testTag("my_rewards_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, YorkshireGold.copy(alpha = 0.2f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF2EFE9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = YorkshireGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "YOUR POINTS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GravyBronze.copy(alpha = 0.6f),
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = String.format("%,d", points),
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = YorkshireGreen
                                )
                            )
                        }

                        Text(
                            text = "$pointsUntilNext until next free wrap",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GravyBronze
                            )
                        )
                    }
                }

                // Nearest Spot Card
                val nearestStore = viewModel.selectedLocation
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp)
                        .clickable { viewModel.navigateTo(AppScreen.LOCATIONS) }
                        .testTag("find_nearest_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = YorkshireGreen),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "NEAREST",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.6f),
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = nearestStore,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            text = "0.4 mi • Open",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )
                    }
                }
            }

            // Row 3: Ask the AI Roastmaster - col-span-2, height ~80.dp
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable { onNavigateToAccountSub("assistant") }
                    .testTag("ai_assistant_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2EFE9))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GravyBronze),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = YorkshireGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "Ask the AI Roastmaster",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = GravyBronze
                                )
                            )
                            Text(
                                text = "\"What's the spiciest option?\"",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = GravyBronze.copy(alpha = 0.6f)
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = GravyBronze.copy(alpha = 0.3f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Row 4: Two smaller square/rect items: Catering (gold) & Yorkshire Club (dark)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable { onNavigateToAccountSub("catering") }
                        .testTag("catering_portal_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = YorkshireGold)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CATERING",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = GravyBronze,
                                letterSpacing = 2.sp
                            )
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable { viewModel.navigateTo(AppScreen.REWARDS) }
                        .testTag("yorkshire_club_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GravyBronze),
                    border = BorderStroke(1.dp, YorkshireGold)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "YORKSHIRE CLUB",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = YorkshireGold,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }

            // Row 5: Two more items: Burrito Passport (white with dark border) & Merchandise (white with light border)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable { onNavigateToAccountSub("passport") }
                        .testTag("passport_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(2.dp, GravyBronze)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PASSPORT",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = GravyBronze,
                                letterSpacing = 2.sp
                            )
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clickable { onNavigateToAccountSub("merch") }
                        .testTag("merch_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, GravyBronze.copy(alpha = 0.2f))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MERCHANDISE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = GravyBronze,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ScrollingPromotionsPanel() {
    val promos = listOf(
        PromoItem("Roast of the Week", "Slow Braised Beef wrap with double crispy roasties and extra thick meat gravy.", YorkshireGold),
        PromoItem("Seasonal Special", "Turkey, cranberry, stuffing, and pigs in blankets! A proper Christmas feast.", YorkshireGreen),
        PromoItem("Yorkshire Club Sub", "Free delivery, double rewards, & member events for £12/month.", RoastBrown)
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(promos) { promo ->
            Card(
                colors = CardDefaults.cardColors(containerColor = promo.bgColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .width(280.dp)
                    .height(110.dp)
                    .clickable { }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = promo.title,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = promo.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

data class PromoItem(val title: String, val description: String, val bgColor: Color)

@Composable
fun QuickActionsGrid(viewModel: BurritoViewModel, onNavigateToAccountSub: (String) -> Unit) {
    val items = listOf(
        QuickGridItem("Order Now", Icons.Default.Restaurant, YorkshireGreen, { viewModel.navigateTo(AppScreen.ORDER) }, "order_now_card"),
        QuickGridItem("My Rewards", Icons.Default.CardGiftcard, YorkshireGold, { viewModel.navigateTo(AppScreen.REWARDS) }, "my_rewards_card"),
        QuickGridItem("Find My Nearest", Icons.Default.LocationOn, RoastBrown, { viewModel.navigateTo(AppScreen.LOCATIONS) }, "find_nearest_card"),
        QuickGridItem("Yorkshire Club", Icons.Default.Star, GravyBronze, { viewModel.navigateTo(AppScreen.REWARDS) }, "yorkshire_club_card"),
        QuickGridItem("Catering Portal", Icons.Default.People, YorkshireGreen, { onNavigateToAccountSub("catering") }, "catering_portal_card"),
        QuickGridItem("Burrito Passport", Icons.Default.WorkspacePremium, RoastBrown, { onNavigateToAccountSub("passport") }, "passport_card"),
        QuickGridItem("AI Assistant", Icons.Default.Android, YorkshireGold, { onNavigateToAccountSub("assistant") }, "ai_assistant_card"),
        QuickGridItem("Merchandise", Icons.Default.ShoppingBag, GravyBronze, { onNavigateToAccountSub("merch") }, "merch_card")
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        for (i in items.indices step 2) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(item = items[i], modifier = Modifier.weight(1f))
                if (i + 1 < items.size) {
                    QuickActionCard(item = items[i + 1], modifier = Modifier.weight(1f))
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun QuickActionCard(item: QuickGridItem, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .height(100.dp)
            .clickable { item.onClick() }
            .testTag(item.testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(item.iconBgColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = item.iconBgColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = item.label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = GravyBronze
                )
            )
        }
    }
}

data class QuickGridItem(
    val label: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val onClick: () -> Unit,
    val testTag: String
)

// ==========================================
// 2. ORDER SCREEN (MOBILE ORDERING & CUSTOMISER)
// ==========================================
@Composable
fun OrderScreenContent(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier
) {
    val cartItems by viewModel.cartItems.collectAsState()
    var isCartViewActive by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Screen Header with Segmented Control for Builder vs Cart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(
                onClick = { isCartViewActive = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isCartViewActive) YorkshireGreen else Color.Transparent,
                    contentColor = if (!isCartViewActive) Color.White else RoastBrown
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("customizer_tab_button")
            ) {
                Icon(Icons.Default.Build, contentDescription = "Builder")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Build Burrito", fontWeight = FontWeight.Bold)
            }
            
            Button(
                onClick = { isCartViewActive = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCartViewActive) YorkshireGreen else Color.Transparent,
                    contentColor = if (isCartViewActive) Color.White else RoastBrown
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).testTag("cart_tab_button")
            ) {
                BadgedBox(
                    badge = {
                        if (cartItems.isNotEmpty()) {
                            Badge(containerColor = YorkshireGold) {
                                Text(cartItems.sumOf { it.quantity }.toString(), color = GravyBronze)
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.ShoppingBasket, contentDescription = "Pudding Basket")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Your Pud", fontWeight = FontWeight.Bold)
            }
        }

        AnimatedContent(
            targetState = isCartViewActive,
            transitionSpec = {
                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
            },
            label = "OrderTabs"
        ) { active ->
            if (active) {
                CartViewPanel(viewModel)
            } else {
                InteractiveCustomiserPanel(viewModel)
            }
        }
    }
}

@Composable
fun InteractiveCustomiserPanel(viewModel: BurritoViewModel) {
    val scrollState = rememberScrollState()
    var currentStep by remember { mutableStateOf(1) }
    
    val fillings = listOf("🥩 Slow Braised Beef", "🍗 Roast Chicken", "🧀 Cauliflower Cheese", "🐖 Pork", "🦃 Turkey (Seasonal)", "🌱 Vegan Roast")
    val potatoes = listOf("Classic Roasties", "Extra Crispy", "Double Portion")
    val stuffing = listOf("Sage & Onion", "Pork", "Gluten Free", "Extra Stuffing")
    val vegetables = listOf("Greens", "Carrots", "Red Cabbage", "Parsnips", "Seasonal Veg")
    val gravies = listOf("Traditional", "Extra Thick", "Peppercorn", "Onion Gravy", "No Gravy")
    val extras = listOf("Yorkshire Pigs in Blankets", "Extra Roast Potatoes", "Extra Meat", "Cheese", "Cranberry Sauce", "Horseradish", "Mustard", "Yorkshire Chilli Sauce")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Step Indicators Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (step in 1..6) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (step == currentStep) YorkshireGold
                            else if (step < currentStep) YorkshireGreen
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { currentStep = step },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = step.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (step <= currentStep) Color.White else RoastBrown
                        )
                    )
                }
            }
        }

        Text(
            text = when (currentStep) {
                1 -> "Step 1: Choose Filling"
                2 -> "Step 2: Choose Potatoes"
                3 -> "Step 3: Choose Stuffing"
                4 -> "Step 4: Choose Vegetables"
                5 -> "Step 5: Choose Gravy"
                else -> "Step 6: Choose Extras"
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = GravyBronze
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Step Options View (Scrollable contents)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            when (currentStep) {
                1 -> {
                    fillings.forEach { fill ->
                        CustomizerRadioButton(
                            label = fill,
                            selected = viewModel.selectedFilling == fill,
                            onClick = { viewModel.selectedFilling = fill }
                        )
                    }
                }
                2 -> {
                    potatoes.forEach { pot ->
                        CustomizerRadioButton(
                            label = pot,
                            selected = viewModel.selectedPotatoes == pot,
                            onClick = { viewModel.selectedPotatoes = pot }
                        )
                    }
                }
                3 -> {
                    stuffing.forEach { stf ->
                        CustomizerRadioButton(
                            label = stf,
                            selected = viewModel.selectedStuffing == stf,
                            onClick = { viewModel.selectedStuffing = stf }
                        )
                    }
                }
                4 -> {
                    vegetables.forEach { veg ->
                        CustomizerCheckbox(
                            label = veg,
                            checked = viewModel.selectedVegetables.contains(veg),
                            onCheckedChange = { viewModel.toggleVegetable(veg) }
                        )
                    }
                }
                5 -> {
                    gravies.forEach { grv ->
                        CustomizerRadioButton(
                            label = grv,
                            selected = viewModel.selectedGravy == grv,
                            onClick = { viewModel.selectedGravy = grv }
                        )
                    }
                }
                6 -> {
                    extras.forEach { ext ->
                        CustomizerCheckbox(
                            label = ext,
                            checked = viewModel.selectedExtras.contains(ext),
                            onCheckedChange = { viewModel.toggleExtra(ext) }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Sticky Bottom Builder Control Bar
        Card(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Summary of custom design
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Custom Yorkshire Wrap",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                        )
                        Text(
                            text = "${viewModel.selectedFilling} • ${viewModel.selectedGravy} Gravy",
                            style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                        )
                    }
                    Text(
                        text = "£${String.format("%.2f", viewModel.customBurritoPrice)}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var favoriteName by remember { mutableStateOf("") }
                    var showFavoriteDialog by remember { mutableStateOf(false) }

                    if (showFavoriteDialog) {
                        AlertDialog(
                            onDismissRequest = { showFavoriteDialog = false },
                            title = { Text("Name Your Custom Burrito") },
                            text = {
                                TextField(
                                    value = favoriteName,
                                    onValueChange = { favoriteName = it },
                                    placeholder = { Text("e.g. Grandma's Sunday Wrap") }
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        if (favoriteName.isNotBlank()) {
                                            viewModel.saveCustomBurritoAsFavorite(favoriteName)
                                            showFavoriteDialog = false
                                            favoriteName = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen)
                                ) {
                                    Text("Save")
                                }
                            }
                        )
                    }

                    // Save Favorite Button
                    IconButton(
                        onClick = { showFavoriteDialog = true },
                        modifier = Modifier
                            .size(50.dp)
                            .border(1.dp, YorkshireGold, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Add Favorite", tint = YorkshireGold)
                    }

                    // Add to basket Button
                    Button(
                        onClick = { viewModel.addCustomBurritoToCart() },
                        colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("add_custom_to_cart_button")
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add to Pudding Wrap", fontWeight = FontWeight.Bold)
                    }
                }

                // Next/Previous indicators for customization steps
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { if (currentStep > 1) currentStep-- },
                        enabled = currentStep > 1
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Prev Step")
                    }

                    TextButton(
                        onClick = { if (currentStep < 6) currentStep++ },
                        enabled = currentStep < 6
                    ) {
                        Text("Next Step")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomizerRadioButton(label: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) YorkshireGold else MaterialTheme.colorScheme.surfaceVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) YorkshireGold.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = GravyBronze))
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = YorkshireGold)
            )
        }
    }
}

@Composable
fun CustomizerCheckbox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (checked) 2.dp else 1.dp,
            color = if (checked) YorkshireGreen else MaterialTheme.colorScheme.surfaceVariant
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (checked) YorkshireGreen.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onCheckedChange(!checked) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = GravyBronze))
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkedColor = YorkshireGreen)
            )
        }
    }
}

@Composable
fun CartViewPanel(viewModel: BurritoViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val scrollState = rememberScrollState()

    if (cartItems.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.HourglassEmpty,
                contentDescription = null,
                tint = RoastBrown.copy(alpha = 0.4f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your Pudding Basket is Empty",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Eee, fill it up with some slow-braised roast beef or pigs in blankets! Build a custom wrap or choose a bundle.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = RoastBrown)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Cart List Section
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Items in Your Basket",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                    )
                }

                items(cartItems) { item ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                                )
                                if (!item.isBundle && item.filling.isNotBlank()) {
                                    Text(
                                        text = "${item.filling} • ${item.potatoes} • ${item.gravy} Gravy",
                                        style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                                    )
                                    if (item.vegetablesJson.isNotBlank()) {
                                        Text(
                                            text = "Veg: ${item.vegetablesJson}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                                        )
                                    }
                                    if (item.extrasJson.isNotBlank()) {
                                        Text(
                                            text = "Extras: ${item.extrasJson}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = YorkshireGreen)
                                        )
                                    }
                                } else if (item.isBundle) {
                                    Text(
                                        text = "Bundle includes: ${item.bundleItemsJson}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "£${String.format("%.2f", item.price)} each",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
                                )
                            }

                            // Quantity selectors
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { viewModel.updateCartQuantity(item, item.quantity - 1) },
                                    modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = item.quantity.toString(),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                IconButton(
                                    onClick = { viewModel.updateCartQuantity(item, item.quantity + 1) },
                                    modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            // Order Settings (Collect vs Delivery, Place order card)
            Card(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Service Mode Selectors (Collection, Delivery, Scheduled Collection)
                    Text(
                        text = "How would you like your roast?",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Collection", "Delivery", "Scheduled").forEach { mode ->
                            Button(
                                onClick = { viewModel.selectedOrderType = mode },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (viewModel.selectedOrderType == mode) YorkshireGold else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (viewModel.selectedOrderType == mode) Color.White else RoastBrown
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Text(mode, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    }

                    // Selected store indicator for click & collect
                    if (viewModel.selectedOrderType == "Collection") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = YorkshireGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Collecting from: ${viewModel.selectedLocation}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                            )
                        }
                    }

                    val basketTotal = cartItems.sumOf { it.price * it.quantity }
                    val deliveryFee = if (viewModel.selectedOrderType == "Delivery") 2.99 else 0.00
                    val loyaltyProfile by viewModel.loyaltyProfile.collectAsState()
                    val loyaltyDiscount = if (loyaltyProfile?.isPremiumMember == true && viewModel.selectedOrderType == "Delivery") -2.99 else 0.00
                    val grandTotal = basketTotal + deliveryFee + loyaltyDiscount

                    // Receipt outline
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Pudding Subtotal", color = RoastBrown)
                            Text("£${String.format("%.2f", basketTotal)}", color = RoastBrown)
                        }
                        if (viewModel.selectedOrderType == "Delivery") {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivery Fee", color = RoastBrown)
                                Text("£${String.format("%.2f", deliveryFee)}", color = RoastBrown)
                            }
                            if (loyaltyProfile?.isPremiumMember == true) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Yorkshire Club (Free Delivery)", color = YorkshireGreen)
                                    Text("-£${String.format("%.2f", deliveryFee)}", color = YorkshireGreen)
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Grand Total", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze))
                            Text("£${String.format("%.2f", grandTotal)}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen))
                        }
                    }

                    // Simulated Payment & Checkout button
                    var selectedPayment by remember { mutableStateOf("Credit Card") }
                    val pointsNeeded = (grandTotal * 100).toInt()
                    val canPayWithPoints = loyaltyProfile != null && loyaltyProfile!!.points >= pointsNeeded

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Credit Card", "Google Pay", "Loyalty Points").forEach { pay ->
                            val enabled = pay != "Loyalty Points" || canPayWithPoints
                            Button(
                                onClick = { selectedPayment = pay },
                                enabled = enabled,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedPayment == pay) YorkshireGreen else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (selectedPayment == pay) Color.White else RoastBrown
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).height(40.dp)
                            ) {
                                Text(
                                    text = if (pay == "Loyalty Points") "Points ($pointsNeeded)" else pay,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.placeOrder(selectedPayment) },
                        colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("checkout_button")
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Place Proper Order • £${String.format("%.2f", grandTotal)}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. LOCATIONS SCREEN
// ==========================================
@Composable
fun LocationsScreenContent(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier
) {
    val locations = listOf(
        BurritoLocation("Camden Market", "Food Hall, Camden Lock Place, NW1 8AL", "09:00 - 21:00", "5-10 min wait", "1.5 miles", "Camden Town (Tube)", "Market parking"),
        BurritoLocation("Boxpark Camden", "Unit 4, 74-88 Chalk Farm Rd, NW1 8AN", "11:00 - 22:00", "No queue", "1.7 miles", "Chalk Farm (Tube)", "Street parking"),
        BurritoLocation("Boxpark Wembley", "Olympic Way, Wembley Park, HA9 0JT", "11:00 - 23:00", "15-20 min wait", "8.2 miles", "Wembley Park (Tube)", "Stadium Red Parking"),
        BurritoLocation("Vauxhall Marketplace", "South Lambeth Rd, Vauxhall, SW8 1SR", "10:00 - 22:30", "10 min wait", "4.3 miles", "Vauxhall (Tube/Train)", "Sainsbury's Pay/Display"),
        BurritoLocation("Leicester Square", "Market Hall, Leicester Square, WC2H 7JY", "09:30 - 23:30", "20 min wait (Busy!)", "2.9 miles", "Leicester Square (Tube)", "Q-Park Leicester Sq")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Map Container Mock
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE2D6C5), Color(0xFFC7B19C))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Simulated grid/roads representation
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Lines for roads
                drawLine(Color.White, start = androidx.compose.ui.geometry.Offset(0f, 150f), end = androidx.compose.ui.geometry.Offset(size.width, 150f), strokeWidth = 10f)
                drawLine(Color.White, start = androidx.compose.ui.geometry.Offset(250f, 0f), end = androidx.compose.ui.geometry.Offset(250f, size.height), strokeWidth = 10f)
                drawLine(Color.White, start = androidx.compose.ui.geometry.Offset(0f, 350f), end = androidx.compose.ui.geometry.Offset(size.width, 350f), strokeWidth = 15f)
            }
            // Pins for locations
            locations.forEachIndexed { i, loc ->
                val offset = when (i) {
                    0 -> Alignment.TopStart
                    1 -> Alignment.TopEnd
                    2 -> Alignment.CenterStart
                    3 -> Alignment.CenterEnd
                    else -> Alignment.BottomCenter
                }
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = loc.name,
                    tint = if (viewModel.selectedLocation == loc.name) YorkshireGold else YorkshireGreen,
                    modifier = Modifier
                        .padding(24.dp)
                        .size(36.dp)
                        .align(offset)
                )
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GravyBronze.copy(alpha = 0.9f)),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "📍 Yorkshire Burrito Map Simulator",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text(
            text = "Select Store for Click & Collect",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(locations) { loc ->
                val selected = viewModel.selectedLocation == loc.name
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) YorkshireGreen.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = if (selected) 2.dp else 1.dp,
                        color = if (selected) YorkshireGreen else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectedLocation = loc.name }
                        .testTag("location_card_${loc.name.replace(" ", "_").lowercase()}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = loc.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                            )
                            if (selected) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = YorkshireGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "SELECTED",
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = loc.address,
                            style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LabelledIconText(Icons.Default.Schedule, loc.hours)
                            LabelledIconText(Icons.Default.Timer, loc.queueEstimate)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LabelledIconText(Icons.Default.Train, "Tube: ${loc.transit}")
                            LabelledIconText(Icons.Default.Info, "Parking: ${loc.parking}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LabelledIconText(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, size = 14.dp, tint = YorkshireGold)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = RoastBrown))
    }
}

data class BurritoLocation(
    val name: String,
    val address: String,
    val hours: String,
    val queueEstimate: String,
    val distance: String,
    val transit: String,
    val parking: String
)

// Helper function to set custom size for Icons
@Composable
fun Icon(imageVector: ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier.size(size)
    )
}

// ==========================================
// 4. REWARDS SCREEN (LOYALTY CARD & CLUB)
// ==========================================
@Composable
fun RewardsScreenContent(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier
) {
    val loyaltyProfile by viewModel.loyaltyProfile.collectAsState()
    val points = loyaltyProfile?.points ?: 0
    val isPremium = loyaltyProfile?.isPremiumMember ?: false
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Digital Loyalty Points Display Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(GravyBronze, RoastBrown)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Yorkshire Gold Points",
                    color = YorkshireGold,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = points.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 48.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.testTag("loyalty_points_display")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Earning 10 points per £1 spent",
                    color = CreamWhite.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Simulated Loyalty Stamp Board (Earn points)
        Text(
            text = "Your Pudding Stamp Board",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Every 100 points gets you a beautiful free reward!",
            style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LoyaltyStampsBoard(points)

        Spacer(modifier = Modifier.height(20.dp))

        // Premium Yorkshire Club Subscription
        PremiumClubSection(isPremium) { viewModel.togglePremiumSubscription() }

        Spacer(modifier = Modifier.height(20.dp))

        // Available Rewards list
        Text(
            text = "Redeem Rewards",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        RewardRedemptionsList(points) { pts ->
            viewModel.placeOrder("Loyalty Points") // triggers points deduction
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LoyaltyStampsBoard(points: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val stampsCount = (points % 100) / 10 // Represent stamps on 10-stamp card
            val boardRows = 2
            val boardCols = 5

            Text(
                text = "Next Free Burrito: ${points % 100}/100 pts towards stamp",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
            )
            Spacer(modifier = Modifier.height(12.dp))

            for (r in 0 until boardRows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (c in 0 until boardCols) {
                        val index = r * boardCols + c
                        val active = index < stampsCount
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (active) YorkshireGold else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(1.dp, RoastBrown.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (active) {
                                Icon(Icons.Default.Check, contentDescription = "Stamped", size = 20.dp, tint = Color.White)
                            } else {
                                Icon(Icons.Default.RadioButtonUnchecked, contentDescription = "Unstamped", size = 20.dp, tint = RoastBrown.copy(alpha = 0.4f))
                            }
                        }
                    }
                }
                if (r == 0) Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PremiumClubSection(isPremium: Boolean, onToggle: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = YorkshireGold.copy(alpha = 0.08f)),
        border = BorderStroke(1.5.dp, YorkshireGold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Yorkshire Club",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                    )
                    Text(
                        text = "Premium Subscription",
                        style = MaterialTheme.typography.bodySmall.copy(color = YorkshireGreen, fontWeight = FontWeight.Bold)
                    )
                }
                Icon(Icons.Default.Stars, contentDescription = null, size = 36.dp, tint = YorkshireGold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Benefits include: Free Delivery, Double Reward Points, Priority Ordering, and Exclusive Previews of Limited Editions. Only £12.00/month.",
                style = MaterialTheme.typography.bodyMedium.copy(color = RoastBrown)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPremium) RoastBrown else YorkshireGreen
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("premium_toggle_button")
            ) {
                Text(
                    text = if (isPremium) "Cancel Membership (Premium Active)" else "Join Yorkshire Club (£12.00/mo)",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RewardRedemptionsList(points: Int, onRedeem: (Int) -> Unit) {
    val rewards = listOf(
        LoyaltyReward("Free Gravy Splash", "Get an extra pot of our piping hot Traditional Gravy.", 50, Icons.Default.Restaurant),
        LoyaltyReward("Free Soft Drink", "Any canned soft drink or mineral water.", 100, Icons.Default.LocalDrink),
        LoyaltyReward("Free Roast Potato Side", "A side of our classic golden crispy roast potatoes.", 150, Icons.Default.Restaurant),
        LoyaltyReward("Free Yorkshire Burrito", "Any regular beef, chicken, or cauli-cheese pudding wrap.", 500, Icons.Default.RestaurantMenu)
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        rewards.forEach { reward ->
            val canRedeem = points >= reward.pointsNeeded
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(YorkshireGold.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(reward.icon, contentDescription = null, size = 20.dp, tint = YorkshireGold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = reward.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                        )
                        Text(
                            text = reward.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onRedeem(reward.pointsNeeded) },
                        enabled = canRedeem,
                        colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("${reward.pointsNeeded} pts", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

data class LoyaltyReward(
    val name: String,
    val description: String,
    val pointsNeeded: Int,
    val icon: ImageVector
)

// ==========================================
// 5. MENU SCREEN
// ==========================================
@Composable
fun MenuScreenContent(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Hero Photo Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(YorkshireGreen, GravyBronze)
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column {
                Text(
                    text = "Interactive Yorkshire Menu",
                    color = YorkshireGold,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Discover ingredients, calories, and customer favorites.",
                    color = CreamWhite,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section 1: Customer Favorites (Wraps)
        Text(
            text = "Pudding Wrap Favorites",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val wraps = listOf(
            MenuProduct("Slow Braised Beef Burrito", "12-hour brisket, roast potatoes, sage & onion stuffing, greens, traditional rich gravy.", 9.50, 840, "Gluten, Milk, Celery", 0, "Dunked with Extra Horseradish"),
            MenuProduct("Roast Chicken Burrito", "Pulled tender chicken breast, roasties, stuffing, carrots, peppercorn gravy.", 8.50, 760, "Gluten, Eggs, Milk", 0, "Served with Cider Apples"),
            MenuProduct("Cauliflower Cheese Burrito", "Creamy three-cheese baked cauliflower superstar, greens, parsnips, savory onion gravy.", 8.00, 690, "Gluten, Milk, Mustard", 0, "Perfect with Yorkshire Chilli Sauce"),
            MenuProduct("Pork & Cracklin' Burrito", "Crispy roasted pork shoulder, pork stuffing, roasties, red cabbage, traditional gravy.", 9.00, 890, "Gluten, Milk", 0, "Awesome with Mustard"),
            MenuProduct("Vegan Butternut Roast", "Herb roasted butternut squash, sage stuffing, parsnips, red cabbage, vegan onion gravy.", 8.00, 580, "Gluten, Celery", 0, "Cranberry drizzle pairing")
        )

        wraps.forEach { wrap ->
            ProductMenuItemRow(wrap) {
                // Clicking customizes this filling
                viewModel.selectedFilling = if (wrap.name.contains("Beef")) "🥩 Slow Braised Beef"
                                            else if (wrap.name.contains("Chicken")) "🍗 Roast Chicken"
                                            else if (wrap.name.contains("Cauliflower")) "🧀 Cauliflower Cheese"
                                            else if (wrap.name.contains("Pork")) "🐖 Pork"
                                            else "🌱 Vegan Roast"
                viewModel.navigateTo(AppScreen.ORDER)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: Meal Bundles
        Text(
            text = "Meal Bundles",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        val bundles = listOf(
            MealBundleItem("Classic Roast Meal", "Beef or Chicken Burrito, crispy roasties side, Yorkshire pudding bites, and a soft drink.", 13.50, listOf("Beef Burrito", "Roasties", "Pudding Bites", "Soft Drink")),
            MealBundleItem("Family Feast", "Four burritos of your choice, two large roasties sides, four desserts, and four canned soft drinks.", 38.00, listOf("4 Burritos", "2 Large Sides", "4 Desserts", "4 Drinks")),
            MealBundleItem("Office Lunch Platter", "Serves 10. A giant wooden tray of assorted burrito wraps, pigs in blankets, and extra gravy boats.", 85.00, listOf("10 Burritos", "Large Pigs in Blankets", "Gravy Boats"))
        )

        bundles.forEach { bundle ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = bundle.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                        )
                        Text(
                            text = "£${String.format("%.2f", bundle.price)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
                        )
                    }
                    Text(
                        text = bundle.description,
                        style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.addBundleToCart(bundle.name, bundle.price, bundle.items) },
                        colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("add_bundle_${bundle.name.replace(" ", "_").lowercase()}")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Bundle to Pud", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProductMenuItemRow(wrap: MenuProduct, onClickCustomize: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = wrap.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                    )
                    Text(
                        text = "£${String.format("%.2f", wrap.price)} • ${wrap.calories} kcal",
                        style = MaterialTheme.typography.bodyMedium.copy(color = YorkshireGreen, fontWeight = FontWeight.Bold)
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = RoastBrown
                )
            }
            
            Text(
                text = wrap.description,
                style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Allergens: ${wrap.allergens}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Red.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Spice Level: ${if (wrap.spiceLevel > 0) "🌶️ Yorkshire Kicker" else "Mild & Savory"}",
                    style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                )
                Text(
                    text = "Recommended drink: ${wrap.drinkPairing}",
                    style = MaterialTheme.typography.bodySmall.copy(color = YorkshireGreen, fontWeight = FontWeight.Bold)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = onClickCustomize,
                    colors = ButtonDefaults.buttonColors(containerColor = YorkshireGold),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(38.dp)
                ) {
                    Text("Load Filling in Customizer 🌯", fontWeight = FontWeight.Bold, color = GravyBronze)
                }
            }
        }
    }
}

data class MenuProduct(
    val name: String,
    val description: String,
    val price: Double,
    val calories: Int,
    val allergens: String,
    val spiceLevel: Int,
    val drinkPairing: String
)

data class MealBundleItem(
    val name: String,
    val description: String,
    val price: Double,
    val items: List<String>
)

// ==========================================
// 6. ACCOUNT & DETAILED SUB-SCREENS
// ==========================================
@Composable
fun AccountScreenContent(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier,
    onNavigateToSub: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val loyaltyProfile by viewModel.loyaltyProfile.collectAsState()
    val isPremium = loyaltyProfile?.isPremiumMember ?: false

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // User Profile Header Block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(YorkshireGreen, GravyBronze)
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(YorkshireGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, size = 32.dp, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Arthur, King of Roasties",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isPremium) "👑 Premium Yorkshire Club Member" else "Loyal Roast Fan",
                        color = YorkshireGold,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account navigation list
        Text(
            text = "Eee, explore our features!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        AccountOptionRow("Burrito Assistant (Gemini AI)", "Ask recipes, catering questions, or order recommendations.", Icons.Default.Android, "assistant", onNavigateToSub)
        AccountOptionRow("Yorkshire Passport (Badges)", "Earn badges like 'Sunday Legend' and collect achievements.", Icons.Default.WorkspacePremium, "passport", onNavigateToSub)
        AccountOptionRow("Catering & Office Portal", "Get quick quotes for corporate, weddings, and festivals.", Icons.Default.People, "catering", onNavigateToSub)
        AccountOptionRow("Merchandise Store", "Order T-Shirts, aprons, mugs, and regional cookbooks.", Icons.Default.ShoppingBag, "merch", onNavigateToSub)
        AccountOptionRow("Digital Gift Cards", "Buy £10, £25, £50, or £100 gift cards for your pals.", Icons.Default.CardGiftcard, "giftcards", onNavigateToSub)
        AccountOptionRow("Community & Comp", "Check customer food photos and write restaurant reviews.", Icons.Default.RateReview, "community", onNavigateToSub)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AccountOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    routeKey: String,
    onNavigateToSub: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onNavigateToSub(routeKey) }
            .testTag("account_option_$routeKey")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(YorkshireGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, size = 20.dp, tint = YorkshireGreen)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                )
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = RoastBrown)
        }
    }
}

// ==========================================
// SUB-SCREENS (PASSPORT, CATERING, AI CHAT, ETC)
// ==========================================

@Composable
fun AiAssistantPanel(viewModel: BurritoViewModel, onBack: () -> Unit) {
    val chatHistory by viewModel.aiChat.collectAsState()
    var userMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    // Scroll to latest message automatically
    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            scrollState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Back toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GravyBronze)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("AI Burrito Assistant", color = YorkshireGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Your friendly British roast expert", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { viewModel.clearAiChat() }) {
                Icon(Icons.Default.Delete, contentDescription = "Clear", tint = Color.White)
            }
        }

        // Chat Bubble List
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatHistory) { message ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (message.isUser) 16.dp else 4.dp,
                            bottomEnd = if (message.isUser) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (message.isUser) YorkshireGreen else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = message.text,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (message.isUser) Color.White else GravyBronze
                                )
                            )
                        }
                    }
                }
            }

            if (viewModel.aiLoading) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = YorkshireGold)
                        Text("Pouring extra gravy...", style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown))
                    }
                }
            }
        }

        // Send Textfield
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = userMessage,
                onValueChange = { userMessage = it },
                placeholder = { Text("e.g., recommend something porky!") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_assistant_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(12.dp)
            )
            IconButton(
                onClick = {
                    if (userMessage.isNotBlank()) {
                        viewModel.sendAiMessage(userMessage)
                        userMessage = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(YorkshireGreen, CircleShape)
                    .testTag("ai_send_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
fun YorkshirePassportPanel(viewModel: BurritoViewModel, onBack: () -> Unit) {
    val badges by viewModel.passportBadges.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GravyBronze)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Your Yorkshire Passport", color = YorkshireGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Collect achievements & explore the capital's roasties", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = YorkshireGold.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, YorkshireGold),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Unlocking the Legend",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                        )
                        Text(
                            text = "Every badge you unlock adds +100 bonus loyalty points to your profile! Eat great burritos, visit stores, and rule the gravy.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = RoastBrown)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Badges Unlocked: ${badges.count { it.isUnlocked }} / ${badges.size}",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
                        )
                    }
                }
            }

            items(badges) { badge ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (badge.isUnlocked) YorkshireGreen.copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (badge.isUnlocked) YorkshireGreen else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (badge.isUnlocked) YorkshireGold else MaterialTheme.colorScheme.surfaceVariant
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (badge.isUnlocked) Icons.Default.WorkspacePremium else Icons.Default.Lock,
                                contentDescription = null,
                                size = 24.dp,
                                tint = if (badge.isUnlocked) Color.White else RoastBrown.copy(alpha = 0.4f)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = badge.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                            )
                            Text(
                                text = badge.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                            )
                            if (badge.isUnlocked) {
                                Text(
                                    text = "Unlocked! +100 bonus pts earned",
                                    style = MaterialTheme.typography.bodySmall.copy(color = YorkshireGreen, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CateringPortalPanel(viewModel: BurritoViewModel, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var guests by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("Office Lunch") }
    var date by remember { mutableStateOf("2026-08-10") }
    
    val cateringHistory by viewModel.cateringRequests.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GravyBronze)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Catering & Corporate Portal", color = YorkshireGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Office lunches, weddings, & events quote engine", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Request a Fast Quotation",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
            )
            Text(
                text = "We provide full hot roasts rolled in giant Yorkshire puddings for private parties, festivals, and corporate feeds across the UK.",
                style = MaterialTheme.typography.bodyMedium.copy(color = RoastBrown)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth().testTag("catering_name_input")
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Your Email") },
                modifier = Modifier.fillMaxWidth().testTag("catering_email_input")
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = guests,
                onValueChange = { guests = it },
                label = { Text("Estimated Guest Count") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().testTag("catering_guests_input")
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Special Requests / Dietary Needs") },
                modifier = Modifier.fillMaxWidth().height(80.dp)
            )

            Button(
                onClick = {
                    val count = guests.toIntOrNull() ?: 10
                    viewModel.submitCateringQuote(name, email, phone, eventType, count, date, notes)
                    // Reset
                    name = ""
                    email = ""
                    phone = ""
                    guests = ""
                    notes = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("submit_catering_quote_button")
            ) {
                Text("Submit Quotation Request", fontWeight = FontWeight.Bold)
            }

            if (cateringHistory.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Past Catering Inquiries",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                )

                cateringHistory.forEach { req ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(req.eventType, fontWeight = FontWeight.Bold, color = GravyBronze)
                                Text(req.status, color = YorkshireGold, fontWeight = FontWeight.Bold)
                            }
                            Text("Guests: ${req.guestCount} • Date: ${req.date}", style = MaterialTheme.typography.bodySmall)
                            if (req.notes.isNotBlank()) {
                                Text("Notes: ${req.notes}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MerchandisePanel(viewModel: BurritoViewModel, onBack: () -> Unit) {
    val merchItems = listOf(
        MerchItem("Yorkshire Burrito Tee", "Authentic heavy cotton t-shirt with our golden pudding emblem.", 22.00),
        MerchItem("Roastmaster Apron", "Heavy-duty canvas roasting apron with deep copper pockets.", 25.00),
        MerchItem("Traditional Gravy Mug", "Pint-sized ceramic mug for proper tea or warm sipping gravy.", 12.00),
        MerchItem("Yorkshire Chilli Bottle", "Our famous hot kick sauce. Thick, smoky, and absolutely mental.", 6.00),
        MerchItem("Street Food Cookbook", "Learn how to wrap Sunday roasts in giant Yorkshire puddings at home.", 18.00)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GravyBronze)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Official Merchandise", color = YorkshireGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Order aprons, t-shirts, and cookbooks", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(merchItems) { merch ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = merch.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                            )
                            Text(
                                text = merch.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown)
                            )
                            Text(
                                text = "£${String.format("%.2f", merch.price)}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
                            )
                        }
                        Button(
                            onClick = { viewModel.addMerchToCart(merch.name, merch.price) },
                            colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Add", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
    }
}

data class MerchItem(val name: String, val description: String, val price: Double)

@Composable
fun GiftCardPanel(viewModel: BurritoViewModel, onBack: () -> Unit) {
    var amount by remember { mutableStateOf(25) }
    var email by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GravyBronze)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Digital Gift Cards", color = YorkshireGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Gift some proper golden roasties", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Buy Digital Gift Voucher",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
            )

            // Select voucher amount card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = YorkshireGold.copy(alpha = 0.08f)),
                border = BorderStroke(1.5.dp, YorkshireGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Selected Card Value", style = MaterialTheme.typography.bodySmall.copy(color = GravyBronze))
                    Text(
                        text = "£$amount",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 56.sp, fontWeight = FontWeight.Bold, color = GravyBronze)
                    )
                }
            }

            // Segmented amount buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10, 25, 50, 100).forEach { amt ->
                    Button(
                        onClick = { amount = amt },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (amount == amt) YorkshireGreen else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (amount == amt) Color.White else RoastBrown
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("£$amt", fontWeight = FontWeight.Bold)
                    }
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Recipient's Email Address") },
                modifier = Modifier.fillMaxWidth().testTag("giftcard_email_input")
            )

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        viewModel.addMerchToCart("Digital £$amount Gift Card", amount.toDouble())
                        email = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().testTag("giftcard_purchase_button")
            ) {
                Text("Add Gift Card to Pud", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CommunityReviewsPanel(viewModel: BurritoViewModel, onBack: () -> Unit) {
    val reviews by viewModel.communityReviews.collectAsState()
    var reviewerName by remember { mutableStateOf("") }
    var reviewComment by remember { mutableStateOf("") }
    var reviewerRating by remember { mutableStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GravyBronze)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text("Yorkshire Community", color = YorkshireGold, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Review combinations & monthly competitions", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Write a Proper Review",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                        )
                        OutlinedTextField(
                            value = reviewerName,
                            onValueChange = { reviewerName = it },
                            label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth().testTag("review_author_input")
                        )
                        OutlinedTextField(
                            value = reviewComment,
                            onValueChange = { reviewComment = it },
                            label = { Text("How was your roast pudding wrap?") },
                            modifier = Modifier.fillMaxWidth().height(80.dp).testTag("review_comment_input")
                        )
                        
                        // Simple rating row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Rating:", color = RoastBrown, fontWeight = FontWeight.Bold)
                            for (stars in 1..5) {
                                IconButton(onClick = { reviewerRating = stars }, modifier = Modifier.size(36.dp)) {
                                    Icon(
                                        imageVector = if (stars <= reviewerRating) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "$stars stars",
                                        tint = YorkshireGold
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (reviewerName.isNotBlank() && reviewComment.isNotBlank()) {
                                    viewModel.addCommunityReview(reviewerName, reviewComment, reviewerRating)
                                    reviewerName = ""
                                    reviewComment = ""
                                    reviewerRating = 5
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("submit_review_button")
                        ) {
                            Text("Post Review", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            items(reviews) { r ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(r.author, fontWeight = FontWeight.Bold, color = GravyBronze)
                            Row {
                                for (i in 1..r.rating) {
                                    Icon(Icons.Default.Star, contentDescription = null, size = 16.dp, tint = YorkshireGold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(r.comment, style = MaterialTheme.typography.bodyMedium.copy(color = RoastBrown))
                    }
                }
            }
        }
    }
}
