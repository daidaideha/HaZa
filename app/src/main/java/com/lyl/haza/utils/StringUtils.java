package com.lyl.haza.utils;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lyl.haza.utils.log.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 */
@SuppressLint("SimpleDateFormat")
public class StringUtils {

    private static final String TAG = StringUtils.class.getSimpleName();

    /***
     * 隐藏11位手机号的中间四位为****
     *
     * @param phone 需要隐藏的手机号 "15012348992"
     * @return "150****8992"
     */
    public static String hidePhoneMidForNumber(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return "";
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 将TextView中的字符全角化，避免由于占位导致的排版混乱问题
     *
     * @param s 需要全角化的内容
     * @return 全角化的内容
     */
    public static String toFullWidth(String s) {
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280
                    && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 是否包含中文 通过正则表达式来判断
     *
     * @param str 需判断字符
     * @return true 包含汉字
     */
    public static Boolean checkChineseOld(String str) {
        Pattern pattern = Pattern.compile(".*[\u4e00-\u9fa5]+.*$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 是否包含中文 通过汉字占2个字节的特殊性来判断
     *
     * @param str 需判断字符
     * @return true 包含汉字
     */
    public static Boolean isChinese(String str) {
        return str.getBytes().length != str.length();
    }

    /**
     * 检查首字母是否为字母
     *
     * @param string 需判断字符
     * @return true
     */
    public static Boolean checkBeginLetter(String string) {
        String check = "^[a-zA-Z].*";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(string);
        return !matcher.matches();
    }

    /**
     * 检查 只能包括英文字母和数字
     *
     * @param string 需判断字符
     * @return true
     */
    public static Boolean checkOnlyNumABC(String string) {
        String check = "^[a-zA-Z0-9]+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(string);
        return !matcher.matches();
    }

    /**
     * 只允许字母、数字和汉字 还有空格
     *
     * @param str 需判断字符
     * @return true
     */
    public static boolean checkChineseNumberAbc(String str) {
        String regEx = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return !m.matches();
    }

    /**
     * 只允许汉字
     *
     * @param str 需判断字符
     * @return true
     */
    public static boolean checkChinese(String str) {
        String regEx = "^[\u4e00-\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return !m.matches();
    }

    /**
     * 检查 字符窜 长度是否合法
     *
     * @param string 需判断字符
     * @param min    最小字符数
     * @param max    最大字符数
     * @return true
     */
    public static Boolean checkRangelength(String string, int min, int max) {
        int length = string.length();
        return !(length >= min && length <= max);
    }

    /**
     * 检查是否是座机号码
     *
     * @param phoneNumber 需判断字符
     * @return true
     */
    public static Boolean checkPhone(String phoneNumber) {
        String check = "^(0[0-9]{2,3})+(\\-[2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * 判断是否是电话号码
     *
     * @param phoneNumber 需判断字符
     * @return boolean
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";

        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phoneNumber);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(phoneNumber);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 将字符串转位日期类型
     *
     * @param date   需要转换的字符型日期
     * @param format 需要转换的格式
     * @return 日期类型
     */
    public static Date formatDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /***
     * 根据需要设定时间格式化类型，进行指定格式化
     *
     * @param time      时间搓
     * @param formatStr 格式化类型
     * @return 格式化后的时间字符串
     */
    public static String formatDate(long time, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date(time));

    }

    /**
     * 以友好的方式显示时间
     *
     * @param date 需要转换的字符型日期
     * @return 友好的方式显示时间
     */
    public static String formatDate2Friendly(String date) {
        Date time = formatDate(date, "yyyy-MM-dd hh:mm:ss");
        return formatDate2Friendly(time);
    }

    /**
     * 以友好的方式显示时间
     *
     * @param time 需要转换的日期型日期
     * @return 友好的方式显示时间
     */
    public static String formatDate2Friendly(Date time) {
        if (time == null) {
            return "Unknown";
        }
        String fTime = "";
        String minutesAgo = "分钟前";
        String hoursAgo = "小时前";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String curDate = format.format(cal.getTime());
        String paramDate = format.format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                fTime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + minutesAgo;
            } else {
                fTime = hour + hoursAgo;
            }
            return fTime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) {
                fTime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + minutesAgo;
            } else {
                fTime = hour + hoursAgo;
            }
        } else if (days == 1) {
            fTime = "昨天";
        } else if (days == 2) {
            fTime = "前天";
        } else if (days > 2 && days <= 10) {
            fTime = days + "天前";
        } else if (days > 10) {
            fTime = new SimpleDateFormat("MM-dd").format(time);
        }
        return fTime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param date   需要转换的字符型日期
     * @param format 需要转换的格式
     * @return boolean
     */
    public static boolean isToday(String date, String format) {
        boolean b = false;
        Date time = formatDate(date, format);
        Date today = new Date();
        if (time != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = dateFormat.format(today);
            String timeDate = dateFormat.format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input 需判断字符
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input)) {
            return true;
        }

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email 需判断字符
     * @return boolean
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return false;
        }
        Pattern EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        return EMAIL.matcher(email).matches();
    }

    /**
     * 判断是否是 数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /***
     * 判断是否符合车牌格式
     *
     * @param licenceNumber 车牌号码
     * @return boolean
     */
    public static boolean isLicenceNumber(String licenceNumber) {
        if (TextUtils.isEmpty(licenceNumber)) {
            return false;
        }
        Pattern p = Pattern.compile("^[a-z][a-z0-9]{5}$");
        Matcher m = p.matcher(licenceNumber.toLowerCase());
        return m.matches();
    }

    /**
     * 电话号码判断，规则，1开头，后十位为0-9的数字
     *
     * @param phone 手机号码
     * @return boolean
     */
    public static boolean checkMobile(String phone) {
        Pattern p = Pattern.compile("^[1][0-9]{10}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static enum DistanceUnits {
        Meter,
        Kilometer
    }

    /***
     * 两经纬度之间的距离（单位：米）
     *
     * @param lat_a 第一个点的纬度
     * @param lng_a 第一个点的经度
     * @param lat_b 第二个点的纬度
     * @param lng_b 第二个点的经度
     * @param unit  单位 Meter 米， Kilometer 千米
     * @return 距离
     */
    public static double calculateDistance(double lat_a, double lng_a, double lat_b, double lng_b, DistanceUnits unit) {
        double distance;
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        switch (unit) {
            case Meter:
                distance = (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(b / 2), 2)))) * 6370693.5;
                break;
            case Kilometer:
            default:
                distance = (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(radLat1) * Math.cos(radLat2)
                        * Math.pow(Math.sin(b / 2), 2)))) * 6371;
                break;
        }
        return distance;
    }

    /***
     * 两经纬度之间的距离（单位：千米）
     *
     * @param lat_a 第一个点的纬度
     * @param lng_a 第一个点的经度
     * @param lat_b 第二个点的纬度
     * @param lng_b 第二个点的经度
     * @param unit  单位 Meter 米， Kilometer 千米
     * @return 保留两位小数的距离字符串（米 -> 1.22m 千米 -> 0.11km）
     */
    public static String calculateDistanceFormat(double lat_a, double lng_a, double lat_b, double lng_b, DistanceUnits unit) {
        String str;
        double s = calculateDistance(lat_a, lng_a, lat_b, lng_b, unit);
        switch (unit) {
            case Meter:
                str = new DecimalFormat("0.##").format(s) + "m";
                break;
            case Kilometer:
            default:
                str = new DecimalFormat("0.##").format(s) + "km";
                break;
        }
        return str;
    }

    /**
     * 比对时间是否在 规定时间内 start <= date >=end
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param date  当前时间
     * @return boolean
     */
    public static boolean checkDateRange(Date start, Date end, Date date) {
        Calendar c_start = Calendar.getInstance();
        Calendar c_end = Calendar.getInstance();
        Calendar c_date = Calendar.getInstance();
        c_start.setTime(start);
        c_end.setTime(end);
        c_date.setTime(date);
        int s_result = c_start.compareTo(c_date);
        int e_result = c_end.compareTo(c_date);
        return s_result <= 0 && e_result >= 0;
    }

    /***
     * 比较目标时间是否小于当前时间
     *
     * @param time   目标时间
     * @param format 格式化字符串
     * @return true 表示小于当前时间， false 表示不小于当前时间
     */
    public static boolean isLessCurTime(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date cDate = null;
        Date curDate = null;
        try {
            cDate = sdf.parse(time);
            curDate = new Date(System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (curDate != null ? curDate.getTime() : 0) - (cDate != null ? cDate.getTime() : 0) > 0;
    }

    /***
     * 比较目标时间是否小于当前时间
     *
     * @param dp     目标时间
     * @param format 格式化字符串
     * @return true 表示小于当前时间， false 表示不小于当前时间
     */
    public static boolean isLessCurTime(DatePicker dp, String format) {
        String time = dp.getYear() + add0(dp.getMonth() + 1) + add0(dp.getDayOfMonth());
        return isLessCurTime(time, format);
    }

    /***
     * 比较目标时间是否在x天内
     *
     * @param time 目标时间
     * @param day  x天
     * @return true 表示在x天内， false 表示不在x天内
     */
    public static boolean isLessXDay(String time, String format, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date cDate = null;
        Date toDate = null;
        try {
            cDate = sdf.parse(time);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, day);
            toDate = c.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (toDate != null ? toDate.getTime() : 0) - (cDate != null ? cDate.getTime() : 0) > 0;
    }

    /***
     * 获取以x天x小时x分钟格式的倒计时时间
     *
     * @param timeDiff 剩余时间
     * @return x天x小时x分钟格式的倒计时时间
     */
    public static String getLeftTimeFormat(long timeDiff) {
        int second = (int) ((timeDiff / (1000)) % 60);
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
        int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
        String leftTime = "";
        if (days > 0) {
            leftTime += days + "天";
        }
        leftTime += hours + "小时" + minutes + "分钟" + second + "秒";
        return leftTime;
    }

    /***
     * 获取以xx:xx:xx格式的倒计时时间
     *
     * @param timeDiff 剩余时间
     * @return xx:xx:xx格式的倒计时时间
     */
    public static String getLeftTimeFormatHms(long timeDiff) {
        int second = (int) ((timeDiff / (1000)) % 60);
        int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
        int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
        return add0(hours) + ":" + add0(minutes) + ":" + add0(second);
    }

    /***
     * 给小于10的数字前面加0
     *
     * @param time 目标数字
     * @return 字符串
     */
    public static String add0(int time) {
        return time < 10 ? "0" + time : "" + time;
    }

    /**
     * 每4位添加一个空格
     *
     * @param str 需格式化字符
     * @return 格式化后的字符
     */
    public static String format4Spaces(String str) {
        String regex = ".{4}(?!$)";
        return str.replaceAll(" ", "").replaceAll(regex, "$0 ");
    }

    /**
     * 前面4位和后面4位保留数字，其他替换成*
     *
     * @param str 需格式化字符
     * @return 格式化后的字符
     */
    public static String formatHeadAndEnd(String str) {
        String starRegex = "(?<=\\d{4})\\d(?=\\d{4})";
        return str.replaceAll(" ", "").replaceAll(starRegex, "*");
    }

    /**
     * 检查 车架号是否符合规则
     *
     * @param vin 车架号
     * @return boolean
     */
    public static boolean verifyVIN(String vin) {
        vin = vin.trim().toUpperCase();
        char verifyChar = 'A'; //校验位字符
        int weights[] = {8, 7, 6, 5, 4, 3, 2, 10, -1, 9, 8, 7, 6, 5, 4, 3, 2}; //权重矩阵
        int sum = 0; //加权和

        if (vin.length() != 17) {
            Log.e(TAG, vin + ": Incomplete");
            return false;
        }

        for (int i = 0; i < vin.length(); i++) {
            if (i == 8) {
                verifyChar = vin.charAt(i);
                // 如果校验位不是0-9，也不是X，就有问题
                if (verifyChar < '0' && verifyChar > '9' && verifyChar != 'X') {
                    Log.e(TAG, "Something's Wrong with " + vin);
                    return false;
                }
                continue;
            }

            // 查询当前字符的映射值
            int value = charMap(vin.charAt(i));

            // System.out.print(value);

            // 如果返回值为-1，说明有问题
            if (value < 0) {
                Log.e(TAG, "Something's Wrong with " + vin);
                return false;
            }

            // 计算加权和
            sum += weights[i] * value;
        }

        // 与校验位进行校验
        if (sum > 0 && verifyChar != 'A') {
            if (sum % 11 < 10 && (sum % 11 == (verifyChar - '0')) || (sum % 11 == 10 && verifyChar == 'X')) {
                // System.out.println(vin + " good."); // Debug Line
                return true;
            } else {
                Log.e(TAG, vin + ": Wrong!");
                return false;
            }
        }
        return false;
    }

    // 按照规则，对所有字符进行赋值
    public static int charMap(char c) {
        if (c == 'I' || c == 'O' || c == 'Q') {
            return 0; // 不可能出现这3个字母
        } else if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'R') {
            return (c - 'A') % 9 + 1;
        } else if (c >= 'S' && c <= 'Z') {
            return (c - 'S') % 9 + 2;
        }
        return -1; // 这一步不该走到
    }

    /**
     * Compares two {@code long} values.
     *
     * @return 0 if lhs = rhs, less than 0 if lhs &lt; rhs, and greater than 0 if lhs &gt; rhs.
     * @since 1.7
     */
    public static int LongCompare(long lhs, long rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    /**
     * 有小数保留两位小数点，无小数返回整数
     *
     * @param number
     * @return string
     */
    public static String floatTwoStr(float number) {
        return new DecimalFormat("0.##").format(number);
    }

    /**
     * 有小数保留一位小数点，无小数返回整数
     *
     * @param number
     * @return string
     */
    public static String floatOneStr(float number) {
        return new DecimalFormat("0.#").format(number);
    }

    /**
     * 千分撇 保留两位小数
     *
     * @param number
     * @return string
     */
    public static String floatThousandStr(float number) {
        return new DecimalFormat("#,000.00").format(number);
    }

    /**
     * 消除电话号码中 可能含有的 +86、86、-、空格等特殊字符
     *
     * @param telNum
     * @return
     */
    public static String trimTelNum(String telNum) {
        if (telNum == null || "".equals(telNum)) {
            return null;
        }
        telNum = telNum.replace("-", "");
        telNum = telNum.replace(" ", "");
        if (substring(telNum, 0, 2).equals("86"))
            telNum = substring(telNum, 2);
        else if (substring(telNum, 0, 3).equals("+86"))
            telNum = substring(telNum, 3);
        return telNum;
    }

    /**
     * 截取字符串
     *
     * @param s
     * @param from
     * @return
     */
    protected static String substring(String s, int from) {
        try {
            return s.substring(from);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 截取字符串
     *
     * @param s
     * @param from
     * @return
     */
    protected static String substring(String s, int from, int len) {
        try {
            return s.substring(from, from + len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 银行卡四位加空格
     *
     * @param editText
     */
    public static void bankCardNumAddSpace(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;
            private boolean isDelete = false;
            private boolean isClipboard = false;
            private int location = 0;// 记录光标的位置

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = !isChanged;
                if (isChanged) {
                    isDelete = before != 0;
                    isClipboard = count > 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = editText.getSelectionEnd();
                    String spaceStr = StringUtils.format4Spaces(s.toString());
                    editText.setText(spaceStr);
                    int spaceLength = editText.getText().length();
                    if (isClipboard) {
                        Selection.setSelection(editText.getText(), spaceLength);
                    } else {
                        boolean isSpace = location % 5 == 0;
                        if (isSpace) {
                            if (isDelete) {
                                location = location - 1;
                            } else {
                                location = location + 1;
                            }
                        }
                        location = Math.max(Math.min(spaceLength, location), 0);
                        Selection.setSelection(editText.getText(), location);
                    }
                }
            }
        });
    }

    /**
     * 格式化带html的内容
     *
     * @return
     */
    public static Spanned fromHtml(String source) {
        if (isEmpty(source)) return null;
        return Html.fromHtml(source);
    }

    /***
     * 将输入框的内容转换float型数字
     *
     * @param editText 输入框
     * @return float数字
     */
    public static float getTextViewFloat(EditText editText) {
        float number;
        try {
            number = Float.parseFloat(editText.getText().toString());
        } catch (NumberFormatException e) {
            number = 0;
        }
        return number;
    }
}
