/**
 * �ļ����ƣ�Simulator.java
 * ��·����driver
 * ������TODO ������
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-10-26����08:31:34
 * �汾��Ver 1.0
 */
package driver;

import system.*;
import global.*;

/**
 * @author Demilichzz
 *
 */
public class Simulator implements Runnable {
	protected VEngine ve;
	private GameState gs;
	private long lastLoopTime=0;
	private long delta; // �������θ��¼��ʱ��
	private int time; // ����delta����������Ӧ����ʱ��ʱ,�����ִ�еĴ���
	private int i;
	private double rTime; // ��Ⱦ��Ӧ�ý�����Ⱦ��ʱ���
	private double spf = 1000.0/60.0;
	private long lastRenderTime=0;
	private double lastrender;
	private double renderRate;		//ÿ����Ⱦ���ʱ��
	private double timeToRender=0;	//���´���Ⱦ��Ҫ������ʱ��
	
	private int updaterate=10;	//������
	private int timeresidue;	//��ǰʱ��ֵ����

	public Simulator(VEngine ve) {
		this.ve = ve;
		gs = ve.gs;
		updaterate=gs.getMSecond();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run(){
		// TODO �������߳����к���
		gs.Render();
		lastLoopTime = System.currentTimeMillis();
		rTime = System.currentTimeMillis();
		lastRenderTime = System.currentTimeMillis();
		lastrender=lastRenderTime;
		renderRate = 1000.0/Global.FPS;
		while (true) {
			//Debug.DebugSimpleMessage("check");
			//if(FileIO.lock.exists()){		//�����
			//FileIO.lock.setReadOnly();
			//}
			lastLoopTime=lastLoopTime+updaterate;		//���ϴθ���ʱ�������
			//Debug.DebugSimpleMessage("����֡");
			gs.updateState();			
			try {
				Thread.sleep(0);		//�ͷŸ����̣߳���������Ⱦ
			} catch (InterruptedException e) {
			}
			while(lastRenderTime+renderRate<=System.currentTimeMillis()){	//����ϴ���Ⱦʱ��+ÿ֡ʱ��<=��ǰʱ��s
				lastrender=lastrender+renderRate;			//�����ϴ���Ⱦʱ��
				lastRenderTime = (long) lastrender;
				//Debug.DebugSimpleMessage("render");
				gs.Render();				//������Ⱦ
				try {
					Thread.sleep(0);		//�ͷŸ����߳�
				} catch (InterruptedException e) {
				}
				while(lastLoopTime+updaterate<=System.currentTimeMillis()){		//��֡
					//Debug.DebugSimpleMessage("��֡");
					lastLoopTime=lastLoopTime+updaterate;
					gs.updateState();	
				}
			}
			try {
				while(System.currentTimeMillis()-lastLoopTime<updaterate){	//�����ǰʱ��û����һ�θ���ʱ��
					Thread.sleep(0,100000);		//�ͷ��߳̽��еȴ�
				}
			}
			catch (InterruptedException e) {
			}
		}
	}

	/*public void run() {
		// TODO Auto-generated method stub
		gs.Render();		//��ʼ��Ⱦ
		lastLoopTime = System.currentTimeMillis();
		rTime = System.currentTimeMillis();
		lastRenderTime = System.currentTimeMillis();
		renderRate = 1000.0/Global.FPS;
		while (true) {
			// ----------------��Ϸ״̬�ı�---------------
			gs.updateState();
			// -------------------------------------------
			delta = System.currentTimeMillis() - lastLoopTime; //���ü��ʱ��=ϵͳ��ǰʱ��-�ϴ�ѭ��������ʱ��
			//--------d>��Ⱦ����,����Ⱦһ��,������d��ֵ���ϴ���Ⱦʱ��----------
			if (delta > renderRate) {
				gs.Render();
				lastRenderTime = System.currentTimeMillis();
				delta = System.currentTimeMillis() - lastLoopTime;
			}
			//--------d<״̬��������,���´���Ⱦʣ��ʱ��<״̬��������-d,������Ⱦ�����״̬----------
			timeToRender = renderRate + lastRenderTime - System
					.currentTimeMillis();
			if (delta <= gs.getMSecond()) {
				if(timeToRender < gs.getMSecond() - delta){
					if (timeToRender > 0) { //��Ϊ�������ڿ��ܱ�Ԥ�Ƶĳ�,��˵������������,timerToRender��<0,��ֱ����Ⱦ
						try {
							Thread.sleep((long) timeToRender);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					gs.fps = (int) (1000 / (System.currentTimeMillis() - lastRenderTime));
					gs.Render();
					lastRenderTime = System.currentTimeMillis();
				}	
				delta = System.currentTimeMillis() -  lastLoopTime;
			}
			//--------״̬���¿���-------------------------
			//--------d>״̬��������,���ظ�����״̬---------------------------------
			//--------��ʹd��ֵ�ڼ�ȥһ�θ��»��ѵ�ʱ��Ļ����ϸ���,ֱ��d<��������----
			else if(gs.getMSecond()<delta){
				while (gs.getMSecond() <= delta) {
					time = (int) (delta / gs.getMSecond());
					for (i = 0; i < time; i++) {
						gs.updateState();
					}
					lastLoopTime = lastLoopTime + (time*gs.getMSecond());
					delta = System.currentTimeMillis() - lastLoopTime;
					//delta = delta - (gs.getMSecond() * time);
				}
			}
			else{
				delta = System.currentTimeMillis() -  lastLoopTime;
			}
			//--------����һѭ������dӦ<=��������--------------------------
			//--------�ȴ���������-d��ʱ��,���и���-----------------------
			try {
				while(System.currentTimeMillis()-lastLoopTime<gs.getMSecond()){
					Thread.sleep(1);
				}
			} catch (InterruptedException e) {
			}
			lastLoopTime = System.currentTimeMillis();
		}
	}*/
}
