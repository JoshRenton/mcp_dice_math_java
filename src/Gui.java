import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.stream.IntStream;

public class Gui {
    private JFrame guiFrame;
    private static JTable outputTable;

    public Gui() {
        initialiseGui();
    }

    private void initialiseGui() {
        guiFrame = new JFrame("MCP Dice Math");
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setLocationRelativeTo(null);

        int[] diceNums = IntStream.range(0, 15).toArray();
        Integer[] diceNumsInt = new Integer[diceNums.length];
        for (int num = 0; num < diceNums.length; num++) {
            diceNumsInt[num] = diceNums[num];
        }

        GridBagLayout gbl = new GridBagLayout();
        guiFrame.setLayout(gbl);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Centers text in dropdown
        DefaultListCellRenderer lcr = new DefaultListCellRenderer();
        lcr.setHorizontalAlignment(DefaultListCellRenderer.HORIZONTAL);

        JComboBox<Integer> atkDiceNumBox = new JComboBox<>(diceNumsInt);
        atkDiceNumBox.setRenderer(lcr);
        JComboBox<Integer> defDiceNumBox = new JComboBox<>(diceNumsInt);
        defDiceNumBox.setRenderer(lcr);
        atkDiceNumBox.addActionListener(new Runner.AtkListener());
        defDiceNumBox.addActionListener(new Runner.DefListener());

        JLabel atkLabel = new JLabel("<html><div style='text-align: center;'> Number of <br> " +
                "Attack Dice");
        atkLabel.setForeground(Color.RED);
        atkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel defLabel = new JLabel("<html><div style='text-align: center;'> Number of <br> " +
                "Defense Dice");
        defLabel.setForeground(Color.BLUE);
        defLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel dicePanel = new JPanel();
        BoxLayout bl = new BoxLayout(dicePanel, BoxLayout.Y_AXIS);
        dicePanel.setLayout(bl);
        dicePanel.add(atkLabel);
        dicePanel.add(atkDiceNumBox);
        dicePanel.add(defLabel);
        dicePanel.add(defDiceNumBox);

        addComponentToLayout(dicePanel, guiFrame, gbl, gbc, 0, 0, 1, 1);

        String[] headers = new String[]{"Number of Hits", "Percent Chance"};
        DefaultTableModel model = new DefaultTableModel(headers, 2);
        outputTable = new JTable(model);
        outputTable.setDefaultEditor(Object.class, null);
        JScrollPane pane = new JScrollPane(outputTable);
        addComponentToLayout(pane, guiFrame, gbl, gbc, 1, 0, 1, 2);

        guiFrame.pack();
    }

    public void showGui() {
        guiFrame.setVisible(true);
    }

    public void displayResults(double[] results) {
        DefaultTableModel model = (DefaultTableModel) outputTable.getModel();
        // Clear table
        model.setRowCount(0);

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < results.length; i++) {
            String percent = df.format(results[i] * 100);

            if (i == 0) {
                model.addRow(new Object[]{i, percent + "%"});
            }
            else if (percent.equals("0")) {
                model.addRow(new Object[]{i + "+", "A gamble for the history books."});
                // Stop displaying results after the chance is basically 0
                break;
            } else {
                model.addRow(new Object[]{i + "+", percent + "%"});
            }
        }
    }

    // Add a component to the container layout
    private void addComponentToLayout(Component component, Container container, GridBagLayout gbl,
                                      GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridheight = gridheight;
        gbc.gridwidth = gridwidth;

        gbl.setConstraints(component, gbc);
        container.add(component);
    }
}
