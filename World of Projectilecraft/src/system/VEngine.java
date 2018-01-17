/**
 * 文件名称：VEngine.java
 * 类路径：system
 * 描述：TODO 程序入口类
 * 作者：Demilichzz
 * 时间：2011-10-26上午03:11:55
 * 版本：Ver 1.0
 */
package system;

import file.FileIO;
import global.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import config.ProjectConfig;
import controller.*;
import data.GameData;
import driver.*;
import jgui.*;

/**
 * @author Demilichzz
 *
 */
public class VEngine {
	public static JFrame frm;		// 游戏窗体
	public static GamePanel p;		// 窗体中用于绘制图像的主体Panel
	public static Simulator s;		// 仿真器
	public static GameState gs;		// 游戏状态
	public static GameListener glistener; // 游戏监听器
	public static GameMouseListener mlistener;	//鼠标监听器
	public static GameWindowListener wlistener;
	public static boolean developmode = true;		//开发模式，用于控制资源文件是由data中解压还是直接使用文件夹
	public static String version = "v0.9";	//版本号
	
	private static Dimension d;
	
	public VEngine(){
		Init();
		Animeconst.prepareAnime();
	}
	public static void main(String[] args) {
		boolean b = FileIO.checkLock();
		if(!b){		//如果检查不成功，则阻止启动
			Debug.DebugPopFrame("已经有一个游戏进程启动");
			//System.exit(0);
		}
		else{
			FileIO.createLock();	//启动时创建锁
			ProjectConfig.LoadConfig();	//加载项目配置信息
			InitResource();	//初始化游戏资源
			VEngine newgame = new VEngine();
			newgame.Gameloop();
		}
	}

	private static void InitResource() {
		if(!developmode){
			FileIO.unPackAllResources();
		}
		Debug.DebugTestTimeStart();
		// TODO 初始化游戏调用的资源
		Imageconst.Init(); //初始化图像资源
		Debug.DebugTestTimeEnd("初始化图像", true);
		Global.Init();		//初始化全局常量
		Animeconst.Init(); //初始化动画资源
		Soundconst.Init(); //初始化声音资源
		//Textconst.Init();
		GameData.InitData();
		if(!developmode){
			FileIO.clearResourceFile();
		}
	}
	private void Gameloop() {
		// TODO 仿真器对游戏状态的更新循环
		new Thread(s).start();	//建立新线程用于仿真器
	}
	public void Init() {
		// TODO 初始化游戏窗体
		//	创建游戏窗体并添加窗体监听器
		frm = new JFrame();
		frm.setLayout(null);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		wlistener = new GameWindowListener();
		frm.addWindowListener(wlistener);
		
		//textbox = new TextCanvas();
		//textbox.setSize(200,35);
		//textbox.setLocation(100, 100);
		//textbox.setVisible(false);		//设置文本框并使其在初始时隐藏
		
		//	定义窗体中的游戏面板区域，覆盖整个窗体范围
		p = new GamePanel(this);
		//d =new Dimension(800,600);
		//p.setPreferredSize(d);
		p.setSize(800, 600);
		p.setLocation(0, 0);
		p.setOpaque(true);

		glistener = new GameListener(this);	//添加游戏监听器,监听游戏输入
		mlistener = new GameMouseListener(this);//添加鼠标监听器
		frm.addKeyListener(glistener);
		p.addKeyListener(glistener);
		p.addMouseListener(mlistener);
		p.addMouseMotionListener(mlistener);
		
		gs = new GameState(this,10);	//创建游戏状态,并设置GS更新时间为10ms
		s = new Simulator(this);	//初始化仿真器
		gs.Init();				//初始化游戏状态
		
		frm.add(p);	//将游戏面板添加到窗体
		//frm.add(textbox);
		
		frm.setSize(806, 628);
		frm.setResizable(false);
		frm.setLocation(100, 10);
		frm.setTitle("WorldofProjectilecraft");
		frm.setVisible(true);
		frm.requestFocus();
	}
	
}
