/**
 * 文件名称：Debug.java
 * 类路径：global
 * 描述：TODO 用于输出错误信息及调试用的公有类
 * 作者：Demilichzz
 * 时间：2012-2-28上午07:20:44
 * 版本：Ver 1.0
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
	protected static int debugcount = 0;	//计数
	protected static VTimer spritetm;	//控制粒子数显示的计时器
	
	public static void resetDebugCount(){
		debugcount=0;
	}
	public static void addDebugCount(){
		// TODO 增加Debug计数 
		debugcount++;
		DebugSimpleMessage("------"+debugcount+"------");
	}
	public static void DebugSimpleMessage(String text) {
		// TODO 输出Debug信息
		if (true){//ProjectConfig.getConfigValue(ProjectConfig.DEBUGMODE) == 1) {
			System.out.println(text);
		}
	}
	public static void DebugPopFrame(String text){
		// TODO 输出Debug信息于弹出的错误窗体
		if (true){//ProjectConfig.getConfigValue(ProjectConfig.DEBUGMODE) == 1) {
			DialogPanel d = new DialogPanel();
			d.setText(text);
			//System.out.println(text);
		}
	}
	public static void DebugSpriteNum(){
		// TODO 显示粒子数
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
		// TODO 测试运行时间开始
		debugtime = new Long(System.currentTimeMillis());
	}
	
	public static long DebugTestTimeEnd(String s, boolean b){
		// TODO 测试运行时间结束
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
