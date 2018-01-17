/**
 * @author Demilichzz
 *
 * 2013-6-8
 */
package global;

import org.lwjgl.Sys;

/**
 * @author Demilichzz
 *
 * 2013-6-8
 */
public class Global {
	public static final int keydelay = 200;
	public static final int FPS = 60;
	public static final int windowX = 1280;
	public static final int windowY = 800;
	public static int unitid = 0;
	
	public static long getTime() {		//使用lwjgl获取当前时间(毫秒)
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * @return
	 */
	public static int generateUnitID() {
		// TODO Auto-generated method stub
		unitid++;
		return unitid;
	}
}
