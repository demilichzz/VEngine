/**
 * 文件名称：Simulator.java
 * 类路径：driver
 * 描述：TODO 仿真器
 * 作者：Demilichzz
 * 时间：2011-10-26上午08:31:34
 * 版本：Ver 1.0
 */
package driver;

import org.lwjgl.opengl.*;

import display.VDisplay;

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
	private long delta; // 计算两次更新间隔时间
	private int time; // 计算delta超过仿真器应更新时间时,需额外执行的次数
	private int i;
	private double rTime; // 渲染器应该进行渲染的时间点
	private double spf = 1000.0/60.0;
	private long lastRenderTime=0;
	private double lastrender;
	private double renderRate;		//每次渲染间隔时间
	private double timeToRender=0;	//到下次渲染需要经过的时间
	
	private int updaterate=10;	//更新率
	private int timeresidue;	//当前时间值余数

	public Simulator(VEngine ve) {
		this.ve = ve;
		gs = ve.gs;
		updaterate=gs.getMSecond();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run(){
		// TODO 仿真器线程运行函数
		ve.renderer.initGL();		//初始化OpenGL参数
		ve.initResource();			//初始化资源，字体
		ve.gs.Init();
		ve.renderer.renderPrepare();
		gs.Render();
		ve.renderer.renderPrepare();
		//renderGL();
		lastLoopTime = System.currentTimeMillis();
		rTime = System.currentTimeMillis();
		lastRenderTime = System.currentTimeMillis();
		lastrender=lastRenderTime;
		renderRate = 1000.0/Global.FPS;
		while (!Display.isCloseRequested()) {
			//Debug.DebugSimpleMessage("check");
			//if(FileIO.lock.exists()){		//检查锁
			//FileIO.lock.setReadOnly();
			//}
			lastLoopTime=lastLoopTime+updaterate;		//将上次更新时间向后移
			//Debug.DebugSimpleMessage("正常帧");
			gs.updateState();			
			try {
				Thread.sleep(0);		//释放更新线程，并进行渲染
			} catch (InterruptedException e) {
			}
			while(lastRenderTime+renderRate<=System.currentTimeMillis()){	//如果上次渲染时间+每帧时间<=当前时间s
				lastrender=lastrender+renderRate;			//更新上次渲染时间
				lastRenderTime = (long) lastrender;
				//Debug.DebugSimpleMessage("render");
				//renderGL();
				gs.Render();				//进行渲染
				try {
					Thread.sleep(0);		//释放更新线程
				} catch (InterruptedException e) {
				}
				while(lastLoopTime+updaterate<=System.currentTimeMillis()){		//补帧
					//Debug.DebugSimpleMessage("补帧");
					lastLoopTime=lastLoopTime+updaterate;
					gs.updateState();	
				}
			}
			try {
				while(System.currentTimeMillis()-lastLoopTime<updaterate){	//如果当前时间没到下一次更新时间
					Thread.sleep(0,100000);		//释放线程进行等待
				}
			}
			catch (InterruptedException e) {
			}
		}
		//上述循环在Display获取关闭请求时结束，之后关闭Lua脚本的加载
		System.out.println("退出游戏");
		VEngine.newgame.editframe.dispose();
		if(gs.lua_core!=null){
			gs.lua_core.destroyScript();
		}
		Display.destroy();
	}
	/*public void run() {
		// TODO Auto-generated method stub
		gs.Render();		//初始渲染
		lastLoopTime = System.currentTimeMillis();
		rTime = System.currentTimeMillis();
		lastRenderTime = System.currentTimeMillis();
		renderRate = 1000.0/Global.FPS;
		while (true) {
			// ----------------游戏状态改变---------------
			gs.updateState();
			// -------------------------------------------
			delta = System.currentTimeMillis() - lastLoopTime; //设置间隔时间=系统当前时间-上次循环结束的时间
			//--------d>渲染周期,则渲染一次,并更新d的值和上次渲染时间----------
			if (delta > renderRate) {
				gs.Render();
				lastRenderTime = System.currentTimeMillis();
				delta = System.currentTimeMillis() - lastLoopTime;
			}
			//--------d<状态更新周期,且下次渲染剩余时间<状态更新周期-d,即先渲染后更新状态----------
			timeToRender = renderRate + lastRenderTime - System
					.currentTimeMillis();
			if (delta <= gs.getMSecond()) {
				if(timeToRender < gs.getMSecond() - delta){
					if (timeToRender > 0) { //因为运行周期可能比预计的长,因此当这种情况发生,timerToRender会<0,则直接渲染
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
			//--------状态更新控制-------------------------
			//--------d>状态更新周期,则重复更新状态---------------------------------
			//--------并使d的值在减去一次更新花费的时间的基础上更新,直到d<更新周期----
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
			//--------经上一循环过后d应<=更新周期--------------------------
			//--------等待更新周期-d的时间,进行更新-----------------------
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
