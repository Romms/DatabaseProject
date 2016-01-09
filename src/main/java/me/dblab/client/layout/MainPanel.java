package me.dblab.client.layout;

import me.dblab.client.clientcontroller.ClientController;
import me.dblab.client.misc.MessageListener;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel implements MessageListener {
    protected ControlPanel controlPanel;
    protected DatabasePanel databasePanel;
    protected ClientController controller;

    public MainPanel(ClientController controller) {
        super(new BorderLayout());

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