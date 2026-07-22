package com.example.data.repository

import com.example.data.api.GeminiAssistant
import com.example.data.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class BurritoRepository(private val database: AppDatabase) {

    // --- DAOs ---
    private val cartItemDao = database.cartItemDao()
    private val favoriteBurritoDao = database.favoriteBurritoDao()
    private val passportBadgeDao = database.passportBadgeDao()
    private val loyaltyProfileDao = database.loyaltyProfileDao()
    private val cateringRequestDao = database.cateringRequestDao()
    private val communityReviewDao = database.communityReviewDao()

    // --- Cart Flows ---
    val allCartItems: Flow<List<CartItemEntity>> = cartItemDao.getAllCartItems()

    suspend fun addToCart(item: CartItemEntity) {
        cartItemDao.insertCartItem(item)
    }

    suspend fun updateCartQuantity(item: CartItemEntity, quantity: Int) {
        if (quantity <= 0) {
            cartItemDao.deleteCartItem(item)
        } else {
            cartItemDao.updateCartItem(item.copy(quantity = quantity))
        }
    }

    suspend fun removeFromCart(item: CartItemEntity) {
        cartItemDao.deleteCartItem(item)
    }

    suspend fun clearCart() {
        cartItemDao.clearCart()
    }

    // --- Favorite Flows ---
    val favoriteBurritos: Flow<List<FavoriteBurritoEntity>> = favoriteBurritoDao.getFavorites()

    suspend fun saveFavorite(fav: FavoriteBurritoEntity) {
        favoriteBurritoDao.insertFavorite(fav)
    }

    suspend fun deleteFavorite(fav: FavoriteBurritoEntity) {
        favoriteBurritoDao.deleteFavorite(fav)
    }

    // --- Gamified Passport Badges ---
    val passportBadges: Flow<List<PassportBadgeEntity>> = passportBadgeDao.getAllBadges()

    suspend fun unlockBadge(badgeId: String) {
        passportBadgeDao.unlockBadge(badgeId, System.currentTimeMillis())
    }

    // --- Loyalty Profile ---
    val loyaltyProfile: Flow<LoyaltyProfileEntity?> = loyaltyProfileDao.getProfile()

    suspend fun addLoyaltyPoints(earnedPoints: Int) {
        val currentProfile = loyaltyProfile.firstOrNull() ?: LoyaltyProfileEntity(id = 1)
        val newPoints = currentProfile.points + earnedPoints
        val newLifetime = currentProfile.lifetimePoints + earnedPoints
        
        loyaltyProfileDao.insertOrUpdateProfile(
            currentProfile.copy(
                points = newPoints,
                lifetimePoints = newLifetime
            )
        )

        // Check gamified badge unlocks based on lifetime points
        if (newLifetime >= 10 && currentProfile.lifetimePoints < 10) {
            unlockBadge("first_burrito")
        }
        if (newLifetime >= 100 && currentProfile.lifetimePoints < 100) {
            unlockBadge("ten_burritos")
        }
        if (newLifetime >= 1000 && currentProfile.lifetimePoints < 1000) {
            unlockBadge("hundred_burritos")
        }
    }

    suspend fun deductLoyaltyPoints(usedPoints: Int) {
        val currentProfile = loyaltyProfile.firstOrNull() ?: LoyaltyProfileEntity(id = 1)
        val newPoints = (currentProfile.points - usedPoints).coerceAtLeast(0)
        loyaltyProfileDao.insertOrUpdateProfile(currentProfile.copy(points = newPoints))
    }

    suspend fun setPremiumSubscription(isPremium: Boolean) {
        val currentProfile = loyaltyProfile.firstOrNull() ?: LoyaltyProfileEntity(id = 1)
        loyaltyProfileDao.insertOrUpdateProfile(
            currentProfile.copy(
                isPremiumMember = isPremium,
                membershipSince = if (isPremium) System.currentTimeMillis() else 0
            )
        )
    }

    // --- Catering requests ---
    val allCateringRequests: Flow<List<CateringRequestEntity>> = cateringRequestDao.getAllCateringRequests()

    suspend fun submitCateringRequest(request: CateringRequestEntity) {
        cateringRequestDao.insertRequest(request)
    }

    // --- Community reviews ---
    val allReviews: Flow<List<CommunityReviewEntity>> = communityReviewDao.getAllReviews()

    suspend fun submitReview(review: CommunityReviewEntity) {
        communityReviewDao.insertReview(review)
    }

    // --- Gemini AI Assistant ---
    suspend fun askAssistant(userQuery: String): String {
        return GeminiAssistant.askAssistant(userQuery)
    }
}
