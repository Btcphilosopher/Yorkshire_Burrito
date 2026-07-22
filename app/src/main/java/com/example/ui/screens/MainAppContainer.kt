package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.BurritoViewModel
import com.example.ui.viewmodel.LiveOrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer(
    viewModel: BurritoViewModel,
    modifier: Modifier = Modifier
) {
    var accountSubScreen by remember { mutableStateOf("none") }
    val cartItems by viewModel.cartItems.collectAsState()
    val loyaltyProfile by viewModel.loyaltyProfile.collectAsState()
    
    // Intercept navigation to clear sub-screens
    val currentTab = viewModel.currentScreen

    LaunchedEffect(currentTab) {
        accountSubScreen = "none"
    }

    Scaffold(
        topBar = {
            if (accountSubScreen == "none" && currentTab != AppScreen.HOME) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = when (currentTab) {
                                    AppScreen.ORDER -> "Order Online"
                                    AppScreen.LOCATIONS -> "Our Stores"
                                    AppScreen.REWARDS -> "Your Rewards"
                                    AppScreen.MENU -> "Our Menu"
                                    AppScreen.ACCOUNT -> "Your Account"
                                    else -> "Yorkshire Burrito"
                                },
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = GravyBronze
                                )
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CreamWhite
                    ),
                    modifier = Modifier.testTag("main_top_app_bar")
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = GravyBronze,
                modifier = Modifier.testTag("main_bottom_nav_bar")
            ) {
                val screens = listOf(
                    NavigationItem("Home", Icons.Default.Home, AppScreen.HOME, "nav_home"),
                    NavigationItem("Order", Icons.Default.Restaurant, AppScreen.ORDER, "nav_order"),
                    NavigationItem("Locations", Icons.Default.LocationOn, AppScreen.LOCATIONS, "nav_locations"),
                    NavigationItem("Rewards", Icons.Default.CardGiftcard, AppScreen.REWARDS, "nav_rewards"),
                    NavigationItem("Menu", Icons.Default.MenuBook, AppScreen.MENU, "nav_menu"),
                    NavigationItem("Account", Icons.Default.Person, AppScreen.ACCOUNT, "nav_account")
                )

                screens.forEach { screen ->
                    val selected = currentTab == screen.screen
                    NavigationBarItem(
                        selected = selected,
                        onClick = { viewModel.navigateTo(screen.screen) },
                        icon = {
                            if (screen.screen == AppScreen.ORDER && cartItems.isNotEmpty()) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = YorkshireGold) {
                                            Text(cartItems.sumOf { it.quantity }.toString(), color = GravyBronze)
                                        }
                                    }
                                ) {
                                    Icon(screen.icon, contentDescription = screen.label, tint = if (selected) YorkshireGreen else GravyBronze.copy(alpha = 0.4f))
                                }
                            } else {
                                Icon(screen.icon, contentDescription = screen.label, tint = if (selected) YorkshireGreen else GravyBronze.copy(alpha = 0.4f))
                            }
                        },
                        label = {
                            Text(
                                text = screen.label,
                                color = if (selected) YorkshireGreen else GravyBronze.copy(alpha = 0.4f),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = YorkshireGreen.copy(alpha = 0.12f),
                            selectedIconColor = YorkshireGreen,
                            unselectedIconColor = GravyBronze.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.testTag(screen.testTag)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Content Switcher
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "MainTabs"
            ) { targetTab ->
                when (targetTab) {
                    AppScreen.HOME -> HomeScreenContent(viewModel) { sub ->
                        accountSubScreen = sub
                        viewModel.navigateTo(AppScreen.ACCOUNT)
                    }
                    AppScreen.ORDER -> OrderScreenContent(viewModel)
                    AppScreen.LOCATIONS -> LocationsScreenContent(viewModel)
                    AppScreen.REWARDS -> RewardsScreenContent(viewModel)
                    AppScreen.MENU -> MenuScreenContent(viewModel)
                    AppScreen.ACCOUNT -> {
                        AnimatedContent(
                            targetState = accountSubScreen,
                            transitionSpec = {
                                slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(targetOffsetX = { -it })
                            },
                            label = "AccountSubScreens"
                        ) { subRoute ->
                            when (subRoute) {
                                "assistant" -> AiAssistantPanel(viewModel) { accountSubScreen = "none" }
                                "passport" -> YorkshirePassportPanel(viewModel) { accountSubScreen = "none" }
                                "catering" -> CateringPortalPanel(viewModel) { accountSubScreen = "none" }
                                "merch" -> MerchandisePanel(viewModel) { accountSubScreen = "none" }
                                "giftcards" -> GiftCardPanel(viewModel) { accountSubScreen = "none" }
                                "community" -> CommunityReviewsPanel(viewModel) { accountSubScreen = "none" }
                                else -> AccountScreenContent(viewModel) { sub ->
                                    accountSubScreen = sub
                                }
                            }
                        }
                    }
                }
            }

            // Real-time Floating Order Progress bar / Kitchen Tracker Dialog
            OrderProgressTrackerFloatingPill(viewModel)
        }
    }
}

@Composable
fun OrderProgressTrackerFloatingPill(viewModel: BurritoViewModel) {
    val status = viewModel.liveKitchenStatus
    val timeRemaining = viewModel.liveKitchenTimeRemaining
    var showDetailsDialog by remember { mutableStateOf(false) }

    if (status != LiveOrderStatus.NONE) {
        // Floating pill on the bottom center of the screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = YorkshireGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable { showDetailsDialog = true }
                    .testTag("kitchen_tracker_pill")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = YorkshireGold,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = when (status) {
                                    LiveOrderStatus.RECEIVED -> "Order Received"
                                    LiveOrderStatus.PREPARING_POTATOES -> "Preparing Crispy Potatoes"
                                    LiveOrderStatus.ROASTING_MEAT -> "Roasting Your Brisket"
                                    LiveOrderStatus.WRAPPING -> "Wrapping Pudding Wrap"
                                    LiveOrderStatus.ADDING_GRAVY -> "Lathering Gravy"
                                    LiveOrderStatus.READY -> "Pudding Wrap Ready!"
                                    else -> "Enjoy Your Roast Wrap!"
                                },
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Click to open Live Roast Kitchen tracker",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
                            )
                        }
                    }
                    if (timeRemaining > 0) {
                        Text(
                            text = "${timeRemaining}m",
                            color = YorkshireGold,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = YorkshireGold)
                    }
                }
            }
        }
    }

    if (showDetailsDialog) {
        AlertDialog(
            onDismissRequest = {
                if (status == LiveOrderStatus.COLLECTED) {
                    viewModel.dismissLiveKitchen()
                }
                showDetailsDialog = false
            },
            title = {
                Text(
                    text = "Live Roast Kitchen",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GravyBronze)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Huge icon showing status
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(YorkshireGold.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (status) {
                                LiveOrderStatus.RECEIVED -> Icons.Default.Receipt
                                LiveOrderStatus.PREPARING_POTATOES -> Icons.Default.Restaurant
                                LiveOrderStatus.ROASTING_MEAT -> Icons.Default.Whatshot
                                LiveOrderStatus.WRAPPING -> Icons.Default.Build
                                LiveOrderStatus.ADDING_GRAVY -> Icons.Default.LocalDrink
                                LiveOrderStatus.READY -> Icons.Default.Star
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            tint = YorkshireGold,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (status) {
                                LiveOrderStatus.RECEIVED -> "Arthur's Roast Order Received"
                                LiveOrderStatus.PREPARING_POTATOES -> "Frying Golden Potatoes"
                                LiveOrderStatus.ROASTING_MEAT -> "Slicing Braised Beef"
                                LiveOrderStatus.WRAPPING -> "Rolling in Giant Pudding Wrap"
                                LiveOrderStatus.ADDING_GRAVY -> "Pouring Premium Gravy Splash"
                                LiveOrderStatus.READY -> "Ready for Collection!"
                                else -> "Order Safely Collected"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GravyBronze),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when (status) {
                                LiveOrderStatus.RECEIVED -> "The carving deck has received your order."
                                LiveOrderStatus.PREPARING_POTATOES -> "We're ensuring those roasties are crisp and fluffy."
                                LiveOrderStatus.ROASTING_MEAT -> "Carving slow braised brisket, succulent chicken, and pork."
                                LiveOrderStatus.WRAPPING -> "Laying ingredients and folding with expert precision."
                                LiveOrderStatus.ADDING_GRAVY -> "Lathering traditional rich beef gravy before enclosing."
                                LiveOrderStatus.READY -> "Head to the counter at ${viewModel.selectedLocation} to grab it!"
                                else -> "Eee, hope it was a proper standard Sunday feast!"
                            },
                            style = MaterialTheme.typography.bodySmall.copy(color = RoastBrown),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Progress Bar
                    val progressValue = when (status) {
                        LiveOrderStatus.RECEIVED -> 0.1f
                        LiveOrderStatus.PREPARING_POTATOES -> 0.3f
                        LiveOrderStatus.ROASTING_MEAT -> 0.5f
                        LiveOrderStatus.WRAPPING -> 0.7f
                        LiveOrderStatus.ADDING_GRAVY -> 0.85f
                        LiveOrderStatus.READY -> 1.0f
                        else -> 1.0f
                    }
                    LinearProgressIndicator(
                        progress = progressValue,
                        color = YorkshireGreen,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    if (timeRemaining > 0) {
                        Text(
                            text = "Estimated Time Remaining: ${timeRemaining} minutes",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = YorkshireGreen)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (status == LiveOrderStatus.COLLECTED) {
                            viewModel.dismissLiveKitchen()
                        }
                        showDetailsDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YorkshireGreen)
                ) {
                    Text(if (status == LiveOrderStatus.COLLECTED) "Great, Thanks!" else "Keep Cooking")
                }
            }
        )
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val screen: AppScreen,
    val testTag: String
)
