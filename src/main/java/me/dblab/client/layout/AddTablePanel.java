package me.dblab.client.layout;

import javax.swing.*;
import java.awt.*;

public class AddTablePanel extends JPanel{
    protected JButton AddColumn;
    protected JButton DeleteColumn;
    protected JTextField ColumnName;
    protected JComboBox<String> ColumnType;
    protected JList<String> ColumnList;
    protected DefaultListModel ColumnListModel;

    protected String[] supportedTypes;

    public AddTablePanel(String[] _supportedTypes){
        supportedTypes = _supportedTypes;

        ColumnListModel = new DefaultListModel();
        ColumnList = new JList<String>(ColumnListModel);

        ColumnName = new JTextField();
        ColumnName.setColumns(20);
        ColumnType = new JComboBox<String>(supportedTypes);
        AddColumn = new JButton("Add Column");
        AddColumn.addActionListener(e -> ColumnListModel.addElement(ColumnName.getText()+","+ColumnType.getSelectedItem().toString()));

        DeleteColumn = new JButton("Delete Selected Column");
        DeleteColumn.addActionListener(e -> ColumnListModel.remove(ColumnList.getSelectedIndex()));



        JPanel panelInput = new JPanel();
        panelInput.setLayout(new FlowLayout(FlowLayout.LEADING));


        panelInput.add(ColumnName);
        panelInput.add(ColumnType);
        panelInput.add(AddColumn);

        panelInput.add(new JLabel("  "));
        panelInput.add(DeleteColumn);


        setLayout(new BorderLayout());

        add(ColumnList, BorderLayout.CENTER);
        add(panelInput, BorderLayout.NORTH);
    }

    public String getScheme(){
        String result = "";
        Integer size = ColumnListModel.getSize();
        for(Integer i =0; i<size; i++){
            result += ColumnListModel.getElementAt(i);
            if(i+1 != size)
                result+=";";
        }

        return result;
    }
}
