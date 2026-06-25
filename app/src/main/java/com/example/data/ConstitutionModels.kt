package com.example.data

data class Article(
    val id: String,
    val number: String,
    val title: String,
    val part: String,
    val schedule: String? = null,
    val rawText: String,
    val simpleExplanation: String
)

data class Amendment(
    val id: String,
    val title: String,
    val actNumber: String,
    val year: String,
    val summary: String,
    val significance: String
)

data class CaseStudy(
    val id: String,
    val title: String,
    val citation: String,
    val relevantArticle: String,
    val summary: String,
    val facts: String,
    val decision: String,
    val impact: String
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val legalPrinciple: String = "",
    val legalPrincipleDescription: String = "",
    val articleReference: String = "",
    val optionFeedbacks: List<String> = emptyList()
)

object ConstitutionData {
    val officialIndianLanguages = listOf(
        "English",
        "Assamese (অসমীয়া)",
        "Bengali (বাংলা)",
        "Bodo (বোড়ো)",
        "Dogri (डোগরী)",
        "Gujarati (ગુજરાતી)",
        "Hindi (हिन्दी)",
        "Kannada (ಕন্নಡ)",
        "Kashmiri (कॉशुर)",
        "Konkani (कोंकणी)",
        "Maithili (मैथिली)",
        "Malayalam (മലയാളം)",
        "Manipuri (মণিপুরী)",
        "Marathi (मराठी)",
        "Nepali (नेपाली)",
        "Odia (ଓଡ଼িয়া)",
        "Punjabi (ਪੰਜਾਬੀ)",
        "Sanskrit (संस्कृतम्)",
        "Santali (সাঁওতালী)",
        "Sindhi (सिन्धी)",
        "Tamil (தமிழ்)",
        "Telugu (తెలుగు)",
        "Urdu (उर्दू)"
    )

    val articles = listOf(
        Article(
            id = "art_1",
            number = "Article 1",
            title = "Name and territory of the Union",
            part = "Part I: The Union and its Territory",
            rawText = "(1) India, that is Bharat, shall be a Union of States. (2) The States and the territories thereof shall be as specified in the First Schedule. (3) The territory of India shall comprise (a) the territories of the States; (b) the Union territories specified in the First Schedule; and (c) such other territories as may be acquired.",
            simpleExplanation = "Defines the dual official names of our nation: 'India' and 'Bharat'. Clarifies that India is a federal framework with unified power, composed of individual states, Union Territories, and any newly acquired lands."
        ),
        Article(
            id = "art_14",
            number = "Article 14",
            title = "Equality before law",
            part = "Part III: Fundamental Rights",
            rawText = "The State shall not deny to any person equality before the law or the equal protection of the laws within the territory of India.",
            simpleExplanation = "Ensures no individual is above the law. Everyone, from the highest authority to a private citizen, receives equal treatment under the courts of law."
        ),
        Article(
            id = "art_19",
            number = "Article 19",
            title = "Protection of certain rights regarding freedom of speech, etc.",
            part = "Part III: Fundamental Rights",
            rawText = "(1) All citizens shall have the right (a) to freedom of speech and expression; (b) to assemble peaceably and without arms; (c) to form associations or unions or co-operative societies; (d) to move freely throughout the territory of India; (e) to reside and settle in any part of the territory of India; (g) to practise any profession, or to carry on any occupation, trade or business.",
            simpleExplanation = "Grants the six core basic freedoms of Indian citizens. These rights are not absolute and can have reasonable restrictions in the interest of public safety, national security, or decency."
        ),
        Article(
            id = "art_21",
            number = "Article 21",
            title = "Protection of life and personal liberty",
            part = "Part III: Fundamental Rights",
            rawText = "No person shall be deprived of his life or personal liberty except according to procedure established by law.",
            simpleExplanation = "The cornerstone of individual rights in India. Broadly interpreted by the Supreme Court to encompass the right to clean air, water, privacy, livelihood, and medical care."
        ),
        Article(
            id = "art_21A",
            number = "Article 21A",
            title = "Right to education",
            part = "Part III: Fundamental Rights",
            rawText = "The State shall provide free and compulsory education to all children of the age of six to fourteen years in such manner as the State may, by law, determine.",
            simpleExplanation = "Mandates the government to provide free and compulsory primary education to every child aged 6-14, ensuring education is a fundamental human right."
        ),
        Article(
            id = "art_32",
            number = "Article 32",
            title = "Remedies for enforcement of rights",
            part = "Part III: Fundamental Rights",
            rawText = "(1) The right to move the Supreme Court by appropriate proceedings for the enforcement of the rights conferred by this Part is guaranteed. (2) The Supreme Court shall have power to issue directions or orders or writs...",
            simpleExplanation = "Known as the 'Heart and Soul of the Constitution' as described by Dr. B.R. Ambedkar. Allows citizens to approach the Supreme Court directly if their fundamental rights are violated."
        ),
        Article(
            id = "art_39A",
            number = "Article 39A",
            title = "Equal justice and free legal aid",
            part = "Part IV: Directive Principles of State Policy",
            rawText = "The State shall secure that the operation of the legal system promotes justice, on a basis of equal opportunity, and shall, in particular, provide free legal aid...",
            simpleExplanation = "Instructs the state to ensure access to justice for all. Promotes equal legal opportunity by offering free legal assistance to underprivileged citizens who cannot afford dynamic legal counsel."
        ),
        Article(
            id = "art_51A",
            number = "Article 51A",
            title = "Fundamental duties",
            part = "Part IVA: Fundamental Duties",
            rawText = "It shall be the duty of every citizen of India (a) to abide by the Constitution and respect its ideals and institutions, the National Flag and the National Anthem; (b) to cherish and follow the noble ideals which inspired our national struggle for freedom...",
            simpleExplanation = "Identifies 11 foundational duties that Indian citizens are morally expected to follow, serving as a reminder of patriotic responsibility towards the sovereignty and harmony of India."
        )
    )

    val amendments = listOf(
        Amendment(
            id = "amend_1",
            title = "The 1st Amendment Act",
            actNumber = "Constitution (First Amendment) Act, 1951",
            year = "1951",
            summary = "Modified fundamental rights to speech and expression, property, and introduced the Ninth Schedule to protect land reform laws from judicial review.",
            significance = "Allowed the young republic to implement vital social reforms like state-owned monopolies and agrarian land redistributions without legal blocks."
        ),
        Amendment(
            id = "amend_42",
            title = "The 42nd Amendment Act",
            actNumber = "Constitution (Forty-second Amendment) Act, 1976",
            year = "1976",
            summary = "Famously termed the 'Mini-Constitution'. Added the words 'Socialist', 'Secular', and 'Integrity' to the Preamble, and inserted Part IVA detailing Fundamental Duties.",
            significance = "Reflected a massive structural push during the national Emergency, cementing secularism and citizen responsibilities as explicit constitutional directives."
        ),
        Amendment(
            id = "amend_44",
            title = "The 44th Amendment Act",
            actNumber = "Constitution (Forty-fourth Amendment) Act, 1978",
            year = "1978",
            summary = "Reversed several controversial provisions of the 42nd Amendment. Declared that Articles 20 and 21 cannot be suspended during an Emergency and changed 'internal disturbance' to 'armed rebellion'.",
            significance = "Restored the balance of democracy and democratic guardrails, ensuring fundamental liberties are shielded from arbitrary state override."
        ),
        Amendment(
            id = "amend_73",
            title = "The 73rd Amendment Act",
            actNumber = "Constitution (Seventy-third Amendment) Act, 1992",
            year = "1992",
            summary = "Granted constitutional status and protection to Panchayati Raj Institutions (local village governance) by introducing Part IX and the Eleventh Schedule.",
            significance = "Decentralized democracy in India, establishing self-governance at the rural grassroots with mandated reservation seats for women and marginalized communities."
        ),
        Amendment(
            id = "amend_101",
            title = "The 101st Amendment Act",
            actNumber = "Constitution (One Hundred and First Amendment) Act, 2016",
            year = "2016",
            summary = "Introduced the Goods and Services Tax (GST) framework across India. Empowered both Parliament and State legislatures to levy tax concurrently.",
            significance = "Unified the diverse state taxation systems into a singular shared national market, representing a major milestone in Indian cooperative fiscal federalism."
        ),
        Amendment(
            id = "amend_106",
            title = "The 106th Amendment Act",
            actNumber = "Constitution (One Hundred and Sixth Amendment) Act, 2023",
            year = "2023",
            summary = "Mandated reservation of one-third of all seats for women in the House of the People (Lok Sabha), the State Legislative Assemblies, and the Legislative Assembly of Delhi.",
            significance = "Ensures significant gender representation in the highest legislative chambers of the nation for a projected 15-year duration."
        )
    )

    val caseStudies = listOf(
        CaseStudy(
            id = "case_kesavananda",
            title = "Kesavananda Bharati v. State of Kerala",
            citation = "AIR 1973 SC 1461",
            relevantArticle = "Article 368 (Amendment Power)",
            summary = "One of the most consequential rulings in Indian constitutional history. Established the 'Basic Structure Doctrine'.",
            facts = "A landowner challenged land reforms after a state government seized temple property. The core legal battle focused on whether Parliament's power to amend the constitution was completely absolute under Article 368.",
            decision = "The Supreme Court ruled (7-6 majority) that while Parliament has wide power to amend the Constitution, it cannot alter, destroy, or rewrite its 'Basic Structure' (e.g. democracy, secularism, rule of law).",
            impact = "Prevented India from turning into a one-party authoritarian state, cementing judicial review as an indestructible check on the legislative branch."
        ),
        CaseStudy(
            id = "case_maneka",
            title = "Maneka Gandhi v. Union of India",
            citation = "AIR 1978 SC 597",
            relevantArticle = "Article 21 (Right to Life)",
            summary = "Expanded the scope of personal liberty and transformed the legal criteria for evaluating state action under Article 21.",
            facts = "Maneka Gandhi's passport was impounded by administrative authorities without giving any specific reason, citing general 'public interest'. She filed a writ petition challenging the arbitrary confiscation.",
            decision = "The Supreme Court ruled that personal liberty under Article 21 is not confined merely to freedom from physical restraint. Any law depriving someone of liberty must satisfy the test of being 'just, fair, and reasonable' rather than merely standard procedural compliance.",
            impact = "Shifted Indian jurisprudence from simple 'Procedure Established by Law' to an active interpretation resembling 'Due Process of Law', enormously expanding human rights protections."
        ),
        CaseStudy(
            id = "case_shahbano",
            title = "Mohd. Ahmed Khan v. Shah Bano Begum",
            citation = "AIR 1985 SC 945",
            relevantArticle = "Article 44 (Uniform Civil Code)",
            summary = "Highlighted the complex clash between individual gender rights, personal religious laws, and Directive Principles.",
            facts = "A divorced Muslim woman, Shah Bano, sued her former husband for basic maintenance under Section 125 of the Criminal Procedure Code. The husband argued that Muslim personal law limited his maintenance obligations.",
            decision = "The Supreme Court upheld her right to alimony, ruling that Section 125 applies to all citizens regardless of religion, overriding conflicting personal laws.",
            impact = "Sparked intense socio-political debates regarding secularism, personal laws, and judicial intervention, accelerating national discourse on the adoption of a Uniform Civil Code."
        ),
        CaseStudy(
            id = "case_puttaswamy",
            title = "K.S. Puttaswamy v. Union of India",
            citation = "(2017) 10 SCC 1",
            relevantArticle = "Article 21 (Right to Privacy)",
            summary = "Established privacy as an intrinsic, constitutionally protected fundamental right under Article 21.",
            facts = "A retired judge challenged the constitutional validity of the Aadhaar biometric identification system, asserting that forced gathering of personal biometrics violates a citizen's basic privacy.",
            decision = "A unanimous 9-judge bench ruled that the Right to Privacy is protected as an essential, fundamental aspect of the Right to Life and Personal Liberty.",
            impact = "Fundamentally reshaped the digital rights landscape in India, leading to extensive privacy checks on surveillance, data protection laws, and LGBTQ+ decriminilization."
        )
    )

    val quizzes = mapOf(
        "Preamble & Territory" to listOf(
            QuizQuestion(
                id = 1,
                question = "Which words were added to the Preamble of the Indian Constitution by the 42nd Amendment Act of 1976?",
                options = listOf(
                    "Sovereign, Democratic, Republic",
                    "Secular, Socialist, Integrity",
                    "Liberty, Equality, Fraternity",
                    "Justice, Social, Economic"
                ),
                correctAnswerIndex = 1,
                explanation = "The 42nd Amendment of 1976 added the terms 'Socialist', 'Secular', and 'Integrity' to the Preamble, highlighting the social and unified goals of the newly reformed republic.",
                legalPrinciple = "Secularism & National Integrity",
                legalPrincipleDescription = "Secularism asserts that the State has no official religion and treats all faiths equally. National Integrity emphasizes unity and fraternal bonding among citizens.",
                articleReference = "The Preamble",
                optionFeedbacks = listOf(
                    "These words are in the original Preamble from 1950 but do not represent the 1976 additions.",
                    "Correct! Added during the 42nd Amendment (1976) to explicitly reflect India's pluralistic fabric and socialist commitments.",
                    "These values are democratic pillars in the original Preamble, inspired by the French Revolution.",
                    "These represent fundamental objectives of justice outlined in the original Preamble."
                )
            ),
            QuizQuestion(
                id = 2,
                question = "According to Article 1, India is defined as a:",
                options = listOf(
                    "Federation of States",
                    "Union of States",
                    "Unitary State with Provinces",
                    "Confederation of Independent Territories"
                ),
                correctAnswerIndex = 1,
                explanation = "Article 1(1) states: 'India, that is Bharat, shall be a Union of States.' This phrase emphasizes that states do not have the right to secede from the country.",
                legalPrinciple = "Indestructible Union of Destructible States",
                legalPrincipleDescription = "While state boundaries can be altered by Parliament, states have no constitutional right to secede. The nation's integrity is permanent.",
                articleReference = "Article 1",
                optionFeedbacks = listOf(
                    "A 'federation' is a voluntary treaty of sovereign states (like the US). India is a union because its structure is indestructible.",
                    "Correct! 'Union of States' reflects that the union is not a result of an agreement and no state has the right to secede.",
                    "India is federal in structure with strong unitary features, but is constitutionally a 'Union of States'.",
                    "A 'confederation' is a loose alliance of independent nations. India is a single sovereign entity."
                )
            )
        ),
        "Fundamental Rights" to listOf(
            QuizQuestion(
                id = 3,
                question = "Which article is famously referred to as the 'Heart and Soul' of the Constitution of India?",
                options = listOf(
                    "Article 14",
                    "Article 19",
                    "Article 21",
                    "Article 32"
                ),
                correctAnswerIndex = 3,
                explanation = "Dr. B.R. Ambedkar termed Article 32 (Constitutional Remedies) as the Heart and Soul, because it gives citizens direct legal recourse to the Supreme Court to restore their violated rights.",
                legalPrinciple = "Right to Constitutional Remedies",
                legalPrincipleDescription = "A right without a remedy is meaningless. Article 32 guarantees the right to petition the Supreme Court directly to enforce fundamental rights.",
                articleReference = "Article 32",
                optionFeedbacks = listOf(
                    "Article 14 guarantees Equality before Law, which is a foundational right but not the institutional 'remedy' itself.",
                    "Article 19 protects core civil liberties like Free Speech, which require Article 32 to be legally enforced.",
                    "Article 21 protects Life and Liberty. While heavily expanded, the enforcement power lies in Article 32.",
                    "Correct! Article 32 is the 'Heart and Soul' because it provides the legal writs needed to challenge violations of rights."
                )
            ),
            QuizQuestion(
                id = 4,
                question = "Which constitutional amendment established the 'Right to Education' as a Fundamental Right under Article 21A?",
                options = listOf(
                    "44th Amendment Act",
                    "86th Amendment Act",
                    "91st Amendment Act",
                    "103rd Amendment Act"
                ),
                correctAnswerIndex = 1,
                explanation = "The 86th Constitutional Amendment Act, enacted in 2002, inserted Article 21A to ensure free and compulsory primary education for all children aged 6 to 14 years.",
                legalPrinciple = "Right to Education as a Life Directive",
                legalPrincipleDescription = "Education is fundamental to individual dignity and personal liberty. The state must provide free, compulsory education to child citizens.",
                articleReference = "Article 21A",
                optionFeedbacks = listOf(
                    "The 44th Amendment restored democratic rights post-Emergency but did not establish the Right to Education.",
                    "Correct! The 86th Amendment Act in 2002 established Article 21A, turning a non-justiciable directive into a fundamental right.",
                    "The 91st Amendment limited the size of council of ministers and has no relation to education.",
                    "The 103rd Amendment introduced EWS reservations and does not cover the 2002 primary education mandate."
                )
            )
        ),
        "Directive Principles & Duties" to listOf(
            QuizQuestion(
                id = 5,
                question = "Which article specifies that the State shall provide equal justice and free legal aid to underprivileged citizens?",
                options = listOf(
                    "Article 39",
                    "Article 39A",
                    "Article 40",
                    "Article 44"
                ),
                correctAnswerIndex = 1,
                explanation = "Article 39A was added by the 42nd Amendment to instruct the state to provide free legal aid to ensure no citizen is denied justice due to financial or economic disabilities.",
                legalPrinciple = "Equal Access to Justice",
                legalPrincipleDescription = "Justice is a mirage if poverty prevents legal representation. Free legal aid ensures economic status does not deny judicial protection.",
                articleReference = "Article 39A",
                optionFeedbacks = listOf(
                    "Article 39 contains general principles of state policy regarding livelihoods and concentration of wealth.",
                    "Correct! Article 39A explicitly directs the state to secure equal justice and provide free legal aid to the needy.",
                    "Article 40 directs the state to organize village panchayats.",
                    "Article 44 instructs the state to secure a Uniform Civil Code for all citizens."
                )
            ),
            QuizQuestion(
                id = 6,
                question = "In which part of the Constitution are 'Fundamental Duties' located?",
                options = listOf(
                    "Part III",
                    "Part IV",
                    "Part IVA",
                    "Part V"
                ),
                correctAnswerIndex = 2,
                explanation = "Fundamental Duties are located in Part IVA (specifically Article 51A). They were added in 1976 to list a citizen's patriotic responsibilities towards the Nation.",
                legalPrinciple = "Civic Duty & Constitutional Patriotism",
                legalPrincipleDescription = "Rights and duties are correlative. Citizens have moral obligations to respect state symbols and promote national harmony.",
                articleReference = "Part IVA (Article 51A)",
                optionFeedbacks = listOf(
                    "Part III contains Fundamental Rights, which are legally enforceable against the state.",
                    "Part IV contains Directive Principles of State Policy, which are non-binding guidelines for lawmaking.",
                    "Correct! Part IVA was added by the 42nd Amendment (1976) to list the 11 fundamental duties of every citizen.",
                    "Part V covers details of the Union Executive, Parliament, and Judiciary."
                )
            )
        )
    )
}
