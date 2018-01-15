/**
 * �ļ����ƣ�UIFactory.java
 * ��·����factory
 * ������TODO UI������,���ڸ��ݸ����ĸ�ʽ���Ĳ�����,�������е�UIClass����������ͬ��UI����
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-11-16����04:47:58
 * �汾��Ver 1.0
 */
package factory;
import ui.*;

/**
 * @author Demilichzz
 *
 */
public class UIFactory {
	/**
	 * @param uiclass
	 * @param uiID
	 * @param imagename
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public static VUI creator(String uiclass, String uiID, String imagename,
			String x, String y, String w, String h) {
		// TODO ʹ�ô�xml�л�ȡ��String����UI
		VUI temp;
		if(imagename.equals("null")){
			int iw = Integer.parseInt(w);
			int ih = Integer.parseInt(h);
			temp = new VUI(iw,ih,uiID);
		}
		else{
			temp = new VUI(imagename,uiID);
		}
		if(uiclass.equals("VMouseActionUI")){
			//temp = new VMouseActionUI(temp);
		}
		double ix = Double.parseDouble(x);
		double iy = Double.parseDouble(y);
		temp.setLoc(ix,iy);
		return temp;
	}
}
