/*
 * �ر������������������ܡ��л����񹲺͹�����Ȩ���������������������������ȷ��ɡ����桢����
 * �����Լ��йع�����Լ�ı�������ͬ�Ƽ�����֪ʶ��Ȩ������һ��Ȩ��������Ϊ�������ܡ�δ������˾��
 * ����ɣ��κ��˲������ԣ������������ڣ��ԷǷ��ķ�ʽ���ơ�������չʾ���������ء����أ�ʹ�ã�
 * �����������й¶��͸¶����¶�����򣬱���˾������׷����Ȩ�ߵķ������Ρ��ش�������
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
     * �������ƣ��ϲ��ַ�������
     * <p>
     * �������ܣ��������ַ�������ϲ���һ��
     * 
     * @param arrayA
     *        �ַ�������A
     * @param arrayB
     *        �ַ�������A
     * @return �ϲ��������b
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:11:45 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ�����ַ���
     * <p>
     * �������ܣ���ָ���ַ����������һ���ַ��������ַ�iChar������ַ���sBufData��isBufLenλ��isBufLenΪ�������룬isBufLenΪ�����ҿ���
     * 
     * @param sBufData
     *        �������ַ���
     * @param iChar
     *        ����ַ�
     * @param isBufLen
     *        ��䳤�ȣ�Ϊ�������룬Ϊ�����ҿ���
     * @return ������ַ���
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:13:36 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ���õ�ǰ����
     * <p>
     * �������ܣ���õ�ǰ����
     * 
     * @param date
     *        ����
     * @param dateFormatPattern
     *        ���ڸ�ʽ�����д��null,��ʽΪyyyy-MM-dd hh:mm:ss
     * @return ����ָ�����ڸ�ʽ�õ�������
     * @throws ParseException
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:15:09 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ���õ�ǰ����
     * <p>
     * �������ܣ���õ�ǰ����
     * 
     * @param dateFormatPattern
     *        ���ڸ�ʽ�����д��null,��ʽΪyyyy-MM-dd hh:mm:ss
     * @throws ParseException
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:16:35 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ��жϸ����������Ƿ���ָ���ĸ�ʽ
     * <p>
     * �������ܣ��жϸ����������Ƿ���ָ���ĸ�ʽ
     * 
     * @param dateString
     *        ָ�������� dateFormatPattern ���ڵĸ�ʽ�����ڸ�ʽ���Բο�SimpleDateFormat�е����ڸ�ʽ
     * @return boolean ����true����ʾ������������ָ���ĸ�ʽ
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:17:49 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��boolean b1=isValidDate("2010-08-11","yyyy-MM-dd"),b1����true; boolean
     *         b2=isValidDate("2010-08-11","yyyy:MM:dd"),b1����false;
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
     * �������ƣ��ж�ָ�����ַ����Ƿ��������ʽ
     * <p>
     * �������ܣ��ж�ָ�����ַ����Ƿ��������ʽ
     * 
     * @param emailAddrString
     *        ��Ҫ�жϵ��ַ���
     * @return boolean ����true��ʾ�������ַ����������ʽ
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:20:49 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ��ж�ָ�����ַ���ת����long�����ݺ�Ĵ�С�Ƿ��ڸ���������ֵ֮��
     * <p>
     * �������ܣ��ж�ָ�����ַ���ת����long�����ݺ�Ĵ�С�Ƿ��ڸ���������ֵ֮��
     * 
     * @param numberString
     *        ��Ҫת�������ֵ��ַ��� min ָ����Χ����Сֵ max ָ����Χ�����ֵ
     * @return boolean Ϊtrue����ʾ������������֮��
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:22:34 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ��ж�ָ�����ַ���ת����double�����ݺ�Ĵ�С�Ƿ��ڸ���������ֵ֮��
     * <p>
     * �������ܣ��ж�ָ�����ַ���ת����long�����ݺ�Ĵ�С�Ƿ��ڸ���������ֵ֮��
     * 
     * @param numberString
     *        ��Ҫת�������ֵ��ַ��� min ָ����Χ����Сֵ max ָ����Χ�����ֵ
     * @return boolean Ϊtrue����ʾ������������֮��
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:22:34 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ��ж�ָ�����ַ���ת����double�����ݺ�Ĵ�С�Ƿ��ڸ���������ֵ֮��
     * <p>
     * �������ܣ��ж�ָ�����ַ���ת����long�����ݺ�Ĵ�С�Ƿ��ڸ���������ֵ֮��
     * 
     * @param numberString
     *        ��Ҫת�������ֵ��ַ��� min ָ����Χ����Сֵ���ַ�����ʽ max ָ����Χ�����ֵ���ַ�����ʽ
     * @return boolean Ϊtrue����ʾ������������֮��
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:22:34 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * ��������: �ж�ָ�����ַ����Ƿ��ڸ����ַ���������
     * <p>
     * �������ܣ��ж�ָ�����ַ����Ƿ��ڸ����ַ���������
     * 
     * @param value
     *        ָ�����ַ��� validStrings ���������� ignoreCase �Ƿ�value���Դ�Сд
     * @return boolean �������ַ������������з���true
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:29:19 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ���ԭ�ַ����в��ҵ���ָ���Ӵ��滻�������ַ���
     * <p>
     * �������ܣ���ԭ�ַ����в��ҵ���ָ���Ӵ��滻�������ַ���
     * 
     * @param in
     *        ԭ�ַ��� from ��Ҫ���ҵ��Ӵ�to�滻from���ַ���
     * @return String �����滻������ַ���
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:31:59 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��replaceInString("adcder", "e", "3");��󷵻ص���adcd3r����abcder�е�e�滻��3��
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
     * �������ƣ����ֽ������nStartλ��ʼ�����һ��byte ����
     * <p>
     * �������ܣ����ֽ������nStartλ��ʼ�����һ��byte ����
     * 
     * @param bObjData
     *        Ŀ������
     * @param nStart
     *        �����ʵλ��
     * @param sSrcData
     *        �������
     *        <p>
     *        ��дʱ�䣺2010-8-25 ����09:33:44 <br>
     *        �޸��ˣ� (�������޸���) <br>
     *        �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *        ������ע��
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
     * �������ƣ�����ָ�����ֽ�λ�÷ָ�һ���ַ���Ϊһ�����������ַ���������
     * <p>
     * �������ܣ�����ָ�����ֽ�λ�÷ָ�һ���ַ���Ϊһ�����������ַ���������
     * 
     * @param source
     *        ���ָ���ַ���
     * @param bytePosition
     *        �ֽ�λ��
     * @return �ָ����ַ�������
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:35:43 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ����ַ�����ʽ������ת����ָ����ʽ������
     * <p>
     * �������ܣ����ַ�����ʽ������ת����ָ����ʽ�����ڣ���󷵻�����
     * 
     * @param dateString
     *        �ַ�����ʽ������ dateFormatPattern �ο�SimpleDateFormat�����ڸ�ʽ
     * @return Date ��������
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:36:43 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ����ַ���ת��������
     * <p>
     * �������ܣ��Ѹ������ַ���ת��������
     * 
     * @param numString
     *        ��Ҫת�����ַ���
     * @return Number ��������
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:38:09 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ����ַ���ת��������
     * <p>
     * �������ܣ�����ָ���ĸ�ʽ�Ǹ����ַ���ת��������
     * 
     * @param numString
     *        ��Ҫת�����ַ��� numFormatPattern ���ָ�ʽ
     * @return Number ��������
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:39:37 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ��ַ����ĸ�ʽת��
     * <p>
     * �������ܣ��ַ����ĸ�ʽת��
     * 
     * @param numString
     *        ��Ҫת�����ַ���
     * @return String ��ʽת������ַ���
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:41:07 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
     * �������ƣ�����ָ����ʽ���ַ����ĸ�ʽת��
     * <p>
     * �������ܣ�����ָ����ʽ���ַ����ĸ�ʽת��
     * 
     * @param numString
     *        ��Ҫת�����ַ��� numFormatPattern �ַ�����ʽ
     * @return String ��ʽת������ַ���
     *         <p>
     *         ��дʱ�䣺2010-8-25 ����09:42:14 <br>
     *         �޸��ˣ� (�������޸���) <br>
     *         �޸�ʱ�䣺(�������޸�ʱ�䣬��������޸������Ӧ��) <br>
     *         ������ע��
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
