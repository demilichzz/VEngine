/**
 * 文件名称：ProjectConfig.java
 * 类路径：config
 * 描述：TODO 项目配置，在项目启动时加载配置文件
 * 作者：Demilichzz
 * 时间：2012-2-29上午04:50:14
 * 版本：Ver 1.0
 */
package config;

import global.Debug;

import interfaces.VInt;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Demilichzz
 * 
 */
public class ProjectConfig {
	// ----------值列表索引-------------------
	public static int DEBUGMODE = 0;
	public static int SHOWUIBORDER = 1;
	public static int SOUND_VOLUME = 2;
	public static int MUSIC_VOLUME = 3;
	public static int PROMPT_TIME = 4;
	public static int HIT_SOUND = 5;
	//----------------------------------------
	public static VInt configvaluelist[] = {new VInt(1),new VInt(0),new VInt(50),new VInt(50),new VInt(3),new VInt(0)};		//通用设置参数列表
	public static String configparamlist[] = {"DEBUGMODE","SHOWUIBORDER","SOUND_VOLUME","MUSIC_VOLUME","PROMPT_TIME","HIT_SOUND"};	//通用设置描述列表
	
	protected static String keyconfigurl = new String("data/KeyConfig.ini");
	protected static String configurl = new String("data/GameConfig.ini"); 
	
	public static void LoadConfig() {
		// Need Code
		// LoadConfigFromFile
		try {
			loadGameConfig(configparamlist);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Debug.DebugSimpleMessage("载入游戏设置");
	}
	
	public static int getConfigValue(int i){
		// TODO 通过索引获取参数值
		return configvaluelist[i].getValue(0);
	}
	public static VInt getConfigVValue(int i){
		// TODO 通过索引获取参数的动态值接口
		return configvaluelist[i];
	}
	public static void setConfigValue(int index,int value){
		// TODO 通过索引设置参数值
		configvaluelist[index].setValue(0, value);
	}
	                                           
	public static void loadGameConfig(String[] paramlist) throws Exception{
		// TODO 载入游戏设置
		int[] valuelist = new int[paramlist.length]; // 配置值数组
		Exception e;
		int i = 0;
		File f = new File(configurl);		
		if (!f.exists()) {			//若ini文件不存在
			Debug.DebugSimpleMessage("未找到GameConfig.ini,将重新生成按键配置");
			f.createNewFile();
			for(int vi=0;vi<6;vi++){
				valuelist[vi]=configvaluelist[vi].getValue(0);
			}
			saveCommonConfig("GameConfig", valuelist, paramlist, configurl);
		} 
		else {
			valuelist = loadCommonConfig("GameConfig",paramlist,configurl);	//载入游戏配置中的参数值
			for(int vi=0;vi<6;vi++){
				configvaluelist[vi].setValue(0, valuelist[vi]);		//设置动态值接口值
			}
		}
		
	}
	
	public static void saveGameConfig(){
		// TODO 保存当前游戏设置为ini
		int[] valuelist = new int[6];
		for(int vi=0;vi<6;vi++){
			valuelist[vi]=configvaluelist[vi].getValue(0);
		}
		try {
			saveCommonConfig("GameConfig", valuelist, configparamlist, configurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int[] loadKeyConfig(String[] paramlist) throws IOException, Exception {
		// TODO 载入键盘设置，返回键位映射表
		int[] keylist = new int[paramlist.length]; // 初始化按键映射表数组
		int i = 0;
		File f = new File(keyconfigurl);
		if (!f.exists()) {			//若ini文件不存在
			Debug.DebugSimpleMessage("未找到KeyConfig.ini,将重新生成按键配置");
			f.createNewFile();
			keylist = loadKeyDefaultConfig(paramlist);
		} 
		else {
			keylist = loadCommonConfig("KeyConfig",paramlist,keyconfigurl);
		}
		return keylist;
	}
	public static int[] loadKeyDefaultConfig(String[] paramlist) throws Exception {
		// TODO Auto-generated method stub
		int[] keylist = new int[11]; // 初始化按键映射表数组
		keylist[0] = KeyEvent.VK_UP;
		keylist[1] = KeyEvent.VK_DOWN;
		keylist[2] = KeyEvent.VK_LEFT;
		keylist[3] = KeyEvent.VK_RIGHT;
		keylist[4] = KeyEvent.VK_ENTER;
		keylist[5] = KeyEvent.VK_SPACE;
		keylist[6] = KeyEvent.VK_ESCAPE;
		keylist[7] = KeyEvent.VK_Z;
		keylist[8] = KeyEvent.VK_X;
		keylist[9] = KeyEvent.VK_C;
		keylist[10] = KeyEvent.VK_SHIFT;

		saveCommonConfig("KeyConfig",keylist,paramlist,keyconfigurl);
		return keylist;
		
	}

	/**
	 * @param section		配置类别,不带[]
	 * @param valuelist		值列表
	 * @param configparam	参数解释列表,必须与值列表长度相等并一一对应
	 * @param configurl 	配置文件地址
	 * @throws Exception
	 */
	public static void saveCommonConfig(String section,int[] valuelist,String[] configparam,String configurl) throws Exception{
		// TODO 通用的ini配置文件保存方案
		if(valuelist.length!=configparam.length){
			throw(new Exception("配置文件参数值与参数解释无法对应"));
		}
		File f = new File(configurl);
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		PrintWriter pw = new PrintWriter(fos);
		String culine = "["+section+"]";		
		pw.println(culine);
		for(int i=0;i<valuelist.length;i++){
			culine=configparam[i]+"="+valuelist[i];
			pw.println(culine);
		}
		pw.close();
		fos.close();
	}
	
	public static int[] loadCommonConfig(String section,String[]paramlist,String configurl) throws Exception{
		// TODO 通用的 ini配置文件加载方案
		int[] valuelist=new int[paramlist.length];
		int i=0;
		File f = new File(configurl);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		line=br.readLine();
		if(!line.equals("["+section+"]")){
			throw(new Exception("加载的ini文件段落不符"));
		}
		while ((line = br.readLine()) != null) {
			if (line.equals(section)) {
				continue;
			}
			else if(line.equals("")){
				continue;
			}
			else {
				if (line.contains(paramlist[i])) {
					line = line.split(paramlist[i]+"=")[1];
					//line = line.substring(line.length() - 2, line.length());
					int value = Integer.parseInt(line);
					valuelist[i] = value;
					i++;
				}
			}
		}
		br.close();
		fr.close();
		return valuelist;
	}
}
