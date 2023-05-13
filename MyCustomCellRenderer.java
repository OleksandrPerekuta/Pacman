import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyCustomCellRenderer extends DefaultTableCellRenderer {

    private int[][] borders;
    private BufferedImage borderImage;
    private BufferedImage cherrySmallImage;
    private BufferedImage cherryBigImage;


    private int cherryRadius;

    public MyCustomCellRenderer(int[][] borders) {
        this.borders = borders;
        cherryRadius = 0;
        borderImage = null;
        try {
            borderImage = ImageIO.read(new File("borderimage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cherrySmallImage = ImageIO.read(new File("cherrySmall.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            cherryBigImage = ImageIO.read(new File("cherryBig.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCherryRadius(int size) {
        cherryRadius = size;
        System.out.println("Cherry radius is = " + cherryRadius);
        repaint();
    }

    public int[][] getBorders() {
        return borders;
    }

    public void drawCherry(JTable table, int row, int column) {
        table.setValueAt(new Cherry(), row, column);
        table.repaint();
    }

    public void eraseCherry(JTable table, int row, int column) {
        table.setValueAt(null, row, column);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel cell = new JPanel(new BorderLayout());
        if (borders[row][column]==0) {
            cell.setBackground(Color.black);
        } else if (borders[row][column]==1){
            JPanel insidePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(borderImage, 0, 0, getWidth(), getHeight(), null);
                }
            };
            cell.add(insidePanel);
            cell.setBackground(Color.white);
        } else if (borders[row][column]==2) {
            JPanel insidePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(cherrySmallImage, 0, 0, getWidth(), getHeight(), null);
                }
            };
            cell.add(insidePanel);
            cell.setBackground(Color.white);
        }
        else if (borders[row][column]==3) {
            JPanel insidePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(cherryBigImage, 0, 0, getWidth(), getHeight(), null);
                }
            };
            cell.add(insidePanel);
            cell.setBackground(Color.white);
        }
        return cell;
    }

    class Cherry {
        private static final Color COLOR = Color.WHITE;
        private int radius;


        public Cherry() {
            this.radius = cherryRadius;
        }

        public static Color getColor() {
            return COLOR;
        }

        public int getRadius() {
            return radius;
        }
    }
}
