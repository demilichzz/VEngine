/**
 * �ļ����ƣ�GameWindowListener.java
 * ��·����controller
 * ������TODO ��Ϸ�����������������Ϸ����������Ӧ
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-4����04:08:38
 * �汾��Ver 1.0
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
		// TODO �ڹر���Ϸ����ʱ�ر����е��õ�Lua�ű�
		if(!VEngine.developmode){
			VEngine.gs.lua_core.destroyScript();
			FileIO.closeClear();
		}
		try {
			if(FileIO.lock_fOS!=null){
				FileIO.lock_fOS.close();	//�ر��ļ����������
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
