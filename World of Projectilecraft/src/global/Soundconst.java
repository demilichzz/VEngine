/**
 * �ļ����ƣ�Soundconst.java
 * ��·����global
 * ������TODO �ڳ�ʼ��ʱ��ȫ�ִ洢ʹ�õ������ļ���Դ
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-3����03:41:40
 * �汾��Ver 1.0
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
	public static HashMap<String, VSound> sound_hashmap = new HashMap<String, VSound>();  //���ļ����洢�����ļ���Hashmap
	//public static ArrayList<Clip> cliplist = new ArrayList<Clip>();		//ѭ��ģʽ����Ƶ�����б�
	public static HashMap<String, VSoundPack> sp_hashmap = new HashMap<String,VSoundPack>();
	public static VQueueSound speech=null;		//������Ƶ�����ڿ���Boss���������в���
	public static VSoundPack bgm;			//��������
	
	public static void Init(){
		Debug.DebugSimpleMessage("��ʼ����Ч��Դ");
		StoreFolder("res/Sound/",-20,VSound.TYPE_SOUND,0);
		StoreFolder("res/Sound/Speech",-15,VSound.TYPE_MUSIC,0);
		StoreFolder("res/Sound/Music",-20,VSound.TYPE_MUSIC,1);
		GetSoundByName("wow_main_theme_1.mp3").setMode(1);
		GetSoundByName("se_plst00.wav").setBaseVolume(-70);
		speech=new VQueueSound();
		bgm = sp_hashmap.get("BGMusic");
		initPack();
		//GetSoundByName("se_plst00.wav").setMode(true);
		prepareSound();			//Ԥ�����Ա�����ε����ӳ�
	}
	public static void initPack(){
		// TODO ��ʼ����Ƶ���б�
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
		sp = new VSoundPack(9);		//Bossս��������
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
		// ����hashmap��������
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
		// TODO ���ļ�����ȡ��Ƶ
		if (filename != null) {
			VSound i = sound_hashmap.get(filename);
			if (i != null) {
				return i;
			} else {
				//Debug.DebugSimpleMessage("δ�ҵ�ָ����Ч" + filename);
				return null;
			}
		} else {
			return null;
		}
	}
	public static VSoundPack GetSoundPack(String name){
		// TODO �������ַ�����ȡ��Ƶ��
		VSoundPack p = sp_hashmap.get(name);
		if(p!=null){
			return p;
		}
		else{
			Debug.DebugSimpleMessage("δ�ҵ�ָ����Ƶ��"+name);
			return null;
		}
	}
	public static void prepareSound(){
		//--------����ʱ�俪ʼ-------------
		Debug.DebugTestTimeStart();
		// TODO ��GS����ʱ���ã���������ѭ��ģʽ��Ч��״̬��
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
		Debug.DebugTestTimeEnd("��ʼ����Ƶ�ļ�", true);
		//-------����ʱ�����-----------
	}
	public static void StoreFolder(String ad,int basevolume,int type, int mode) {
		// TODO ��Ŀ���ļ����µ������ļ��洢��ti_hashmap��
		File f = new File(ad); // �½��ļ�ʵ��
		File[] list = f.listFiles(); // �˴���ȡ�ļ����µ������ļ�
		for (int i = 0; i < list.length; i++) {
			String s = list[i].getName();
			if(list[i].exists()&&!list[i].isDirectory()){
				VSound sound = new VSound(list[i]);
				sound.setBaseVolume(basevolume);	//���û�������
				sound.setType(type);		//�������
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
