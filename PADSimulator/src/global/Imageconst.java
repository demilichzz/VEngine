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
	public static HashMap<String, VTexture> txr_hashmap = new HashMap<String, VTexture>();  //按文件名存储图像的Hashmap
	public static VTexture[] txr_strlist;
	public static VTexture nullpic;
	public static String dynamicload_folder;
	
	public static void Init() {
		Debug.DebugTestTimeStart();
		//Debug.DebugSimpleMessage("初始化图像资源");
		StoreFolder("res/Pic/Preload");
		dynamicload_folder = "res/Pic/Dynamicload";
		//setCrop("terrain.png",16,16);
		//------------将ti_hashmap内对象存储为数组,用于在JList中使用--------
		txr_strlist = new VTexture[txr_hashmap.size()];
		int i=0;
		for(VTexture vi : txr_hashmap.values()){
			txr_strlist[i]=vi;
			i++;
		}
		nullpic = txr_hashmap.get("nullpic.png");
		Debug.DebugTestTimeEnd("初始化图像资源", true);
	}

	private static void setCrop(String string, int x,int y) {
		// TODO 调用指定VImage的setCrop函数进行图像分割存储
		VTexture ti = (VTexture) txr_hashmap.get(string);
		ti.setCrop(x,y);
	}

	public static VTexture GetImageByName(String filename){
		// TODO 按文件名获取VImageInterface形式的图像
		VTexture i = txr_hashmap.get(filename);
		if(i!=null){
			return i;
		}
		else{
			if(filename.startsWith("MONS")){
				return loadImage(dynamicload_folder+"/Monster",filename);
			}
			else{
				return loadImage(dynamicload_folder+"/Portrait",filename);
			}
			//Debug.DebugSimpleMessage("未找到指定图像"+filename);
		}
	}
	public static VTexture loadImage(String folder,String name){
		File f = new File(folder+"/"+name); // 新建文件实例
		VTexture image;
		try {
			image = new VTexture(f.getPath(),name);
			txr_hashmap.put(name, image);
			return image;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void StoreFolder(String ad) {
		// TODO 将目标文件夹下的所有文件存储至ti_hashmap中
		File f = new File(ad); // 新建文件实例
		StoreFolder(f);
	}
	public static void StoreFolder(File f){
		File[] list = f.listFiles(); // 此处获取文件夹下的所有文件
		//System.out.println(f.getName());
		for (int i = 0; i < list.length; i++) {
			String s = list[i].getName();
			//if(s.endsWith(".png")||s.endsWith(".PNG")){
			if(!list[i].isDirectory()){
				VTexture image = new VTexture(list[i].getPath(),s);
				txr_hashmap.put(s, image);
			}
			else{
				StoreFolder(list[i]);	//递归获取子文件夹
				//VTexture image = new VTexture(list[i].getPath());
				//txr_hashmap.put(s, image);
			}
		}
	}
/*	public static void StoreFolder(String ad, int w, int h, int i, int j) {
		File f = new File(ad); // 新建文件实例
		File[] list = f.listFiles(); // 此处获取文件夹下的所有文件
		for (int ti = 0; ti < list.length; ti++) {
			if (!list[ti].isDirectory()) {
				String s = list[ti].getName();
				if(s.endsWith(".png")){
					VImage image = new VImage(list[ti]);
					image.setCrop(w, h, i, j);
					txr_hashmap.put(s, image);
				}
			}
		}
	}*/
}
