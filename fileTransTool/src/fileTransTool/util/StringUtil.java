/*
 * 特别声明：本技术材料受《中华人民共和国著作权法》、《计算机软件保护条例》等法律、法规、行政
 * 规章以及有关国际条约的保护，赞同科技享有知识产权、保留一切权利并视其为技术秘密。未经本公司书
 * 面许可，任何人不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使用，
 * 不得向第三方泄露、透露、披露。否则，本公司将依法追究侵权者的法律责任。特此声明！
 *
 * Special Declaration: These technical material reserved as the technical secrets by AGREE 
 * TECHNOLOGY have been protected by the "Copyright Law" "ordinances on Protection of Computer 
 * Software" and other relevant administrative regulations and international treaties. Without 
 * the written permission of the Company, no person may use (including but not limited to the 
 * illegal copy, distribute, display, image, upload, and download) and disclose the above 
 * technical documents to any third party. Otherwise, any infringer shall afford the legal 
 * liability to the company.
 */
/*
 * Copyright(C) 2006 Agree Tech, All rights reserved.
 *
 * Created on 2006-7-21   by Xu Haibo
 */

package fileTransTool.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil
{
    // Static format objects
    private static SimpleDateFormat dateFormat = new SimpleDateFormat();

    private static DecimalFormat numberFormat = new DecimalFormat();

    /**
     * 
     * 函数名称：合并字符串数组
     * <p>
     * 函数功能：将两个字符串数组合并成一个
     * 
     * @param arrayA
     *        字符串数组A
     * @param arrayB
     *        字符串数组A
     * @return 合并后的数组b
     *         <p>
     *         编写时间：2010-8-25 上午09:11:45 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String[] combineArray(String[] arrayA, String[] arrayB)
    {
        if (arrayA == null)
        {
            return arrayB;
        }
        if (arrayB == null)
        {
            return arrayA;
        }
        String[] array = new String[arrayA.length + arrayB.length];
        System.arraycopy(arrayA, 0, array, 0, arrayA.length);
        System.arraycopy(arrayB, 0, array, arrayA.length, arrayB.length);
        return array;
    }

    /**
     * 
     * 函数名称：填充字符串
     * <p>
     * 函数功能：用指定字符串填充另外一个字符串，用字符iChar填充满字符串sBufData到isBufLen位，isBufLen为负数左靠齐，isBufLen为正数右靠齐
     * 
     * @param sBufData
     *        被填充的字符串
     * @param iChar
     *        填充字符
     * @param isBufLen
     *        填充长度，为负数左靠齐，为正数右靠齐
     * @return 填充后的字符串
     *         <p>
     *         编写时间：2010-8-25 上午09:13:36 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String fixFill(String sBufData, String iChar, int isBufLen)
    {
        assert (sBufData != null);
        assert (iChar != null);

        String sRetMsg;
        byte bObjData[];
        byte bBufData[];
        try
        {
            bBufData = sBufData.getBytes("GBK");
        } catch (UnsupportedEncodingException e)
        {
            bBufData = sBufData.getBytes();
        }
        byte bCharData[];
        try
        {
            bCharData = iChar.getBytes("GBK");
        } catch (UnsupportedEncodingException e)
        {
            bCharData = iChar.getBytes();
        }
        int iLen;

        int sBufDataLen = bBufData.length;
        if (isBufLen < 0)
        {
            iLen = 0 - isBufLen;
            bObjData = new byte[iLen];
            if (sBufDataLen > iLen)
            {
                sBufDataLen = iLen;
            }
        } else
        {
            iLen = isBufLen;
            bObjData = new byte[isBufLen];
            if (sBufDataLen > iLen)
            {
                int iStart = sBufDataLen - iLen;
                for (int i = 0; i < iLen; i++)
                {
                    bBufData[i] = bBufData[i + iStart];
                }
                sBufDataLen = iLen;
            }
        }
        if (isBufLen < 0)
        {
            for (int i = 0; i < sBufDataLen; i++)
            {
                bObjData[i] = bBufData[i];
            }
            for (int i = sBufDataLen; i < iLen; i++)
            {
                bObjData[i] = bCharData[0];
            }
        } else
        {
            int iStart = isBufLen - sBufDataLen;
            for (int i = 0; i < iStart; i++)
            {
                bObjData[i] = bCharData[0];
            }
            for (int i = 0; i < sBufDataLen; i++)
            {
                bObjData[iStart + i] = bBufData[i];
            }
        }
        /*
         * if (isBufLen < 0) { bObjData = hzFixFill(bObjData, iLen); }
         */
        sRetMsg = new String(bObjData);
        return sRetMsg;
    }

    /**
     * 
     * 函数名称：获得当前日期
     * <p>
     * 函数功能：获得当前日期
     * 
     * @param date
     *        日期
     * @param dateFormatPattern
     *        日期格式，如果写入null,格式为yyyy-MM-dd hh:mm:ss
     * @return 根据指定日期格式得到的日期
     * @throws ParseException
     *         <p>
     *         编写时间：2010-8-25 上午09:15:09 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String getDate(Date date, String dateFormatPattern)
            throws ParseException
    {
        if (date == null)
        {
            Date sysdate = new Date();
            date = sysdate;
        }
        if (dateFormatPattern == null)
        {
            dateFormatPattern = "yyyy-MM-dd hh:mm:ss";
        }
        dateFormat = new SimpleDateFormat(dateFormatPattern);
        return dateFormat.format(date);
    }

    /**
     * 
     * 函数名称：获得当前日期
     * <p>
     * 函数功能：获得当前日期
     * 
     * @param dateFormatPattern
     *        日期格式，如果写入null,格式为yyyy-MM-dd hh:mm:ss
     * @throws ParseException
     *         <p>
     *         编写时间：2010-8-25 上午09:16:35 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String getSysDate(String dateFormatPattern)
            throws ParseException
    {
        Date sysdate = new Date();
        if (dateFormatPattern == null)
        {
            dateFormatPattern = "yyyy-MM-dd hh:mm:ss";
        }
        dateFormat = new SimpleDateFormat(dateFormatPattern);
        return dateFormat.format(sysdate);
    }

    /**
     * 
     * 函数名称：判断给定的日期是否是指定的格式
     * <p>
     * 函数功能：判断给定的日期是否是指定的格式
     * 
     * @param dateString
     *        指定的日期 dateFormatPattern 日期的格式。日期格式可以参考SimpleDateFormat中的日期格式
     * @return boolean 返回true，表示给定的日期是指定的格式
     *         <p>
     *         编写时间：2010-8-25 上午09:17:49 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：boolean b1=isValidDate("2010-08-11","yyyy-MM-dd"),b1返回true; boolean
     *         b2=isValidDate("2010-08-11","yyyy:MM:dd"),b1返回false;
     */
    public static boolean isValidDate(String dateString,
            String dateFormatPattern)
    {
        assert (dateString != null);
        assert (dateFormatPattern != null);
        Date validDate = null;
        synchronized (dateFormat)
        {
            try
            {
                dateFormat.applyPattern(dateFormatPattern);
                dateFormat.setLenient(false);
                validDate = dateFormat.parse(dateString);
            } catch (ParseException e)
            {
                // Ignore and return null
            }
        }
        return validDate != null;
    }

    /**
     * 
     * 函数名称：判断指定的字符串是否是邮箱格式
     * <p>
     * 函数功能：判断指定的字符串是否是邮箱格式
     * 
     * @param emailAddrString
     *        需要判断的字符串
     * @return boolean 返回true表示给定的字符串是邮箱格式
     *         <p>
     *         编写时间：2010-8-25 上午09:20:49 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static boolean isValidEmailAddr(String emailAddrString)
    {
        boolean isValid = false;
        if (emailAddrString != null && emailAddrString.indexOf("@") != -1
                && emailAddrString.indexOf(".") != -1)
        {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 
     * 函数名称：判断指定的字符串转换成long型数据后的大小是否在给定的两个值之间
     * <p>
     * 函数功能：判断指定的字符串转换成long型数据后的大小是否在给定的两个值之间
     * 
     * @param numberString
     *        需要转换成数字的字符串 min 指定范围的最小值 max 指定范围的最大值
     * @return boolean 为true，表示在两个给定数之间
     *         <p>
     *         编写时间：2010-8-25 上午09:22:34 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static boolean isValidInteger(String numberString, long min, long max)
    {
        assert (numberString != null);
        Long validLong = null;
        try
        {
            Number aNumber = numberFormat.parse(numberString);
            long aLong = aNumber.longValue();
            if (aLong >= min && aLong <= max)
            {
                validLong = new Long(aLong);
            }
        } catch (ParseException e)
        {
            // Ignore and return null
        }
        return validLong != null;
    }

    /**
     * 
     * 函数名称：判断指定的字符串转换成double型数据后的大小是否在给定的两个值之间
     * <p>
     * 函数功能：判断指定的字符串转换成long型数据后的大小是否在给定的两个值之间
     * 
     * @param numberString
     *        需要转换成数字的字符串 min 指定范围的最小值 max 指定范围的最大值
     * @return boolean 为true，表示在两个给定数之间
     *         <p>
     *         编写时间：2010-8-25 上午09:22:34 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static boolean isValidNumber(String numberString, double min,
            double max)
    {
        assert (numberString != null);

        boolean validNumber = false;
        try
        {
            Number aNumber = toNumber(numberString);
            double anDouble = aNumber.doubleValue();
            if (anDouble >= min && anDouble <= max)
            {
                validNumber = true;
            }
        } catch (ParseException e)
        {
            // Ignore and return null
        }
        return validNumber;
    }

    /**
     * 
     * 函数名称：判断指定的字符串转换成double型数据后的大小是否在给定的两个值之间
     * <p>
     * 函数功能：判断指定的字符串转换成long型数据后的大小是否在给定的两个值之间
     * 
     * @param numberString
     *        需要转换成数字的字符串 min 指定范围的最小值的字符串形式 max 指定范围的最大值的字符串形式
     * @return boolean 为true，表示在两个给定数之间
     *         <p>
     *         编写时间：2010-8-25 上午09:22:34 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static boolean isValidNumber(String numberString, String min,
            String max)
    {
        assert (numberString != null);
        assert (min != null);
        assert (max != null);
        boolean validNumber = false;
        try
        {
            Number aNumber = toNumber(numberString);
            Number nMin = toNumber(min);
            Number nMax = toNumber(max);
            double anDouble = aNumber.doubleValue();
            double dMin = nMin.doubleValue();
            double dMax = nMax.doubleValue();
            if (anDouble >= dMin && anDouble <= dMax)
            {
                validNumber = true;
            }
        } catch (ParseException e)
        {
            // Ignore and return null
        }
        return validNumber;
    }

    /**
     * 
     * 函数名称: 判断指定的字符串是否在给定字符串数组中
     * <p>
     * 函数功能：判断指定的字符串是否在给定字符串数组中
     * 
     * @param value
     *        指定的字符串 validStrings 给定的数组 ignoreCase 是否value忽略大小写
     * @return boolean 当给定字符串存在数组中返回true
     *         <p>
     *         编写时间：2010-8-25 上午09:29:19 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static boolean isValidString(String value, String[] validStrings,
            boolean ignoreCase)
    {
        boolean isValid = false;
        for (int i = 0; validStrings != null && i < validStrings.length; i++)
        {
            if (ignoreCase)
            {
                if (validStrings[i].equalsIgnoreCase(value))
                {
                    isValid = true;
                    break;
                }
            } else
            {
                if (validStrings[i].equals(value))
                {
                    isValid = true;
                    break;
                }
            }
        }
        return isValid;
    }

    /**
     * 
     * 函数名称：在原字符串中查找到的指定子串替换成另外字符串
     * <p>
     * 函数功能：在原字符串中查找到的指定子串替换成另外字符串
     * 
     * @param in
     *        原字符串 from 需要查找的子串to替换from的字符串
     * @return String 类型替换后的新字符串
     *         <p>
     *         编写时间：2010-8-25 上午09:31:59 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：replaceInString("adcder", "e", "3");最后返回的是adcd3r，把abcder中的e替换成3了
     */
    public static String replaceInString(String in, String from, String to)
    {
        if (in == null || from == null || to == null)
        {
            return in;
        }

        StringBuffer newValue = new StringBuffer();
        char[] inChars = in.toCharArray();
        int inLen = inChars.length;
        char[] fromChars = from.toCharArray();
        int fromLen = fromChars.length;

        for (int i = 0; i < inLen; i++)
        {
            if (inChars[i] == fromChars[0] && (i + fromLen) <= inLen)
            {
                boolean isEqual = true;
                for (int j = 1; j < fromLen; j++)
                {
                    if (inChars[i + j] != fromChars[j])
                    {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual)
                {
                    newValue.append(to);
                    i += fromLen - 1;
                } else
                {
                    newValue.append(inChars[i]);
                }
            } else
            {
                newValue.append(inChars[i]);
            }
        }
        return newValue.toString();
    }

    /**
     * 
     * 函数名称：往字节数组第nStart位开始填充另一个byte 数组
     * <p>
     * 函数功能：往字节数组第nStart位开始填充另一个byte 数组
     * 
     * @param bObjData
     *        目标数组
     * @param nStart
     *        填充其实位置
     * @param sSrcData
     *        填充数组
     *        <p>
     *        编写时间：2010-8-25 上午09:33:44 <br>
     *        修改人： (函数的修改者) <br>
     *        修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *        函数备注：
     */
    public static byte[] setBytesData(byte bObjData[], int nStart,
            byte sSrcData[])
    {
        assert (bObjData != null);
        assert (sSrcData != null);
        int nCount;
        nCount = sSrcData.length;
        for (int i = 0; i < nCount; i++)
            bObjData[nStart + i] = sSrcData[i];
        return bObjData;
    }

    public static String toHexTable(byte byteSrc[], int lengthOfLine)
    {
        return toHexTable(byteSrc, lengthOfLine, 7);
    }

    public static String toHexTable(byte byteSrc[], int lengthOfLine, int flag)
    {
        StringBuffer hexTableBuffer = new StringBuffer(256);
        int lineCount = byteSrc.length / lengthOfLine;
        int totalLen = byteSrc.length;
        if (byteSrc.length % lengthOfLine != 0)
            lineCount++;
        for (int lineNumber = 0; lineNumber < lineCount; lineNumber++)
        {
            int startPos = lineNumber * lengthOfLine;
            byte lineByte[] = new byte[Math.min(lengthOfLine, totalLen
                    - startPos)];
            System.arraycopy(byteSrc, startPos, lineByte, 0, lineByte.length);
            int flagA = flag & 4;
            if (4 == flagA)
            {
                int count = lengthOfLine * lineNumber;
                String addrStr = Integer.toString(count, 16);
                int len = addrStr.length();
                for (int i = 0; i < 8 - len; i++)
                    hexTableBuffer.append('0');

                hexTableBuffer.append(addrStr);
                hexTableBuffer.append("h: ");
            }
            int flagB = flag & 2;
            if (2 == flagB)
            {
                StringBuffer byteStrBuf = new StringBuffer();
                for (int i = 0; i < lineByte.length; i++)
                {
                    String num = Integer.toHexString(lineByte[i] & 0xff);
                    if (num.length() < 2)
                        byteStrBuf.append('0');
                    byteStrBuf.append(num);
                    byteStrBuf.append(' ');
                }
                hexTableBuffer.append(fixFill(byteStrBuf.toString(), " ", 48));
                hexTableBuffer.append("; ");
            }
            int flagC = flag & 1;
            if (1 == flagC)
            {
                for (int i = 0; i < lineByte.length; i++)
                {
                    char c = (char) lineByte[i];
                    if (c < '!')
                        c = '.';
                    try
                    {
                        if (c >= '\240' && i < lineByte.length - 1)
                        {
                            char c2 = (char) lineByte[i + 1];
                            if (c2 >= '\240')
                            {
                                String str = new String(lineByte, i, 2);
                                hexTableBuffer.append(str);
                                i++;
                                continue;
                            }
                        }
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    hexTableBuffer.append("");
                    hexTableBuffer.append(c);
                }

            }
            if (lineNumber >= lineCount - 1)
                break;
            hexTableBuffer.append('\n');
        }

        return hexTableBuffer.toString();
    }

    /**
     * 
     * 函数名称：按照指定的字节位置分割一个字符串为一个含有两个字符串的数组
     * <p>
     * 函数功能：按照指定的字节位置分割一个字符串为一个含有两个字符串的数组
     * 
     * @param source
     *        待分割的字符串
     * @param bytePosition
     *        字节位置
     * @return 分割后的字符串数组
     *         <p>
     *         编写时间：2010-8-25 上午09:35:43 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String[] splitAtBytePosition(String source, int bytePosition)
    {
        int byteWidth = 0;
        int charCursor = 0;
        for (; charCursor < source.length(); charCursor++)
        {
            char c = source.charAt(charCursor);
            int width = c > 0xff ? 2 : 1;
            if (byteWidth + width > bytePosition)
            {
                break;
            }
            byteWidth += width;
        }
        String prefix = source.substring(0, charCursor);
        String suffix = source.substring(charCursor);
        return new String[] { prefix, suffix };
    }

    /**
     * 
     * 函数名称：把字符串格式的日期转换成指定格式的日期
     * <p>
     * 函数功能：把字符串格式的日期转换成指定格式的日期，最后返回日期
     * 
     * @param dateString
     *        字符串形式的日期 dateFormatPattern 参考SimpleDateFormat中日期格式
     * @return Date 返回日期
     *         <p>
     *         编写时间：2010-8-25 上午09:36:43 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static Date toDate(String dateString, String dateFormatPattern)
            throws ParseException
    {
        assert (dateString != null);
        Date date = null;
        if (dateFormatPattern == null)
        {
            dateFormatPattern = "yyyy-MM-dd hh:mm:ss";
        }
        synchronized (dateFormat)
        {
            dateFormat.applyPattern(dateFormatPattern);
            dateFormat.setLenient(false);
            date = dateFormat.parse(dateString);
        }
        return date;
    }

    /**
     * 
     * 函数名称：把字符串转换成数字
     * <p>
     * 函数功能：把给定的字符串转换成数字
     * 
     * @param numString
     *        需要转换的字符串
     * @return Number 返回数字
     *         <p>
     *         编写时间：2010-8-25 上午09:38:09 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static Number toNumber(String numString) throws ParseException
    {
        assert (numString != null);
        Number number = null;
        String numFormatPattern = "############.##";
        synchronized (numberFormat)
        {
            numberFormat.applyPattern(numFormatPattern);
            number = numberFormat.parse(numString);
        }
        return number;
    }

    /**
     * 
     * 函数名称：把字符串转换成数字
     * <p>
     * 函数功能：按照指定的格式那个把字符串转换成数字
     * 
     * @param numString
     *        需要转换的字符串 numFormatPattern 数字格式
     * @return Number 返回数字
     *         <p>
     *         编写时间：2010-8-25 上午09:39:37 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static Number toNumber(String numString, String numFormatPattern)
            throws ParseException
    {
        assert (numString != null);
        Number number = null;
        if (numFormatPattern == null)
        {
            numFormatPattern = "############.##";
        }
        synchronized (numberFormat)
        {
            numberFormat.applyPattern(numFormatPattern);
            number = numberFormat.parse(numString);
        }
        return number;
    }

    /**
     * 
     * 函数名称：字符串的格式转换
     * <p>
     * 函数功能：字符串的格式转换
     * 
     * @param numString
     *        需要转换的字符串
     * @return String 格式转换后的字符串
     *         <p>
     *         编写时间：2010-8-25 上午09:41:07 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String toNumberString(String numString) throws ParseException
    {
        String number = "0";
        Number num = toNumber(numString);
        String numFormatPattern = "###,###,###,###.##";
        synchronized (numberFormat)
        {
            numberFormat.applyPattern(numFormatPattern);
            number = numberFormat.format(num);
        }
        return number;
    }

    /**
     * 
     * 函数名称：按照指定格式将字符串的格式转换
     * <p>
     * 函数功能：按照指定格式将字符串的格式转换
     * 
     * @param numString
     *        需要转换的字符串 numFormatPattern 字符串格式
     * @return String 格式转换后的字符串
     *         <p>
     *         编写时间：2010-8-25 上午09:42:14 <br>
     *         修改人： (函数的修改者) <br>
     *         修改时间：(函数的修改时间，与上面的修改人相对应。) <br>
     *         函数备注：
     */
    public static String toNumberString(String numString,
            String numFormatPattern) throws ParseException
    {
        String number = "0";
        Number num = toNumber(numString);
        if (numFormatPattern == null)
        {
            numFormatPattern = "###,###,###,###.##";
        }
        synchronized (numberFormat)
        {
            numberFormat.applyPattern(numFormatPattern);
            number = numberFormat.format(num);
        }
        return number;
    }
}
