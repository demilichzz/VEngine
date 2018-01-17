/**
 * 文件名称：VTimer.java
 * 类路径：timer
 * 描述：TODO 计时器类，用于实现定时事件
 * 作者：Demilichzz
 * 时间：2012-3-4上午03:02:10
 * 版本：Ver 1.0
 */
package timer;

import system.VEngine;

/**
 * @author Demilichzz
 *
 */
public class VTimer {
	protected VTimerProcessor tp = VEngine.newgame.gs.gs_tp;	//处理器
	protected VLuaAction action;						//计时器行为接口
	protected int time;									//计时器间隔(ms)
	protected int endTime;								//计时器结束时间(ms)
	protected int tpf;									//每次帧更新时间(ms)
	protected boolean perodic;							//是否可重复
	protected boolean destroy = false;					//是否应被销毁状态
	protected int runTime = 0;							//计时器已运行时间
	//-----------控制计时器将添加到哪一个处理器的参数常量------------------------------------------
	public static final int TIMER_GAMESTATE=0;		//游戏状态
	public static final int TIMER_STAGE=1;			//当前关卡
	
	public VTimer(int time,int endTime,boolean perodic,VLuaAction action){
		this.action = action;
		tpf = VEngine.newgame.gs.getMSecond();
		this.time = time;
		this.endTime = endTime;
		this.perodic = perodic;
	}
	public void setAction(VLuaAction action){
		this.action = action;
	}
	public void timerStart(){
		// TODO 默认的timerStart将timer添加到游戏关卡的计时器处理器中
		timerStart(1);
	}
	public void timerStart(int index){
		switch(index){
		case 0:{
			tp.addTimer(this);
			break;
		}
		case 1:{
			VEngine.newgame.gs.getCurrentStage().tp.addTimer(this);
			break;
		}
		}
	}
	public void die(){
		destroy=true;
	}
	public void update() {
		// TODO 更新状态
		runTime = runTime+tpf;
		if(runTime>=time){	//指定时间已到
			action.action();				//执行action的计时器行为
			if(perodic){	//若Timer可重复
				if(endTime!=-1000){		//-1000则永久持续
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
		// TODO 获取是否为销毁状态
		return destroy;
	}
	public int getRunTime(){
		return runTime;
	}
}
