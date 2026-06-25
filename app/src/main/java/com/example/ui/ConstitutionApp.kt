package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.example.data.*
import com.example.ui.viewmodel.ConstitutionViewModel

data class TranslationContext(
    val selectedLanguage: String,
    val translatedTexts: Map<String, String>,
    val translationLoadingState: Map<String, Boolean>,
    val onTranslate: (String, String) -> Unit
)

val LocalTranslationContext = staticCompositionLocalOf<TranslationContext> {
    error("No TranslationContext provided")
}

@Composable
fun TranslateText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontFamily: androidx.compose.ui.text.font.FontFamily? = null
) {
    val context = LocalTranslationContext.current
    val targetLang = selectedLanguageName(context.selectedLanguage)
    
    if (targetLang == "English") {
        Text(
            text = text,
            modifier = modifier,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color,
            textAlign = textAlign,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
            maxLines = maxLines,
            overflow = overflow,
            fontFamily = fontFamily
        )
    } else {
        val itemId = "static_${text.replace(" ", "_").replace("?", "").replace(",", "").replace(".", "").replace("/", "").lowercase()}"
        TranslateContainer(
            itemId = itemId,
            originalText = text,
            targetLanguage = targetLang,
            translatedTexts = context.translatedTexts,
            translationLoadingState = context.translationLoadingState,
            onTranslate = context.onTranslate
        ) { translated ->
            Text(
                text = translated,
                modifier = modifier,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = color,
                textAlign = textAlign,
                lineHeight = lineHeight,
                letterSpacing = letterSpacing,
                maxLines = maxLines,
                overflow = overflow,
                fontFamily = fontFamily
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstitutionApp(viewModel: ConstitutionViewModel) {
    var currentTab by remember { mutableStateOf(0) } // 0: Preamble, 1: Search, 2: Amendments, 3: Case Studies, 4: Quizzes
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val quizScores by viewModel.quizScores.collectAsStateWithLifecycle()
    val translatedTexts by viewModel.translatedTexts.collectAsStateWithLifecycle()
    val translationLoadingState by viewModel.translationLoadingState.collectAsStateWithLifecycle()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPartFilter by remember { mutableStateOf("All Parts") }

    var showSplash by remember { mutableStateOf(true) }

    if (showSplash) {
        FlagHoistingSplashScreen(onAnimationFinished = { showSplash = false })
    } else {
        val translationContext = remember(selectedLanguage, translatedTexts, translationLoadingState) {
            TranslationContext(
                selectedLanguage = selectedLanguage,
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = { key, text -> viewModel.translateField(key, text) }
            )
        }
        CompositionLocalProvider(LocalTranslationContext provides translationContext) {
            Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Beautiful Ashoka Chakra custom logo
                                Canvas(modifier = Modifier.size(32.dp).padding(end = 8.dp)) {
                                    drawCircle(
                                        color = Color(0xFF0F172A),
                                        radius = size.minDimension / 2f,
                                        style = Stroke(width = 2.dp.toPx())
                                    )
                                    drawCircle(
                                        color = Color(0xFF4F46E5),
                                        radius = size.minDimension / 4f,
                                        style = Stroke(width = 1.dp.toPx())
                                    )
                                    for (i in 0 until 24) {
                                        val angle = i * (360f / 24f)
                                        val xRad = Math.toRadians(angle.toDouble())
                                        val start = center
                                        val end = Offset(
                                            x = (center.x + (size.minDimension / 2f) * Math.cos(xRad)).toFloat(),
                                            y = (center.y + (size.minDimension / 2f) * Math.sin(xRad)).toFloat()
                                        )
                                        drawLine(color = Color(0xFF4F46E5), start = start, end = end, strokeWidth = 1.dp.toPx())
                                    }
                                }
                                Column {
                                    TranslateText(
                                        text = "Bharat Sanvidhan",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                    TranslateText(
                                        text = "REPUBLIC OF INDIA",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 9.sp,
                                        color = Color(0xFF4F46E5),
                                        letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                WavingIndianFlag(
                                    modifier = Modifier
                                        .border(0.5.dp, Color(0xFFE2E8F0), RoundedCornerShape(1.dp))
                                )
                            }
                        },
                    actions = {
                        // Language Selector Custom Capsule Button
                        Row(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFEEF2FF))
                                .clickable { showLanguageDialog = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("language_selector_button"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Change Language",
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedLanguage.split(" ").first().uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3730A3)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color(0xFF4F46E5),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 0.dp
                ) {
                    val tabs = listOf(
                        Triple("Preamble", Icons.Default.Description, 0),
                        Triple("Search", Icons.Default.Search, 1),
                        Triple("Amendments", Icons.Default.History, 2),
                        Triple("Cases", Icons.Default.Gavel, 3),
                        Triple("Quiz", Icons.Default.Quiz, 4)
                    )
                    tabs.forEach { (label, icon, tabIdx) ->
                        NavigationBarItem(
                            selected = currentTab == tabIdx,
                            onClick = { currentTab = tabIdx },
                            label = { TranslateText(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            icon = { Icon(icon, contentDescription = label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF4F46E5),
                                selectedTextColor = Color(0xFF4F46E5),
                                unselectedIconColor = Color(0xFF64748B),
                                unselectedTextColor = Color(0xFF64748B),
                                indicatorColor = Color(0xFFEEF2FF)
                            ),
                            modifier = Modifier.testTag("nav_tab_$tabIdx")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F9))
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    slideInVertically(animationSpec = spring()) + fadeIn() togetherWith
                    slideOutVertically() + fadeOut()
                },
                label = "MainScreenTabs"
            ) { targetTab ->
                when (targetTab) {
                    0 -> PreambleScreen(
                        selectedLanguage = selectedLanguage,
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = { key, text -> viewModel.translateField(key, text) },
                        onNavigateToTab = { tab, query, filter ->
                            currentTab = tab
                            if (query != null) searchQuery = query
                            if (filter != null) selectedPartFilter = filter
                        }
                    )
                    1 -> SearchScreen(
                        selectedLanguage = selectedLanguage,
                        bookmarks = bookmarks,
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = { key, text -> viewModel.translateField(key, text) },
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        selectedPartFilter = selectedPartFilter,
                        onSelectedPartFilterChange = { selectedPartFilter = it },
                        onBookmarkToggle = { article ->
                            viewModel.toggleBookmark(
                                id = article.id,
                                type = "article",
                                title = article.number,
                                subtitle = article.title
                            )
                        }
                    )
                    2 -> AmendmentsScreen(
                        selectedLanguage = selectedLanguage,
                        bookmarks = bookmarks,
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = { key, text -> viewModel.translateField(key, text) },
                        onBookmarkToggle = { amend ->
                            viewModel.toggleBookmark(
                                id = amend.id,
                                type = "amendment",
                                title = amend.title,
                                subtitle = amend.actNumber
                            )
                        }
                    )
                    3 -> CasesScreen(
                        selectedLanguage = selectedLanguage,
                        bookmarks = bookmarks,
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = { key, text -> viewModel.translateField(key, text) },
                        onBookmarkToggle = { case ->
                            viewModel.toggleBookmark(
                                id = case.id,
                                type = "case",
                                title = case.title,
                                subtitle = case.citation
                            )
                        }
                    )
                    4 -> QuizzesScreen(
                        selectedLanguage = selectedLanguage,
                        scores = quizScores,
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = { key, text -> viewModel.translateField(key, text) },
                        onQuizCompleted = { cat, score, total -> viewModel.saveQuizScore(cat, score, total) }
                    )
                }
            }
        }
        }

        // Language Chooser Dialog
        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text("Select Language / भाषा चुनें", fontWeight = FontWeight.Bold) },
                text = {
                    LazyColumn(modifier = Modifier.height(280.dp)) {
                        items(ConstitutionData.officialIndianLanguages) { language ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.setLanguage(language)
                                        showLanguageDialog = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = language,
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedLanguage == language) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedLanguage == language) Color(0xFF1E3A8A) else Color(0xFF1E293B)
                                )
                                if (selectedLanguage == language) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color(0xFF1E3A8A)
                                    )
                                }
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLanguageDialog = false }) {
                        Text("Close", color = Color(0xFF64748B))
                    }
                }
            )
        }
        }
    }
}


@Composable
fun PreambleScreen(
    selectedLanguage: String,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onNavigateToTab: (Int, String?, String?) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Item 1: Article of the Day Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            TranslateText(
                                text = "ARTICLE OF THE DAY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF92400E)
                            )
                        }
                        TranslateText(
                            text = "Part III",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TranslateText(
                        text = "Article 21",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TranslateText(
                        text = "No person shall be deprived of his life or personal liberty except according to procedure established by law.",
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = Color(0xFF475569)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onNavigateToTab(1, "Article 21", "All Parts") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        TranslateText(
                            text = "Read Full Article",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Item 2: Grid of Quick Categories
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 1: Amendments
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFEEF2FF),
                        borderColor = Color(0xFFE0E7FF),
                        iconColor = Color(0xFF4F46E5),
                        icon = Icons.Default.History,
                        title = "Historical Amendments",
                        subtitle = "105+ Records",
                        onClick = { onNavigateToTab(2, null, null) }
                    )
                    // Card 2: Landmark Cases
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFECFDF5),
                        borderColor = Color(0xFFD1FAE5),
                        iconColor = Color(0xFF059669),
                        icon = Icons.Default.Gavel,
                        title = "Landmark Cases",
                        subtitle = "Kesavananda & more",
                        onClick = { onNavigateToTab(3, null, null) }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card 3: Learning Quiz
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFFFF1F2),
                        borderColor = Color(0xFFFFE4E6),
                        iconColor = Color(0xFFE11D48),
                        icon = Icons.Default.Quiz,
                        title = "Learning Quiz",
                        subtitle = "Level: Beginner",
                        onClick = { onNavigateToTab(4, null, null) }
                    )
                    // Card 4: Schedules & Parts
                    CategoryCard(
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFF0F9FF),
                        borderColor = Color(0xFFE0F2FE),
                        iconColor = Color(0xFF0284C7),
                        icon = Icons.Default.MenuBook,
                        title = "Schedules & Parts",
                        subtitle = "Detailed Index",
                        onClick = { onNavigateToTab(1, null, "All Parts") }
                    )
                }
            }
        }

        // Item 3: Preamble Container Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                TranslateText(
                    text = "THE PREAMBLE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                TranslateContainer(
                    itemId = "preamble",
                    originalText = originalPreambleText,
                    targetLanguage = selectedLanguageName(selectedLanguage),
                    translatedTexts = translatedTexts,
                    translationLoadingState = translationLoadingState,
                    onTranslate = onTranslate
                ) { displayText ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = displayText,
                                fontSize = 15.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Serif,
                                color = Color(0xFF1E293B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    borderColor: Color,
    iconColor: Color,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(125.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TranslateText(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            TranslateText(
                text = subtitle,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = iconColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

private val originalPreambleText = """
    WE, THE PEOPLE OF INDIA, having solemnly resolved to constitute India into a SOVEREIGN SOCIALIST SECULAR DEMOCRATIC REPUBLIC and to secure to all its citizens:

    JUSTICE, social, economic and political;

    LIBERTY of thought, expression, belief, faith and worship;

    EQUALITY of status and of opportunity;

    and to promote among them all

    FRATERNITY assuring the dignity of the individual and the unity and integrity of the Nation;

    IN OUR CONSTITUENT ASSEMBLY this twenty-sixth day of November, 1949, do HEREBY ADOPT, ENACT AND GIVE TO OURSELVES THIS CONSTITUTION.
""".trimIndent()

@Composable
fun TranslateContainer(
    itemId: String,
    originalText: String,
    targetLanguage: String,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    content: @Composable (String) -> Unit
) {
    if (targetLanguage == "English") {
        content(originalText)
        return
    }

    val cacheKey = "${itemId}_${targetLanguage}"
    val translation = translatedTexts[cacheKey]
    val isLoading = translationLoadingState[cacheKey] ?: false

    LaunchedEffect(targetLanguage) {
        onTranslate(itemId, originalText)
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color(0xFF1E3A8A), modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Translating to $targetLanguage...", fontSize = 12.sp, color = Color(0xFF64748B))
            }
        }
    } else if (translation != null) {
        content(translation)
    } else {
        content(originalText)
    }
}

fun selectedLanguageName(lang: String): String {
    return lang.split(" ").first()
}

@Composable
fun SearchScreen(
    selectedLanguage: String,
    bookmarks: List<Bookmark>,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedPartFilter: String,
    onSelectedPartFilterChange: (String) -> Unit,
    onBookmarkToggle: (Article) -> Unit
) {
    val filteredArticles = remember(searchQuery, selectedPartFilter) {
        ConstitutionData.articles.filter { article ->
            val matchesQuery = article.number.contains(searchQuery, ignoreCase = true) ||
                    article.title.contains(searchQuery, ignoreCase = true) ||
                    article.simpleExplanation.contains(searchQuery, ignoreCase = true)
            val matchesFilter = selectedPartFilter == "All Parts" || article.part.contains(selectedPartFilter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }

    val parts = listOf("All Parts", "Part I", "Part III", "Part IV", "Part IVA")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { TranslateText("Search Articles (e.g. Equality, Article 19)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("article_search_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFF4F46E5),
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            singleLine = true
        )

        // Filters Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            parts.forEach { part ->
                val isSelected = selectedPartFilter == part
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Color(0xFF4F46E5) else Color.White)
                        .border(
                            1.dp,
                            if (isSelected) Color(0xFF4F46E5) else Color(0xFFE2E8F0),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onSelectedPartFilterChange(part) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    TranslateText(
                        text = part,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color(0xFF475569)
                    )
                }
            }
        }

        // Articles List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            if (filteredArticles.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SentimentDissatisfied,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TranslateText(
                            text = "No Articles found matching query",
                            fontSize = 16.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                items(filteredArticles, key = { it.id }) { article ->
                    val isBookmarked = bookmarks.any { it.id == article.id }
                    ArticleCard(
                        article = article,
                        isBookmarked = isBookmarked,
                        selectedLanguage = selectedLanguage,
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = onTranslate,
                        onBookmarkClick = { onBookmarkToggle(article) }
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: Article,
    isBookmarked: Boolean,
    selectedLanguage: String,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onBookmarkClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("article_card_${article.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = article.number,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF4F46E5)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TranslateText(
                        text = article.title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TranslateText(
                        text = article.part,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBookmarkClick,
                        modifier = Modifier.testTag("bookmark_btn_${article.id}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) Color(0xFFF59E0B) else Color(0xFF94A3B8)
                        )
                    }
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand Detail",
                            tint = Color(0xFF0F172A)
                        )
                    }
                }
            }

            // Expanded view with Constitution text & explanation
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(16.dp))

                // Original legal text
                TranslateText(
                    text = "CONSTITUTIONAL TEXT",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    color = Color(0xFF4F46E5),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TranslateContainer(
                    itemId = "${article.id}_raw",
                    originalText = article.rawText,
                    targetLanguage = selectedLanguageName(selectedLanguage),
                    translatedTexts = translatedTexts,
                    translationLoadingState = translationLoadingState,
                    onTranslate = onTranslate
                ) { displayText ->
                    Text(
                        text = displayText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        color = Color(0xFF475569),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC), shape = RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Citizen's Simple Explanation
                TranslateText(
                    text = "CITIZEN'S SUMMARY",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp,
                    color = Color(0xFF059669),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TranslateContainer(
                    itemId = "${article.id}_explanation",
                    originalText = article.simpleExplanation,
                    targetLanguage = selectedLanguageName(selectedLanguage),
                    translatedTexts = translatedTexts,
                    translationLoadingState = translationLoadingState,
                    onTranslate = onTranslate
                ) { displayText ->
                    Text(
                        text = displayText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1E293B)
                    )
                }
            }
        }
    }
}

@Composable
fun AmendmentsScreen(
    selectedLanguage: String,
    bookmarks: List<Bookmark>,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onBookmarkToggle: (Amendment) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp)
    ) {
        item {
            TranslateText(
                text = "HISTORICAL AMENDMENTS",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TranslateText(
                text = "Track key changes made to the constitution over time to support India's evolving democracy.",
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(ConstitutionData.amendments, key = { it.id }) { amend ->
            val isBookmarked = bookmarks.any { it.id == amend.id }
            AmendmentTimelineItem(
                amend = amend,
                isBookmarked = isBookmarked,
                selectedLanguage = selectedLanguage,
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate,
                onBookmarkClick = { onBookmarkToggle(amend) }
            )
        }
    }
}

@Composable
fun AmendmentTimelineItem(
    amend: Amendment,
    isBookmarked: Boolean,
    selectedLanguage: String,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("amendment_card_${amend.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = amend.year,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = Color(0xFF4F46E5)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        TranslateText(
                            text = amend.title,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        TranslateText(
                            text = amend.actNumber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.testTag("bookmark_amend_${amend.id}")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Color(0xFFF59E0B) else Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(16.dp))

            // Amendment Summary
            TranslateText(
                text = "SUMMARY",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = Color(0xFF4F46E5),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            TranslateContainer(
                itemId = "${amend.id}_summary",
                originalText = amend.summary,
                targetLanguage = selectedLanguageName(selectedLanguage),
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate
            ) { displayText ->
                Text(
                    text = displayText,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Significance
            TranslateText(
                text = "SIGNIFICANCE & IMPACT",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = Color(0xFF059669),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            TranslateContainer(
                itemId = "${amend.id}_significance",
                originalText = amend.significance,
                targetLanguage = selectedLanguageName(selectedLanguage),
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate
            ) { displayText ->
                Text(
                    text = displayText,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E293B)
                )
            }
        }
    }
}

@Composable
fun CasesScreen(
    selectedLanguage: String,
    bookmarks: List<Bookmark>,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onBookmarkToggle: (CaseStudy) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp)
    ) {
        item {
            TranslateText(
                text = "LANDMARK CASE STUDIES",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TranslateText(
                text = "Explore judicial rulings that have shaped civil liberties and governed constitutional boundaries.",
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(ConstitutionData.caseStudies, key = { it.id }) { case ->
            val isBookmarked = bookmarks.any { it.id == case.id }
            CaseTimelineItem(
                case = case,
                isBookmarked = isBookmarked,
                selectedLanguage = selectedLanguage,
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate,
                onBookmarkClick = { onBookmarkToggle(case) }
            )
        }
    }
}

@Composable
fun CaseTimelineItem(
    case: CaseStudy,
    isBookmarked: Boolean,
    selectedLanguage: String,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("case_card_${case.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    TranslateText(
                        text = case.title,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFECFDF5))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TranslateText(
                                text = "Linked to:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = case.relevantArticle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.testTag("bookmark_case_${case.id}")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) Color(0xFFF59E0B) else Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                TranslateText(
                    text = "Case Citation:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = case.citation,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFE2E8F0))
            Spacer(modifier = Modifier.height(16.dp))

            // Facts
            TranslateText(
                text = "FACTS OF THE CASE",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = Color(0xFF4F46E5),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            TranslateContainer(
                itemId = "${case.id}_facts",
                originalText = case.facts,
                targetLanguage = selectedLanguageName(selectedLanguage),
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate
            ) { displayText ->
                Text(
                    text = displayText,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Decision
            TranslateText(
                text = "SUPREME COURT DECISION",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = Color(0xFF059669),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            TranslateContainer(
                itemId = "${case.id}_decision",
                originalText = case.decision,
                targetLanguage = selectedLanguageName(selectedLanguage),
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate
            ) { displayText ->
                Text(
                    text = displayText,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Impact
            TranslateText(
                text = "CONSTITUTIONAL IMPACT",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 10.sp,
                color = Color(0xFF0284C7),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            TranslateContainer(
                itemId = "${case.id}_impact",
                originalText = case.impact,
                targetLanguage = selectedLanguageName(selectedLanguage),
                translatedTexts = translatedTexts,
                translationLoadingState = translationLoadingState,
                onTranslate = onTranslate
            ) { displayText ->
                Text(
                    text = displayText,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E293B)
                )
            }
        }
    }
}


@Composable
fun QuizzesScreen(
    selectedLanguage: String,
    scores: List<QuizScore>,
    translatedTexts: Map<String, String>,
    translationLoadingState: Map<String, Boolean>,
    onTranslate: (String, String) -> Unit,
    onQuizCompleted: (String, Int, Int) -> Unit
) {
    var activeCategory by remember { mutableStateOf<String?>(null) }
    var currentQuestionIdx by remember { mutableStateOf(0) }
    var selectedAnswerIdx by remember { mutableStateOf<Int?>(null) }
    var scoreValue by remember { mutableStateOf(0) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showQuizFinishedScreen by remember { mutableStateOf(false) }

    if (activeCategory == null) {
        // Main categories explore screen
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp)
        ) {
            item {
                TranslateText(
                    text = "GAMIFIED CIVIL LITERACY",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF0F172A),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                TranslateText(
                    text = "Test your legal knowledge and earn certificates of civic literacy! All content translated on-the-fly.",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Display historical scores if any
            if (scores.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                        shape = RoundedCornerShape(24.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            TranslateText(
                                text = "Your Achievements / Scores",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color(0xFF4F46E5)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            scores.take(3).forEach { s ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TranslateText(s.category, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        TranslateText("Score:", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4F46E5))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("${s.score}/${s.totalQuestions} (${String.format("%.1f", s.percentage)}%)", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4F46E5))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Explore Categories
            items(ConstitutionData.quizzes.keys.toList()) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            activeCategory = category
                            currentQuestionIdx = 0
                            selectedAnswerIdx = null
                            scoreValue = 0
                            isSubmitted = false
                            showQuizFinishedScreen = false
                        }
                        .testTag("quiz_category_$category"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEEF2FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFF4F46E5))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                TranslateText(category, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF0F172A))
                                Spacer(modifier = Modifier.height(2.dp))
                                TranslateText("2 Questions • Translate Available", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                            }
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = "Play", tint = Color(0xFF4F46E5))
                    }
                }
            }
        }
    } else {
        val categoryName = activeCategory!!
        val questions = ConstitutionData.quizzes[categoryName] ?: emptyList()

        if (showQuizFinishedScreen) {
            // Finished Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(48.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                TranslateText(
                    text = "Quiz Completed!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TranslateText(
                    text = categoryName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TranslateText(
                        text = "You Scored:",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = Color(0xFF4F46E5)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$scoreValue / ${questions.size}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = Color(0xFF4F46E5)
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = { activeCategory = null },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("finish_quiz_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    TranslateText("Return to Categories", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }
        } else {
            // Active Question Screen
            val currentQuestion = questions[currentQuestionIdx]

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Header progress bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TranslateText("Question", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${currentQuestionIdx + 1} of ${questions.size}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        }
                        IconButton(onClick = { activeCategory = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Exit Quiz", tint = Color(0xFF0F172A))
                        }
                    }
                    LinearProgressIndicator(
                        progress = { (currentQuestionIdx + 1).toFloat() / questions.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF4F46E5),
                        trackColor = Color(0xFFE2E8F0)
                    )
                }

                // Question Title (Localized)
                item {
                    TranslateContainer(
                        itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_title",
                        originalText = currentQuestion.question,
                        targetLanguage = selectedLanguageName(selectedLanguage),
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = onTranslate
                    ) { displayText ->
                        Text(
                            text = displayText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 26.sp,
                            color = Color(0xFF0F172A),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }

                // Multiple choice answers (Localized)
                items(currentQuestion.options.indices.toList()) { idx ->
                    val optionText = currentQuestion.options[idx]
                    val isSelected = selectedAnswerIdx == idx
                    
                    val cardBgColor = when {
                        isSubmitted && idx == currentQuestion.correctAnswerIndex -> Color(0xFFECFDF5) // Correct green
                        isSubmitted && isSelected && selectedAnswerIdx != currentQuestion.correctAnswerIndex -> Color(0xFFFFF1F2) // Incorrect red
                        isSelected -> Color(0xFFEEF2FF) // Selected indigo-50
                        else -> Color.White
                    }

                    val borderColor = when {
                        isSubmitted && idx == currentQuestion.correctAnswerIndex -> Color(0xFF059669)
                        isSubmitted && isSelected && selectedAnswerIdx != currentQuestion.correctAnswerIndex -> Color(0xFFE11D48)
                        isSelected -> Color(0xFF4F46E5)
                        else -> Color(0xFFE2E8F0)
                    }

                    val letterColor = when {
                        isSubmitted && idx == currentQuestion.correctAnswerIndex -> Color(0xFF059669)
                        isSubmitted && isSelected && selectedAnswerIdx != currentQuestion.correctAnswerIndex -> Color(0xFFE11D48)
                        isSelected -> Color(0xFF4F46E5)
                        else -> Color(0xFF94A3B8)
                    }

                    TranslateContainer(
                        itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_opt_$idx",
                        originalText = optionText,
                        targetLanguage = selectedLanguageName(selectedLanguage),
                        translatedTexts = translatedTexts,
                        translationLoadingState = translationLoadingState,
                        onTranslate = onTranslate
                    ) { displayText ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isSubmitted) { selectedAnswerIdx = idx }
                                .testTag("quiz_option_$idx"),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(if (isSelected || isSubmitted) 2.dp else 1.dp, borderColor),
                            colors = CardDefaults.cardColors(containerColor = cardBgColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A'.code + idx).toChar()}.",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = letterColor
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = displayText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }
                    }
                }

                // Question Feedback & Explanation (Localized)
                if (isSubmitted) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val isCorrect = selectedAnswerIdx == currentQuestion.correctAnswerIndex

                            // Card 1: Answer Feedback Status
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isCorrect) Color(0xFFECFDF5) else Color(0xFFFFF1F2)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isCorrect) Color(0xFF10B981) else Color(0xFFF43F5E)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (isCorrect) Color(0xFF059669) else Color(0xFFE11D48),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    TranslateText(
                                        text = if (isCorrect) "✓ Correct Answer!" else "✗ Incorrect Answer!",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = if (isCorrect) Color(0xFF059669) else Color(0xFFE11D48)
                                    )
                                }
                            }

                            // Card 2: Option Specific Feedbacks (Explaining options)
                            if (currentQuestion.optionFeedbacks.isNotEmpty()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        TranslateText(
                                            text = "ANSWER BREAKDOWN",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp,
                                            color = Color(0xFF64748B),
                                            modifier = Modifier.padding(bottom = 12.dp)
                                        )

                                        // Show feedback for the selected answer
                                        val selectedIdx = selectedAnswerIdx ?: -1
                                        if (selectedIdx >= 0 && selectedIdx < currentQuestion.options.size) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                TranslateText(
                                                    text = "Your Answer:",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = if (isCorrect) Color(0xFF059669) else Color(0xFFE11D48)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${('A'.code + selectedIdx).toChar()}.",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = if (isCorrect) Color(0xFF059669) else Color(0xFFE11D48)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                TranslateContainer(
                                                    itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_opt_plain_${selectedIdx}",
                                                    originalText = currentQuestion.options[selectedIdx],
                                                    targetLanguage = selectedLanguageName(selectedLanguage),
                                                    translatedTexts = translatedTexts,
                                                    translationLoadingState = translationLoadingState,
                                                    onTranslate = onTranslate
                                                ) { displayText ->
                                                    Text(
                                                        text = displayText,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = if (isCorrect) Color(0xFF059669) else Color(0xFFE11D48)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            TranslateContainer(
                                                itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_opt_feedback_${selectedIdx}",
                                                originalText = currentQuestion.optionFeedbacks.getOrElse(selectedIdx) { "" },
                                                targetLanguage = selectedLanguageName(selectedLanguage),
                                                translatedTexts = translatedTexts,
                                                translationLoadingState = translationLoadingState,
                                                onTranslate = onTranslate
                                            ) { displayText ->
                                                Text(
                                                    text = displayText,
                                                    fontSize = 13.sp,
                                                    lineHeight = 20.sp,
                                                    color = Color(0xFF475569),
                                                    modifier = Modifier.padding(bottom = 12.dp)
                                                )
                                            }
                                        }

                                        // Show feedback for correct answer if the user made a mistake
                                        if (!isCorrect) {
                                            HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 8.dp))
                                            val correctIdx = currentQuestion.correctAnswerIndex
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                TranslateText(
                                                    text = "Correct Answer:",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFF059669)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "${('A'.code + correctIdx).toChar()}.",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = Color(0xFF059669)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                TranslateContainer(
                                                    itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_opt_plain_${correctIdx}",
                                                    originalText = currentQuestion.options[correctIdx],
                                                    targetLanguage = selectedLanguageName(selectedLanguage),
                                                    translatedTexts = translatedTexts,
                                                    translationLoadingState = translationLoadingState,
                                                    onTranslate = onTranslate
                                                ) { displayText ->
                                                    Text(
                                                        text = displayText,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF059669)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            TranslateContainer(
                                                itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_opt_feedback_${correctIdx}",
                                                originalText = currentQuestion.optionFeedbacks.getOrElse(correctIdx) { "" },
                                                targetLanguage = selectedLanguageName(selectedLanguage),
                                                translatedTexts = translatedTexts,
                                                translationLoadingState = translationLoadingState,
                                                onTranslate = onTranslate
                                            ) { displayText ->
                                                Text(
                                                    text = displayText,
                                                    fontSize = 13.sp,
                                                    lineHeight = 20.sp,
                                                    color = Color(0xFF475569)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Card 3: Legal Principle Card
                            if (currentQuestion.legalPrinciple.isNotEmpty()) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE)),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AccountBalance,
                                                    contentDescription = null,
                                                    tint = Color(0xFF4F46E5),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                TranslateText(
                                                    text = "CONSTITUTIONAL PRINCIPLE",
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF4F46E5)
                                                )
                                            }
                                            if (currentQuestion.articleReference.isNotEmpty()) {
                                                TranslateContainer(
                                                    itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_article_ref",
                                                    originalText = currentQuestion.articleReference,
                                                    targetLanguage = selectedLanguageName(selectedLanguage),
                                                    translatedTexts = translatedTexts,
                                                    translationLoadingState = translationLoadingState,
                                                    onTranslate = onTranslate
                                                ) { displayText ->
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(Color(0xFFE0E7FF))
                                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = displayText,
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = Color(0xFF4F46E5)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TranslateContainer(
                                            itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_legal_principle",
                                            originalText = currentQuestion.legalPrinciple,
                                            targetLanguage = selectedLanguageName(selectedLanguage),
                                            translatedTexts = translatedTexts,
                                            translationLoadingState = translationLoadingState,
                                            onTranslate = onTranslate
                                        ) { displayText ->
                                            Text(
                                                text = displayText,
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 16.sp,
                                                color = Color(0xFF1E1B4B)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        TranslateContainer(
                                            itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_legal_principle_desc",
                                            originalText = currentQuestion.legalPrincipleDescription,
                                            targetLanguage = selectedLanguageName(selectedLanguage),
                                            translatedTexts = translatedTexts,
                                            translationLoadingState = translationLoadingState,
                                            onTranslate = onTranslate
                                        ) { displayText ->
                                            Text(
                                                text = displayText,
                                                fontSize = 13.sp,
                                                lineHeight = 20.sp,
                                                color = Color(0xFF312E81)
                                            )
                                        }
                                    }
                                }
                            }

                            // Card 4: Detailed Constitutional Significance / Explanation
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    TranslateText(
                                        text = "CONSTITUTIONAL RATIONALE & SIGNIFICANCE",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F172A),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    
                                    TranslateContainer(
                                        itemId = "quiz_${categoryName.replace(" ", "_")}_q_${currentQuestion.id}_explanation",
                                        originalText = currentQuestion.explanation,
                                        targetLanguage = selectedLanguageName(selectedLanguage),
                                        translatedTexts = translatedTexts,
                                        translationLoadingState = translationLoadingState,
                                        onTranslate = onTranslate
                                    ) { displayText ->
                                        Text(
                                            text = displayText,
                                            fontSize = 14.sp,
                                            lineHeight = 22.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF475569)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Submit / Next Question action bar
                item {
                    Button(
                        onClick = {
                            if (!isSubmitted) {
                                isSubmitted = true
                                if (selectedAnswerIdx == currentQuestion.correctAnswerIndex) {
                                    scoreValue++
                                }
                            } else {
                                if (currentQuestionIdx + 1 < questions.size) {
                                    currentQuestionIdx++
                                    selectedAnswerIdx = null
                                    isSubmitted = false
                                } else {
                                    onQuizCompleted(categoryName, scoreValue, questions.size)
                                    showQuizFinishedScreen = true
                                }
                            }
                        },
                        enabled = selectedAnswerIdx != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("quiz_action_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4F46E5),
                            disabledContainerColor = Color(0xFFEEF2FF)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        val btnText = if (!isSubmitted) "Submit Answer" else if (currentQuestionIdx + 1 < questions.size) "Next Question" else "Finish Quiz"
                        TranslateText(
                            text = btnText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (selectedAnswerIdx != null) Color.White else Color(0xFF94A3B8)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WavingIndianFlag(
    modifier: Modifier = Modifier,
    height: Dp = 20.dp,
    width: Dp = 30.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "flag_wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.size(width = width, height = height)) {
        val w = size.width
        val h = size.height
        val waveAmplitude = h * 0.08f // 8% of height for wave amplitude
        val bandHeight = h / 3f

        // Draw Saffron, White, Green bands with wave offsets
        val sliceWidth = 1f
        for (x in 0 until w.toInt()) {
            val fx = x.toFloat()
            val waveOffset = kotlin.math.sin((fx / w) * 1.5f * (2 * Math.PI).toFloat() - phase) * waveAmplitude

            // Saffron band
            drawRect(
                color = Color(0xFFFF9933),
                topLeft = Offset(fx, waveOffset),
                size = Size(sliceWidth, bandHeight)
            )

            // White band
            drawRect(
                color = Color.White,
                topLeft = Offset(fx, bandHeight + waveOffset),
                size = Size(sliceWidth, bandHeight)
            )

            // Green band
            drawRect(
                color = Color(0xFF128807),
                topLeft = Offset(fx, 2 * bandHeight + waveOffset),
                size = Size(sliceWidth, bandHeight)
            )
        }

        // Draw Ashoka Chakra at the center
        val centerX = w / 2f
        val chakraWaveOffset = kotlin.math.sin((centerX / w) * 1.5f * (2 * Math.PI).toFloat() - phase) * waveAmplitude
        val centerY = h / 2f + chakraWaveOffset
        val chakraRadius = bandHeight * 0.4f

        if (chakraRadius > 1.dp.toPx()) {
            drawCircle(
                color = Color(0xFF000080),
                radius = chakraRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1.dp.toPx())
            )

            // Draw 24 spokes
            val spokeCount = 24
            for (i in 0 until spokeCount) {
                val angle = i * (360f / spokeCount)
                val angleRad = Math.toRadians(angle.toDouble())
                val endX = centerX + chakraRadius * kotlin.math.cos(angleRad).toFloat()
                val endY = centerY + chakraRadius * kotlin.math.sin(angleRad).toFloat()
                drawLine(
                    color = Color(0xFF000080),
                    start = Offset(centerX, centerY),
                    end = Offset(endX, endY),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
        }
    }
}


@Composable
fun PetalShower(
    modifier: Modifier = Modifier,
    alpha: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "petal_shower")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        if (w == 0f || h == 0f) return@Canvas

        val petalCount = 35
        val colors = listOf(
            Color(0xFFFF9933), // Saffron
            Color(0xFFFFFFFF), // White
            Color(0xFF128807)  // Green
        )

        for (i in 0 until petalCount) {
            val seed = i * 13
            val startX = (seed * 17) % w
            val speed = 80.dp.toPx() + (seed % 7) * 15.dp.toPx()
            val scale = 0.6f + (seed % 5) * 0.15f
            val swingAmplitude = 15.dp.toPx() + (seed % 4) * 8.dp.toPx()
            val swingFrequency = 1.5f + (seed % 3) * 0.5f

            val elapsedY = time * speed * 0.05f
            val y = (elapsedY + (seed * 23) % h) % h

            val swing = kotlin.math.sin((y / h) * swingFrequency * Math.PI.toFloat() * 4) * swingAmplitude
            val x = (startX + swing) % w

            val rotationAngle = (y * 0.5f + seed) % 360f
            val color = colors[i % colors.size]

            val petalWidth = 6.dp.toPx() * scale
            val petalHeight = 12.dp.toPx() * scale

            drawContext.canvas.save()
            drawContext.canvas.translate(x, y)
            drawContext.canvas.rotate(rotationAngle)

            drawRoundRect(
                color = color.copy(alpha = 0.85f * alpha),
                topLeft = Offset(-petalWidth / 2f, -petalHeight / 2f),
                size = Size(petalWidth, petalHeight),
                cornerRadius = CornerRadius(petalWidth, petalWidth / 2f)
            )

            drawLine(
                color = if (color == Color.White) Color(0xFFCBD5E1).copy(alpha = 0.5f * alpha) else Color.White.copy(alpha = 0.4f * alpha),
                start = Offset(0f, -petalHeight / 2f),
                end = Offset(0f, petalHeight / 2f),
                strokeWidth = 0.8.dp.toPx()
            )

            drawContext.canvas.restore()
        }
    }
}


@Composable
fun FlagHoistingSplashScreen(
    onAnimationFinished: () -> Unit
) {
    val hoistProgress = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val flowerAlpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "splash_flag_wave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    LaunchedEffect(Unit) {
        launch {
            hoistProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
            )
        }
        launch {
            // Wait for flag to be halfway up, then start fading in other elements
            kotlinx.coroutines.delay(1500)
            launch {
                textAlpha.animateTo(1f, animationSpec = tween(1000))
            }
            launch {
                flowerAlpha.animateTo(1f, animationSpec = tween(1200))
            }
        }
        // Hold at top for 1.5s after completion
        kotlinx.coroutines.delay(4500)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF7ED), // Very soft saffron tint
                        Color(0xFFFFFFFF), // White
                        Color(0xFFF0FDF4)  // Very soft green tint
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val progressVal = hoistProgress.value
        if (progressVal > 0.5f) {
            PetalShower(modifier = Modifier.fillMaxSize(), alpha = flowerAlpha.value)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp)
                ) {
                    val w = size.width
                    val h = size.height

                    val poleX = w / 2f
                    val poleTopY = h * 0.1f
                    val poleBottomY = h * 0.9f
                    val poleThickness = 4.dp.toPx()

                    // Gold sphere on top
                    drawCircle(
                        color = Color(0xFFD4AF37),
                        radius = 8.dp.toPx(),
                        center = Offset(poleX, poleTopY)
                    )

                    // Flagpole body
                    drawRect(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF94A3B8),
                                Color(0xFFCBD5E1),
                                Color(0xFF64748B)
                            )
                        ),
                        topLeft = Offset(poleX - poleThickness / 2f, poleTopY),
                        size = Size(poleThickness, poleBottomY - poleTopY)
                    )

                    // Base stand
                    val standY1 = poleBottomY
                    val standY2 = poleBottomY + 10.dp.toPx()
                    val standY3 = poleBottomY + 20.dp.toPx()

                    drawRoundRect(
                        color = Color(0xFFE2E8F0),
                        topLeft = Offset(poleX - 20.dp.toPx(), standY1),
                        size = Size(40.dp.toPx(), 10.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )

                    drawRoundRect(
                        color = Color(0xFFCBD5E1),
                        topLeft = Offset(poleX - 35.dp.toPx(), standY2),
                        size = Size(70.dp.toPx(), 10.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )

                    drawRoundRect(
                        color = Color(0xFF94A3B8),
                        topLeft = Offset(poleX - 50.dp.toPx(), standY3),
                        size = Size(100.dp.toPx(), 12.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )

                    val flagHeight = 44.dp.toPx()
                    val flagWidth = 72.dp.toPx()

                    val startFlagY = poleBottomY - flagHeight - 10.dp.toPx()
                    val endFlagY = poleTopY + 6.dp.toPx()

                    val currentFlagY = startFlagY + (endFlagY - startFlagY) * progressVal

                    // Draw rope lines
                    drawLine(
                        color = Color(0xFF94A3B8),
                        start = Offset(poleX - 3.dp.toPx(), poleTopY + 8.dp.toPx()),
                        end = Offset(poleX - 3.dp.toPx(), poleBottomY),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                    )

                    val flagLeftX = poleX + 1.dp.toPx()
                    val waveAmplitude = flagHeight * 0.07f
                    val bandHeight = flagHeight / 3f

                    val sizeFactor = 0.3f + 0.7f * progressVal
                    val actualFlagWidth = flagWidth * sizeFactor
                    val actualFlagHeight = flagHeight * sizeFactor
                    val actualBandHeight = actualFlagHeight / 3f
                    val actualWaveAmplitude = waveAmplitude * sizeFactor

                    // Slice-based waving drawing
                    val sliceWidth = 1f
                    for (x in 0 until actualFlagWidth.toInt()) {
                        val fx = x.toFloat()
                        val waveOffset = kotlin.math.sin((fx / actualFlagWidth) * 1.5f * (2 * Math.PI).toFloat() - phase) * actualWaveAmplitude
                        val targetX = flagLeftX + fx

                        // Saffron
                        drawRect(
                            color = Color(0xFFFF9933),
                            topLeft = Offset(targetX, currentFlagY + waveOffset),
                            size = Size(sliceWidth, actualBandHeight)
                        )

                        // White
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(targetX, currentFlagY + actualBandHeight + waveOffset),
                            size = Size(sliceWidth, actualBandHeight)
                        )

                        // Green
                        drawRect(
                            color = Color(0xFF128807),
                            topLeft = Offset(targetX, currentFlagY + 2 * actualBandHeight + waveOffset),
                            size = Size(sliceWidth, actualBandHeight)
                        )
                    }

                    // Ashoka Chakra on waving flag
                    val chakraCenterX = flagLeftX + actualFlagWidth / 2f
                    val chakraWaveOffset = kotlin.math.sin(((actualFlagWidth / 2f) / actualFlagWidth) * 1.5f * (2 * Math.PI).toFloat() - phase) * actualWaveAmplitude
                    val chakraCenterY = currentFlagY + actualFlagHeight / 2f + chakraWaveOffset
                    val chakraRadius = actualBandHeight * 0.4f

                    if (chakraRadius > 1.dp.toPx()) {
                        drawCircle(
                            color = Color(0xFF000080),
                            radius = chakraRadius,
                            center = Offset(chakraCenterX, chakraCenterY),
                            style = Stroke(width = 0.8.dp.toPx())
                        )

                        for (i in 0 until 24) {
                            val angle = i * (360f / 24f)
                            val angleRad = Math.toRadians(angle.toDouble())
                            val endX = chakraCenterX + chakraRadius * kotlin.math.cos(angleRad).toFloat()
                            val endY = chakraCenterY + chakraRadius * kotlin.math.sin(angleRad).toFloat()
                            drawLine(
                                color = Color(0xFF000080),
                                start = Offset(chakraCenterX, chakraCenterY),
                                end = Offset(endX, endY),
                                strokeWidth = 0.4.dp.toPx()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "BHAARAT SANVIDHAN",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF1E1B4B),
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.testTag("splash_title")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "CONSTITUTION OF INDIA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF4F46E5),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(bottom = 32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .width(40.dp)
                            .background(Color(0xFFFF9933))
                    )
                    Text(
                        text = " सत्यमेव जयते ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Serif,
                        color = Color(0xFF128807),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .width(40.dp)
                            .background(Color(0xFF128807))
                    )
                }
            }
        }
    }
}
