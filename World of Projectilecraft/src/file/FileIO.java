/**
 * 	TODO 文件处理静态类
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
	protected static File[] filelist = new File[1024];			//文件列表
	protected static String[] filefolders = new String[1024];	//文件目录列表
	protected static String[] filenames = new String[1024];	//文件名列表
	protected static int[] lengths = new int[1024];			//文件长度列表
	protected int filenum = 0;
	protected static int count = 0;
	
	protected static String version = Global.version;			//使用的资源文件版本
	protected static String filelock = "data/run.lck" ;			//锁定程序运行的文件路径
	public static File lock=null;
	private static boolean flag;
	private static File file;
	public static FileOutputStream lock_fOS = null;		//用于确定是否有一个程序实例正在运行的输出流
	
	public static void unPackAllResources(){
		// TODO 删除已存在资源文件并从文件包解压资源文件，仅当非开发模式时进行调用
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
		Debug.DebugTestTimeEnd("解压缩资源文件", true);
	}
	
	public static void clearResourceFile(){
		// TODO 在初始化资源完成后清除资源文件
		Debug.DebugTestTimeStart();
		delFolder("res/UI");
		delFolder("res/Unit");
		Debug.DebugTestTimeEnd("清除资源文件", true);
	}
	public static void closeClear(){
		// TODO 在游戏结束时清除资源文件
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
		// TODO 检查文件锁，在启动时调用以尝试删除文件锁，返回尝试是否成功
		lock = new File(filelock);
		if(!lock.exists()){
			//Debug.DebugSimpleMessage("文件锁不存在");
		}
		else{
			//Debug.DebugSimpleMessage("文件锁存在");
			return lock.delete();
		}
		return true;
	}
	
	public static void createLock(){
		// TODO 创建文件锁，对锁文件保持写入状态，并在新开程序实例时尝试删除锁文件以确定是否已有同一游戏实例在运行中
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
			lock_fOS.write(1);	//写入文件，用以保持文件锁被占用
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
	    // 判断目录或文件是否存在   
	    if (!file.exists()) {  // 不存在返回 false   
	        return flag;   
	    } else {   
	        // 判断是否为文件   
	        if (file.isFile()) {  // 为文件时调用删除文件方法   
	            return deleteFile(sPath);   
	        } else {  // 为目录时调用删除目录方法   
	            return deleteDirectory(sPath);   
	        }   
	    }   
	}
	
	public static boolean deleteFile(String sPath) {   
	    flag = false;   
	    file = new File(sPath);   
	    // 路径为文件且不为空则进行删除   
	    if (file.isFile() && file.exists()) {   
	        file.delete();   
	        flag = true;   
	    }
	    return flag;   
	}  
	
	public static boolean deleteDirectory(String sPath) {   
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符   
	    if (!sPath.endsWith(File.separator)) {   
	        sPath = sPath + File.separator;   
	    }   
	    File dirFile = new File(sPath);   
	    //如果dir对应的文件不存在，或者不是一个目录，则退出   
	    if (!dirFile.exists() || !dirFile.isDirectory()) {   
	        return false;   
	    }   
	    flag = true;   
	    //删除文件夹下的所有文件(包括子目录)   
	    File[] files = dirFile.listFiles();   
	    for (int i = 0; i < files.length; i++) {   
	        //删除子文件   
	        if (files[i].isFile()) {   
	            flag = deleteFile(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        } //删除子目录   
	        else {   
	            flag = deleteDirectory(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        }   
	    }   
	    if (!flag) return false;   
	    //删除当前目录
	    if (dirFile.delete()) {   
	        return true;   
	    } else {   
	        return false;   
	    }   
	}
	public static void unpackFile(String packfile) throws Exception{
		// TODO 从data文件包中解压文件
		initList();			//重置列表
		File PackFile = new File(packfile);		//获取目标data文件
		getFileListFromPack(PackFile);			//从Data文件中获取文件目录信息并解压
	}
	/**
	 * @param packFile
	 * @throws Exception 
	 */
	private static void getFileListFromPack(File packFile) throws Exception {
		// TODO 获取文件目录并解压
		FileInputStream fIS = null;				//输入流
		FileOutputStream fOS = null;			//输出流
		if(packFile.exists()&&!packFile.isDirectory()){
			fIS = new FileInputStream(packFile);
			byte[] buffer = null;
			boolean load = true;
			buffer = new byte[64];		//读取版本号
			fIS.read(buffer);
			String temp = new String(buffer,"UTF-8");
			if(!temp.contains(version)){
				System.out.println("版本号不正确");
				return;
			}
			buffer = new byte[64];		//读取文件数量字节块
			fIS.read(buffer);
			temp = new String(buffer,"UTF-8");
			if(temp.contains("*")){
				StringTokenizer st = new StringTokenizer(temp, "*");	//截取字符串
				int filecount=Integer.parseInt(st.nextToken());		//并转换为文件长度整数记录
			}
			//-----------载入文件头--------------------------------------------
			while(load){
				buffer = new byte[64];
				fIS.read(buffer);
				temp = new String(buffer,"UTF-8");
				if(temp.contains("!@#$%^&*()")){		//如果获取到结束标记
					load=false;							//结束获取文件头的循环
				}
				else{
					if(temp.contains("^")){	//name
						StringTokenizer st = new StringTokenizer(temp, "^");	//截取字符串
						filenames[count]=st.nextToken();
					}
					else if(temp.contains("&")){	//folder
						StringTokenizer st = new StringTokenizer(temp, "&");	//截取字符串
						filefolders[count]=st.nextToken();
					}
					else if(temp.contains("*")){	//length
						StringTokenizer st = new StringTokenizer(temp, "*");	//截取字符串
						lengths[count]=Integer.parseInt(st.nextToken());		//并转换为文件长度整数记录
						count++;
					}
				}
			}
			//-----------根据文件头信息解压文件-----------------------------------------
			for(int i=0;i<count;i++){
				File folder = new File(filefolders[i]);
				if(!folder.exists()){
					folder.mkdirs();
				}
				File newfile = new File(filefolders[i]+"/"+filenames[i]);
				if(!newfile.exists()){			//建立文件
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
			System.out.println("错误：目标data文件不存在");
		}
	}

	private static void initList() {
		// TODO 初始化列表
		filelist = new File[1024];			//文件列表
		filefolders = new String[1024];	//文件目录列表
		filenames = new String[1024];	//文件名列表
		lengths = new int[1024];			//文件长度列表
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
		//---------载入头-----------------------------------------------
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
		//--------解压文件-----------------------------------------------------
		for(int i=0;i<count;i++){
			File newfile = new File(folder+"/"+filenames[i]);
			if(!newfile.exists()){			//建立文件
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
		//--------递归解压Data-------------------------------------
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
