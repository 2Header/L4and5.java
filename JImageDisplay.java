package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Этот класс позволяет отображать фракталы. Наследуется от JComponent
class JImageDisplay extends JComponent
{
    // Управляет изображением, содержимое которого мы можем записать.
    private BufferedImage displayImage;

    //получение изображение из другого класса
    public BufferedImage getImage() {
        return displayImage;
    }

    //Конструктор принимает целочисленные значения ширины и высоты
    // и инициализирует свой объект BufferedImage как новое изображение
    // с этой шириной и высотой типа изображения TYPE_INT_RGB.
    public JImageDisplay(int width, int height) {
        displayImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // вызывает метод родительского класса с заданными параметрами ширины и высоты
        Dimension imageDimension = new Dimension(width, height);
        super.setPreferredSize(imageDimension);

    }

    //границы объета рисовались корректно
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(displayImage, 0, 0, displayImage.getWidth(),
                displayImage.getHeight(), null);
    }
    //красит все в черный
    public void clearImage()
    {
        int[] blankArray = new int[getWidth() * getHeight()];
        displayImage.setRGB(0, 0, getWidth(), getHeight(), blankArray, 0, 1);
    }
    //красит пиксель в спец цвет
    public void drawPixel(int x, int y, int rgbColor)
    {
        displayImage.setRGB(x, y, rgbColor);
    }
}