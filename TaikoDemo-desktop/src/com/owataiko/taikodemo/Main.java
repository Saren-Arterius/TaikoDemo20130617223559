package com.owataiko.taikodemo;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "TaikoDemo";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 320;
		
		new LwjglApplication(new TaikoGame(new TaikoAudio() {
			
			@Override
			public void unload() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void stopBgm() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void playBgm() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void load() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void kat() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void don() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public long bgmPos() {
				// TODO Auto-generated method stub
				return 0;
			}
		}), cfg);
	}
}
