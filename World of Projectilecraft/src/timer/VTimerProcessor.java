/**
 * �ļ����ƣ�VTimerProcessor.java
 * ��·����timer
 * ������TODO ��ʱ��������,�����д����ļ�ʱ�����뵽�����������н���ͳһ����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-4����02:59:47
 * �汾��Ver 1.0
 */
package timer;

import java.util.ArrayList;

/**
 * @author Demilichzz
 *
 */
public class VTimerProcessor {
	protected ArrayList<VTimer> preplist;	//Ԥ���б�
	protected ArrayList<VTimer> timerlist;		//��ʱ���б�
	protected ArrayList<VTimer> destroylist;	//�����ټ�ʱ���б�
	protected int tpruntime;					//���������д���
	protected boolean lock=false;
	
	public VTimerProcessor(){
		preplist = new ArrayList<VTimer>();
		timerlist = new ArrayList<VTimer>();
		destroylist = new ArrayList<VTimer>();
		tpruntime = 0;
	}
	
	public void process(){
		// TODO ����Ϸ״̬����ʱ�����б������м�ʱ��,����״̬Ϊ�����ٵļ�ʱ������
		tpruntime++;
		timerProcess(timerlist);
		timerDestroy(timerlist);
		timerAdd(timerlist);	//�������Timer����
	}

	public void reset(){
		// TODO ���ü�ʱ����������״̬����������ִ�Timer
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
		// TODO ����timerlist�����м�ʱ�����¼�
		lock=true;
		for(VTimer tm:timerlist){
			tm.update();
		}
		lock=false;
	}
	private void timerDestroy(ArrayList<VTimer> timerlist) {
		// TODO ��timerlist�д�������״̬�ļ�ʱ���Ƴ�
		for(VTimer tm:timerlist){
			if(tm.getDestroy()){
				destroylist.add(tm);
			}
		}
		timerlist.removeAll(destroylist);
		destroylist.clear();
	}
	private void timerAdd(ArrayList<VTimer> timerlist) {
		// TODO ��Ԥ���б��еļ�ʱ�����
		if(preplist.size()>0){
			timerlist.addAll(preplist);
		}
		preplist.clear();
	}
}
