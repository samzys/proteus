/**
 * 
 */
package proteus.gwt.client.app.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author sam
 * 
 */
public class SimulationInit extends SimplePanel {

	private static SimulationInitUiBinder uiBinder = GWT
			.create(SimulationInitUiBinder.class);

	interface SimulationInitUiBinder extends UiBinder<Widget, SimulationInit> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */

	public @UiField
	DialogBox box;
	public @UiField
	TextBox tbStartTime;
	public @UiField
	TextBox tbStopTime;
	public @UiField
	TextBox tbInterval;
	public @UiField
	Button btSimulate;
	public @UiField
	Button btCancel;

	public SimulationInit() {
		add(uiBinder.createAndBindUi(this));
	}

}
