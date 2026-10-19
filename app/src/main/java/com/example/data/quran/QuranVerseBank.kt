package com.example.data.quran

import com.example.data.model.Verse

object QuranVerseBank {

    private val staticVerses: Map<Int, Verse> = mapOf(
        // Al-Fatihah (1:1 - 1:7)
        1 to Verse(
            1, 1, 1,
            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
            "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
            "Bismillahi r-rahmani r-rahim",
            audioUrlFor(1, 1)
        ),
        2 to Verse(
            2, 1, 2,
            "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
            "[All] praise is [due] to Allah, Lord of the worlds -",
            "Al-hamdu lillahi rabbi l-'alamin",
            audioUrlFor(1, 2)
        ),
        3 to Verse(
            3, 1, 3,
            "الرَّحْمَٰنِ الرَّحِيمِ",
            "The Entirely Merciful, the Especially Merciful,",
            "Ar-rahmani r-rahim",
            audioUrlFor(1, 3)
        ),
        4 to Verse(
            4, 1, 4,
            "مَالِكِ يَوْمِ الدِّينِ",
            "Sovereign of the Day of Recompense.",
            "Maliki yawmi d-din",
            audioUrlFor(1, 4)
        ),
        5 to Verse(
            5, 1, 5,
            "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            "It is You we worship and You we ask for help.",
            "Iyyaka na'budu wa-iyyaka nasta'in",
            audioUrlFor(1, 5)
        ),
        6 to Verse(
            6, 1, 6,
            "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
            "Guide us to the straight path -",
            "Ihdina s-sirata l-mustaqim",
            audioUrlFor(1, 6)
        ),
        7 to Verse(
            7, 1, 7,
            "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
            "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.",
            "Sirata l-ladhina an'amta 'alayhim ghayri l-maghdubi 'alayhim wa-la d-dallin",
            audioUrlFor(1, 7)
        ),

        // Al-Baqarah (2:1 - 2:5)
        8 to Verse(
            8, 2, 1,
            "الم",
            "Alif, Lam, Meem.",
            "Alif-Lam-Meem",
            audioUrlFor(2, 1)
        ),
        9 to Verse(
            9, 2, 2,
            "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ",
            "This is the Book about which there is no doubt, a guidance for those conscious of Allah -",
            "Dhalika l-kitabu la rayba fih, hudan lil-muttaqin",
            audioUrlFor(2, 2)
        ),
        10 to Verse(
            10, 2, 3,
            "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنفِقُونَ",
            "Who believe in the unseen, establish prayer, and spend out of what We have provided for them,",
            "Al-ladhina yu'minuna bi-l-ghaybi wa-yuqimuna s-salata wa-mimma razaqnahum yunfiqun",
            audioUrlFor(2, 3)
        ),
        11 to Verse(
            11, 2, 4,
            "وَالَّذِينَ يُؤْمِنُونَ بِمَا أُنزِلَ إِلَيْكَ وَمَا أُنزِلَ مِن قَبْلِكَ وَبِالْآخِرَةِ هُمْ يُوقِنُونَ",
            "And who believe in what has been revealed to you, [O Muhammad], and what was revealed before you, and of the Hereafter they are certain [in faith].",
            "Wa-l-ladhina yu'minuna bima unzila ilayka wa-ma unzila min qablika wa-bi-l-akhirati hum yuqinun",
            audioUrlFor(2, 4)
        ),
        12 to Verse(
            12, 2, 5,
            "أُولَٰئِكَ عَلَىٰ هُدًى مِّن رَّبِّهِمْ ۖ وَأُولَٰئِكَ هُمُ الْمُفْلِحُونَ",
            "Those are upon [right] guidance from their Lord, and it is those who are the successful.",
            "Ula'ika 'ala hudan min rabbihim wa-ula'ika humu l-muflihun",
            audioUrlFor(2, 5)
        ),

        // Ayat Al-Kursi (2:255) -> global 8 + 254 = 262
        262 to Verse(
            262, 2, 255,
            "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
            "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth.",
            "Allahu la ilaha illa huwa l-hayyu l-qayyum, la ta'khudhuhu sinatun wa-la nawm",
            audioUrlFor(2, 255)
        ),

        // Al-Ikhlas (112:1 - 112:4) -> global 6222..6225
        6222 to Verse(
            6222, 112, 1,
            "قُلْ هُوَ اللَّهُ أَحَدٌ",
            "Say, \"He is Allah, [who is] One,",
            "Qul huwa Allahu ahad",
            audioUrlFor(112, 1)
        ),
        6223 to Verse(
            6223, 112, 2,
            "اللَّهُ الصَّمَدُ",
            "Allah, the Eternal Refuge.",
            "Allahu s-samad",
            audioUrlFor(112, 2)
        ),
        6224 to Verse(
            6224, 112, 3,
            "لَمْ يَلِدْ وَلَمْ يُولَدْ",
            "He neither begets nor is born,",
            "Lam yalid wa-lam yulad",
            audioUrlFor(112, 3)
        ),
        6225 to Verse(
            6225, 112, 4,
            "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            "Nor is there to Him any equivalent.\"",
            "Wa-lam yakun lahu kufuwan ahad",
            audioUrlFor(112, 4)
        ),

        // Al-Falaq (113:1 - 113:5) -> 6226..6230
        6226 to Verse(
            6226, 113, 1,
            "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ",
            "Say, \"I seek refuge in the Lord of daybreak",
            "Qul a'udhu bi-rabbi l-falaq",
            audioUrlFor(113, 1)
        ),
        6227 to Verse(
            6227, 113, 2,
            "مِن شَرِّ مَا خَلَقَ",
            "From the evil of that which He created",
            "Min sharri ma khalaq",
            audioUrlFor(113, 2)
        ),
        6228 to Verse(
            6228, 113, 3,
            "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ",
            "And from the evil of darkness when it settles",
            "Wa-min sharri ghasiqin idha waqab",
            audioUrlFor(113, 3)
        ),
        6229 to Verse(
            6229, 113, 4,
            "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ",
            "And from the evil of the blowers in knots",
            "Wa-min sharri n-naffathati fi l-'uqad",
            audioUrlFor(113, 4)
        ),
        6230 to Verse(
            6230, 113, 5,
            "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            "And from the evil of an envier when he envies.\"",
            "Wa-min sharri hasidin idha hasad",
            audioUrlFor(113, 5)
        ),

        // An-Nas (114:1 - 114:6) -> 6231..6236
        6231 to Verse(
            6231, 114, 1,
            "قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
            "Say, \"I seek refuge in the Lord of mankind,",
            "Qul a'udhu bi-rabbi n-nas",
            audioUrlFor(114, 1)
        ),
        6232 to Verse(
            6232, 114, 2,
            "مَلِكِ النَّاسِ",
            "The Sovereign of mankind,",
            "Maliki n-nas",
            audioUrlFor(114, 2)
        ),
        6233 to Verse(
            6233, 114, 3,
            "إِلَٰهِ النَّاسِ",
            "The God of mankind,",
            "Ilahi n-nas",
            audioUrlFor(114, 3)
        ),
        6234 to Verse(
            6234, 114, 4,
            "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ",
            "From the evil of the retreating whisperer -",
            "Min sharri l-waswasi l-khannas",
            audioUrlFor(114, 4)
        ),
        6235 to Verse(
            6235, 114, 5,
            "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ",
            "Who whispers into the breasts of mankind -",
            "Al-ladhi yuwaswisu fi suduri n-nas",
            audioUrlFor(114, 5)
        ),
        6236 to Verse(
            6236, 114, 6,
            "مِنَ الْجِنَّةِ وَالنَّاسِ",
            "From among the jinn and mankind.\"",
            "Mina l-jinnati wa-n-nas",
            audioUrlFor(114, 6)
        ),

        // Al-Kawthar (108:1 - 108:3) -> 6205..6207
        6205 to Verse(
            6205, 108, 1,
            "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ",
            "Indeed, We have granted you, [O Muhammad], al-Kawthar.",
            "Inna a'taynaka l-kawthar",
            audioUrlFor(108, 1)
        ),
        6206 to Verse(
            6206, 108, 2,
            "فَصَلِّ لِرَبِّكَ وَانْحَرْ",
            "So pray to your Lord and sacrifice [to Him alone].",
            "Fa-salli li-rabbika wa-nhar",
            audioUrlFor(108, 2)
        ),
        6207 to Verse(
            6207, 108, 3,
            "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ",
            "Indeed, your enemy is the one cut off.",
            "Inna shani'aka huwa l-abtar",
            audioUrlFor(108, 3)
        ),

        // Al-Asr (103:1 - 103:3) -> 6177..6179
        6177 to Verse(
            6177, 103, 1,
            "وَالْعَصْرِ",
            "By time,",
            "Wa-l-'asr",
            audioUrlFor(103, 1)
        ),
        6178 to Verse(
            6178, 103, 2,
            "إِنَّ الْإِنسَانَ لَفِي خُسْرٍ",
            "Indeed, mankind is in loss,",
            "Inna l-insana lafi khusr",
            audioUrlFor(103, 2)
        ),
        6179 to Verse(
            6179, 103, 3,
            "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ",
            "Except for those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.",
            "Illa l-ladhina amanu wa-'amilu s-salihati wa-tawasaw bi-l-haqqi wa-tawasaw bi-s-sabr",
            audioUrlFor(103, 3)
        ),

        // Al-Mulk (67:1 - 67:3) -> 5242..5244
        5242 to Verse(
            5242, 67, 1,
            "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
            "Blessed is He in whose hand is dominion, and He is over all things competent -",
            "Tabaraka l-ladhi bi-yadihi l-mulku wa-huwa 'ala kulli shay'in qadir",
            audioUrlFor(67, 1)
        ),
        5243 to Verse(
            5243, 67, 2,
            "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
            "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -",
            "Al-ladhi khalaqa l-mawta wa-l-hayata li-yabluwakum ayyukum ahsanu 'amala",
            audioUrlFor(67, 2)
        )
    )

    fun getVerse(globalIndex: Int): Verse {
        val clamped = globalIndex.coerceIn(1, SurahRegistry.TOTAL_QURAN_VERSES)
        staticVerses[clamped]?.let { return it }

        val (surah, ayah) = SurahRegistry.getSurahAndAyahFromGlobalIndex(clamped)
        return Verse(
            globalIndex = clamped,
            surahNumber = surah.number,
            ayahNumber = ayah,
            textArabic = generateVerseArabicPlaceholder(surah.nameArabic, ayah),
            translationEnglish = "Recitation of Surah ${surah.nameEnglish}, Verse $ayah. [Recite with mindfulness and reflection].",
            transliteration = "${surah.nameEnglish} : Ayah $ayah",
            audioUrl = audioUrlFor(surah.number, ayah)
        )
    }

    fun getVersesForSurah(surahNumber: Int): List<Verse> {
        val surah = SurahRegistry.getSurah(surahNumber)
        val start = surah.globalStartVerse
        val end = start + surah.totalVerses - 1
        return (start..end).map { getVerse(it) }
    }

    private fun generateVerseArabicPlaceholder(surahNameArabic: String, ayah: Int): String {
        return "﴿ $surahNameArabic • آية $ayah ﴾"
    }

    fun audioUrlFor(surahNumber: Int, ayahNumber: Int): String {
        return String.format(
            "https://everyayah.com/data/Alafasy_128kbps/%03d%03d.mp3",
            surahNumber,
            ayahNumber
        )
    }
}
