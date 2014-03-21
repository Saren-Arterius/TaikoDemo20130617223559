package com.luzi82.elly.event;

import java.lang.ref.WeakReference;

public abstract class EbEventHandler<O, E> {

    final WeakReference<O> mRef;

    public EbEventHandler(O aObj) {
        mRef = new WeakReference<O>(aObj);
    }

    public boolean active() {
        return mRef.get() != null;
    }

    public void onEvent(E aEvent) {
        final O o = mRef.get();
        if (o == null) {
            return;
        }
        onEvent(o, aEvent);
    }

    public abstract void onEvent(O aObject, E aEvent);
}