package com.luzi82.elly.logic;

import java.io.Serializable;

public class EbObject<L extends EbLogic<L>> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -732730721023555684L;

    public int                mId;

    public EbPos2             mPos2;

    public Float              mHP;

    public void tick(L aLogic) {
    }

    public void liveTick(L aLogic) {
    }

    public void deathTick(L aLogic) {
    }

    public EbObject<L> getClosest(L aLogic, Class<?>[] aTargetList, float aRange) {
        float range2 = aRange * aRange;
        EbObject<L> ret = null;
        final EbObject<L>[] objAry = aLogic.objectList();
        for (final EbObject<L> o: objAry) {
            if (o.mPos2 == null) {
                continue;
            }
            boolean good = false;
            for (final Class<?> c: aTargetList) {
                if (c.isInstance(o)) {
                    good = true;
                    break;
                }
            }
            if (!good) {
                continue;
            }
            final float t = distance2(o);
            if (t < range2) {
                ret = o;
                range2 = t;
            }
        }
        return ret;
    }

    public float distance2(EbObject<L> aOther) {
        float ret = 0;
        float t = mPos2.mX - aOther.mPos2.mX;
        ret += t * t;
        t = mPos2.mY - aOther.mPos2.mY;
        ret += t * t;
        return ret;
    }

}
