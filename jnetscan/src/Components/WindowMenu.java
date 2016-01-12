package Components;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.beans.*;

/*
*  WindowMenu.java
*  Article : Conquer Swing deficiencies in MDI development
*  Autur : Gerald Nunn, JavaWorld.com, 05/25/01
*  URL : http://www.javaworld.com/javaworld/jw-05-2001/jw-0525-mdi.html
*  Source code : http://www.javaworld.com/jw-05-2001/mdi/jw-0525-mdi.zip
*/

/**
 *
 * @author Gerald Nunn
 */

public class WindowMenu extends JMenu {
    private JDesktopPane desktop;
    private JMenuItem cascade=new JMenuItem("Cascade");
    private JMenuItem tile=new JMenuItem("Tile");

    public WindowMenu(JDesktopPane desktop) {
        this.desktop=desktop;
        setText("Window");
        cascade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                cascadeFrames();
            }
        });
        tile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                tileFrames();
            }
        });
        addMenuListener(new MenuListener() {
            public void menuCanceled (MenuEvent e) {}

            public void menuDeselected (MenuEvent e) {
                removeAll();
            }

            public void menuSelected (MenuEvent e) {
                buildChildMenus();
            }
        });
    }

    /** Sets up the children menus depending on the current desktop state */
    private void buildChildMenus() {
        int i;
        ChildMenuItem menu;
        JInternalFrame[] array = desktop.getAllFrames();

        add(cascade);
        add(tile);
        if (array.length > 0) addSeparator();
        cascade.setEnabled(array.length > 0);
        tile.setEnabled(array.length > 0);

        for (i = 0; i < array.length; i++) {
            menu = new ChildMenuItem(array[i]);
            menu.setState(i == 0);
            menu.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    JInternalFrame frame = ((ChildMenuItem)ae.getSource()).getFrame();
                    frame.moveToFront();
                    try {
                        frame.setSelected(true);
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                }
            });
            menu.setIcon(array[i].getFrameIcon());
            add(menu);
        }
    }

    /** This JCheckBoxMenuItem descendant is used to track the child frame that corresponds to a give menu. */
    class ChildMenuItem extends JCheckBoxMenuItem {
        private JInternalFrame frame;

        public ChildMenuItem(JInternalFrame frame) {
            super(frame.getTitle());
            this.frame=frame;
        }

        public JInternalFrame getFrame() {
            return frame;
        }
    }
    
 public void setAllSize(Dimension d){
        desktop.setMinimumSize(d);
        desktop.setMaximumSize(d);
        desktop.setPreferredSize(d);
    }
    
    public void setNormalSize() {
        JScrollPane scrollPane=getScrollPane();
        int x = 0;
        int y = 0;
        Insets scrollInsets = getScrollPaneInsets();

        if (scrollPane != null) {
            Dimension d = scrollPane.getVisibleRect().getSize();
            if (scrollPane.getBorder() != null) {
               d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right,
                         d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            d.setSize(d.getWidth() - 20, d.getHeight() - 20);
            setAllSize(new Dimension(x,y));
            scrollPane.invalidate();
            scrollPane.validate();
        }
    }
    
    public void cascadeFrames() {
        int x = 0;
        int y = 0;
        JInternalFrame allFrames[] = desktop.getAllFrames();

        setNormalSize();
        int frameHeight = (desktop.getBounds().height - 5) - allFrames.length * 20;
        int frameWidth = (desktop.getBounds().width - 5) - allFrames.length * 20;
        for (int i = allFrames.length - 1; i >= 0; i--) {
            allFrames[i].setSize(frameWidth,frameHeight);
            allFrames[i].setLocation(x,y);
            x = x + 20;
            y = y + 20;
        }
    }
    
    public void tileFrames() {
        java.awt.Component allFrames[] = desktop.getAllFrames();
        setNormalSize();
        int frameHeight = desktop.getBounds().height/ allFrames.length;
        int y = 0;
        for (int i = 0; i < allFrames.length; i++) {
            allFrames[i].setSize(desktop.getBounds().width, frameHeight);
            allFrames[i].setLocation(0, y);
            y = y + frameHeight;
        }
    }

    private Insets getScrollPaneInsets() {
        JScrollPane scrollPane=getScrollPane();
        if (scrollPane==null) return new Insets(0,0,0,0);
        else return getScrollPane().getBorder().getBorderInsets(scrollPane);
    }

    private JScrollPane getScrollPane() {
        if (desktop.getParent() instanceof JViewport) {
            JViewport viewPort = (JViewport)desktop.getParent();
            if (viewPort.getParent() instanceof JScrollPane)
                return (JScrollPane)viewPort.getParent();
        }
        return null;
    }        
}
