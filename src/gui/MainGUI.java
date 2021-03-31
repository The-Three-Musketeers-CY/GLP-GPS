package gui;

import config.GPSConfig;
import model.*;
import model.identifiers.TransportIdentifier;
import model.repositories.TransportRepository;
import process.Dijkstra;
import process.builders.MapBuilder;
import gui.view.MapView;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;


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
    private MapView mapView;

    private JButton resetButton = new JButton("Reset default position");
    private JButton calculateItinerary = new JButton("Rechercher");
    private JButton addStep = new JButton("Ajouter une étape");

    private JTextField startNode = new JTextField();
    private JTextField arrivalNode = new JTextField();
    private ArrayList<JTextField> steps;

    private JPanel autoCompletePanel = new JPanel();

    private JLabel startLabel = new JLabel("Saisissez un point de départ :");
    private JLabel arrivalLabel = new JLabel("Saisissez un point de d'arrivée :");
    private JLabel stepLabel = new JLabel("Etapes intérmédiaires");

    private JRadioButton defaultTimeItinerary = new JRadioButton("Le + court");
    private JRadioButton distanceItinerary = new JRadioButton("La - courte distance");
    private JRadioButton costItinerary = new JRadioButton("Le - cher");

    private JPanel testItinerary = new JPanel();
    private ItineraryView itineraryView;
    private JPanel mapPanel = new JPanel();

    public MainGUI(String title, String mapPath) {
        super(title);

        try {
            setIconImage(new ImageIcon(ImageIO.read(new File("favicon.png"))).getImage());
        } catch (IOException e) {
            // TODO : Gérer cette exception
        }

        steps = new ArrayList<>();

        // Building map
        MapBuilder mapBuilder = new MapBuilder(mapPath);
        map = mapBuilder.buildMap();

        // Creating mapView with map info
        mapView = new MapView(map);

        // Initializing MainGUI components
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

        AutoCompletion newListener = new AutoCompletion(startNode);
        startNode.setPreferredSize(new Dimension(210,30));
        startNode.setMaximumSize(startNode.getPreferredSize());
        startNode.getDocument().addDocumentListener(newListener);
        startNode.addFocusListener(newListener);
        arrivalNode.setPreferredSize(new Dimension(210,30));
        arrivalNode.setMaximumSize(arrivalNode.getPreferredSize());
        newListener = new AutoCompletion(arrivalNode);
        arrivalNode.getDocument().addDocumentListener(newListener);
        arrivalNode.addFocusListener(newListener);

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
        calculateItinerary.removeMouseListener(calculateItinerary.getMouseListeners()[0]);
        calculateItinerary.addMouseListener(new CalculateItineraryListener());
        testItinerary.add(calculateItinerary);

        addStep.setPreferredSize(new Dimension(210, 30));
        addStep.setBackground(new Color(240,240,240));
        addStep.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        addStep.setMaximumSize(calculateItinerary.getPreferredSize());
        addStep.removeMouseListener(addStep.getMouseListeners()[0]);
        addStep.addMouseListener(new AddStepListener());
        testItinerary.add(addStep);

        defaultTimeItinerary.setSelected(true);
        testItinerary.add(defaultTimeItinerary);
        testItinerary.add(distanceItinerary);
        testItinerary.add(costItinerary);

        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(defaultTimeItinerary);
        radioButtonGroup.add(distanceItinerary);
        radioButtonGroup.add(costItinerary);

        JLabel copyrightsWallethChevalierLabel = new JLabel("Benjamin Walleth | Paul Chevalier");
        JLabel copyrightsDenoyerLabel = new JLabel("William Denoyer");
        JLabel copyrightsGLPLabel = new JLabel("GLP GPS - 2021");
        testItinerary.add(copyrightsWallethChevalierLabel);
        testItinerary.add(copyrightsDenoyerLabel);
        testItinerary.add(copyrightsGLPLabel);

        autoCompletePanel.setLayout(new BoxLayout(autoCompletePanel,BoxLayout.Y_AXIS));

        // Creating all layout constraints
        layout.putConstraint(SpringLayout.NORTH, itineraryLabel, 5, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, startLabel, 10, SpringLayout.SOUTH, itineraryLabel);
        layout.putConstraint(SpringLayout.NORTH, startNode, 2, SpringLayout.SOUTH, startLabel);
        layout.putConstraint(SpringLayout.NORTH, arrivalLabel, 10, SpringLayout.SOUTH, startNode);
        layout.putConstraint(SpringLayout.NORTH, arrivalNode, 2, SpringLayout.SOUTH, arrivalLabel);
        layout.putConstraint(SpringLayout.NORTH, calculateItinerary, 10, SpringLayout.SOUTH, arrivalNode);
        layout.putConstraint(SpringLayout.NORTH, addStep,10, SpringLayout.SOUTH,calculateItinerary);
        layout.putConstraint(SpringLayout.NORTH, defaultTimeItinerary, 10, SpringLayout.SOUTH, addStep);
        layout.putConstraint(SpringLayout.NORTH, distanceItinerary, 5, SpringLayout.SOUTH, defaultTimeItinerary);
        layout.putConstraint(SpringLayout.NORTH, costItinerary, 5, SpringLayout.SOUTH, distanceItinerary);
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

            for (Node node : map.getNodes().values()) {
                if (node.getPosition().getX() + mapView.getNewDecX() <= x+6 && node.getPosition().getX() + mapView.getNewDecX() >= x-6
                        && node.getPosition().getY() + mapView.getNewDecY() <= y+6 && node.getPosition().getY() + mapView.getNewDecY() >= y-6) {
                    if (node.isPOI()) {
                        String[] buttons = { "OK", "Départ","Arrivée"};
                        int result = JOptionPane.showOptionDialog(mapView,  node.getPoi().getName() + " | Type : " + node.getPoi().getType().toString(), "Informations sur le lieu",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);

                        if (result == 1) {
                            startNode.setText(node.getPoi().getName());
                        } else if(result == 2) {
                            arrivalNode.setText(node.getPoi().getName());
                        }

                    } else JOptionPane.showMessageDialog(mapView,node.toString(), "PAS POI", JOptionPane.ERROR_MESSAGE);
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

    private class AutoCompletion implements DocumentListener, FocusListener{

        private JTextField component;

        public AutoCompletion(JTextField component){
            this.component = component ;
            autoCompletePanel.setVisible(false);
            testItinerary.add(autoCompletePanel);
            testItinerary.setComponentZOrder(autoCompletePanel, 0);
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
            SpringLayout layout = (SpringLayout) testItinerary.getLayout();
            //Remove all old components
            for(Component component : autoCompletePanel.getComponents()){
                autoCompletePanel.remove(component);
            }
            // Set the panel constraints
            layout.putConstraint(SpringLayout.NORTH,autoCompletePanel,0,SpringLayout.SOUTH,component);

            // Show the panel
            if (component.getText().isBlank()) {
                autoCompletePanel.setVisible(false);
            } else {
                autoCompletePanel.setVisible(true);
            }

            for(String nodeName : nodeNames) {
                if (nodeName.equals(currentText)) {
                    autoCompletePanel.setVisible(false);
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
                    autoCompletePanel.add(nodeNameLabel);
                }
                testItinerary.updateUI();
            }

        }

        @Override
        public void focusGained(FocusEvent e) {
            autoComplete();
        }

        @Override
        public void focusLost(FocusEvent e) {
            autoCompletePanel.setVisible(false);
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
            String start = startNode.getText() ;
            String arrival = arrivalNode.getText() ;
            ArrayList<String> stepsString = new ArrayList<>() ;

            //Get all step's name and check it
            for (JTextField jTextField : steps){
                if (jTextField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(mapView,"Veuilliez saisir une étape intermédiaire valide","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    stepsString.add(jTextField.getText());
                }
            }

            //Get all node's name
            ArrayList<String> nodeNames = map.getAllNodeNames();

            //Check nodes
            if (start.isEmpty() || arrival.isEmpty()){
                JOptionPane.showMessageDialog(mapView,"Saisissez tous les champs !","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
            } else if ((map.getNodes().containsKey(start) || nodeNames.contains(start)) && (map.getNodes().containsKey(arrival) || nodeNames.contains(arrival))) {

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
                //transportsToAvoid.addAll(TransportRepository.getInstance().getTransportToAvoid(TransportIdentifier.BICYCLE));
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
                testItinerary.setVisible(false);
                revalidate();
            } else {
                JOptionPane.showMessageDialog(mapView,"Destination inconnue","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
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
            int numberStep = steps.size();

            //Add a limit : max 3 steps
            if (numberStep > 2) {
                return;
            } else if (numberStep == 2) {
                addStep.setVisible(false);
            }

            //Get the layout
            SpringLayout layout = (SpringLayout) testItinerary.getLayout();

            //Add the new component
            JTextField newStep = new JTextField();
            newStep.setPreferredSize(new Dimension(175,height));
            newStep.setMaximumSize(newStep.getPreferredSize());
            AutoCompletion newListener = new AutoCompletion(newStep);
            newStep.getDocument().addDocumentListener(newListener);
            newStep.addFocusListener(newListener);

            JButton rmStep = new JButton("X");
            rmStep.setPreferredSize(new Dimension(30, 30));
            rmStep.setBackground(new Color(240,240,240));
            rmStep.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
            rmStep.setMaximumSize(calculateItinerary.getPreferredSize());
            rmStep.addActionListener(new RemoveSteplistener(newStep));

            testItinerary.add(newStep);
            testItinerary.add(rmStep);

            //Add the new text field to the steps list
            steps.add(newStep);

            //Add Label if it's first step
            if(numberStep == 0) {
                testItinerary.add(stepLabel);
                layout.putConstraint(SpringLayout.NORTH,stepLabel,10,SpringLayout.SOUTH,startNode);
            }
            layout.putConstraint(SpringLayout.NORTH, newStep,numberStep*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNode);
            layout.putConstraint(SpringLayout.NORTH, rmStep, numberStep*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNode);
            layout.putConstraint(SpringLayout.WEST, rmStep, 5, SpringLayout.EAST, newStep);
            layout.putConstraint(SpringLayout.NORTH, arrivalLabel,(numberStep+1)*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNode);

            testItinerary.updateUI();
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

    private class RemoveSteplistener implements ActionListener {

        private JTextField stepField;

        public RemoveSteplistener(JTextField stepField) {
            this.stepField = stepField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //Variables
            int height = 30;
            int marginTop = 10;
            int blockHeight = height + marginTop;
            int numberStep = steps.size();
            SpringLayout layout = (SpringLayout) testItinerary.getLayout();

            steps.remove(stepField);
            testItinerary.remove(stepField);
            testItinerary.remove((JButton) e.getSource());

            if ((numberStep - 1) == 0) {
                testItinerary.remove(stepLabel);
                layout.putConstraint(SpringLayout.NORTH, arrivalLabel, marginTop, SpringLayout.SOUTH, startNode);
            } else {
                for (JTextField stepF : steps) {
                    layout.putConstraint(SpringLayout.NORTH, stepF, steps.indexOf(stepF)*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNode);
                    layout.putConstraint(SpringLayout.NORTH, testItinerary.findComponentAt(205, stepF.getY()), steps.indexOf(stepF)*blockHeight + marginTop + 20, SpringLayout.SOUTH, startNode);
                }
                layout.putConstraint(SpringLayout.NORTH, arrivalLabel, (numberStep - 1) * blockHeight + marginTop + 20, SpringLayout.SOUTH, startNode);
            }

            if (numberStep == 3) {
                addStep.setVisible(true);
            }

            testItinerary.updateUI();
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
            JLabel time = new JLabel();
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
                                    iconPath = "src/img/round_plane_black_24dp.png";
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

            time.setText((int)Math.ceil(itinerary.getTime()) + " min de trajet");
            cost.setText(String.format("%.2f",itinerary.getCost()) + " €");

            button.addActionListener(new NewItineraryListener());
            button.setPreferredSize(new Dimension(210, 30));
            button.setBackground(new Color(200,200,200));
            button.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

            this.add(itineraryStart);
            this.add(itineraryFinal);
            this.add(time);
            this.add(cost);
            this.add(button);

            itineraryLayout.putConstraint(SpringLayout.NORTH, itineraryStart, 15, SpringLayout.SOUTH, cost);
            itineraryLayout.putConstraint(SpringLayout.NORTH, itineraryFinal, 15, SpringLayout.SOUTH, previousLabel);
            itineraryLayout.putConstraint(SpringLayout.NORTH, time, 15,SpringLayout.NORTH, this);
            itineraryLayout.putConstraint(SpringLayout.NORTH, cost, 15, SpringLayout.SOUTH, time);
            itineraryLayout.putConstraint(SpringLayout.SOUTH, button, -10, SpringLayout.SOUTH, this);
            itineraryLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, button, 0, SpringLayout.HORIZONTAL_CENTER, this);
            this.setPreferredSize(IDEAL_ITINERARY_PANEL_DIMENSION);
        }

    }

    private class NewItineraryListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getContentPane().remove(itineraryView);
            testItinerary.setVisible(true);
            mapView.setItinerary(null);
            mapView.updateUI();
            revalidate();
        }

    }

}
