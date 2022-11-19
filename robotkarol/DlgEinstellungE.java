package robotkarol;

import java.awt.Color;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import karoleditor.KEdtDocument;
import karoleditor.KEdtEditorKit;
import karoleditor.KEdtStyleContext;
import karoleditor.KEdtToken;

public class DlgEinstellungE extends JDialog implements ActionListener, MouseListener {
   JSpinner spnTab;
   JRadioButton rbtnurEinrueck;
   JRadioButton rbtSchreibw;
   private JTextField txtKEY;
   private JTextField txtKEYANW;
   private JTextField txtIDENTIFIER;
   private JTextField txtPARAMETER;
   private JTextField txtNUMBER;
   private JTextField txtKAROL;
   private JComboBox<String> cmbxSchrift;
   private JCheckBox ckbxZeilenDruck;
   private JCheckBox ckbxZeilen;
   private JCheckBox ckbxFarbe;
   private KarolView view;

   public DlgEinstellungE(Frame owner) {
      super(owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(420, 390));
      this.setTitle("Einstellungen Editor");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgEinstellungE.class.getResource("/icons/menu_editor.gif")));
      this.setModal(true);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public boolean showModal(KarolView vw) {
      this.view = vw;
      KEdtDocument doc = this.view.getProgDocument();
      KEdtEditorKit kit = (KEdtEditorKit)this.view.getProgTextPanel().getEditorKit();
      this.spnTab.getModel().setValue(kit.getTAB_LEN());
      String tempStr = " " + Integer.toString(doc.getFontsizeNormal());
      this.cmbxSchrift.setSelectedIndex(1);
      this.cmbxSchrift.setSelectedItem(tempStr);
      this.txtKEY.setBackground(new Color(doc.getStyleColor(KEdtToken.TokenType.KEY)));
      this.txtPARAMETER.setBackground(new Color(doc.getStyleColor(KEdtToken.TokenType.PNUMBER)));
      this.txtKEYANW.setBackground(new Color(doc.getStyleColor(KEdtToken.TokenType.KEYANW)));
      this.txtNUMBER.setBackground(new Color(doc.getStyleColor(KEdtToken.TokenType.NUMBER)));
      this.txtIDENTIFIER.setBackground(new Color(doc.getStyleColor(KEdtToken.TokenType.IDENTIFIER)));
      this.txtKAROL.setBackground(new Color(doc.getStyleColor(KEdtToken.TokenType.KAROL)));
      if (doc.getMitSchreibweise()) {
         this.rbtSchreibw.setSelected(true);
      } else {
         this.rbtnurEinrueck.setSelected(true);
      }

      this.ckbxZeilen.setSelected(this.view.getGutterPanel().getMitZeilennummer());
      this.ckbxZeilenDruck.setSelected(false);
      this.ckbxFarbe.setSelected(true);
      this.setVisible(true);
      return true;
   }

   private void GUIAnlegen() {
      this.setBounds(100, 100, 400, 430);
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 1));
      Component verticalStrut = Box.createVerticalStrut(5);
      verticalStrut.setPreferredSize(new Dimension(0, 5));
      this.getContentPane().add(verticalStrut);
      JPanel contentTabFont = new JPanel();
      contentTabFont.setMaximumSize(new Dimension(400, 32767));
      contentTabFont.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " ", 4, 2, new Font("Tahoma", 0, 11), null));
      this.getContentPane().add(contentTabFont);
      GridBagLayout gbl_contentTabFont = new GridBagLayout();
      gbl_contentTabFont.columnWidths = new int[5];
      gbl_contentTabFont.rowHeights = new int[1];
      gbl_contentTabFont.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0};
      gbl_contentTabFont.rowWeights = new double[]{0.0};
      contentTabFont.setLayout(gbl_contentTabFont);
      JLabel lblTab = new JLabel("Tabulatorabstand");
      lblTab.setFont(new Font("Tahoma", 0, 11));
      lblTab.setPreferredSize(new Dimension(90, 14));
      lblTab.setHorizontalAlignment(4);
      GridBagConstraints gbc_lblTab = new GridBagConstraints();
      gbc_lblTab.anchor = 17;
      gbc_lblTab.insets = new Insets(5, 8, 5, 5);
      gbc_lblTab.gridx = 0;
      gbc_lblTab.gridy = 0;
      contentTabFont.add(lblTab, gbc_lblTab);
      this.spnTab = new JSpinner();
      this.spnTab.setPreferredSize(new Dimension(40, 20));
      this.spnTab.setFont(new Font("Tahoma", 0, 11));
      this.spnTab.setModel(new SpinnerNumberModel(2, 1, 12, 1));
      GridBagConstraints gbc_spnTab = new GridBagConstraints();
      gbc_spnTab.insets = new Insets(0, 0, 0, 5);
      gbc_spnTab.anchor = 17;
      gbc_spnTab.gridx = 1;
      gbc_spnTab.gridy = 0;
      contentTabFont.add(this.spnTab, gbc_spnTab);
      JLabel lblNewLabel = new JLabel("Schriftgröße");
      lblNewLabel.setFont(new Font("Tahoma", 0, 11));
      lblNewLabel.setPreferredSize(new Dimension(95, 14));
      lblNewLabel.setHorizontalAlignment(4);
      GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
      gbc_lblNewLabel.anchor = 17;
      gbc_lblNewLabel.insets = new Insets(5, 32, 5, 5);
      gbc_lblNewLabel.gridx = 2;
      gbc_lblNewLabel.gridy = 0;
      contentTabFont.add(lblNewLabel, gbc_lblNewLabel);
      this.cmbxSchrift = new JComboBox<>();
      this.cmbxSchrift.setPreferredSize(new Dimension(46, 20));
      this.cmbxSchrift.setMinimumSize(new Dimension(40, 20));
      this.cmbxSchrift.setFont(new Font("Tahoma", 0, 11));
      this.cmbxSchrift.setModel(new DefaultComboBoxModel<>(new String[]{" 8", " 10", " 11", " 12", " 13", " 14", " 18", " 24"}));
      this.cmbxSchrift.setSelectedIndex(4);
      this.cmbxSchrift.setMaximumRowCount(8);
      GridBagConstraints gbc_cmbxSchrift = new GridBagConstraints();
      gbc_cmbxSchrift.insets = new Insets(0, 0, 0, 5);
      gbc_cmbxSchrift.anchor = 17;
      gbc_cmbxSchrift.gridx = 3;
      gbc_cmbxSchrift.gridy = 0;
      contentTabFont.add(this.cmbxSchrift, gbc_cmbxSchrift);
      Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
      GridBagConstraints gbc_rigidArea = new GridBagConstraints();
      gbc_rigidArea.gridx = 4;
      gbc_rigidArea.gridy = 0;
      contentTabFont.add(rigidArea, gbc_rigidArea);
      Component verticalStrut_3 = Box.createVerticalStrut(20);
      verticalStrut_3.setPreferredSize(new Dimension(0, 5));
      verticalStrut_3.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_3);
      JPanel contentFarben = new JPanel();
      contentFarben.setMaximumSize(new Dimension(400, 32767));
      contentFarben.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Farben ", 4, 2, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
      this.getContentPane().add(contentFarben);
      GridBagLayout gbl_contentFarben = new GridBagLayout();
      gbl_contentFarben.columnWidths = new int[]{20, 80, 20, 80};
      gbl_contentFarben.rowHeights = new int[]{30, 30, 30, 30};
      gbl_contentFarben.columnWeights = new double[]{0.0, 0.0, 0.0};
      gbl_contentFarben.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
      contentFarben.setLayout(gbl_contentFarben);
      this.txtKEY = new JTextField();
      this.txtKEY.setBorder(new BevelBorder(1, null, null, null, null));
      this.txtKEY.setEnabled(false);
      this.txtKEY.setEditable(false);
      this.txtKEY.setBackground(Color.RED);
      this.txtKEY.setFont(new Font("Tahoma", 0, 11));
      this.txtKEY.setColumns(2);
      this.txtKEY.setPreferredSize(new Dimension(20, 18));
      GridBagConstraints gbc_txtKEY = new GridBagConstraints();
      gbc_txtKEY.anchor = 18;
      gbc_txtKEY.insets = new Insets(5, 0, 5, 5);
      gbc_txtKEY.gridx = 0;
      gbc_txtKEY.gridy = 0;
      contentFarben.add(this.txtKEY, gbc_txtKEY);
      this.txtKEY.addMouseListener(this);
      JLabel lblFarbeFrKontrollstrukturen = new JLabel("Farbe für Kontrollstrukturen");
      lblFarbeFrKontrollstrukturen.setFont(new Font("Tahoma", 0, 11));
      lblFarbeFrKontrollstrukturen.setHorizontalAlignment(2);
      GridBagConstraints gbc_lblFarbeFrKontrollstrukturen = new GridBagConstraints();
      gbc_lblFarbeFrKontrollstrukturen.anchor = 17;
      gbc_lblFarbeFrKontrollstrukturen.insets = new Insets(5, 5, 5, 5);
      gbc_lblFarbeFrKontrollstrukturen.gridx = 1;
      gbc_lblFarbeFrKontrollstrukturen.gridy = 0;
      contentFarben.add(lblFarbeFrKontrollstrukturen, gbc_lblFarbeFrKontrollstrukturen);
      this.txtPARAMETER = new JTextField();
      this.txtPARAMETER.setEnabled(false);
      this.txtPARAMETER.setEditable(false);
      this.txtPARAMETER.setBorder(new BevelBorder(1, null, null, null, null));
      this.txtPARAMETER.setBackground(Color.RED);
      this.txtPARAMETER.setFont(new Font("Tahoma", 0, 11));
      this.txtPARAMETER.setColumns(2);
      this.txtPARAMETER.setPreferredSize(new Dimension(20, 18));
      GridBagConstraints gbc_txtPARAMETER = new GridBagConstraints();
      gbc_txtPARAMETER.anchor = 18;
      gbc_txtPARAMETER.insets = new Insets(5, 15, 5, 5);
      gbc_txtPARAMETER.gridx = 2;
      gbc_txtPARAMETER.gridy = 0;
      contentFarben.add(this.txtPARAMETER, gbc_txtPARAMETER);
      this.txtPARAMETER.addMouseListener(this);
      JLabel lblFarbeFrParameter = new JLabel("Farbe für Parameter");
      lblFarbeFrParameter.setFont(new Font("Tahoma", 0, 11));
      lblFarbeFrParameter.setHorizontalAlignment(2);
      lblFarbeFrParameter.setPreferredSize(new Dimension(140, 14));
      GridBagConstraints gbc_lblFarbeFrParameter = new GridBagConstraints();
      gbc_lblFarbeFrParameter.anchor = 17;
      gbc_lblFarbeFrParameter.insets = new Insets(5, 5, 5, 0);
      gbc_lblFarbeFrParameter.gridx = 3;
      gbc_lblFarbeFrParameter.gridy = 0;
      contentFarben.add(lblFarbeFrParameter, gbc_lblFarbeFrParameter);
      this.txtKEYANW = new JTextField();
      this.txtKEYANW.setEnabled(false);
      this.txtKEYANW.setEditable(false);
      this.txtKEYANW.setBorder(new BevelBorder(1, null, null, null, null));
      this.txtKEYANW.setBackground(Color.RED);
      this.txtKEYANW.setFont(new Font("Tahoma", 0, 11));
      this.txtKEYANW.setColumns(2);
      this.txtKEYANW.setPreferredSize(new Dimension(20, 18));
      GridBagConstraints gbc_txtKEYANW = new GridBagConstraints();
      gbc_txtKEYANW.anchor = 18;
      gbc_txtKEYANW.insets = new Insets(5, 0, 5, 5);
      gbc_txtKEYANW.gridx = 0;
      gbc_txtKEYANW.gridy = 1;
      contentFarben.add(this.txtKEYANW, gbc_txtKEYANW);
      this.txtKEYANW.addMouseListener(this);
      JLabel lblFarbeFrVordefinierte = new JLabel("<html><body>Farbe für vordefinierte <br>Anweisungen und Bedingungen</body></html>");
      lblFarbeFrVordefinierte.setFont(new Font("Tahoma", 0, 11));
      lblFarbeFrVordefinierte.setHorizontalAlignment(2);
      GridBagConstraints gbc_lblFarbeFrVordefinierte = new GridBagConstraints();
      gbc_lblFarbeFrVordefinierte.anchor = 17;
      gbc_lblFarbeFrVordefinierte.insets = new Insets(0, 5, 5, 5);
      gbc_lblFarbeFrVordefinierte.gridx = 1;
      gbc_lblFarbeFrVordefinierte.gridy = 1;
      contentFarben.add(lblFarbeFrVordefinierte, gbc_lblFarbeFrVordefinierte);
      this.txtNUMBER = new JTextField();
      this.txtNUMBER.setEnabled(false);
      this.txtNUMBER.setEditable(false);
      this.txtNUMBER.setBorder(new BevelBorder(1, null, null, null, null));
      this.txtNUMBER.setBackground(Color.RED);
      this.txtNUMBER.setFont(new Font("Tahoma", 0, 11));
      this.txtNUMBER.setColumns(2);
      this.txtNUMBER.setPreferredSize(new Dimension(20, 18));
      GridBagConstraints gbc_txtNUMBER = new GridBagConstraints();
      gbc_txtNUMBER.anchor = 18;
      gbc_txtNUMBER.insets = new Insets(5, 15, 5, 5);
      gbc_txtNUMBER.gridx = 2;
      gbc_txtNUMBER.gridy = 1;
      contentFarben.add(this.txtNUMBER, gbc_txtNUMBER);
      this.txtNUMBER.addMouseListener(this);
      JLabel lblFarbeFrZahlen = new JLabel("Farbe für Zahlen");
      lblFarbeFrZahlen.setFont(new Font("Tahoma", 0, 11));
      lblFarbeFrZahlen.setHorizontalAlignment(2);
      lblFarbeFrZahlen.setPreferredSize(new Dimension(140, 14));
      GridBagConstraints gbc_lblFarbeFrZahlen = new GridBagConstraints();
      gbc_lblFarbeFrZahlen.anchor = 17;
      gbc_lblFarbeFrZahlen.insets = new Insets(0, 5, 5, 0);
      gbc_lblFarbeFrZahlen.gridx = 3;
      gbc_lblFarbeFrZahlen.gridy = 1;
      contentFarben.add(lblFarbeFrZahlen, gbc_lblFarbeFrZahlen);
      this.txtIDENTIFIER = new JTextField();
      this.txtIDENTIFIER.setEnabled(false);
      this.txtIDENTIFIER.setEditable(false);
      this.txtIDENTIFIER.setBorder(new BevelBorder(1, null, null, null, null));
      this.txtIDENTIFIER.setBackground(Color.RED);
      this.txtIDENTIFIER.setFont(new Font("Tahoma", 0, 11));
      this.txtIDENTIFIER.setColumns(2);
      this.txtIDENTIFIER.setPreferredSize(new Dimension(20, 18));
      GridBagConstraints gbc_txtIDENTIFIER = new GridBagConstraints();
      gbc_txtIDENTIFIER.anchor = 18;
      gbc_txtIDENTIFIER.insets = new Insets(5, 0, 5, 5);
      gbc_txtIDENTIFIER.gridx = 0;
      gbc_txtIDENTIFIER.gridy = 2;
      contentFarben.add(this.txtIDENTIFIER, gbc_txtIDENTIFIER);
      this.txtIDENTIFIER.addMouseListener(this);
      JLabel lblFarbeFrSelbstdefinierte = new JLabel("<html><body>Farbe für selbstdefinierte <br>Anweisungen und Bedingungen</body></html>");
      lblFarbeFrSelbstdefinierte.setFont(new Font("Tahoma", 0, 11));
      lblFarbeFrSelbstdefinierte.setHorizontalAlignment(2);
      GridBagConstraints gbc_lblFarbeFrSelbstdefinierte = new GridBagConstraints();
      gbc_lblFarbeFrSelbstdefinierte.anchor = 17;
      gbc_lblFarbeFrSelbstdefinierte.insets = new Insets(0, 5, 5, 5);
      gbc_lblFarbeFrSelbstdefinierte.gridx = 1;
      gbc_lblFarbeFrSelbstdefinierte.gridy = 2;
      contentFarben.add(lblFarbeFrSelbstdefinierte, gbc_lblFarbeFrSelbstdefinierte);
      this.txtKAROL = new JTextField();
      this.txtKAROL.setEnabled(false);
      this.txtKAROL.setEditable(false);
      this.txtKAROL.setBorder(new BevelBorder(1, null, null, null, null));
      this.txtKAROL.setBackground(Color.RED);
      this.txtKAROL.setFont(new Font("Tahoma", 0, 11));
      this.txtKAROL.setColumns(2);
      this.txtKAROL.setPreferredSize(new Dimension(20, 18));
      GridBagConstraints gbc_txtKAROL = new GridBagConstraints();
      gbc_txtKAROL.anchor = 18;
      gbc_txtKAROL.insets = new Insets(5, 15, 5, 5);
      gbc_txtKAROL.gridx = 2;
      gbc_txtKAROL.gridy = 2;
      contentFarben.add(this.txtKAROL, gbc_txtKAROL);
      this.txtKAROL.addMouseListener(this);
      JLabel lblFarbeFrKarol = new JLabel("Farbe für Karol");
      lblFarbeFrKarol.setFont(new Font("Tahoma", 0, 11));
      lblFarbeFrKarol.setHorizontalAlignment(2);
      lblFarbeFrKarol.setPreferredSize(new Dimension(140, 14));
      GridBagConstraints gbc_lblFarbeFrKarol = new GridBagConstraints();
      gbc_lblFarbeFrKarol.insets = new Insets(0, 5, 5, 0);
      gbc_lblFarbeFrKarol.anchor = 17;
      gbc_lblFarbeFrKarol.gridx = 3;
      gbc_lblFarbeFrKarol.gridy = 2;
      contentFarben.add(lblFarbeFrKarol, gbc_lblFarbeFrKarol);
      JButton btnStandard = new JButton("Standardeinstellungen");
      btnStandard.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_btnStandard = new GridBagConstraints();
      gbc_btnStandard.gridwidth = 2;
      gbc_btnStandard.insets = new Insets(0, 0, 0, 5);
      gbc_btnStandard.gridx = 2;
      gbc_btnStandard.gridy = 3;
      btnStandard.addActionListener(this);
      btnStandard.setActionCommand("Standard");
      contentFarben.add(btnStandard, gbc_btnStandard);
      Component verticalStrut_1 = Box.createVerticalStrut(20);
      verticalStrut_1.setPreferredSize(new Dimension(0, 5));
      verticalStrut_1.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_1);
      JPanel contentFormatieren = new JPanel();
      contentFormatieren.setMaximumSize(new Dimension(400, 32767));
      contentFormatieren.setBorder(
         new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Formatieren ", 4, 2, new Font("Tahoma", 0, 11), new Color(0, 0, 0))
      );
      this.getContentPane().add(contentFormatieren);
      GridBagLayout gbl_contentFormatieren = new GridBagLayout();
      gbl_contentFormatieren.columnWidths = new int[]{402};
      gbl_contentFormatieren.rowHeights = new int[]{16};
      gbl_contentFormatieren.columnWeights = new double[]{1.0, 0.0};
      gbl_contentFormatieren.rowWeights = new double[]{0.0};
      contentFormatieren.setLayout(gbl_contentFormatieren);
      this.rbtnurEinrueck = new JRadioButton("nur Einrücken ");
      this.rbtnurEinrueck.setFont(new Font("Tahoma", 0, 11));
      this.rbtnurEinrueck.setPreferredSize(new Dimension(110, 14));
      this.rbtnurEinrueck.setMinimumSize(new Dimension(110, 14));
      GridBagConstraints gbc_rbtnurEinrueck = new GridBagConstraints();
      gbc_rbtnurEinrueck.anchor = 17;
      gbc_rbtnurEinrueck.fill = 3;
      gbc_rbtnurEinrueck.insets = new Insets(0, 5, 0, 0);
      gbc_rbtnurEinrueck.gridx = 0;
      gbc_rbtnurEinrueck.gridy = 0;
      contentFormatieren.add(this.rbtnurEinrueck, gbc_rbtnurEinrueck);
      this.rbtSchreibw = new JRadioButton("Einrücken und Schreibweise anpassen");
      this.rbtSchreibw.setFont(new Font("Tahoma", 0, 11));
      this.rbtSchreibw.setAlignmentX(0.5F);
      GridBagConstraints gbc_rbtSchreibw = new GridBagConstraints();
      gbc_rbtSchreibw.anchor = 17;
      gbc_rbtSchreibw.fill = 3;
      gbc_rbtSchreibw.insets = new Insets(0, 5, 0, 0);
      gbc_rbtSchreibw.gridx = 1;
      gbc_rbtSchreibw.gridy = 0;
      contentFormatieren.add(this.rbtSchreibw, gbc_rbtSchreibw);
      ButtonGroup gruppe = new ButtonGroup();
      gruppe.add(this.rbtnurEinrueck);
      gruppe.add(this.rbtSchreibw);
      Component verticalStrut_5 = Box.createVerticalStrut(5);
      this.getContentPane().add(verticalStrut_5);
      JPanel contentBildschirm = new JPanel();
      contentBildschirm.setMaximumSize(new Dimension(400, 32767));
      contentBildschirm.setBorder(
         new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Bildschirm ", 4, 2, new Font("Tahoma", 0, 11), new Color(0, 0, 0))
      );
      this.getContentPane().add(contentBildschirm);
      GridBagLayout gbl_contentBildschirm = new GridBagLayout();
      gbl_contentBildschirm.columnWidths = new int[]{401};
      gbl_contentBildschirm.rowHeights = new int[]{33};
      gbl_contentBildschirm.columnWeights = new double[]{1.0};
      gbl_contentBildschirm.rowWeights = new double[]{0.0};
      contentBildschirm.setLayout(gbl_contentBildschirm);
      this.ckbxZeilen = new JCheckBox("Zeilennummern anzeigen");
      this.ckbxZeilen.setFont(new Font("Tahoma", 0, 11));
      this.ckbxZeilen.setMaximumSize(new Dimension(360, 23));
      this.ckbxZeilen.setAlignmentX(0.5F);
      GridBagConstraints gbc_ckbxZeilen = new GridBagConstraints();
      gbc_ckbxZeilen.anchor = 17;
      gbc_ckbxZeilen.insets = new Insets(0, 5, 0, 0);
      gbc_ckbxZeilen.gridx = 0;
      gbc_ckbxZeilen.gridy = 0;
      contentBildschirm.add(this.ckbxZeilen, gbc_ckbxZeilen);
      Component verticalStrut_2 = Box.createVerticalStrut(20);
      verticalStrut_2.setPreferredSize(new Dimension(0, 5));
      verticalStrut_2.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_2);
      JPanel contentAusdruck = new JPanel();
      contentAusdruck.setMaximumSize(new Dimension(400, 32767));
      contentAusdruck.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Ausdruck ", 4, 2, new Font("Tahoma", 0, 11), new Color(0, 0, 0)));
      this.getContentPane().add(contentAusdruck);
      GridBagLayout gbl_contentAusdruck = new GridBagLayout();
      gbl_contentAusdruck.columnWidths = new int[]{0, 198, 0};
      gbl_contentAusdruck.rowHeights = new int[]{35};
      gbl_contentAusdruck.columnWeights = new double[]{0.0, 0.0, 1.0};
      gbl_contentAusdruck.rowWeights = new double[]{0.0};
      contentAusdruck.setLayout(gbl_contentAusdruck);
      this.ckbxZeilenDruck = new JCheckBox("Zeilennummern anzeigen");
      this.ckbxZeilenDruck.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_ckbxZeilenDruck = new GridBagConstraints();
      gbc_ckbxZeilenDruck.anchor = 17;
      gbc_ckbxZeilenDruck.insets = new Insets(0, 5, 0, 5);
      gbc_ckbxZeilenDruck.gridx = 0;
      gbc_ckbxZeilenDruck.gridy = 0;
      contentAusdruck.add(this.ckbxZeilenDruck, gbc_ckbxZeilenDruck);
      this.ckbxFarbe = new JCheckBox("in Farbe");
      this.ckbxFarbe.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_ckbxFarbe = new GridBagConstraints();
      gbc_ckbxFarbe.insets = new Insets(0, 22, 0, 5);
      gbc_ckbxFarbe.anchor = 17;
      gbc_ckbxFarbe.gridx = 1;
      gbc_ckbxFarbe.gridy = 0;
      contentAusdruck.add(this.ckbxFarbe, gbc_ckbxFarbe);
      Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
      GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
      gbc_rigidArea_1.gridx = 2;
      gbc_rigidArea_1.gridy = 0;
      contentAusdruck.add(rigidArea_1, gbc_rigidArea_1);
      Component verticalStrut_4 = Box.createVerticalStrut(20);
      verticalStrut_4.setPreferredSize(new Dimension(0, 5));
      verticalStrut_4.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_4);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setMaximumSize(new Dimension(400, 32767));
      buttonPanel.setMinimumSize(new Dimension(150, 30));
      buttonPanel.setPreferredSize(new Dimension(150, 33));
      buttonPanel.setFocusTraversalKeysEnabled(false);
      buttonPanel.setEnabled(false);
      buttonPanel.setBorder(null);
      buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
      buttonPanel.add(Box.createHorizontalGlue());
      JButton okButton = new JButton("OK");
      okButton.setContentAreaFilled(false);
      okButton.setBorder(new BevelBorder(0, null, null, null, null));
      okButton.setPreferredSize(new Dimension(85, 23));
      okButton.setMinimumSize(new Dimension(85, 23));
      okButton.setMaximumSize(new Dimension(85, 23));
      okButton.addActionListener(this);
      okButton.setActionCommand("OK");
      buttonPanel.add(okButton);
      this.getRootPane().setDefaultButton(okButton);
      Component rigidArea2 = Box.createRigidArea(new Dimension(5, 0));
      rigidArea2.setPreferredSize(new Dimension(20, 0));
      rigidArea2.setMinimumSize(new Dimension(20, 0));
      rigidArea2.setMaximumSize(new Dimension(20, 0));
      buttonPanel.add(rigidArea2);
      JButton cancelButton = new JButton("Abbrechen");
      cancelButton.setPreferredSize(new Dimension(86, 23));
      cancelButton.setMaximumSize(new Dimension(86, 23));
      cancelButton.setMinimumSize(new Dimension(86, 23));
      cancelButton.setContentAreaFilled(false);
      cancelButton.setBorder(new BevelBorder(0, null, null, null, null));
      cancelButton.addActionListener(this);
      cancelButton.setActionCommand("Cancel");
      buttonPanel.add(cancelButton);
      Component rigidArea3 = Box.createRigidArea(new Dimension(5, 0));
      rigidArea3.setPreferredSize(new Dimension(10, 0));
      rigidArea3.setMinimumSize(new Dimension(10, 0));
      rigidArea3.setMaximumSize(new Dimension(10, 0));
      buttonPanel.add(rigidArea3);
      this.getContentPane().add(buttonPanel);
   }

   @Override
   public void mouseClicked(MouseEvent me) {
      if (me.getSource() == this.txtKEY) {
         Color tempColor = JColorChooser.showDialog(this, "Farbe für Kontrollstrukturen", this.txtKEY.getBackground());
         if (tempColor != null) {
            this.txtKEY.setBackground(tempColor);
         }
      }

      if (me.getSource() == this.txtKEYANW) {
         Color tempColor = JColorChooser.showDialog(this, "Farbe für vordef. Anweisungen/Bedingungen", this.txtKEYANW.getBackground());
         if (tempColor != null) {
            this.txtKEYANW.setBackground(tempColor);
         }
      }

      if (me.getSource() == this.txtIDENTIFIER) {
         Color tempColor = JColorChooser.showDialog(this, "Farbe für selbstdef. Anweisungen/Bedingungen", this.txtIDENTIFIER.getBackground());
         if (tempColor != null) {
            this.txtIDENTIFIER.setBackground(tempColor);
         }
      }

      if (me.getSource() == this.txtPARAMETER) {
         Color tempColor = JColorChooser.showDialog(this, "Farbe für Parameter", this.txtPARAMETER.getBackground());
         if (tempColor != null) {
            this.txtPARAMETER.setBackground(tempColor);
         }
      }

      if (me.getSource() == this.txtNUMBER) {
         Color tempColor = JColorChooser.showDialog(this, "Farbe für Zahlen", this.txtNUMBER.getBackground());
         if (tempColor != null) {
            this.txtNUMBER.setBackground(tempColor);
         }
      }

      if (me.getSource() == this.txtKAROL) {
         Color tempColor = JColorChooser.showDialog(this, "Farbe für Karol", this.txtKAROL.getBackground());
         if (tempColor != null) {
            this.txtKAROL.setBackground(tempColor);
         }
      }
   }

   @Override
   public void mousePressed(MouseEvent me) {
   }

   @Override
   public void mouseReleased(MouseEvent me) {
   }

   @Override
   public void mouseEntered(MouseEvent me) {
   }

   @Override
   public void mouseExited(MouseEvent me) {
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      KEdtDocument doc = this.view.getProgDocument();
      KEdtEditorKit kit = (KEdtEditorKit)this.view.getProgTextPanel().getEditorKit();
      if (ae.getActionCommand() == "OK") {
         kit.setTAB_LEN((int) this.spnTab.getModel().getValue());
         String tempStr = (String)this.cmbxSchrift.getSelectedItem();
         tempStr = tempStr.trim();
         doc.setFontsizeNormal(Integer.valueOf(tempStr));
         this.view.getGutterPanel().setFontWidth();
         this.view.getGutterPanel().repaint();
         doc.setStyleColor(KEdtToken.TokenType.KEY, this.txtKEY.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.KEYEND, this.txtKEY.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.KEYANW, this.txtKEYANW.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.KEYBED, this.txtKEYANW.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.NUMBER, this.txtNUMBER.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.PNUMBER, this.txtPARAMETER.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.PCOLOR, this.txtPARAMETER.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.IDENTIFIER, this.txtIDENTIFIER.getBackground().getRGB());
         doc.setStyleColor(KEdtToken.TokenType.KAROL, this.txtKAROL.getBackground().getRGB());
         doc.setMitSchreibweise(this.rbtSchreibw.isSelected());
         this.view.getGutterPanel().setMitZeilennummer(this.ckbxZeilen.isSelected());
         this.setVisible(false);
      }

      if (ae.getActionCommand() == "Cancel") {
         this.setVisible(false);
      }

      if (ae.getActionCommand() == "Standard") {
         this.txtKEY.setBackground(new Color(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.KEY)));
         this.txtKEYANW.setBackground(new Color(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.KEYANW)));
         this.txtNUMBER.setBackground(new Color(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.NUMBER)));
         this.txtPARAMETER.setBackground(new Color(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.PNUMBER)));
         this.txtIDENTIFIER.setBackground(new Color(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.IDENTIFIER)));
         this.txtKAROL.setBackground(new Color(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.KAROL)));
      }
   }
}
