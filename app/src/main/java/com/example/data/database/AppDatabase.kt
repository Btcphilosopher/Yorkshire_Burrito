package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        CartItemEntity::class,
        FavoriteBurritoEntity::class,
        PassportBadgeEntity::class,
        LoyaltyProfileEntity::class,
        CateringRequestEntity::class,
        CommunityReviewEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartItemDao(): CartItemDao
    abstract fun favoriteBurritoDao(): FavoriteBurritoDao
    abstract fun passportBadgeDao(): PassportBadgeDao
    abstract fun loyaltyProfileDao(): LoyaltyProfileDao
    abstract fun cateringRequestDao(): CateringRequestDao
    abstract fun communityReviewDao(): CommunityReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yorkshire_burrito_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate default badges when database is created
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                database.passportBadgeDao().insertBadges(
                                    listOf(
                                        PassportBadgeEntity("first_burrito", "First Roast Wrap", "Enjoyed your first Yorkie wrap!", "ic_first_burrito"),
                                        PassportBadgeEntity("ten_burritos", "Pudding Royalty", "Ordered 10 delicious burritos", "ic_ten_burritos"),
                                        PassportBadgeEntity("every_filling", "Filling Collector", "Tried beef, chicken, cauli cheese, pork, turkey, & vegan!", "ic_every_filling"),
                                        PassportBadgeEntity("christmas_special", "Festive Feaster", "Tasted our famous Christmas Special wrap", "ic_christmas_special"),
                                        PassportBadgeEntity("sunday_legend", "Sunday Roast Devotee", "Ordered an XL Roast on a Sunday!", "ic_sunday_legend"),
                                        PassportBadgeEntity("hundred_burritos", "Grailst of Gravy", "Ordered 100 Yorkshire Burritos!", "ic_hundred_burritos"),
                                        PassportBadgeEntity("visit_all_stores", "London Explorer", "Visited and checked in at all locations", "ic_visit_all_stores")
                                    )
                                )
                                database.loyaltyProfileDao().insertOrUpdateProfile(
                                    LoyaltyProfileEntity(id = 1, points = 0, isPremiumMember = false)
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
