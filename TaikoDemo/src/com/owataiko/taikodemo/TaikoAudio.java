package com.owataiko.taikodemo;

public interface TaikoAudio {

    public boolean supportLowLatency();

    public void load();

    public void unload();

    public void don();

    public void kat();

    public void playBgm();

    public void stopBgm();

    public long bgmPos();

}
