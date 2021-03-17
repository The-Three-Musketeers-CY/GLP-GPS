package gui;

import config.GPSConfig;
import model.Itinerary;
import model.Map;
import model.Node;
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
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    private MapView mapView ;

    private JButton resetButton = new JButton("Reset default position");
    private JButton calculateItinerary = new JButton("Rechercher") ;
    private JButton addStep = new JButton("Ajouter une étape");
    private JButton rmStep = new JButton("X");

    private JTextField startNode = new JTextField();
    private JTextField arrivalNode = new JTextField();
    private ArrayList<JTextField> steps ;

    private JPanel autoCompletePanel = new JPanel();

    private JLabel startLabel = new JLabel("Saisissez un point de départ :");
    private JLabel arrivalLabel = new JLabel("Saisissez un point de d'arrivée :");

    private JPanel testItinerary = new JPanel();
    private JPanel mapPanel = new JPanel();

    public MainGUI(String title, String mapPath) {
        super(title);

        try {
            setIconImage(new ImageIcon(ImageIO.read(new File("favicon.png"))).getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        testItinerary.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                rmStep.setVisible(false);
                for(JTextField jTextField : steps){
                    jTextField.setPreferredSize(new Dimension(210,30));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

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

        JLabel copyrightsWallethChevalierLabel = new JLabel("Benjamin Walleth | Paul Chevalier");
        JLabel copyrightsDenoyerLabel = new JLabel("William Denoyer");
        JLabel copyrightsGLPLabel = new JLabel("GLP GPS - 2021");
        testItinerary.add(copyrightsWallethChevalierLabel);
        testItinerary.add(copyrightsDenoyerLabel);
        testItinerary.add(copyrightsGLPLabel);

        rmStep.setPreferredSize(new Dimension(30, 30));
        rmStep.setBackground(new Color(240,240,240));
        rmStep.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        rmStep.setMaximumSize(calculateItinerary.getPreferredSize());

        autoCompletePanel.setLayout(new BoxLayout(autoCompletePanel,BoxLayout.Y_AXIS));

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

    private class AutoCompletion implements DocumentListener, FocusListener{

        JTextField component ;

        public AutoCompletion(JTextField component){
            this.component = component ;
            autoCompletePanel.setVisible(false);
            testItinerary.add(autoCompletePanel);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            autoCompletion();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            autoCompletion();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        public void autoCompletion(){

            ArrayList<String> nodeNames = map.getAllNodeNames();
            String currentText = component.getText();

            //Get the layout
            SpringLayout layout = (SpringLayout) testItinerary.getLayout();
            //Remove all old components
            for(Component component : autoCompletePanel.getComponents()){
                autoCompletePanel.remove(component);
            }
            //Set the panel constraints
            layout.putConstraint(SpringLayout.NORTH,autoCompletePanel,0,SpringLayout.SOUTH,component);

            //Show the panel
            if(component.getText().isBlank()){
                autoCompletePanel.setVisible(false);
            }else {
                autoCompletePanel.setVisible(true);
            }

            for(String nodeName : nodeNames){
                if(nodeName.equals(currentText)){
                    autoCompletePanel.setVisible(false);
                }else if(Pattern.matches("(?i)"+currentText+".*",nodeName)){
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
                    testItinerary.revalidate();
                }
            }
        }

        @Override
        public void focusGained(FocusEvent e) {
            autoCompletion();
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
            for(JTextField jTextField : steps){
                if(jTextField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(mapView,"Veuilliez saisir une étape intermédiaire valide","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
                }else if(stepsString.contains(jTextField.getText())){
                    JOptionPane.showMessageDialog(mapView,"Cette étape est déja saisie : "+jTextField.getText(),"Erreur de saisie",JOptionPane.ERROR_MESSAGE);
                }else{
                    stepsString.add(jTextField.getText());
                }
            }

            //Get all node's name
            ArrayList<String> nodeNames = map.getAllNodeNames();

            //Check nodes
            if(start.isEmpty() || arrival.isEmpty()){
                JOptionPane.showMessageDialog(mapView,"Saisissez tous les champs !","Erreur de saisie",JOptionPane.ERROR_MESSAGE);
            }else if((map.getNodes().containsKey(start) || nodeNames.contains(start)) && (map.getNodes().containsKey(arrival) || nodeNames.contains(arrival))) {

                Node startingNode, arrivalNode;
                startingNode = map.getNodeFromId(start);
                arrivalNode = map.getNodeFromId(arrival);


                if(startingNode == null) startingNode = map.getNodeFromName(start);
                if(arrivalNode == null) arrivalNode = map.getNodeFromName(arrival);

                ArrayList<Node> nodes = new ArrayList<>();
                nodes.add(startingNode);
                for(String stepNodeField : stepsString) {
                    Node stepNode;
                    stepNode = map.getNodeFromId(stepNodeField);
                    if(stepNode == null) stepNode = map.getNodeFromName(stepNodeField);
                    nodes.add(stepNode);
                }
                nodes.add(arrivalNode);
                Itinerary itinerary = Dijkstra.calculateTotalItinerary(nodes, map);
                mapView.setItinerary(itinerary);
                mapView.repaint();
                JOptionPane.showMessageDialog(mapView, itinerary.toString());
            }else{
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
            int height = 30 ;
            int marginTop = 10 ;
            int blockHeight = height + marginTop ;
            int numberStep = steps.size() ;

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
            JPanel ctnStep = new JPanel();
            ctnStep.setBackground(Color.WHITE);
            ctnStep.add(newStep);
            newStep.setPreferredSize(new Dimension(210,height));
            newStep.setMaximumSize(newStep.getPreferredSize());
            testItinerary.add(ctnStep);
            //Add the new text field to the steps list
            steps.add(newStep);

            //TODO On peut supprimer celui-ci mais c'est moins fluide...
            ctnStep.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    rmStep.setVisible(true);
                    ctnStep.add(rmStep);
                    newStep.setPreferredSize(new Dimension(180,30));
                    //Reset size of previous TextFields
                    for(JTextField jTextField : steps){
                        if(!newStep.equals(jTextField)) jTextField.setPreferredSize(new Dimension(210,30));
                    }
                    testItinerary.revalidate();
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            newStep.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //Here, it's second check for more fluidity
                    rmStep.setVisible(true);
                    if(!ctnStep.isAncestorOf(rmStep)) {
                        ctnStep.add(rmStep);
                        newStep.setPreferredSize(new Dimension(180, 30));
                        testItinerary.revalidate();
                    }
                    //Reset size of previous TextFields
                    for(JTextField jTextField : steps){
                        if(!newStep.equals(jTextField)) jTextField.setPreferredSize(new Dimension(210,30));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            //Add Label if it's first step
            if(numberStep == 0) {
                JLabel jLabel = new JLabel("Etapes intérmédiaires");
                testItinerary.add(jLabel);
                layout.putConstraint(SpringLayout.NORTH,jLabel,10,SpringLayout.SOUTH,startNode);
            }
            layout.putConstraint(SpringLayout.NORTH,ctnStep,numberStep*blockHeight + marginTop + 20,SpringLayout.SOUTH,startNode);
            layout.putConstraint(SpringLayout.NORTH,arrivalLabel,(numberStep+1)*blockHeight + marginTop + 20,SpringLayout.SOUTH,startNode);

            testItinerary.revalidate();
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

}
