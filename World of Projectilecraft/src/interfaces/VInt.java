/**
 * �ļ����ƣ�VInt.java
 * ��·����interfaces
 * ������TODO ��ֵ̬�ӿڵĴ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-4-5����12:34:03
 * �汾��Ver 1.0
 */
package interfaces;

/**
 * @author Demilichzz
 *
 */
public class VInt implements VValueInterface{
	protected int value;
	
	public VInt(int v){
		value = v;
	}
	public int getValue(int index) {
		// TODO Auto-generated method stub
		return value;
	}

	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		this.value = value;
	}
}
