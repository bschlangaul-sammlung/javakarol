package karoleditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class DlgGeheZu extends JDialog implements ActionListener {
   private final JPanel contentPanel = new JPanel();
   private final JPanel buttonPanel = new JPanel();
   private JSpinner spnZeile;
   private boolean istOkay = false;

   public DlgGeheZu(Frame owner) {
      super(owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(220, 100));
      this.setTitle("Gehe zu");
      this.setModal(true);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public boolean showModal(int aktZeile, int maxZeile) {
      SpinnerNumberModel spMod = (SpinnerNumberModel)this.spnZeile.getModel();
      spMod.setMaximum(maxZeile);
      spMod.setValue(new Integer(aktZeile));
      this.istOkay = false;
      this.setVisible(true);
      return this.istOkay;
   }

   public int getNeueZeile() {
      Integer i = (Integer)this.spnZeile.getModel().getValue();
      return i;
   }

   private void GUIAnlegen() {
      this.setBounds(100, 100, 220, 107);
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(this.contentPanel, "North");
      this.contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      this.contentPanel.setLayout(new BoxLayout(this.contentPanel, 0));
      Component rigidArea = Box.createRigidArea(new Dimension(5, 0));
      this.contentPanel.add(rigidArea);
      JLabel lblUeberschrift = new JLabel("Setze den Cursor in Zeile");
      lblUeberschrift.setAlignmentX(0.5F);
      lblUeberschrift.setHorizontalAlignment(2);
      lblUeberschrift.setFont(new Font("Tahoma", 0, 11));
      lblUeberschrift.setHorizontalTextPosition(0);
      this.contentPanel.add(lblUeberschrift);
      rigidArea = Box.createRigidArea(new Dimension(5, 5));
      this.contentPanel.add(rigidArea);
      this.spnZeile = new JSpinner();
      this.spnZeile.setFont(new Font("Tahoma", 0, 14));
      this.spnZeile.setModel(new SpinnerNumberModel(5, 1, 100, 1));
      this.contentPanel.add(this.spnZeile);
      this.getContentPane().add(this.buttonPanel, "South");
      this.buttonPanel.setPreferredSize(new Dimension(10, 33));
      this.buttonPanel.setFocusTraversalKeysEnabled(false);
      this.buttonPanel.setEnabled(false);
      this.buttonPanel.setBorder(null);
      this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 0));
      JButton okButton = new JButton("OK");
      okButton.setMaximumSize(new Dimension(86, 23));
      okButton.setMinimumSize(new Dimension(86, 23));
      okButton.setFont(new Font("Tahoma", 0, 11));
      okButton.setAlignmentY(0.0F);
      okButton.setContentAreaFilled(false);
      okButton.setBorder(new BevelBorder(0, null, null, null, null));
      okButton.setPreferredSize(new Dimension(86, 23));
      okButton.addActionListener(this);
      Component horizontalGlue = Box.createHorizontalGlue();
      this.buttonPanel.add(horizontalGlue);
      okButton.setActionCommand("OK");
      this.getRootPane().setDefaultButton(okButton);
      this.buttonPanel.add(okButton);
      rigidArea = Box.createRigidArea(new Dimension(5, 0));
      this.buttonPanel.add(rigidArea);
      JButton cancelButton = new JButton("Abbrechen");
      cancelButton.setMaximumSize(new Dimension(86, 23));
      cancelButton.setFont(new Font("Tahoma", 0, 11));
      cancelButton.setAlignmentY(0.0F);
      cancelButton.setMinimumSize(new Dimension(86, 23));
      cancelButton.setPreferredSize(new Dimension(86, 23));
      cancelButton.setContentAreaFilled(false);
      cancelButton.setBorder(new BevelBorder(0, null, null, null, null));
      cancelButton.addActionListener(this);
      cancelButton.setActionCommand("Cancel");
      this.buttonPanel.add(cancelButton);
      rigidArea = Box.createRigidArea(new Dimension(10, 0));
      this.buttonPanel.add(rigidArea);
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      this.setVisible(false);
      this.istOkay = ae.getActionCommand() == "OK";
   }
}
