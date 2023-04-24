//Julia Zezula grudzień 2022

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class GUI extends JPanel implements ActionListener, ChangeListener {
    private static final long serialVersionUID = 1L; //universal version identifier for a Serializable class.
    // Deserialization uses this number to ensure that a loaded class corresponds exactly to a serialized object.

    // pole tekstowe do wprowadzania wartości K
    private JTextField TextFieldK;

    // suwak do wybierania wartości N
    private JSlider SliderN;

    // pola etykiet do wyświetlania aktualnych wartości N i K
    private JLabel LabelN;
    private JLabel LabelK;
    private JLabel plotLabel;
    private JCheckBox checkBox;
    private boolean drawCoordinateSystem = false;

    // zmienne przechowujące aktualne wartości N i K
    private int valueOfN = 1;
    private double valueOfK = 1.0;
    double maxY = Double.MIN_VALUE;
    //int x;
    //int y;

    public GUI() {
        // utworzenie pól tekstowych i suwaka
        TextFieldK = new JTextField(10);
        TextFieldK.addActionListener(this);
        SliderN = new JSlider(1, 25, 1);
        SliderN.setPaintTicks(true);
        SliderN.setPaintTrack(true);
        SliderN.setMinorTickSpacing(1); //przedzialy co 1
        SliderN.setMajorTickSpacing(4); //podpisujemy label dla 1,5,itd
        SliderN.setPaintLabels(true);
        SliderN.addChangeListener(this);

        // utworzenie pól etykiet
        LabelN = new JLabel("N = 1");
        LabelK = new JLabel("K = 1.0");

        checkBox = new JCheckBox("siatka");
        checkBox.addActionListener(this);


        plotLabel = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                //call the paintComponent method of the GUI class to draw the plot
                GUI.this.paintComponent(g, g);

            }
        };


        setLayout(new GridBagLayout());
        JPanel northPanel = new JPanel();
        northPanel.add(TextFieldK);
        northPanel.add(LabelK);
        northPanel.add(SliderN);
        northPanel.add(LabelN);
        northPanel.add(checkBox);

        GridBagConstraints LayoutConstraints = new GridBagConstraints();
        LayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        LayoutConstraints.gridx = 0;
        LayoutConstraints.gridy = 0;
        add(northPanel, LayoutConstraints);

        //add the plotLabel to the center of the GUI panel
        LayoutConstraints.fill = GridBagConstraints.BOTH;
        LayoutConstraints.gridx = 0;
        LayoutConstraints.gridy = 1;
        LayoutConstraints.weightx = 1.0;
        LayoutConstraints.weighty = 1.0;
        add(plotLabel, LayoutConstraints);


    }

    public static void main(String[] args) {
        // utworzenie okna z naszym panelem
        JFrame frameSinusPlot = new JFrame("Function Plotter");
        frameSinusPlot.add(new GUI());

        frameSinusPlot.pack();
        frameSinusPlot.setLocationRelativeTo(null);
        frameSinusPlot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameSinusPlot.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // obsługa zdarzenia zmiany wartości K w polu tekstowym
        try {
            valueOfK = NumberFormat.getNumberInstance().parse(TextFieldK.getText()).doubleValue();
            if (valueOfK < 1.0 || valueOfK > 10.0) {
                // jeśli wartość jest niepoprawna ustawiamy domyślną wartość 1.0
                valueOfK = 1.0;
            }
            LabelK.setText("K = " + valueOfK);
            repaint();
        } catch (ParseException ex) {
            // obsługa wyjątku gdy wprowadzona wartość nie jest liczbą zmiennoprzecinkową
            TextFieldK.setText("");
        }

        if (e.getSource() == checkBox) {
            drawCoordinateSystem = !drawCoordinateSystem;
            plotLabel.repaint();
        }

    }


    public void stateChanged(ChangeEvent e) {
        // obsługa zdarzenia zmiany wartości N na suwaku
        valueOfN = SliderN.getValue();
        LabelN.setText("N = " + valueOfN);
        repaint();
    }

    public void paintComponent(Graphics gPlot, Graphics gLabel) {
        super.paintComponent(gPlot);

        // pobranie aktualnych rozmiarów panelu
        int widthPlot = plotLabel.getWidth();
        int heightPlot = plotLabel.getHeight();



        for (int x = 0; x < widthPlot; x++) {
            double y = getY(x);
            if (y > maxY) {
                maxY = y;
            }
        };

        // obliczenie liczby punktów do narysowania
        int numberOfPoints = Math.max(widthPlot, heightPlot);

        // obliczenie kroku dla zmiennej x
        double xStep = 2 * valueOfK * Math.PI / numberOfPoints;

        //ustaw kolor rysowanych elementów
        gPlot.setColor(Color.BLACK);


        // check if maximum y value is greater than height of plotLabel
        if (maxY > heightPlot) {
            // calculate new height of plotLabel based on maximum y value and width of plotLabel
            int newHeight = (int) (heightPlot * (maxY / heightPlot));
            // set new size of plotLabel
            plotLabel.setPreferredSize(new Dimension(widthPlot, newHeight));
            // update variables for width and height of plotLabel
            widthPlot = plotLabel.getWidth();
            heightPlot = plotLabel.getHeight();
            plotLabel.revalidate();
        }

        // rysowanie układu współrzednych
        if (drawCoordinateSystem) {
            gPlot.drawLine(0, heightPlot / 2, widthPlot, heightPlot / 2);
            gPlot.drawLine(widthPlot / 2, 0, widthPlot / 2, heightPlot);
            // calculate label for x-axis
            String xLabel = String.format("%.1fπ", valueOfK * 2);
            gPlot.drawString("-" + xLabel, 0, heightPlot / 2 + 15);
            gPlot.drawString(xLabel, widthPlot - 40, heightPlot / 2 + 15);

            // calculate label for y-axis
            String yLabel = String.format("%.1f", maxY);
            gPlot.drawString("-" + yLabel, widthPlot / 2 - 15, heightPlot - 5);
            gPlot.drawString(yLabel, widthPlot / 2 - 15, 15);
        }

        int previousX = -1;
        int previousY = -1;

        // rysowanie wykresu
        for (int i = 0; i < numberOfPoints; i++) {

            // obliczenie wartości x dla danego punktu
            double x = getX(xStep, i);

            // obliczenie wartości funkcji dla danego punktu
            double y = getY(x);


            // przeliczenie współrzędnych na współrzędne ekranu
            int screenX = getScreenX(widthPlot, x);
            int screenY = getScreenY(heightPlot, y);

            if (previousX != -1){
                int distX = Math.abs(screenX-previousX);
                int distY = Math.abs(screenY-previousY);

                if(distY > 8){
                    int extraPoints = distY/5;
                    for(int j = 1; j < extraPoints; j++){
                    double x2 = getX2(xStep,i, extraPoints, j);
                    double y2 = getY(x2);
                    //gPlot.setColor(Color.RED);
                    gPlot.fillOval(getScreenX(widthPlot,x2), getScreenY(heightPlot,y2), 3, 3);
                    //System.out.println("operating on " + screenX + " " + screenY + " " + getScreenX(widthPlot,x2) + " " + getScreenY(heightPlot,y2));
            }}
            }
            //gPlot.setColor(Color.BLACK);
            // narysowanie punktu
            gPlot.fillOval(screenX, screenY, 3, 3);

            previousX = screenX;
            previousY = screenY;
        }

    }

    private int getScreenY(int heightPlot, double y) {
        return (int) ((y+maxY) * heightPlot / (2* maxY));
    }

    private int getScreenX(int widthPlot, double x) {
        return (int) ((x + valueOfK * Math.PI) * widthPlot / (2 * valueOfK * Math.PI));
    }

    private double getY(double x) {
        double y = 0.0;
        for (int j = 1; j <= valueOfN; j++) {
            y += -2 * Math.pow(-1, j) * Math.sin(j * x) / j;
        }
        return y;
    }

    private double getX(double xStep, int i) {
        return -valueOfK * Math.PI + i * xStep;
    }

    private double getX2(double xStep, int i, int extraPoints, int j) {
        return -valueOfK * Math.PI + (i -1) * xStep + j*xStep/extraPoints;
    }



    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
    }


}
