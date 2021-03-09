package gui;

import config.GPSConfig;
import model.Itinerary;
import model.Map;
import model.Node;
import process.Dijkstra;
import process.builders.MapBuilder;
import gui.view.MapView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class MainGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(GPSConfig.WINDOW_WIDTH, GPSConfig.WINDOW_HEIGHT);
    private static final Dimension IDEAL_MAPVIEW_DIMENSION = new Dimension(GPSConfig.MAPVIEW_WIDTH, GPSConfig.MAPVIEW_HEIGHT);
    private static final Dimension IDEAL_ITINERARY_PANEL_DIMENSION = new Dimension(GPSConfig.ITINERARY_PANEL_WIDTH, GPSConfig.ITINERARY_PANEL_HEIGHT);

    private static final int MIN_DEC_POS_X = Math.min(GPSConfig.WINDOW_WIDTH - GPSConfig.MAP_SIZE_WIDTH, 0);
    private static final int MIN_DEC_POS_Y = Math.min(GPSConfig.WINDOW_HEIGHT - GPSConfig.MAP_SIZE_HEIGHT, 0);
    private static final int MAX_DEC_POS_X = 0;
    private static final int MAX_DEC_POS_Y = 0;

    private Map map;
    private MapView mapView ;

    private JButton resetButton = new JButton("Reset default position");
    private JButton calculateItinerary = new JButton("Calculate") ;

    private JTextField startNode = new JTextField();
    private JTextField arrivalNode = new JTextField();

    private JPanel testItinerary = new JPanel();
    private JPanel mapPanel = new JPanel();

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

        mapPanel.setLayout(new BorderLayout());
        mapPanel.add(BorderLayout.NORTH, mapView);
        ResetDefaultPosButtonListener resetDefaultPosButtonListener = new ResetDefaultPosButtonListener();
        resetButton.addActionListener(resetDefaultPosButtonListener);
        mapPanel.add(BorderLayout.SOUTH, resetButton);
        contentPane.add(BorderLayout.WEST, mapPanel);

        SpringLayout layout = new SpringLayout();

        testItinerary.setLayout(layout);
        testItinerary.setPreferredSize(IDEAL_ITINERARY_PANEL_DIMENSION);
        testItinerary.setBackground(Color.WHITE);
        testItinerary.setBorder(new EmptyBorder(new Insets(5, 20, 0, 20)));

        startNode.setPreferredSize(new Dimension(210,30));
        startNode.setMaximumSize(startNode.getPreferredSize());
        arrivalNode.setPreferredSize(new Dimension(210,30));
        arrivalNode.setMaximumSize(arrivalNode.getPreferredSize());

        JLabel itineraryLabel = new JLabel("Itinéraire");
        itineraryLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        testItinerary.add(itineraryLabel);

        JLabel startLabel = new JLabel("Saisissez un point de départ :");
        testItinerary.add(startLabel);
        testItinerary.add(Box.createRigidArea(new Dimension(0, 2)));
        testItinerary.add(startNode);

        testItinerary.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel arrivalLabel = new JLabel("Saisissez un point de d'arrivée :");
        testItinerary.add(arrivalLabel);
        testItinerary.add(Box.createRigidArea(new Dimension(0, 2)));
        testItinerary.add(arrivalNode);

        testItinerary.add(Box.createRigidArea(new Dimension(0, 10)));

        calculateItinerary.setPreferredSize(new Dimension(210, 30));
        calculateItinerary.setMaximumSize(calculateItinerary.getPreferredSize());
        calculateItinerary.addActionListener(new CalculateItineraryListener());
        testItinerary.add(calculateItinerary);

        layout.putConstraint(SpringLayout.NORTH, itineraryLabel, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, startLabel, 10, SpringLayout.SOUTH, itineraryLabel);
        layout.putConstraint(SpringLayout.NORTH, startNode, 2, SpringLayout.SOUTH, startLabel);
        layout.putConstraint(SpringLayout.NORTH, arrivalLabel, 10, SpringLayout.SOUTH, startNode);
        layout.putConstraint(SpringLayout.NORTH, arrivalNode, 2, SpringLayout.SOUTH, arrivalLabel);
        layout.putConstraint(SpringLayout.NORTH, calculateItinerary, 10, SpringLayout.SOUTH, arrivalNode);
        contentPane.add(BorderLayout.EAST,testItinerary);

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

            if (dx + mapView.getDecPosX() < MAX_DEC_POS_X && dx + mapView.getDecPosX() > MIN_DEC_POS_X) mapView.setNewDecX(dx + mapView.getDecPosX());
            else if (dx + mapView.getDecPosX() >= MAX_DEC_POS_X) mapView.setNewDecX(MAX_DEC_POS_X);
            else if (dx + mapView.getDecPosX() <= MIN_DEC_POS_X) mapView.setNewDecX(MIN_DEC_POS_X);
            if (dy + mapView.getDecPosY() < MAX_DEC_POS_Y && dy + mapView.getDecPosY() > MIN_DEC_POS_Y) mapView.setNewDecY(dy + mapView.getDecPosY());
            else if (dy + mapView.getDecPosY() >= MAX_DEC_POS_Y) mapView.setNewDecY(MAX_DEC_POS_Y);
            else if (dy + mapView.getDecPosY() <= MIN_DEC_POS_Y) mapView.setNewDecY(MIN_DEC_POS_Y);
            mapView.repaint();
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
                    else JOptionPane.showMessageDialog(mapView,node.toString(), "PAS POI", JOptionPane.ERROR_MESSAGE);
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

    private class CalculateItineraryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String start = startNode.getText() ;
            String arrival = arrivalNode.getText() ;

            //Check
            if(start.isEmpty() || arrival.isEmpty()){
                JOptionPane.showMessageDialog(mapView,"Saisissez tous les champs !","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
            }else if(map.getNodes().containsKey(start) && map.getNodes().containsKey(arrival)){
                Itinerary itinerary = Dijkstra.calculateItinerary(map.getNodes().get(start),map.getNodes().get(arrival),map);
                mapView.setItinerary(itinerary);
                mapView.repaint();
                JOptionPane.showMessageDialog(mapView,itinerary.toString());
            }else{
                JOptionPane.showMessageDialog(mapView,"Destination inconnue","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

}
