/**
 * 文件名称：VtoXMLInterface.java
 * 类路径：xml
 * 描述：TODO 转换为XML包类的接口,实现此接口的toXMLPack函数从而将类实例转化为易于转换的XML包类
 * 作者：Demilichzz
 * 时间：2012-4-19下午02:13:52
 * 版本：Ver 1.0
 */
package xml;


/**
 * @author Demilichzz
 *
 */
public interface VtoXMLInterface {
	public XMLPack toXMLPack();
	public void setValueFromPack(XMLPack p);
}
