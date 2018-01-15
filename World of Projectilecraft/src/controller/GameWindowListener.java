/**
 * 文件名称：GameWindowListener.java
 * 类路径：controller
 * 描述：TODO 游戏窗体监听器，控制游戏窗体的相关响应
 * 作者：Demilichzz
 * 时间：2012-3-4上午04:08:38
 * 版本：Ver 1.0
 */
package controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import file.FileIO;

import system.VEngine;

/**
 * @author Demilichzz
 *
 */
public class GameWindowListener implements WindowListener{
	public GameWindowListener(){

	}
	
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}
	public void windowClosed(WindowEvent e) {
		// TODO 在关闭游戏窗口时关闭所有调用的Lua脚本
		if(!VEngine.developmode){
			VEngine.gs.lua_core.destroyScript();
			FileIO.closeClear();
		}
		try {
			if(FileIO.lock_fOS!=null){
				FileIO.lock_fOS.close();	//关闭文件锁的输出流
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		System.exit(0);
	}
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
