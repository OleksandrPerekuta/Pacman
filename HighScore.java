import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighScore extends JFrame implements KeyListener {
    JPanel mainPanel;
    JPanel upperPanel;
    JPanel middlePanel;
    JPanel downPanel;
    private JList<String> sortedList;
    Font font=null;

    public HighScore() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("04B_30__.TTF"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        Font workFont = font.deriveFont(36f);

        sortedList = new JList<>();
        sortedList.setFont(workFont);
        sortedList.setForeground(Color.yellow);
        sortedList.setBackground(Color.black);
        sortedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sortedList.setVisibleRowCount(5); // Show only the first 5 lines

        JScrollPane scrollPane = new JScrollPane(sortedList);
        getContentPane().add(scrollPane);

        // Read and sort the lines from the text file
        List<String> sortedLines = readAndSortLinesFromFile("scores.txt");

        // Display the sorted lines in the JList
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < Math.min(sortedLines.size(), 5); i++) {
            listModel.addElement(sortedLines.get(i));
        }
        sortedList.setModel(listModel);

        JLabel upperLabel=new JLabel("Highest scores:");
        upperLabel.setForeground(Color.yellow);
        upperLabel.setFont(workFont);
        upperPanel=new JPanel();
        upperPanel.add(upperLabel);
        middlePanel=new JPanel();
        middlePanel.add(sortedList);
        JLabel exitLabel=new JLabel(">>> EXIT <<<");
        exitLabel.setFont(workFont);
        exitLabel.setForeground(Color.yellow);
        downPanel=new JPanel();
        downPanel.add(exitLabel);
        mainPanel=new JPanel(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.weighty=0.33;
        gbc.weightx=1.0;
        gbc.anchor=GridBagConstraints.CENTER;
        gbc.gridx=0;
        gbc.gridy=0;
        mainPanel.add(upperPanel,gbc);
        gbc.gridy=1;
        mainPanel.add(middlePanel,gbc);
        gbc.gridy=2;
        mainPanel.add(downPanel,gbc);
        mainPanel.setPreferredSize(new Dimension(500,500));
        for (Component c : mainPanel.getComponents()){
            c.setBackground(Color.black);
        }
        mainPanel.setBackground(Color.black);
        add(mainPanel);
        setBackground(Color.black);
        setTitle("Sorted Text List");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(mainPanel.getPreferredSize());
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        addKeyListener(this);
        setFocusable(true);
    }

    private List<String> readAndSortLinesFromFile(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort the lines based on the integers using a custom Comparator
        Collections.sort(lines, new LineComparator());

        return lines;
    }

    // Custom Comparator implementation to sort lines based on the integers using regex
    private static class LineComparator implements Comparator<String> {
        private static final Pattern INTEGER_PATTERN = Pattern.compile("(\\d+)");

        @Override
        public int compare(String line1, String line2) {
            Matcher matcher1 = INTEGER_PATTERN.matcher(line1);
            Matcher matcher2 = INTEGER_PATTERN.matcher(line2);

            if (matcher1.find() && matcher2.find()) {
                int integer1 = Integer.parseInt(matcher1.group());
                int integer2 = Integer.parseInt(matcher2.group());
                return Integer.compare(integer2, integer1); // Compare in reverse order for largest to smallest
            }

            // If no integers found, consider the lines equal
            return 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            SwingUtilities.invokeLater(()->new Menu());
            this.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
