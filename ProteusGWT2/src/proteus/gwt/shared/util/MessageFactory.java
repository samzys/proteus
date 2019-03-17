package proteus.gwt.shared.util;

public class MessageFactory {
	private static final native <T> T eval(String json) /*-{
		return eval('(' + json + ')');
	}-*/;
	
	public static <T> T create(String input, boolean strip) {
		//remove tag from raw
		if (strip) {
			input = input.substring(input.indexOf(">{")+1, input.length()-6);
		}
		return eval(input);
	}
	
	public static <T> T create(String input) {
		return create(input, true);
	}
}
