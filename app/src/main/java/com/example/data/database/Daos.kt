package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface FavoriteBurritoDao {
    @Query("SELECT * FROM favorite_burritos")
    fun getFavorites(): Flow<List<FavoriteBurritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(fav: FavoriteBurritoEntity)

    @Delete
    suspend fun deleteFavorite(fav: FavoriteBurritoEntity)
}

@Dao
interface PassportBadgeDao {
    @Query("SELECT * FROM passport_badges")
    fun getAllBadges(): Flow<List<PassportBadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadges(badges: List<PassportBadgeEntity>)

    @Query("UPDATE passport_badges SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :badgeId")
    suspend fun unlockBadge(badgeId: String, timestamp: Long)
}

@Dao
interface LoyaltyProfileDao {
    @Query("SELECT * FROM loyalty_profile WHERE id = 1")
    fun getProfile(): Flow<LoyaltyProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: LoyaltyProfileEntity)
}

@Dao
interface CateringRequestDao {
    @Query("SELECT * FROM catering_requests ORDER BY timestamp DESC")
    fun getAllCateringRequests(): Flow<List<CateringRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: CateringRequestEntity)
}

@Dao
interface CommunityReviewDao {
    @Query("SELECT * FROM community_reviews ORDER BY timestamp DESC")
    fun getAllReviews(): Flow<List<CommunityReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: CommunityReviewEntity)
}
