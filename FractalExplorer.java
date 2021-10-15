package com.company;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class FractalExplorer
{
    private int displaySize;
    private JImageDisplay display;

    // для каждого типа фракталов
    private FractalGenerator fractal;

    /// диапазон
    private Rectangle2D.Double range;

    // конструктор, принимающий размер дисплея
    public FractalExplorer(int size) {
        // хранит размер
        displaySize = size;

        // инициализирует поля
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    // Создание интерфейса, кнопок
    public void createAndShowGUI()
    {
        // создаем тайтл оконного приложения
        display.setLayout(new BorderLayout());
        JFrame myFrame = new JFrame("Fractal Explorer");

        // добавляем изображение в центр
        myFrame.add(display, BorderLayout.CENTER);

        //кнопка сброса
        JButton resetButton = new JButton("Reset");

        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);

        // обработка нажатия мыши
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);

        // закрытие
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComboBox myComboBox = new JComboBox();

        // добовление фракталов в комбобокс
        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);
        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        // добовление фракталов в комбобокс
        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        myFrame.add(myPanel, BorderLayout.NORTH);

        // кнопка сохранения и сброса
        JButton saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        myFrame.add(myBottomPanel, BorderLayout.SOUTH);

        // кнопка сохранения
        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);


        // видимый фрейм и запрет изменения окна
        myFrame.pack();
        myFrame.setVisible(true);
        myFrame.setResizable(false);

    }

    // метод отрисовки и изображения фрактала
    private void drawFractal()
    {
        //проходим через все пиксили на дисплее
        for (int x=0; x<displaySize; x++){
            for (int y=0; y<displaySize; y++){

                //Нахождение соответствующих координатов xCoord и yCoord в области отображения фрактала.
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, x);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, y);

                //Вычислите количество итераций для координат в области отображения фрактала.
                int iteration = fractal.numIterations(xCoord, yCoord);

                //Если число итераций -1 то пиксель черный
                if (iteration == -1){
                    display.drawPixel(x, y, 0);
                }

                else {
                    //В противном случае выберор значения оттенка на основе числа иттераций
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);

                    //Обновление дисплея с цветами каждого пикселя.
                    display.drawPixel(x, y, rgbColor);
                }

            }
        }
        //Когда все пиксели будут отрисованы, перекрасьте JImageDisplay
        //в соответствии с текущим содержимым его изображения.
        display.repaint();
    }
    // внутренний класс для обработки событий
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // дает строку, представляющую команду действия
            String command = e.getActionCommand();

            // если выбран комбо бокс, дать юзеру выбор фрактала
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                //инициализация и отрисовка фрактала
                fractal.getInitialRange(range);
                drawFractal();

            }
            // если выбрана ресет, нарисовать фрактал
            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }
            // если выбрано сохранение, сохраненить текущий фрактал
            else if (command.equals("Save")) {

                // место для хранения места сохранения))
                JFileChooser myFileChooser = new JFileChooser();

                // сохранение в png
                FileFilter extensionFilter =
                        new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);
                // защита от сохранения не пнгшки
                myFileChooser.setAcceptAllFileFilterUsed(false);

                // директория для сохранения
                int userSelection = myFileChooser.showSaveDialog(display);

                // если результатом операции выбора файла является
                // APPROVE_OPTION, продолжить сохранение
                if (userSelection == JFileChooser.APPROVE_OPTION) {

                    // название сохраняемому файлу
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    // попытка сохранить фрактал на диск
                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                        //  FileInputStream in = new FileInputStream("sdasjdasjdsajd.txt");
                    }
                    // поимка исключений и вывод ошибки
                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                // если выбор в диалоговок окне не прошел успешно, вернуть false
                else return;
            }
        }
    }

    //Обработчик событий MouseListener с дисплея
    private class MouseHandler extends MouseAdapter
    {
        //Когда обработчик получает событие щелчка мыши,
        // он отображает пиксельные координаты щелчка в область отображаемого фрактала,
        // а затем вызывает метод recenterAndZoomRange() генератора с координатами,
        // которые были нажаты, и масштабом 0,5.
        @Override
        public void mouseClicked(MouseEvent e)
        {
            // х координата клика
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            // у координата клика
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            // Вызовите метод recenterAndZoomRange() генератора с
            // координатами, которые были нажаты, и шкалой 0,5.
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            // перерисовка фрактала после изменения отображаемой области
            drawFractal();
        }
    }

    //Создаем экземпляр и рисуем фрактал
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }
}
