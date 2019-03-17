package proteus.gwt.client.app.ui.menu;

import proteus.gwt.client.app.ui.LogoutBox;
import proteus.gwt.client.app.ui.LoginBox;
import proteus.gwt.client.app.util.HTMLGetter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;

public class ProteusMainMenu extends Composite {

	private static ProteusMainMenuUiBinder uiBinder = GWT
			.create(ProteusMainMenuUiBinder.class);

	interface ProteusMainMenuUiBinder extends UiBinder<Widget, ProteusMainMenu> {
	}

	public @UiField
	MenuItem mielectricalCircuit;

	public @UiField
	MenuItem mispringSystem;


	public @UiField
	MenuItem midcMotor;

	public @UiField
	MenuItem midoublePendulum;
		
	public @UiField
	MenuItem miNewModel;

	public @UiField
	MenuItem miNewClass;

	public @UiField
	MenuItem miNew;
	public @UiField
	MenuItem miDemo;
	public @UiField
	MenuItem miSave;
	public @UiField
	MenuItem miOpen;
	public @UiField
	MenuItem miExportAs;
	public @UiField
	MenuItem miPrint;
	public @UiField
	MenuItem miExit;

	public @UiField
	MenuItem miUndo;
	public @UiField
	MenuItem miRedo;
	public @UiField
	MenuItem miDelete;

	public @UiField
	MenuItem miZoomIn;
	public @UiField
	MenuItem miZoomOut;

	public @UiField
	MenuItem miRotateClockwise;
	public @UiField
	MenuItem miRotateAntiClockwise;
	public @UiField
	MenuItem miFlipHorizontal;
	public @UiField
	MenuItem miFlipVertical;
	public @UiField
	MenuItem miInsert;

	public @UiField
	MenuItem miSimulate;
	public @UiField
	MenuItem miPlot;

	public @UiField
	MenuItem miHelp;
	public @UiField
	MenuItem miAbout;

	public @UiField
	MenuItem miExportMO;
	public @UiField
	MenuItem miExportPDF;
	public @UiField
	MenuItem miExportBMP;
	public @UiField
	MenuItem miExportJPEG;
	public @UiField
	MenuItem miExportPNG;

	public @UiField
	MenuItem userLogin;

	public @UiField
	MenuItem userInfo;

	private String space = "&nbsp;&nbsp;";
	private String spaceWithoutImage = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	private LoginBox loginBox;

	private LogoutBox logoutBox;

	public ProteusMainMenu() {
		initWidget(uiBinder.createAndBindUi(this));
		String imagePath = "/resources/images/";

		miNew.setHTML(HTMLGetter.getImageText(imagePath + "new.png", space
				+ "New"));
		// miDemo.setHTML(HTMLGetter.getImageText(imagePath + "demo.png", space
		// + "Demo"));
		miOpen.setHTML(HTMLGetter.getImageText(imagePath + "open.png", space
				+ "Open"));
		miSave.setHTML(HTMLGetter.getImageText(imagePath + "save.png", space
				+ "Save"));
		// miExportAs.setHTML(HTMLGetter.getImageText(imagePath + "export.png",
		// space + "Export As"));
		miPrint.setHTML(HTMLGetter.getImageText(imagePath + "print.png", space
				+ "Print"));
		// miExit.setHTML(HTMLGetter.getImageText(imagePath + "exit.png", space
		// + "Exit"));

		miDemo.setHTML(spaceWithoutImage + "Demo");
		miExportAs.setHTML(spaceWithoutImage + "Export As");
		miExit.setHTML(spaceWithoutImage + "Exit");

		miUndo.setHTML(HTMLGetter.getImageText(imagePath + "undo.png", space
				+ "Undo"));
		miRedo.setHTML(HTMLGetter.getImageText(imagePath + "redo.png", space
				+ "Redo"));
		miDelete.setHTML(HTMLGetter.getImageText(imagePath + "delete.png",
				space + "Delete"));

		miZoomIn.setHTML(HTMLGetter.getImageText(imagePath + "zoomin.png",
				space + "Zoom In"));
		miZoomOut.setHTML(HTMLGetter.getImageText(imagePath + "zoomout.png",
				space + "Zoom Out"));

		miRotateClockwise.setHTML(HTMLGetter.getImageText(imagePath
				+ "rotate_cw.png", space + "Rotate Clockwise"));
		miRotateAntiClockwise.setHTML(HTMLGetter.getImageText(imagePath
				+ "rotate_ccw.png", space + "Rotate Anti Clockwise"));
		miFlipHorizontal.setHTML(HTMLGetter.getImageText(imagePath
				+ "flip_horizontal.png", space + "Flip Horizontal"));
		miFlipVertical.setHTML(HTMLGetter.getImageText(imagePath
				+ "flip_vertical.png", space + "Flip Vertical"));
		miInsert.setHTML(spaceWithoutImage + "Insert");

		miSimulate.setHTML(HTMLGetter.getImageText(imagePath + "run.png", space
				+ "Simulate"));
		miPlot.setHTML(HTMLGetter.getImageText(imagePath + "charts.png", space
				+ "Plot Result"));

		miHelp.setHTML(HTMLGetter.getImageText(imagePath + "help.png", space
				+ "Help"));
		miAbout.setHTML(spaceWithoutImage + "About");

		initUserAcc();
	}

	private void initUserAcc() {
		loginBox = new LoginBox();
		logoutBox = new LogoutBox();

		setAnonymousProfile();
	}

	public void onLoginComplete(String text, boolean success) {
		if (success) {
			loginBox.hide();
			userInfo.setText(text);
			userLogin.setText("Logout");
			userLogin.setCommand(null);
			userLogin.setCommand(logoutBox);
		} else {
			// set error msg
		}
	}

	public void onLogoutComplete() {
		logoutBox.hide();
		setAnonymousProfile();
	}

	public void setProfile(proteus.gwt.client.app.jsonObject.Users ui) {
		// Registry.registerObject("UserProfile", ui);
		if (ui == null) {
			setAnonymousProfile();
		} else {
			userInfo.setText(ui.getName());
			// this.setSubMenu(getUserTool());
		}
	}

	private void setAnonymousProfile() {
		userLogin.setCommand(loginBox);
		userLogin.setText("Login");
		userInfo.setStyleName("label-style");
		userInfo.setText("Anonymous");
		userInfo.setStyleName("label-style");
	}
}
