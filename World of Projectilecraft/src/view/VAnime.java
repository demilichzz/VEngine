/**
 * 文件名称：VAnime.java
 * 类路径：view
 * 描述：TODO 通过将VImage中的分割图片按预定义的队列连续播放实现动画显示
 * 作者：Demilichzz
 * 时间：2011-11-4上午11:26:24
 * 版本：Ver 1.0
 */
package view;

import java.awt.Image;
import java.awt.image.*;

import system.*;
import global.*;

/**
 * @author Demilichzz
 *
 */
public class VAnime implements VImageInterface{
	//--------------动画模式常量-------------------------------
	public static final int ANIMEMODE_PERODIC = 0;		//循环模式
	public static final int ANIMEMODE_LASTFRAME = 1;	//末帧停止模式
	public static final int ANIMEMODE_SINGLE = 2;		//单次播放模式
	public static final int ANIMEMODE_RETURN = 3;		//往返模式
	
	protected VImageInterface imagesource;		//图像源
	protected String name;
	protected int frame = 1;					//每秒帧数
	protected double time = 1;					//每帧毫秒
	
	protected int plsize = 1;					//播放列表大小
	protected int playlist[];					//播放列表
	protected long animecount = 0;				//动画计数,以变化的值决定对调用者返回动画的哪一帧
	protected boolean stateanime = false;		//动画状态是否与GameState相关,若是,
												//则仅游戏状态为正常状态(非暂停之类)时动画计数才会增加
	protected int animemode = 1;		//动画模式，默认为末帧停止
	
	public VAnime(String str,int f){
		imagesource = Imageconst.GetImageByName(str);
		name=str;
		if(f>0){
			frame = f;
			time = 1000.0/f;
		}
	}
	public VAnime getInstance(){
		// TODO 获取动画副本，需要重复绘制同一个动画时使用，副本的动画计数为初始状态
		VAnime newanime = new VAnime(this.name,frame);
		newanime.setPlayList(plsize);
		return newanime;
	}
	public void setState(boolean b){
		// TODO 设置动画是否为与GS状态机有关的动画
		stateanime = b;
	}
	public void setMode(int i){
		animemode = i;
	}
	public void animeReset(){
		// TODO 重置动画播放
		if(stateanime){
			animecount = VEngine.gs.getCurrentStage().updatecount*VEngine.gs.getMSecond();
		}
		else{
			animecount = System.currentTimeMillis();
		}
	}
	public void stateReset(){
		animecount = 0;
	}
	public void setPlayList(int[] list){
		// TODO 设置自定义播放列表
		playlist = list;
		plsize = playlist.length;
	}
	public void setPlayList(int x){
		// TODO 顺序播放,x为播放列表大小
		plsize = x;
		playlist = new int[x];
		for(int i=0;i<x;i++){
			playlist[i]=i;
		}
	}
	public Image getImage(){
		int index=0;			//最终获取源VImageInterface图像的索引
		long uc=0;			//更新时间计数
		if(stateanime){		//如果是关卡状态动画（暂停时不更新）
			uc = VEngine.gs.getCurrentStage().updatecount*VEngine.gs.getMSecond();
		}
		else{			//否则更新时间计数为获取当前毫秒
			uc = System.currentTimeMillis();
		}
		switch(animemode){
		case ANIMEMODE_PERODIC:{		//循环模式
			if(animecount==0||uc-animecount>=time*plsize){
				//计数超出动画帧数组范围则回到开始
				animecount = uc;
			}
			index = playlist[(int)((uc-animecount)/time)];	//计算索引
			return imagesource.getImage(index);
		}
		case ANIMEMODE_LASTFRAME:{		//末帧停止模式
			if(animecount==0){
				//仅在初次调用时将animecount设置为当前时间
				animecount = uc;
			}
			int plindex = (int)((uc-animecount)/time);	//计算索引
			if(plindex>=plsize){
				plindex = plsize-1;	//如超出范围则设为尾帧
			}
			return imagesource.getImage(playlist[plindex]);
		}
		default:{
			return null;
		}
		}
	}
	public Image getImage(int i) {
		// TODO 获取动画指定帧的图像
		return imagesource.getImage(i);
	}
	public int getHeight() {
		// TODO Auto-generated method stub
		return imagesource.getHeight();
	}
	public int getWidth() {
		// TODO Auto-generated method stub
		return imagesource.getWidth();
	}

	public void prepareAnime() {
		// TODO Auto-generated method stub
 		for(int i=0;i<plsize;i++){
			if(imagesource.getImage(i)!=null){
				VEngine.p.prepareImage(imagesource.getImage(i), VEngine.p);
			}
		}
	}
}
