package com.example.data

import kotlinx.coroutines.flow.Flow

class ConstitutionRepository(private val dao: ConstitutionDao) {
    val allBookmarks: Flow<List<Bookmark>> = dao.getAllBookmarks()
    val allQuizScores: Flow<List<QuizScore>> = dao.getAllQuizScores()

    suspend fun isBookmarked(id: String): Boolean = dao.isBookmarked(id)

    suspend fun insertBookmark(bookmark: Bookmark) = dao.insertBookmark(bookmark)

    suspend fun deleteBookmarkById(id: String) = dao.deleteBookmarkById(id)

    suspend fun insertQuizScore(score: QuizScore) = dao.insertQuizScore(score)

    suspend fun getTranslation(itemId: String, languageCode: String): TranslationCache? {
        val cacheKey = "${itemId}_${languageCode}"
        return dao.getTranslation(cacheKey)
    }

    suspend fun saveTranslation(itemId: String, languageCode: String, originalText: String, translatedText: String, language: String) {
        val cacheKey = "${itemId}_${languageCode}"
        val cache = TranslationCache(
            cacheKey = cacheKey,
            originalText = originalText,
            translatedText = translatedText,
            language = language
        )
        dao.insertTranslation(cache)
    }
}
