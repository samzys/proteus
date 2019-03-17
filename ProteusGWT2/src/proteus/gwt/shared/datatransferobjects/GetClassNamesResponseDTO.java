package proteus.gwt.shared.datatransferobjects;

import proteus.gwt.shared.util.TreeData;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GetClassNamesResponseDTO implements IsSerializable {
	private boolean success;
	private String message;
	private TreeData treeData;
	

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	

	public TreeData getTreeData() {
		return treeData;
	}

	public void setTreeData(TreeData treeData) {
		this.treeData = treeData;
	}
	

}
