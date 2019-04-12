package com.purelazy.game;

// Problem: Device offline, Solution: Off/On USB Debugging in device settings

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;

public class Start extends ScreenAndLifeCycle implements Input.TextInputListener {

    protected Music backgroundMusic;
    String playerName = "guest";

    @Override
    public void create() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("data/thebeam.ogg"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(1f);
        backgroundMusic.play();
        Gdx.input.getTextInput(this, "Enter you name/你的名字", "", "Your name here");
    }

    public void input (String name) {
        if (name.equals("")) playerName = "Guest"; else playerName = name;
        setScreen(new Menu01_LevelChooser(this));
        //setScreen(new Maze01_Maze16(this));
    };

    public void canceled () {
        Gdx.app.exit();
    };
}