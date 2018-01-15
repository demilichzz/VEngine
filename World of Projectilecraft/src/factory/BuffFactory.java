/**
 * 	TODO Buff����������ʹ��ָ����������BuffЧ��
 */
package factory;

import java.util.*;

import combat.*;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class BuffFactory {
	protected static HashMap<String,int[]> valuemap=new HashMap<String,int[]>();	//��BuffIDΪ����Buff����ֵHashmap
	
	public static VBuff creator(String ID){
		// TODO ͨ��ID����ָ��Buff
		int[]value=valuemap.get(ID);
		if(value!=null){
			VBuff b=new VBuff(ID,value[0]);		//����0Ϊ����ʱ��
			b.setMaxStack(value[1]);			//����1Ϊ���ѵ�����
			b.dr=value[2];						//����2Ϊ�˺����Ӱٷֱ�
			return b;
		}
		else{
			return null;
		}
	}

	/**
	 * 
	 */
	public static void initFactory() {
		// TODO ��ʼ��Buff����
		valuemap=new HashMap<String,int[]>();
		valuemap.put("arcaneblast",new int[]{1,4,15});	//����
		valuemap.put("wrath", new int[]{-1,99,15});		//���֮ŭ
		valuemap.put("weakness", new int[]{6,1,100});	//����������
		valuemap.put("invincible", new int[]{-1,1,-100});	//�޵�
	}
}
