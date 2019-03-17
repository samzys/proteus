package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SimulationResultDTO implements IsSerializable, Serializable {
	
	private List<PlotDataDTO> pds = new ArrayList<PlotDataDTO>();
	private String title;

	/**
	 * @return the pds
	 */
	public List<PlotDataDTO> getPds() {
		return pds;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}



	/**
	 * @param pds the pds to set
	 */
	public void setPds(List<PlotDataDTO> pds) {
		this.pds = pds;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
