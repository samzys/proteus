package proteus.gwt.client.app.event;

import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;

import com.google.gwt.event.shared.GwtEvent;

public class ExportEvent extends GwtEvent<ExportEventHandler> {
	public static Type<ExportEventHandler> TYPE = new Type<ExportEventHandler>();

	public ExportProteusDTO exportDTO;

	public ExportEvent(ExportProteusDTO exportDTO) {
		super();
		this.exportDTO = exportDTO;
	}

	public ExportProteusDTO getExportDTO() {
		return exportDTO;
	}

	@Override
	public Type<ExportEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExportEventHandler handler) {
		handler.onExport(this);
	}
}