/**
 * �ļ����ƣ�VStage.java
 * ��·����stage
 * ������TODO ��Ϸ�ؿ���,���ڴ洢������ͬ����Ϸ�ؿ��������Ĳ�ͬ��Ϸ�߼�������
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-5����06:42:15
 * �汾��Ver 1.0
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
	protected String sid;		//�ؿ�ΨһID,������GS�Ĺؿ��б�������
	protected FSMclass fsm_sstage;
	protected boolean inited = false;
	public VTimerProcessor tp;		//�ؿ��ļ�ʱ��������
	public int updatecount = 0;	//�ؿ����¼���
	/**
	 * @param gs   �ؿ���������Ϸ״̬
	 * @param sid  �ؿ�ID,Ψһ
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
		// TODO ��ʼ���ؿ�
		tp = new VTimerProcessor();
		inited = true;
	}
	public void resetStageState(){
		// TODO ���ùؿ�״̬
	}
	public void fsmStateTransition(int fsm_action){
		// TODO �����״̬������״̬����״̬ת��
		//fsm_p.SetCurrentState(fsm_p.StateTransition(fsm_action));
	}
	public int getfsmState(){
		// TODO ��ȡ״̬
		return fsm_sstage.GetCurrentState();
	}
	public void updateStage(){
		// TODO ����GS��״̬���µ�ǰ�ؿ�
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
