package mightypork.utils.math.color.pal;


import mightypork.utils.math.color.Color;


/**
 * PAL16 palette via http://androidarts.com/palette/16pal.htm
 * 
 * @author Ondřej Hruška (MightyPork)
 */
public interface PAL16 {
	
	Color VOID = Color.fromHex(0x000000);
	Color ASH = Color.fromHex(0x9D9D9D);
	Color BLIND = Color.fromHex(0xFFFFFF);
	
	Color BLOODRED = Color.fromHex(0xBE2633);
	Color PIGMEAT = Color.fromHex(0xE06F8B);
	
	Color OLDPOOP = Color.fromHex(0x493C2B);
	Color NEWPOOP = Color.fromHex(0xA46422);
	Color BLAZE = Color.fromHex(0xEB8931);
	Color ZORNSKIN = Color.fromHex(0xF7E26B);
	
	Color SHADEGREEN = Color.fromHex(0x2F484E);
	Color LEAFGREEN = Color.fromHex(0x44891A);
	Color SLIMEGREEN = Color.fromHex(0xA3CE27);
	
	Color NIGHTBLUE = Color.fromHex(0x1B2632);
	Color SEABLUE = Color.fromHex(0x005784);
	Color SKYBLUE = Color.fromHex(0x31A2F2);
	Color CLOUDBLUE = Color.fromHex(0xB2DCEF);
}
