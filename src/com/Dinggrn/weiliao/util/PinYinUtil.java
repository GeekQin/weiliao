package com.Dinggrn.weiliao.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 * 拼音工具类
 * @author pjy
 *
 */
public class PinYinUtil {
	/**
	 * 将指定的字符串转化为全大写的汉语拼音格式
	 * "老王"--->"LAOWANG"
	 * "王a老"--->"WANGALAO"
	 * "abc" ---> "ABC"
	 * "a$b$c"--->"A#B#C"
	 * 多音字取第一种读音
	 * @param string
	 * @return
	 */
	public static String getPinYin(String string){
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<string.length();i++){
				String str = string.substring(i,i+1);
				if(str.matches("[\u4e00-\u9fff]")){
					char c = str.charAt(0);
					String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c, format);
					//如果有多音字的情况，只取第一种读音
					sb.append(pinyins[0]);
				}else if(str.matches("[a-zA-Z]")){
					sb.append(str.toUpperCase());
				}else{
					sb.append("#");
				}
			}
			return sb.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			throw new RuntimeException("不正确的拼音格式");
		}
	}
	
	/**
	 * 返回指定内容转为汉语拼音格式后的首字母
	 * @param string
	 * @return
	 */
	public static String getSortLetter(String string){
		return getPinYin(string).substring(0,1);
	}
}
