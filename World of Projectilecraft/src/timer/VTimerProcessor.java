/**
 * 文件名称：VTimerProcessor.java
 * 类路径：timer
 * 描述：TODO 计时器处理器,将所有创建的计时器加入到处理器对象中进行统一处理
 * 作者：Demilichzz
 * 时间：2012-3-4上午02:59:47
 * 版本：Ver 1.0
 */
package timer;

import java.util.ArrayList;

/**
 * @author Demilichzz
 *
 */
public class VTimerProcessor {
	protected ArrayList<VTimer> preplist;	//预备列表
	protected ArrayList<VTimer> timerlist;		//计时器列表
	protected ArrayList<VTimer> destroylist;	//将销毁计时器列表
	protected int tpruntime;					//处理器运行次数
	protected boolean lock=false;
	
	public VTimerProcessor(){
		preplist = new ArrayList<VTimer>();
		timerlist = new ArrayList<VTimer>();
		destroylist = new ArrayList<VTimer>();
		tpruntime = 0;
	}
	
	public void process(){
		// TODO 在游戏状态更新时处理列表中所有计时器,并将状态为已销毁的计时器销毁
		tpruntime++;
		timerProcess(timerlist);
		timerDestroy(timerlist);
		timerAdd(timerlist);	//处理添加Timer请求
	}

	public void reset(){
		// TODO 重置计时器处理器的状态，清除所有现存Timer
		tpruntime = 0;
		preplist = new ArrayList<VTimer>();
		timerlist = new ArrayList<VTimer>();
		destroylist = new ArrayList<VTimer>();
	}
	public void addTimer(VTimer tm){
		if(!lock){
			timerlist.add(tm);
		}
		else{
			preplist.add(tm);
		}
	}
	private void timerProcess(ArrayList<VTimer> timerlist) {
		// TODO 处理timerlist中所有计时器的事件
		lock=true;
		for(VTimer tm:timerlist){
			tm.update();
		}
		lock=false;
	}
	private void timerDestroy(ArrayList<VTimer> timerlist) {
		// TODO 将timerlist中处于销毁状态的计时器移除
		for(VTimer tm:timerlist){
			if(tm.getDestroy()){
				destroylist.add(tm);
			}
		}
		timerlist.removeAll(destroylist);
		destroylist.clear();
	}
	private void timerAdd(ArrayList<VTimer> timerlist) {
		// TODO 将预备列表中的计时器添加
		if(preplist.size()>0){
			timerlist.addAll(preplist);
		}
		preplist.clear();
	}
}
