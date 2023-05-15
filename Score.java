import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Score extends JFrame implements KeyListener {
    JPanel main=new JPanel(new GridBagLayout());
    JPanel exitPanel =new JPanel(new GridLayout(1,3));
    JPanel score=new JPanel(new GridLayout(6,1));
    Font font=new Font("04b",NORMAL,30);
    public Score(){
        addKeyListener(this);
        main.setBackground(Color.black);
        exitPanel.setBackground(Color.black);
        GridBagConstraints gbc=new GridBagConstraints();

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.weighty=0.8;
        gbc.fill=GridBagConstraints.BOTH;
        main.add(score,gbc);

        gbc.gridy=1;
        gbc.weighty=0.2;
        main.add(exitPanel,gbc);
        gbc=new GridBagConstraints();

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=0.3;
        gbc.weighty=1;
        gbc.anchor=GridBagConstraints.EAST;
        JLabel arrowLeft = new JLabel(">>>");
        arrowLeft.setForeground(Color.yellow);
        arrowLeft.setBackground(Color.black);
        arrowLeft.setFont(font);
        arrowLeft.setOpaque(true);
        arrowLeft.setVerticalAlignment(JLabel.CENTER);
        arrowLeft.setHorizontalAlignment(JLabel.RIGHT);
        exitPanel.add(arrowLeft, gbc);

        gbc.gridx=1;
        gbc.weightx=0.4;
        gbc.anchor=GridBagConstraints.CENTER;
        JLabel exit = new JLabel("EXIT");
        exit.setForeground(Color.yellow);
        exit.setBackground(Color.black);
        exit.setFont(font);
        exit.setOpaque(true);
        exit.setVerticalAlignment(JLabel.CENTER);
        exit.setHorizontalAlignment(JLabel.CENTER);
        exitPanel.add(exit, gbc);

        gbc.gridx=2;
        gbc.gridy=0;
        gbc.weightx=0.3;
        gbc.weighty=1;
        gbc.anchor=GridBagConstraints.WEST;
        JLabel arrowRight = new JLabel("<<<");
        arrowRight.setForeground(Color.yellow);
        arrowRight.setBackground(Color.black);
        arrowRight.setFont(font);
        arrowRight.setOpaque(true);
        arrowRight.setVerticalAlignment(JLabel.CENTER);
        arrowRight.setHorizontalAlignment(JLabel.LEFT);
        exitPanel.add(arrowRight, gbc);
        gbc=new GridBagConstraints();

        score.setBackground(Color.black);
        JLabel bestGames=new JLabel("BEST GAMES:");
        JLabel one=new JLabel("one");
        JLabel two=new JLabel("two");
        JLabel three=new JLabel("three");
        JLabel four=new JLabel("four");
        JLabel five=new JLabel("five");
        score.setForeground(Color.yellow);
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.weighty=1.0/6.0;
        bestGames.setHorizontalAlignment(SwingConstants.CENTER);
        bestGames.setFont(font);
        bestGames.setForeground(Color.yellow);

        score.add(bestGames,gbc);
        gbc.gridy=1;
        one.setHorizontalAlignment(SwingConstants.CENTER);
        one.setFont(font);
        one.setForeground(Color.yellow);

        score.add(one,gbc);
        gbc.gridy=2;
        two.setHorizontalAlignment(SwingConstants.CENTER);
        two.setFont(font);
        two.setForeground(Color.yellow);

        score.add(two,gbc);
        gbc.gridy=3;
        three.setHorizontalAlignment(SwingConstants.CENTER);
        three.setFont(font);
        three.setForeground(Color.yellow);

        score.add(three,gbc);
        gbc.gridy=4;
        four.setHorizontalAlignment(SwingConstants.CENTER);
        four.setFont(font);
        four.setForeground(Color.yellow);

        score.add(four,gbc);
        gbc.gridy=5;
        five.setHorizontalAlignment(SwingConstants.CENTER);
        five.setFont(font);
        five.setForeground(Color.yellow);

        score.add(five,gbc);









        add(main);
        setSize(new Dimension(500,500));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Score");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
            SwingUtilities.invokeLater(()->new Menu());
            this.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
