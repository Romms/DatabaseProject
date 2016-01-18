package me.dblab.client.layout;

import me.dblab.client.clientcontroller.ClientController;
import me.dblab.client.misc.DatabaseUpdateListener;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class ControlPanel extends JPanel implements DatabaseUpdateListener {
    protected JButton newButton;
    protected JButton loadButton;
    protected JButton saveButton;
    protected JButton closeButton;

    protected JButton addTableButton;
    protected JButton removeTableButton;
    protected JButton intersectTablesButton;
    protected JButton productTablesButton;

    protected ClientController controller;

    public ControlPanel(ClientController controller) {
        super(new GridLayout(1, 2));

        this.controller = controller;

        setUpUI();
    }

    private void setUpUI() {
////        newButton = new JButton("New");
////        loadButton = new JButton("Open");
////        saveButton = new JButton("Save");
////        closeButton = new JButton("Close");
//
////        addTableButton = new JButton("Add");
////        removeTableButton = new JButton("Remove");
//
//        intersectTablesButton = new JButton("Intersect");
//        productTablesButton = new JButton("Product");
//
////        newButton.addActionListener(actionEvent -> controller.newDatabase());
////        loadButton.addActionListener(actionEvent -> controller.loadDatabase());
////        saveButton.addActionListener(actionEvent -> controller.saveDatabase());
////        closeButton.addActionListener(actionEvent -> controller.closeDatabase());
//
////        addTableButton.addActionListener(actionEvent -> controller.createTable());
////        removeTableButton.addActionListener(actionEvent -> controller.removeTable());
//        intersectTablesButton.addActionListener(actionEvent -> controller.intersectTables());
//        productTablesButton.addActionListener(actionEvent -> controller.productTables());
//
//        JPanel leftSide = new JPanel();
//        leftSide.setLayout(new FlowLayout(FlowLayout.LEADING));
////        leftSide.add(new JLabel("Date Base:"));
////        leftSide.add(newButton);
////        leftSide.add(loadButton);
////        leftSide.add(saveButton);
////        leftSide.add(closeButton);
//
//        JPanel rightSide = new JPanel();
//        rightSide.setLayout(new FlowLayout(FlowLayout.TRAILING));
////        rightSide.add(addTableButton);
////        rightSide.add(removeTableButton);
////        rightSide.add(intersectTablesButton);
//        rightSide.add(productTablesButton);
//
//
//        add(leftSide);
//        add(rightSide);

        controller.addUpdateListener(this);
    }

    @Override
    public void handleDatabaseUpdate() {
        try {
            setTableButtonsVisibility(controller.getDatabaseController().isOpened());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setTableButtonsVisibility(boolean visible) {
//        saveButton.setEnabled(visible);
//        closeButton.setEnabled(visible);
//        addTableButton.setEnabled(visible);
//        removeTableButton.setEnabled(visible);
//        intersectTablesButton.setEnabled(visible);
//        productTablesButton.setEnabled(visible);
    }
}
