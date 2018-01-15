/**
 * �ļ����ƣ�Debug.java
 * ��·����global
 * ������TODO �������������Ϣ�������õĹ�����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-2-28����07:20:44
 * �汾��Ver 1.0
 */
package global;

import jgui.DialogPanel;
import timer.VLuaAction;
import timer.VTimer;
import config.ProjectConfig;
import data.GameData;

/**
 * @author Demilichzz
 * 
 */
public class Debug {
	private static long debugtime = 0;
	private static int debugint = 0;
	private static int debugvalue = 0;
	protected static int debugcount = 0;	//����
	protected static VTimer spritetm;	//������������ʾ�ļ�ʱ��
	
	public static void resetDebugCount(){
		debugcount=0;
	}
	public static void addDebugCount(){
		// TODO ����Debug���� 
		debugcount++;
		DebugSimpleMessage("------"+debugcount+"------");
	}
	public static void DebugSimpleMessage(String text) {
		// TODO ���Debug��Ϣ
		if (true){//ProjectConfig.getConfigValue(ProjectConfig.DEBUGMODE) == 1) {
			System.out.println(text);
		}
	}
	public static void DebugPopFrame(String text){
		// TODO ���Debug��Ϣ�ڵ����Ĵ�����
		if (true){//ProjectConfig.getConfigValue(ProjectConfig.DEBUGMODE) == 1) {
			DialogPanel d = new DialogPanel();
			d.setText(text);
			//System.out.println(text);
		}
	}
	public static void DebugSpriteNum(){
		// TODO ��ʾ������
		spritetm = new VTimer(1000,1000000,true,new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				Debug.DebugSimpleMessage("Amount of Sprites:"+GameData.spriteList.size());
				Debug.DebugSimpleMessage("Time:"+System.currentTimeMillis());
			}			
		});
		spritetm.timerStart();
	}
	public static void DebugTestTimeStart(){
		// TODO ��������ʱ�俪ʼ
		debugtime = new Long(System.currentTimeMillis());
	}
	
	public static long DebugTestTimeEnd(String s, boolean b){
		// TODO ��������ʱ�����
		long d = System.currentTimeMillis() - debugtime;	
		if(s!=null&&b){
			if(d>9){
				System.out.println(s+"---"+d+"---");
			}
			else if(d>=0){
				System.out.println(s+":"+d);
			}
		}
		debugvalue  = (int) (debugvalue+d);
		debugint++;
		if(debugint>=100){
			debugint = 0;
			System.out.println("!!!!!Averge "+s+" = "+debugvalue/100);
			debugvalue = 0;
		}
		return d;
	}

}
