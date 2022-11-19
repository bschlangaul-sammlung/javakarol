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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DlgEinstellungK extends JDialog implements ActionListener {
   JLabel lblWert;
   JSlider sldVerzoegerung;
   JCheckBox ckbxWelt;
   JCheckBox ckbkKontrolle;
   JCheckBox ckbxScroll;
   JSpinner spnSprung;
   JSpinner spnStart;
   JSpinner spnMaximal;
   JRadioButton rbtKeine;
   JRadioButton rbtHinweis;
   JRadioButton rbtAbbruch;
   KarolProgram prog;
   KarolRoboter karol;
   KarolController cont;

   public DlgEinstellungK(Frame owner) {
      super(owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(420, 430));
      this.setTitle("Einstellungen Karol");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgEinstellungK.class.getResource("/icons/Karol.gif")));
      this.setModal(true);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public void showModal(KarolProgram prg, KarolRoboter robo, KarolController con) {
      this.prog = prg;
      this.karol = robo;
      this.cont = con;
      int vz = this.prog.getProgVerzoegerung();
      if (vz >= 0 && vz <= 2000) {
         this.sldVerzoegerung.getModel().setValue(vz);
         float wertZahl = (0.0F + (float)vz) / 1000.0F;
         String wertStr = String.format(" %4.2f s ", wertZahl);
         this.lblWert.setText(wertStr);
      }

      this.ckbxWelt.setSelected(this.cont.getRestoreWelt());
      this.spnSprung.getModel().setValue(this.karol.SprungshoeheGeben());
      boolean kontrolle = this.karol.RucksackPruefungGeben();
      this.ckbkKontrolle.setSelected(kontrolle);
      this.spnStart.setEnabled(kontrolle);
      this.spnMaximal.setEnabled(kontrolle);
      this.spnStart.getModel().setValue(this.karol.getRucksackStart());
      this.spnMaximal.getModel().setValue(this.karol.getRucksackMaximum());
      this.ckbxScroll.setSelected(this.prog.getScrollOnStep());
      if (this.prog.getStopOnError()) {
         this.rbtAbbruch.setSelected(true);
      } else if (this.prog.getShowOnError()) {
         this.rbtHinweis.setSelected(true);
      } else {
         this.rbtKeine.setSelected(true);
      }

      this.setVisible(true);
   }

   private int getVerzoegerung() {
      return this.sldVerzoegerung.getModel().getValue();
   }

   private boolean getRestoreW() {
      return this.ckbxWelt.isSelected();
   }

   private int getMaxSprung() {
      return (int) this.spnSprung.getModel().getValue();
   }

   private int getTragStart() {
      return (int) this.spnStart.getModel().getValue();
   }

   private int getTragMaximal() {
      return (int) this.spnMaximal.getModel().getValue();
   }

   private boolean getTragPruefung() {
      return this.ckbkKontrolle.isSelected();
   }

   private boolean getScrollOnStep() {
      return this.ckbxScroll.isSelected();
   }

   private boolean getShowOnError() {
      return this.rbtHinweis.isSelected();
   }

   private boolean getStopOnError() {
      return this.rbtAbbruch.isSelected();
   }

   private void GUIAnlegen() {
      this.setBounds(100, 100, 370, 430);
      this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), 1));
      Component verticalStrut = Box.createVerticalStrut(20);
      verticalStrut.setMinimumSize(new Dimension(0, 0));
      verticalStrut.setPreferredSize(new Dimension(0, 5));
      this.getContentPane().add(verticalStrut);
      JPanel contentAblauf = new JPanel();
      contentAblauf.setMaximumSize(new Dimension(360, 32767));
      contentAblauf.setPreferredSize(new Dimension(360, 70));
      contentAblauf.setBorder(new TitledBorder(null, "", 4, 2, null, null));
      this.getContentPane().add(contentAblauf);
      GridBagLayout gbl_contentAblauf = new GridBagLayout();
      gbl_contentAblauf.columnWidths = new int[3];
      gbl_contentAblauf.rowHeights = new int[4];
      gbl_contentAblauf.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
      gbl_contentAblauf.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
      contentAblauf.setLayout(gbl_contentAblauf);
      JLabel lblVerzoegerung = new JLabel("Ablaufverzögerung");
      lblVerzoegerung.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_lblVerzoegerung = new GridBagConstraints();
      gbc_lblVerzoegerung.anchor = 13;
      gbc_lblVerzoegerung.insets = new Insets(5, 8, 5, 5);
      gbc_lblVerzoegerung.gridx = 0;
      gbc_lblVerzoegerung.gridy = 0;
      contentAblauf.add(lblVerzoegerung, gbc_lblVerzoegerung);
      this.lblWert = new JLabel(" 0,00 s ");
      this.lblWert.setFont(new Font("Tahoma", 0, 11));
      this.lblWert.setBorder(new BevelBorder(1, null, null, null, null));
      GridBagConstraints gbc_lblWert = new GridBagConstraints();
      gbc_lblWert.anchor = 17;
      gbc_lblWert.insets = new Insets(5, 5, 5, 0);
      gbc_lblWert.gridx = 1;
      gbc_lblWert.gridy = 0;
      contentAblauf.add(this.lblWert, gbc_lblWert);
      this.sldVerzoegerung = new JSlider();
      this.sldVerzoegerung.setBorder(null);
      this.sldVerzoegerung.setMinorTickSpacing(100);
      this.sldVerzoegerung.setMaximum(2000);
      this.sldVerzoegerung.setPaintTicks(true);
      this.sldVerzoegerung.setPreferredSize(new Dimension(350, 23));
      GridBagConstraints gbc_sldVerzoegerung = new GridBagConstraints();
      gbc_sldVerzoegerung.insets = new Insets(0, 0, 5, 0);
      gbc_sldVerzoegerung.gridwidth = 2;
      gbc_sldVerzoegerung.gridx = 0;
      gbc_sldVerzoegerung.gridy = 1;
      contentAblauf.add(this.sldVerzoegerung, gbc_sldVerzoegerung);
      this.sldVerzoegerung.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent ce) {
            JSlider sl = (JSlider)ce.getSource();
            float wertZahl = (0.0F + (float)sl.getValue()) / 1000.0F;
            String wertStr = String.format(" %4.2f s ", wertZahl);
            DlgEinstellungK.this.lblWert.setText(wertStr);
            DlgEinstellungK.this.lblWert.setFont(new Font("Tahoma", 0, 11));
         }
      });
      JLabel lblNewLabel_1 = new JLabel("0 Sekunden");
      lblNewLabel_1.setFont(new Font("Tahoma", 0, 11));
      lblNewLabel_1.setMaximumSize(new Dimension(92, 14));
      lblNewLabel_1.setPreferredSize(new Dimension(92, 14));
      GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
      gbc_lblNewLabel_1.anchor = 17;
      gbc_lblNewLabel_1.insets = new Insets(0, 5, 0, 5);
      gbc_lblNewLabel_1.gridx = 0;
      gbc_lblNewLabel_1.gridy = 2;
      contentAblauf.add(lblNewLabel_1, gbc_lblNewLabel_1);
      JLabel lblNewLabel_2 = new JLabel("2 Sekunden");
      lblNewLabel_2.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
      gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
      gbc_lblNewLabel_2.anchor = 13;
      gbc_lblNewLabel_2.gridx = 1;
      gbc_lblNewLabel_2.gridy = 2;
      contentAblauf.add(lblNewLabel_2, gbc_lblNewLabel_2);
      Component verticalStrut_1 = Box.createVerticalStrut(20);
      verticalStrut_1.setPreferredSize(new Dimension(0, 5));
      verticalStrut_1.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_1);
      JPanel contentWelt = new JPanel();
      contentWelt.setMaximumSize(new Dimension(360, 32767));
      contentWelt.setBorder(new TitledBorder(null, "", 4, 2, null, null));
      contentWelt.setPreferredSize(new Dimension(360, 40));
      this.getContentPane().add(contentWelt);
      GridBagLayout gbl_contentWelt = new GridBagLayout();
      gbl_contentWelt.columnWidths = new int[]{401, 0};
      gbl_contentWelt.rowHeights = new int[]{33, 0};
      gbl_contentWelt.columnWeights = new double[]{1.0, Double.MIN_VALUE};
      gbl_contentWelt.rowWeights = new double[]{0.0, Double.MIN_VALUE};
      contentWelt.setLayout(gbl_contentWelt);
      this.ckbxWelt = new JCheckBox("Vor Programmstart gespeicherte Welt wiederherstellen");
      this.ckbxWelt.setFont(new Font("Tahoma", 0, 11));
      this.ckbxWelt.setMaximumSize(new Dimension(360, 23));
      this.ckbxWelt.setAlignmentX(0.5F);
      GridBagConstraints gbc_ckbxWelt = new GridBagConstraints();
      gbc_ckbxWelt.anchor = 17;
      gbc_ckbxWelt.insets = new Insets(0, 5, 0, 0);
      gbc_ckbxWelt.fill = 3;
      gbc_ckbxWelt.gridx = 0;
      gbc_ckbxWelt.gridy = 0;
      contentWelt.add(this.ckbxWelt, gbc_ckbxWelt);
      Component verticalStrut_2 = Box.createVerticalStrut(20);
      verticalStrut_2.setPreferredSize(new Dimension(0, 5));
      verticalStrut_2.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_2);
      JPanel contentSprung = new JPanel();
      contentSprung.setMaximumSize(new Dimension(360, 32767));
      contentSprung.setBorder(new TitledBorder(null, "", 4, 2, null, null));
      contentSprung.setPreferredSize(new Dimension(360, 40));
      this.getContentPane().add(contentSprung);
      GridBagLayout gbl_contentSprung = new GridBagLayout();
      gbl_contentSprung.columnWidths = new int[]{198, 198, 0};
      gbl_contentSprung.rowHeights = new int[]{35, 0};
      gbl_contentSprung.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
      gbl_contentSprung.rowWeights = new double[]{0.0, Double.MIN_VALUE};
      contentSprung.setLayout(gbl_contentSprung);
      JLabel lblSprung = new JLabel("Anzahl Ziegel, um die Karol maximal hoch-/tiefspringen kann");
      lblSprung.setFont(new Font("Tahoma", 0, 11));
      lblSprung.setHorizontalAlignment(4);
      lblSprung.setAlignmentX(0.5F);
      GridBagConstraints gbc_lblSprung = new GridBagConstraints();
      gbc_lblSprung.anchor = 17;
      gbc_lblSprung.insets = new Insets(0, 8, 0, 5);
      gbc_lblSprung.gridx = 0;
      gbc_lblSprung.gridy = 0;
      contentSprung.add(lblSprung, gbc_lblSprung);
      this.spnSprung = new JSpinner();
      this.spnSprung.setFont(new Font("Tahoma", 0, 11));
      this.spnSprung.setMinimumSize(new Dimension(35, 20));
      this.spnSprung.setPreferredSize(new Dimension(35, 20));
      this.spnSprung.setMaximumSize(new Dimension(35, 20));
      this.spnSprung.setModel(new SpinnerNumberModel(1, 1, 10, 1));
      GridBagConstraints gbc_spnSprung = new GridBagConstraints();
      gbc_spnSprung.insets = new Insets(5, 0, 5, 10);
      gbc_spnSprung.anchor = 17;
      gbc_spnSprung.gridx = 1;
      gbc_spnSprung.gridy = 0;
      contentSprung.add(this.spnSprung, gbc_spnSprung);
      Component verticalStrut_3 = Box.createVerticalStrut(20);
      verticalStrut_3.setPreferredSize(new Dimension(0, 5));
      verticalStrut_3.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_3);
      JPanel contentRucksack = new JPanel();
      contentRucksack.setMaximumSize(new Dimension(360, 32767));
      contentRucksack.setBorder(new TitledBorder(null, "", 4, 2, null, null));
      contentRucksack.setPreferredSize(new Dimension(360, 70));
      this.getContentPane().add(contentRucksack);
      GridBagLayout gbl_contentRucksack = new GridBagLayout();
      gbl_contentRucksack.columnWidths = new int[6];
      gbl_contentRucksack.rowHeights = new int[3];
      gbl_contentRucksack.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
      gbl_contentRucksack.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
      contentRucksack.setLayout(gbl_contentRucksack);
      this.ckbkKontrolle = new JCheckBox("Anzahl der Ziegel kontrollieren, welche Karol mit sich tragen kann");
      this.ckbkKontrolle.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_ckbkKontrolle = new GridBagConstraints();
      gbc_ckbkKontrolle.weightx = 1.0;
      gbc_ckbkKontrolle.insets = new Insets(5, 5, 5, 0);
      gbc_ckbkKontrolle.anchor = 17;
      gbc_ckbkKontrolle.gridwidth = 5;
      gbc_ckbkKontrolle.gridx = 0;
      gbc_ckbkKontrolle.gridy = 0;
      contentRucksack.add(this.ckbkKontrolle, gbc_ckbkKontrolle);
      this.ckbkKontrolle.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            JCheckBox cb = (JCheckBox)ae.getSource();
            if (cb.isSelected()) {
               DlgEinstellungK.this.spnStart.setEnabled(true);
               DlgEinstellungK.this.spnMaximal.setEnabled(true);
            } else {
               DlgEinstellungK.this.spnStart.setEnabled(false);
               DlgEinstellungK.this.spnMaximal.setEnabled(false);
            }
         }
      });
      Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
      rigidArea.setPreferredSize(new Dimension(40, 20));
      GridBagConstraints gbc_rigidArea = new GridBagConstraints();
      gbc_rigidArea.insets = new Insets(0, 5, 0, 5);
      gbc_rigidArea.gridx = 0;
      gbc_rigidArea.gridy = 1;
      contentRucksack.add(rigidArea, gbc_rigidArea);
      JLabel lblZiegelStart = new JLabel("beim Start");
      lblZiegelStart.setFont(new Font("Tahoma", 0, 11));
      lblZiegelStart.setHorizontalAlignment(4);
      lblZiegelStart.setAlignmentX(0.5F);
      lblZiegelStart.setPreferredSize(new Dimension(60, 14));
      GridBagConstraints gbc_lblZiegelStart = new GridBagConstraints();
      gbc_lblZiegelStart.anchor = 13;
      gbc_lblZiegelStart.insets = new Insets(0, 5, 0, 5);
      gbc_lblZiegelStart.gridx = 1;
      gbc_lblZiegelStart.gridy = 1;
      contentRucksack.add(lblZiegelStart, gbc_lblZiegelStart);
      this.spnStart = new JSpinner();
      this.spnStart.setFont(new Font("Tahoma", 0, 11));
      this.spnStart.setModel(new SpinnerNumberModel(0, 0, 255, 1));
      GridBagConstraints gbc_spnStart = new GridBagConstraints();
      gbc_spnStart.insets = new Insets(0, 0, 0, 5);
      gbc_spnStart.gridx = 2;
      gbc_spnStart.gridy = 1;
      contentRucksack.add(this.spnStart, gbc_spnStart);
      JLabel lblNewLabel = new JLabel("maximal");
      lblNewLabel.setFont(new Font("Tahoma", 0, 11));
      lblNewLabel.setHorizontalAlignment(4);
      lblNewLabel.setPreferredSize(new Dimension(60, 14));
      GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
      gbc_lblNewLabel.anchor = 13;
      gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
      gbc_lblNewLabel.gridx = 3;
      gbc_lblNewLabel.gridy = 1;
      contentRucksack.add(lblNewLabel, gbc_lblNewLabel);
      this.spnMaximal = new JSpinner();
      this.spnMaximal.setFont(new Font("Tahoma", 0, 11));
      this.spnMaximal.setModel(new SpinnerNumberModel(0, 0, 255, 1));
      GridBagConstraints gbc_spnMaximal = new GridBagConstraints();
      gbc_spnMaximal.anchor = 17;
      gbc_spnMaximal.gridx = 4;
      gbc_spnMaximal.gridy = 1;
      contentRucksack.add(this.spnMaximal, gbc_spnMaximal);
      Component verticalStrut_4 = Box.createVerticalStrut(20);
      verticalStrut_4.setPreferredSize(new Dimension(0, 5));
      verticalStrut_4.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_4);
      JPanel contentScroll = new JPanel();
      contentScroll.setMaximumSize(new Dimension(360, 32767));
      contentScroll.setBorder(new TitledBorder(null, "", 4, 2, null, null));
      contentScroll.setPreferredSize(new Dimension(360, 40));
      this.getContentPane().add(contentScroll);
      GridBagLayout gbl_contentScroll = new GridBagLayout();
      gbl_contentScroll.columnWidths = new int[]{401, 0};
      gbl_contentScroll.rowHeights = new int[]{33, 0};
      gbl_contentScroll.columnWeights = new double[]{1.0, Double.MIN_VALUE};
      gbl_contentScroll.rowWeights = new double[]{0.0, Double.MIN_VALUE};
      contentScroll.setLayout(gbl_contentScroll);
      this.ckbxScroll = new JCheckBox("Beim Einzelschritt automatisches Scrollen des Programmtexts");
      this.ckbxScroll.setFont(new Font("Tahoma", 0, 11));
      this.ckbxScroll.setMaximumSize(new Dimension(360, 23));
      this.ckbxScroll.setAlignmentX(0.5F);
      GridBagConstraints gbc_ckbxScroll = new GridBagConstraints();
      gbc_ckbxScroll.anchor = 17;
      gbc_ckbxScroll.insets = new Insets(0, 5, 0, 0);
      gbc_ckbxScroll.fill = 3;
      gbc_ckbxScroll.gridx = 0;
      gbc_ckbxScroll.gridy = 0;
      contentScroll.add(this.ckbxScroll, gbc_ckbxScroll);
      Component verticalStrut_5 = Box.createVerticalStrut(20);
      verticalStrut_5.setPreferredSize(new Dimension(0, 5));
      verticalStrut_5.setMinimumSize(new Dimension(0, 0));
      this.getContentPane().add(verticalStrut_5);
      JPanel contentFehler = new JPanel();
      contentFehler.setMaximumSize(new Dimension(360, 32767));
      contentFehler.setBorder(
         new TitledBorder(UIManager.getBorder("TitledBorder.border"), " Verhalten bei Ablauffehler ", 4, 2, new Font("Tahoma", 0, 11), new Color(0, 0, 0))
      );
      contentFehler.setPreferredSize(new Dimension(360, 100));
      this.getContentPane().add(contentFehler);
      GridBagLayout gbl_contentFehler = new GridBagLayout();
      gbl_contentFehler.columnWidths = new int[]{402, 0};
      gbl_contentFehler.rowHeights = new int[]{16, 16, 16, 0};
      gbl_contentFehler.columnWeights = new double[]{1.0, Double.MIN_VALUE};
      gbl_contentFehler.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
      contentFehler.setLayout(gbl_contentFehler);
      this.rbtKeine = new JRadioButton("keine Auswirkung");
      this.rbtKeine.setFont(new Font("Tahoma", 0, 11));
      GridBagConstraints gbc_rbtKeine = new GridBagConstraints();
      gbc_rbtKeine.fill = 1;
      gbc_rbtKeine.insets = new Insets(0, 20, 0, 0);
      gbc_rbtKeine.gridx = 0;
      gbc_rbtKeine.gridy = 0;
      contentFehler.add(this.rbtKeine, gbc_rbtKeine);
      this.rbtHinweis = new JRadioButton("nur Hinweise geben");
      this.rbtHinweis.setFont(new Font("Tahoma", 0, 11));
      this.rbtHinweis.setAlignmentX(0.5F);
      GridBagConstraints gbc_rbtHinweis = new GridBagConstraints();
      gbc_rbtHinweis.fill = 1;
      gbc_rbtHinweis.insets = new Insets(0, 20, 0, 0);
      gbc_rbtHinweis.gridx = 0;
      gbc_rbtHinweis.gridy = 1;
      contentFehler.add(this.rbtHinweis, gbc_rbtHinweis);
      this.rbtAbbruch = new JRadioButton("Programmabbruch");
      this.rbtAbbruch.setFont(new Font("Tahoma", 0, 11));
      this.rbtAbbruch.setAlignmentX(0.5F);
      this.rbtAbbruch.setSelected(true);
      GridBagConstraints gbc_rbtAbbruch = new GridBagConstraints();
      gbc_rbtAbbruch.insets = new Insets(0, 20, 0, 0);
      gbc_rbtAbbruch.fill = 1;
      gbc_rbtAbbruch.gridx = 0;
      gbc_rbtAbbruch.gridy = 2;
      contentFehler.add(this.rbtAbbruch, gbc_rbtAbbruch);
      ButtonGroup gruppe = new ButtonGroup();
      gruppe.add(this.rbtKeine);
      gruppe.add(this.rbtHinweis);
      gruppe.add(this.rbtAbbruch);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setMaximumSize(new Dimension(365, 32767));
      buttonPanel.setMinimumSize(new Dimension(150, 30));
      buttonPanel.setPreferredSize(new Dimension(150, 33));
      buttonPanel.setFocusTraversalKeysEnabled(false);
      buttonPanel.setEnabled(false);
      buttonPanel.setBorder(null);
      buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
      buttonPanel.add(Box.createHorizontalGlue());
      JButton okButton = new JButton("OK");
      okButton.setFont(new Font("Tahoma", 0, 11));
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
      cancelButton.setFont(new Font("Tahoma", 0, 11));
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
   public void actionPerformed(ActionEvent ae) {
      this.setVisible(false);
      if (ae.getActionCommand() == "OK") {
         this.prog.setProgVerzoegerung(this.getVerzoegerung());
         this.cont.setRestoreWelt(this.getRestoreW());
         this.karol.SprunghoeheSetzen(this.getMaxSprung());
         this.karol.setRucksackPruefung(this.getTragPruefung());
         this.karol.setRucksackMaximum(this.getTragMaximal());
         this.karol.setRucksackStart(this.getTragStart());
         this.prog.setScrollOnStep(this.getScrollOnStep());
         this.prog.setOnError(this.getShowOnError(), this.getStopOnError());
      }
   }
}
