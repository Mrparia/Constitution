package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Bookmark
import com.example.data.ConstitutionRepository
import com.example.data.GeminiTranslationClient
import com.example.data.QuizScore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ConstitutionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ConstitutionRepository
    private val translationClient = GeminiTranslationClient()
    private val sharedPreferences = application.getSharedPreferences("constitution_prefs", android.content.Context.MODE_PRIVATE)

    // Database Flows
    val bookmarks: StateFlow<List<Bookmark>>
    val quizScores: StateFlow<List<QuizScore>>

    // Selected state representing target Indian language
    private val _selectedLanguage = MutableStateFlow(sharedPreferences.getString("selected_lang", "English") ?: "English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Cache state to represent active on-demand translations
    // Map of format "itemId_langCode" to Translated String
    private val _translatedTexts = MutableStateFlow<Map<String, String>>(emptyMap())
    val translatedTexts: StateFlow<Map<String, String>> = _translatedTexts.asStateFlow()

    // Loading status for on-demand translations
    private val _translationLoadingState = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val translationLoadingState: StateFlow<Map<String, Boolean>> = _translationLoadingState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ConstitutionRepository(database.constitutionDao())
        
        bookmarks = repository.allBookmarks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
        quizScores = repository.allQuizScores.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
        sharedPreferences.edit().putString("selected_lang", language).apply()
    }

    // Requests translation, checks Room cache first, falls back to Gemini API, saves result to Room
    fun translateField(itemId: String, originalText: String) {
        val targetLang = _selectedLanguage.value
        if (targetLang == "English") return // No translation needed for English

        val cacheKey = "${itemId}_${targetLang}"
        if (_translatedTexts.value.containsKey(cacheKey)) return // Already in active memory

        viewModelScope.launch {
            _translationLoadingState.value = _translationLoadingState.value + (cacheKey to true)
            
            // Check Room DB Cache
            val cachedTranslation = repository.getTranslation(itemId, targetLang)
            if (cachedTranslation != null) {
                _translatedTexts.value = _translatedTexts.value + (cacheKey to cachedTranslation.translatedText)
            } else {
                // Call Gemini REST API
                val translated = translationClient.translateText(originalText, targetLang)
                // Save to Room Cache
                repository.saveTranslation(itemId, targetLang, originalText, translated, targetLang)
                _translatedTexts.value = _translatedTexts.value + (cacheKey to translated)
            }
            
            _translationLoadingState.value = _translationLoadingState.value + (cacheKey to false)
        }
    }

    fun toggleBookmark(id: String, type: String, title: String, subtitle: String) {
        viewModelScope.launch {
            if (repository.isBookmarked(id)) {
                repository.deleteBookmarkById(id)
            } else {
                repository.insertBookmark(
                    Bookmark(id = id, type = type, title = title, subtitle = subtitle)
                )
            }
        }
    }

    fun saveQuizScore(category: String, score: Int, total: Int) {
        viewModelScope.launch {
            val pct = if (total > 0) (score.toFloat() / total.toFloat() * 100) else 0f
            repository.insertQuizScore(
                QuizScore(category = category, score = score, totalQuestions = total, percentage = pct)
            )
        }
    }
}
