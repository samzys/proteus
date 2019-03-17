package proteus.gwt.client.app.event;

import proteus.gwt.client.app.ui.ModelViewInstance;

import com.google.gwt.event.shared.GwtEvent;

public class MVIChangeEvent extends GwtEvent<MVIChangeEventHandler>{

	public static Type<MVIChangeEventHandler> TYPE = new Type<MVIChangeEventHandler>();
	
	private ModelViewInstance instance;
	public MVIChangeEvent(ModelViewInstance instance) {
		this.instance = instance;
	}
	@Override
	protected void dispatch(MVIChangeEventHandler handler) {
		handler.onChange(this);
	}
	@Override
	public Type<MVIChangeEventHandler> getAssociatedType() {
		return TYPE;
	}
	
	/**
	 * @return the instance
	 */
	public ModelViewInstance getInstance() {
		return instance;
	}
	/**
	 * @param instance the instance to set
	 */
	public void setInstance(ModelViewInstance instance) {
		this.instance = instance;
	}
}
