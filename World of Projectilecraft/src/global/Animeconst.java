/**
 * 文件名称：Animeconst.java
 * 类路径：global
 * 描述：TODO 在初始化时预定义使用的动画
 * 作者：Demilichzz
 * 时间：2011-11-4上午11:29:25
 * 版本：Ver 1.0
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
		// TODO 按文件名获取VImageInterface形式的图像
		VImageInterface i = anime_hashmap.get(filename);
		if(i!=null){
			return i;
		}
		else{
			Debug.DebugSimpleMessage("未找到指定图像"+filename);
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
		// TODO 重置所有动画的播放计数使之为0
		Iterator iter = anime_hashmap.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    VAnime a = (VAnime) entry.getValue();
		   	a.stateReset();
		}
	}
	public static void prepareAnime(){
		// TODO 预备动画
		Iterator iter = anime_hashmap.entrySet().iterator(); 
		while (iter.hasNext()) { 
			Map.Entry entry = (Map.Entry) iter.next(); 
		    VAnime a = (VAnime) entry.getValue();
		   	a.prepareAnime();
		}
	}
}
