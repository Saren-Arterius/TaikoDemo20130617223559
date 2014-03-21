package com.owataiko.taikodemo;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        final TaikoGame.Param param = new TaikoGame.Param();
        param.mTaikoAudio = new TaikoAudioSP();
        param.mTextRenderFactory = new TextRenderFactory(getAssets());

        initialize(new TaikoGame(param), cfg);
    }

    class TaikoAudioSP implements TaikoAudio {

        SoundPool   mSoundPool;
        int         mDonId;
        int         mKatId;

        MediaPlayer mMediaPlayer;

        @Override
        public void load() {
            if (mSoundPool == null) {
                mSoundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
                mDonId = mSoundPool.load(MainActivity.this, R.raw.don, 1);
                mKatId = mSoundPool.load(MainActivity.this, R.raw.kat, 1);
            }
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bgm);
            }
        }

        @Override
        public void unload() {
            if (mSoundPool != null) {
                mSoundPool.release();
                mSoundPool = null;
            }
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        }

        @Override
        public void don() {
            if (mSoundPool != null) {
                mSoundPool.play(mDonId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }

        @Override
        public void kat() {
            if (mSoundPool != null) {
                mSoundPool.play(mKatId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }

        @Override
        public void playBgm() {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }

        @Override
        public void stopBgm() {
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            }
        }

        @Override
        public long bgmPos() {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    return mMediaPlayer.getCurrentPosition();
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        public boolean supportLowLatency() {
            final PackageManager pm = getPackageManager();
            return pm.hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY);
        }

    }

    // class TaikoAudioNA implements TaikoAudio {
    //
    // public void load() {
    // NativeAudio.load();
    // }
    //
    // public void unload() {
    // NativeAudio.unload();
    // }
    //
    // public void don() {
    // NativeAudio.don();
    // }
    //
    // public void kat() {
    // NativeAudio.kat();
    // }
    //
    // }

}
