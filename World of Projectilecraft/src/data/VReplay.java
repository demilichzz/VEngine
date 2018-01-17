/**
 * 	TODO ¼���࣬������ÿ����Ϸ״̬���µ�ͬʱ��¼��Ҳ�����������¼���ļ��ȡ�
 */
package data;

import java.io.*;
import java.util.*;
import java.text.*;


import controller.*;
import system.*;
import ui.VUI;
import event.*;
import global.*;

/**
 * @author Demilichzz
 *
 * 2012-11-8
 */
public class VReplay {
	protected ArrayList<int[]>statelist;	//����״̬��
	public long randomseed=0;		//�������
	protected int instance;			//��������
	protected int talent;			//����츳
	protected int mode;				//��Ϸģʽ
	protected int endframe;			//����ʱ��stage���´���
	protected ArrayList<Integer>hitlist;	//����֡����
	
	public VReplay(){
		statelist = new ArrayList<int[]>();
		hitlist = new ArrayList<Integer>();
	}
	public void InitReplay(){
		// TODO ��ʼ��¼����ص���Ϸ����
		instance=1;
		talent=GameData.pc.getTalent();
		mode=GameData.gamemode;
	}
	public void addKeyState(int[] ks){
		// TODO ����ǰ���µļ���״̬��ӵ��б�
		int[] state=new int[11];
		for(int i=0;i<11;i++){
			if(i==GameListener.KEY_UP||i==GameListener.KEY_DOWN||i==GameListener.KEY_LEFT||
				i==GameListener.KEY_RIGHT||i==GameListener.KEY_Z||i==GameListener.KEY_X||i==GameListener.KEY_SHIFT){
				//������Ҳ�������
				state[i]=ks[i];
			}
		}
		statelist.add(state);
	}
	public void addHitState(int frame){
		// TODO ��ӱ�����ʱ��֡��
		hitlist.add(frame);
	}
	public void setEndFrame(int frame){
		// TODO ���ý���֡
		endframe = frame;
	}
	public void setGameState(){
		// TODO ��¼���ļ��ļ�¼������Ϸ����
		GlobalEvent.loadInstance(instance);
		GameData.setTalent(talent);
		GameData.setMode(mode);
	}

	public int[] getKeystate() {
		// TODO ��ȡ��¼�İ�����Ϣ
		if(statelist.size()>0){
			int[] state=statelist.get(0);
			statelist.remove(0);
			return state;
		}
		return null;	
	}
	public void saveToFile() {
		// TODO д���ļ�
		FileOutputStream fos;
		String path="data/Replay/";	//¼���ļ���·��
		String completepath="";		//����·��
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HHmmss");//���Է�����޸����ڸ�ʽ
		String datestr = dateFormat.format( now );
		completepath=path+datestr+".rep";
		File f = new File(completepath);
		if(f.exists()){
			int i=0;
			String newpath = path;
			while(f.exists()){						//���¼���ļ����ظ�
				newpath=path+datestr+"_"+i+".rep";		//Ϊ�ļ�����Ӻ�׺
				f=new File(newpath);
				i++;
			}
			completepath=newpath;
			i=0;
		}
		else{
			
		}
		try {
			fos = new FileOutputStream(completepath);
			PrintWriter pw = new PrintWriter(fos);
			pw.println(randomseed);
			pw.println(instance);
			pw.println(talent);
			pw.println(mode);
			for (int[] state: statelist) {
				for (int i = 0; i < state.length; i++) {
					pw.print(state[i]+",");
					pw.flush();
				}
				pw.println("");
				pw.flush();
			}
			for(int i=0;i<hitlist.size();i++){
				pw.print(hitlist.get(i)+";");
				pw.flush();
			}
			pw.println("");
			pw.flush();
			pw.println("EndFrame:"+endframe+";");
			pw.flush();
			pw.close();
			VUI uihint = VEngine.gs.uiparent.getUIByID("ui_savehint");
			uihint.setVisible(true);
			uihint.addText("¼���ļ�������"+completepath, 0, 0, Global.f, Global.c);
			//Debug.DebugSimpleMessage("¼���ļ�������"+completepath);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadFromFile(String path){
		//String path="data/Replay/replay.rep";
		statelist = new ArrayList<int[]>();
		try {
			BufferedReader bIn = new BufferedReader(new FileReader(path));
			String line = bIn.readLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				StringTokenizer semi = new StringTokenizer(line,";");
				if (st.countTokens()!=11) {		//�������
					if(line.contains(";")){
						
					}
					else{
						randomseed=Long.parseLong(line);
						line = bIn.readLine();
						instance = Integer.parseInt(line);
						line = bIn.readLine();
						talent = Integer.parseInt(line);
						line = bIn.readLine();
						mode = Integer.parseInt(line);
					}
				} 
				else{							//���밴����Ϣ
					int[]state =new int[11];
					for (int i=0;i<11;i++) {
						state[i]=Integer.parseInt(st.nextToken());
					}
					statelist.add(state);
				}
				line = bIn.readLine(); // ����һ��
			}
			Debug.DebugSimpleMessage("����¼���ļ�");
			bIn.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
