/**
 * 文件名称：Soundconst.java
 * 类路径：global
 * 描述：TODO 在初始化时在全局存储使用的声音文件资源
 * 作者：Demilichzz
 * 时间：2012-3-3上午03:41:40
 * 版本：Ver 1.0
 */
package global;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import config.ProjectConfig;

import sound.*;
import view.VImage;
import view.VImageInterface;

/**
 * @author Demilichzz
 *
 */
public class Soundconst {
	//public static HashMap<String, VSoundInterface> music_hashmap = new HashMap<String, VSoundInterface>();
	public static HashMap<String, VSound> sound_hashmap = new HashMap<String, VSound>();  //按文件名存储声音文件的Hashmap
	//public static ArrayList<Clip> cliplist = new ArrayList<Clip>();		//循环模式的音频数据列表
	public static HashMap<String, VSoundPack> sp_hashmap = new HashMap<String,VSoundPack>();
	public static VQueueSound speech=null;		//队列音频，用于控制Boss语音不并行播放
	public static VSoundPack bgm;			//背景音乐
	
	public static void Init(){
		Debug.DebugSimpleMessage("初始化音效资源");
		StoreFolder("res/Sound/",-20,VSound.TYPE_SOUND,0);
		StoreFolder("res/Sound/Speech",-15,VSound.TYPE_MUSIC,0);
		StoreFolder("res/Sound/Music",-20,VSound.TYPE_MUSIC,1);
		GetSoundByName("wow_main_theme_1.mp3").setMode(1);
		GetSoundByName("se_plst00.wav").setBaseVolume(-70);
		speech=new VQueueSound();
		bgm = sp_hashmap.get("BGMusic");
		initPack();
		//GetSoundByName("se_plst00.wav").setMode(true);
		prepareSound();			//预处理，以避免初次调用延迟
	}
	public static void initPack(){
		// TODO 初始化音频包列表
		VSoundPack sp = new VSoundPack(4);
		sp.addPrefix("HumanMale_Hit_", 3);
		sp.addPrefix("HumanMale_Death_", 1);
		sp_hashmap.put("HumanMale", sp);
		sp = new VSoundPack(4);
		sp.addPrefix("HumanFemale_Hit_", 4);
		sp.addPrefix("HumanFemale_Death_", 1);
		sp_hashmap.put("HumanFemale", sp);
		sp = new VSoundPack(4);
		sp.addPrefix("Ice_Lance_Impact_Hit_", 1);
		sp.addPrefix("HumanMale_Death_", 1);
		sp_hashmap.put("Biu", sp);
		sp = new VSoundPack(9);		//Boss战背景音乐
		sp.addPrefix("wow_main_theme_",1);
		sp.addPrefix("DS_Morchok_",5);
		sp.addPrefix("DS_Zonozz_",3);
		sp.addPrefix("DS_Hagara_",2);
		sp.addPrefix("DS_Ultraxion_",4);
		sp.addPrefix("DS_Blackhorn_",1);
		sp.addPrefix("DS_DWSpine_",2);
		sp.addPrefix("DS_DWMadness_",1);
		sp.addPrefix("Tavern_",1);
		sp_hashmap.put("BGMusic",sp);
		bgm=sp_hashmap.get("BGMusic");		
	}
	public static VQueueSound getSpeech(){
		return speech;
	}
	public static void setVolume(int v,int type){
		// 遍历hashmap设置音量
		Iterator iter = sound_hashmap.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
			VSound sound = (VSound) entry.getValue();
		    if(type!=VSound.TYPE_BOTH){
		    	if(sound.getType()==type){
		    		sound.setVolume(v);
		    	}
		    }
		    else{
		    	sound.setVolume(v);
		    }
		}
	}
	public static VSound GetSoundByName(String filename){
		// TODO 按文件名获取音频
		if (filename != null) {
			VSound i = sound_hashmap.get(filename);
			if (i != null) {
				return i;
			} else {
				//Debug.DebugSimpleMessage("未找到指定音效" + filename);
				return null;
			}
		} else {
			return null;
		}
	}
	public static VSoundPack GetSoundPack(String name){
		// TODO 按索引字符串获取音频包
		VSoundPack p = sp_hashmap.get(name);
		if(p!=null){
			return p;
		}
		else{
			Debug.DebugSimpleMessage("未找到指定音频包"+name);
			return null;
		}
	}
	public static void prepareSound(){
		//--------测试时间开始-------------
		Debug.DebugTestTimeStart();
		// TODO 在GS更新时调用，处理所有循环模式音效的状态。
		Iterator iter = sound_hashmap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			VSound sound = (VSound) entry.getValue();
			if(sound.getType()==VSound.TYPE_MUSIC){
				sound.setVolume(ProjectConfig.getConfigValue(ProjectConfig.MUSIC_VOLUME));
			}
			else if(sound.getType()==VSound.TYPE_SOUND){
				sound.setVolume(ProjectConfig.getConfigValue(ProjectConfig.SOUND_VOLUME));
			}
			sound.prepare();
		}
		Debug.DebugTestTimeEnd("初始化音频文件", true);
		//-------测试时间结束-----------
	}
	public static void StoreFolder(String ad,int basevolume,int type, int mode) {
		// TODO 将目标文件夹下的所有文件存储至ti_hashmap中
		File f = new File(ad); // 新建文件实例
		File[] list = f.listFiles(); // 此处获取文件夹下的所有文件
		for (int i = 0; i < list.length; i++) {
			String s = list[i].getName();
			if(list[i].exists()&&!list[i].isDirectory()){
				VSound sound = new VSound(list[i]);
				sound.setBaseVolume(basevolume);	//设置基础音量
				sound.setType(type);		//设置类别
				sound.setMode(mode);
				sound_hashmap.put(s, sound);
			}
		}
	}

	public static void clear() {
		// TODO Auto-generated method stub
		bgm.soundStop();
		bgm=null;
		speech.soundStop();
		speech=null;
		sound_hashmap.clear();
		sound_hashmap=null;
		sp_hashmap.clear();
		sp_hashmap=null;
	}
}
