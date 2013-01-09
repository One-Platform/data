package com.sinosoft.one.data.jade.parsers.mysql.util;

import com.sinosoft.one.data.jade.parsers.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class MysqlStringUtil extends StringUtil {
	/**
	 * Returns the byte[] representation of the given string (re)using the given
	 * charset converter, and the given encoding.
	 * 
	 * @param s
	 *            the string to convert
	 * @param converter
	 *            the converter to reuse
	 * @param encoding
	 *            the character encoding to use
	 * @param serverEncoding
	 *            DOCUMENT ME!
	 * @param parserKnowsUnicode
	 *            DOCUMENT ME!
	 * 
	 * @return byte[] representation of the string
	 * 
	 * @throws java.sql.SQLException
	 *             if an encoding unsupported by the JVM is supplied.
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static final byte[] getBytes(String s,
			SingleByteCharsetConverter converter, String encoding,
			String serverEncoding, boolean parserKnowsUnicode)
			throws UnsupportedEncodingException {
		byte[] b = null;

		if (converter != null) {
			b = converter.toBytes(s);
		} else if (encoding == null) {
			b = s.getBytes();
		} else {
			b = s.getBytes(encoding);

			if (!parserKnowsUnicode && (encoding.equalsIgnoreCase("SJIS") //$NON-NLS-1$
					|| encoding.equalsIgnoreCase("BIG5") //$NON-NLS-1$
			|| encoding.equalsIgnoreCase("GBK"))) { //$NON-NLS-1$

				if (!encoding.equalsIgnoreCase(serverEncoding)) {
					b = escapeEasternUnicodeByteStream(b, s, 0, s.length());
				}
			}
		}
		return b;
	}

	/**
	 * Adds '+' to decimal numbers that are positive (MySQL doesn't understand
	 * them otherwise
	 * 
	 * @param dString
	 *            The value as a string
	 * 
	 * @return String the string with a '+' added (if needed)
	 */
	public static final String fixDecimalExponent(String dString) {
		int ePos = dString.indexOf("E"); //$NON-NLS-1$

		if (ePos == -1) {
			ePos = dString.indexOf("e"); //$NON-NLS-1$
		}

		if (ePos != -1) {
			if (dString.length() > (ePos + 1)) {
				char maybeMinusChar = dString.charAt(ePos + 1);

				if (maybeMinusChar != '-' && maybeMinusChar != '+') {
					StringBuffer buf = new StringBuffer(dString.length() + 1);
					buf.append(dString.substring(0, ePos + 1));
					buf.append('+');
					buf.append(dString.substring(ePos + 1, dString.length()));
					dString = buf.toString();
				}
			}
		}

		return dString;
	}


}
