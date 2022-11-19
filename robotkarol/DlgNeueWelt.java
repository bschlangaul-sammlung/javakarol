package robotkarol;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
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

public class DlgNeueWelt extends JDialog implements ActionListener {
   private final JPanel contentPanel = new JPanel();
   private final JPanel buttonPanel = new JPanel();
   private JSpinner spnBreite;
   private JSpinner spnHoehe;
   private JSpinner spnLaenge;
   private boolean istOkay = false;

   public DlgNeueWelt(KarolView owner) {
      super((Frame)owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(220, 230));
      this.setMinimumSize(new Dimension(220, 230));
      this.setTitle("Neue Welt");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgNeueWelt.class.getResource("/icons/Karol.gif")));
      this.setModal(true);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      this.pack();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public boolean showModal(int breite, int laenge, int hoehe) {
      this.spnBreite.getModel().setValue(new Integer(breite));
      this.spnLaenge.getModel().setValue(new Integer(laenge));
      this.spnHoehe.getModel().setValue(new Integer(hoehe));
      this.istOkay = false;
      this.setVisible(true);
      return this.istOkay;
   }

   public int getNeueBreite() {
      Integer i = (Integer)this.spnBreite.getModel().getValue();
      return i;
   }

   public int getNeueLaenge() {
      Integer i = (Integer)this.spnLaenge.getModel().getValue();
      return i;
   }

   public int getNeueHoehe() {
      Integer i = (Integer)this.spnHoehe.getModel().getValue();
      return i;
   }

   private void GUIAnlegen() {
      this.setBounds(100, 100, 279, 230);
      this.getContentPane().setLayout(new BorderLayout());
      this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.getContentPane().add(this.contentPanel, "Center");
      GridBagLayout gbl_contentPanel = new GridBagLayout();
      this.contentPanel.setLayout(gbl_contentPanel);
      JLabel lblUeberschrift = new JLabel("Ausdehnung der neuen Welt");
      lblUeberschrift.setHorizontalAlignment(2);
      lblUeberschrift.setFont(new Font("Tahoma", 0, 14));
      lblUeberschrift.setBorder(new EmptyBorder(0, 0, 0, 0));
      lblUeberschrift.setHorizontalTextPosition(0);
      lblUeberschrift.setPreferredSize(new Dimension(200, 20));
      lblUeberschrift.setMaximumSize(new Dimension(200, 20));
      lblUeberschrift.setMinimumSize(new Dimension(200, 20));
      GridBagConstraints gbc_lblUeberschrift = new GridBagConstraints();
      gbc_lblUeberschrift.gridwidth = 3;
      gbc_lblUeberschrift.insets = new Insets(10, 10, 10, 0);
      gbc_lblUeberschrift.anchor = 18;
      gbc_lblUeberschrift.gridx = 0;
      gbc_lblUeberschrift.gridy = 0;
      this.contentPanel.add(lblUeberschrift, gbc_lblUeberschrift);
      lblUeberschrift = new JLabel("Breite");
      lblUeberschrift.setFont(new Font("Tahoma", 0, 14));
      lblUeberschrift.setPreferredSize(new Dimension(40, 20));
      lblUeberschrift.setMinimumSize(new Dimension(40, 20));
      lblUeberschrift.setMaximumSize(new Dimension(40, 20));
      lblUeberschrift.setHorizontalTextPosition(0);
      lblUeberschrift.setBorder(new EmptyBorder(0, 0, 0, 0));
      lblUeberschrift.setHorizontalAlignment(0);
      gbc_lblUeberschrift = new GridBagConstraints();
      gbc_lblUeberschrift.insets = new Insets(5, 10, 10, 5);
      gbc_lblUeberschrift.anchor = 18;
      gbc_lblUeberschrift.gridx = 0;
      gbc_lblUeberschrift.gridy = 1;
      this.contentPanel.add(lblUeberschrift, gbc_lblUeberschrift);
      this.spnBreite = new JSpinner();
      this.spnBreite.setFont(new Font("Tahoma", 0, 14));
      this.spnBreite.setModel(new SpinnerNumberModel(5, 1, 100, 1));
      GridBagConstraints gbc_spinner = new GridBagConstraints();
      gbc_spinner.fill = 2;
      gbc_spinner.insets = new Insets(5, 5, 10, 5);
      gbc_spinner.gridx = 1;
      gbc_spinner.gridy = 1;
      this.contentPanel.add(this.spnBreite, gbc_spinner);
      Component rigidArea = Box.createRigidArea(new Dimension(5, 0));
      rigidArea.setPreferredSize(new Dimension(80, 0));
      rigidArea.setMinimumSize(new Dimension(40, 0));
      rigidArea.setMaximumSize(new Dimension(40, 0));
      gbc_lblUeberschrift = new GridBagConstraints();
      gbc_lblUeberschrift.anchor = 17;
      gbc_lblUeberschrift.insets = new Insets(0, 0, 5, 0);
      gbc_lblUeberschrift.gridx = 2;
      gbc_lblUeberschrift.gridy = 1;
      this.contentPanel.add(rigidArea, gbc_lblUeberschrift);
      lblUeberschrift = new JLabel("Höhe");
      lblUeberschrift.setPreferredSize(new Dimension(40, 20));
      lblUeberschrift.setMinimumSize(new Dimension(40, 20));
      lblUeberschrift.setMaximumSize(new Dimension(40, 20));
      lblUeberschrift.setHorizontalTextPosition(0);
      lblUeberschrift.setHorizontalAlignment(0);
      lblUeberschrift.setFont(new Font("Tahoma", 0, 14));
      lblUeberschrift.setBorder(new EmptyBorder(0, 0, 0, 0));
      gbc_lblUeberschrift = new GridBagConstraints();
      gbc_lblUeberschrift.anchor = 17;
      gbc_lblUeberschrift.insets = new Insets(5, 10, 10, 5);
      gbc_lblUeberschrift.gridx = 0;
      gbc_lblUeberschrift.gridy = 3;
      this.contentPanel.add(lblUeberschrift, gbc_lblUeberschrift);
      this.spnHoehe = new JSpinner();
      this.spnHoehe.setFont(new Font("Tahoma", 0, 14));
      this.spnHoehe.setModel(new SpinnerNumberModel(6, 1, 10, 1));
      GridBagConstraints gbc_spnHoehe = new GridBagConstraints();
      gbc_spnHoehe.fill = 2;
      gbc_spnHoehe.insets = new Insets(5, 5, 10, 5);
      gbc_spnHoehe.gridx = 1;
      gbc_spnHoehe.gridy = 3;
      this.contentPanel.add(this.spnHoehe, gbc_spnHoehe);
      lblUeberschrift = new JLabel("Länge");
      lblUeberschrift.setFont(new Font("Tahoma", 0, 14));
      lblUeberschrift.setPreferredSize(new Dimension(40, 20));
      lblUeberschrift.setMinimumSize(new Dimension(40, 20));
      lblUeberschrift.setMaximumSize(new Dimension(40, 20));
      lblUeberschrift.setHorizontalTextPosition(0);
      lblUeberschrift.setHorizontalAlignment(0);
      lblUeberschrift.setBorder(new EmptyBorder(0, 0, 0, 0));
      gbc_lblUeberschrift = new GridBagConstraints();
      gbc_lblUeberschrift.anchor = 18;
      gbc_lblUeberschrift.insets = new Insets(5, 10, 10, 5);
      gbc_lblUeberschrift.gridx = 0;
      gbc_lblUeberschrift.gridy = 2;
      this.contentPanel.add(lblUeberschrift, gbc_lblUeberschrift);
      this.spnLaenge = new JSpinner();
      this.spnLaenge.setFont(new Font("Tahoma", 0, 14));
      this.spnLaenge.setModel(new SpinnerNumberModel(10, 1, 100, 1));
      GridBagConstraints gbc_spnLaenge = new GridBagConstraints();
      gbc_spnLaenge.fill = 2;
      gbc_spnLaenge.insets = new Insets(5, 5, 10, 5);
      gbc_spnLaenge.gridx = 1;
      gbc_spnLaenge.gridy = 2;
      this.contentPanel.add(this.spnLaenge, gbc_spnLaenge);
      this.buttonPanel.setPreferredSize(new Dimension(10, 33));
      this.buttonPanel.setFocusTraversalKeysEnabled(false);
      this.buttonPanel.setEnabled(false);
      this.buttonPanel.setBorder(null);
      this.getContentPane().add(this.buttonPanel, "South");
      this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 0));
      Component rigidAreax = Box.createRigidArea(new Dimension(5, 0));
      rigidAreax.setPreferredSize(new Dimension(10, 0));
      rigidAreax.setMinimumSize(new Dimension(10, 0));
      rigidAreax.setMaximumSize(new Dimension(10, 0));
      this.buttonPanel.add(rigidAreax);
      JButton okButton = new JButton("OK");
      okButton.setFont(new Font("Tahoma", 0, 14));
      okButton.setAlignmentY(0.0F);
      okButton.setContentAreaFilled(false);
      okButton.setBorder(new BevelBorder(0, null, null, null, null));
      okButton.setPreferredSize(new Dimension(85, 23));
      okButton.setMinimumSize(new Dimension(85, 23));
      okButton.setMaximumSize(new Dimension(85, 23));
      okButton.addActionListener(this);
      okButton.setActionCommand("OK");
      this.buttonPanel.add(okButton);
      this.getRootPane().setDefaultButton(okButton);
      Component rigidAreaxx = Box.createRigidArea(new Dimension(5, 0));
      rigidAreaxx.setPreferredSize(new Dimension(20, 0));
      rigidAreaxx.setMinimumSize(new Dimension(20, 0));
      rigidAreaxx.setMaximumSize(new Dimension(20, 0));
      this.buttonPanel.add(rigidAreaxx);
      JButton cancelButton = new JButton("Abbrechen");
      cancelButton.setMaximumSize(new Dimension(86, 23));
      cancelButton.setFont(new Font("Tahoma", 0, 14));
      cancelButton.setAlignmentY(0.0F);
      cancelButton.setMinimumSize(new Dimension(86, 23));
      cancelButton.setPreferredSize(new Dimension(86, 23));
      cancelButton.setContentAreaFilled(false);
      cancelButton.setBorder(new BevelBorder(0, null, null, null, null));
      cancelButton.addActionListener(this);
      cancelButton.setActionCommand("Cancel");
      this.buttonPanel.add(cancelButton);
      Component rigidAreaxxx = Box.createRigidArea(new Dimension(5, 0));
      rigidAreaxxx.setPreferredSize(new Dimension(10, 0));
      rigidAreaxxx.setMinimumSize(new Dimension(10, 0));
      rigidAreaxxx.setMaximumSize(new Dimension(10, 0));
      this.buttonPanel.add(rigidAreaxxx);
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      this.setVisible(false);
      this.istOkay = ae.getActionCommand() == "OK";
   }
}
