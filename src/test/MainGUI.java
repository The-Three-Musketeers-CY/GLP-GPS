package test;

import model.Map;
import process.builders.MapBuilder;
import view.MapView;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

public class MainGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800, 400);

    private static final Dimension IDEAL_MAPVIEW_DIMENSION = new Dimension(800, 400);

    private Map map;

    private MapView mapView ;

    private JPanel buttonsPanel = new JPanel();

    public MainGUI(String title, String mapPath) {
        super(title);

        MapBuilder mapBuilder = new MapBuilder(mapPath);
        map = mapBuilder.buildMap();
        mapView = new MapView(map);

        init();
    }

    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        buttonsPanel.setLayout(new GridLayout(5, 1));
        buttonsPanel.setPreferredSize(new Dimension(200, 400));
        // TODO : Set borders with buttonsPanel.setBorder();
        buttonsPanel.add(new JLabel("test"));
        buttonsPanel.add(new JButton("test"));

        contentPane.add(BorderLayout.EAST, buttonsPanel);

        mapView.setPreferredSize(IDEAL_MAPVIEW_DIMENSION);
        contentPane.add(BorderLayout.WEST, mapView);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setPreferredSize(IDEAL_MAIN_DIMENSION);
        setResizable(false);
        mapView.repaint();
    }

    public static void main(String[] args) {
        new MainGUI("Test", "map.xml");
    }

}
