/**
 * 
 */
package proteus.gwt.shared.highlighter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.regexp.shared.RegExp;

/**
 *
 * @author Lei Ting
 * Created Apr 13, 2011
 */
public class RegexModelicaSyntaxHighlighter implements ISyntaxHighlighter {
	
	private static final Logger logger = Logger.getLogger("RegexModelicaSyntaxHighlighter");
	
	public static final Map<String, String> regexp = new HashMap<String, String>();

	public static final String numbers = "\\d+(\\.\\d+)?((e|E)(\\+|\\-)?\\d+)?";
	public static final String string = "\"([^\"\\\\]*|\\\\a|\\\\b|\\\\f|\\\\n|\\\\r|\\\\t|\\\\v|\\\\\''|\\\\'|\\\\\\?|\\\\)\"";
	public static final String comment_c = "/\\*(.|\\n)*\\*/";
	public static final String comment_cpp = "//[^\\n]*\\n?";
	public static final String comment_string = string + "(\\s*\\+\\s*" + string + ")*";
	public static final String keywords = "([^\\w])(" +
		"algorithm|and|annotation|assert|block|break|" +
		"class|connect|constrainedby|der|discrete|" +
		"each|else|elseif|elsewhen|encapsulated|end|" +
		"enumeration|equation|expandable|extends|eternal|" +
		"false|final|flow|for|function|if|import|in|initial|inner|input|" +
		"loop|model|not|or|outer|output|" +
		"package|parameter|partial|protected|public|" +
		"record|redeclare|replaceable|return|" +
		"then|true|type|when|while|within" + 
		")([^\\w])";
	
	public static final RegExp keywordsRegex = RegExp.compile(keywords, "m");
	public static final RegExp numbersRegex = RegExp.compile(numbers, "m");
	
	static {
		regexp.put(
				keywords, 
				"$1<span class=\"keywords\">$2</span>$3");
		regexp.put(
				numbers, 
				"<span class=\"numbers\">$0</span>");
	}
	
	public String highlight(String code) {
		String s = code;
		// TODO filter to safe html
//		String s = SafeHtmlUtils.htmlEscapeAllowEntities(code);

		// TODO handle comments
		// TODO handle strings
		
		s = keywordsRegex.replace(s, "$1<span class=\"keywords\">$2</span>$3");
		s = numbersRegex.replace(s, "<span class=\"numbers\">$0</span>");

		s = "<pre class=\"code\">"+s+"</pre>";
		
		return s;
	}
}
