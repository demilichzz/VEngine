/**
 * 文件名称：VInt.java
 * 类路径：interfaces
 * 描述：TODO 动态值接口的代理
 * 作者：Demilichzz
 * 时间：2012-4-5上午12:34:03
 * 版本：Ver 1.0
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
