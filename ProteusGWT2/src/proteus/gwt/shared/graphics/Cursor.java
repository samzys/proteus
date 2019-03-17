/**
 * 
 */
package proteus.gwt.shared.graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lei Ting
 *
 */
public class Cursor {

    /**
     * The default cursor type (gets set if no cursor is defined).
     */
    public static final int	DEFAULT_CURSOR   		= 0;

    /**
     * The crosshair cursor type.
     */
    public static final int	CROSSHAIR_CURSOR 		= 1;

    /**
     * The text cursor type.
     */
    public static final int	TEXT_CURSOR 	 		= 2;

    /**
     * The wait cursor type.
     */
    public static final int	WAIT_CURSOR	 		= 3;

    /**
     * The south-west-resize cursor type.
     */
    public static final int	SW_RESIZE_CURSOR	 	= 4;

    /**
     * The south-east-resize cursor type.
     */
    public static final int	SE_RESIZE_CURSOR	 	= 5;

    /**
     * The north-west-resize cursor type.
     */
    public static final int	NW_RESIZE_CURSOR		= 6;

    /**
     * The north-east-resize cursor type.
     */
    public static final int	NE_RESIZE_CURSOR	 	= 7;

    /**
     * The north-resize cursor type.
     */
    public static final int	N_RESIZE_CURSOR 		= 8;

    /**
     * The south-resize cursor type.
     */
    public static final int	S_RESIZE_CURSOR 		= 9;

    /**
     * The west-resize cursor type.
     */
    public static final int	W_RESIZE_CURSOR	 		= 10;

    /**
     * The east-resize cursor type.
     */
    public static final int	E_RESIZE_CURSOR			= 11;

    /**
     * The hand cursor type.
     */
    public static final int	HAND_CURSOR			= 12;

    /**
     * The move cursor type.
     */
    public static final int	MOVE_CURSOR			= 13;

    public static final int CUSTOM = 14;
    
    protected static Cursor predefined[] = new Cursor[14];

    private int cursor = DEFAULT_CURSOR;
    
    private String cursorName;
    
    private static List<String> cursorNames = new ArrayList<String>();
    
    public Cursor(int cursor) {
    	this.setCursor(cursor);
    }
    
    /*
     * build a custom cursor
     */
    public Cursor(String name) {
    	cursor = CUSTOM;
    	setCursorName(name);
    	
    	if (!getCursorNames().contains(cursorName)) {
    		getCursorNames().add(cursorName);
    	}
    }
    
	public static Cursor getPredefinedCursor(int cursor) {
		return new Cursor(cursor);
	}
	
	public static Cursor getDefaultCursor() {
		return new Cursor(DEFAULT_CURSOR);
	}

	/**
	 * @param cursor the cursor to set
	 */
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	/**
	 * @return the cursor
	 */
	public int getCursor() {
		return cursor;
	}

	/**
	 * @param cursorName the cursorName to set
	 */
	public void setCursorName(String cursorName) {
		this.cursorName = cursorName;
	}

	/**
	 * @return the cursorName
	 */
	public String getCursorName() {
		return cursorName;
	}

	/**
	 * @param cursorNames the cursorNames to set
	 */
	public static void setCursorNames(List<String> cursorNames) {
		Cursor.cursorNames = cursorNames;
	}

	/**
	 * @return the cursorNames
	 */
	public static List<String> getCursorNames() {
		return cursorNames;
	}
}
