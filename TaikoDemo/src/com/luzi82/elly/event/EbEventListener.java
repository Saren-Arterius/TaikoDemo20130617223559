package com.luzi82.elly.event;


public interface EbEventListener<E> {

	public void onEvent(E aEvent);
	
	public boolean active();

}
