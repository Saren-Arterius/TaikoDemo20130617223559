package com.luzi82.elly.draw;

import com.badlogic.gdx.graphics.GL10;
import com.luzi82.elly.camera.EbLogicScreen;
import com.luzi82.elly.logic.EbObject;

public class EbDraw {

    public enum Type {
        STATIC,
        OBJECT,
    }

    public final Type mType;

    public EbDraw(Type aType) {
        mType = aType;
    }

    public void drawStatic(GL10 aGl, EbLogicScreen<?, ?> aScreen) {
    }

    public void preDrawObject(GL10 aGl) {
    }

    public void drawObject(GL10 aGl, EbObject<?> aObject) {
    }

    public void postDrawObject(GL10 aGl) {
    }

    public void alloc() {
    }

    public void free() {
    }

}
