/**
 * �ļ����ƣ�ProjectConfig.java
 * ��·����config
 * ������TODO ��Ŀ���ã�����Ŀ����ʱ���������ļ�
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-2-29����04:50:14
 * �汾��Ver 1.0
 */
package config;

import global.Debug;

import interfaces.VInt;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Demilichzz
 * 
 */
public class ProjectConfig {
	// ----------ֵ�б�����-------------------
	public static int DEBUGMODE = 0;
	public static int SHOWUIBORDER = 1;
	public static int SOUND_VOLUME = 2;
	public static int MUSIC_VOLUME = 3;
	public static int PROMPT_TIME = 4;
	public static int HIT_SOUND = 5;
	//----------------------------------------
	public static VInt configvaluelist[] = {new VInt(1),new VInt(0),new VInt(50),new VInt(50),new VInt(3),new VInt(0)};		//ͨ�����ò����б�
	public static String configparamlist[] = {"DEBUGMODE","SHOWUIBORDER","SOUND_VOLUME","MUSIC_VOLUME","PROMPT_TIME","HIT_SOUND"};	//ͨ�����������б�
	
	protected static String keyconfigurl = new String("data/KeyConfig.ini");
	protected static String configurl = new String("data/GameConfig.ini"); 
	
	public static void LoadConfig() {
		// Need Code
		// LoadConfigFromFile
		try {
			loadGameConfig(configparamlist);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Debug.DebugSimpleMessage("������Ϸ����");
	}
	
	public static int getConfigValue(int i){
		// TODO ͨ��������ȡ����ֵ
		return configvaluelist[i].getValue(0);
	}
	public static VInt getConfigVValue(int i){
		// TODO ͨ��������ȡ�����Ķ�ֵ̬�ӿ�
		return configvaluelist[i];
	}
	public static void setConfigValue(int index,int value){
		// TODO ͨ���������ò���ֵ
		configvaluelist[index].setValue(0, value);
	}
	                                           
	public static void loadGameConfig(String[] paramlist) throws Exception{
		// TODO ������Ϸ����
		int[] valuelist = new int[paramlist.length]; // ����ֵ����
		Exception e;
		int i = 0;
		File f = new File(configurl);		
		if (!f.exists()) {			//��ini�ļ�������
			Debug.DebugSimpleMessage("δ�ҵ�GameConfig.ini,���������ɰ�������");
			f.createNewFile();
			for(int vi=0;vi<6;vi++){
				valuelist[vi]=configvaluelist[vi].getValue(0);
			}
			saveCommonConfig("GameConfig", valuelist, paramlist, configurl);
		} 
		else {
			valuelist = loadCommonConfig("GameConfig",paramlist,configurl);	//������Ϸ�����еĲ���ֵ
			for(int vi=0;vi<6;vi++){
				configvaluelist[vi].setValue(0, valuelist[vi]);		//���ö�ֵ̬�ӿ�ֵ
			}
		}
		
	}
	
	public static void saveGameConfig(){
		// TODO ���浱ǰ��Ϸ����Ϊini
		int[] valuelist = new int[6];
		for(int vi=0;vi<6;vi++){
			valuelist[vi]=configvaluelist[vi].getValue(0);
		}
		try {
			saveCommonConfig("GameConfig", valuelist, configparamlist, configurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int[] loadKeyConfig(String[] paramlist) throws IOException, Exception {
		// TODO ����������ã����ؼ�λӳ���
		int[] keylist = new int[paramlist.length]; // ��ʼ������ӳ�������
		int i = 0;
		File f = new File(keyconfigurl);
		if (!f.exists()) {			//��ini�ļ�������
			Debug.DebugSimpleMessage("δ�ҵ�KeyConfig.ini,���������ɰ�������");
			f.createNewFile();
			keylist = loadKeyDefaultConfig(paramlist);
		} 
		else {
			keylist = loadCommonConfig("KeyConfig",paramlist,keyconfigurl);
		}
		return keylist;
	}
	public static int[] loadKeyDefaultConfig(String[] paramlist) throws Exception {
		// TODO Auto-generated method stub
		int[] keylist = new int[11]; // ��ʼ������ӳ�������
		keylist[0] = KeyEvent.VK_UP;
		keylist[1] = KeyEvent.VK_DOWN;
		keylist[2] = KeyEvent.VK_LEFT;
		keylist[3] = KeyEvent.VK_RIGHT;
		keylist[4] = KeyEvent.VK_ENTER;
		keylist[5] = KeyEvent.VK_SPACE;
		keylist[6] = KeyEvent.VK_ESCAPE;
		keylist[7] = KeyEvent.VK_Z;
		keylist[8] = KeyEvent.VK_X;
		keylist[9] = KeyEvent.VK_C;
		keylist[10] = KeyEvent.VK_SHIFT;

		saveCommonConfig("KeyConfig",keylist,paramlist,keyconfigurl);
		return keylist;
		
	}

	/**
	 * @param section		�������,����[]
	 * @param valuelist		ֵ�б�
	 * @param configparam	���������б�,������ֵ�б�����Ȳ�һһ��Ӧ
	 * @param configurl 	�����ļ���ַ
	 * @throws Exception
	 */
	public static void saveCommonConfig(String section,int[] valuelist,String[] configparam,String configurl) throws Exception{
		// TODO ͨ�õ�ini�����ļ����淽��
		if(valuelist.length!=configparam.length){
			throw(new Exception("�����ļ�����ֵ����������޷���Ӧ"));
		}
		File f = new File(configurl);
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		PrintWriter pw = new PrintWriter(fos);
		String culine = "["+section+"]";		
		pw.println(culine);
		for(int i=0;i<valuelist.length;i++){
			culine=configparam[i]+"="+valuelist[i];
			pw.println(culine);
		}
		pw.close();
		fos.close();
	}
	
	public static int[] loadCommonConfig(String section,String[]paramlist,String configurl) throws Exception{
		// TODO ͨ�õ� ini�����ļ����ط���
		int[] valuelist=new int[paramlist.length];
		int i=0;
		File f = new File(configurl);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		line=br.readLine();
		if(!line.equals("["+section+"]")){
			throw(new Exception("���ص�ini�ļ����䲻��"));
		}
		while ((line = br.readLine()) != null) {
			if (line.equals(section)) {
				continue;
			}
			else if(line.equals("")){
				continue;
			}
			else {
				if (line.contains(paramlist[i])) {
					line = line.split(paramlist[i]+"=")[1];
					//line = line.substring(line.length() - 2, line.length());
					int value = Integer.parseInt(line);
					valuelist[i] = value;
					i++;
				}
			}
		}
		br.close();
		fr.close();
		return valuelist;
	}
}
