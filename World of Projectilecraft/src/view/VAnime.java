/**
 * �ļ����ƣ�VAnime.java
 * ��·����view
 * ������TODO ͨ����VImage�еķָ�ͼƬ��Ԥ����Ķ�����������ʵ�ֶ�����ʾ
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-11-4����11:26:24
 * �汾��Ver 1.0
 */
package view;

import java.awt.Image;
import java.awt.image.*;

import system.*;
import global.*;

/**
 * @author Demilichzz
 *
 */
public class VAnime implements VImageInterface{
	//--------------����ģʽ����-------------------------------
	public static final int ANIMEMODE_PERODIC = 0;		//ѭ��ģʽ
	public static final int ANIMEMODE_LASTFRAME = 1;	//ĩֹ֡ͣģʽ
	public static final int ANIMEMODE_SINGLE = 2;		//���β���ģʽ
	public static final int ANIMEMODE_RETURN = 3;		//����ģʽ
	
	protected VImageInterface imagesource;		//ͼ��Դ
	protected String name;
	protected int frame = 1;					//ÿ��֡��
	protected double time = 1;					//ÿ֡����
	
	protected int plsize = 1;					//�����б��С
	protected int playlist[];					//�����б�
	protected long animecount = 0;				//��������,�Ա仯��ֵ�����Ե����߷��ض�������һ֡
	protected boolean stateanime = false;		//����״̬�Ƿ���GameState���,����,
												//�����Ϸ״̬Ϊ����״̬(����֮ͣ��)ʱ���������Ż�����
	protected int animemode = 1;		//����ģʽ��Ĭ��Ϊĩֹ֡ͣ
	
	public VAnime(String str,int f){
		imagesource = Imageconst.GetImageByName(str);
		name=str;
		if(f>0){
			frame = f;
			time = 1000.0/f;
		}
	}
	public VAnime getInstance(){
		// TODO ��ȡ������������Ҫ�ظ�����ͬһ������ʱʹ�ã������Ķ�������Ϊ��ʼ״̬
		VAnime newanime = new VAnime(this.name,frame);
		newanime.setPlayList(plsize);
		return newanime;
	}
	public void setState(boolean b){
		// TODO ���ö����Ƿ�Ϊ��GS״̬���йصĶ���
		stateanime = b;
	}
	public void setMode(int i){
		animemode = i;
	}
	public void animeReset(){
		// TODO ���ö�������
		if(stateanime){
			animecount = VEngine.gs.getCurrentStage().updatecount*VEngine.gs.getMSecond();
		}
		else{
			animecount = System.currentTimeMillis();
		}
	}
	public void stateReset(){
		animecount = 0;
	}
	public void setPlayList(int[] list){
		// TODO �����Զ��岥���б�
		playlist = list;
		plsize = playlist.length;
	}
	public void setPlayList(int x){
		// TODO ˳�򲥷�,xΪ�����б��С
		plsize = x;
		playlist = new int[x];
		for(int i=0;i<x;i++){
			playlist[i]=i;
		}
	}
	public Image getImage(){
		int index=0;			//���ջ�ȡԴVImageInterfaceͼ�������
		long uc=0;			//����ʱ�����
		if(stateanime){		//����ǹؿ�״̬��������ͣʱ�����£�
			uc = VEngine.gs.getCurrentStage().updatecount*VEngine.gs.getMSecond();
		}
		else{			//�������ʱ�����Ϊ��ȡ��ǰ����
			uc = System.currentTimeMillis();
		}
		switch(animemode){
		case ANIMEMODE_PERODIC:{		//ѭ��ģʽ
			if(animecount==0||uc-animecount>=time*plsize){
				//������������֡���鷶Χ��ص���ʼ
				animecount = uc;
			}
			index = playlist[(int)((uc-animecount)/time)];	//��������
			return imagesource.getImage(index);
		}
		case ANIMEMODE_LASTFRAME:{		//ĩֹ֡ͣģʽ
			if(animecount==0){
				//���ڳ��ε���ʱ��animecount����Ϊ��ǰʱ��
				animecount = uc;
			}
			int plindex = (int)((uc-animecount)/time);	//��������
			if(plindex>=plsize){
				plindex = plsize-1;	//�糬����Χ����Ϊβ֡
			}
			return imagesource.getImage(playlist[plindex]);
		}
		default:{
			return null;
		}
		}
	}
	public Image getImage(int i) {
		// TODO ��ȡ����ָ��֡��ͼ��
		return imagesource.getImage(i);
	}
	public int getHeight() {
		// TODO Auto-generated method stub
		return imagesource.getHeight();
	}
	public int getWidth() {
		// TODO Auto-generated method stub
		return imagesource.getWidth();
	}

	public void prepareAnime() {
		// TODO Auto-generated method stub
 		for(int i=0;i<plsize;i++){
			if(imagesource.getImage(i)!=null){
				VEngine.p.prepareImage(imagesource.getImage(i), VEngine.p);
			}
		}
	}
}
