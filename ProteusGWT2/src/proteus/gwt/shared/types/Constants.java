package proteus.gwt.shared.types;

public interface Constants {
	public static final String HEADER_PREFIX_MODEL = "Model:",
			HEADER_PREFIX_START_TIME = "StartTime:",
			HEADER_PREFIX_STOP_TIME = "StopTime:", 
			HEADER_PREFIX_INTERVALS="Intervals";
	public static final String HEADER_USER = "User:", HEADER_PASS = "Pass:",
			HEADER_MID = "ModelID:", HEADER_UID = "UserID:";

	public static final String NOT_UR_MODEL_ERROR = "This Model belongs to some other ppl!",
			FILE_NOT_FOUND_ERROR = "The MO file was Not Found!",
			FILE_WRITTEN_FAILED_ERROR = "File Written Failed!";

	public static final String RESP_OK = "[OK]", RESP_ERROR = "[ERROR]";

	public static final int STATUS_PENDING = 0, STATUS_RUNNING = 1,
			STATUS_DONE = 2, STATUS_CANCELED = 3, STATUS_ERROR = 4;

	public static final String[] statusTextArray = {
			"PENDING: Job has been submitted, but hasn't started running yet",
			"RUNNING: Job is running, check back again for an updated status",
			"DONE: Job is complete, you can now retrieve the results",
			"CANCELED: Job was canceled",
			"ERROR: An error occurred during execution" };
	public static final String INVALID_USER = "Incorrect username or password!";
	public static final String FILE_IO_ERROR = "Failed to open this file";
}
