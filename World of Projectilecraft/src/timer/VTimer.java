/**
 * �ļ����ƣ�VTimer.java
 * ��·����timer
 * ������TODO ��ʱ���࣬����ʵ�ֶ�ʱ�¼�
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-4����03:02:10
 * �汾��Ver 1.0
 */
package timer;

import system.VEngine;

/**
 * @author Demilichzz
 *
 */
public class VTimer {
	protected VTimerProcessor tp = VEngine.gs.gs_tp;	//������
	protected VLuaAction action;						//��ʱ����Ϊ�ӿ�
	protected int time;									//��ʱ�����(ms)
	protected int endTime;								//��ʱ������ʱ��(ms)
	protected int tpf;									//ÿ��֡����ʱ��(ms)
	protected boolean perodic;							//�Ƿ���ظ�
	protected boolean destroy = false;					//�Ƿ�Ӧ������״̬
	protected int runTime = 0;							//��ʱ��������ʱ��
	//-----------���Ƽ�ʱ������ӵ���һ���������Ĳ�������------------------------------------------
	public static final int TIMER_GAMESTATE=0;		//��Ϸ״̬
	public static final int TIMER_STAGE=1;
	
	public VTimer(int time,int endTime,boolean perodic,VLuaAction action){
		this.action = action;
		tpf = VEngine.gs.getMSecond();
		this.time = time;
		this.endTime = endTime;
		this.perodic = perodic;
	}
	public void setAction(VLuaAction action){
		this.action = action;
	}
	public void timerStart(){
		// TODO Ĭ�ϵ�timerStart��timer��ӵ���Ϸ�ؿ��ļ�ʱ����������
		timerStart(1);
	}
	public void timerStart(int index){
		switch(index){
		case 0:{
			tp.addTimer(this);
			break;
		}
		case 1:{
			VEngine.gs.getCurrentStage().tp.addTimer(this);
			break;
		}
		}
	}
	public void die(){
		destroy=true;
	}
	public void update() {
		// TODO ����״̬
		runTime = runTime+tpf;
		if(runTime>=time){	//ָ��ʱ���ѵ�
			action.action();				//ִ��action�ļ�ʱ����Ϊ
			if(perodic){	//��Timer���ظ�
				if(endTime!=-1000){		//-1000�����ó���
					endTime = endTime-runTime;
				}
				if(endTime==0){
					endTime=-1;
				}
				runTime=runTime-time;
			}
			else{
				destroy=true;
			}
		}
		if(runTime>=endTime&&endTime!=0&&endTime!=-1000){
			destroy=true;
		}
	}

	public boolean getDestroy() {
		// TODO ��ȡ�Ƿ�Ϊ����״̬
		return destroy;
	}
	
}
