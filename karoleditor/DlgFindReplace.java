package karoleditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class DlgFindReplace extends JDialog implements ActionListener {
   private final JPanel contentTopPanel = new JPanel();
   private final JPanel contentBottomPanel = new JPanel();
   private final JPanel buttonPanel = new JPanel();
   private JComboBox<String> cbSuchen;
   private JComboBox<String> cbErsetzen;
   private JCheckBox chkGrossKlein;
   private JCheckBox chkWort;
   private JCheckBox chkAbCursor;
   private JCheckBox chkMarkierung;
   private JRadioButton rbtVor;
   private JRadioButton rbtZurueck;
   private JLabel lblErsetzen;
   private boolean isOkay = false;
   private String searchText;
   private String replaceText;
   private boolean grossOption;
   private boolean wortOption;
   private boolean abCursorOption;
   private boolean nurMarkiertOption;
   private boolean vorwaertsOption;

   public DlgFindReplace(Frame owner) {
      super(owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(320, 260));
      this.setTitle("Text ersetzen");
      this.setModal(true);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public boolean showModal(boolean find) {
      this.isOkay = false;
      if (find) {
         this.setTitle("Text suchen");
         this.lblErsetzen.setEnabled(false);
         this.cbErsetzen.setEnabled(false);
         this.rbtVor.setEnabled(true);
         this.rbtZurueck.setEnabled(true);
         this.chkAbCursor.setSelected(true);
      } else {
         this.setTitle("Text ersetzen");
         this.lblErsetzen.setEnabled(true);
         this.cbErsetzen.setEnabled(true);
         this.rbtVor.setEnabled(false);
         this.rbtZurueck.setEnabled(false);
         this.rbtVor.setSelected(true);
         this.chkAbCursor.setSelected(false);
      }

      this.setVisible(true);
      return this.isOkay;
   }

   public boolean getGrossOption() {
      return this.grossOption;
   }

   public boolean getWortOption() {
      return this.wortOption;
   }

   public boolean getAbCursorOption() {
      return this.abCursorOption;
   }

   public boolean getNurMarkiertOption() {
      return this.nurMarkiertOption;
   }

   public boolean getVorwaertsOption() {
      return this.vorwaertsOption;
   }

   public String getSearchText() {
      return this.searchText;
   }

   public String getReplaceText() {
      return this.replaceText;
   }

   private void GUIAnlegen() {
      this.setBounds(100, 100, 320, 256);
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 1));
      this.contentTopPanel.setPreferredSize(new Dimension(150, 50));
      this.contentTopPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      GridBagLayout gbl_contentTopPanel = new GridBagLayout();
      gbl_contentTopPanel.rowWeights = new double[]{0.0, 0.0};
      gbl_contentTopPanel.columnWidths = new int[]{103, 144};
      gbl_contentTopPanel.columnWeights = new double[]{0.0, 1.0};
      this.contentTopPanel.setLayout(gbl_contentTopPanel);
      JLabel lblSuchen = new JLabel("Suchen nach:");
      lblSuchen.setFocusable(false);
      GridBagConstraints gbc_lblSuchen = new GridBagConstraints();
      gbc_lblSuchen.insets = new Insets(5, 5, 10, 5);
      gbc_lblSuchen.anchor = 17;
      gbc_lblSuchen.gridx = 0;
      gbc_lblSuchen.gridy = 0;
      this.contentTopPanel.add(lblSuchen, gbc_lblSuchen);
      this.cbSuchen = new JComboBox<>();
      this.cbSuchen.setPreferredSize(new Dimension(150, 20));
      this.cbSuchen.setEditable(true);
      this.cbSuchen.setMaximumRowCount(5);
      GridBagConstraints gbc_cbSuchen = new GridBagConstraints();
      gbc_cbSuchen.anchor = 17;
      gbc_cbSuchen.insets = new Insets(5, 0, 10, 5);
      gbc_cbSuchen.fill = 2;
      gbc_cbSuchen.gridx = 1;
      gbc_cbSuchen.gridy = 0;
      this.contentTopPanel.add(this.cbSuchen, gbc_cbSuchen);
      this.lblErsetzen = new JLabel("Ersetzen durch:");
      GridBagConstraints gbc_lblErsetzen = new GridBagConstraints();
      gbc_lblErsetzen.anchor = 17;
      gbc_lblErsetzen.insets = new Insets(5, 5, 10, 5);
      gbc_lblErsetzen.gridx = 0;
      gbc_lblErsetzen.gridy = 1;
      this.contentTopPanel.add(this.lblErsetzen, gbc_lblErsetzen);
      this.cbErsetzen = new JComboBox<>();
      this.cbErsetzen.setEditable(true);
      this.cbErsetzen.setMaximumRowCount(5);
      GridBagConstraints gbc_cbErsetzen = new GridBagConstraints();
      gbc_cbErsetzen.anchor = 17;
      gbc_cbErsetzen.insets = new Insets(5, 0, 10, 5);
      gbc_cbErsetzen.fill = 2;
      gbc_cbErsetzen.gridx = 1;
      gbc_cbErsetzen.gridy = 1;
      this.contentTopPanel.add(this.cbErsetzen, gbc_cbErsetzen);
      this.getContentPane().add(this.contentTopPanel);
      gbl_contentTopPanel = new GridBagLayout();
      gbl_contentTopPanel.columnWidths = new int[]{184, 129, 0};
      gbl_contentTopPanel.rowHeights = new int[]{121, 0};
      gbl_contentTopPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
      gbl_contentTopPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
      this.contentBottomPanel.setLayout(gbl_contentTopPanel);
      JPanel pnlOptionen = new JPanel();
      pnlOptionen.setBorder(new TitledBorder(null, "Optionen", 4, 2, null, null));
      gbc_lblSuchen = new GridBagConstraints();
      gbc_lblSuchen.fill = 2;
      gbc_lblSuchen.anchor = 18;
      gbc_lblSuchen.insets = new Insets(0, 5, 0, 5);
      gbc_lblSuchen.gridx = 0;
      gbc_lblSuchen.gridy = 0;
      this.contentBottomPanel.add(pnlOptionen, gbc_lblSuchen);
      pnlOptionen.setLayout(new GridLayout(0, 1, 0, 0));
      this.chkGrossKlein = new JCheckBox("Groß-/Kleinschreibung");
      pnlOptionen.add(this.chkGrossKlein);
      this.chkWort = new JCheckBox("Nur ganze Wörter");
      pnlOptionen.add(this.chkWort);
      this.chkAbCursor = new JCheckBox("Suchen ab Cursor");
      pnlOptionen.add(this.chkAbCursor);
      this.chkMarkierung = new JCheckBox("Nur markierter Text");
      pnlOptionen.add(this.chkMarkierung);
      JPanel pnlRichtung = new JPanel();
      pnlRichtung.setBorder(new TitledBorder(null, "Richtung", 4, 2, null, null));
      gbc_lblErsetzen = new GridBagConstraints();
      gbc_lblErsetzen.insets = new Insets(0, 0, 0, 5);
      gbc_lblErsetzen.fill = 2;
      gbc_lblErsetzen.anchor = 18;
      gbc_lblErsetzen.gridx = 1;
      gbc_lblErsetzen.gridy = 0;
      this.contentBottomPanel.add(pnlRichtung, gbc_lblErsetzen);
      pnlRichtung.setLayout(new GridLayout(0, 1, 0, 0));
      this.rbtVor = new JRadioButton("Vorwärts");
      this.rbtVor.setSelected(true);
      pnlRichtung.add(this.rbtVor);
      this.rbtZurueck = new JRadioButton("Rückwärts");
      this.rbtZurueck.setAlignmentX(0.5F);
      pnlRichtung.add(this.rbtZurueck);
      ButtonGroup gruppe = new ButtonGroup();
      gruppe.add(this.rbtVor);
      gruppe.add(this.rbtZurueck);
      this.contentBottomPanel.setPreferredSize(new Dimension(150, 80));
      this.getContentPane().add(this.contentBottomPanel);
      this.buttonPanel.setMinimumSize(new Dimension(150, 30));
      this.buttonPanel.setPreferredSize(new Dimension(150, 33));
      this.buttonPanel.setFocusTraversalKeysEnabled(false);
      this.buttonPanel.setEnabled(false);
      this.buttonPanel.setBorder(null);
      this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 0));
      this.buttonPanel.add(Box.createHorizontalGlue());
      JButton okButton = new JButton("OK");
      okButton.setContentAreaFilled(false);
      okButton.setBorder(new BevelBorder(0, null, null, null, null));
      okButton.setPreferredSize(new Dimension(85, 23));
      okButton.setMinimumSize(new Dimension(85, 23));
      okButton.setMaximumSize(new Dimension(85, 23));
      okButton.addActionListener(this);
      okButton.setActionCommand("OK");
      this.buttonPanel.add(okButton);
      this.getRootPane().setDefaultButton(okButton);
      Component rigidArea = Box.createRigidArea(new Dimension(5, 0));
      rigidArea.setPreferredSize(new Dimension(20, 0));
      rigidArea.setMinimumSize(new Dimension(20, 0));
      rigidArea.setMaximumSize(new Dimension(20, 0));
      this.buttonPanel.add(rigidArea);
      JButton cancelButton = new JButton("Abbrechen");
      cancelButton.setPreferredSize(new Dimension(86, 23));
      cancelButton.setMaximumSize(new Dimension(86, 23));
      cancelButton.setMinimumSize(new Dimension(86, 23));
      cancelButton.setContentAreaFilled(false);
      cancelButton.setBorder(new BevelBorder(0, null, null, null, null));
      cancelButton.addActionListener(this);
      cancelButton.setActionCommand("Cancel");
      this.buttonPanel.add(cancelButton);
      Component rigidArea2 = Box.createRigidArea(new Dimension(5, 0));
      rigidArea2.setPreferredSize(new Dimension(10, 0));
      rigidArea2.setMinimumSize(new Dimension(10, 0));
      rigidArea2.setMaximumSize(new Dimension(10, 0));
      this.buttonPanel.add(rigidArea2);
      this.getContentPane().add(this.buttonPanel);
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      this.setVisible(false);
      if (ae.getActionCommand() == "OK") {
         this.isOkay = true;
         this.searchText = (String)this.cbSuchen.getSelectedItem();
         if (!this.searchText.isEmpty()) {
            this.cbSuchen.setSelectedItem("");
            if (this.cbSuchen.getItemAt(0) == null || !this.searchText.equals(this.cbSuchen.getItemAt(0))) {
               if (this.cbSuchen.getItemCount() == 5) {
                  this.cbSuchen.removeItemAt(4);
               }

               this.cbSuchen.insertItemAt(this.searchText, 0);
            }
         }

         if (this.cbErsetzen.isEnabled()) {
            this.replaceText = (String)this.cbErsetzen.getSelectedItem();
            if (!this.replaceText.isEmpty()) {
               this.cbErsetzen.setSelectedItem("");
               if (this.cbErsetzen.getItemAt(0) == null || !this.replaceText.equals(this.cbErsetzen.getItemAt(0))) {
                  if (this.cbErsetzen.getItemCount() == 5) {
                     this.cbErsetzen.removeItemAt(4);
                  }

                  this.cbErsetzen.insertItemAt(this.replaceText, 0);
               }
            }
         } else {
            this.replaceText = "";
         }

         this.grossOption = this.chkGrossKlein.isSelected();
         this.wortOption = this.chkWort.isSelected();
         this.abCursorOption = this.chkAbCursor.isSelected();
         this.nurMarkiertOption = this.chkMarkierung.isSelected();
         this.vorwaertsOption = this.rbtVor.isSelected();
      }
   }
}
