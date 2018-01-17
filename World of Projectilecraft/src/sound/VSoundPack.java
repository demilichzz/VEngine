/**
 * 	TODO ��Ƶ���࣬��ͨ��������ַ�����������ѡȡָ����Ƶ���ţ������������Ƿ񲢷����ŵȹ���
 */	
package sound;

import interfaces.VSoundController;
import global.Debug;
import global.FSMconst;
import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-11-28
 */
public class VSoundPack implements VSoundController{
	protected String[]	soundprefix;	//��Ƶǰ׺
	protected int[]	indexrange;			//������Χ
	protected int[] indexlist;			//���������б�
	protected int currentindex=-1;			//��ǰ����
	protected String lastsound="";
	protected String currentsound="";
	protected int maxtype;			//һ�����ڼ�¼����Ƶǰ׺�������
	protected String suffix = ".mp3";	//��׺
	protected boolean mixmode = false;	//�Ƿ�Ϊ����ģʽ���粻��������Ƶ����ʱֹͣǰһ��Ƶ
	
	public VSoundPack(int i){
		soundprefix = new String[i];
		indexrange = new int[i];
		indexlist = new int[i];
		maxtype = i;
	}
	/**
	 * @param prefix ��Ƶǰ׺�ַ�����Ϊ�ļ������һλ����֮ǰ�������ַ�
	 * @param range
	 */
	public void addPrefix(String prefix,int range){
		// TODO ����Ƶ���������Ƶǰ׺
		for(int i=0;i<maxtype;i++){
			if(indexrange[i]==0){		//�������ʣ��
				soundprefix[i]=prefix;
				indexrange[i]=range;
				indexlist[i]=1;
				return;
			}
		}
		Debug.DebugSimpleMessage("����Ƶ������");
	}
	public void setMixMode(boolean b){
		mixmode = b;
	}
	public void soundPlay(int p){
		if(p<maxtype&&p!=-1){
			currentindex=p;
			String str = soundprefix[p];
			soundPlay(str);
		}
	}
	public void soundPlay(String prefix){
		int sindex=-1;		//��ȡ��Ӧ����
		for(int i=0;i<maxtype;i++){
			if(soundprefix[i]!=null){
				if(soundprefix[i].equals(prefix)){
					sindex = i;
				}
			}
		}
		if(sindex!=-1){
			String filename = soundprefix[sindex]+indexlist[sindex]+suffix;		//�����ַ�����ȡ��Ƶ�ļ���
			indexlist[sindex]++;
			if(indexlist[sindex]>indexrange[sindex]){	//����Χ
				indexlist[sindex]=1;
			}
			if(!lastsound.equals("")){
				VSound last = Soundconst.GetSoundByName(lastsound);
				if(last!=null){
					last.soundStop();
				}
			}
			VSound current = Soundconst.GetSoundByName(filename);
			if(current!=null){
				lastsound = filename;
				current.soundPlay(this);
			}
		}
		else{
			Debug.DebugSimpleMessage("�޷��ڴ˰����ҵ�ǰ׺Ϊ"+prefix+"����Ƶ�ļ�");
		}
	}
	public void soundStop(){
		// TODO ֹͣ��������Ƶ
		VSound last = Soundconst.GetSoundByName(lastsound);
		if(last!=null){
			currentindex=-1;
			last.soundStop();
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VSoundController#musicend()
	 */
	@Override
	public void musicend(int i) {
		// TODO Auto-generated method stub
		switch(i){
		case FSMconst.SOUND_STATE_PLAY:{		//��PLAY״̬�ص���˵���������Ѳ������
			this.soundPlay(currentindex);	//�������ڲ��ŵ��б�����һ����Ƶ
			break;
		}
		case FSMconst.SOUND_STATE_STOP:{		//STOP״̬�ص���˵��������;ֹͣ
			break;
		}
		}
		
	}
}
