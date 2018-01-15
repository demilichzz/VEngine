/**
 * 	TODO �ļ�����̬��
 */
package file;

import global.Debug;
import global.Global;
import global.Soundconst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author Demilichzz
 *
 * 2012-11-25
 */
public class FileIO {
	protected static File[] filelist = new File[1024];			//�ļ��б�
	protected static String[] filefolders = new String[1024];	//�ļ�Ŀ¼�б�
	protected static String[] filenames = new String[1024];	//�ļ����б�
	protected static int[] lengths = new int[1024];			//�ļ������б�
	protected int filenum = 0;
	protected static int count = 0;
	
	protected static String version = Global.version;			//ʹ�õ���Դ�ļ��汾
	protected static String filelock = "data/run.lck" ;			//�����������е��ļ�·��
	public static File lock=null;
	private static boolean flag;
	private static File file;
	public static FileOutputStream lock_fOS = null;		//����ȷ���Ƿ���һ������ʵ���������е������
	
	public static void unPackAllResources(){
		// TODO ɾ���Ѵ�����Դ�ļ������ļ�����ѹ��Դ�ļ��������ǿ���ģʽʱ���е���
		delFolder("res/Sound");
		Debug.DebugTestTimeStart();
		try {
			unpackFile("res/Sound.data");
			unpackFile("res/Sprite.data");
			unpackFile("res/Anime.data");
			unpackFile("res/UI.data");
			unpackFile("res/Unit.data");
			unpackFile("data/Script.data");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Debug.DebugTestTimeEnd("��ѹ����Դ�ļ�", true);
	}
	
	public static void clearResourceFile(){
		// TODO �ڳ�ʼ����Դ��ɺ������Դ�ļ�
		Debug.DebugTestTimeStart();
		delFolder("res/UI");
		delFolder("res/Unit");
		Debug.DebugTestTimeEnd("�����Դ�ļ�", true);
	}
	public static void closeClear(){
		// TODO ����Ϸ����ʱ�����Դ�ļ�
		Soundconst.clear();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		delFolder("res/Sound");
		delFolder("res/Sprite");
		delFolder("res/Anime");
		delFolder("data/Script");
	}
	
	public static boolean checkLock(){
		// TODO ����ļ�����������ʱ�����Գ���ɾ���ļ��������س����Ƿ�ɹ�
		lock = new File(filelock);
		if(!lock.exists()){
			//Debug.DebugSimpleMessage("�ļ���������");
		}
		else{
			//Debug.DebugSimpleMessage("�ļ�������");
			return lock.delete();
		}
		return true;
	}
	
	public static void createLock(){
		// TODO �����ļ����������ļ�����д��״̬�������¿�����ʵ��ʱ����ɾ�����ļ���ȷ���Ƿ�����ͬһ��Ϸʵ����������
		lock = new File(filelock);
		if(!lock.exists()){
			try {
				lock.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			lock_fOS = new FileOutputStream(lock);	
			lock_fOS.write(1);	//д���ļ������Ա����ļ�����ռ��
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean delFolder(String sPath){
		flag = false;   
	    file = new File(sPath);   
	    // �ж�Ŀ¼���ļ��Ƿ����   
	    if (!file.exists()) {  // �����ڷ��� false   
	        return flag;   
	    } else {   
	        // �ж��Ƿ�Ϊ�ļ�   
	        if (file.isFile()) {  // Ϊ�ļ�ʱ����ɾ���ļ�����   
	            return deleteFile(sPath);   
	        } else {  // ΪĿ¼ʱ����ɾ��Ŀ¼����   
	            return deleteDirectory(sPath);   
	        }   
	    }   
	}
	
	public static boolean deleteFile(String sPath) {   
	    flag = false;   
	    file = new File(sPath);   
	    // ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��   
	    if (file.isFile() && file.exists()) {   
	        file.delete();   
	        flag = true;   
	    }
	    return flag;   
	}  
	
	public static boolean deleteDirectory(String sPath) {   
	    //���sPath�����ļ��ָ�����β���Զ�����ļ��ָ���   
	    if (!sPath.endsWith(File.separator)) {   
	        sPath = sPath + File.separator;   
	    }   
	    File dirFile = new File(sPath);   
	    //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�   
	    if (!dirFile.exists() || !dirFile.isDirectory()) {   
	        return false;   
	    }   
	    flag = true;   
	    //ɾ���ļ����µ������ļ�(������Ŀ¼)   
	    File[] files = dirFile.listFiles();   
	    for (int i = 0; i < files.length; i++) {   
	        //ɾ�����ļ�   
	        if (files[i].isFile()) {   
	            flag = deleteFile(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        } //ɾ����Ŀ¼   
	        else {   
	            flag = deleteDirectory(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        }   
	    }   
	    if (!flag) return false;   
	    //ɾ����ǰĿ¼
	    if (dirFile.delete()) {   
	        return true;   
	    } else {   
	        return false;   
	    }   
	}
	public static void unpackFile(String packfile) throws Exception{
		// TODO ��data�ļ����н�ѹ�ļ�
		initList();			//�����б�
		File PackFile = new File(packfile);		//��ȡĿ��data�ļ�
		getFileListFromPack(PackFile);			//��Data�ļ��л�ȡ�ļ�Ŀ¼��Ϣ����ѹ
	}
	/**
	 * @param packFile
	 * @throws Exception 
	 */
	private static void getFileListFromPack(File packFile) throws Exception {
		// TODO ��ȡ�ļ�Ŀ¼����ѹ
		FileInputStream fIS = null;				//������
		FileOutputStream fOS = null;			//�����
		if(packFile.exists()&&!packFile.isDirectory()){
			fIS = new FileInputStream(packFile);
			byte[] buffer = null;
			boolean load = true;
			buffer = new byte[64];		//��ȡ�汾��
			fIS.read(buffer);
			String temp = new String(buffer,"UTF-8");
			if(!temp.contains(version)){
				System.out.println("�汾�Ų���ȷ");
				return;
			}
			buffer = new byte[64];		//��ȡ�ļ������ֽڿ�
			fIS.read(buffer);
			temp = new String(buffer,"UTF-8");
			if(temp.contains("*")){
				StringTokenizer st = new StringTokenizer(temp, "*");	//��ȡ�ַ���
				int filecount=Integer.parseInt(st.nextToken());		//��ת��Ϊ�ļ�����������¼
			}
			//-----------�����ļ�ͷ--------------------------------------------
			while(load){
				buffer = new byte[64];
				fIS.read(buffer);
				temp = new String(buffer,"UTF-8");
				if(temp.contains("!@#$%^&*()")){		//�����ȡ���������
					load=false;							//������ȡ�ļ�ͷ��ѭ��
				}
				else{
					if(temp.contains("^")){	//name
						StringTokenizer st = new StringTokenizer(temp, "^");	//��ȡ�ַ���
						filenames[count]=st.nextToken();
					}
					else if(temp.contains("&")){	//folder
						StringTokenizer st = new StringTokenizer(temp, "&");	//��ȡ�ַ���
						filefolders[count]=st.nextToken();
					}
					else if(temp.contains("*")){	//length
						StringTokenizer st = new StringTokenizer(temp, "*");	//��ȡ�ַ���
						lengths[count]=Integer.parseInt(st.nextToken());		//��ת��Ϊ�ļ�����������¼
						count++;
					}
				}
			}
			//-----------�����ļ�ͷ��Ϣ��ѹ�ļ�-----------------------------------------
			for(int i=0;i<count;i++){
				File folder = new File(filefolders[i]);
				if(!folder.exists()){
					folder.mkdirs();
				}
				File newfile = new File(filefolders[i]+"/"+filenames[i]);
				if(!newfile.exists()){			//�����ļ�
					newfile.createNewFile(); 
				}
				buffer = new byte[lengths[i]];
				fIS.read(buffer);
				fOS = new FileOutputStream(newfile);
				fOS.write(buffer);
				fOS.flush();
				fOS.close();
			}
			fIS.close();
		}
		else{
			System.out.println("����Ŀ��data�ļ�������");
		}
	}

	private static void initList() {
		// TODO ��ʼ���б�
		filelist = new File[1024];			//�ļ��б�
		filefolders = new String[1024];	//�ļ�Ŀ¼�б�
		filenames = new String[1024];	//�ļ����б�
		lengths = new int[1024];			//�ļ������б�
		count =0;
	}

	public static void unpackFile(String packfile,String folder) throws Exception{
		FileInputStream fIS = null;
		FileOutputStream fOS = null;
		File DirFile = new File(folder);
		File PackFile = new File(packfile);
		String[] filenames = new String[1024];
		int[] lengths = new int[1024];
		int count=0;
		byte[] buffer = null;
		//---------����ͷ-----------------------------------------------
		boolean load = true;
		fIS = new FileInputStream(PackFile);
		if(!DirFile.exists()){
			DirFile.mkdirs();
		}
		while(load){
			buffer = new byte[128];
			fIS.read(buffer);
			String temp = new String(buffer,"UTF-8");
			if(temp.contains("!@#$%^&*()")){
				load=false;
			}
			else{
				if(temp.contains("^^^")){	//name
					StringTokenizer st = new StringTokenizer(temp, "^^^");
					filenames[count]=st.nextToken();
				}
				else if(temp.contains("***")){	//length
					StringTokenizer st = new StringTokenizer(temp, "***");
					lengths[count]=Integer.parseInt(st.nextToken());
					count++;
				}
			}
		}
		//--------��ѹ�ļ�-----------------------------------------------------
		for(int i=0;i<count;i++){
			File newfile = new File(folder+"/"+filenames[i]);
			if(!newfile.exists()){			//�����ļ�
				try {
					newfile.createNewFile();
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				buffer = new byte[lengths[i]];
				fIS.read(buffer);
				fOS = new FileOutputStream(newfile);
				fOS.write(buffer);
				fOS.flush();
				fOS.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fIS.close();
		//--------�ݹ��ѹData-------------------------------------
		if (DirFile.exists()) {
			if (DirFile.isDirectory()) {
				File[] files = DirFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					if(files[i].isFile()){
						if(files[i].getName().endsWith(".data")){
							String sname = files[i].getName();
							sname = sname.substring(0, sname.length()-5);
							unpackFile(files[i].getPath(),DirFile.getPath()+"/"+sname);
						}
					}
				}
			}
		}
	}
}
