import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class UserInputName extends JFrame implements KeyListener {
    private JLabel upperLabel=new JLabel("enter your name");
    private JLabel nameLabel =new JLabel("");
    private final JLabel exitLabel=new JLabel(">>> NEXT <<<");
    private JPanel mainPanel=new JPanel(new GridBagLayout());;
    Font font=null;

    public UserInputName(){
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);
        nameLabel.setFont(workFont);
        exitLabel.setFont(workFont);
        upperLabel.setFont(workFont);
        upperLabel.setForeground(Color.yellow);
        nameLabel.setForeground(Color.YELLOW);
        exitLabel.setForeground(Color.YELLOW);

        mainPanel.setPreferredSize(new Dimension(500,500));
        mainPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc =new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weighty=0.2;
        gbc.weightx=1.0;
        mainPanel.add(upperLabel,gbc);
        gbc.gridy=1;
        gbc.weighty=0.6;
        mainPanel.add(nameLabel,gbc);
        gbc.gridy=2;
        gbc.weighty=0.2;
        mainPanel.add(exitLabel,gbc);




        add(mainPanel);
        setTitle("Enter your name");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new FlowLayout(0, 0, 0));
        setSize(500, 500);
        setLocationRelativeTo(null);
        //setBackground(Color.black);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String text = nameLabel.getText();
            SwingUtilities.invokeLater(() -> new SizeOfTheMap(text));
            this.dispose();
        }else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            nameLabel.setText("");
        }else if (e.isShiftDown() && e.isControlDown() && e.getKeyCode()==KeyEvent.VK_Q) {
            SwingUtilities.invokeLater(()->new Menu());
            this.dispose();
        }
    }
    public void keyTyped(KeyEvent e) {
        nameLabel.setText(nameLabel.getText() + e.getKeyChar());
    }
    public void keyReleased(KeyEvent e) {}
}
