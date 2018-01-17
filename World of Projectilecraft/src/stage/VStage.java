/**
 * 文件名称：VStage.java
 * 类路径：stage
 * 描述：TODO 游戏关卡类,用于存储并处理不同的游戏关卡所包含的不同游戏逻辑及参数
 * 作者：Demilichzz
 * 时间：2012-3-5上午06:42:15
 * 版本：Ver 1.0
 */
package stage;

import controller.*;
import interfaces.*;
import fsm.FSMclass;
import global.*;
import system.*;
import timer.VTimerProcessor;

/**
 * @author Demilichzz
 *
 */
public class VStage implements VKeyProcessorInterface{
	protected String sid;		//关卡唯一ID,用于在GS的关卡列表中索引
	protected FSMclass fsm_sstage;
	protected boolean inited = false;
	public VTimerProcessor tp;		//关卡的计时器处理器
	public int updatecount = 0;	//关卡更新计数
	/**
	 * @param gs   关卡依附的游戏状态
	 * @param sid  关卡ID,唯一
	 */
	public VStage(){
		
	}
	public VStage(GameState gs,String sid){
		this.sid = sid;
		try {
			gs.addStage(this);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getStageID(){
		return sid;
	}
	public void Init(){
		// TODO 初始化关卡
		tp = new VTimerProcessor();
		inited = true;
	}
	public void resetStageState(){
		// TODO 重置关卡状态
	}
	public void fsmStateTransition(int fsm_action){
		// TODO 对玩家状态机输入状态进行状态转换
		//fsm_p.SetCurrentState(fsm_p.StateTransition(fsm_action));
	}
	public int getfsmState(){
		// TODO 获取状态
		return fsm_sstage.GetCurrentState();
	}
	public void updateStage(){
		// TODO 随着GS的状态更新当前关卡
		if(!inited){
			Init();
		}
		tp.process();
	}
	/**
	 * @param keystate
	 */
	public void processKeyAction(int[] keystate) {
		// TODO Auto-generated method stub
	}
}
