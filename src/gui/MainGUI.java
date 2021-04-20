package gui;

import config.GPSConfig;
import log.config.LoggerConfig;
import log.LoggerUtility;
import model.*;
import model.identifiers.TransportIdentifier;
import model.repositories.TransportRepository;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import process.Dijkstra;
import process.builders.MapBuilder;
import gui.view.MapView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(GPSConfig.WINDOW_WIDTH, GPSConfig.WINDOW_HEIGHT);
    private static final Dimension IDEAL_MAPVIEW_DIMENSION = new Dimension(GPSConfig.MAPVIEW_WIDTH, GPSConfig.MAPVIEW_HEIGHT);
    private static final Dimension IDEAL_ITINERARY_PANEL_DIMENSION = new Dimension(GPSConfig.ITINERARY_PANEL_WIDTH, GPSConfig.ITINERARY_PANEL_HEIGHT);
    private static final Dimension FIELD_BUTTON_PREFERRED_SIZE_DIMENSION = new Dimension(210, 30);

    private static final int MIN_DEC_POS_X = Math.min(GPSConfig.WINDOW_WIDTH - GPSConfig.MAP_SIZE_WIDTH, 0);
    private static final int MIN_DEC_POS_Y = Math.min(GPSConfig.WINDOW_HEIGHT - GPSConfig.MAP_SIZE_HEIGHT, 0);
    private static final int MAX_DEC_POS_X = 0;
    private static final int MAX_DEC_POS_Y = 0;

    private Map map;
    private MapView mapView;

    private Logger logger = LoggerUtility.getLogger(MainGUI.class, LoggerConfig.LOG_FILE_TYPE);

    private JButton resetButton = new JButton("Reset default position");
    private JButton calculateButton = new JButton("Rechercher");
    private JButton addStepButton = new JButton("Ajouter une étape");

    private JTextField startNodeField = new JTextField();
    private JTextField arrivalNodeField = new JTextField();
    private ArrayList<JTextField> stepNodesField;

    private JPanel autoCompletionPanel = new JPanel();

    private JLabel startLabel = new JLabel("Saisissez un point de départ :");
    private JLabel arrivalLabel = new JLabel("Saisissez un point de d'arrivée :");
    private JLabel stepLabel = new JLabel("Etapes intérmédiaires");
    private JLabel itineraryOptionsLabel = new JLabel("Options d'itinéraire");
    private JLabel transportsOptionsLabel = new JLabel("Modes de transport disponibles");

    private JRadioButton defaultTimeItinerary = new JRadioButton("Le + rapide");
    private JRadioButton distanceItinerary = new JRadioButton("La + courte distance");
    private JRadioButton costItinerary = new JRadioButton("Le - cher");

    private JCheckBox busTransport = new JCheckBox();
    private JCheckBox carTransport = new JCheckBox();
    private JCheckBox footTransport = new JCheckBox();
    private JCheckBox metroTransport = new JCheckBox();
    private JCheckBox trainTransport = new JCheckBox();
    private JCheckBox bikeTransport = new JCheckBox();
    private JCheckBox planeTransport = new JCheckBox();
    private JCheckBox boatTransport = new JCheckBox();

    private JButton allSelected = new JButton("Tous les modes de transports");

    private JPanel itineraryPanel = new JPanel();
    private ItineraryView itineraryView;
    private JPanel mapPanel = new JPanel();

    public MainGUI(String title, String mapPath) {
        super(title);

        // Adding icon to application
        try {
            setIconImage(new ImageIcon(ImageIO.read(new File("src/img/favicon.png"))).getImage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        stepNodesField = new ArrayList<>();

        try {
            // Building map
            MapBuilder mapBuilder = new MapBuilder(mapPath);
            map = mapBuilder.buildMap();

            // Creating mapView with map info
            mapView = new MapView(map);

            // Initializing MainGUI components
            init();
        } catch (IllegalArgumentException | SAXException | ParserConfigurationException | IOException argumentException) {
            JOptionPane.showMessageDialog(this, "Impossible de lancer l'application : une erreur a eue lieu avec la carte !", "Erreur", JOptionPane.ERROR_MESSAGE);
            logger.fatal(argumentException.getMessage());
        }

    }

    private void init() {
        // Setting layout
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Adding map dragging to mapView
        MapDraggedListener mapDraggedListener = new MapDraggedListener();
        mapView.addMouseListener(mapDraggedListener);
        mapView.addMouseMotionListener(mapDraggedListener);

        // Adding POIClick event to mapView
        POIClickListener poiClickListener = new POIClickListener();
        mapView.addMouseListener(poiClickListener);
        mapView.setPreferredSize(IDEAL_MAPVIEW_DIMENSION);

        // Adding mapView & resetButton to mapPanel
        mapPanel.setLayout(new BorderLayout());
        mapPanel.add(BorderLayout.NORTH, mapView);
        ResetDefaultPosButtonListener resetDefaultPosButtonListener = new ResetDefaultPosButtonListener();
        resetButton.addActionListener(resetDefaultPosButtonListener);
        mapPanel.add(BorderLayout.SOUTH, resetButton);
        contentPane.add(BorderLayout.WEST, mapPanel);

        // Creating itinerary panel with a SpringLayout
        SpringLayout layout = new SpringLayout();
        itineraryPanel.setLayout(layout);
        itineraryPanel.setPreferredSize(IDEAL_ITINERARY_PANEL_DIMENSION);
        itineraryPanel.setBackground(Color.WHITE);
        itineraryPanel.setBorder(new EmptyBorder(new Insets(5, 20, 0, 20)));

        AutoCompletionListener autoCompletionListener;

        autoCompletionListener = new AutoCompletionListener(startNodeField);
        startNodeField.setPreferredSize(FIELD_BUTTON_PREFERRED_SIZE_DIMENSION);
        startNodeField.setMaximumSize(startNodeField.getPreferredSize());
        startNodeField.getDocument().addDocumentListener(autoCompletionListener);
        startNodeField.addFocusListener(autoCompletionListener);

        autoCompletionListener = new AutoCompletionListener(arrivalNodeField);
        arrivalNodeField.setPreferredSize(FIELD_BUTTON_PREFERRED_SIZE_DIMENSION);
        arrivalNodeField.setMaximumSize(arrivalNodeField.getPreferredSize());
        arrivalNodeField.getDocument().addDocumentListener(autoCompletionListener);
        arrivalNodeField.addFocusListener(autoCompletionListener);

        JLabel itineraryLabel = new JLabel("Itinéraire");
        itineraryLabel.setFont(new Font("Serif", Font.PLAIN, 20));
        itineraryPanel.add(itineraryLabel);

        itineraryPanel.add(startLabel);
        itineraryPanel.add(startNodeField);
        itineraryPanel.add(arrivalLabel);
        itineraryPanel.add(arrivalNodeField);

        calculateButton.setPreferredSize(FIELD_BUTTON_PREFERRED_SIZE_DIMENSION);
        calculateButton.setBackground(new Color(200,200,200));
        calculateButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        calculateButton.setMaximumSize(calculateButton.getPreferredSize());
        calculateButton.removeMouseListener(calculateButton.getMouseListeners()[0]);
        calculateButton.addMouseListener(new CalculateItineraryListener());
        itineraryPanel.add(calculateButton);

        addStepButton.setPreferredSize(FIELD_BUTTON_PREFERRED_SIZE_DIMENSION);
        addStepButton.setBackground(new Color(240,240,240));
        addStepButton.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        addStepButton.setMaximumSize(calculateButton.getPreferredSize());
        addStepButton.removeMouseListener(addStepButton.getMouseListeners()[0]);
        addStepButton.addMouseListener(new AddStepListener());
        itineraryPanel.add(addStepButton);

        itineraryPanel.add(itineraryOptionsLabel);

        defaultTimeItinerary.setSelected(true);
        itineraryPanel.add(defaultTimeItinerary);
        itineraryPanel.add(distanceItinerary);
        itineraryPanel.add(costItinerary);

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(defaultTimeItinerary);
        radioButtonGroup.add(distanceItinerary);
        radioButtonGroup.add(costItinerary);

        itineraryPanel.add(transportsOptionsLabel);

        itineraryPanel.add(busTransport);
        JLabel busTransportLabel = new JLabel(new ImageIcon("src/img/round_directions_bus_black_24dp.png"));
        itineraryPanel.add(busTransportLabel);
        layout.putConstraint(SpringLayout.WEST,busTransportLabel, 0, SpringLayout.EAST, busTransport);
        layout.putConstraint(SpringLayout.NORTH,busTransportLabel, 0, SpringLayout.NORTH, busTransport);
        busTransport.setSelected(true);

        itineraryPanel.add(carTransport);
        JLabel carTransportLabel = new JLabel(new ImageIcon("src/img/round_directions_car_black_24dp.png"));
        itineraryPanel.add(carTransportLabel);
        layout.putConstraint(SpringLayout.WEST,carTransportLabel, 0, SpringLayout.EAST, carTransport);
        layout.putConstraint(SpringLayout.NORTH,carTransportLabel, 0, SpringLayout.NORTH, carTransport);
        carTransport.setSelected(true);

        itineraryPanel.add(footTransport);
        JLabel footTransportLabel = new JLabel(new ImageIcon("src/img/round_directions_walk_black_24dp.png"));
        itineraryPanel.add(footTransportLabel);
        layout.putConstraint(SpringLayout.WEST,footTransportLabel, 0, SpringLayout.EAST, footTransport);
        layout.putConstraint(SpringLayout.NORTH,footTransportLabel, 0, SpringLayout.NORTH, footTransport);
        footTransport.setSelected(true);
        footTransport.setEnabled(false);

        itineraryPanel.add(metroTransport);
        JLabel metroTransportLabel = new JLabel(new ImageIcon("src/img/round_subway_black_24dp.png"));
        itineraryPanel.add(metroTransportLabel);
        layout.putConstraint(SpringLayout.WEST,metroTransportLabel, 0, SpringLayout.EAST, metroTransport);
        layout.putConstraint(SpringLayout.NORTH,metroTransportLabel, 0, SpringLayout.NORTH, metroTransport);
        metroTransport.setSelected(true);

        itineraryPanel.add(trainTransport);
        JLabel trainTransportLabel = new JLabel(new ImageIcon("src/img/round_train_black_24dp.png"));
        itineraryPanel.add(trainTransportLabel);
        layout.putConstraint(SpringLayout.WEST,trainTransportLabel, 0, SpringLayout.EAST, trainTransport);
        layout.putConstraint(SpringLayout.NORTH,trainTransportLabel, 0, SpringLayout.NORTH, trainTransport);
        trainTransport.setSelected(true);

        itineraryPanel.add(planeTransport);
        JLabel planeTransportLabel = new JLabel(new ImageIcon("src/img/round_flight_black_24dp.png"));
        itineraryPanel.add(planeTransportLabel);
        layout.putConstraint(SpringLayout.WEST,planeTransportLabel, 0, SpringLayout.EAST, planeTransport);
        layout.putConstraint(SpringLayout.NORTH,planeTransportLabel, 0, SpringLayout.NORTH, planeTransport);
        planeTransport.setSelected(true);

        itineraryPanel.add(boatTransport);
        JLabel boatTransportLabel = new JLabel(new ImageIcon("src/img/round_directions_boat_black_24dp.png"));
        itineraryPanel.add(boatTransportLabel);
        layout.putConstraint(SpringLayout.WEST,boatTransportLabel, 0, SpringLayout.EAST, boatTransport);
        layout.putConstraint(SpringLayout.NORTH,boatTransportLabel, 0, SpringLayout.NORTH, boatTransport);
        boatTransport.setSelected(true);

        itineraryPanel.add(bikeTransport);
        JLabel bikeTransportLabel = new JLabel(new ImageIcon("src/img/round_directions_bike_black_24dp.png"));
        itineraryPanel.add(bikeTransportLabel);
        layout.putConstraint(SpringLayout.WEST,bikeTransportLabel, 0, SpringLayout.EAST, bikeTransport);
        layout.putConstraint(SpringLayout.NORTH,bikeTransportLabel, 0, SpringLayout.NORTH, bikeTransport);
        bikeTransport.setSelected(true);

        allSelected.setPreferredSize(FIELD_BUTTON_PREFERRED_SIZE_DIMENSION);
        allSelected.setBackground(new Color(240,240,240));
        allSelected.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        allSelected.setMaximumSize(calculateButton.getPreferredSize());
        allSelected.addActionListener(new ResetSelectionListener());
        itineraryPanel.add(allSelected);

        JLabel copyrightsWallethChevalierLabel = new JLabel("Benjamin Walleth | Paul Chevalier");
        JLabel copyrightsDenoyerLabel = new JLabel("William Denoyer");

        JLabel copyrightsGLPLabel = new JLabel("GLP GPS - 2021");
        itineraryPanel.add(copyrightsWallethChevalierLabel);
        itineraryPanel.add(copyrightsDenoyerLabel);
        itineraryPanel.add(copyrightsGLPLabel);

        autoCompletionPanel.setLayout(new BoxLayout(autoCompletionPanel,BoxLayout.Y_AXIS));

        // Creating all layout constraints
        layout.putConstraint(SpringLayout.NORTH, itineraryLabel, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, startLabel, 10, SpringLayout.SOUTH, itineraryLabel);
        layout.putConstraint(SpringLayout.NORTH, startNodeField, 2, SpringLayout.SOUTH, startLabel);
        layout.putConstraint(SpringLayout.NORTH, arrivalLabel, 10, SpringLayout.SOUTH, startNodeField);
        layout.putConstraint(SpringLayout.NORTH, arrivalNodeField, 2, SpringLayout.SOUTH, arrivalLabel);
        layout.putConstraint(SpringLayout.NORTH, calculateButton, 10, SpringLayout.SOUTH, arrivalNodeField);
        layout.putConstraint(SpringLayout.NORTH, addStepButton,10, SpringLayout.SOUTH, calculateButton);

        layout.putConstraint(SpringLayout.NORTH, itineraryOptionsLabel, 20, SpringLayout.SOUTH, addStepButton);

        layout.putConstraint(SpringLayout.NORTH, defaultTimeItinerary, 10, SpringLayout.SOUTH, itineraryOptionsLabel);
        layout.putConstraint(SpringLayout.NORTH, distanceItinerary, 5, SpringLayout.SOUTH, defaultTimeItinerary);
        layout.putConstraint(SpringLayout.NORTH, costItinerary, 5, SpringLayout.SOUTH, distanceItinerary);

        layout.putConstraint(SpringLayout.NORTH, transportsOptionsLabel, 10, SpringLayout.SOUTH, costItinerary);

        layout.putConstraint(SpringLayout.NORTH, footTransport, 10, SpringLayout.SOUTH, transportsOptionsLabel);
        layout.putConstraint(SpringLayout.NORTH, carTransport, 10, SpringLayout.SOUTH, footTransport);
        layout.putConstraint(SpringLayout.NORTH, busTransport, 10, SpringLayout.SOUTH, carTransport);
        layout.putConstraint(SpringLayout.NORTH, metroTransport, 10, SpringLayout.SOUTH, busTransport);
        layout.putConstraint(SpringLayout.EAST, footTransport, 60, SpringLayout.WEST, costItinerary);
        layout.putConstraint(SpringLayout.EAST, carTransport, 60, SpringLayout.WEST, costItinerary);
        layout.putConstraint(SpringLayout.EAST, busTransport, 60, SpringLayout.WEST, costItinerary);
        layout.putConstraint(SpringLayout.EAST, metroTransport, 60, SpringLayout.WEST, costItinerary);
        layout.putConstraint(SpringLayout.EAST, trainTransport, 100, SpringLayout.WEST, footTransport);
        layout.putConstraint(SpringLayout.EAST, bikeTransport, 100, SpringLayout.WEST, footTransport);
        layout.putConstraint(SpringLayout.EAST, planeTransport, 100, SpringLayout.WEST, footTransport);
        layout.putConstraint(SpringLayout.EAST, boatTransport, 100, SpringLayout.WEST, footTransport);
        layout.putConstraint(SpringLayout.NORTH, trainTransport, 10, SpringLayout.SOUTH, transportsOptionsLabel);
        layout.putConstraint(SpringLayout.NORTH, bikeTransport, 10, SpringLayout.SOUTH, trainTransport);
        layout.putConstraint(SpringLayout.NORTH, planeTransport, 10, SpringLayout.SOUTH, bikeTransport);
        layout.putConstraint(SpringLayout.NORTH, boatTransport, 10, SpringLayout.SOUTH, planeTransport);
        layout.putConstraint(SpringLayout.NORTH, allSelected, 10, SpringLayout.SOUTH, boatTransport);

        layout.putConstraint(SpringLayout.SOUTH, copyrightsWallethChevalierLabel, -50, SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, copyrightsDenoyerLabel, 2, SpringLayout.SOUTH, copyrightsWallethChevalierLabel);
        layout.putConstraint(SpringLayout.NORTH, copyrightsGLPLabel, 2, SpringLayout.SOUTH, copyrightsDenoyerLabel);


        // Adding itineraryPanel at EAST of the frame
        contentPane.add(BorderLayout.EAST, itineraryPanel);

        // Creating frame
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

            for (Node node : map.getNodes().values()) {
                if (node.getPosition().getX() + mapView.getNewDecX() <= x+6 && node.getPosition().getX() + mapView.getNewDecX() >= x-6
                        && node.getPosition().getY() + mapView.getNewDecY() <= y+6 && node.getPosition().getY() + mapView.getNewDecY() >= y-6) {
                    if (node.isPOI()) {
                        String[] buttons = { "OK", "Départ","Arrivée"};
                        int result = JOptionPane.showOptionDialog(mapView,  node.getPoi().getName() + " | Type : " + node.getPoi().getType().toString(), "Informations sur le lieu",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);

                        if (result == 1) {
                            startNodeField.setText(node.getPoi().getName());
                        } else if(result == 2) {
                            arrivalNodeField.setText(node.getPoi().getName());
                        }

                    }
                    // else JOptionPane.showMessageDialog(mapView,node.toString(), "PAS POI", JOptionPane.ERROR_MESSAGE);>
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

    private class AutoCompletionListener implements DocumentListener, FocusListener{

        private JTextField component;

        public AutoCompletionListener(JTextField component){
            this.component = component ;
            autoCompletionPanel.setVisible(false);
            itineraryPanel.add(autoCompletionPanel);
            itineraryPanel.setComponentZOrder(autoCompletionPanel, 0);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            autoComplete();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            autoComplete();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // Do nothing...
        }

        public void autoComplete(){

            ArrayList<String> nodeNames = map.getAllNodeNames();
            String currentText = component.getText();

            // Get the layout
            SpringLayout layout = (SpringLayout) itineraryPanel.getLayout();
            //Remove all old components
            for(Component component : autoCompletionPanel.getComponents()){
                autoCompletionPanel.remove(component);
            }
            // Set the panel constraints
            layout.putConstraint(SpringLayout.NORTH, autoCompletionPanel,0,SpringLayout.SOUTH,component);

            // Show the panel
            autoCompletionPanel.setVisible(!component.getText().isBlank());

            for(String nodeName : nodeNames) {
                if (nodeName.equals(currentText)) {
                    autoCompletionPanel.setVisible(false);
                } else if (Pattern.matches("(?i)"+currentText+".*",nodeName)) {
                    JLabel nodeNameLabel = new JLabel(nodeName);
                    nodeNameLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            super.mouseClicked(e);
                            component.setText(nodeName);
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            super.mouseEntered(e);
                            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        }
                    });
                    autoCompletionPanel.add(nodeNameLabel);
                }
                itineraryPanel.updateUI();
            }

        }

        @Override
        public void focusGained(FocusEvent e) {
            autoComplete();
        }

        @Override
        public void focusLost(FocusEvent e) {
            autoCompletionPanel.setVisible(false);
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

    private class CalculateItineraryListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            String start = startNodeField.getText() ;
            String arrival = arrivalNodeField.getText() ;
            ArrayList<String> stepsString = new ArrayList<>() ;

            //Get all node's name
            ArrayList<String> nodeNames = map.getAllNodeNames();

            try{
                //Check start and arrival nodes
                if (start.isEmpty() || arrival.isEmpty()){
                    throw new IllegalArgumentException("Départ ou arrivée non saisie !");
                }

                //Get all step's name and check it
                for (JTextField jTextField : stepNodesField){
                    if (jTextField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(mapView,"Aucune étape saisie","Recherche d'itinéraire",JOptionPane.WARNING_MESSAGE);
                    } else {
                        stepsString.add(jTextField.getText());
                    }
                }

                for(String steps : stepsString){
                    if(!nodeNames.contains(steps) && !map.getNodes().containsKey(steps)){
                        throw new IllegalArgumentException("Etape inconnue. Vérifiez votre saisie.");
                    }
                }

                if ((map.getNodes().containsKey(start) || nodeNames.contains(start)) && (map.getNodes().containsKey(arrival) || nodeNames.contains(arrival))) {

                    Node startingNode, arrivalNode;
                    startingNode = map.getNodeFromId(start);
                    arrivalNode = map.getNodeFromId(arrival);


                    if (startingNode == null) startingNode = map.getNodeFromName(start);
                    if (arrivalNode == null) arrivalNode = map.getNodeFromName(arrival);

                    ArrayList<Node> nodes = new ArrayList<>();
                    nodes.add(startingNode);
                    for (String stepNodeField : stepsString) {
                        Node stepNode;
                        stepNode = map.getNodeFromId(stepNodeField);
                        if (stepNode == null) stepNode = map.getNodeFromName(stepNodeField);
                        nodes.add(stepNode);
                    }
                    nodes.add(arrivalNode);


                    ArrayList<Transport> transportsToAvoid = new ArrayList<>();
                    if(!busTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.BUS));
                    }
                    if(!carTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.CAR));
                    }
                    if(!metroTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.METRO));
                    }
                    if(!trainTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.TRAIN));
                    }
                    if(!boatTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.BOAT));
                    }
                    if(!planeTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.PLANE));
                    }
                    if(!bikeTransport.isSelected()){
                        transportsToAvoid.add(TransportRepository.getInstance().getTransports().get(TransportIdentifier.BICYCLE));
                    }

                    Itinerary itinerary;

                    if (defaultTimeItinerary.isSelected()) {
                        itinerary = Dijkstra.calculateItinerary(nodes, map,transportsToAvoid, Dijkstra.DEFAULT_BY_TIME);
                    } else if (distanceItinerary.isSelected()) {
                        itinerary = Dijkstra.calculateItinerary(nodes, map,transportsToAvoid, Dijkstra.BY_DISTANCE);
                    } else if (costItinerary.isSelected()) {
                        itinerary = Dijkstra.calculateItinerary(nodes, map,transportsToAvoid, Dijkstra.BY_COST);
                    } else {
                        itinerary = Dijkstra.calculateItinerary(nodes, map,transportsToAvoid, Dijkstra.DEFAULT_BY_TIME);
                    }

                    mapView.setItinerary(itinerary);
                    mapView.repaint();

                    itineraryView = new ItineraryView(itinerary);
                    itineraryView.setBackground(Color.WHITE);
                    itineraryView.setBorder(new EmptyBorder(new Insets(5, 20, 0, 20)));
                    getContentPane().add(BorderLayout.EAST, itineraryView);
                    itineraryPanel.setVisible(false);
                    revalidate();

                } else {
                    throw new IllegalArgumentException("Destination inconnue. Vérifiez votre itinéraire.");
                }

            }catch (IllegalArgumentException argumentException){
                logger.error(argumentException.getMessage());
                JOptionPane.showMessageDialog(mapView,argumentException.getMessage(),"Recherche d'itinéraire",JOptionPane.ERROR_MESSAGE);
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
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private class AddStepListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {

            //Variables
            int height = 30;
            int marginTop = 10;
            int blockHeight = height + marginTop;
            int numberStep = stepNodesField.size();
            SpringLayout layout = (SpringLayout) itineraryPanel.getLayout();

            //Add a limit : max 3 steps
            if (numberStep >= GPSConfig.MAX_STEP_ITINERARY) {
                return;
            } else if (numberStep == (GPSConfig.MAX_STEP_ITINERARY - 1)) {
                addStepButton.setVisible(false);
                layout.putConstraint(SpringLayout.NORTH, itineraryOptionsLabel, 20, SpringLayout.SOUTH, calculateButton);
            }

            //Add the new component
            JTextField newStep = new JTextField();
            newStep.setPreferredSize(new Dimension(175,height));
            newStep.setMaximumSize(newStep.getPreferredSize());
            AutoCompletionListener newListener = new AutoCompletionListener(newStep);
            newStep.getDocument().addDocumentListener(newListener);
            newStep.addFocusListener(newListener);

            JButton rmStep = new JButton("X");
            rmStep.setPreferredSize(new Dimension(30, 30));
            rmStep.setBackground(new Color(240,240,240));
            rmStep.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            rmStep.setMaximumSize(calculateButton.getPreferredSize());
            rmStep.addActionListener(new RemoveStepListener(newStep));

            itineraryPanel.add(newStep);
            itineraryPanel.add(rmStep);

            //Add the new text field to the steps list
            stepNodesField.add(newStep);

            //Add Label if it's first step
            if(numberStep == 0) {
                itineraryPanel.add(stepLabel);
                layout.putConstraint(SpringLayout.NORTH,stepLabel,10,SpringLayout.SOUTH, startNodeField);
            }
            layout.putConstraint(SpringLayout.NORTH, newStep,numberStep*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNodeField);
            layout.putConstraint(SpringLayout.NORTH, rmStep, numberStep*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNodeField);
            layout.putConstraint(SpringLayout.WEST, rmStep, 5, SpringLayout.EAST, newStep);
            layout.putConstraint(SpringLayout.NORTH, arrivalLabel,(numberStep+1)*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNodeField);

            itineraryPanel.updateUI();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private class RemoveStepListener implements ActionListener {

        private JTextField stepField;

        public RemoveStepListener(JTextField stepField) {
            this.stepField = stepField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //Variables
            int height = 30;
            int marginTop = 10;
            int blockHeight = height + marginTop;
            int numberStep = stepNodesField.size();
            SpringLayout layout = (SpringLayout) itineraryPanel.getLayout();

            stepNodesField.remove(stepField);
            itineraryPanel.remove(stepField);
            itineraryPanel.remove((JButton) e.getSource());

            if ((numberStep - 1) == 0) {
                itineraryPanel.remove(stepLabel);
                layout.putConstraint(SpringLayout.NORTH, arrivalLabel, marginTop, SpringLayout.SOUTH, startNodeField);
            } else {
                for (JTextField stepF : stepNodesField) {
                    layout.putConstraint(SpringLayout.NORTH, stepF, stepNodesField.indexOf(stepF)*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNodeField);
                    layout.putConstraint(SpringLayout.NORTH, itineraryPanel.findComponentAt(205, stepF.getY()), stepNodesField.indexOf(stepF)*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNodeField);
                }
                layout.putConstraint(SpringLayout.NORTH, arrivalLabel, (numberStep - 1) * blockHeight + marginTop + 20, SpringLayout.SOUTH, startNodeField);
            }

            if (numberStep == GPSConfig.MAX_STEP_ITINERARY) {
                addStepButton.setVisible(true);
                layout.putConstraint(SpringLayout.NORTH, itineraryOptionsLabel, 20, SpringLayout.SOUTH, addStepButton);
            }

            itineraryPanel.updateUI();
        }

    }

    private class ItineraryView extends JPanel {

        private Itinerary itinerary;

        public ItineraryView(Itinerary itinerary) {
            this.itinerary = itinerary;

            init();
        }

        private void init() {
            SpringLayout itineraryLayout = new SpringLayout();
            this.setLayout(itineraryLayout);
            JLabel timeDistance = new JLabel();
            JLabel cost = new JLabel();
            JLabel itineraryStart = new JLabel();
            JLabel itineraryFinal = new JLabel();
            JButton button = new JButton("Nouvel itinéraire");

            JLabel previousLabel = itineraryStart;
            int cpt = 0;
            for(StepItinerary stepItinerary : itinerary.getStepItineraries()){
                if(cpt != 0 && cpt < itinerary.getStepItineraries().size()){
                    JLabel stepLabel = new JLabel();
                    stepLabel.setText(stepItinerary.getStepItineraryNodes()[0].getPoi().getName());
                    stepLabel.setIcon(new ImageIcon("src/img/round_place_black_24DP.png"));
                    this.add(stepLabel);
                    itineraryLayout.putConstraint(SpringLayout.NORTH, stepLabel, 15, SpringLayout.SOUTH, previousLabel);
                    previousLabel = stepLabel;
                }
                for(int i=0; i<stepItinerary.getStepItineraryNodes().length-1;i++){
                    Node node = stepItinerary.getStepItineraryNodes()[i];
                    Transport transport = stepItinerary.getTransportsUsed()[i];
                    if(node.isPOI()){
                        POI poi = node.getPoi();
                        JLabel namePoi = new JLabel();
                        namePoi.setText(poi.getName());
                        if(i<stepItinerary.getStepItineraryNodes().length-1 && transport != stepItinerary.getTransportsUsed()[i+1]){
                            Transport nextTransport = stepItinerary.getTransportsUsed()[i+1];
                            String iconPath;
                            switch (nextTransport.getIdentifier()){
                                case BUS :
                                    iconPath = "src/img/round_directions_bus_black_24dp.png";
                                    break;
                                case CAR:
                                    iconPath = "src/img/round_directions_car_black_24dp.png";
                                    break;
                                case BICYCLE:
                                    iconPath = "src/img/round_directions_bike_black_24dp.png";
                                    break;
                                case FOOT:
                                    iconPath = "src/img/round_directions_walk_black_24dp.png";
                                    break;
                                case BOAT:
                                    iconPath = "src/img/round_directions_boat_black_24dp.png";
                                    break;
                                case METRO:
                                    iconPath = "src/img/round_subway_black_24dp.png";
                                    break;
                                case TRAIN:
                                    iconPath = "src/img/round_train_black_24dp.png";
                                    break;
                                case PLANE:
                                    iconPath = "src/img/round_flight_black_24dp.png";
                                    break;
                                default:
                                    iconPath = null;
                            }
                            namePoi.setIcon(new ImageIcon(iconPath));
                            itineraryLayout.putConstraint(SpringLayout.WEST, namePoi, 25, SpringLayout.WEST, itineraryStart);
                        }
                        else {
                            namePoi.setIcon(new ImageIcon("src/img/dot.png"));
                            itineraryLayout.putConstraint(SpringLayout.WEST, namePoi, 45, SpringLayout.WEST, itineraryStart);
                        }
                        this.add(namePoi);
                        itineraryLayout.putConstraint(SpringLayout.NORTH, namePoi, 15, SpringLayout.SOUTH, previousLabel);
                        previousLabel = namePoi;
                    }
                }
                cpt++;
            }

            ImageIcon icon = new ImageIcon("src/img/placeholder.png");
            itineraryStart.setIcon(icon);
            itineraryFinal.setIcon(icon);
            itineraryStart.setText(itinerary.getStepItineraries().get(0).getStepItineraryNodes()[0].getPoi().getName());
            itineraryFinal.setText(itinerary.getStepItineraries().get(itinerary.getStepItineraries().size()-1).getStepItineraryNodes()[itinerary.getStepItineraries().get(itinerary.getStepItineraries().size()-1).getStepItineraryNodes().length-1].getPoi().getName());

            //Distance display
            String distanceTxt = " m";
            float distanceValue = (float) (Math.ceil(itinerary.getDistance()));
            String format = "%.0f" ;

            if(Math.ceil(itinerary.getDistance()) > 999){
                distanceTxt = " km" ;
                distanceValue = (float) (Math.ceil(itinerary.getDistance())/1000) ;
                format = "%.1f" ;
            }

            int timeValue = (int) Math.ceil(itinerary.getTime());
            int hourValue = 0 ;
            while (timeValue >= 60){
                hourValue++ ;
                timeValue-= 60 ;
            }
            String timeTxt;
            if(hourValue == 0){
                timeTxt = timeValue + " min de trajet -- ";
            }else{
                timeTxt = hourValue + " h " + timeValue%60 + " min de trajet -- ";
            }

            timeDistance.setText(timeTxt + String.format(format,distanceValue) + distanceTxt);
            cost.setText(String.format("%.2f",itinerary.getCost()) + " €");

            button.addActionListener(new NewItineraryListener());
            button.setPreferredSize(FIELD_BUTTON_PREFERRED_SIZE_DIMENSION);
            button.setBackground(new Color(200,200,200));
            button.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            });

            this.add(itineraryStart);
            this.add(itineraryFinal);
            this.add(timeDistance);
            this.add(cost);
            this.add(button);

            itineraryLayout.putConstraint(SpringLayout.NORTH, itineraryStart, 15, SpringLayout.SOUTH, cost);
            itineraryLayout.putConstraint(SpringLayout.NORTH, itineraryFinal, 15, SpringLayout.SOUTH, previousLabel);
            itineraryLayout.putConstraint(SpringLayout.NORTH, timeDistance, 15,SpringLayout.NORTH, this);
            itineraryLayout.putConstraint(SpringLayout.NORTH, cost, 15, SpringLayout.SOUTH, timeDistance);
            itineraryLayout.putConstraint(SpringLayout.SOUTH, button, -10, SpringLayout.SOUTH, this);
            itineraryLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, button, 0, SpringLayout.HORIZONTAL_CENTER, this);
            this.setPreferredSize(IDEAL_ITINERARY_PANEL_DIMENSION);
        }

    }

    private class ResetSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            busTransport.setSelected(true);
            carTransport.setSelected(true);
            metroTransport.setSelected(true);
            trainTransport.setSelected(true);
            planeTransport.setSelected(true);
            boatTransport.setSelected(true);
            bikeTransport.setSelected(true);
        }
    }

    private class NewItineraryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().remove(itineraryView);
            itineraryPanel.setVisible(true);
            mapView.setItinerary(null);
            mapView.updateUI();
            revalidate();
        }

    }

}
