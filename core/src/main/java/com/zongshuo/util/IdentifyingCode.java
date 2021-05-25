package com.zongshuo.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:32
 * @Description:
 */
public class IdentifyingCode {
    public static enum  CodeType{
        NUMBER(0, "NUMBER"),
        UPPER_CASE_LETTER(1, "UPPER_CASE_LETTER"),
        LOWER_CASE_LATTER(2, "LOWER_CASE_LATTER"),
        UPPER_AND_LOWER_LETTER(3, "UPPER_AND_LOWER_LETTER"),
        NUMBER_AND_UPPER_LETTER(4, "NUMBER_AND_UPPER_LETTER"),
        NUMBER_AND_LOWER_LETTER(5, "NUMBER_AND_LOWER_LETTER"),
        NUMBER_AND_LETTER(6, "NUMBER_AND_LETTER");

        private int key;
        private String value;
        CodeType(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 返回size个scope范围内的随机数
     * 个数和范围应大于0
     * @param size 个数
     * @param scope 范围
     * @return ArrayList
     */
    public static List<Integer> createRandomNum(int size, int scope){
        List<Integer> integers = new ArrayList<>(size);
        if (size <=0 || scope <=0) return integers;

        Random random = new Random();
        for (int i=0 ; i<size ; i++){
            integers.add(random.nextInt(scope));
        }
        return integers;
    }

    /**
     * 获取纯数字验证码
     * @param size  数字个数
     * @param scope 数字范围
     * @return
     */
    public static String getNumberCode(int size, int scope){
        StringBuilder code = new StringBuilder();
        for (Integer integer : createRandomNum(size, scope)){
            code.append(integer);
        }
        return code.toString();
    }

    public static String getCode(int size, CodeType type){
        String code = "";
        if (type == CodeType.NUMBER){
            return getNumberCode(size, 10);
        }
        Random random = new Random();
        List<Integer> integers = createRandomNum(size, 26);
        switch (type) {
            case LOWER_CASE_LATTER:
                code = getLatterCode(integers, 97);
                break;
            case UPPER_CASE_LETTER:
                code = getLatterCode(integers, 65);
                break;
            case UPPER_AND_LOWER_LETTER:
                code = getLatterCode(integers);
                break;
            case NUMBER_AND_LOWER_LETTER:
                code = getNumberAndLatterCode(integers, 97);
                break;
            case NUMBER_AND_UPPER_LETTER:
                code = getNumberAndLatterCode(integers, 65);
                break;
            case NUMBER_AND_LETTER:
                code = getNumberAndLatterCode(integers, 0);
                break;
        }
        return code;
    }

    /**
     * 根据随机数组和类型，返回大写字母或小写字母字符串
     * @param integers 随机数组
     * @param type 类型
     * @return
     */
    private static String getLatterCode(List<Integer> integers, int type){
        StringBuilder builder = new StringBuilder();
        for (Integer integer : integers){
            builder.append((char)(integer.intValue()+type));
        }
        return builder.toString();
    }

    /**
     * 根据随机数组返回大小写字母混合的字符串
     * @param integers 随机数组
     * @return
     */
    private static String getLatterCode(List<Integer> integers){
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (Integer integer : integers){
            builder.append((char)((random.nextInt() & 1) == 1 ? integer.intValue() + 65 : integer.intValue() + 97));
        }
        return builder.toString();
    }

    /**
     * 根据随机数组和类型返回数字和大小写字母组合的字符串
     * 可能出现情况：1、全是数字；2、全是字母；3、二者混合
     * @param integers
     * @param type
     * @return
     */
    private static String getNumberAndLatterCode(List<Integer> integers, int type){
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (Integer integer : integers){
            if ((random.nextInt() & 1) == 1){
                builder.append(integer.intValue()/10);
                continue;
            }
            if (type == 0){
                builder.append((char)((random.nextInt() & 1 ) == 1 ? integer.intValue() + 65 : integer.intValue() + 97));
                continue;
            }
            builder.append((char) integer.intValue() + type);
        }
        return builder.toString();
    }

    public static void getCodeImage(String code, int width, int height, OutputStream stream) throws IOException {
        GenerateImage image = new GenerateImage();
        image.setWidth(width);
        image.setHeight(height);
        image.setLineNum(20);
        image.setFontSize(30);
        image.setFontName("黑体");
        image.generateImage(code, stream);
    }

    public static void main(String[] args) {
        String code = getCode(8, CodeType.NUMBER_AND_LETTER);
        File file = new File("D:\\hehe.jpg");
        try {
            OutputStream stream = new FileOutputStream(file);
            getCodeImage(code, 120, 45, stream);
            stream.flush();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
