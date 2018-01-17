/**
 * 文件名称：VImageInterface.java
 * 类路径：view
 * 描述：TODO 图像接口
 * 作者：Demilichzz
 * 时间：2011-10-27上午06:11:44
 * 版本：Ver 1.0
 */
package view;

import java.awt.Image;

/**
 * @author Demilichzz
 *
 */
public interface VImageInterface {
	public Image getImage();		//获取当前Image
	public Image getImage(int i);	//按索引获取Image
	public int getWidth();
	public int getHeight();
}
