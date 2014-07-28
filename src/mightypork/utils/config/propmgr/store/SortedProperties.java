package mightypork.utils.config.propmgr.store;


import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Properties stored in file, alphabetically sorted.<br>
 * Uses UTF-8 encoding and each property can have it's own comment.<br>
 * FIXME The quality of this class is dubious. It would probably be a good idea
 * to rewrite it without using {@link java.util.Properties} at all.
 *
 * @author Ondřej Hruška (MightyPork)
 */
class SortedProperties extends java.util.Properties {

	/** Comments for individual keys */
	private final Hashtable<String, String> keyComments = new Hashtable<>();


	private static void writeComments(BufferedWriter bw, String comm) throws IOException
	{
		final String comments = comm.replace("\n\n", "\n \n");

		final int len = comments.length();
		int current = 0;
		int last = 0;
		final char[] uu = new char[6];
		uu[0] = '\\';
		uu[1] = 'u';
		while (current < len) {
			final char c = comments.charAt(current);
			if (c > '\u00ff' || c == '\n' || c == '\r') {
				if (last != current) {
					bw.write("# " + comments.substring(last, current));
				}

				if (c > '\u00ff') {
					uu[2] = hexDigit(c, 12);
					uu[3] = hexDigit(c, 8);
					uu[4] = hexDigit(c, 4);
					uu[5] = hexDigit(c, 0);
					bw.write(new String(uu));
				} else {
					bw.newLine();
					if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n') {
						current++;
					}
				}
				last = current + 1;
			}
			current++;
		}
		if (last != current) {
			bw.write("# " + comments.substring(last, current));
		}

		bw.newLine();
		bw.newLine();
		bw.newLine();
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public synchronized Enumeration keys()
	{
		final Enumeration keysEnum = super.keys();
		final Vector keyList = new Vector();
		while (keysEnum.hasMoreElements()) {
			keyList.add(keysEnum.nextElement());
		}
		Collections.sort(keyList); //sort!
		return keyList.elements();
	}


	private static String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode)
	{
		final int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		final StringBuffer result = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			final char ch = theString.charAt(x);

			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((ch > 61) && (ch < 127)) {
				if (ch == '\\') {
					result.append('\\');
					result.append('\\');
					continue;
				}
				result.append(ch);
				continue;
			}

			switch (ch) {
				case ' ':
					if (x == 0 || escapeSpace) {
						result.append('\\');
					}
					result.append(' ');
					break;

				case '\t':
					result.append('\\');
					result.append('t');
					break;

				case '\n':
					result.append('\\');
					result.append('n');
					break;

				case '\r':
					result.append('\\');
					result.append('r');
					break;

				case '\f':
					result.append('\\');
					result.append('f');
					break;

				case '=': // Fall through
				case ':': // Fall through
				case '#': // Fall through
				case '!':
					result.append('\\');
					result.append(ch);
					break;

				default:
					if (((ch < 0x0020) || (ch > 0x007e)) & escapeUnicode) {
						result.append('\\');
						result.append('u');
						result.append(hexDigit(ch, 12));
						result.append(hexDigit(ch, 8));
						result.append(hexDigit(ch, 4));
						result.append(hexDigit(ch, 0));
					} else {
						result.append(ch);
					}
			}
		}

		return result.toString();
	}


	/**
	 * Set additional comment to a key
	 *
	 * @param key key for comment
	 * @param comment the comment
	 */
	public void setKeyComment(String key, String comment)
	{
		keyComments.put(key, comment);
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void store(OutputStream out, String comments) throws IOException
	{
		final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

		final boolean escUnicode = false;
		boolean firstEntry = true;
		String lastSectionBeginning = "";

		if (comments != null) {
			writeComments(bw, comments);
		}

		synchronized (this) {
			for (final Enumeration e = keys(); e.hasMoreElements();) {
				boolean wasNewLine = false;

				String key = (String) e.nextElement();
				String val = (String) get(key);
				key = saveConvert(key, true, escUnicode);
				val = saveConvert(val, false, escUnicode);

				// separate sections
				if (!lastSectionBeginning.equals(key.split("[.]")[0])) {
					if (!firstEntry) {
						bw.newLine();
						bw.newLine();
					}

					wasNewLine = true;
					lastSectionBeginning = key.split("[.]")[0];
				}

				if (keyComments.containsKey(key)) {
					String cm = keyComments.get(key);
					cm = cm.replace("\r", "\n");
					cm = cm.replace("\r\n", "\n");
					cm = cm.replace("\n\n", "\n \n");

					final String[] cmlines = cm.split("\n");

					// newline before comments
					if (!wasNewLine && !firstEntry) {
						bw.newLine();
					}

					for (final String cmline : cmlines) {
						bw.write("# " + cmline);
						bw.newLine();
					}
				}

				bw.write(key + " = " + val);
				bw.newLine();

				firstEntry = false;
			}
		}
		bw.flush();
	}


	private static String escapifyStr(String str)
	{
		final StringBuilder result = new StringBuilder();

		final int len = str.length();
		for (int x = 0; x < len; x++) {
			final char ch = str.charAt(x);
			if (ch <= 0x007e) {
				result.append(ch);
				continue;
			}

			result.append('\\');
			result.append('u');
			result.append(hexDigit(ch, 12));
			result.append(hexDigit(ch, 8));
			result.append(hexDigit(ch, 4));
			result.append(hexDigit(ch, 0));
		}
		return result.toString();
	}


	private static char hexDigit(char ch, int offset)
	{
		final int val = (ch >> offset) & 0xF;
		if (val <= 9) {
			return (char) ('0' + val);
		}

		return (char) ('A' + val - 10);
	}


	@Override
	public synchronized void load(InputStream is) throws IOException
	{
		load(is, "utf-8");
	}


	public void load(InputStream is, String encoding) throws IOException
	{
		final StringBuilder sb = new StringBuilder();
		final InputStreamReader isr = new InputStreamReader(is, encoding);
		while (true) {
			final int temp = isr.read();
			if (temp < 0) {
				break;
			}

			final char c = (char) temp;
			sb.append(c);
		}

		// discard comments
		final String read = sb.toString().replaceAll("(#|;|//|--)[^\n]*\n", "\n");

		final String inputString = escapifyStr(read);
		final byte[] bs = inputString.getBytes("ISO-8859-1");
		final ByteArrayInputStream bais = new ByteArrayInputStream(bs);

		super.load(bais);
	}
}
