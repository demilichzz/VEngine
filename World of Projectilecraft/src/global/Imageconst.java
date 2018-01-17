/**
 * 文件名称：Imageconst.java
 * 类路径：global
 * 描述：TODO 在游戏加载时初始化,以字符串索引形式存储游戏用到的所有图像资源文件
 * 作者：Demilichzz
 * 时间：2011-10-27上午06:10:20
 * 版本：Ver 1.0
 */
package global;

import java.awt.*;
import java.io.*;
import java.util.*;

import view.*;

/**
 * @author Demilichzz
 * 
 */
public class Imageconst {
	public static HashMap<String, VImageInterface> ti_hashmap = new HashMap<String, VImageInterface>();  //按文件名存储图像的Hashmap
	public static VImageInterface[] ti_strlist;
	
	public static void Init() {
		Debug.DebugSimpleMessage("初始化图像资源");
		StoreFolder("res/UI");
		StoreFolder("res/Unit");
		StoreFolder("res/Anime");
		StoreFolder("res/Sprite",16,16,10,1);
		StoreFolder("res/Sprite/32x32",32,32,10,1);
		StoreFolder("res/Sprite/Player",64,64,1,1);
		StoreFolder("res/Sprite/24x24",24,24,10,1);
		StoreFolder("res/Sprite/Anime",64,64,10,1);
		StoreFolder("res/Sprite/Spec");
		/*StoreFolder("res/Char", 32, 48, 4, 4);
		StoreFolder("res/UI/Cursor");
		StoreFolder("res/UI/UI_V1.0");
		StoreFolder("res/Icons");
		setCrop("UI_Board_02.png", 30, 30, 3, 3);
		setCrop("config_value_anime.png",200,30,1,4);
		setCrop("combat_players.png",30,30,10,1);*/
		setCrop("Sprite_SBall.png",16,16,10,1);
		setCrop("Sprite_MBall.png",32,32,10,1);
		setCrop("Sprite_MBall_a.png",32,32,10,1);
		setCrop("Sprite_MBall_2circle.png",32,32,10,1);
		setCrop("Sprite_LBall.png",64,64,10,1);
		setCrop("Sprite_LBall_a_bottom.png",64,64,10,1);
		setCrop("Sprite_LBall_a_top.png",64,64,10,1);
		setCrop("Sprite_Shadow.png",128,128,1,1);
		setCrop("Sprite_laser_bottom.png",16,640,1,1);	
		setCrop("Sprite_laser_top.png",16,640,1,1);	
		setCrop("pc_iceblock.png",128,128,10,1);
		setCrop("SkillIcon_Anime.png",70,70,8,1);
		//------------将ti_hashmap内对象存储为数组,用于在JList中使用--------
		ti_strlist = new VImageInterface[ti_hashmap.size()];
		int i=0;
		for(VImageInterface vi : ti_hashmap.values()){
			ti_strlist[i]=vi;
			i++;
		}
	}

	private static void setCrop(String string, int w, int h, int i, int j) {
		// TODO 调用指定VImage的setCrop函数进行图像分割存储
		VImage ti = (VImage) ti_hashmap.get(string);
		ti.setCrop(w, h, i, j);
	}

	public static VImageInterface GetImageByName(String filename){
		// TODO 按文件名获取VImageInterface形式的图像
		VImageInterface i = ti_hashmap.get(filename);
		if(i!=null){
			return i;
		}
		else{
			//Debug.DebugSimpleMessage("未找到指定图像"+filename);
			return null;
		}
	}
	
	public static void StoreFolder(String ad) {
		// TODO 将目标文件夹下的所有文件存储至ti_hashmap中
		File f = new File(ad); // 新建文件实例
		File[] list = f.listFiles(); // 此处获取文件夹下的所有文件
		for (int i = 0; i < list.length; i++) {
			String s = list[i].getName();
			if(s.endsWith(".png")){
				VImage image = new VImage(list[i]);
				ti_hashmap.put(s, image);
			}
			else{
				VImage image = new VImage(list[i]);
				ti_hashmap.put(s, image);
			}
		}
	}

	public static void StoreFolder(String ad, int w, int h, int i, int j) {
		File f = new File(ad); // 新建文件实例
		File[] list = f.listFiles(); // 此处获取文件夹下的所有文件
		for (int ti = 0; ti < list.length; ti++) {
			if (!list[ti].isDirectory()) {
				String s = list[ti].getName();
				if(s.endsWith(".png")){
					VImage image = new VImage(list[ti]);
					image.setCrop(w, h, i, j);
					ti_hashmap.put(s, image);
				}
			}
		}
	}
}
