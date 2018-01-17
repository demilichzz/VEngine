/**
 * �ļ����ƣ�VStringNode.java
 * ��·����xml
 * ������TODO �ַ����ڵ���,����ʵ�ִ�ͬһ����ʵ���ȡ����String��һ��String��������ݽṹ
 * 			  �Զ����µ�����״�ṹ
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-4-21����08:58:09
 * �汾��Ver 1.0
 */
package xml;

import java.util.ArrayList;

/**
 * @author Demilichzz
 *
 */
public class VStringNode {
	protected String nodename;
	protected boolean isAttribute;

	protected ArrayList<VStringNode> children;	// �ӽڵ�
		
	public VStringNode(String name){
		nodename = name;
		isAttribute = false;
		children=new ArrayList<VStringNode>();
	}
	public void addChildren(ArrayList<VStringNode> children){
		// TODO ����ӽڵ��б�
		this.children.addAll(children);
	}
	public void addChildren(String[] children){
		// TODO ͨ���ַ�����������ӽڵ�
		for(int i=0;i<children.length;i++){
			VStringNode n = new VStringNode(children[i]);
			addChild(n);
		}
	}
	public void addChild(VStringNode n){
		// TODO ��ӵ����ӽڵ�
		if(n!=null){
			this.children.add(n);
		}
	}
	public void addChild(String str){
		// TODO ͨ���ַ�����ӵ����ڵ�
		VStringNode n = new VStringNode(str);
		addChild(n);
	}
	
	public ArrayList<VStringNode> getChildren(){
		// TODO ��ȡ�ӽڵ�����
		return children;
	}
	public String getName(){
		// TODO ��ȡ�ýڵ����ƣ�ֵ��
		return nodename;
	}
	public void setName(String name){
		this.nodename = name;
	}
	public boolean haveChildren(){
		// TODO ��ȡ�ڵ��Ƿ������ӽڵ�
		if(children!=null){
			if(children.size()>0){
				return true;
			}
		}
		return false;
	}
	public boolean isAttribute() {
		// TODO ��ȡ�ڵ��Ƿ�������
		return isAttribute;
	}
	public void setAttribute(boolean b) {
		// TODO ���ýڵ��Ƿ�������
		this.isAttribute = b;
	}
}
