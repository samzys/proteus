package proteus.gwt.shared.util;

import java.io.IOException;

public class StringBufferInputStream extends InputStream {

	protected int position;

	public StringBufferInputStream(String text) {
		this.text = text;
	}

	@Override
	public synchronized int available() throws IOException {
		if (text != null) {
			return text.length();
		}

		return 0;
	}

	@Override
	public synchronized int read() {
		return (this.position == this.text.length()) ? -1 : this.text
				.charAt(this.position++);
	}

	@Override
	public int read(char[] b, int off, int len) {
		if (position >= text.length())
			return -1;
		int n = Math.min(text.length() - position, len);
		text.getChars(position, position + n, b, off);
		position += n;
		return n;
	}

	public synchronized void reset() {
		position = 0;
	}
}
