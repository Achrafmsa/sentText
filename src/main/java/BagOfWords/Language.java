package BagOfWords;

public class Language {

    public enum language {
        Arabic,
        French,
        English,
    }

    public static String getRegex(language lang) {
        String regex = "";
        if (lang == language.Arabic)
            regex = "[^\\p{InArabic}]";
        else if (lang == language.English || lang == language.French)
            regex = "[^\\p{Alpha}]";
        return regex;
    }
}