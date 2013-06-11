package com.luzi82.elly.logic;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public abstract class EbProcess<T extends EbObject<L>, L extends EbLogic<L>> {

	public boolean out_success;

	public Map<String, Object> process(L aLogic, T aActor,
			Map<String, Object> aRequest) throws IllegalArgumentException,
			IllegalAccessException {
		Class<?> clazz = getClass();
		Field[] fieldAry = clazz.getFields();
		for (Field field : fieldAry) {
			if (!field.getName().startsWith("in_"))
				continue;
			String key = field.getName().substring(3);
			Class<?> fieldClass = field.getType();
			if (EbObject.class.isAssignableFrom(fieldClass)) {
				field.set(this, aLogic.get((Integer) aRequest.get(key)));
			} else {
				field.set(this, aRequest.get(key));
			}
		}
		process(aLogic, aActor);
		HashMap<String, Object> response = new HashMap<String, Object>();
		for (Field field : fieldAry) {
			if (!field.getName().startsWith("out_"))
				continue;
			String key = field.getName().substring(4);
			if (field.get(this) instanceof EbObject) {
				response.put(key, ((EbObject<?>) field.get(this)).mId);
			} else {
				response.put(key, field.get(this));
			}
		}
		return response;
	}

	public abstract void process(L ebLogic, T actor);

}
