/**
 * 
 */
package combat;

import system.VEngine;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class VBuff {
	public String ID;
	public boolean alive;
	protected double time;					//持续时间,-1则永久持续
	protected int time_f;					//毫秒
	public int stack = 1;			//堆叠次数
	protected int maxstack = 1;
	//------------效果参数--------------------------
	public int dr = 0;	//受到伤害修正百分比	正增负减
	
	public VBuff(String ID,int time){
		this.ID = ID;
		setTime(time);
		alive=true;
	}
	public void setTime(double time){
		// TODO 设置持续时间
		this.time = time;
		time_f = (int) (time*1000);
	}
	public void setMaxStack(int s){
		// TODO 设置最大堆叠
		maxstack=s;
	}
	public void addStack() {
		// TODO 添加堆叠或更新时间
		if(stack+1<=maxstack){		//若未到最大堆叠
			stack++;			//则堆叠次数+1
		}
		timeReset();		//刷新时间
	}
	public void timeReset(){
		// TODO 重置时间
		time_f = (int) (time*1000);
	}

	public void lifeMinus() {
		// TODO Auto-generated method stub
		if(time_f!=-1000){
			time_f = time_f-VEngine.gs.getMSecond();
			if(time_f<=0){
				alive=false;
			}
		}
	}
	public double getDR(){
		return dr*stack;
	}
	
	
}
