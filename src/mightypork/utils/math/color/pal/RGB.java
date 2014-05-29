package mightypork.utils.math.color.pal;


import mightypork.utils.math.color.Color;


/**
 * Basic RGB palette
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public class RGB {
	
	public static final Color BLACK_10 = Color.rgba(0, 0, 0, 0.1);
	public static final Color BLACK_20 = Color.rgba(0, 0, 0, 0.2);
	public static final Color BLACK_30 = Color.rgba(0, 0, 0, 0.3);
	public static final Color BLACK_40 = Color.rgba(0, 0, 0, 0.4);
	public static final Color BLACK_50 = Color.rgba(0, 0, 0, 0.5);
	public static final Color BLACK_60 = Color.rgba(0, 0, 0, 0.6);
	public static final Color BLACK_70 = Color.rgba(0, 0, 0, 0.7);
	public static final Color BLACK_80 = Color.rgba(0, 0, 0, 0.8);
	public static final Color BLACK_90 = Color.rgba(0, 0, 0, 0.9);
	
	
	public static final Color WHITE = Color.fromHex(0xFFFFFF);
	public static final Color BLACK = Color.fromHex(0x000000);
	public static final Color GRAY_DARK = Color.fromHex(0x808080);
	public static final Color GRAY = Color.fromHex(0xA0A0A0);
	public static final Color GRAY_LIGHT = Color.fromHex(0xC0C0C0);
	
	public static final Color RED = Color.fromHex(0xFF0000);
	public static final Color GREEN = Color.fromHex(0x00FF00);
	public static final Color BLUE = Color.fromHex(0x0000FF);
	
	public static final Color YELLOW = Color.fromHex(0xFFFF00);
	public static final Color CYAN = Color.fromHex(0x00FFFF);
	public static final Color MAGENTA = Color.fromHex(0xFF00FF);
	
	public static final Color PINK = Color.fromHex(0xFF3FFC);
	public static final Color ORANGE = Color.fromHex(0xFC4800);
	public static final Color BROWN = Color.fromHex(0x83501B);
	
	public static final Color NONE = Color.rgba(0, 0, 0, 0);
}
