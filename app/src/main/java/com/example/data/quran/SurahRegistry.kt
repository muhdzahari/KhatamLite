package com.example.data.quran

import com.example.data.model.SurahInfo

object SurahRegistry {

    val surahs: List<SurahInfo> = listOf(
        SurahInfo(1, "الفَاتِحَة", "Al-Fatihah", "The Opening", 7, "Meccan", 1, 1),
        SurahInfo(2, "البَقَرَة", "Al-Baqarah", "The Cow", 286, "Medinan", 1, 8),
        SurahInfo(3, "آل عِمْرَان", "Ali 'Imran", "Family of Imran", 200, "Medinan", 3, 294),
        SurahInfo(4, "النِّسَاء", "An-Nisa", "The Women", 176, "Medinan", 4, 494),
        SurahInfo(5, "المَائِدَة", "Al-Ma'idah", "The Table Spread", 120, "Medinan", 6, 670),
        SurahInfo(6, "الأَنْعَام", "Al-An'am", "The Cattle", 165, "Meccan", 7, 790),
        SurahInfo(7, "الأَعْرَاف", "Al-A'raf", "The Heights", 206, "Meccan", 8, 955),
        SurahInfo(8, "الأَنْفَال", "Al-Anfal", "The Spoils of War", 75, "Medinan", 9, 1161),
        SurahInfo(9, "التَّوْبَة", "At-Tawbah", "The Repentance", 129, "Medinan", 10, 1236),
        SurahInfo(10, "يُونُس", "Yunus", "Jonah", 109, "Meccan", 11, 1365),
        SurahInfo(11, "هُود", "Hud", "Hud", 123, "Meccan", 11, 1474),
        SurahInfo(12, "يُوسُف", "Yusuf", "Joseph", 111, "Meccan", 12, 1597),
        SurahInfo(13, "الرَّعْد", "Ar-Ra'd", "The Thunder", 43, "Medinan", 13, 1708),
        SurahInfo(14, "إِبْرَاهِيم", "Ibrahim", "Abraham", 52, "Meccan", 13, 1751),
        SurahInfo(15, "الحِجْر", "Al-Hijr", "The Rocky Tract", 99, "Meccan", 14, 1803),
        SurahInfo(16, "النَّحْل", "An-Nahl", "The Bee", 128, "Meccan", 14, 1902),
        SurahInfo(17, "الإِسْرَاء", "Al-Isra", "The Night Journey", 111, "Meccan", 15, 2030),
        SurahInfo(18, "الكَهْف", "Al-Kahf", "The Cave", 110, "Meccan", 15, 2141),
        SurahInfo(19, "مَرْيَم", "Maryam", "Mary", 98, "Meccan", 16, 2251),
        SurahInfo(20, "طه", "Ta-Ha", "Ta-Ha", 135, "Meccan", 16, 2349),
        SurahInfo(21, "الأَنْبِيَاء", "Al-Anbiya", "The Prophets", 112, "Meccan", 17, 2484),
        SurahInfo(22, "الحَجّ", "Al-Hajj", "The Pilgrimage", 78, "Medinan", 17, 2596),
        SurahInfo(23, "المُؤْمِنُون", "Al-Mu'minun", "The Believers", 118, "Meccan", 18, 2674),
        SurahInfo(24, "النُّور", "An-Nur", "The Light", 64, "Medinan", 18, 2792),
        SurahInfo(25, "الفُرْقَان", "Al-Furqan", "The Criterion", 77, "Meccan", 18, 2856),
        SurahInfo(26, "الشُّعَرَاء", "Ash-Shu'ara", "The Poets", 227, "Meccan", 19, 2933),
        SurahInfo(27, "النَّمْل", "An-Naml", "The Ant", 93, "Meccan", 19, 3160),
        SurahInfo(28, "القَصَص", "Al-Qasas", "The Stories", 88, "Meccan", 20, 3253),
        SurahInfo(29, "العَنْكَبُوت", "Al-'Ankabut", "The Spider", 69, "Meccan", 20, 3341),
        SurahInfo(30, "الرُّوم", "Ar-Rum", "The Romans", 60, "Meccan", 21, 3410),
        SurahInfo(31, "لُقْمَان", "Luqman", "Luqman", 34, "Meccan", 21, 3470),
        SurahInfo(32, "السَّجْدَة", "As-Sajdah", "The Prostration", 30, "Meccan", 21, 3504),
        SurahInfo(33, "الأَحْزَاب", "Al-Ahzab", "The Combined Forces", 73, "Medinan", 21, 3534),
        SurahInfo(34, "سَبَأ", "Saba", "Sheba", 54, "Meccan", 22, 3607),
        SurahInfo(35, "فَاطِر", "Fatir", "Originator", 45, "Meccan", 22, 3661),
        SurahInfo(36, "يس", "Ya-Sin", "Ya-Sin", 83, "Meccan", 22, 3706),
        SurahInfo(37, "الصَّافَّات", "As-Saffat", "Those Who Set The Ranks", 182, "Meccan", 23, 3789),
        SurahInfo(38, "ص", "Sad", "The Letter Sad", 88, "Meccan", 23, 3971),
        SurahInfo(39, "الزُّمَر", "Az-Zumar", "The Troops", 75, "Meccan", 23, 4059),
        SurahInfo(40, "غَافِر", "Ghafir", "The Forgiver", 85, "Meccan", 24, 4134),
        SurahInfo(41, "فُصِّلَت", "Fussilat", "Explained in Detail", 54, "Meccan", 24, 4219),
        SurahInfo(42, "الشُّورَى", "Ash-Shura", "The Consultation", 53, "Meccan", 25, 4273),
        SurahInfo(43, "الزُّخْرُف", "Az-Zukhruf", "The Ornaments of Gold", 89, "Meccan", 25, 4326),
        SurahInfo(44, "الدُّخَان", "Ad-Dukhan", "The Smoke", 59, "Meccan", 25, 4415),
        SurahInfo(45, "الجَاثِيَة", "Al-Jathiyah", "The Crouching", 37, "Meccan", 25, 4474),
        SurahInfo(46, "الأَحْقَاف", "Al-Ahqaf", "The Wind-Curved Sandhills", 35, "Meccan", 26, 4511),
        SurahInfo(47, "مُحَمَّد", "Muhammad", "Muhammad", 38, "Medinan", 26, 4546),
        SurahInfo(48, "الفَتْح", "Al-Fath", "The Victory", 29, "Medinan", 26, 4584),
        SurahInfo(49, "الحُجُرَات", "Al-Hujurat", "The Rooms", 18, "Medinan", 26, 4613),
        SurahInfo(50, "ق", "Qaf", "The Letter Qaf", 45, "Meccan", 26, 4631),
        SurahInfo(51, "الذَّارِيَات", "Adh-Dhariyat", "The Winnowing Winds", 60, "Meccan", 26, 4676),
        SurahInfo(52, "الطُّور", "At-Tur", "The Mount", 49, "Meccan", 27, 4736),
        SurahInfo(53, "النَّجْم", "An-Najm", "The Star", 62, "Meccan", 27, 4785),
        SurahInfo(54, "القَمَر", "Al-Qamar", "The Moon", 55, "Meccan", 27, 4847),
        SurahInfo(55, "الرَّحْمَن", "Ar-Rahman", "The Beneficent", 78, "Medinan", 27, 4902),
        SurahInfo(56, "الوَاقِعَة", "Al-Waqi'ah", "The Inevitable", 96, "Meccan", 27, 4980),
        SurahInfo(57, "الحَدِيد", "Al-Hadid", "The Iron", 29, "Medinan", 27, 5076),
        SurahInfo(58, "المُجَادِلَة", "Al-Mujadila", "The Pleading Woman", 22, "Medinan", 28, 5105),
        SurahInfo(59, "الحَشْر", "Al-Hashr", "The Exile", 24, "Medinan", 28, 5127),
        SurahInfo(60, "المُمْتَحَنَة", "Al-Mumtahanah", "She That Is To Be Examined", 13, "Medinan", 28, 5151),
        SurahInfo(61, "الصَّفّ", "As-Saff", "The Ranks", 14, "Medinan", 28, 5164),
        SurahInfo(62, "الجُمُعَة", "Al-Jumu'ah", "The Congregation, Friday", 11, "Medinan", 28, 5178),
        SurahInfo(63, "المُنَافِقُون", "Al-Munafiqun", "The Hypocrites", 11, "Medinan", 28, 5189),
        SurahInfo(64, "التَّغَابُن", "At-Taghabun", "The Mutual Disillusion", 18, "Medinan", 28, 5200),
        SurahInfo(65, "الطَّلَاق", "At-Talaq", "The Divorce", 12, "Medinan", 28, 5218),
        SurahInfo(66, "التَّحْرِيم", "At-Tahrim", "The Prohibition", 12, "Medinan", 28, 5230),
        SurahInfo(67, "المُلْك", "Al-Mulk", "The Sovereignty", 30, "Meccan", 29, 5242),
        SurahInfo(68, "القَلَم", "Al-Qalam", "The Pen", 52, "Meccan", 29, 5272),
        SurahInfo(69, "الحَاقَّة", "Al-Haqqah", "The Reality", 52, "Meccan", 29, 5324),
        SurahInfo(70, "المَعَارِج", "Al-Ma'arij", "The Ascending Stairways", 44, "Meccan", 29, 5376),
        SurahInfo(71, "نُوح", "Nuh", "Noah", 28, "Meccan", 29, 5420),
        SurahInfo(72, "الجِنّ", "Al-Jinn", "The Jinn", 28, "Meccan", 29, 5448),
        SurahInfo(73, "المُزَّمِّل", "Al-Muzzammil", "The Enshrouded One", 20, "Meccan", 29, 5476),
        SurahInfo(74, "المُدَّثِّر", "Al-Muddaththir", "The Cloaked One", 56, "Meccan", 29, 5496),
        SurahInfo(75, "القِيَامَة", "Al-Qiyamah", "The Resurrection", 40, "Meccan", 29, 5552),
        SurahInfo(76, "الإِنْسَان", "Al-Insan", "Man", 31, "Medinan", 29, 5592),
        SurahInfo(77, "المُرْسَلَات", "Al-Mursalat", "The Emissaries", 50, "Meccan", 29, 5623),
        SurahInfo(78, "النَّبَأ", "An-Naba", "The Tidings", 40, "Meccan", 30, 5673),
        SurahInfo(79, "النَّازِعَات", "An-Nazi'at", "Those Who Drag Forth", 46, "Meccan", 30, 5713),
        SurahInfo(80, "عَبَسَ", "'Abasa", "He Frowned", 42, "Meccan", 30, 5759),
        SurahInfo(81, "التَّكْوِير", "At-Takwir", "The Overthrowing", 29, "Meccan", 30, 5801),
        SurahInfo(82, "الانْفِطَار", "Al-Infitar", "The Cleaving", 19, "Meccan", 30, 5830),
        SurahInfo(83, "المُطَفِّفِين", "Al-Mutaffifin", "Defrauding", 36, "Meccan", 30, 5849),
        SurahInfo(84, "الانْشِقَاق", "Al-Inshiqaq", "The Splitting Open", 25, "Meccan", 30, 5885),
        SurahInfo(85, "البُرُوج", "Al-Buruj", "The Mansions of the Stars", 22, "Meccan", 30, 5910),
        SurahInfo(86, "الطَّارِق", "At-Tariq", "The Morning Star", 17, "Meccan", 30, 5932),
        SurahInfo(87, "الأَعْلَى", "Al-A'la", "The Most High", 19, "Meccan", 30, 5949),
        SurahInfo(88, "الغَاشِيَة", "Al-Ghashiyah", "The Overwhelming Event", 26, "Meccan", 30, 5968),
        SurahInfo(89, "الفَجْر", "Al-Fajr", "The Dawn", 30, "Meccan", 30, 5994),
        SurahInfo(90, "البَلَد", "Al-Balad", "The City", 20, "Meccan", 30, 6024),
        SurahInfo(91, "الشَّمْس", "Ash-Shams", "The Sun", 15, "Meccan", 30, 6044),
        SurahInfo(92, "اللَّيْل", "Al-Layl", "The Night", 21, "Meccan", 30, 6059),
        SurahInfo(93, "الضُّحَى", "Ad-Duha", "The Morning Hours", 11, "Meccan", 30, 6080),
        SurahInfo(94, "الشَّرْح", "Ash-Sharh", "The Relief", 8, "Meccan", 30, 6091),
        SurahInfo(95, "التِّين", "At-Tin", "The Fig", 8, "Meccan", 30, 6099),
        SurahInfo(96, "العَلَق", "Al-'Alaq", "The Clot", 19, "Meccan", 30, 6107),
        SurahInfo(97, "القَدْر", "Al-Qadr", "The Power, Fate", 5, "Meccan", 30, 6126),
        SurahInfo(98, "البَيِّنَة", "Al-Bayyinah", "The Clear Proof", 8, "Medinan", 30, 6131),
        SurahInfo(99, "الزَّلْزَلَة", "Az-Zalzalah", "The Earthquake", 8, "Medinan", 30, 6139),
        SurahInfo(100, "العَادِيَات", "Al-'Adiyat", "The Courser", 11, "Meccan", 30, 6147),
        SurahInfo(101, "القَارِعَة", "Al-Qari'ah", "The Calamity", 11, "Meccan", 30, 6158),
        SurahInfo(102, "التَّكَاثُر", "At-Takathur", "The Rivalry in World Increase", 8, "Meccan", 30, 6169),
        SurahInfo(103, "العَصْر", "Al-'Asr", "The Declining Day, Epoch", 3, "Meccan", 30, 6177),
        SurahInfo(104, "الهُمَزَة", "Al-Humazah", "The Traducer", 9, "Meccan", 30, 6180),
        SurahInfo(105, "الفِيل", "Al-Fil", "The Elephant", 5, "Meccan", 30, 6189),
        SurahInfo(106, "قُرَيْش", "Quraysh", "Quraysh", 4, "Meccan", 30, 6194),
        SurahInfo(107, "المَاعُون", "Al-Ma'un", "Small Kindness", 7, "Meccan", 30, 6198),
        SurahInfo(108, "الكَوْثَر", "Al-Kawthar", "Abundance", 3, "Meccan", 30, 6205),
        SurahInfo(109, "الكَافِرُون", "Al-Kafirun", "The Disbelievers", 6, "Meccan", 30, 6208),
        SurahInfo(110, "النَّصْر", "An-Nasr", "Divine Support", 3, "Medinan", 30, 6214),
        SurahInfo(111, "المَسَد", "Al-Masad", "The Palm Fibre", 5, "Meccan", 30, 6217),
        SurahInfo(112, "الإِخْلَاص", "Al-Ikhlas", "The Sincerity", 4, "Meccan", 30, 6222),
        SurahInfo(113, "الفَلَق", "Al-Falaq", "The Daybreak", 5, "Meccan", 30, 6226),
        SurahInfo(114, "النَّاس", "An-Nas", "Mankind", 6, "Meccan", 30, 6231)
    )

    const val TOTAL_QURAN_VERSES = 6236

    fun getSurah(number: Int): SurahInfo {
        return surahs.getOrNull(number - 1) ?: surahs.first()
    }

    fun getSurahAndAyahFromGlobalIndex(globalIndex: Int): Pair<SurahInfo, Int> {
        val clampedIndex = globalIndex.coerceIn(1, TOTAL_QURAN_VERSES)
        for (i in surahs.indices.reversed()) {
            val surah = surahs[i]
            if (clampedIndex >= surah.globalStartVerse) {
                val ayah = clampedIndex - surah.globalStartVerse + 1
                return Pair(surah, ayah)
            }
        }
        return Pair(surahs.first(), 1)
    }

    fun getGlobalIndex(surahNumber: Int, ayahNumber: Int): Int {
        val surah = getSurah(surahNumber)
        val clampedAyah = ayahNumber.coerceIn(1, surah.totalVerses)
        return surah.globalStartVerse + clampedAyah - 1
    }

    fun formatVerseReference(globalIndex: Int): String {
        val (surah, ayah) = getSurahAndAyahFromGlobalIndex(globalIndex)
        return "${surah.nameEnglish} $ayah (${surah.nameArabic})"
    }

    fun formatVerseRange(startGlobal: Int, endGlobal: Int): String {
        val (startSurah, startAyah) = getSurahAndAyahFromGlobalIndex(startGlobal)
        val (endSurah, endAyah) = getSurahAndAyahFromGlobalIndex(endGlobal)
        return if (startSurah.number == endSurah.number) {
            if (startAyah == endAyah) {
                "${startSurah.nameEnglish} : $startAyah"
            } else {
                "${startSurah.nameEnglish} : $startAyah–$endAyah"
            }
        } else {
            "${startSurah.nameEnglish}:$startAyah – ${endSurah.nameEnglish}:$endAyah"
        }
    }
}
