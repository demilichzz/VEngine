/**
 * �ļ����ƣ�VCollisionInterface.java
 * ��·����interfaces
 * ������TODO ��ײģ�ͽӿڣ�ʵ�ָýӿڵ���ײ��⺯���Զ�������ײģ�Ͷ��������ײ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-7����09:58:25
 * �汾��Ver 1.0
 */
package interfaces;

/**
 * @author Demilichzz
 *
 */
public interface VCollisionInterface {
	public boolean cDetection(VCollisionInterface target);
	public int getCType();	//��ȡ��ײģ������
	public double getCollRad();	//��ȡԲ����ײ�İ뾶
}
