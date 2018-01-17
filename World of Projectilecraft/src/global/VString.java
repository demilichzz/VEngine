/**
 * 文件名称：VString.java
 * 类路径：global
 * 描述：TODO 用于字符串操作的公有类,包含对中文字符进行判断截取等功能的函数
 * 作者：Demilichzz
 * 时间：2012-3-12上午07:36:29
 * 版本：Ver 1.0
 */
package global;

/**
 * @author Demilichzz
 * 
 */
public class VString {
	/**
	 * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位
	 * 
	 * @param str
	 *            原始字符串
	 * @param srcPos
	 *            开始位置
	 * @param specialCharsLength
	 *            截取长度(汉、日、韩文字符长度为2)
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
		// 获得字符串的长度
		char[] chars = str.toCharArray();
		if (srcPos > chars.length) {
			return "";
		}
		int cl1 = getCharsLength(chars, srcPos);
		int charsLength = getCharsLength(chars, specialCharsLength);
		return new String(chars, cl1, charsLength-cl1);
	}

	/**
	 * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
	 * 
	 * @param chars
	 *            一段字符
	 * @param specialCharsLength
	 *            输入长度，汉、日、韩文字符长度为2
	 * @return 输出长度，所有字符均长度为1
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
	 * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
	 * 
	 * @param c
	 *            字符
	 * @return 字符长度
	 */
	private static int getSpecialCharLength(char c) {
		if (isLetter(c)) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param c
	 *            需要判断的字符
	 * @return 返回true,Ascill字符
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s
	 *            需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null) {
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			// 如果为汉，日，韩，则多加一位
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}
}
