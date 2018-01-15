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
	protected double time;					//����ʱ��,-1�����ó���
	protected int time_f;					//����
	public int stack = 1;			//�ѵ�����
	protected int maxstack = 1;
	//------------Ч������--------------------------
	public int dr = 0;	//�ܵ��˺������ٷֱ�	��������
	
	public VBuff(String ID,int time){
		this.ID = ID;
		setTime(time);
		alive=true;
	}
	public void setTime(double time){
		// TODO ���ó���ʱ��
		this.time = time;
		time_f = (int) (time*1000);
	}
	public void setMaxStack(int s){
		// TODO �������ѵ�
		maxstack=s;
	}
	public void addStack() {
		// TODO ��Ӷѵ������ʱ��
		if(stack+1<=maxstack){		//��δ�����ѵ�
			stack++;			//��ѵ�����+1
		}
		timeReset();		//ˢ��ʱ��
	}
	public void timeReset(){
		// TODO ����ʱ��
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
