package com.zongshuo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:40
 * @Description:
 */
public class GenerateImage {
    private Random random = new Random();
    //图片宽度
    private int width = 200;
    //图片高度
    private int height = 80;
    //背景色
    private Color bgColor;
    //边框色
    private Color rectColor;
    //图片格式
    private String formatName = "JPEG";
    //干扰线条数
    private int lineNum = 0;
    //字体名称
    private String fontName = "宋体";
    //字体大小
    private int fontSize = 15;
    //字体颜色
    private Color fontColor = new Color(0, 0, 0);
    //文字旋转弧度
    private double radian = 0;
    private double rotateX = 0;
    private double rotateY = 0;
    //缩放
    private double scale = 1;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public void setRectColor(Color rectColor) {
        this.rectColor = rectColor;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public void setRadian(double radian) {
        this.radian = radian;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
    }

    public void setRotateY(double rotateY) {
        this.rotateY = rotateY;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * 画干扰线
     * @param graphics
     */
    private void drawRandomLine(Graphics graphics){
        if (0 == lineNum) return;
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;
        for (int i=0 ; i<lineNum ; i++){
            startX = random.nextInt(width);
            startY = random.nextInt(height);
            endX = random.nextInt(width);
            endY = random.nextInt(height);
            graphics.setColor(getRandomColor(100, 155));
            graphics.drawLine(startX, startY, endX, endY);
        }
    }

    /**
     * 获取随机颜色
     * @return
     */
    private Color getRandomColor(int base, int range){
        if ((base+range) > 255) range = 255 - base;

        int red = base + random.nextInt(range);
        int green = base + random.nextInt(range);
        int blue = base + random.nextInt(range);

        return new Color(red, green, blue);
    }

    /**
     * 将内容画入画布
     * @param text
     * @param graphics
     */
    private void drawContent(String text, Graphics2D graphics){
        //旋转文字
        graphics.rotate(Math.toRadians(radian), rotateX, rotateY);
        graphics.scale(scale, scale);
        //设置文字属性
        graphics.setColor(fontColor);
        Font font = new Font(fontName, Font.PLAIN, fontSize);
        graphics.setFont(font);
        //获取字符的高宽信息
        FontMetrics metrics = graphics.getFontMetrics(font);
        int charHeight = metrics.getHeight();
        int charWidth = 0;
        //画字符
        int offsetLeft = 0;
        int rowIndex = 1;
        char c;
        for (int i=0 ; i<text.length() ; i++){
            c = text.charAt(i);
            charWidth = metrics.charWidth(c);
            if (Character.isISOControl(c) || offsetLeft >= (width - charWidth)){
                rowIndex++;
                offsetLeft = 0;
            }
            graphics.drawString(String.valueOf(c), offsetLeft, rowIndex*charHeight);
            offsetLeft += charWidth;
        }
    }

    public void generateImage(String text, OutputStream stream) throws IOException {
        //设置图片大小底色及边框
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(bgColor);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(rectColor);
        graphics.drawRect(0, 0, width-1, height-1);

        //画干扰线
        drawRandomLine(graphics);

        //画内容
        drawContent(text, graphics);

        //将图片以设置的格式输出到流
        ImageIO.write(image, formatName, stream);
    }
}
