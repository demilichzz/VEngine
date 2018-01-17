/**
 * @author Demilichzz
 *	静态数据类，存储游戏中使用的不会变化的静态数据，包括像素块的纹理索引等数据
 * 2013-6-25
 */
package data;

import org.newdawn.slick.Color;

/**
 * @author Demilichzz
 *
 * 2013-6-25
 */
public class StaticData {
	public static String[] gempiclist ={"fire.png","water.png","wood.png","light.png","dark.png","heart.png","poison.png","jammer.png","poisonB.png"};
	public static Color[] colorlist = {Color.red,Color.blue,Color.green,new Color(255,255,95),new Color(205,65,205),new Color(240,240,240)};
}
