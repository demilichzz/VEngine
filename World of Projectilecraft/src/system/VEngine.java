/**
 * �ļ����ƣ�VEngine.java
 * ��·����system
 * ������TODO ���������
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-10-26����03:11:55
 * �汾��Ver 1.0
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
	public static JFrame frm;		// ��Ϸ����
	public static GamePanel p;		// ���������ڻ���ͼ�������Panel
	public static Simulator s;		// ������
	public static GameState gs;		// ��Ϸ״̬
	public static GameListener glistener; // ��Ϸ������
	public static GameMouseListener mlistener;	//��������
	public static GameWindowListener wlistener;
	public static boolean developmode = true;		//����ģʽ�����ڿ�����Դ�ļ�����data�н�ѹ����ֱ��ʹ���ļ���
	public static String version = "v0.9";	//�汾��
	
	private static Dimension d;
	
	public VEngine(){
		Init();
		Animeconst.prepareAnime();
	}
	public static void main(String[] args) {
		boolean b = FileIO.checkLock();
		if(!b){		//�����鲻�ɹ�������ֹ����
			Debug.DebugPopFrame("�Ѿ���һ����Ϸ��������");
			//System.exit(0);
		}
		else{
			FileIO.createLock();	//����ʱ������
			ProjectConfig.LoadConfig();	//������Ŀ������Ϣ
			InitResource();	//��ʼ����Ϸ��Դ
			VEngine newgame = new VEngine();
			newgame.Gameloop();
		}
	}

	private static void InitResource() {
		if(!developmode){
			FileIO.unPackAllResources();
		}
		Debug.DebugTestTimeStart();
		// TODO ��ʼ����Ϸ���õ���Դ
		Imageconst.Init(); //��ʼ��ͼ����Դ
		Debug.DebugTestTimeEnd("��ʼ��ͼ��", true);
		Global.Init();		//��ʼ��ȫ�ֳ���
		Animeconst.Init(); //��ʼ��������Դ
		Soundconst.Init(); //��ʼ��������Դ
		//Textconst.Init();
		GameData.InitData();
		if(!developmode){
			FileIO.clearResourceFile();
		}
	}
	private void Gameloop() {
		// TODO ����������Ϸ״̬�ĸ���ѭ��
		new Thread(s).start();	//�������߳����ڷ�����
	}
	public void Init() {
		// TODO ��ʼ����Ϸ����
		//	������Ϸ���岢��Ӵ��������
		frm = new JFrame();
		frm.setLayout(null);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		wlistener = new GameWindowListener();
		frm.addWindowListener(wlistener);
		
		//textbox = new TextCanvas();
		//textbox.setSize(200,35);
		//textbox.setLocation(100, 100);
		//textbox.setVisible(false);		//�����ı���ʹ���ڳ�ʼʱ����
		
		//	���崰���е���Ϸ������򣬸����������巶Χ
		p = new GamePanel(this);
		//d =new Dimension(800,600);
		//p.setPreferredSize(d);
		p.setSize(800, 600);
		p.setLocation(0, 0);
		p.setOpaque(true);

		glistener = new GameListener(this);	//�����Ϸ������,������Ϸ����
		mlistener = new GameMouseListener(this);//�����������
		frm.addKeyListener(glistener);
		p.addKeyListener(glistener);
		p.addMouseListener(mlistener);
		p.addMouseMotionListener(mlistener);
		
		gs = new GameState(this,10);	//������Ϸ״̬,������GS����ʱ��Ϊ10ms
		s = new Simulator(this);	//��ʼ��������
		gs.Init();				//��ʼ����Ϸ״̬
		
		frm.add(p);	//����Ϸ�����ӵ�����
		//frm.add(textbox);
		
		frm.setSize(806, 628);
		frm.setResizable(false);
		frm.setLocation(100, 10);
		frm.setTitle("WorldofProjectilecraft");
		frm.setVisible(true);
		frm.requestFocus();
	}
	
}
