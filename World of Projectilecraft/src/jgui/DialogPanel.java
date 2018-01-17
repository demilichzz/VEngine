/**
 * 	TODO 弹出式对话框
 */	
package jgui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameWindowListener;

/**
 * @author Demilichzz
 *
 * 2012-11-27
 */
public class DialogPanel extends JPanel{
	JDialog dialog=new JDialog();
	
	public DialogPanel(){
		dialog.add(this);
		dialog.setResizable(false);
		dialog.setSize(400,50);
		dialog.setLocation(300,300);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GameWindowListener wlistener = new GameWindowListener();
		dialog.addWindowListener(wlistener);
		dialog.setVisible(true);
	}
	public void setText(String text){
		dialog.setTitle(text);
	}
}
