package mightypork.utils.math.color.pal;


import mightypork.utils.math.color.Color;


/**
 * CGA palette
 *
 * @author Ondřej Hruška (MightyPork)
 */
public interface CGA {

	Color BLACK = Color.fromHex(0x000000);
	Color GRAY_DARK = Color.fromHex(0x686868);
	Color GRAY_LIGHT = Color.fromHex(0xB8B8B8);
	Color WHITE = Color.fromHex(0xFFFFFF);

	Color RED_DARK = Color.fromHex(0xC41F0C);
	Color RED_LIGHT = Color.fromHex(0xFF706A);
	Color MAGENTA_DARK = Color.fromHex(0xC12BB6);
	Color MAGENTA_LIGHT = Color.fromHex(0xFF76FD);
	Color BLUE_DARK = Color.fromHex(0x0019B6);
	Color BLUE_LIGHT = Color.fromHex(0x5F6EFC);
	Color CYAN_DARK = Color.fromHex(0x00B6B8);
	Color CYAN_LIGHT = Color.fromHex(0x23FCFE);
	Color GREEN_DARK = Color.fromHex(0x00B41D);
	Color GREEN_LIGHT = Color.fromHex(0x39FA6F);

	Color YELLOW = Color.fromHex(0xFFFD72);
	Color BROWN = Color.fromHex(0xC16A14);
}
