import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SizeOfTheMap extends JFrame implements KeyListener {
    Font font=null;
    private String userName;
    private JPanel mainPanel=new JPanel(new GridBagLayout());
    private JPanel upArrowPanel;
    private JPanel downArrowPanel;
    private JLabel upperLabel=new JLabel("set size");
    private int planeSize = 10;
    private JLabel sizeLabel=new JLabel(String.valueOf(planeSize));
    private JLabel exitLabel=new JLabel(">>> PLAY <<<");
    public SizeOfTheMap(String userName){
        this.userName=userName;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);
        Font fontForNumbers=font.deriveFont(48f);
        upperLabel.setFont(workFont);
        upperLabel.setForeground(Color.yellow);
        exitLabel.setFont(workFont);
        exitLabel.setForeground(Color.yellow);
        sizeLabel.setFont(fontForNumbers);
        sizeLabel.setForeground(Color.yellow);
        upArrowPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage image;
                try {
                     image= ImageIO.read(new File("arrowUp.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                double scaleFactor = (double) getHeight() / image.getHeight();
                int newWidth = (int) (image.getWidth() * scaleFactor);
                g.drawImage(image,getWidth()/2-(newWidth/2),0,newWidth,getHeight(),null);
            }
        };
        upArrowPanel.setBackground(Color.black);
        downArrowPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage image;
                try {
                    image= ImageIO.read(new File("arrowDown.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                double scaleFactor = (double) getHeight() / image.getHeight();
                int newWidth = (int) (image.getWidth() * scaleFactor);
                g.drawImage(image,getWidth()/2-(newWidth/2),0,newWidth,getHeight(),null);
            }
        };
        downArrowPanel.setBackground(Color.black);
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.weighty=0.2;
        gbc.weightx=1.0;
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(upperLabel,gbc);
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(upArrowPanel,gbc);
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy=2;
        mainPanel.add(sizeLabel,gbc);
        gbc.gridy=3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(downArrowPanel,gbc);
        gbc.fill=GridBagConstraints.NONE;
        gbc.gridy=4;
        mainPanel.add(exitLabel,gbc);





        mainPanel.setBackground(Color.BLACK);
        mainPanel.setPreferredSize(new Dimension(500,500));
        setTitle("Choose size of the map");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setSize(mainPanel.getPreferredSize());
        setLayout(new FlowLayout(0, 0, 0));

        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && planeSize < 100){
            planeSize++;
            sizeLabel.setText(String.valueOf(planeSize));
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && planeSize > 10) {
            planeSize--;
            sizeLabel.setText(String.valueOf(planeSize));
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            SwingUtilities.invokeLater(()->new Game(Integer.parseInt(sizeLabel.getText()),this.userName));
            this.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
