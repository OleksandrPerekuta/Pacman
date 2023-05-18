import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Menu extends JFrame implements KeyListener {
    Font font=null;
    private int selectedIndex = 0;
    ArrayList<JLabel> menuLabels=new ArrayList<>();
    ArrayList<JLabel> arrowLabels=new ArrayList<>();
    JPanel menuPanel=new JPanel(new GridBagLayout());

    public Menu(){
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);
        addKeyListener(this);
        JLabel newGame=new JLabel("NEW GAME");
        JLabel bestRate=new JLabel("HIGH SCORE");
        JLabel exit=new JLabel("EXIT");
        menuLabels.add(newGame);
        menuLabels.add(bestRate);
        menuLabels.add(exit);
        for (JLabel label:menuLabels){
            label.setFont(workFont);
            label.setForeground(Color.yellow);
        }
        GridBagConstraints gbc =new GridBagConstraints();
        for(int i=0;i<3;i++){
            JLabel arrowLeft=new JLabel(">>>");
            arrowLeft.setFont(workFont);
            arrowLeft.setForeground(Color.yellow);
            arrowLabels.add(arrowLeft);
            gbc.gridx=0;
            gbc.gridy=i;
            gbc.weightx=0.3;
            gbc.weighty=1.0/3.0;
            gbc.anchor=GridBagConstraints.EAST;
            menuPanel.add(arrowLeft,gbc);
        }
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.weightx=0.4;
        gbc.anchor=GridBagConstraints.CENTER;
        menuPanel.add(newGame,gbc);
        gbc.gridx=1;
        gbc.gridy=1;
        menuPanel.add(bestRate,gbc);

        gbc.gridx=1;
        gbc.gridy=2;
        menuPanel.add(exit,gbc);
        for(int i=0;i<3;i++){
            JLabel arrowRight=new JLabel("<<<");
            arrowRight.setFont(workFont);
            arrowRight.setForeground(Color.yellow);
            arrowLabels.add(arrowRight);
            gbc.gridx=3;
            gbc.gridy=i;
            gbc.weightx=0.3;
            gbc.weighty=1.0/3.0;
            gbc.anchor=GridBagConstraints.WEST;
            menuPanel.add(arrowRight,gbc);
        }
        menuPanel.setBackground(Color.BLACK);

        setArrowVisibility();

        add(menuPanel);
        setResizable(false);
        setTitle("Menu");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setArrowVisibility() {
        for (int i = 0; i < menuLabels.size(); i++) {
            JLabel arrowLeft = arrowLabels.get(i);
            JLabel arrowRight = arrowLabels.get(i + 3);
            arrowLeft.setVisible(selectedIndex == i);
            arrowRight.setVisible(selectedIndex == i);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_DOWN){
            selectedIndex++;
            if (selectedIndex>2)
                selectedIndex=0;
        } else if (e.getKeyCode()==KeyEvent.VK_UP) {
            selectedIndex--;
            if (selectedIndex<0)
                selectedIndex=2;
        } else if (e.getKeyCode()==KeyEvent.VK_ENTER) {
            switch (selectedIndex) {
                case 0:
                    SwingUtilities.invokeLater(()->new UserInputName());
                    this.dispose();
                    break;
                case 1:
                    SwingUtilities.invokeLater(()->new HighScore());
                    this.dispose();
                    break;
                case 2:
                    System.exit(0);
                    break;
            }
        }
        setArrowVisibility();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
