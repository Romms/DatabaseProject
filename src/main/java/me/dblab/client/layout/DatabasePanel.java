package me.dblab.client.layout;

import me.dblab.client.clientcontroller.ClientController;
import me.dblab.client.misc.DatabaseUpdateListener;
import me.dblab.exceptions.ColumnNotExistsException;
import me.dblab.databasecontroller.DatabaseController;

import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.TableNotExistsException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.rmi.RemoteException;

public class DatabasePanel extends JPanel implements DatabaseUpdateListener {
    private static final String NULL_STRING = "<NULL>";

    protected ClientController controller;
    protected JTabbedPane tabbedPane;

    public DatabasePanel(ClientController controller) {
        super(new GridLayout(1, 1));

        this.controller = controller;
        this.tabbedPane = new JTabbedPane();

        tabbedPane.setPreferredSize(new Dimension(800, 400));
        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        refreshTabbedPane();

        controller.addUpdateListener(this);
    }

    @Override
    public void handleDatabaseUpdate() {
        try {
            showDatabase(controller.getDatabaseController());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showDatabase(DatabaseController databaseController) throws RemoteException {
        tabbedPane.setVisible(databaseController.isOpened());
        if (!databaseController.isOpened()) {
            return;
        }

        int selI = tabbedPane.getSelectedIndex();
        refreshTabbedPane();

        String[] tableNames = databaseController.getTableNames();

        for (int i = 0; i < tableNames.length; ++i) {
            showTable(databaseController, tableNames[i]);
            if (tableNames[i].equals(controller.getActiveTable())) {
                tabbedPane.setSelectedIndex(i);
            }
        }

        tabbedPane.setSelectedIndex(selI);
    }

    private void showTable(DatabaseController databaseController, String tableName) throws RemoteException {
        try {
            tabbedPane.addTab(tableName, getTableComponent(databaseController, tableName));
        } catch (TableNotExistsException e) {
            e.printStackTrace();
        }
    }

    private JComponent getTableComponent(DatabaseController databaseController, String tableName) throws RemoteException, TableNotExistsException {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        String[] rowIds = databaseController.getTableRowIds(tableName);
        int numRows = rowIds.length;

        String[] columnNames = databaseController.getTableColumns(tableName);
        int numCols = columnNames.length;

        String[][] values = new String[numRows][numCols];
        try {
            for (int row = 0; row < numRows; ++row) {
                for (int col = 0; col < numCols; ++col) {
                    String value = databaseController.tableGetValue(tableName, rowIds[row], columnNames[col]);
                    values[row][col] = value == null ? NULL_STRING : value;
                }
            }
        } catch (TableNotExistsException | RowNotExistsException | ColumnNotExistsException e) {
            e.printStackTrace();
        }

        JTable table = new JTable(values, columnNames);

        table.getModel().addTableModelListener(event -> {
            if (event.getFirstRow() != event.getLastRow() || event.getType() != TableModelEvent.UPDATE) {
                throw new RuntimeException("Unsupported change to table");
            }

            int row = event.getFirstRow();
            int col = event.getColumn();

            String value = (String) table.getValueAt(row, col);
            if (value.equals(NULL_STRING)) {
                value = null;
            }

            try {
                controller.databaseTableSetValue(tableName, rowIds[row], table.getColumnName(col), value);
                controller.notifyUpdateListeners();
            } catch (Exception e) {
                //ClientController.showWarning(exception.getMessage());
            }
        });


        JButton addEmptyRowButton = new JButton("Add Row");
        JButton deleteTableButton = new JButton("Delete Table");
        JButton deleteRowsButton = new JButton("Delete Selected Rows");

        addEmptyRowButton.addActionListener(actionEvent -> controller.addEmptyRow(tableName));

        deleteTableButton.addActionListener(actionEvent -> controller.removeTable(tableName));

        deleteRowsButton.addActionListener(actionEvent -> {
            int[] rowsToDelete = table.getSelectedRows();

            String[] rowIdsToDelete = new String[rowsToDelete.length];
            for (int index = 0; index < rowsToDelete.length; ++index) {
                rowIdsToDelete[index] = rowIds[rowsToDelete[index]];
            }

            try {
                controller.removeTableRows(tableName, rowIdsToDelete);
            } catch (TableNotExistsException e) {
                e.printStackTrace();
            }
        });

        JButton intersectTablesButton = new JButton("Intersect");
        intersectTablesButton.addActionListener(actionEvent -> controller.intersectTables());


        JPanel panelGrid = new JPanel(new GridLayout(1, 2));

        JPanel leftSide = new JPanel();
        leftSide.setLayout(new FlowLayout(FlowLayout.LEADING));
        leftSide.add(addEmptyRowButton);
        leftSide.add(deleteRowsButton);

        JPanel rightSide = new JPanel();
        rightSide.setLayout(new FlowLayout(FlowLayout.TRAILING));
        rightSide.add(deleteTableButton);
        rightSide.add(intersectTablesButton);


        panelGrid.add(leftSide);
        panelGrid.add(rightSide);


        // Put all together
        panel.add(panelGrid, BorderLayout.PAGE_START);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void refreshTabbedPane() {
        tabbedPane.removeAll();

        System.out.println(":asfasf");

        String[] supportedTypes = new String[0];
        try {
            supportedTypes = controller.getDatabaseController().getSupportedTypes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        addTablePanel = new AddTablePanel(supportedTypes);

        newTableName = new JTextField("Table Name");
        newTableName.setColumns(20);
        JButton addTable = new JButton("Add Table");
        addTable.addActionListener(e -> controller.createTable(newTableName.getText(), addTablePanel.getScheme()));

        JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelLabel.add(newTableName);
        panelLabel.add(addTable);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(panelLabel,BorderLayout.NORTH);
        panel.add(addTablePanel,BorderLayout.CENTER);
        tabbedPane.addTab("+", panel);

    }

    private JTextField newTableName;
    private AddTablePanel addTablePanel;
}
