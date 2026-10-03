package ru.mirea.danilov.anishot.data.network;

/**
 * Замоканный JSON-сервис каталога (практика 2: NetworkApi).
 * Позже тело ответа заменяется на HTTP.
 */
public class NetworkApi {
    public String getCatalogJson() {
        return "["
                + item(1, "Фрирен", "葬送のフリーレン", "Провожающая в последний путь",
                "https://cdn.myanimelist.net/images/anime/1015/138006l.jpg",
                "#4A3A78", "2023 · MADHOUSE · 28 серий",
                "Эльф-маг Фрирен провожает товарищей после победы над королём демонов.", 88)
                + ","
                + item(2, "Магическая битва", "呪術廻戦", "Jujutsu Kaisen",
                "https://cdn.myanimelist.net/images/anime/1171/109222l.jpg",
                "#3A2458", "2020 · MAPPA · 2 сезона",
                "Юджи Итадори становится сосудом Сукуны.", 86)
                + ","
                + item(3, "Агенты времени", "时光代理人", "Link Click",
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx126403-BfVSRzWUtVFW.png",
                "#1E4A6E", "2021 · LAN Studio · 2 сезона",
                "Чэн Сяоши и Лу Гуан входят в снимки и правят прошлое.", 85)
                + ","
                + item(4, "Человек-бензопила", "チェンソーマン", "Chainsaw Man",
                "https://cdn.myanimelist.net/images/anime/1806/126216l.jpg",
                "#8A4A18", "2022 · MAPPA · 12 серий",
                "Дэндзи заключает контракт с демоном Почитой.", 84)
                + ","
                + item(5, "Атака титанов", "進撃の巨人", "Attack on Titan",
                "https://cdn.myanimelist.net/images/anime/10/47347l.jpg",
                "#5A2018", "2013 · WIT / MAPPA · 4 сезона",
                "После прорыва стены Эрен клянётся уничтожить титанов.", 90)
                + "]";
    }

    public String getSceneJson() {
        return "{\"anilistId\":1,\"title\":\"Фрирен\",\"episode\":14,\"at\":724.0,\"similarity\":0.974,"
                + "\"imageUrl\":\"https://cdn.myanimelist.net/images/anime/1015/138006l.jpg\",\"videoUrl\":\"\"}";
    }

    private static String item(int id, String title, String original, String subtitle,
                               String image, String color, String meta, String description, int score) {
        return "{\"id\":" + id
                + ",\"title\":\"" + title + "\""
                + ",\"original\":\"" + original + "\""
                + ",\"subtitle\":\"" + subtitle + "\""
                + ",\"image\":\"" + image + "\""
                + ",\"color\":\"" + color + "\""
                + ",\"meta\":\"" + meta + "\""
                + ",\"description\":\"" + description + "\""
                + ",\"score\":" + score + "}";
    }
}
