package com.purelazy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Created by andre on 25/08/15.
 */
public class Localisation {

    // Localisation
    /**
     * Basically, the I18NBundle class is used to store and fetch strings that are locale
     * sensitive. A bundle allows you to easily provide different translations for you application.
     */
    static Locale en_GB = new Locale("en", "GB"); // English, Great Britain
    static Locale zh_CH = new Locale("zh", "CH"); // Chinese, China
    static Locale ja_JA = new Locale("ja", "JP"); // Japanese Japan
    static Locale hi_IN = new Locale("hi", "IN"); // Hindi, India
    static Locale ru_RU = new Locale("ru", "RU"); // Russian, Russian Federation

    static FileHandle baseFileHandle = Gdx.files.internal("i18n/textLevel1");

    static I18NBundle myChineseBundle = I18NBundle.createBundle(baseFileHandle, zh_CH);
    static String value = myChineseBundle.get("name");
    FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/DFLS1B.TTF"));
    //Font font = gen.generateFont(40, "好", false);
}
