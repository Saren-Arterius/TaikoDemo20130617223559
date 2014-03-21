package com.luzi82.elly;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Logger;

public abstract class EbScreen<G extends EbGame> implements Screen {

    protected G      iParent;
    protected int    mScreenWidth;
    protected int    mScreenHeight;

    private boolean  iMemberLoaded = false;

    protected Logger iLogger       = new Logger(this.getClass().getSimpleName(), Logger.DEBUG);

    protected EbScreen(G aParent) {
        iParent = aParent;
    }

    @Override
    public final void show() {
        iLogger.debug("show");
        onScreenShow();
        loadMember();
    }

    @Override
    public final void resume() {
        iLogger.debug("resume");
        onScreenResume();
        onScreenResize();
        loadMember();
    }

    @Override
    public final void resize(int aWidth, int aHeight) {
        iLogger.debug("resize");
        final boolean sizeChanged = (mScreenWidth != aWidth) || (mScreenHeight != aHeight);
        mScreenWidth = aWidth;
        mScreenHeight = aHeight;
        if (sizeChanged) {
            onScreenResize();
            if (iMemberLoaded) {
                disposeMember();
                loadMember();
            }
        }
    }

    @Override
    public final void render(float aDelta) {
        // iLogger.debug("render");
        if (iMemberLoaded) {
            onScreenRender(aDelta);
        }
    }

    @Override
    public final void hide() {
        iLogger.debug("hide");
        onScreenHide();
        disposeMember();
    }

    @Override
    public final void pause() {
        iLogger.debug("pause");
        onScreenPause();
        disposeMember();
    }

    @Override
    public final void dispose() {
        // iLogger.debug("dispose");
        onScreenDispose();
        disposeMember();
    }

    private void loadMember() {
        if (iMemberLoaded) {
            return;
        }
        iMemberLoaded = true;
        onScreenLoadMember();
    }

    private void disposeMember() {
        // iLogger.debug("disposeMember");
        onScreenDisposeMember();
        EbDeepDispose.disposeMember(this, EbScreen.class);
        iMemberLoaded = false;
    }

    protected void onScreenLoadMember() {
        // dummy
    }

    protected void onScreenDisposeMember() {
        // dummy
    }

    protected void onScreenResize() {
        // dummy
    }

    protected void onScreenShow() {
        // dummy
    }

    protected void onScreenResume() {
        // dummy
    }

    protected void onScreenRender(float aDelta) {
        // dummy
    }

    protected void onScreenHide() {
        // dummy
    }

    protected void onScreenPause() {
        // dummy
    }

    protected void onScreenDispose() {
        // dummy
    }

    public boolean keyDown(int keycode, long aTime) {
        // dummy
        return false;
    }

    public boolean keyUp(int keycode, long aTime) {
        // dummy
        return false;
    }

    public boolean keyTyped(char character, long aTime) {
        // dummy
        return false;
    }

    public boolean touchDown(int x, int y, int pointer, int button, long aTime) {
        // dummy
        return false;
    }

    public boolean touchUp(int x, int y, int pointer, int button, long aTime) {
        // dummy
        return false;
    }

    public boolean touchDragged(int x, int y, int pointer, long aTime) {
        // dummy
        return false;
    }

    public boolean touchMoved(int x, int y, long aTime) {
        // dummy
        return false;
    }

    public boolean scrolled(int amount, long aTime) {
        // dummy
        return false;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public Logger getLogger() {
        return iLogger;
    }

}
