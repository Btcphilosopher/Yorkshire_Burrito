package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.*
import com.example.data.repository.BurritoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Screen enum representing core navigation
enum class AppScreen {
    HOME,
    ORDER,
    LOCATIONS,
    REWARDS,
    MENU,
    ACCOUNT
}

// Order status states for the Live Kitchen
enum class LiveOrderStatus {
    NONE,
    RECEIVED,
    PREPARING_POTATOES,
    ROASTING_MEAT,
    WRAPPING,
    ADDING_GRAVY,
    READY,
    COLLECTED
}

// Simple chat message for AI Assistant
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class BurritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BurritoRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BurritoRepository(database)
        
        // Populate standard initial community reviews if none exist
        viewModelScope.launch {
            repository.allReviews.firstOrNull()?.let { reviews ->
                if (reviews.isEmpty()) {
                    repository.submitReview(CommunityReviewEntity(author = "George S. from Leeds", comment = "Proper Sunday roast wrapped in a fluffy pudding! The beef is melt-in-the-mouth. Best street food in London!", rating = 5))
                    repository.submitReview(CommunityReviewEntity(author = "Emma W. from Camden", comment = "Cauliflower cheese burrito is life! Paired with the hot Yorkshire Chilli sauce - unbelievable combo.", rating = 5))
                    repository.submitReview(CommunityReviewEntity(author = "Chef Arthur", comment = "Roast chicken burrito is juicy, but those crispy roasties stole the show. Lathered in peppercorn gravy, marvelous.", rating = 4))
                }
            }
        }
    }

    // --- Flows from Database ---
    val cartItems: StateFlow<List<CartItemEntity>> = repository.allCartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteBurritos: StateFlow<List<FavoriteBurritoEntity>> = repository.favoriteBurritos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val passportBadges: StateFlow<List<PassportBadgeEntity>> = repository.passportBadges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val loyaltyProfile: StateFlow<LoyaltyProfileEntity?> = repository.loyaltyProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val cateringRequests: StateFlow<List<CateringRequestEntity>> = repository.allCateringRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val communityReviews: StateFlow<List<CommunityReviewEntity>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- UI/UX States ---
    var currentScreen by mutableStateOf(AppScreen.HOME)
        private set

    fun navigateTo(screen: AppScreen) {
        currentScreen = screen
    }

    // --- Customizer Selected Options (Step 1-6) ---
    var selectedFilling by mutableStateOf("🥩 Slow Braised Beef")
    var selectedPotatoes by mutableStateOf("Classic Roasties")
    var selectedStuffing by mutableStateOf("Sage & Onion")
    var selectedVegetables by mutableStateOf(setOf("Greens", "Carrots"))
    var selectedGravy by mutableStateOf("Traditional")
    var selectedExtras by mutableStateOf(setOf("Yorkshire Pigs in Blankets"))
    
    var customBurritoPrice by mutableStateOf(9.50)
        private set

    fun toggleVegetable(veg: String) {
        selectedVegetables = if (selectedVegetables.contains(veg)) {
            selectedVegetables - veg
        } else {
            selectedVegetables + veg
        }
        recalculatePrice()
    }

    fun toggleExtra(extra: String) {
        selectedExtras = if (selectedExtras.contains(extra)) {
            selectedExtras - extra
        } else {
            selectedExtras + extra
        }
        recalculatePrice()
    }

    fun resetCustomizer() {
        selectedFilling = "🥩 Slow Braised Beef"
        selectedPotatoes = "Classic Roasties"
        selectedStuffing = "Sage & Onion"
        selectedVegetables = setOf("Greens", "Carrots")
        selectedGravy = "Traditional"
        selectedExtras = setOf("Yorkshire Pigs in Blankets")
        recalculatePrice()
    }

    private fun recalculatePrice() {
        var basePrice = 8.50
        // Custom filling additions
        if (selectedFilling.contains("Beef") || selectedFilling.contains("Turkey")) {
            basePrice += 1.00
        }
        // Double portion potatoes
        if (selectedPotatoes == "Double Portion") {
            basePrice += 1.50
        }
        // Extra stuffing
        if (selectedStuffing == "Extra Stuffing") {
            basePrice += 0.75
        }
        // Extras
        selectedExtras.forEach { _ ->
            basePrice += 1.25
        }
        customBurritoPrice = basePrice
    }

    // --- Cart Actions ---
    fun addCustomBurritoToCart(customName: String = "Custom Roast Wrap") {
        viewModelScope.launch {
            val item = CartItemEntity(
                name = customName,
                filling = selectedFilling,
                potatoes = selectedPotatoes,
                stuffing = selectedStuffing,
                vegetablesJson = selectedVegetables.joinToString(","),
                gravy = selectedGravy,
                extrasJson = selectedExtras.joinToString(","),
                price = customBurritoPrice,
                quantity = 1,
                isBundle = false
            )
            repository.addToCart(item)
            // Reset after adding
            resetCustomizer()
            // Navigate to Menu or Cart (we'll stay on Order, show a toast or message)
        }
    }

    fun addBundleToCart(bundleName: String, price: Double, items: List<String>) {
        viewModelScope.launch {
            val item = CartItemEntity(
                name = bundleName,
                price = price,
                quantity = 1,
                isBundle = true,
                bundleItemsJson = items.joinToString(",")
            )
            repository.addToCart(item)
        }
    }

    fun addMerchToCart(merchName: String, price: Double) {
        viewModelScope.launch {
            val item = CartItemEntity(
                name = merchName,
                price = price,
                quantity = 1,
                isBundle = false
            )
            repository.addToCart(item)
        }
    }

    fun updateCartQuantity(item: CartItemEntity, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(item, quantity)
        }
    }

    fun removeFromCart(item: CartItemEntity) {
        viewModelScope.launch {
            repository.removeFromCart(item)
        }
    }

    // --- Favorites Actions ---
    fun saveCustomBurritoAsFavorite(customName: String) {
        viewModelScope.launch {
            val fav = FavoriteBurritoEntity(
                customName = customName,
                filling = selectedFilling,
                potatoes = selectedPotatoes,
                stuffing = selectedStuffing,
                vegetablesJson = selectedVegetables.joinToString(","),
                gravy = selectedGravy,
                extrasJson = selectedExtras.joinToString(","),
                price = customBurritoPrice
            )
            repository.saveFavorite(fav)
        }
    }

    fun deleteFavorite(fav: FavoriteBurritoEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(fav)
        }
    }

    // --- Click & Collect & Order Flow ---
    var selectedLocation by mutableStateOf("Camden Market")
    var selectedOrderType by mutableStateOf("Collection") // Collection, Delivery, Scheduled
    var liveKitchenStatus by mutableStateOf(LiveOrderStatus.NONE)
    var liveKitchenTimeRemaining by mutableStateOf(0)

    fun placeOrder(paymentMethod: String) {
        viewModelScope.launch {
            val currentCart = cartItems.value
            if (currentCart.isEmpty()) return@launch

            // Calculate total spent
            val totalSpent = currentCart.sumOf { it.price * it.quantity }
            // Earn 10 points per £1 spent
            val pointsEarned = (totalSpent * 10).toInt()

            // Deduct points if paying with loyalty points
            if (paymentMethod == "Loyalty Points") {
                val currentProfile = loyaltyProfile.value
                val pointsNeeded = (totalSpent * 100).toInt() // e.g. 100 points per £1
                if (currentProfile != null && currentProfile.points >= pointsNeeded) {
                    repository.deductLoyaltyPoints(pointsNeeded)
                } else {
                    // Fail gracefully
                    return@launch
                }
            } else {
                // Earn points
                repository.addLoyaltyPoints(pointsEarned)
            }

            // If user bought a special filling or xl, unlock custom badges!
            currentCart.forEach { item ->
                if (item.filling.contains("Turkey")) {
                    repository.unlockBadge("christmas_special")
                }
                if (item.name.contains("XL") || item.name.contains("Feast")) {
                    repository.unlockBadge("sunday_legend")
                }
            }

            // Trigger Sunday badge if today is Sunday
            val currentDay = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK)
            if (currentDay == java.util.Calendar.SUNDAY) {
                repository.unlockBadge("sunday_legend")
            }

            // Clear Cart and start Live Kitchen simulation
            repository.clearCart()
            simulateLiveKitchen()
        }
    }

    private fun simulateLiveKitchen() {
        viewModelScope.launch {
            liveKitchenStatus = LiveOrderStatus.RECEIVED
            liveKitchenTimeRemaining = 12 // 12 minutes countdown

            delay(3000)
            liveKitchenStatus = LiveOrderStatus.PREPARING_POTATOES
            liveKitchenTimeRemaining = 10

            delay(3000)
            liveKitchenStatus = LiveOrderStatus.ROASTING_MEAT
            liveKitchenTimeRemaining = 7

            delay(3000)
            liveKitchenStatus = LiveOrderStatus.WRAPPING
            liveKitchenTimeRemaining = 5

            delay(3000)
            liveKitchenStatus = LiveOrderStatus.ADDING_GRAVY
            liveKitchenTimeRemaining = 3

            delay(3000)
            liveKitchenStatus = LiveOrderStatus.READY
            liveKitchenTimeRemaining = 0

            delay(4000)
            liveKitchenStatus = LiveOrderStatus.COLLECTED
            repository.unlockBadge("first_burrito")
        }
    }

    fun dismissLiveKitchen() {
        liveKitchenStatus = LiveOrderStatus.NONE
    }

    // --- AI Assistant Flow ---
    private val _aiChat = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("Eee, welcome to Yorkshire Burrito! Ask me anything about our giant pudding wraps, locations, catering, or rewards. I'm proper ready to help!", isUser = false)
        )
    )
    val aiChat: StateFlow<List<ChatMessage>> = _aiChat.asStateFlow()
    
    var aiLoading by mutableStateOf(false)
        private set

    fun sendAiMessage(message: String) {
        if (message.isBlank()) return
        
        viewModelScope.launch {
            // Add user message
            val currentList = _aiChat.value.toMutableList()
            currentList.add(ChatMessage(message, isUser = true))
            _aiChat.value = currentList
            
            aiLoading = true
            
            // Call Gemini
            val reply = repository.askAssistant(message)
            
            aiLoading = false
            
            // Add AI response
            val updatedList = _aiChat.value.toMutableList()
            updatedList.add(ChatMessage(reply, isUser = false))
            _aiChat.value = updatedList
        }
    }

    fun clearAiChat() {
        _aiChat.value = listOf(
            ChatMessage("Eee, let's start fresh! Ask me anything about our legendary Yorkshire Burritos.", isUser = false)
        )
    }

    // --- Premium Membership subscription ---
    fun togglePremiumSubscription() {
        viewModelScope.launch {
            val currentProfile = loyaltyProfile.value
            val isPremium = currentProfile?.isPremiumMember == true
            repository.setPremiumSubscription(!isPremium)
        }
    }

    // --- Catering Request Submission ---
    fun submitCateringQuote(name: String, email: String, phone: String, eventType: String, guests: Int, date: String, notes: String) {
        viewModelScope.launch {
            val req = CateringRequestEntity(
                name = name,
                email = email,
                phone = phone,
                eventType = eventType,
                guestCount = guests,
                date = date,
                notes = notes
            )
            repository.submitCateringRequest(req)
        }
    }

    // --- Community reviews ---
    fun addCommunityReview(author: String, comment: String, rating: Int) {
        viewModelScope.launch {
            val review = CommunityReviewEntity(
                author = author,
                comment = comment,
                rating = rating
            )
            repository.submitReview(review)
        }
    }
}

// ViewModel Factory to assist with AndroidViewModel creation
class BurritoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BurritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BurritoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
