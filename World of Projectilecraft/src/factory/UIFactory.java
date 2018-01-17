/**
 * 文件名称：UIFactory.java
 * 类路径：factory
 * 描述：TODO UI工厂类,用于根据给定的格式化的参数组,按照其中的UIClass参数创建不同的UI对象
 * 作者：Demilichzz
 * 时间：2011-11-16下午04:47:58
 * 版本：Ver 1.0
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
		// TODO 使用从xml中获取的String表创建UI
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
