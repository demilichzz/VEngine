/**
 * �ļ����ƣ�VString.java
 * ��·����global
 * ������TODO �����ַ��������Ĺ�����,�����������ַ������жϽ�ȡ�ȹ��ܵĺ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-12����07:36:29
 * �汾��Ver 1.0
 */
package global;

/**
 * @author Demilichzz
 * 
 */
public class VString {
	/**
	 * ��ȡһ���ַ��ĳ���(�����ա������ַ�����Ϊ2),��������Ӣ��,������ֲ����ã�����ȡһ���ַ�λ
	 * 
	 * @param str
	 *            ԭʼ�ַ���
	 * @param srcPos
	 *            ��ʼλ��
	 * @param specialCharsLength
	 *            ��ȡ����(�����ա������ַ�����Ϊ2)
	 * @return
	 */
	public static String substring(String str, int srcPos,
			int specialCharsLength) {
		if (str == null || "".equals(str) || specialCharsLength < 1) {
			return "";
		}
		if (srcPos < 0) {
			srcPos = 0;
		}
		if (specialCharsLength <= 0) {
			return "";
		}
		// ����ַ����ĳ���
		char[] chars = str.toCharArray();
		if (srcPos > chars.length) {
			return "";
		}
		int cl1 = getCharsLength(chars, srcPos);
		int charsLength = getCharsLength(chars, specialCharsLength);
		return new String(chars, cl1, charsLength-cl1);
	}

	/**
	 * ��ȡһ���ַ��ĳ��ȣ����볤���к����ա������ַ�����Ϊ2����������������ַ�������Ϊ1
	 * 
	 * @param chars
	 *            һ���ַ�
	 * @param specialCharsLength
	 *            ���볤�ȣ������ա������ַ�����Ϊ2
	 * @return ������ȣ������ַ�������Ϊ1
	 */
	private static int getCharsLength(char[] chars, int specialCharsLength) {
		int count = 0;
		int normalCharsLength = 0;
		for (int i = 0; i < chars.length; i++) {
			int specialCharLength = getSpecialCharLength(chars[i]);
			if (count <= specialCharsLength - specialCharLength) {
				count += specialCharLength;
				normalCharsLength++;
			} else {
				break;
			}
		}
		return normalCharsLength;
	}

	/**
	 * ��ȡ�ַ����ȣ������ա������ַ�����Ϊ2��ASCII����ַ�����Ϊ1
	 * 
	 * @param c
	 *            �ַ�
	 * @return �ַ�����
	 */
	private static int getSpecialCharLength(char c) {
		if (isLetter(c)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * �ж�һ���ַ���Ascill�ַ����������ַ����纺���գ������ַ���
	 * 
	 * @param c
	 *            ��Ҫ�жϵ��ַ�
	 * @return ����true,Ascill�ַ�
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * �õ�һ���ַ����ĳ���,��ʾ�ĳ���,һ�����ֻ��պ��ĳ���Ϊ2,Ӣ���ַ�����Ϊ1
	 * 
	 * @param s
	 *            ��Ҫ�õ����ȵ��ַ���
	 * @return i�õ����ַ�������
	 */
	public static int length(String s) {
		if (s == null) {
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			// ���Ϊ�����գ���������һλ
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}
}
