package mightypork.utils.files;


import java.io.File;


public class OsUtils {
	
	public static enum EnumOS
	{
		linux, macos, solaris, unknown, windows;
		
		public boolean isLinux()
		{
			return this == linux || this == solaris;
		}
		
		
		public boolean isMac()
		{
			return this == macos;
		}
		
		
		public boolean isWindows()
		{
			return this == windows;
		}
	}
	
	private static EnumOS cachedOs;
	
	
	public static File getHomeWorkDir(String dirname)
	{
		final String userhome = System.getProperty("user.home", ".");
		File file;
		
		switch (getOs()) {
			case linux:
			case solaris:
				file = new File(userhome, dirname + '/');
				break;
			
			case windows:
				final String appdata = System.getenv("APPDATA");
				
				if (appdata != null) {
					file = new File(appdata, dirname + '/');
				} else {
					file = new File(userhome, dirname + '/');
				}
				
				break;
			
			case macos:
				file = new File(userhome, "Library/Application Support/" + dirname);
				break;
			
			default:
				file = new File(userhome, dirname + "/");
				break;
		}
		
		return file;
	}
	
	
	public static EnumOS getOs()
	{
		if (cachedOs != null) return cachedOs;
		
		final String s = System.getProperty("os.name").toLowerCase();
		
		if (s.contains("win")) {
			cachedOs = EnumOS.windows;
			
		} else if (s.contains("mac")) {
			cachedOs = EnumOS.macos;
			
		} else if (s.contains("linux") || s.contains("unix")) {
			cachedOs = EnumOS.linux;
			
		} else if (s.contains("solaris") || s.contains("sunos")) {
			cachedOs = EnumOS.solaris;
			
		} else {
			cachedOs = EnumOS.unknown;
		}
		
		return cachedOs;
	}
	
}
