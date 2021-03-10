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
import java.util.ArrayList;


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
    private JButton calculateItinerary = new JButton("Rechercher") ;
    private JButton addStep = new JButton("Ajouter une étape");

    private JTextField startNode = new JTextField();
    private JTextField arrivalNode = new JTextField();
    private ArrayList<JTextField> steps ;

    private JLabel startLabel = new JLabel("Saisissez un point de départ :");
    private JLabel arrivalLabel = new JLabel("Saisissez un point de d'arrivée :");

    private JPanel testItinerary = new JPanel();
    private JPanel mapPanel = new JPanel();

    public MainGUI(String title, String mapPath) {
        super(title);

        MapBuilder mapBuilder = new MapBuilder(mapPath);
        map = mapBuilder.buildMap();
        mapView = new MapView(map);

        steps = new ArrayList<>();
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

        testItinerary.add(startLabel);
        testItinerary.add(Box.createRigidArea(new Dimension(0, 2)));
        testItinerary.add(startNode);

        testItinerary.add(Box.createRigidArea(new Dimension(0, 10)));

        testItinerary.add(arrivalLabel);
        testItinerary.add(Box.createRigidArea(new Dimension(0, 2)));
        testItinerary.add(arrivalNode);

        testItinerary.add(Box.createRigidArea(new Dimension(0, 10)));

        calculateItinerary.setPreferredSize(new Dimension(210, 30));
        calculateItinerary.setBackground(new Color(200,200,200));
        calculateItinerary.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        calculateItinerary.setMaximumSize(calculateItinerary.getPreferredSize());
        calculateItinerary.addActionListener(new CalculateItineraryListener());
        testItinerary.add(calculateItinerary);

        addStep.setPreferredSize(new Dimension(210, 30));
        addStep.setBackground(new Color(240,240,240));
        addStep.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        addStep.setMaximumSize(calculateItinerary.getPreferredSize());
        addStep.addActionListener(new AddStepListener());
        testItinerary.add(addStep);

        JLabel copyrightsWallethChevalierLabel = new JLabel("Benjamin Walleth | Paul Chevalier");
        JLabel copyrightsDenoyerLabel = new JLabel("William Denoyer");
        JLabel copyrightsGLPLabel = new JLabel("GLP GPS - 2021");
        testItinerary.add(copyrightsWallethChevalierLabel);
        testItinerary.add(copyrightsDenoyerLabel);
        testItinerary.add(copyrightsGLPLabel);

        layout.putConstraint(SpringLayout.NORTH, itineraryLabel, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, startLabel, 10, SpringLayout.SOUTH, itineraryLabel);
        layout.putConstraint(SpringLayout.NORTH, startNode, 2, SpringLayout.SOUTH, startLabel);
        layout.putConstraint(SpringLayout.NORTH, arrivalLabel, 10, SpringLayout.SOUTH, startNode);
        layout.putConstraint(SpringLayout.NORTH, arrivalNode, 2, SpringLayout.SOUTH, arrivalLabel);
        layout.putConstraint(SpringLayout.NORTH, calculateItinerary, 10, SpringLayout.SOUTH, arrivalNode);
        layout.putConstraint(SpringLayout.NORTH,addStep,10,SpringLayout.SOUTH,calculateItinerary);
        layout.putConstraint(SpringLayout.SOUTH, copyrightsWallethChevalierLabel, -50, SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, copyrightsDenoyerLabel, 2, SpringLayout.SOUTH, copyrightsWallethChevalierLabel);
        layout.putConstraint(SpringLayout.NORTH, copyrightsGLPLabel, 2, SpringLayout.SOUTH, copyrightsDenoyerLabel);
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
                    if(node.isPOI()) {
                        String[] buttons = { "OK", "Départ","Arrivée"};
                        int result = JOptionPane.showOptionDialog(mapView,  node.getPoi().getName() + " | Type : " + node.getPoi().getType().toString(), "Informations sur le lieu",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);

                        if(result == 1){
                            startNode.setText(node.getPoi().getName());
                        }else if(result == 2){
                            arrivalNode.setText(node.getPoi().getName());
                        }

                    }else JOptionPane.showMessageDialog(mapView,node.toString(), "PAS POI", JOptionPane.ERROR_MESSAGE);
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

            //Get all node's name
            ArrayList<String> nodeNames = new ArrayList<>();
            for(Node node : map.getNodes().values()){
                if(node.isPOI()) nodeNames.add(node.getPoi().getName());
            }
            //Check nodes
            if(start.isEmpty() || arrival.isEmpty()){
                JOptionPane.showMessageDialog(mapView,"Saisissez tous les champs !","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
            }else if((map.getNodes().containsKey(start) || nodeNames.contains(start)) && (map.getNodes().containsKey(arrival) || nodeNames.contains(arrival))) {

                Node startingNode, arrivalNode;
                startingNode = map.getNodeFromId(start);
                arrivalNode = map.getNodeFromId(arrival);

                if(startingNode == null) startingNode = map.getNodeFromName(start);
                if(arrivalNode == null) arrivalNode = map.getNodeFromName(arrival);

                Itinerary itinerary = Dijkstra.calculateItinerary(startingNode, arrivalNode, map);
                mapView.setItinerary(itinerary);
                mapView.repaint();
                JOptionPane.showMessageDialog(mapView, itinerary.toString());
            }else{
                JOptionPane.showMessageDialog(mapView,"Destination inconnue","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private class AddStepListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {

            //Variables
            int height = 30 ;
            int marginTop = 10 ;
            int blockHeight = height + marginTop ;
            int numberStep = steps.size() ;

            if (numberStep > 7) {
                return;
            } else if (numberStep == 7) {
                addStep.setVisible(false);
            }

            //Get the layout
            SpringLayout layout = (SpringLayout) testItinerary.getLayout();

            //Add the new component
            JTextField newStep = new JTextField();
            newStep.setPreferredSize(new Dimension(210,height));
            newStep.setMaximumSize(newStep.getPreferredSize());
            testItinerary.add(newStep);
            steps.add(newStep);
            //Add Label if it's first step
            if(numberStep == 0) {
                JLabel jLabel = new JLabel("Etapes intérmédiaires");
                testItinerary.add(jLabel);
                layout.putConstraint(SpringLayout.NORTH,jLabel,10,SpringLayout.SOUTH,startNode);
            }
            layout.putConstraint(SpringLayout.NORTH,newStep,numberStep*blockHeight + marginTop + 20,SpringLayout.SOUTH,startNode);
            layout.putConstraint(SpringLayout.NORTH,arrivalLabel,(numberStep+1)*blockHeight + marginTop + 20,SpringLayout.SOUTH,startNode);

            testItinerary.revalidate();


        }
    }

}
