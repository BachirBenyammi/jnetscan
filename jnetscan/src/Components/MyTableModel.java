package Components;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/*
 * MyTableModel.java
 *
 * Created on Feb, 09 2008, 07:40
 */

/**
 *
 * @author benbac
 */

public class MyTableModel extends AbstractTableModel {
    
    private String[] columnNames = 
        {"IP Address", "Host Name", "Reachable", "Response Time", "Open Ports"};        
    
    private Object[][] data = {};
    
    Class[] types = new Class [] {
        java.lang.String.class, 
        java.lang.String.class, 
        java.lang.Boolean.class, 
        java.lang.Integer.class, 
        java.lang.String.class
    };
    
        public void InitialData (String NetAdr, ArrayList<Integer> list, int count)
        {
            data = new Object[count][columnNames.length];
            for (int i=count; i>0; i--)
                setValueAt(NetAdr + list.get(i-1), count - i, 0);            
        }
        
        public void clearData()
        {
           data = new Object [][]{}; 
        }
        
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
        
        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        
        @Override
        public Class getColumnClass(int col) {
            return types[col];        
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }    
