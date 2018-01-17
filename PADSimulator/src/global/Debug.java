/**
 * 文件名称：Debug.java
 * 类路径：global
 * 描述：TODO 用于输出错误信息及调试用的公有类
 * 作者：Demilichzz
 * 时间：2012-2-28上午07:20:44
 * 版本：Ver 1.0
 */
package global;

import org.newdawn.slick.Color;

import timer.*;
import ui.*;
import view.*;
import event.*;

/**
 * @author Demilichzz
 * 
 */
public class Debug {
	private static long debugtime = 0;
	private static int debugint = 0;
	private static int debugvalue = 0;
	protected static int debugcount = 0;	//计数
//	protected static VTimer spritetm;	//控制粒子数显示的计时器
	
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
	public static void DebugLogMessage(String text) {
		// TODO 输出战斗记录信息
		if (true){//ProjectConfig.getConfigValue(ProjectConfig.DEBUGMODE) == 1) {
			System.out.println(text);
		}
	}
	public static void DebugGamehintMessage(String text){
		// TODO 以游戏提示的形式输出信息
		final VUI hintui = GlobalEvent.getUIByID("uihint");
		hintui.clearText();
		VText hinttext = new VText(text,0,0,Color.white,"font_hint",true);
		hinttext.setLayout(VText.Layout_CENTER);
		hintui.addText(hinttext);
		hintui.setVisible(true);
		VTimer tm = new VTimer(2000,2000,false,new VLuaAction(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				hintui.setVisible(false);
			}
		});
		tm.timerStart();
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
