package com.luzi82.elly;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.badlogic.gdx.utils.Disposable;

public class EbDeepDispose {

    public static void disposeMember(Object aObject, Class<?> aRootClass) {
        // iLogger.debug("disposeMember");
        for (Class<?> c = aObject.getClass(); c != aRootClass; c = c.getSuperclass()) {
            final Field[] fv = c.getDeclaredFields();
            for (final Field f: fv) {
                if ((f.getModifiers() & (Modifier.FINAL | Modifier.STATIC)) != 0) {
                    continue;
                }
                final String n = f.getName();
                f.setAccessible(true);
                if (n.startsWith("m")) {
                    // iLogger.debug(n);
                    try {
                        final Object o = f.get(aObject);
                        EbDeepDispose.deepDispose(o);
                        if (!f.getType().isPrimitive()) {
                            f.set(aObject, null);
                        }
                    } catch (final IllegalArgumentException e) {
                        // iLogger.debug("", e);
                    } catch (final IllegalAccessException e) {
                        // iLogger.debug("", e);
                    }
                }
                f.setAccessible(false);
            }
        }
    }

    public static void deepDispose(Object mObject) {
        if (mObject == null) {
            return;
        }
        final Class<?> c = mObject.getClass();
        if (c.isPrimitive()) {
            // do nothing
        } else if (Disposable.class.isAssignableFrom(c)) {
            final Disposable d = (Disposable) mObject;
            d.dispose();
        } else if (c.isArray()) {
            final Class<?> cc = c.getComponentType();
            if (cc.isPrimitive()) {
                // do nothing
            } else {
                for (int i = 0; i < Array.getLength(mObject); ++i) {
                    final Object o = Array.get(mObject, i);
                    EbDeepDispose.deepDispose(o);
                    Array.set(mObject, i, null);
                }
            }
        }
    }

}
