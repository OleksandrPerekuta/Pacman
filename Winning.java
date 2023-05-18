import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Winning extends JFrame implements KeyListener {
    String username;
    int score;
    Font font=null;

    JPanel upperPanel=new JPanel();
    JLabel upperLabel;
    JPanel middlePanel=new JPanel();
    JLabel middleLabel;
    JPanel downPanel=new JPanel();
    JLabel downLabel=new JLabel(">>> EXIT <<<");
    JPanel mainPanel;

    public Winning(String username, int score){
        this.username=username;
        this.score=score;
        upperLabel=new JLabel(this.username+", you Won!");
        middleLabel=new JLabel("your score is: "+this.score);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);
        upperLabel.setFont(workFont);
        upperLabel.setForeground(Color.yellow);
        upperPanel.add(upperLabel);
        middleLabel.setFont(workFont);
        middleLabel.setForeground(Color.yellow);
        middlePanel.add(middleLabel);
        downLabel.setFont(workFont);
        downLabel.setForeground(Color.yellow);
        downPanel.add(downLabel);
        mainPanel=new JPanel(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.weightx=1.0;
        gbc.weighty=0.33;
        gbc.gridx=0;
        gbc.anchor=GridBagConstraints.CENTER;

        gbc.gridy=0;
        mainPanel.add(upperPanel,gbc);
        gbc.gridy=1;
        mainPanel.add(middlePanel,gbc);
        gbc.gridy=2;
        mainPanel.add(downPanel,gbc);
        mainPanel.setPreferredSize(new Dimension(500,500));
        upperLabel.setHorizontalAlignment(SwingConstants.CENTER);
        upperLabel.setVerticalAlignment(SwingConstants.CENTER);
        middleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        middleLabel.setVerticalAlignment(SwingConstants.CENTER);
        downLabel.setHorizontalAlignment(SwingConstants.CENTER);
        downLabel.setVerticalAlignment(SwingConstants.CENTER);







        for (Component c: mainPanel.getComponents())
            c.setBackground(Color.black);
        mainPanel.setBackground(Color.black);
        setTitle("Victory");
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
        if (e.getKeyCode()==KeyEvent.VK_ENTER) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
                writer.write(score+" "+username);
                writer.newLine();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            System.exit(0);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
