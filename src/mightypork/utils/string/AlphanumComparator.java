/*
 * The Alphanum Algorithm is an improved sorting algorithm for strings
 * containing numbers.  Instead of sorting numbers in ASCII order like
 * a standard sort, this algorithm sorts numbers in numeric order.
 *
 * The Alphanum Algorithm is discussed at http://www.DaveKoelle.com
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package mightypork.utils.string;


import java.util.Comparator;


/**
 * String comparator taking care of strings with numbers.
 *
 * @author Daniel Migowski
 * @author Andre Bogus
 * @author David Koelle
 * @author Ondřej Hruška (MightyPork)
 */
public class AlphanumComparator implements Comparator<String> {

	public static final AlphanumComparator instance = new AlphanumComparator();


	private final boolean isDigit(char ch)
	{
		return ch >= '0' && ch <= '9';
	}


	/**
	 * Length of string is passed in for improved efficiency (only need to
	 * calculate it once)
	 **/
	private final String getChunk(String s, int slength, int marker)
	{
		final StringBuilder chunk = new StringBuilder();
		char c = s.charAt(marker);
		chunk.append(c);
		marker++;

		if (isDigit(c)) {
			while (marker < slength) {
				c = s.charAt(marker);
				if (!isDigit(c)) break;
				chunk.append(c);
				marker++;
			}
		} else {
			while (marker < slength) {
				c = s.charAt(marker);
				if (isDigit(c)) break;
				chunk.append(c);
				marker++;
			}
		}
		return chunk.toString();
	}


	@Override
	public int compare(String s1, String s2)
	{
		int thisMarker = 0;
		int thatMarker = 0;
		final int s1Length = s1.length();
		final int s2Length = s2.length();

		while (thisMarker < s1Length && thatMarker < s2Length) {
			final String thisChunk = getChunk(s1, s1Length, thisMarker);
			thisMarker += thisChunk.length();

			final String thatChunk = getChunk(s2, s2Length, thatMarker);
			thatMarker += thatChunk.length();

			// If both chunks contain numeric characters, sort them numerically
			int result = 0;
			if (isDigit(thisChunk.charAt(0)) && isDigit(thatChunk.charAt(0))) {
				// Simple chunk comparison by length.
				final int thisChunkLength = thisChunk.length();
				result = thisChunkLength - thatChunk.length();
				// If equal, the first different number counts
				if (result == 0) {
					for (int i = 0; i < thisChunkLength; i++) {
						result = thisChunk.charAt(i) - thatChunk.charAt(i);
						if (result != 0) {
							return result;
						}
					}
				}
			} else {
				result = thisChunk.compareTo(thatChunk);
			}

			if (result != 0) return result;
		}

		return s1Length - s2Length;
	}
}
