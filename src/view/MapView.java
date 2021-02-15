package view;

import model.Map;
import model.Network;
import model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MapView extends JPanel{

    private Map map ;

    private int decX = 0;
    private int decY = 0;

    private int posX = 0;
    private int posY = 0;

    private int cursorPosX = 0;
    private int cursorPosY = 0;

    private class Drag implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            int dx = e.getX() - cursorPosX;
            int dy = e.getY() - cursorPosY;

            System.out.println(dx);
            decX = dx + posX;
            decY = dy + posY;
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private class Click implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            for(Node node : map.getNodes().values()){
                if(node.getPosition().getX() + decX <= x+6 && node.getPosition().getX() + decX >= x-6
                        && node.getPosition().getY() + decY <= y+6 && node.getPosition().getY() + decY >= y-6){
                    System.out.println("OKKK !");
                    if(node.isPOI())
                    JOptionPane.showMessageDialog(MapView.this,node.getPoi().getName());
                    else JOptionPane.showMessageDialog(MapView.this,"Ce n'est pas un POI", "PAS POI", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            cursorPosX = e.getX();
            cursorPosY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            posX = decX;
            posY = decY;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public MapView(Map map){
        this.map = map ;
        this.addMouseListener(new Click());
        this.addMouseMotionListener(new Drag());

        JButton test = new JButton("Reset Position !");
        test.addActionListener(e -> {
            posX = 0;
            posY = 0;
            decX = 0;
            decY = 0;
            repaint();
        });

        add(test);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();

        for (Network network : map.getNetworks().values()){
           for(String nodeId1 : network.getWays().keySet()){
               Node node1 = map.getNodes().get(nodeId1);
               for(String nodeId2 : network.getWays().get(nodeId1).keySet()){
                   Node node2 = map.getNodes().get(nodeId2);
                   g2d.setStroke(new BasicStroke(5));
                   g2d.drawLine(node1.getPosition().getX() + decX,node1.getPosition().getY() + decY, node2.getPosition().getX() + decX, node2.getPosition().getY() + decY);
               }
           }
        }
        for(Node node : map.getNodes().values()){
            g.setColor(nodeColorType(node));
            g.fillOval(node.getPosition().getX()-6 + decX,node.getPosition().getY()-6 + decY,12,12);
        }
    }
    private Color nodeColorType(Node node){
        Color color = Color.BLACK;
        if(node.getPoi() != null) {
            switch (node.getPoi().getType()) {
                case ATTRACTION: {
                    color = Color.ORANGE;
                    break;
                }
                case BUILDING: {
                    color = Color.BLUE;
                    break;
                }
                case STATION: {
                    color = Color.GRAY;
                    break;
                }
                default:
                    color = Color.BLACK;
            }
        }
        return color;
    }

}
