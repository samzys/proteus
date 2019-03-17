package proteus.gwt.client.app.ui.canvas;

import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.modelica.Component;

/**
 * @author sam This class is corresponse to the parameters inside the model.
 *         Because some model requires variable value from these paramters. not
 *         finished so far
 * 
 */
public class ParameterItem extends CanvasItem {

	private ParameterDTO para;

	public ParameterItem(Canvas canvas, String _prefix) {
		super(canvas, _prefix);
		// TODO Auto-generated constructor stub
	}

	public ParameterItem(DiagramCanvas dc, Component c, ParameterDTO para, String _prefix){
		super(dc, _prefix);
		this.para = para;
	
	}
	@Override
	public CanvasItem copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCodeCanvas() {
		// TODO Auto-generated method stub

	}

	public ParameterDTO getPara() {
		return para;
	}

	public void setPara(ParameterDTO para) {
		this.para = para;
	}

}
