package com.owataiko.taikodemo;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;

		initialize(new TaikoGame(new TaikoAudioImp()), cfg);
	}

	class TaikoAudioImp implements TaikoAudio {

		public void load() {
			// TODO Auto-generated method stub
			
		}

		public void unload() {
			// TODO Auto-generated method stub
			
		}

		public void don() {
			// TODO Auto-generated method stub
			
		}

		public void kat() {
			// TODO Auto-generated method stub
			
		}

	}

}
