import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Lost extends JFrame implements KeyListener {
    JPanel mainPanel=new JPanel(new GridBagLayout());
    JLabel label=new JLabel("You lost!");
    JLabel exit=new JLabel(">>> EXIT <<<");
    Font font=null;
    public Lost(){
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);
        label.setFont(workFont);
        label.setForeground(Color.yellow);
        label.setBackground(Color.black);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        exit.setFont(workFont);
        exit.setForeground(Color.yellow);
        exit.setBackground(Color.black);
        exit.setHorizontalAlignment(SwingConstants.CENTER);
        exit.setVerticalAlignment(SwingConstants.CENTER);

        mainPanel.setBackground(Color.black);
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridy=0;
        gbc.gridx=0;
        gbc.weighty=0.9;
        gbc.weightx=1.0;
        gbc.anchor=GridBagConstraints.CENTER;
        mainPanel.add(label,gbc);
        gbc.gridy=1;
        gbc.weighty=0.1;
        mainPanel.add(exit,gbc);
        mainPanel.setPreferredSize(new Dimension(500,500));
        addKeyListener(this);
        setBackground(Color.black);
        setSize(mainPanel.getPreferredSize());
        setTitle("Lost");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setSize(mainPanel.getPreferredSize());
        setLayout(new FlowLayout(0, 0, 0));
        setLocationRelativeTo(null);
        setFocusTraversalKeysEnabled(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
