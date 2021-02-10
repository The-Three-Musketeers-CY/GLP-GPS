package test;

import model.Map;
import process.builders.MapBuilder;

import javax.swing.*;
import java.awt.*;

public class MapGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800, 400);

    private MapBuilder mapBuilder;
    private Map map;

    public MapGUI(String title, String mapPath) {
        super(title);

        mapBuilder = new MapBuilder(mapPath);
        map = mapBuilder.buildMap();

        init();
    }

    private void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setPreferredSize(IDEAL_MAIN_DIMENSION);
        setResizable(false);
    }

    public static void main(String[] args) {
        new MapGUI("Test", "map.xml");
    }

}
