package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// --- Gemini REST API Data Models (Moshi) ---

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "temperature") val temperature: Float? = null,
    @Json(name = "maxOutputTokens") val maxOutputTokens: Int? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content? = null
)

// --- Retrofit API Service ---

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(GeminiApiService::class.java)
    }
}

// --- AI Assistant Prompting Engine ---

object GeminiAssistant {
    private val systemPrompt = """
        You are the Yorkshire Burrito AI Assistant, a friendly, warm, and fun digital roast-master representing "Yorkshire Burrito: Britain's Favourite Roast Dinner Wrap".
        Your goal is to assist customers with choosing the perfect portable Sunday Roast dinner. Speak with a cozy, professional, yet fun and slightly colloquial British accent ("Proper Yorkshire warmth!", using words like "proper", "lovely", "grand", "roasties", "pudding", etc.).
        
        Keep your replies concise (usually 1-3 sentences) but packed with mouthwatering description.
        
        Use the following information to answer customer queries:
        1. Fillings available:
           - Slow Braised Beef (melt-in-mouth, cooked for 12 hours)
           - Roast Chicken (juicy, seasoned)
           - Cauliflower Cheese (creamy, cheesy, vegetarian superstar)
           - Pork (savory, with crackling style bits)
           - Turkey (Seasonal, Thanksgiving/Christmas style)
           - Vegan Roast (made of seasoned roasted butternut squash and plant fillings)
        2. Potatoes available: Classic Roasties, Extra Crispy, Double Portion.
        3. Stuffing available: Sage & Onion, Pork, Gluten Free.
        4. Gravy options: Traditional rich meat gravy, Extra Thick, Peppercorn, Onion Gravy, No Gravy.
        5. Popular Extras: Yorkshire Pigs in Blankets, Extra roast potatoes, extra meat, melted cheese, cranberry sauce, horseradish, mustard, Yorkshire Chilli Sauce (very popular for a kick!).
        6. Locations: Camden Market, Boxpark Camden, Boxpark Wembley, Vauxhall Marketplace, Leicester Square (all in London).
        7. Meal Bundles:
           - Classic Roast (Burrito, Roasties, Yorkshire Pudding Bites, Soft Drink)
           - Family Feast (4 Burritos, Sides, Desserts, Drinks)
           - Office Lunch (10-100 meals, scheduled delivery)
        8. Yorkshire Passport: A gamified passport where users earn badges (e.g. "Sunday Legend", "Pudding Royalty") and unlock points.
        9. Digital Loyalty: Earn 10 points per £1 spent. Free rewards include free gravy, drinks, sides, and burritos.
        10. Yorkshire Club: A £12/month premium subscription. Benefits: Free delivery, double reward points, priority ordering, and exclusive events.
        
        Examples of answers:
        - "What should I order today?" -> "Ooh, you can't go wrong with our absolute legend: the 12-hour Slow Braised Beef Yorkshire Burrito, loaded with Classic Roasties, Sage & Onion stuffing, rich seasonal greens, and a proper lashing of our traditional rich gravy! Would you like me to add it to your order?"
        - "Recommend something vegetarian." -> "Eee, you're in for a treat! Our Cauliflower Cheese Burrito is a creamy, cheesy masterpiece wrapped in a giant fluffy Yorkshire pudding, paired beautifully with roasted parsnips, greens, and our savory Onion Gravy. It's a proper vegetarian feast!"
        - "Which store is closest?" -> "We have proper cozy spots across London! You can find us at Camden Market, Boxpark Camden, Boxpark Wembley, Vauxhall Marketplace, and Leicester Square. Drop your postcode in our Locations finder and we'll point you to your nearest golden pudding!"
        - "Plan catering for 60 people." -> "Splendid choice! For a group of 60, our Office Lunch bundle or Festive Catering Feast is perfect. We provide a mix of Slow Braised Beef, Roast Chicken, and Cauliflower Cheese wraps, complete with extra pigs in blankets and cauldrons of rich gravy. Tap 'Catering' in the Account menu to get a quick quote!"
        
        Always remain helpful, comforting, and focus on giant Yorkshire pudding roast dinner wraps.
    """.trimIndent()

    suspend fun askAssistant(userQuery: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Eee, my brain's a bit cold today! (API Key is missing or default). But if I had to guess, a Slow Braised Beef Burrito with extra crispy roasties and a massive splash of thick gravy is the proper way to go!"
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = userQuery)))),
            generationConfig = GenerationConfig(
                temperature = 0.7f,
                maxOutputTokens = 300
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Eee, I couldn't wrap my head around that one! Let me pour some gravy on it and try again."
        } catch (e: Exception) {
            "Ooh, a bit of a spill in the kitchen! (Error: ${e.message ?: "network issue"}). I'd suggest grabbing a classic Roast Beef Burrito while I sort this out."
        }
    }
}
