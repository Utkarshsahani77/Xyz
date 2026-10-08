package com.example.data

import com.example.model.Album
import com.example.model.AmbientSound
import com.example.model.LyricLine
import com.example.model.Playlist
import com.example.model.RadioStation
import com.example.model.Song

object CuratedMusicData {

    // Ambient Soundscapes
    val ambientSounds: List<AmbientSound> = listOf(
        AmbientSound(
            id = "ambient_monsoon",
            name = "Monsoon Rain",
            hindiName = "रिमझिम बारिश",
            iconEmoji = "🌧️",
            audioUrl = "https://actions.google.com/sounds/v1/weather/rain_heavy.ogg"
        ),
        AmbientSound(
            id = "ambient_himalaya",
            name = "Himalayan Breeze",
            hindiName = "हिमालयन बयार",
            iconEmoji = "🏔️",
            audioUrl = "https://actions.google.com/sounds/v1/weather/wind_howling.ogg"
        ),
        AmbientSound(
            id = "ambient_temple",
            name = "Temple Chimes",
            hindiName = "मंदिर की घंटियाँ",
            iconEmoji = "🔔",
            audioUrl = "https://actions.google.com/sounds/v1/foley/wind_chimes.ogg"
        ),
        AmbientSound(
            id = "ambient_ganga",
            name = "Ganga Stream",
            hindiName = "गंगा तट धारा",
            iconEmoji = "🌊",
            audioUrl = "https://actions.google.com/sounds/v1/water/stream_water.ogg"
        ),
        AmbientSound(
            id = "ambient_bonfire",
            name = "Night Bonfire",
            hindiName = "अलाव की रात",
            iconEmoji = "🔥",
            audioUrl = "https://actions.google.com/sounds/v1/ambiences/outdoor_night.ogg"
        )
    )

    // Hindi Synced Lyrics with Dual Devanagari & Romanized Hindi
    val lyricsKesariya = listOf(
        LyricLine(0L, "♪ (बांसुरी और एकॉस्टिक गिटार आलाप • Spatial Audio) ♪", "♪ (Bansuri & Acoustic Guitar Intro) ♪"),
        LyricLine(3500L, "केसरिया तेरा इश्क़ है पिया", "Kesariya tera ishq hai piya"),
        LyricLine(7800L, "रंग जाऊं जो मैं हाथ लगाऊं", "Rang jaaun jo main hath lagaun"),
        LyricLine(12000L, "दिन बीते सारा तेरी फ़िक्र में", "Din beete saara teri fikr mein"),
        LyricLine(16500L, "रैन सारी तेरे नाम बिताऊं", "Rain saari tere naam bitaun"),
        LyricLine(21000L, "केसरिया तेरा इश्क़ है पिया", "Kesariya tera ishq hai piya"),
        LyricLine(25500L, "रंग जाऊं जो मैं हाथ लगाऊं", "Rang jaaun jo main hath lagaun"),
        LyricLine(29000L, "♪ (सितार और तबले की दिलकश जुगलबंदी) ♪", "♪ (Sitar & Tabla Jugalbandi Outro) ♪")
    )

    val lyricsRaataanLambiyan = listOf(
        LyricLine(0L, "♪ (रूहानी एकॉस्टिक गिटार इंट्रो • Utkarsh Master) ♪", "♪ (Soulful Acoustic Guitar Intro) ♪"),
        LyricLine(4000L, "काटूं कैसे रातां ओ सांवरे", "Kaatun kaise raataan o saanwre"),
        LyricLine(8500L, "जिया नहीं जाता सुन बावरे", "Jiya nahi jaata sun baawre"),
        LyricLine(13000L, "के रातां लम्बियां लम्बियां रे", "Ke raataan lambiyan lambiyan re"),
        LyricLine(17500L, "कटे तेरे संग संग रे", "Kate tere sang sang re"),
        LyricLine(21500L, "के रातां लम्बियां लम्बियां रे", "Ke raataan lambiyan lambiyan re"),
        LyricLine(26000L, "कटे तेरे संग संग रे", "Kate tere sang sang re")
    )

    val lyricsTumHiHo = listOf(
        LyricLine(0L, "♪ (पियानो का दर्द भरा मेलोडी आलाप • Hi-Res Lossless) ♪", "♪ (Iconic Emotional Piano Intro) ♪"),
        LyricLine(4500L, "क्योंकि तुम ही हो, अब तुम ही हो", "Kyunki tum hi ho, ab tum hi ho"),
        LyricLine(9500L, "ज़िन्दगी अब तुम ही हो", "Zindagi ab tum hi ho"),
        LyricLine(14500L, "चैन भी, मेरा दर्द भी", "Chain bhi, mera dard bhi"),
        LyricLine(19500L, "मेरी आशिक़ी अब तुम ही हो", "Meri aashiqui ab tum hi ho"),
        LyricLine(25000L, "तेरा मेरा रिश्ता है कैसा, इक पल दूर गवारा नहीं", "Tera mera rishta hai kaisa, ik pal door gavara nahi")
    )

    val lyricsChannaMereya = listOf(
        LyricLine(0L, "♪ (शहनाई और एकॉस्टिक गिटार इंट्रो • Spatial Audio) ♪", "♪ (Shehnai & Acoustic Guitar Intro) ♪"),
        LyricLine(4000L, "ओ पिया...", "O piya..."),
        LyricLine(8000L, "चन्ना मेरेया मेरेया, चन्ना मेरेया मेरेया", "Channa mereya mereya, channa mereya mereya"),
        LyricLine(14500L, "चन्ना मेरेया मेरेया बेलिया, ओ पिया...", "Channa mereya mereya beliya, o piya..."),
        LyricLine(21000L, "महफ़िल में तेरी, हम ना रहे जो", "Mehfil mein teri, hum na rahe jo"),
        LyricLine(26500L, "ग़म तो नहीं है, ग़म तो नहीं है", "Gham toh nahi hai, gham toh nahi hai")
    )

    val lyricsKunFayaKun = listOf(
        LyricLine(0L, "♪ (सूफी तानपूरा और हारमोनियम आलाप • A.R. Rahman) ♪", "♪ (Sufi Tanpura & Harmonium Alaap) ♪"),
        LyricLine(5000L, "जब कहीं पे कुछ नहीं भी नहीं था", "Jab kahin pe kuch nahi bhi nahi tha"),
        LyricLine(10500L, "वही था, वही था, वही था", "Wahi tha, wahi tha, wahi tha"),
        LyricLine(16000L, "कुन फाया कुन, कुन फाया कुन", "Kun faya kun, kun faya kun"),
        LyricLine(22000L, "मन के मेरे ये भरम, कच्चे मेरे ये करम", "Mann ke mere yeh bharam, kache mere yeh karam"),
        LyricLine(27000L, "तू ही मेरे दिल का सुकून, कुन फाया कुन...", "Tu hi mere dil ka sukoon, kun faya kun...")
    )

    val lyricsBaarishein = listOf(
        LyricLine(0L, "♪ (एकॉस्टिक गिटार और बारिश की बूंदें • Indie) ♪", "♪ (Acoustic Guitar & Raindrops Intro) ♪"),
        LyricLine(4500L, "हवाएं बदल रहीं हैं, रुख अपना", "Hawayein badal rahi hain, rukh apna"),
        LyricLine(9500L, "जैसे तुम बदल गए हो", "Jaise tum badal gaye ho"),
        LyricLine(14500L, "ये बारिशें फिर से याद दिलाती हैं", "Yeh baarishein phir se yaad dilaati hain"),
        LyricLine(20500L, "तेरी वो भीगी बातें, सुहानी रातें", "Teri woh bheegi baatein, suhani raatein"),
        LyricLine(26000L, "खिड़की पे बैठ कर चाय की चुस्की...", "Khidki pe baith kar chai ki chuski...")
    )

    val lyricsDilDiyanGallan = listOf(
        LyricLine(0L, "♪ (रूमानियत से भरी गिटार धुन • Yash Raj Music) ♪", "♪ (Romantic Acoustic Strum Intro) ♪"),
        LyricLine(4000L, "दिल दियां गल्लां, करेंगे नाल नाल बह के", "Dil diyan gallan, karenge naal naal beh ke"),
        LyricLine(9000L, "अंख नाले अंख नू मिला के", "Ankh naale ankh nu mila ke"),
        LyricLine(14500L, "रोज़ वंडेंगे तेरे नाल हस के", "Roz vandenge तेरे naal has ke"),
        LyricLine(20000L, "दिल दियां गल्लां, करेंगे नाल नाल बह के", "Dil diyan gallan, karenge naal naal beh ke"),
        LyricLine(26000L, "सच्चियां मोहब्बतां निभा के...", "Sachiyaan mohabbataan nibha ke...")
    )

    val lyricsLover = listOf(
        LyricLine(0L, "♪ (धमाकेदार पॉप और सिंथ बीट • Diljit Dosanjh) ♪", "♪ (Upbeat Punjabi Pop Synth Intro) ♪"),
        LyricLine(4000L, "तेरा नी मैं, तेरा नी मैं लवर", "Tera ni main, tera ni main lover"),
        LyricLine(8500L, "कोई शक ना करीं, मेरे प्यार ते", "Koi shak na kareen, mere pyaar te"),
        LyricLine(13500L, "तेरा नी मैं, तेरा नी मैं लवर", "Tera ni main, tera ni main lover"),
        LyricLine(18500L, "रंग तेरा सोहणेया, दिल मेरा मोहणेया", "Rang tera sohneya, dil mera mohneya"),
        LyricLine(24000L, "दिलजीत दोसांझ वाइब... उत्कर्ष म्यूज़िक", "Diljit Dosanjh Vibe... Utkarsh Music")
    )

    val lyricsIlahi = listOf(
        LyricLine(0L, "♪ (सफर की गिटार और ड्रम बीट • Travel Vibes) ♪", "♪ (Travel Acoustic Guitar Strum) ♪"),
        LyricLine(4000L, "शामें मलंग सी, रातें सुरंग सी", "Shaamein malang si, raatein surang si"),
        LyricLine(9000L, "बागी उड़ान पे ही ना जाने क्यूँ", "Baaghi udaan pe hi na jaane kyun"),
        LyricLine(14500L, "इलाही मेरा जी आए आए", "Ilahi mera jee aaye aaye"),
        LyricLine(19500L, "इलाही मेरा जी आए आए", "Ilahi mera jee aaye aaye"),
        LyricLine(25000L, "कल पे सवाल है, जीना फ़िलहाल है...", "Kal pe sawaal hai, jeena filhaal hai...")
    )

    val lyricsMastMagan = listOf(
        LyricLine(0L, "♪ (ढोलक और एकॉस्टिक गिटार तान • 2 States) ♪", "♪ (Soulful Dholak & Guitar Intro) ♪"),
        LyricLine(4000L, "इश्क़ की धुन में ऐसा रंगा", "Ishq ki dhun mein aisa ranga"),
        LyricLine(9000L, "मन मस्त मगन, मन मस्त मगन", "Mann mast magan, mann mast magan"),
        LyricLine(14000L, "बस तेरा नाम दोहराए", "Bas tera naam dohraaye"),
        LyricLine(19500L, "मन मस्त मगन, मन मस्त मगन", "Mann mast magan, mann mast magan"),
        LyricLine(25000L, "चाहे जो भी कहे ये ज़माना...", "Chaahe jo bhi kahe yeh zamaana...")
    )

    // Global Songs Lyrics
    val lyricsLevitating = listOf(
        LyricLine(0L, "♪ (Funky Disco Bassline & Claps • Spatial Audio) ♪", "♪ (Intro) ♪"),
        LyricLine(3500L, "If you wanna run away with me, I know a galaxy", "If you wanna run away with me"),
        LyricLine(7500L, "And I can take you for a ride", "And I can take you for a ride"),
        LyricLine(11500L, "I had a premonition that we fell into a rhythm", "Fell into a rhythm"),
        LyricLine(16000L, "Where the music don't stop for life", "Music don't stop for life"),
        LyricLine(20500L, "You want me, I want you, baby", "You want me, I want you, baby"),
        LyricLine(24000L, "My sugarboo, I'm levitating!", "My sugarboo, I'm levitating!"),
        LyricLine(28000L, "♪ (The Milky Way, we're re-negading) ♪", "♪ (Disco Drop) ♪")
    )

    val lyricsAsItWas = listOf(
        LyricLine(0L, "♪ (80s Synth Bell Intro • Utkarsh Master) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "Hold on, the bell is ringing", "Hold on, the bell is ringing"),
        LyricLine(8500L, "In this world, it's just us", "In this world, it's just us"),
        LyricLine(13000L, "You know it's not the same as it was", "You know it's not the same as it was"),
        LyricLine(17500L, "In this world, it's just us", "In this world, it's just us"),
        LyricLine(22000L, "You know it's not the same as it was", "You know it's not the same as it was"),
        LyricLine(26000L, "As it was, as it was...", "As it was, as it was...")
    )

    val lyricsCruelSummer = listOf(
        LyricLine(0L, "♪ (Ethereal Synth Pad & Vocoder) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "Fever dream high in the quiet of the night", "Fever dream high"),
        LyricLine(8500L, "You know that I caught it", "You know that I caught it"),
        LyricLine(13000L, "Bad, bad boy, shiny toy with a price", "Shiny toy with a price"),
        LyricLine(17500L, "You know that I bought it", "You know that I bought it"),
        LyricLine(21500L, "And it's new, the shape of your body", "The shape of your body"),
        LyricLine(26000L, "It's blue, the feeling I've got...", "It's a cruel summer...")
    )

    val lyricsShapeOfYou = listOf(
        LyricLine(0L, "♪ (Marimba Loop & Acoustic Percussion) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "The club isn't the best place to find a lover", "The club isn't the best place"),
        LyricLine(8000L, "So the bar is where I go", "So the bar is where I go"),
        LyricLine(12000L, "Me and my friends at the table doing shots", "Drinking fast and we talk slow"),
        LyricLine(17000L, "Come over and start up a conversation with just me", "And trust me I'll give it a chance"),
        LyricLine(21000L, "I'm in love with the shape of you", "I'm in love with the shape of you"),
        LyricLine(25500L, "We push and pull like a magnet do...", "I'm in love with your body...")
    )

    val lyricsBirdsOfAFeather = listOf(
        LyricLine(0L, "♪ (Warm Vintage Lo-Fi Guitar Chords) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "I want you to stay, 'til I'm in the grave", "I want you to stay"),
        LyricLine(9500L, "'Til I rot away, dead and buried", "'Til I rot away"),
        LyricLine(15000L, "'Til I'm in the casket you carry", "In the casket you carry"),
        LyricLine(20500L, "Birds of a feather, we should stick together", "Stick together"),
        LyricLine(26000L, "I love you 'til the day that I die...", "I love you 'til the day that I die...")
    )

    val lyricsSunflower = listOf(
        LyricLine(0L, "♪ (Melodic 808 & Sweet Chimes • Spider-Man) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "Needless to say, I keep in check", "Needless to say, I keep in check"),
        LyricLine(8000L, "She was a bad-bad, nevertheless", "She was a bad-bad, nevertheless"),
        LyricLine(12500L, "Callin' it quits now, baby, I'm a wreck", "Baby, I'm a wreck"),
        LyricLine(17000L, "Crash at my place, baby, you're a wreck", "Baby, you're a wreck"),
        LyricLine(21000L, "Then you're left in the dust", "Left in the dust"),
        LyricLine(25500L, "Unless I stuck by ya, you're a sunflower!", "You're a sunflower!")
    )

    val lyricsMyUniverse = listOf(
        LyricLine(0L, "♪ (Cosmic EDM Synth Intro • Spatial Audio) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "You, you are my universe", "You are my universe"),
        LyricLine(8500L, "And I just want to put you first", "Put you first"),
        LyricLine(13000L, "And you, you are my universe", "You are my universe"),
        LyricLine(17500L, "And you make my world light up inside", "Light up inside"),
        LyricLine(22500L, "매일 밤 네게 날아가 (Flying to you every night)", "Flying to you every night"),
        LyricLine(27000L, "꿈이란 것도 잊은 채, 웃으며 너를 만나...", "Meeting you with a smile...")
    )

    val lyricsEasyOnMe = listOf(
        LyricLine(0L, "♪ (Grand Piano Emotional Chords) ♪", "♪ (Intro) ♪"),
        LyricLine(4500L, "There ain't no gold in this river", "There ain't no gold in this river"),
        LyricLine(9500L, "That I've been washin' my hands in forever", "Washin' my hands in forever"),
        LyricLine(15000L, "I know there is hope in these waters", "Hope in these waters"),
        LyricLine(20500L, "Go easy on me, baby!", "Go easy on me, baby!"),
        LyricLine(25000L, "I was still a child, didn't get the chance to...", "Go easy on me...")
    )

    val lyricsBeliever = listOf(
        LyricLine(0L, "♪ (Thunderous Drumbeat & Rhythm) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "First things first, I'ma say all the words inside my head", "First things first"),
        LyricLine(9000L, "I'm fired up and tired of the way that things have been", "Fired up and tired"),
        LyricLine(14500L, "Second thing second, don't you tell me what you think that I could be", "Second thing second"),
        LyricLine(20500L, "Pain! You made me a, you made me a believer, believer!", "You made me a believer!"),
        LyricLine(26000L, "Pain! You break me down and build me up, believer!", "Believer!")
    )

    val lyricsStarboy = listOf(
        LyricLine(0L, "♪ (Daft Punk Electro-Pop Synth & 808) ♪", "♪ (Intro) ♪"),
        LyricLine(4000L, "I'm tryna put you in the worst mood, ah", "Put you in the worst mood"),
        LyricLine(8500L, "P1 cleaner than your church shoes, ah", "Cleaner than your church shoes"),
        LyricLine(13000L, "Milli point two just to hurt you, ah", "Milli point two just to hurt you"),
        LyricLine(17500L, "All red Lamb' just to tease you, ah", "All red Lamb'"),
        LyricLine(21500L, "Look what you've done, I'm a motherfuckin' Starboy!", "I'm a starboy!"),
        LyricLine(26000L, "Look what you've done, I'm a starboy...", "Starboy...")
    )

    // Curated Indian & Bollywood Songs
    val curatedSongs: List<Song> = listOf(
        Song(
            id = "curated_1",
            title = "Kesariya (केसरिया)",
            artist = "Arijit Singh & Pritam",
            album = "Brahmāstra (ब्रह्मास्त्र)",
            durationMs = 268000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/9f/13/ca/9f13ca3b-e533-03e0-f19a-f0aaa774581d/196589311191.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/38/4c/5c/384c5c8f-3ff8-e457-b2f7-3158ce108649/mzaf_12389299033886433185.plus.aac.p.m4a",
            genre = "Bollywood Romantic",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsKesariya
        ),
        Song(
            id = "curated_2",
            title = "Raataan Lambiyan (रातां लम्बियां)",
            artist = "Jubin Nautiyal & Asees Kaur",
            album = "Shershaah (शेरशाह)",
            durationMs = 230000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/61/65/ae/6165aee9-8bb9-0bd4-02b0-5d0f1e6257a3/886449510238.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/99/0c/38/990c381b-0530-8c0d-87a9-18b050b97f0a/mzaf_10418866714500530894.plus.aac.p.m4a",
            genre = "Bollywood Love",
            audioQualityBadge = "UTKARSH MASTER",
            lyrics = lyricsRaataanLambiyan
        ),
        Song(
            id = "curated_3",
            title = "Tum Hi Ho (तुम ही हो)",
            artist = "Arijit Singh & Mithoon",
            album = "Aashiqui 2 (आशिकी २)",
            durationMs = 262000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/bb/23/ee/bb23eeed-0c35-4f1d-2b11-485622777ae4/8902894353007_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/3a/8c/9b/3a8c9b0b-2def-750a-f615-1555bf941edf/mzaf_17229496441442805917.plus.aac.p.m4a",
            genre = "Hindi Classic Love",
            audioQualityBadge = "HI-RES LOSSLESS",
            lyrics = lyricsTumHiHo
        ),
        Song(
            id = "curated_4",
            title = "Channa Mereya (चन्ना मेरेया)",
            artist = "Arijit Singh & Pritam",
            album = "Ae Dil Hai Mushkil",
            durationMs = 289000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/bc/6e/4d/bc6e4d0c-adec-b431-7b60-16f5689f9664/886446201597.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/d5/f9/98/d5f998a7-0090-ee2d-03f8-557ad6c5bf65/mzaf_14251357991592637728.plus.aac.p.m4a",
            genre = "Sufi / Soul",
            audioQualityBadge = "LOSSLESS",
            lyrics = lyricsChannaMereya
        ),
        Song(
            id = "curated_5",
            title = "Kun Faya Kun (कुन फाया कुन)",
            artist = "A.R. Rahman & Mohit Chauhan",
            album = "Rockstar (रॉकस्टार)",
            durationMs = 473000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/56/ac/41/56ac41f7-99f3-3eae-3b07-443167292c4e/8902894697408_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/ce/72/5e/ce725e63-6977-b281-3766-fff46d6e0c07/mzaf_7464998292185183397.plus.aac.p.m4a",
            genre = "Sufi Devotional",
            audioQualityBadge = "UTKARSH MASTER",
            lyrics = lyricsKunFayaKun
        ),
        Song(
            id = "curated_6",
            title = "Baarishein (बारिशें)",
            artist = "Anuv Jain",
            album = "Baarishein - Single",
            durationMs = 210000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/a7/46/2f/a7462f7e-6dde-b235-2f5e-90e31af919a7/7799192004134_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/22/a0/7d/22a07d46-b904-0717-95da-ec335aac6407/mzaf_13582924850644616784.plus.aac.p.m4a",
            genre = "Acoustic Hindi Indie",
            audioQualityBadge = "HI-RES LOSSLESS",
            lyrics = lyricsBaarishein
        ),
        Song(
            id = "curated_7",
            title = "Dil Diyan Gallan (दिल दियां गल्लां)",
            artist = "Atif Aslam & Vishal-Shekhar",
            album = "Tiger Zinda Hai",
            durationMs = 260000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/55/16/15/5516155a-733c-800c-1081-e5e982e8a603/849486091696_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/70/5a/67/705a67b0-a61f-db2c-bb6a-ab06c78a4386/mzaf_5706499000965104644.plus.aac.p.m4a",
            genre = "Bollywood Ballad",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsDilDiyanGallan
        ),
        Song(
            id = "curated_8",
            title = "Lover (पंजाबी वाइब)",
            artist = "Diljit Dosanjh & Intense",
            album = "MoonChild Era",
            durationMs = 190000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/8a/89/e4/8a89e445-d2c6-f8ac-a828-27818b0c1afe/859749638209_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/38/d5/7a/38d57a99-39fc-e901-7c45-fa6260ec83c1/mzaf_8083696285926392389.plus.aac.p.m4a",
            genre = "Punjabi Pop",
            audioQualityBadge = "LOSSLESS",
            lyrics = lyricsLover
        ),
        Song(
            id = "curated_9",
            title = "Ilahi (इलाही)",
            artist = "Arijit Singh",
            album = "Yeh Jawaani Hai Deewani",
            durationMs = 228000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/62/d6/74/62d67432-0670-631f-db6a-d4bac3adae4b/8902894353328_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/75/27/64/75276455-6751-e9d7-1187-b863a2ffefbc/mzaf_4313837125483855343.plus.aac.p.m4a",
            genre = "Bollywood Travel Indie",
            audioQualityBadge = "HI-RES LOSSLESS",
            lyrics = lyricsIlahi
        ),
        Song(
            id = "curated_10",
            title = "Mast Magan (मन मस्त मगन)",
            artist = "Arijit Singh & Chinmayi",
            album = "2 States (२ स्टेट्स)",
            durationMs = 280000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/e1/1b/83/e11b8398-0089-cfb1-f998-16125b3f17c4/8902894355612_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/81/06/cb/8106cb3a-d580-f3d9-a395-72ec2f17c45b/mzaf_9326055917836660950.plus.aac.p.m4a",
            genre = "Hindi Sufi Love",
            audioQualityBadge = "UTKARSH MASTER",
            lyrics = lyricsMastMagan
        )
    )

    // All Over The World (Global Billboard & Worldwide Chart-Toppers)
    val worldSongs: List<Song> = listOf(
        Song(
            id = "world_1",
            title = "Levitating",
            artist = "Dua Lipa",
            album = "Future Nostalgia",
            durationMs = 203000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/6c/11/d6/6c11d681-aa3a-d59e-4c2e-f77e181026ab/190295092665.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/59/dc/4d/59dc4dda-93ff-8f1c-c536-f005f6ea6af5/mzaf_3066686759813252385.plus.aac.p.m4a",
            genre = "Global Pop / Dance",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsLevitating
        ),
        Song(
            id = "world_2",
            title = "As It Was",
            artist = "Harry Styles",
            album = "Harry's House",
            durationMs = 167000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/2a/19/fb/2a19fb85-2f70-9e44-f2a9-82abe679b88e/886449990061.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/67/10/16/67101606-3869-ca44-6c03-e13d6322cb51/mzaf_1135399237022217274.plus.aac.p.m4a",
            genre = "Synthpop / Indie",
            audioQualityBadge = "UTKARSH MASTER",
            lyrics = lyricsAsItWas
        ),
        Song(
            id = "world_3",
            title = "Cruel Summer",
            artist = "Taylor Swift",
            album = "Lover",
            durationMs = 178000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/49/3d/ab/493dab54-f920-9043-6181-80993b8116c9/19UMGIM53909.rgb.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/44/af/81/44af8168-9609-1b85-5048-ada08dceacf3/mzaf_1341699644335558812.plus.aac.p.m4a",
            genre = "Pop",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsCruelSummer
        ),
        Song(
            id = "world_4",
            title = "Shape of You",
            artist = "Ed Sheeran",
            album = "÷ (Divide)",
            durationMs = 233000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/15/e6/e8/15e6e8a4-4190-6a8b-86c3-ab4a51b88288/190295851286.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/44/c7/4f/44c74f0d-72dc-6143-d4d0-ba14d661ca0d/mzaf_9566898362556366703.plus.aac.p.m4a",
            genre = "Pop",
            audioQualityBadge = "HI-RES LOSSLESS",
            lyrics = lyricsShapeOfYou
        ),
        Song(
            id = "world_5",
            title = "Birds of a Feather",
            artist = "Billie Eilish",
            album = "HIT ME HARD AND SOFT",
            durationMs = 196000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/92/9f/69/929f69f1-9977-3a44-d674-11f70c852d1b/24UMGIM36186.rgb.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/34/31/d3/3431d34e-847f-5d66-df83-0bce688d997e/mzaf_18106743962423782018.plus.aac.p.m4a",
            genre = "Alternative",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsBirdsOfAFeather
        ),
        Song(
            id = "world_6",
            title = "Sunflower",
            artist = "Post Malone & Swae Lee",
            album = "Spider-Man: Into the Spider-Verse",
            durationMs = 158000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/4b/30/2c/4b302cb6-7a14-5464-4e97-0577e9d0be49/18UMGIM82277.rgb.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/98/f0/d6/98f0d67e-f8bf-762d-cac7-1c6b3b6b35dd/mzaf_4543283896248560946.plus.aac.p.m4a",
            genre = "Hip-Hop / Pop",
            audioQualityBadge = "LOSSLESS",
            lyrics = lyricsSunflower
        ),
        Song(
            id = "world_7",
            title = "My Universe",
            artist = "Coldplay & BTS",
            album = "Music of the Spheres",
            durationMs = 226000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/df/60/00/df600071-c5c7-24e7-e4d7-1a6b71fa44e7/190296529818.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/ac/9a/20/ac9a20f7-0065-92f0-7fbe-c136bc5ddcf4/mzaf_10985566651421931051.plus.aac.p.m4a",
            genre = "Alternative / K-Pop",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsMyUniverse
        ),
        Song(
            id = "world_8",
            title = "Easy On Me",
            artist = "Adele",
            album = "30",
            durationMs = 224000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/73/6d/7c/736d7cfb-c79d-c9a9-4170-5e71d008dea1/886449666430.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/4c/08/30/4c08305e-0556-30a1-d1ba-2f916b56d62c/mzaf_10419822985277809788.plus.aac.p.m4a",
            genre = "Pop / Soul",
            audioQualityBadge = "HI-RES LOSSLESS",
            lyrics = lyricsEasyOnMe
        ),
        Song(
            id = "world_9",
            title = "Believer",
            artist = "Imagine Dragons",
            album = "Evolve",
            durationMs = 204000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/11/7a/b8/117ab805-6811-8929-18b9-0fad7baf0c25/17UMGIM98210.rgb.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/c0/3f/36/c03f367a-b66b-fd0a-a54c-30f8250c4410/mzaf_12768434238801682952.plus.aac.p.m4a",
            genre = "Alternative Rock",
            audioQualityBadge = "UTKARSH MASTER",
            lyrics = lyricsBeliever
        ),
        Song(
            id = "world_10",
            title = "Starboy",
            artist = "The Weeknd ft. Daft Punk",
            album = "Starboy",
            durationMs = 230000L,
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/90/a6/f7/90a6f7b1-e0c1-3f14-93a9-3d027e85c2c4/16UMGIM56475.rgb.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/59/dc/4d/59dc4dda-93ff-8f1c-c536-f005f6ea6af5/mzaf_3066686759813252385.plus.aac.p.m4a",
            genre = "R&B / Electronic",
            audioQualityBadge = "SPATIAL AUDIO",
            lyrics = lyricsStarboy
        )
    )

    // Complete Library of Indian & Global Worldwide Hits
    val allCuratedSongs: List<Song> = curatedSongs + worldSongs

    // Live Indian & Worldwide Radio Stations
    val radioStations: List<RadioStation> = listOf(
        RadioStation(
            id = "radio_world_1",
            name = "Utkarsh World 1 Live",
            tagline = "Global flagship broadcast live from London, Mumbai, New York & Tokyo.",
            hostOrShow = "The Global Countdown with Zane",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/6c/11/d6/6c11d681-aa3a-d59e-4c2e-f77e181026ab/190295092665.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/59/dc/4d/59dc4dda-93ff-8f1c-c536-f005f6ea6af5/mzaf_3066686759813252385.plus.aac.p.m4a",
            isLive = true,
            frequencyLabel = "GLOBAL LIVE • 24/7"
        ),
        RadioStation(
            id = "radio_mirchi",
            name = "Radio Mirchi 98.3 FM",
            tagline = "It's Hot! Live Bollywood chartbusters broadcast from Mumbai.",
            hostOrShow = "RJ Naved & Jeeturaaj Live",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/9f/13/ca/9f13ca3b-e533-03e0-f19a-f0aaa774581d/196589311191.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/38/4c/5c/384c5c8f-3ff8-e457-b2f7-3158ce108649/mzaf_12389299033886433185.plus.aac.p.m4a",
            isLive = true,
            frequencyLabel = "LIVE • MUMBAI 98.3"
        ),
        RadioStation(
            id = "radio_redfm",
            name = "Red FM 93.5 • बजाते रहो!",
            tagline = "Bajaate Raho! Superhit Hindi blockbusters and RJ banter.",
            hostOrShow = "Morning No. 1 with RJ Raunac",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/61/65/ae/6165aee9-8bb9-0bd4-02b0-5d0f1e6257a3/886449510238.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/99/0c/38/990c381b-0530-8c0d-87a9-18b050b97f0a/mzaf_10418866714500530894.plus.aac.p.m4a",
            isLive = true,
            frequencyLabel = "LIVE • RED FM 93.5"
        ),
        RadioStation(
            id = "radio_utkarsh_desi",
            name = "Utkarsh Bollywood Hits",
            tagline = "Non-stop Arijit Singh, Shreya Ghoshal, A.R. Rahman & Pritam 24/7.",
            hostOrShow = "Non-Stop Melodies Live",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/bb/23/ee/bb23eeed-0c35-4f1d-2b11-485622777ae4/8902894353007_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/3a/8c/9b/3a8c9b0b-2def-750a-f615-1555bf941edf/mzaf_17229496441442805917.plus.aac.p.m4a",
            isLive = true,
            frequencyLabel = "LIVE 24/7 • UTKARSH"
        ),
        RadioStation(
            id = "radio_punjabi",
            name = "Punjabi Beats Radio",
            tagline = "Bhangra, Punjabi Pop & Hip-Hop featuring Diljit, Karan Aujla & AP Dhillon.",
            hostOrShow = "Desi Crew Countdown",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/8a/89/e4/8a89e445-d2c6-f8ac-a828-27818b0c1afe/859749638209_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/38/d5/7a/38d57a99-39fc-e901-7c45-fa6260ec83c1/mzaf_8083696285926392389.plus.aac.p.m4a",
            isLive = true,
            frequencyLabel = "PUNJABI HITS • 24/7"
        ),
        RadioStation(
            id = "radio_sufi",
            name = "Sufi & Raag Lounge",
            tagline = "Peaceful Indian classical sitar, flutes, and soul-stirring qawwalis.",
            hostOrShow = "Ganga Ghat Evening Raag",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/56/ac/41/56ac41f7-99f3-3eae-3b07-443167292c4e/8902894697408_cover.jpg/600x600bb.jpg",
            streamUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/ce/72/5e/ce725e63-6977-b281-3766-fff46d6e0c07/mzaf_7464998292185183397.plus.aac.p.m4a",
            isLive = true,
            frequencyLabel = "SUFI & MEDITATION"
        )
    )

    // Curated Indian & Worldwide Playlists with Authentic Artwork
    val curatedPlaylists: List<Playlist> = listOf(
        Playlist(
            id = "playlist_world_top",
            name = "Global Top 50 (दुनिया के टॉप 50)",
            description = "The biggest hits across the world right now on Utkarsh Music.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/6c/11/d6/6c11d681-aa3a-d59e-4c2e-f77e181026ab/190295092665.jpg/600x600bb.jpg",
            songs = worldSongs
        ),
        Playlist(
            id = "playlist_bollywood_top",
            name = "Bollywood Top 50 (बॉलीवुड)",
            description = "The biggest Hindi songs right now across India on Utkarsh Music.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/9f/13/ca/9f13ca3b-e533-03e0-f19a-f0aaa774581d/196589311191.jpg/600x600bb.jpg",
            songs = listOf(curatedSongs[0], curatedSongs[1], curatedSongs[2], curatedSongs[3], curatedSongs[6], curatedSongs[9])
        ),
        Playlist(
            id = "playlist_billboard",
            name = "Billboard Hot 100",
            description = "International chart-toppers and record breakers.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/2a/19/fb/2a19fb85-2f70-9e44-f2a9-82abe679b88e/886449990061.jpg/600x600bb.jpg",
            songs = listOf(worldSongs[1], worldSongs[2], worldSongs[4], worldSongs[0], worldSongs[5])
        ),
        Playlist(
            id = "playlist_hindi_romance",
            name = "Hindi Romance & Love (इश्क़ सुफ़ियाना)",
            description = "Heartwarming Bollywood love ballads with live synced Hindi lyrics.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/61/65/ae/6165aee9-8bb9-0bd4-02b0-5d0f1e6257a3/886449510238.jpg/600x600bb.jpg",
            songs = listOf(curatedSongs[0], curatedSongs[1], curatedSongs[2], curatedSongs[6], curatedSongs[9])
        ),
        Playlist(
            id = "playlist_sufi_soul",
            name = "Sufi & Coke Studio (रूहानी सुकून)",
            description = "Transcendent spiritual compositions by A.R. Rahman, Nusrat & Pritam.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/56/ac/41/56ac41f7-99f3-3eae-3b07-443167292c4e/8902894697408_cover.jpg/600x600bb.jpg",
            songs = listOf(curatedSongs[4], curatedSongs[3], curatedSongs[9])
        ),
        Playlist(
            id = "playlist_punjabi_heat",
            name = "Punjabi Heatwave (पंजाबी बीट्स)",
            description = "High energy Punjabi pop, Bhangra & Dhol anthems.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/8a/89/e4/8a89e445-d2c6-f8ac-a828-27818b0c1afe/859749638209_cover.jpg/600x600bb.jpg",
            songs = listOf(curatedSongs[7], curatedSongs[1], curatedSongs[0])
        ),
        Playlist(
            id = "playlist_acoustic_indie",
            name = "Desi Acoustic & Chai (चाय और सुकून)",
            description = "Gentle acoustic strums, lo-fi chai sessions & calm Hindi indie.",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music211/v4/a7/46/2f/a7462f7e-6dde-b235-2f5e-90e31af919a7/7799192004134_cover.jpg/600x600bb.jpg",
            songs = listOf(curatedSongs[5], curatedSongs[8], curatedSongs[0])
        )
    )

    // Featured Indian & Global Albums with Original Covers
    val featuredAlbums: List<Album> = listOf(
        Album(
            id = "album_future_nostalgia",
            title = "Future Nostalgia",
            artist = "Dua Lipa",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music116/v4/6c/11/d6/6c11d681-aa3a-d59e-4c2e-f77e181026ab/190295092665.jpg/600x600bb.jpg",
            year = "2020",
            badge = "Spatial Audio",
            songs = listOf(worldSongs[0])
        ),
        Album(
            id = "album_brahmastra",
            title = "Brahmāstra (Part 1)",
            artist = "Pritam & Arijit Singh",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music112/v4/9f/13/ca/9f13ca3b-e533-03e0-f19a-f0aaa774581d/196589311191.jpg/600x600bb.jpg",
            year = "2022",
            badge = "Utkarsh Master",
            songs = listOf(curatedSongs[0])
        ),
        Album(
            id = "album_harrys_house",
            title = "Harry's House",
            artist = "Harry Styles",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music126/v4/2a/19/fb/2a19fb85-2f70-9e44-f2a9-82abe679b88e/886449990061.jpg/600x600bb.jpg",
            year = "2022",
            badge = "Utkarsh Master",
            songs = listOf(worldSongs[1])
        ),
        Album(
            id = "album_rockstar",
            title = "Rockstar (रॉकस्टार)",
            artist = "A.R. Rahman & Irshad Kamil",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music115/v4/56/ac/41/56ac41f7-99f3-3eae-3b07-443167292c4e/8902894697408_cover.jpg/600x600bb.jpg",
            year = "2011",
            badge = "Spatial Audio",
            songs = listOf(curatedSongs[4])
        ),
        Album(
            id = "album_aashiqui2",
            title = "Aashiqui 2 (आशिकी २)",
            artist = "Mithoon, Ankit Tiwari & Jeet Gannguli",
            coverUrl = "https://is1-ssl.mzstatic.com/image/thumb/Music221/v4/bb/23/ee/bb23eeed-0c35-4f1d-2b11-485622777ae4/8902894353007_cover.jpg/600x600bb.jpg",
            year = "2013",
            badge = "Hi-Res Lossless",
            songs = listOf(curatedSongs[2])
        )
    )

    val searchCategories = listOf(
        "Global Top 50",
        "Bollywood",
        "Billboard Hot 100",
        "Hindi Romance",
        "Taylor Swift",
        "Arijit Singh",
        "Dua Lipa",
        "A.R. Rahman",
        "Harry Styles",
        "Punjabi Pop",
        "Sufi & Qawwali",
        "Desi Lo-Fi",
        "Spatial Audio",
        "Bhangra",
        "Indian Classical",
        "Sleep & Ambient"
    )
}
