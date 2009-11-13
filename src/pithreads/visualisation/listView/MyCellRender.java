/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.listView;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author mathurin
 */
public class MyCellRender extends JLabel implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof MsgLog)
                  {
                  setOpaque(true);

                  MsgLog msgLog = (MsgLog)value;

                  
                  if(isSelected){
                  msgLog.setSelected(true);
                  setBackground(Color.BLACK);
                  setForeground(Color.WHITE);
                  } 
                  else
                  {
                  msgLog.setSelected(false);
                  setForeground(msgLog.getColor());
                  setBackground(Color.WHITE);
                  }
                  setText(msgLog.getMessage());

            }
                  return this;
    }
      
}
