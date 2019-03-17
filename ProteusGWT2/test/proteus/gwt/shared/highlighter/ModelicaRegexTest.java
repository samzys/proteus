/**
 * 
 */
package proteus.gwt.shared.highlighter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gwt.regexp.shared.RegExp;

/**
 *
 * @author Lei Ting
 * Created Apr 14, 2011
 */
public class ModelicaRegexTest {

	/* 
	 * Note: this will be tested using server side RegExp implementation, 
	 * i.e. java implementation
	 * 
	 * Client side RegExp implementation is not tested here.
	 */
	@Test
	public void testRegExps() {
		RegExp e1 = RegExp.compile(RegexModelicaSyntaxHighlighter.comment_c, "gm");
		assertEquals(true, e1.test("/**/"));
		assertEquals(true, e1.test("func();\\n/*abc\n\rdef\nhij*/\nfunc();"));

		RegExp e2 = RegExp.compile(RegexModelicaSyntaxHighlighter.comment_cpp, "gm");
		assertEquals(true, e2.test("//"));
		assertEquals(true, e2.test("func();\\n//this is a comment\nfunc();"));

		RegExp e4 = RegExp.compile(RegexModelicaSyntaxHighlighter.string, "gm");
		assertEquals(true, e4.test("\"\""));
		assertEquals(true, e4.test("\"\\\"\\t\""));
		assertEquals(true, e4.test("func();\\n\"abcde\""));

		RegExp e3 = RegExp.compile(RegexModelicaSyntaxHighlighter.comment_string, "gm");
		assertEquals(true, e3.test("\"\"+\"\""));
		assertEquals(true, e3.test("func();\\n\"abcde\" +\"fg\""));

		RegExp e5 = RegExp.compile(RegexModelicaSyntaxHighlighter.numbers, "gm");
		assertEquals(true, e5.test("100"));
		assertEquals(true, e5.test("func();\\n1.2e90"));
		assertEquals(true, e5.test("func();-3.1415926;"));

		RegExp e6 = RegExp.compile(RegexModelicaSyntaxHighlighter.keywords, "m");
		assertEquals(true, e6.test(" annotation="));
		assertEquals(true, e6.test(" annotation "));
		assertEquals(false, e6.test("sannotation"));
		assertEquals(false, e6.test(" string "));
		
		// mixed
		RegExp m1 = RegExp.compile("("+RegexModelicaSyntaxHighlighter.comment_c+")|(" + 
				RegexModelicaSyntaxHighlighter.string+")", "m");
		assertEquals(true, m1.test("func();\\n/*abc\n\rdef\nhij*/\nfunc();"));
		assertEquals(true, m1.test("func();\\n\"abcde\""));
	}
}
