/**
 * 	TODO 音频类，实现wav音频文件的播放
 */
package sound;

import global.Debug;
import global.FSMconst;
import global.Soundconst;

import interfaces.VSoundController;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.applet.Applet;
import java.applet.AudioClip;
import javax.sound.sampled.*;

import sun.audio.*; 
import timer.VLuaAction;
import timer.VTimer;

/**
 * @author Demilichzz
 *
 * 2012-11-12
 */
public class VSound implements VSoundInterface,VSoundController{
	public static final int TYPE_SOUND=0;
	public static final int TYPE_MUSIC=1;
	public static final int TYPE_BOTH =-1;
	
	protected File sound;			//音频文件
	protected VSoundThread thread=null;
	protected VSoundController parent=null;
	protected int mode =0;	//音频是否为可控制模式
	protected double basevolume=0;		//基础音量
	protected int volume=50;		//在游戏设置参数中定义的音量值，范围0-100
	protected boolean endmark=false;	
	protected int type=TYPE_SOUND;			//
	
	public VSound(){
		parent=this;
	}
	public VSound(File sound) {
		this.sound = sound;
		parent=this;
	}
	public void setBaseVolume(double i) {
		// TODO 设置基础音量
		basevolume=i;
		if(thread!=null){
			thread.setBaseVolume(basevolume);
		}
	}
	public void setVolume(int v){
		// TODO 在系统设置中设置音量，值范围为0-100
		volume=v;
		if(thread!=null){
			thread.setVolume(volume);
		}
	}
	@Override
	public void soundPause() {
		// TODO Auto-generated method stub
		thread.stateTransition(FSMconst.SOUND_INPUT_PAUSE);
	}
	public void soundPlay(VSoundController p){
		soundPlay();
		thread.setController(p);
		thread.setMode(mode);
	}
	@Override
	public void soundPlay(){
		// TODO 播放音频
		if (volume > 0) {	//音量非0时才进行播放 
			if (mode != 0) {
				if (thread == null) {	//仅当thread为空时才创建线程，不会重复创建同一音频的不同线程
					thread = new VSoundThread(sound);	//传入最高级调用的音频控制器作为音频结束事件回调对象
				}
				thread.setBaseVolume(basevolume);
				thread.setVolume(volume);
				thread.setMode(mode);
				if(thread.getCurrentState()==FSMconst.SOUND_STATE_STOP){
					thread.stateTransition(FSMconst.SOUND_STATE_PLAY);	//输入PLAY状态
					Thread t = new Thread(thread);
					t.start();
				}
				else{		//如不是STOP状态则对应线程仍在运行
					thread.stateTransition(FSMconst.SOUND_STATE_PLAY);
				}
			} 
			else { // 非可控制模式
				thread = new VSoundThread(sound);
				thread.setBaseVolume(basevolume);
				thread.setVolume(volume);
				thread.setMode(mode);
				thread.stateTransition(FSMconst.SOUND_STATE_PLAY);
				Thread t = new Thread(thread); // 生成一个新的音频线程实例，播放后将自动回收资源
				t.start();
				/*
				 * VSoundThread st = new VSoundThread(sound,this.mode);
				 * thread=st; st.setVolume(volume); Thread t=new Thread(st);
				 * //生成一个新的音频线程实例，播放后将自动回收资源 t.start();
				 */
			}
		}
	}
	public void prepare(){
		thread = new VSoundThread(sound);
	}
	@Override
	public void soundStop() {
		// TODO 使可控模式音频停止循环
		if(thread!=null){
			//thread.setBaseVolume(-80);
			thread.stateTransition(FSMconst.SOUND_INPUT_STOP);
		}
	}
	public void setMode(int i) {
		// TODO Auto-generated method stub
		mode=i;
	}
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}
	public void setType(int i) {
		// TODO Auto-generated method stub
		type=i;
	}
	/* (non-Javadoc)
	 * @see interfaces.VSoundController#musicend()
	 */
	@Override
	public void musicend(int i) {
		// TODO 非循环音频结束事件
		//Debug.DebugSimpleMessage("sss");
	}
}
