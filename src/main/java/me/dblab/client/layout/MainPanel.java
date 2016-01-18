package me.dblab.client.layout;

import me.dblab.client.clientcontroller.ClientController;
import me.dblab.client.misc.MessageListener;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel implements MessageListener {
    protected ControlPanel controlPanel;
    protected DatabasePanel databasePanel;
    protected ClientController controller;
    protected JFrame mainFrame;

    public MainPanel(JFrame frame, ClientController controller) {
        mainFrame = frame;
        setLayout(new BorderLayout());

        this.controller = controller;
        controller.addMessageListener(this);

        setUpUI();
    }

    private void setUpUI() {
        controlPanel = new ControlPanel(controller);
        databasePanel = new DatabasePanel(controller);

        add(controlPanel, BorderLayout.PAGE_START);
        add(databasePanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(7000, 7000));

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Data Base");

        menuBar.add(menu);

        JMenuItem menuItemNew = new JMenuItem("New");
        JMenuItem menuItemLoad = new JMenuItem("Open");
        JMenuItem menuItemSave = new JMenuItem("Save");
        JMenuItem menuItemClose = new JMenuItem("Close");

        menuItemNew.addActionListener(actionEvent -> controller.newDatabase());
        menuItemLoad.addActionListener(actionEvent -> controller.loadDatabase());
        menuItemSave.addActionListener(actionEvent -> controller.saveDatabase());
        menuItemClose.addActionListener(actionEvent -> controller.closeDatabase());

        menu.add(menuItemNew);
        menu.add(menuItemLoad);
        menu.add(menuItemSave);
        menu.add(menuItemClose);

        mainFrame.setJMenuBar(menuBar);


        controller.notifyUpdateListeners();
    }

    @Override
    public void handleMessage(String message) {
        JOptionPane.showMessageDialog(
            null,
            message,
            "Message Dialog",
            JOptionPane.INFORMATION_MESSAGE,
            null
        );
    }
}