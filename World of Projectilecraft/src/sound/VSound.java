/**
 * 	TODO ��Ƶ�࣬ʵ��wav��Ƶ�ļ��Ĳ���
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
	
	protected File sound;			//��Ƶ�ļ�
	protected VSoundThread thread=null;
	protected VSoundController parent=null;
	protected int mode =0;	//��Ƶ�Ƿ�Ϊ�ɿ���ģʽ
	protected double basevolume=0;		//��������
	protected int volume=50;		//����Ϸ���ò����ж��������ֵ����Χ0-100
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
		// TODO ���û�������
		basevolume=i;
		if(thread!=null){
			thread.setBaseVolume(basevolume);
		}
	}
	public void setVolume(int v){
		// TODO ��ϵͳ����������������ֵ��ΧΪ0-100
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
		// TODO ������Ƶ
		if (volume > 0) {	//������0ʱ�Ž��в��� 
			if (mode != 0) {
				if (thread == null) {	//����threadΪ��ʱ�Ŵ����̣߳������ظ�����ͬһ��Ƶ�Ĳ�ͬ�߳�
					thread = new VSoundThread(sound);	//������߼����õ���Ƶ��������Ϊ��Ƶ�����¼��ص�����
				}
				thread.setBaseVolume(basevolume);
				thread.setVolume(volume);
				thread.setMode(mode);
				if(thread.getCurrentState()==FSMconst.SOUND_STATE_STOP){
					thread.stateTransition(FSMconst.SOUND_STATE_PLAY);	//����PLAY״̬
					Thread t = new Thread(thread);
					t.start();
				}
				else{		//�粻��STOP״̬���Ӧ�߳���������
					thread.stateTransition(FSMconst.SOUND_STATE_PLAY);
				}
			} 
			else { // �ǿɿ���ģʽ
				thread = new VSoundThread(sound);
				thread.setBaseVolume(basevolume);
				thread.setVolume(volume);
				thread.setMode(mode);
				thread.stateTransition(FSMconst.SOUND_STATE_PLAY);
				Thread t = new Thread(thread); // ����һ���µ���Ƶ�߳�ʵ�������ź��Զ�������Դ
				t.start();
				/*
				 * VSoundThread st = new VSoundThread(sound,this.mode);
				 * thread=st; st.setVolume(volume); Thread t=new Thread(st);
				 * //����һ���µ���Ƶ�߳�ʵ�������ź��Զ�������Դ t.start();
				 */
			}
		}
	}
	public void prepare(){
		thread = new VSoundThread(sound);
	}
	@Override
	public void soundStop() {
		// TODO ʹ�ɿ�ģʽ��Ƶֹͣѭ��
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
		// TODO ��ѭ����Ƶ�����¼�
		//Debug.DebugSimpleMessage("sss");
	}
}
