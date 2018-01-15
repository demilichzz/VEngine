/**
 * �ļ����ƣ�Animeconst.java
 * ��·����global
 * ������TODO �ڳ�ʼ��ʱԤ����ʹ�õĶ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-11-4����11:29:25
 * �汾��Ver 1.0
 */
package global;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sound.VSoundInterface;
import view.*;
/**
 * @author Demilichzz
 *
 */
public class Animeconst {
	public static HashMap<String, VAnime> anime_hashmap = new HashMap<String, VAnime>();
	
	public static void Init(){
		//StoreAnime("config_value_anime.png",12,"config_value_anime",false,0);
		StoreAnime("Sprite_born_anime.png",20,"Sprite_born_anime",false,VAnime.ANIMEMODE_LASTFRAME,10);
		StoreAnime("pc_iceblock.png",40,"pc_iceblock_anime",true,VAnime.ANIMEMODE_LASTFRAME,10);
		StoreAnime("SkillIcon_Anime.png",20,"skillcd_anime",true,VAnime.ANIMEMODE_PERODIC,8);
		//StoreAnime("UI_Cursor_Editor.png",8,"UICursor");
	}
	
	public static VImageInterface GetAnimeByName(String filename){
		// TODO ���ļ�����ȡVImageInterface��ʽ��ͼ��
		VImageInterface i = anime_hashmap.get(filename);
		if(i!=null){
			return i;
		}
		else{
			Debug.DebugSimpleMessage("δ�ҵ�ָ��ͼ��"+filename);
			return null;
		}
	}
	public static VAnime GetAnimeInstance(String filename){
		VAnime i = anime_hashmap.get(filename);
		if(i!=null){
			return i.getInstance();
		}
		else{
			return null;
		}
	}
	private static void StoreAnime(String str, int i, String index,boolean b,int mode,int pllist) {
		// TODO Auto-generated method stub
		VAnime va = new VAnime(str,i);
		va.setPlayList(pllist);
		va.setState(b);
		va.setMode(mode);
		anime_hashmap.put(index,va);
	}
	public static void resetAnime(){
		// TODO �������ж����Ĳ��ż���ʹ֮Ϊ0
		Iterator iter = anime_hashmap.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    VAnime a = (VAnime) entry.getValue();
		   	a.stateReset();
		}
	}
	public static void prepareAnime(){
		// TODO Ԥ������
		Iterator iter = anime_hashmap.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    VAnime a = (VAnime) entry.getValue();
		   	a.prepareAnime();
		}
	}
}
