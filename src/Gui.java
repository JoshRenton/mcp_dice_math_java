import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Objects;
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

        JPanel atkFacePanel = createDiceFaceOptionsPanel("atk");
        JPanel defFacePanel = createDiceFaceOptionsPanel("def");

        JPanel leftPanel = new JPanel();
        BoxLayout blLeft = new BoxLayout(leftPanel, BoxLayout.Y_AXIS);
        leftPanel.setLayout(blLeft);
        leftPanel.add(atkPanel);
        leftPanel.add(atkFacePanel);

        JPanel rightPanel = new JPanel();
        BoxLayout blRight = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(blRight);
        rightPanel.add(defPanel);
        rightPanel.add(defFacePanel);

        addComponentToLayout(leftPanel, guiFrame, gbl, gbc, 0, 0, 1, 1);
        addComponentToLayout(rightPanel, guiFrame, gbl, gbc, 2, 0, 1, 1);

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

    private JPanel createDiceFaceOptionsPanel(String prefix) {
        JPanel panel = new JPanel();
        BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(bl);

        JCheckBox cbHit;
        if (Objects.equals(prefix, "atk")) {
            cbHit = new JCheckBox("Hit", true);
        } else {
            cbHit = new JCheckBox("Hit");
        }
        cbHit.setName(prefix + "Hit");
        cbHit.addActionListener(new FaceListener());

        JCheckBox cbBlock;
        if (Objects.equals(prefix, "def")) {
            cbBlock = new JCheckBox("Block", true);
        } else {
            cbBlock = new JCheckBox("Block");
        }
        cbBlock.setName(prefix + "Block");
        cbBlock.addActionListener(new FaceListener());

        JCheckBox cbWild = new JCheckBox("Wild", true);
        cbWild.setName(prefix + "Wild");
        cbWild.addActionListener(new FaceListener());

        JCheckBox cbBlank = new JCheckBox("Blank");
        cbBlank.setName(prefix + "Blank");
        cbBlank.addActionListener(new FaceListener());

        JCheckBox cbCrit = new JCheckBox("Crit", true);
        cbCrit.setName(prefix + "Crit");
        cbCrit.addActionListener(new FaceListener());

        JCheckBox cbFail = new JCheckBox("Fail");
        cbFail.setName(prefix + "Fail");
        cbFail.addActionListener(new FaceListener());

        panel.add(cbHit);
        panel.add(cbBlock);
        panel.add(cbWild);
        panel.add(cbBlank);
        panel.add(cbCrit);
        panel.add(cbFail);

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

    public static class FaceListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = ((JCheckBox) e.getSource()).getName();
            String prefix = name.substring(0, 3);
            String faceName = name.substring(3);
            if (Objects.equals(prefix, "atk")) {
                Runner.updateAtkDie(faceName);
            } else if (Objects.equals(prefix, "def")) {
                Runner.updateDefDie(faceName);
            }
        }
    }
 }
