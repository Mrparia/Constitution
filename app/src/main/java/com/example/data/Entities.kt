package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "bookmarks")
data class Bookmark(
    @PrimaryKey val id: String, // format: "article_1" or "amendment_1" or "case_1"
    val type: String, // "article", "amendment", "case"
    val title: String,
    val subtitle: String,
    val bookmarkedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_scores")
data class QuizScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "Preamble", "Fundamental Rights", "Directive Principles", etc.
    val score: Int,
    val totalQuestions: Int,
    val percentage: Float,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "translations")
data class TranslationCache(
    @PrimaryKey val cacheKey: String, // format: "${itemId}_${languageCode}"
    val originalText: String,
    val translatedText: String,
    val language: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface ConstitutionDao {
    // Bookmarks
    @Query("SELECT * FROM bookmarks ORDER BY bookmarkedAt DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE id = :id LIMIT 1)")
    suspend fun isBookmarked(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    // Quiz Scores
    @Query("SELECT * FROM quiz_scores ORDER BY timestamp DESC")
    fun getAllQuizScores(): Flow<List<QuizScore>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizScore(score: QuizScore)

    // Translation Cache
    @Query("SELECT * FROM translations WHERE cacheKey = :cacheKey LIMIT 1")
    suspend fun getTranslation(cacheKey: String): TranslationCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslation(translation: TranslationCache)
}
