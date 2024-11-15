import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
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

        int[] diceNumbers = IntStream.range(0, 15).toArray();
        Integer[] diceNumbersInt = new Integer[diceNumbers.length];
        for (int num = 0; num < diceNumbers.length; num++) {
            diceNumbersInt[num] = diceNumbers[num];
        }

        GridBagLayout gbl = new GridBagLayout();
        guiFrame.setLayout(gbl);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JPanel atkPanel = createDiceOptionPanel(diceNumbersInt, new Runner.AtkListener(),
                "Attack", Color.RED);
        JPanel defPanel = createDiceOptionPanel(diceNumbersInt, new Runner.DefListener(),
                "Defense", Color.BLUE);

        JPanel sidePanel = new JPanel();
        BoxLayout bl = new BoxLayout(sidePanel, BoxLayout.Y_AXIS);
        sidePanel.setLayout(bl);
        sidePanel.add(atkPanel);
        sidePanel.add(defPanel);

        addComponentToLayout(sidePanel, guiFrame, gbl, gbc, 0, 0, 1, 1);

        String[] headers = new String[]{"Number of Hits", "Percent Chance"};
        DefaultTableModel model = new DefaultTableModel(headers, 2);
        outputTable = new JTable(model);
        outputTable.setDefaultEditor(Object.class, null);
        JScrollPane pane = new JScrollPane(outputTable);
        addComponentToLayout(pane, guiFrame, gbl, gbc, 1, 0, 1, 2);

        guiFrame.pack();
    }

    private JPanel createDiceOptionPanel(Integer[] diceNumbers, ActionListener listener, String diceSide,
                                         Color textColor) {
        JPanel panel = new JPanel();

        // Centers text in dropdown
        DefaultListCellRenderer lcr = new DefaultListCellRenderer();
        lcr.setHorizontalAlignment(DefaultListCellRenderer.HORIZONTAL);

        JComboBox<Integer> diceNumBox = new JComboBox<>(diceNumbers);
        diceNumBox.setRenderer(lcr);

        diceNumBox.addActionListener(listener);

        JLabel label = new JLabel("<html><div style='text-align: center;'> Number of <br> " +
                 diceSide + " Dice");
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);
        panel.add(label);
        panel.add(diceNumBox);

        return panel;
    }

    public void showGui() {
        guiFrame.setVisible(true);
    }

    public void displayResults(double[] results) {
        DefaultTableModel model = (DefaultTableModel) outputTable.getModel();
        // Clear table
        model.setRowCount(0);

        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < results.length; i++) {
            String percent = df.format(results[i] * 100);

            if (i == 0) {
                model.addRow(new Object[]{i, percent});
            }
            else if (percent.equals("0.00")) {
                model.addRow(new Object[]{i + "+", "Domino would be pushing her luck."});
                // Stop displaying results after the chance is basically 0
                break;
            } else {
                model.addRow(new Object[]{i + "+", percent});
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
        gbc.ipady = 25;

        gbl.setConstraints(component, gbc);
        container.add(component);
    }

    public void createModifyDiceWindow() {
        JFrame modify = new JFrame("Modify Dice");
        modify.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        modify.pack();
        modify.setVisible(true);
    }
 }
