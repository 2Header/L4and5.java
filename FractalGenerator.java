package com.company;

import java.awt.geom.Rectangle2D;

//Этот класс предоставляет общий интерфейс и операции для генераторов фракталов,
// которые есть в Fractal Explorer
public abstract class FractalGenerator {

    //Этот класс предоставляет общий интерфейс и операции для генераторов фракталов,
    // которые есть в Fractal Explorer
    public static double getCoord(double rangeMin, double rangeMax,
                                  int size, int coord) {

        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }


    // 3адает заданный прямоугольник, содержащий начальный диапазон, подходящий для генерируемого фрактала.
    public abstract void getInitialRange(Rectangle2D.Double range);


    //Обновляет текущий диапазон для центрирования по заданным координатам и увеличения или уменьшения масштаба
    // с заданным коэффициентом масштабирования.
    public void recenterAndZoomRange(Rectangle2D.Double range,
                                     double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }


    //Учитывает координату <em>x< / em> + <em>iy< / em> в комплексной плоскости,
    // вычисляет и возвращает число итераций до того, как фрактальная функция покинет
    // ограничивающую область для этой точки.
    // Точка, которая не убегает до достижения предела итерации, обозначается результатом -1.
    public abstract int numIterations(double x, double y);

}