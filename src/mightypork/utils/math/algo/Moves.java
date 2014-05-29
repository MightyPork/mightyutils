package mightypork.utils.math.algo;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import mightypork.utils.math.Calc;


/**
 * Move lists, bit masks and other utilities
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class Moves {
	
	public static final byte BIT_NW = (byte) 0b10000000;
	public static final byte BIT_N = (byte) 0b01000000;
	public static final byte BIT_NE = (byte) 0b00100000;
	public static final byte BIT_E = (byte) 0b00010000;
	public static final byte BIT_SE = (byte) 0b00001000;
	public static final byte BIT_S = (byte) 0b00000100;
	public static final byte BIT_SW = (byte) 0b00000010;
	public static final byte BIT_W = (byte) 0b00000001;
	
	public static final byte BITS_CARDINAL = BIT_N | BIT_S | BIT_E | BIT_W;
	public static final byte BITS_DIAGONAL = BIT_NE | BIT_NW | BIT_SE | BIT_SW;
	
	public static final byte BITS_NW_CORNER = BIT_W | BIT_NW | BIT_N;
	public static final byte BITS_NE_CORNER = BIT_E | BIT_NE | BIT_N;
	public static final byte BITS_SW_CORNER = BIT_W | BIT_SW | BIT_S;
	public static final byte BITS_SE_CORNER = BIT_E | BIT_SE | BIT_S;
	
	public static final Move NW = Move.make(-1, -1);
	public static final Move N = Move.make(0, -1);
	public static final Move NE = Move.make(1, -1);
	public static final Move E = Move.make(1, 0);
	public static final Move SE = Move.make(1, 1);
	public static final Move S = Move.make(0, 1);
	public static final Move SW = Move.make(-1, 1);
	public static final Move W = Move.make(-1, 0);
	
	//@formatter:off	
	/** All sides, in the order of bits. */
	public final static List<Move> ALL_SIDES = Collections.unmodifiableList(Arrays.asList(
		NW,
		N,
		NE,
		E,
		SE,
		S,
		SW,
		W
	));
	
	public final static List<Move> CARDINAL_SIDES = Collections.unmodifiableList(Arrays.asList(
		N,
		E,
		S,
		W
	));
	
	//@formatter:on
	
	/**
	 * Get element from all sides
	 * 
	 * @param i side index
	 * @return the side coord
	 */
	public static Move getSide(int i)
	{
		return ALL_SIDES.get(i);
	}
	
	
	public static byte getBit(int i)
	{
		return (byte) (1 << (7 - i));
	}
	
	
	public static Move randomCardinal()
	{
		return Calc.pick(CARDINAL_SIDES);
	}
	
	
	public static Move randomCardinal(Random rand)
	{
		return Calc.pick(rand, CARDINAL_SIDES);
	}
}
