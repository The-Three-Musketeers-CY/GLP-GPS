package gui;

import model.Map;
import model.Node;
import process.builders.MapBuilder;
import gui.view.MapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(1000, 600);
    private static final Dimension IDEAL_MAPVIEW_DIMENSION = new Dimension(1000, 600);

    private static final int MIN_DEC_POS_X = -500;
    private static final int MIN_DEC_POS_Y = -500;
    private static final int MAX_DEC_POS_X = 0;
    private static final int MAX_DEC_POS_Y = 0;

    private Map map;
    private MapView mapView ;

    private JButton resetButton = new JButton("Reset default position");

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

        MapDraggedListener mapDraggedListener = new MapDraggedListener();
        mapView.addMouseListener(mapDraggedListener);
        mapView.addMouseMotionListener(mapDraggedListener);
        POIClickListener poiClickListener = new POIClickListener();
        mapView.addMouseListener(poiClickListener);

        mapView.setPreferredSize(IDEAL_MAPVIEW_DIMENSION);
        contentPane.add(BorderLayout.WEST, mapView);

        ResetDefaultPosButtonListener resetDefaultPosButtonListener = new ResetDefaultPosButtonListener();
        resetButton.addActionListener(resetDefaultPosButtonListener);
        contentPane.add(BorderLayout.SOUTH, resetButton);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setPreferredSize(IDEAL_MAIN_DIMENSION);
        setResizable(false);
        mapView.repaint();
    }

    private class MapDraggedListener implements MouseMotionListener, MouseListener {

        private int cursorPosX = 0;
        private int cursorPosY = 0;

        @Override
        public void mouseDragged(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            int dx = e.getX() - cursorPosX;
            int dy = e.getY() - cursorPosY;

            if (dx + mapView.getDecPosX() <= MAX_DEC_POS_X && dx + mapView.getDecPosX() >= MIN_DEC_POS_X) mapView.setNewDecX(dx + mapView.getDecPosX());
            if (dy + mapView.getDecPosY() <= MAX_DEC_POS_Y && dy + mapView.getDecPosY() >= MIN_DEC_POS_Y) mapView.setNewDecY(dy + mapView.getDecPosY());
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            cursorPosX = e.getX();
            cursorPosY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            mapView.setDecPosX(mapView.getNewDecX());
            mapView.setDecPosY(mapView.getNewDecY());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class POIClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            for(Node node : map.getNodes().values()){
                if(node.getPosition().getX() + mapView.getNewDecX() <= x+6 && node.getPosition().getX() + mapView.getNewDecX() >= x-6
                        && node.getPosition().getY() + mapView.getNewDecY() <= y+6 && node.getPosition().getY() + mapView.getNewDecY() >= y-6){
                    if(node.isPOI())
                        JOptionPane.showMessageDialog(mapView, node.getPoi().getName() + " | Type : " + node.getPoi().getType().toString(), "POI Info", JOptionPane.PLAIN_MESSAGE);
                    else JOptionPane.showMessageDialog(mapView,"Ce n'est pas un POI", "PAS POI", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    private class ResetDefaultPosButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            mapView.setDecPosX(0);
            mapView.setDecPosY(0);
            mapView.setNewDecX(0);
            mapView.setNewDecY(0);
            mapView.repaint();
        }

    }

}
