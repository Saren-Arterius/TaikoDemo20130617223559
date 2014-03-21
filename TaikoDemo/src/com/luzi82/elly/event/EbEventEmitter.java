package com.luzi82.elly.event;

import java.util.LinkedList;

public class EbEventEmitter<E> {

    final public LinkedList<EbEventListener<E>> mListenerList = new LinkedList<EbEventListener<E>>();

    public void addListener(EbEventListener<E> aListener) {
        mListenerList.add(aListener);
    }

    public void removeListener(EbEventListener<E> aListener) {
        mListenerList.remove(aListener);
    }

    public void emit(E aEvent) {
        clearGc();
        for (final EbEventListener<E> el: mListenerList) {
            el.onEvent(aEvent);
        }
    }

    public void clearGc() {
        final LinkedList<EbEventListener<E>> clearList = new LinkedList<EbEventListener<E>>();
        for (final EbEventListener<E> el: mListenerList) {
            if (!el.active()) {
                clearList.add(el);
            }
        }
        mListenerList.removeAll(clearList);
    }

}
