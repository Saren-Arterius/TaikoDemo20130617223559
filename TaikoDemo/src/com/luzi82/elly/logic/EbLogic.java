package com.luzi82.elly.logic;

import java.util.HashMap;
import java.util.Map;

public class EbLogic<L extends EbLogic<L>> {

    public Map<Integer, EbObject<L>> mObjectMap = new HashMap<Integer, EbObject<L>>();

    public int                       mNextId    = 1;

    public float                     mWorldW    = Float.NaN;
    public float                     mWorldH    = Float.NaN;
    public boolean                   mWorldXLoop;
    public boolean                   mWorldYLoop;

    public void remove(int aId) {
        mObjectMap.remove(aId);
    }

    public void remove(EbObject<L> aObject) {
        remove(aObject.mId);
    }

    public void add(EbObject<L> aObject) {
        aObject.mId = mNextId++;
        mObjectMap.put(aObject.mId, aObject);
    }

    public EbObject<L> get(int aId) {
        return mObjectMap.get(aId);
    }

    public Map<String, Object> process(Map<String, Object> request) {
        try {
            final int actorId = (Integer) request.get(EbLogic.ACTOR);
            final EbObject<L> actor = get(actorId);
            final Class<?> actorClass = actor.getClass();

            final String processName = (String) request.get(EbLogic.PROCESS);

            Class<?> processClass = null;
            for (final Class<?> clazz: actorClass.getDeclaredClasses()) {
                if (clazz.getSimpleName().equals(processName)) {
                    processClass = clazz;
                    break;
                }
            }
            @SuppressWarnings("unchecked")
            final EbProcess<EbObject<L>, L> process = (EbProcess<EbObject<L>, L>) processClass.newInstance();

            return process.process(thisL(), actor, request);
        } catch (final Exception e) {
            e.printStackTrace();
            final HashMap<String, Object> response = new HashMap<String, Object>();
            response.put(EbLogic.SUCCESS, false);
            response.put(EbLogic.EXCEPTION, e);
            return response;
        }
    }

    @SuppressWarnings("unchecked")
    private L thisL() {
        return (L) this;
    }

    public void tick() {
        final EbObject<L>[] objAry = objectList();
        for (final EbObject<L> o: objAry) {
            o.tick(thisL());
        }
        for (final EbObject<L> o: objAry) {
            if (o.mHP == null) {
                continue;
            }
            if (o.mHP > 0) {
                o.liveTick(thisL());
            } else {
                o.deathTick(thisL());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public EbObject<L>[] objectList() {
        return mObjectMap.values().toArray(new EbObject[0]);
    }

    public static final String ACTOR     = "actor";
    public static final String PROCESS   = "process";
    public static final String SUCCESS   = "success";
    public static final String EXCEPTION = "exception";

}
