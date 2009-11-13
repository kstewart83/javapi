/*
 * MainFrame.java
 *
 * Created on 30. Juli 2008, 16:18
 */
package pithreads.visualisation;

import com.sun.opengl.util.Animator;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import pithreads.visualisation.listView.MyCellRender;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.model.Model;
import pithreads.visualisation.view.Renderer;
import pithreads.visualisation.visuEvent.NewFileToParseEnteredEvent;

/**
 *
 * @author cylab
 * @author mbien
 */
public class MainFrame extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    static {
        // When using a GLCanvas, we have to set the Popup-Menues to be HeavyWeight,
        // so they display properly on top of the GLCanvas
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }
    private Animator animator;
    private Renderer glRenderer;
    private IModel model;
    private ApplicationController applicationController;

    /** Creates new form MainFrame */
    public MainFrame(ApplicationController c) {
        //on initialise les composants
        initComponents();
        //on creer le model vide
        this.model = c.getModel();
        setTitle("PiDebug");
        this.applicationController = c;
        //on cree le renderer
        glRenderer = new Renderer();
        //on cree le transcripteur d'evenement de la vue du canvas


        canvas.addGLEventListener(glRenderer);

        animator = new Animator(canvas);

        listOfTreads.setModel(new DefaultListModel());
        listOfTreads.setCellRenderer(new MyCellRender());


        listOfChannels.setModel(new DefaultListModel());
        listOfChannels.setCellRenderer(new MyCellRender());

        listOfChannelInformations.setModel(new DefaultListModel());
        listOfChannelInformations.setCellRenderer(new MyCellRender());

        listOfPiThreadInformations.setModel(new DefaultListModel());
        listOfPiThreadInformations.setCellRenderer(new MyCellRender());

        listOfLogs.setModel(new DefaultListModel());
        listOfLogs.setCellRenderer(new MyCellRender());

        // This is a workaround for the GLCanvas not adjusting its size, when the frame is resized.
        canvas.setMinimumSize(new Dimension());

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

    }

    public JList getListOfPiThreads() {
        return this.listOfTreads;
    }

    public JList getListOfPiChannels() {
        return this.listOfChannels;
    }

    public JList getListOfPiChannelsInformations() {

        return this.listOfChannelInformations;
    }

    public JList getListOfPiThreadsInformations() {
        return this.listOfPiThreadInformations;
    }

    public Renderer getRenderer() {
        return this.glRenderer;
    }

    public GLCanvas getCanvas() {
        return this.canvas;
    }

    @Override
    public void setVisible(boolean show) {
        if (!show) {
            animator.stop();
        }
        super.setVisible(show);
        if (!show) {
            animator.start();
        }
    }

    JList getListOfLogs() {
        return this.listOfLogs;
    }

    void setFileParseSelected(File fileSelected) {
        applicationController.receive(new NewFileToParseEnteredEvent(this, fileSelected));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        canvas = new GLCanvas(createGLCapabilites());
        toolBarMenu = new JToolBar();
        to2DViewMenuButton = new JButton();
        to3DViewMenuButton = new JButton();
        jSeparator1 = new Separator();
        jLabel5 = new JLabel();
        sliderSpeed = new JSlider();
        jLabel6 = new JLabel();
        jScrollPane1 = new JScrollPane();
        listOfTreads = new JList();
        jScrollPane2 = new JScrollPane();
        listOfChannels = new JList();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jScrollPane3 = new JScrollPane();
        listOfPiThreadInformations = new JList();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jScrollPane4 = new JScrollPane();
        listOfChannelInformations = new JList();
        jScrollPane5 = new JScrollPane();
        listOfLogs = new JList();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        selectFileToParseMenu = new JMenuItem();
        jMenu2 = new JMenu();
        viewMenu = new JMenu();
        modeViewMenu = new JMenu();
        to2DModeViewCheckMenu = new JCheckBoxMenuItem();
        to3DModeViewCheckMenu = new JCheckBoxMenuItem();
        speedViewMenu = new JMenu();
        mainSpeedViewMenu = new JMenu();
        mainSpeedFastViewMenu = new JCheckBoxMenuItem();
        mainSpeedMediumViewMenu = new JCheckBoxMenuItem();
        mainSpeedLowViewMenu = new JCheckBoxMenuItem();
        movemnentSpeedViewMenu = new JMenu();
        movementSpeedFastViewMenu = new JCheckBoxMenuItem();
        movementSpeedMediumViewMenu = new JCheckBoxMenuItem();
        movementSpeedLowViewMenu = new JCheckBoxMenuItem();
        reOragnisationSpeedViewMenu = new JMenu();
        reOrganisationSpeedFastViewMenu = new JCheckBoxMenuItem();
        reOrganisationSpeedMediumViewMenu = new JCheckBoxMenuItem();
        reOrganisationSpeedLowViewMenu = new JCheckBoxMenuItem();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PiDebug");

        canvas.setRealized(false);

        toolBarMenu.setRollover(true);

        to2DViewMenuButton.setBackground(new Color(204, 204, 204));
        to2DViewMenuButton.setText("2D");
        to2DViewMenuButton.setFocusable(false);
        to2DViewMenuButton.setHorizontalTextPosition(SwingConstants.CENTER);
        to2DViewMenuButton.setSelected(true);
        to2DViewMenuButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        to2DViewMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                to2DViewMenuButtonActionPerformed(evt);
            }
        });
        toolBarMenu.add(to2DViewMenuButton);

        to3DViewMenuButton.setBackground(new Color(204, 204, 204));
        to3DViewMenuButton.setText("3D");
        to3DViewMenuButton.setFocusable(false);
        to3DViewMenuButton.setHorizontalTextPosition(SwingConstants.CENTER);
        to3DViewMenuButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        to3DViewMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                to3DViewMenuButtonActionPerformed(evt);
            }
        });
        toolBarMenu.add(to3DViewMenuButton);
        toolBarMenu.add(jSeparator1);

        jLabel5.setText("lent  ");
        toolBarMenu.add(jLabel5);

        sliderSpeed.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                sliderSpeedMouseDragged(evt);
            }
        });
        toolBarMenu.add(sliderSpeed);

        jLabel6.setText("  vite");
        toolBarMenu.add(jLabel6);

        jScrollPane1.setViewportView(listOfTreads);

        jScrollPane2.setViewportView(listOfChannels);

        jLabel1.setText("Liste des PiChannels :");

        jLabel2.setText("Liste des PiThreads :");

        jScrollPane3.setViewportView(listOfPiThreadInformations);

        jLabel3.setText("Informations sur le Thread sélectionné :");

        jLabel4.setText("Informations sur le Channel sélectionné :");

        jScrollPane4.setViewportView(listOfChannelInformations);

        jScrollPane5.setViewportView(listOfLogs);

        fileMenu.setText("Fichier");

        selectFileToParseMenu.setText("Ouvrir un fichier \"execution\"");
        selectFileToParseMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                selectFileToParseMenuActionPerformed(evt);
            }
        });
        fileMenu.add(selectFileToParseMenu);

        menuBar.add(fileMenu);

        jMenu2.setText("Edition");
        menuBar.add(jMenu2);

        viewMenu.setText("Affichage");

        modeViewMenu.setText("mode de vue");

        to2DModeViewCheckMenu.setSelected(true);
        to2DModeViewCheckMenu.setText("vue 2D");
        to2DModeViewCheckMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                to2DModeViewCheckMenuActionPerformed(evt);
            }
        });
        modeViewMenu.add(to2DModeViewCheckMenu);

        to3DModeViewCheckMenu.setText("vue 3D");
        to3DModeViewCheckMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                to3DModeViewCheckMenuActionPerformed(evt);
            }
        });
        modeViewMenu.add(to3DModeViewCheckMenu);

        viewMenu.add(modeViewMenu);

        speedViewMenu.setText("vitesse des elements de la vue");

        mainSpeedViewMenu.setText("vitesse générale d'affichage");

        mainSpeedFastViewMenu.setText("vitesse rapide");
        mainSpeedFastViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mainSpeedFastViewMenuActionPerformed(evt);
            }
        });
        mainSpeedViewMenu.add(mainSpeedFastViewMenu);

        mainSpeedMediumViewMenu.setSelected(true);
        mainSpeedMediumViewMenu.setText("vitesse moyen");
        mainSpeedMediumViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mainSpeedMediumViewMenuActionPerformed(evt);
            }
        });
        mainSpeedViewMenu.add(mainSpeedMediumViewMenu);

        mainSpeedLowViewMenu.setText("vitesse lent");
        mainSpeedLowViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                mainSpeedLowViewMenuActionPerformed(evt);
            }
        });
        mainSpeedViewMenu.add(mainSpeedLowViewMenu);

        speedViewMenu.add(mainSpeedViewMenu);

        movemnentSpeedViewMenu.setText("vitesse d'affichage des mouvements");

        movementSpeedFastViewMenu.setText("vitesse rapide");
        movementSpeedFastViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                movementSpeedFastViewMenuActionPerformed(evt);
            }
        });
        movemnentSpeedViewMenu.add(movementSpeedFastViewMenu);

        movementSpeedMediumViewMenu.setSelected(true);
        movementSpeedMediumViewMenu.setText("vitesse moyenne");
        movementSpeedMediumViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                movementSpeedMediumViewMenuActionPerformed(evt);
            }
        });
        movemnentSpeedViewMenu.add(movementSpeedMediumViewMenu);

        movementSpeedLowViewMenu.setText("vitesse lente");
        movementSpeedLowViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                movementSpeedLowViewMenuActionPerformed(evt);
            }
        });
        movemnentSpeedViewMenu.add(movementSpeedLowViewMenu);

        speedViewMenu.add(movemnentSpeedViewMenu);

        reOragnisationSpeedViewMenu.setText("vitesse d'affichage des ré-organisations spaciales");

        reOrganisationSpeedFastViewMenu.setText("vitesse rapide");
        reOrganisationSpeedFastViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reOrganisationSpeedFastViewMenuActionPerformed(evt);
            }
        });
        reOragnisationSpeedViewMenu.add(reOrganisationSpeedFastViewMenu);

        reOrganisationSpeedMediumViewMenu.setSelected(true);
        reOrganisationSpeedMediumViewMenu.setText("vitesse moyenne");
        reOrganisationSpeedMediumViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reOrganisationSpeedMediumViewMenuActionPerformed(evt);
            }
        });
        reOragnisationSpeedViewMenu.add(reOrganisationSpeedMediumViewMenu);

        reOrganisationSpeedLowViewMenu.setText("vitesse lente");
        reOrganisationSpeedLowViewMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reOrganisationSpeedLowViewMenuActionPerformed(evt);
            }
        });
        reOragnisationSpeedViewMenu.add(reOrganisationSpeedLowViewMenu);

        speedViewMenu.add(reOragnisationSpeedViewMenu);

        viewMenu.add(speedViewMenu);

        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(canvas, GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, GroupLayout.DEFAULT_SIZE, 919, Short.MAX_VALUE))
                    .addComponent(toolBarMenu, GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(70, 70, 70))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel2))
                    .addComponent(toolBarMenu, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(ComponentPlacement.RELATED))
                    .addGroup(Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(canvas, GroupLayout.PREFERRED_SIZE, 494, GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)))
                .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, 0, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void to3DViewMenuButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_to3DViewMenuButtonActionPerformed
        // TODO add your handling code here:

        if (!to3DViewMenuButton.isSelected()) {
            to3DViewMenuButton.setSelected(true);
            to3DModeViewCheckMenu.setSelected(true);
            to2DViewMenuButton.setSelected(false);
            to2DModeViewCheckMenu.setSelected(false);
            glRenderer.setMode(1);
        }
    }//GEN-LAST:event_to3DViewMenuButtonActionPerformed

    private void to2DViewMenuButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_to2DViewMenuButtonActionPerformed
        // TODO add your handling code here:
        if (!to2DViewMenuButton.isSelected()) {
            to2DViewMenuButton.setSelected(true);
            to2DModeViewCheckMenu.setSelected(true);
            to3DViewMenuButton.setSelected(false);
            to3DModeViewCheckMenu.setSelected(false);
            glRenderer.setMode(0);
        }
    }//GEN-LAST:event_to2DViewMenuButtonActionPerformed

    private void to2DModeViewCheckMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_to2DModeViewCheckMenuActionPerformed
        // TODO add your handling code here:
        if (to2DModeViewCheckMenu.isSelected()) {
            to2DViewMenuButton.setSelected(true);
            to2DModeViewCheckMenu.setSelected(true);
            to3DViewMenuButton.setSelected(false);
            to3DModeViewCheckMenu.setSelected(false);
            System.out.println("ici 2D");
            glRenderer.setMode(0);
        }
    }//GEN-LAST:event_to2DModeViewCheckMenuActionPerformed

    private void to3DModeViewCheckMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_to3DModeViewCheckMenuActionPerformed
        // TODO add your handling code here:

        if (to3DModeViewCheckMenu.isSelected()) {
            to3DViewMenuButton.setSelected(true);
            to3DModeViewCheckMenu.setSelected(true);
            to2DViewMenuButton.setSelected(false);
            to2DModeViewCheckMenu.setSelected(false);
            System.out.println("ici 3D");
            glRenderer.setMode(1);
        }
    }//GEN-LAST:event_to3DModeViewCheckMenuActionPerformed

    private void mainSpeedMediumViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_mainSpeedMediumViewMenuActionPerformed
        // TODO add your handling code here:
        model.setMilliSpeed(600);
        mainSpeedLowViewMenu.setSelected(false);
        mainSpeedFastViewMenu.setSelected(false);
        mainSpeedMediumViewMenu.setSelected(true);
    }//GEN-LAST:event_mainSpeedMediumViewMenuActionPerformed

    private void mainSpeedLowViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_mainSpeedLowViewMenuActionPerformed
        // TODO add your handling code here:
        model.setMilliSpeed(1200);
        mainSpeedLowViewMenu.setSelected(true);
        mainSpeedFastViewMenu.setSelected(false);
        mainSpeedMediumViewMenu.setSelected(false);
    }//GEN-LAST:event_mainSpeedLowViewMenuActionPerformed

    private void mainSpeedFastViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_mainSpeedFastViewMenuActionPerformed
        // TODO add your handling code here:
        model.setMilliSpeed(250);
        mainSpeedLowViewMenu.setSelected(false);
        mainSpeedFastViewMenu.setSelected(true);
        mainSpeedMediumViewMenu.setSelected(false);
    }//GEN-LAST:event_mainSpeedFastViewMenuActionPerformed

    private void movementSpeedFastViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_movementSpeedFastViewMenuActionPerformed
        // TODO add your handling code here:
        model.setMovementSpeed(5000);
        movementSpeedFastViewMenu.setSelected(true);
        movementSpeedLowViewMenu.setSelected(false);
        movementSpeedMediumViewMenu.setSelected(false);
    }//GEN-LAST:event_movementSpeedFastViewMenuActionPerformed

    private void movementSpeedMediumViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_movementSpeedMediumViewMenuActionPerformed
        // TODO add your handling code here:
        model.setMovementSpeed(10000);
        movementSpeedFastViewMenu.setSelected(false);
        movementSpeedLowViewMenu.setSelected(false);
        movementSpeedMediumViewMenu.setSelected(true);
    }//GEN-LAST:event_movementSpeedMediumViewMenuActionPerformed

    private void movementSpeedLowViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_movementSpeedLowViewMenuActionPerformed
        // TODO add your handling code here:
        model.setMovementSpeed(25000);
        movementSpeedFastViewMenu.setSelected(false);
        movementSpeedLowViewMenu.setSelected(true);
        movementSpeedMediumViewMenu.setSelected(false);
    }//GEN-LAST:event_movementSpeedLowViewMenuActionPerformed

    private void selectFileToParseMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_selectFileToParseMenuActionPerformed
        OpenDialog dialog = new OpenDialog(this);

        dialog.setVisible(true);
        dialog.setAlwaysOnTop(true);
    // TODO add your handling code here:
    }//GEN-LAST:event_selectFileToParseMenuActionPerformed

    private void reOrganisationSpeedFastViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_reOrganisationSpeedFastViewMenuActionPerformed
        applicationController.getModel().setReOrganisationSpeed(1);
        reOrganisationSpeedFastViewMenu.setSelected(true);
        reOrganisationSpeedLowViewMenu.setSelected(false);
        reOrganisationSpeedMediumViewMenu.setSelected(false);

    }//GEN-LAST:event_reOrganisationSpeedFastViewMenuActionPerformed

    private void reOrganisationSpeedMediumViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_reOrganisationSpeedMediumViewMenuActionPerformed
        applicationController.getModel().setReOrganisationSpeed(5);
        reOrganisationSpeedMediumViewMenu.setSelected(true);
        reOrganisationSpeedFastViewMenu.setSelected(false);
        reOrganisationSpeedLowViewMenu.setSelected(false);
    }//GEN-LAST:event_reOrganisationSpeedMediumViewMenuActionPerformed

    private void reOrganisationSpeedLowViewMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_reOrganisationSpeedLowViewMenuActionPerformed
        applicationController.getModel().setReOrganisationSpeed(10);
        reOrganisationSpeedLowViewMenu.setSelected(true);
        reOrganisationSpeedFastViewMenu.setSelected(false);
        reOrganisationSpeedMediumViewMenu.setSelected(false);
    }//GEN-LAST:event_reOrganisationSpeedLowViewMenuActionPerformed

    private void sliderSpeedMouseDragged(MouseEvent evt) {//GEN-FIRST:event_sliderSpeedMouseDragged
    
        int value =101 - sliderSpeed.getModel().getValue() ;
        if(value<=50){
        applicationController.getModel().setReOrganisationSpeed(value/10);
        applicationController.getModel().setMovementSpeed(200*value);
        applicationController.getModel().setMilliSpeed(12*value);
        }
        else
        {
        applicationController.getModel().setReOrganisationSpeed(value/10);
        applicationController.getModel().setMovementSpeed(200*value*2);
        applicationController.getModel().setMilliSpeed(12*value);
        }
        //TODO : comment sortir d'un sleep??
        //applicationController.receive(new ResumeEvent);
        //System.out.println("reOrganisationSpeed= "+reOrganisationSpeed+"  globalSpeed="+   globalSpeed+"    movementSpeed="+movementSpeed);
        
        
    }//GEN-LAST:event_sliderSpeedMouseDragged

    /**
     * Called from within initComponents().
     * hint: to customize the generated code choose 'Customize Code' in the contextmenu
     * of the selected UI Component you wish to cutomize in design mode.
     * @return Returns customized GLCapabilities.
     */
    private GLCapabilities createGLCapabilites() {

        GLCapabilities capabilities = new GLCapabilities();
        capabilities.setHardwareAccelerated(true);

        // try to enable 2x anti aliasing - should be supported on most hardware
        capabilities.setNumSamples(2);
        capabilities.setSampleBuffers(true);

        return capabilities;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GLCanvas canvas;
    private JMenu fileMenu;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JMenu jMenu2;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private JScrollPane jScrollPane5;
    private Separator jSeparator1;
    private JList listOfChannelInformations;
    private JList listOfChannels;
    private JList listOfLogs;
    private JList listOfPiThreadInformations;
    private JList listOfTreads;
    private JCheckBoxMenuItem mainSpeedFastViewMenu;
    private JCheckBoxMenuItem mainSpeedLowViewMenu;
    private JCheckBoxMenuItem mainSpeedMediumViewMenu;
    private JMenu mainSpeedViewMenu;
    private JMenuBar menuBar;
    private JMenu modeViewMenu;
    private JCheckBoxMenuItem movementSpeedFastViewMenu;
    private JCheckBoxMenuItem movementSpeedLowViewMenu;
    private JCheckBoxMenuItem movementSpeedMediumViewMenu;
    private JMenu movemnentSpeedViewMenu;
    private JMenu reOragnisationSpeedViewMenu;
    private JCheckBoxMenuItem reOrganisationSpeedFastViewMenu;
    private JCheckBoxMenuItem reOrganisationSpeedLowViewMenu;
    private JCheckBoxMenuItem reOrganisationSpeedMediumViewMenu;
    private JMenuItem selectFileToParseMenu;
    private JSlider sliderSpeed;
    private JMenu speedViewMenu;
    private JCheckBoxMenuItem to2DModeViewCheckMenu;
    private JButton to2DViewMenuButton;
    private JCheckBoxMenuItem to3DModeViewCheckMenu;
    private JButton to3DViewMenuButton;
    private JToolBar toolBarMenu;
    private JMenu viewMenu;
    // End of variables declaration//GEN-END:variables
    private float posXMouse;
    private float posYMouse;
    private int mouseNumButton;

    public void mouseDragged(MouseEvent arg0) {
        float moveX = (float) ((arg0.getPoint().x - posXMouse) / 100.0);
        float moveY = (float) ((arg0.getPoint().y - posYMouse) / 100.0);
        if (mouseNumButton == 1) {
            this.glRenderer.getCamera().setPosX(glRenderer.getCamera().getPosX() + moveX);
            this.glRenderer.getCamera().setPosY(glRenderer.getCamera().getPosY() - moveY);
            posXMouse = (float) arg0.getPoint().x;
            posYMouse = (float) arg0.getPoint().y;
        }
        if (mouseNumButton == 3) {
            this.glRenderer.getCamera().setAngleY(glRenderer.getCamera().getAngleY() + (moveX * 20));
            this.glRenderer.getCamera().setAngleX(glRenderer.getCamera().getAngleX() - (moveY * 20));
            posXMouse = (float) arg0.getPoint().x;
            posYMouse = (float) arg0.getPoint().y;
        }
        this.glRenderer.getDrawable().repaint();
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
        posXMouse = arg0.getPoint().x;
        posYMouse = arg0.getPoint().y;
        mouseNumButton = arg0.getButton();
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseWheelMoved(MouseWheelEvent arg0) {
        int wheelCounter = arg0.getWheelRotation();
        if (wheelCounter != 0) {
            this.glRenderer.getCamera().setPosZ(glRenderer.getCamera().getPosZ() - wheelCounter);
        }
        this.glRenderer.getDrawable().repaint();
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyChar() == ' ') {
            this.glRenderer.getCamera().resetCamera();
            this.glRenderer.getDrawable().repaint();
        }
        if (arg0.getKeyChar() == 'p') {
            //this.glRenderer.setVitesse(0);
            //this.glRenderer.activate();
            this.glRenderer.getDrawable().repaint();
        }
        if (arg0.VK_UP == arg0.getKeyCode()) {
            // this.glRenderer.activate();
            //this.glRenderer.incrVitesse();
        }
        if (arg0.VK_DOWN == arg0.getKeyCode()) {
            //this.glRenderer.decrVitesse();
        }
    }

    public void keyReleased(KeyEvent arg0) {
    }
}
