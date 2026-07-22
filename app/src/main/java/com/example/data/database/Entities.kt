package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String, // e.g., "Custom Yorkshire Burrito" or "Family Feast"
    val filling: String = "",
    val potatoes: String = "",
    val stuffing: String = "",
    val vegetablesJson: String = "[]", // Serialized list of selected veg
    val gravy: String = "",
    val extrasJson: String = "[]", // Serialized list of extras
    val price: Double,
    val quantity: Int = 1,
    val isBundle: Boolean = false,
    val bundleItemsJson: String = "[]"
)

@Entity(tableName = "favorite_burritos")
data class FavoriteBurritoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customName: String,
    val filling: String,
    val potatoes: String,
    val stuffing: String,
    val vegetablesJson: String,
    val gravy: String,
    val extrasJson: String,
    val price: Double
)

@Entity(tableName = "passport_badges")
data class PassportBadgeEntity(
    @PrimaryKey val id: String, // e.g. "first_burrito", "visit_all"
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long = 0
)

@Entity(tableName = "loyalty_profile")
data class LoyaltyProfileEntity(
    @PrimaryKey val id: Int = 1, // Singleton profile
    val points: Int = 0,
    val isPremiumMember: Boolean = false,
    val membershipSince: Long = 0,
    val lifetimePoints: Int = 0,
    val birthday: String = ""
)

@Entity(tableName = "catering_requests")
data class CateringRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val eventType: String, // "Office Lunch", "Wedding", "Festival", etc.
    val guestCount: Int,
    val date: String,
    val notes: String = "",
    val status: String = "Pending Quote",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "community_reviews")
data class CommunityReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val author: String,
    val comment: String,
    val rating: Int,
    val foodPhotoUrl: String = "", // Placeholders or generated images
    val combinationsJson: String = "", // custom combo used
    val timestamp: Long = System.currentTimeMillis()
)
