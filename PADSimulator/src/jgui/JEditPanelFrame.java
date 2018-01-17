/**
 * @author Demilichzz
 *	用于编辑宝石面板的窗体
 * 2014-4-20
 */
package jgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import data.GameData;
import entities.VOrb;

import system.VEngine;

/**
 * @author Demilichzz
 *
 * 2014-4-20
 */
public class JEditPanelFrame extends JFrame{
	private Container contentPane;
	private JTextArea gemArea;
	private JLabel picLabel;
	
	private int[][] orbs = new int[5][6];
	
	public JEditPanelFrame(){
		super("编辑面板");
		contentPane = this.getContentPane();
		this.setLocation(800, 100);
		this.setSize(260,500);
		contentPane.setLayout(new BorderLayout());
		gemArea = new JTextArea("",5,6);	//宝石区域矩阵
		gemArea.setFont(new Font("微软雅黑",Font.BOLD,64));
		gemArea.setLineWrap(true);		//设置需要换行

		DocumentListener gemareadl = new DocumentListener(){		//用于控制输入及宝石矩阵变化的文本监听器
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				final Document d = e.getDocument();
				final int offset = e.getOffset();
				try {
					String insText = d.getText(offset, 1);
					if(insText.matches("[0-8]")&&offset<30&&d.getLength()<31){		//插入字符是1-8的数字
						updateGemMatrix();		//更新宝石面板的分布数据
					}
					else{
						try{
							new Thread(new Thread(){	//新建线程处理删除字符
								public void run()
								{
									try{
										d.remove(offset, 1);	//删除插入的字符
										gemArea.setCaretPosition(gemArea.getCaretPosition()-1);
									}
									catch(Exception e){
										System.out.println(e.toString());
									}
								}
							}).start();
						}
						catch(Exception te){
							System.out.println(te.toString());
						}
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				updateGemMatrix();	//更新宝石面板的分布数据
			}
		};
		gemArea.getDocument().addDocumentListener(gemareadl);
		contentPane.add(gemArea,BorderLayout.CENTER);
		picLabel = new JLabel();
		picLabel.setPreferredSize(new Dimension(260,32));
		picLabel.setIcon(new ImageIcon("res/pic/PadIndex.png"));
		contentPane.add(picLabel,BorderLayout.NORTH);
		this.setResizable(false);
		this.setVisible(false);
	}
	public void loadGemMatrix(VOrb[][] orbs){
		// TODO 从gamearea中获取宝石矩阵并输入窗体的TextArea中
		this.orbs = new int[orbs.length][orbs[0].length];
		for(int i=0;i<orbs.length;i++){
			for(int j=0;j<orbs[0].length;j++){
				this.orbs[i][j] = orbs[i][j].getValue(VOrb.ORB_TYPE);
			}
		}
		getMatrixToTextarea();
	}
	public void getMatrixToTextarea(){
		// TODO 将宝石数值矩阵转化为在TextArea上显示的文字
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		String temptext = "";
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				if(orbs[i][j]!=-1){
					temptext = temptext+orbs[i][j];
				}
				else{
					temptext = temptext+0;
				}
			}
		}
		gemArea.setText(temptext);
	}
	public void updateGemMatrix(){
		// TODO 将TextArea的文本数据转化问宝石数值矩阵
		char[] orbsvalue = gemArea.getText().toCharArray();
		int size = orbsvalue.length;
		int y = orbs.length;		//行
		int x = orbs[0].length;		//列
		for(int i=0;i<y;i++){
			for(int j=0;j<x;j++){
				int index = i*x+j;
				if(index<size){
					orbs[i][j] = Character.digit(orbsvalue[index],10);
				}
				else{
					orbs[i][j] = 1;
				}
			}
		}
		GameData.ga.synchronousGems(orbs);
	}
	public void showframe() {
		// TODO Auto-generated method stub
		this.setVisible(true);
		this.setVisible(true);	//必须写两次才能获取焦点
		//gemArea.requestFocus();
	}
}
