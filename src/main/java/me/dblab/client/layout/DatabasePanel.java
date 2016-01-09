package me.dblab.client.layout;

import me.dblab.client.clientcontroller.ClientController;
import me.dblab.client.misc.DatabaseUpdateListener;
import me.dblab.exceptions.ColumnNotExistsException;
import me.dblab.databasecontroller.DatabaseController;

import me.dblab.exceptions.RowNotExistsException;
import me.dblab.exceptions.TableNotExistsException;

import javax.swing.*;
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

        add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

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

        tabbedPane.removeAll();

        String[] tableNames = databaseController.getTableNames();

        for (int i = 0; i < tableNames.length; ++i) {
            showTable(databaseController, tableNames[i]);
            if (tableNames[i].equals(controller.getActiveTable())) {
                tabbedPane.setSelectedIndex(i);
            }
        }
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

        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        JButton addEmptyRowButton = new JButton("Add Empty Row");
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

        controlPanel.add(addEmptyRowButton);
        controlPanel.add(deleteTableButton);
        controlPanel.add(deleteRowsButton);

        // Put all together
        panel.add(controlPanel, BorderLayout.PAGE_START);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
}
