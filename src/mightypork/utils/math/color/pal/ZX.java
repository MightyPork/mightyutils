package mightypork.utils.math.color.pal;


import mightypork.utils.math.color.Color;


/**
 * ZX Spectrum palette
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface ZX {

	Color BLACK = Color.fromHex(0x000000);
	Color GRAY = Color.fromHex(0xCBCBCB);
	Color WHITE = Color.fromHex(0xFFFFFF);

	Color RED_DARK = Color.fromHex(0xD8240F);
	Color RED_LIGHT = Color.fromHex(0xFF3016);
	Color MAGENTA_DARK = Color.fromHex(0xD530C9);
	Color MAGENTA_LIGHT = Color.fromHex(0xFF3FFC);
	Color BLUE_DARK = Color.fromHex(0x001DC8);
	Color BLUE_LIGHT = Color.fromHex(0x0027FB);
	Color CYAN_DARK = Color.fromHex(0x00C9CB);
	Color CYAN_LIGHT = Color.fromHex(0xFFFD33);
	Color GREEN_DARK = Color.fromHex(0x00C721);
	Color GREEN_LIGHT = Color.fromHex(0x00F92C);

	Color YELLOW_DARK = Color.fromHex(0xCECA26);
	Color YELLOW_LIGHT = Color.fromHex(0xFFFD33);
}
