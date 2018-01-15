/**
 * 	TODO 录像类，用于在每次游戏状态更新的同时记录玩家操作，及生成录像文件等。
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
	protected ArrayList<int[]>statelist;	//按键状态表
	public long randomseed=0;		//随机种子
	protected int instance;			//副本索引
	protected int talent;			//玩家天赋
	protected int mode;				//游戏模式
	protected int endframe;			//结束时的stage更新次数
	protected ArrayList<Integer>hitlist;	//被击帧数表
	
	public VReplay(){
		statelist = new ArrayList<int[]>();
		hitlist = new ArrayList<Integer>();
	}
	public void InitReplay(){
		// TODO 初始化录像相关的游戏参数
		instance=1;
		talent=GameData.pc.getTalent();
		mode=GameData.gamemode;
	}
	public void addKeyState(int[] ks){
		// TODO 将当前更新的键盘状态添加到列表
		int[] state=new int[11];
		for(int i=0;i<11;i++){
			if(i==GameListener.KEY_UP||i==GameListener.KEY_DOWN||i==GameListener.KEY_LEFT||
				i==GameListener.KEY_RIGHT||i==GameListener.KEY_Z||i==GameListener.KEY_X||i==GameListener.KEY_SHIFT){
				//过滤玩家操作按键
				state[i]=ks[i];
			}
		}
		statelist.add(state);
	}
	public void addHitState(int frame){
		// TODO 添加被击中时的帧数
		hitlist.add(frame);
	}
	public void setEndFrame(int frame){
		// TODO 设置结束帧
		endframe = frame;
	}
	public void setGameState(){
		// TODO 按录像文件的记录设置游戏参数
		GlobalEvent.loadInstance(instance);
		GameData.setTalent(talent);
		GameData.setMode(mode);
	}

	public int[] getKeystate() {
		// TODO 获取记录的按键信息
		if(statelist.size()>0){
			int[] state=statelist.get(0);
			statelist.remove(0);
			return state;
		}
		return null;	
	}
	public void saveToFile() {
		// TODO 写入文件
		FileOutputStream fos;
		String path="data/Replay/";	//录像文件夹路径
		String completepath="";		//完整路径
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd-HHmmss");//可以方便地修改日期格式
		String datestr = dateFormat.format( now );
		completepath=path+datestr+".rep";
		File f = new File(completepath);
		if(f.exists()){
			int i=0;
			String newpath = path;
			while(f.exists()){						//如果录像文件名重复
				newpath=path+datestr+"_"+i+".rep";		//为文件名添加后缀
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
			uihint.addText("录像文件保存于"+completepath, 0, 0, Global.f, Global.c);
			//Debug.DebugSimpleMessage("录像文件保存于"+completepath);
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
				if (st.countTokens()!=11) {		//载入参数
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
				else{							//载入按键信息
					int[]state =new int[11];
					for (int i=0;i<11;i++) {
						state[i]=Integer.parseInt(st.nextToken());
					}
					statelist.add(state);
				}
				line = bIn.readLine(); // 读下一行
			}
			Debug.DebugSimpleMessage("载入录像文件");
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
