/**
 * 文件名称：Global.java
 * 类路径：global
 * 描述：TODO
 * 作者：Demilichzz
 * 时间：2011-10-26上午08:33:11
 * 版本：Ver 1.0
 */
package global;

import java.awt.Color;
import java.awt.Font;

/**
 * @author Demilichzz
 *
 */
public class Global {
	public static final String version = "0.9 beta";
	//-----------VERSION----------------------------------
	public static final int FPS = 100; // FPS
	public static final Font f = new Font(null, Font.PLAIN, 16);
	public static final Color c = Color.WHITE;
	public static final Font f_threat = new Font(null,Font.BOLD,32);
	public static final Font f_config = new Font(null,Font.BOLD,32);
	public static final Color c_config = Color.BLACK;
	//-----------FONT----------------------------------
	public static Font Font_common_font = new Font(null, Font.BOLD, 16);
	public static Color Font_common_color = Color.GREEN;
	public static final Font Font_DBMTimer = new Font(null, Font.BOLD, 16);
	public static final Color Color_DBMTimer = Color.WHITE;
	public static final Color[] Color_Laser = new Color[10];
	public static final Color[] Color_LaserBottom = new Color[10];
	//----------碰撞类型---------------------------------
	public static final int CTYPE_ROUND = 0;		//圆形碰撞
	public static final int CTYPE_LASER = 1;		//直线激光碰撞
	public static final int CTYPE_CHAINROUND = 2;	//圆形链碰撞
	
	public static void Init(){
		// TODO 初始化
		Color_Laser[0]=new Color(255,0,0);
		Color_Laser[1]=new Color(255,170,0);
		Color_Laser[2]=new Color(170,255,0);
		Color_Laser[3]=new Color(0,255,0);
		Color_Laser[4]=new Color(0,255,170);
		Color_Laser[5]=new Color(0,170,255);
		Color_Laser[6]=new Color(0,0,255);
		Color_Laser[7]=new Color(170,0,255);
		Color_Laser[8]=new Color(255,0,170);
		Color_Laser[9]=new Color(0,0,0);
		Color_LaserBottom[0]=new Color(255,102,102,100);
		Color_LaserBottom[1]=new Color(255,204,102,100);
		Color_LaserBottom[2]=new Color(204,255,102,100);
		Color_LaserBottom[3]=new Color(102,255,102,100);
		Color_LaserBottom[4]=new Color(102,255,204,100);
		Color_LaserBottom[5]=new Color(102,204,255,100);
		Color_LaserBottom[6]=new Color(102,102,255,100);
		Color_LaserBottom[7]=new Color(204,102,255,100);
		Color_LaserBottom[8]=new Color(255,102,204,100);
		Color_LaserBottom[9]=new Color(102,102,102,100);
	}
}
