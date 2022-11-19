package robotkarol;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javakarol.FehlerAnzeige;
import javakarol.Roboter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import karoleditor.KEdtDocument;
import karoleditor.KEdtEditorKit;
import karoleditor.KEdtGutter;

public class KarolView extends JFrame {
   private KarolWelt karolWelt;
   public Karol3D welt3DPanel;
   public Karol2D welt2DPanel;
   public KarolStruktogramm struktoPanel;
   private FehlerAnzeige fehlerAnzeige;
   private JTextPane progEditorPane;
   private KEdtGutter progRandspalte;
   private String programmName;
   private String weltName;
   private String mainPfad;
   private String fileSeparator;
   private KarolView.DisplayStatusType viewStatus;
   private JLabel lblPositionXWert;
   private JLabel lblPositionYWert;
   private JLabel lblBlickWert;
   private JLabel lblZiegel;
   private JLabel lblZiegelWert;
   private JButton btnKarolLinks;
   private JButton btnKarolVor;
   private JButton btnKarolRechts;
   private JButton btnKarolHinlegen;
   private JButton btnKarolAufheben;
   private JButton btnKarolMarke;
   private JButton btnKarolQuaderAbl;
   private JButton btnKarolQuaderEntf;
   private JButton btnWeltNeu;
   private JButton btnWeltOeffnen;
   private JButton btnWeltSpeichern;
   private JButton btnWeltLoeschen;
   private JButton btnWeltWiederherstellen;
   private JButton btnDarstell2D;
   private JButton btnProgNeu;
   private JButton btnProgOeffnen;
   private JButton btnProgSpeichern;
   private JButton btnProgLampe;
   private JButton btnSyntax;
   private JButton btnAblaufNormal;
   private JButton btnAblaufSchnell;
   private JButton btnAblaufEinzeln;
   private JButton btnAblaufPause;
   private JButton btnAblaufStopp;
   private JButton btnDiagrSpeichern;
   private JButton btnDiagrKopieren;
   private JButton btnDiagrDrucken;
   private JButton btnDiagrEnde;
   private JButton btnUebersicht;
   private JMenuItem mntmNeueWelt;
   private JMenuItem mntmOeffnenWelt;
   private JMenuItem mntmWeltSpeichern;
   private JMenuItem mntmWeltSpeichernUnter;
   private JMenuItem mntmAlsBildSpeichern;
   private JMenuItem mntmWeltLoeschen;
   private JMenuItem mntmBereicheAnordnen;
   private JMenuItem mntmDarstellung;
   private JMenuItem mntmWeltDirektFestlegen;
   private JMenuItem mntmGrafikVergroessern;
   private JMenuItem mntmGrafikVerkleinern;
   private JMenuItem mntmNeu;
   private JMenuItem mntmOeffnen;
   private JMenuItem mntmSpeichern;
   private JMenuItem mntmSpeichernUnter;
   private JMenuItem mntmDrucken;
   private JMenuItem mntmStart;
   private JMenuItem mntmSchnelllauf;
   private JMenuItem mntmEinzelschritt;
   private JMenuItem mntmPause;
   private JMenuItem mntmStopp;
   private JMenuItem mntmSyntaxpruefung;
   private JMenuItem mntmStopppunkt;
   private JMenuItem mntmInhalt;
   private JMenuItem mntmTipps;
   private JMenuItem mntmProgramminfo;
   private JMenuItem mntmKarol;
   private JMenuItem mntmEditor;
   private JMenuItem mntmAlsBildKopieren;
   private JMenuItem mntmAlsBildDrucken;
   private JMenuItem mntmBeenden;
   private JMenuItem mntmAnzeigenStrukto;
   private JMenuItem mntmSpeichernStrukto;
   private JMenuItem mntmKopierenStrukto;
   private JMenuItem mntmDruckenStrukto;
   private JMenuItem mntmFigurWechseln;
   private JMenu mnWelt;
   private JMenu mnAblauf;
   private JPanel panelProgramm;
   private JPanel panelError;
   private JPanel panelCodeView;
   private JPanel panelKarol;
   private JPanel panelNavigator;
   private JSplitPane panelSplitterTop;
   private JSplitPane panelSplitterBottom;
   private JSplitPane panelSplitterGesamt;
   private JScrollPane scrollBoxKarol;
   private JScrollPane scrollBoxCodeView;
   private JScrollPane scrollBoxProgramm;
   private ImageIcon[] progLampeIcon = new ImageIcon[4];
   public DlgNeueWelt dlgNeueWelt;
   public DlgOpenSaveFile dlgOpenSave;
   public DlgWeltFestlegen dlgWeltFestlegen;
   public DlgEinstellungK dlgEinstellungK;
   public DlgEinstellungE dlgEinstellungE;
   public DlgAbout dlgAbout;
   public DlgTipps dlgTipps;
   public DlgHilfe dlgHilfe;

   public KarolView(KarolWelt w) {
      this.setMinimumSize(new Dimension(400, 300));
      this.setPreferredSize(new Dimension(940, 700));
      this.setLocation(100, 100);
      this.setTitle("Robot Karol");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(KarolView.class.getResource("/icons/Karol.gif")));
      this.setDefaultCloseOperation(0);
      this.karolWelt = w;
      this.ProgIconsLaden();
      this.GUIAnlegen();
      this.MenuAnlegen();
      this.DialogeAnlegen();
      this.programmName = "";
      this.weltName = "";
      this.viewStatus = KarolView.DisplayStatusType.VST_UNBESTIMMT;
      this.InfoBoxLoeschen();
      this.Umschalten3D2D(false);
   }

   public Karol3D getWelt3DPanel() {
      return this.welt3DPanel;
   }

   public Karol2D getWelt2DPanel() {
      return this.welt2DPanel;
   }

   public KEdtGutter getGutterPanel() {
      return this.progRandspalte;
   }

   public JTextPane getProgTextPanel() {
      return this.progEditorPane;
   }

   public KEdtDocument getProgDocument() {
      return (KEdtDocument)this.progEditorPane.getDocument();
   }

   public KarolStruktogramm getStruktoPanel() {
      return this.struktoPanel;
   }

   public JScrollPane getScrollBoxProgramm() {
      return this.scrollBoxProgramm;
   }

   public FehlerAnzeige getFehlerAnzeige() {
      return this.fehlerAnzeige;
   }

   public KarolView.DisplayStatusType getViewStatus() {
      return this.viewStatus;
   }

   public void setCodeViewTree(JTree tree) {
      this.scrollBoxCodeView.setViewportView(tree);
   }

   public String getMainPfad() {
      return this.mainPfad;
   }

   public String getFileSeparator() {
      return this.fileSeparator;
   }

   public void setMainPfad(String neuerPfad, String f) {
      this.mainPfad = neuerPfad;
      this.fileSeparator = f;
   }

   public void InfoBoxLoeschen() {
      this.fehlerAnzeige.setText("Programm : " + this.programmName + "\n");
      this.fehlerAnzeige.append("Welt : " + this.weltName + "\n");
      this.fehlerAnzeige.append("----------\n \n \n \n");
   }

   public void InfoBoxHinzufuegen(String str) {
      this.fehlerAnzeige.append(str);
   }

   public void InfoBoxHinzufuegen(int zeile, String str) {
      this.fehlerAnzeige.setLineText(zeile, str);
   }

   public void InfoBoxSetWeltName(String s) {
      this.weltName = s;
   }

   public void InfoBoxSetProgrammName(String s) {
      this.programmName = s;
   }

   public void KarolStatusAnzeige(Roboter robo) {
      this.lblPositionXWert.setText(Integer.toString(robo.PositionXGeben()));
      this.lblPositionYWert.setText(Integer.toString(robo.PositionYGeben()));
      this.lblBlickWert.setText(String.valueOf(robo.BlickrichtungGeben()));
      if (robo.RucksackPruefungGeben()) {
         this.lblZiegel.setText("Ziegel:");
         this.lblZiegelWert.setText(Integer.toString(robo.AnzahlZiegelRucksackGeben()));
      } else {
         this.lblZiegel.setText("");
         this.lblZiegelWert.setText("");
      }
   }

   public void ansichtGanzZeichnen(Roboter robo) {
      if (this.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
         this.getWelt3DPanel().zeichneWeltGanz();
      }

      if (this.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
         this.getWelt2DPanel().zeichneWeltGanz();
      }

      this.InfoBoxLoeschen();
      this.KarolStatusAnzeige(robo);
   }

   public void ansichtAktualisieren(KarolRoboter robo) {
      if (this.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
         this.getWelt3DPanel().zeichneWelt();
      }

      if (this.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
         this.getWelt2DPanel().zeichneWelt();
      }

      if (robo.getFehlerText().isEmpty()) {
         this.InfoBoxHinzufuegen(4, " \n");
      } else {
         this.InfoBoxHinzufuegen(4, robo.toString() + robo.getFehlerText() + "\n");
      }

      this.KarolStatusAnzeige(robo);
   }

   public void ProgrammLampeSchalten(KarolProgram.ProgStatusType status) {
      switch(status) {
         case PStNothing:
            this.btnProgLampe.setIcon(this.progLampeIcon[0]);
            break;
         case PStRunning:
            this.btnProgLampe.setIcon(this.progLampeIcon[3]);
            break;
         case PStRunningFast:
            this.btnProgLampe.setIcon(this.progLampeIcon[3]);
            break;
         case PStTracing:
            this.btnProgLampe.setIcon(this.progLampeIcon[2]);
            break;
         case PStStop:
            this.btnProgLampe.setIcon(this.progLampeIcon[1]);
      }

      this.btnProgLampe.invalidate();
   }

   public void ProgrammLampeReset() {
      this.ProgrammLampeSchalten(KarolProgram.ProgStatusType.PStNothing);
   }

   public void BereicheAnordnen() {
      Dimension d = this.getPreferredSize();
      this.setSize(d);
      this.panelSplitterGesamt.resetToPreferredSizes();
      this.panelSplitterTop.resetToPreferredSizes();
      this.panelSplitterBottom.resetToPreferredSizes();
   }

   public void Umschalten3D2D(boolean zweiDWeltAnzeigen) {
      if (zweiDWeltAnzeigen) {
         this.mntmDarstellung.setText("3D Darstellung");
         this.btnDarstell2D.setText("3D");
         this.btnDarstell2D.setFont(new Font("Tahoma", 0, 14));
         this.btnDarstell2D.setToolTipText("zur 3D Darstellung");
         this.mntmWeltDirektFestlegen.setEnabled(true);
         this.mntmAlsBildSpeichern.setEnabled(false);
         this.mntmGrafikVergroessern.setEnabled(false);
         this.mntmGrafikVerkleinern.setEnabled(false);
         this.viewStatus = KarolView.DisplayStatusType.VST_2D;
         this.welt2DPanel.resetWeltAnzeige();
         this.scrollBoxKarol.setViewportView(this.welt2DPanel);
         this.welt2DPanel.zeichneWeltGanz();
         this.welt2DPanel.revalidate();
      } else {
         this.mntmDarstellung.setText("2D Darstellung");
         this.btnDarstell2D.setText("2D");
         this.btnDarstell2D.setFont(new Font("Tahoma", 0, 14));
         this.btnDarstell2D.setToolTipText("zur 2D Darstellung");
         this.mntmWeltDirektFestlegen.setEnabled(false);
         this.dlgWeltFestlegen.hideNonModal();
         this.mntmAlsBildSpeichern.setEnabled(true);
         this.mntmGrafikVergroessern.setEnabled(true);
         this.mntmGrafikVerkleinern.setEnabled(true);
         this.viewStatus = KarolView.DisplayStatusType.VST_3D;
         this.welt3DPanel.resetWeltAnzeige();
         this.scrollBoxKarol.setViewportView(this.welt3DPanel);
         this.welt3DPanel.zoomZuruecksetzen();
         this.welt3DPanel.zeichneWeltGanz();
         this.welt3DPanel.revalidate();
      }
   }

   public void Umschalten3DStrukto(boolean struktoAnzeigen) {
      CardLayout cLayout = (CardLayout)this.panelNavigator.getLayout();
      if (struktoAnzeigen) {
         this.mntmAnzeigenStrukto.setText("Ausblenden");
         this.mntmSpeichernStrukto.setEnabled(true);
         this.mntmKopierenStrukto.setEnabled(true);
         this.mntmDruckenStrukto.setEnabled(true);
         this.mnWelt.setEnabled(false);
         this.mnAblauf.setEnabled(false);
         cLayout.show(this.panelNavigator, "DiagrNavig");
         this.viewStatus = KarolView.DisplayStatusType.VST_DIAGRAMM;
         this.scrollBoxKarol.setViewportView(this.struktoPanel);
         this.struktoPanel.zeichneStruktoGanz();
         this.struktoPanel.revalidate();
      } else {
         this.mntmAnzeigenStrukto.setText("Anzeigen");
         this.mntmSpeichernStrukto.setEnabled(false);
         this.mntmKopierenStrukto.setEnabled(false);
         this.mntmDruckenStrukto.setEnabled(false);
         this.mnWelt.setEnabled(true);
         this.mnAblauf.setEnabled(true);
         cLayout.show(this.panelNavigator, "KarolNavig");
         this.viewStatus = KarolView.DisplayStatusType.VST_3D;
         this.Umschalten3D2D(false);
      }
   }

   public void UmschaltenGrKl(boolean gross, boolean klein) {
      this.mntmGrafikVergroessern.setEnabled(gross);
      this.mntmGrafikVerkleinern.setEnabled(klein);
   }

   private void MenuAnlegen() {
      JMenuBar menuBar = new JMenuBar();
      this.setJMenuBar(menuBar);
      JMenu mnDatei = new JMenu("Datei");
      menuBar.add(mnDatei);
      this.mntmNeu = new JMenuItem("Neu");
      this.mntmNeu.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_new.gif")));
      mnDatei.add(this.mntmNeu);
      this.mntmOeffnen = new JMenuItem("Öffnen ...");
      this.mntmOeffnen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_open.gif")));
      this.mntmOeffnen.setAccelerator(KeyStroke.getKeyStroke(79, 2));
      mnDatei.add(this.mntmOeffnen);
      this.mntmSpeichern = new JMenuItem("Speichern");
      this.mntmSpeichern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_save.gif")));
      this.mntmSpeichern.setAccelerator(KeyStroke.getKeyStroke(83, 2));
      mnDatei.add(this.mntmSpeichern);
      this.mntmSpeichernUnter = new JMenuItem("Speichern unter ...");
      this.mntmSpeichernUnter.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_save_as.gif")));
      this.mntmSpeichernUnter.setAccelerator(KeyStroke.getKeyStroke(123, 0));
      mnDatei.add(this.mntmSpeichernUnter);
      mnDatei.add(new JSeparator());
      this.mntmDrucken = new JMenuItem("Drucken ...");
      this.mntmDrucken.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_printer.gif")));
      this.mntmDrucken.setAccelerator(KeyStroke.getKeyStroke(80, 2));
      mnDatei.add(this.mntmDrucken);
      mnDatei.add(new JSeparator());
      this.mntmBeenden = new JMenuItem("Beenden");
      this.mntmBeenden.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_exit.gif")));
      mnDatei.add(this.mntmBeenden);
      JMenu mnBearbeiten = new JMenu("Bearbeiten");
      menuBar.add(mnBearbeiten);
      JMenuItem mntmRueckgaengig = mnBearbeiten.add(this.progEditorPane.getActionMap().get("undo"));
      mntmRueckgaengig.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_undo.gif")));
      mntmRueckgaengig.setAccelerator(KeyStroke.getKeyStroke(90, 2));
      mntmRueckgaengig.setText("Rückgängig");
      JMenuItem mntmWiderrufen = mnBearbeiten.add(this.progEditorPane.getActionMap().get("redo"));
      mntmWiderrufen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_redo.gif")));
      mntmWiderrufen.setAccelerator(KeyStroke.getKeyStroke(90, 3));
      mntmWiderrufen.setText("Widerrufen");
      mnBearbeiten.add(new JSeparator());
      JMenuItem mntmAusschneiden = mnBearbeiten.add(this.progEditorPane.getActionMap().get("cut-to-clipboard"));
      mntmAusschneiden.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_cut.gif")));
      mntmAusschneiden.setAccelerator(KeyStroke.getKeyStroke(88, 2));
      mntmAusschneiden.setText("Ausschneiden");
      JMenuItem mntmKopieren = mnBearbeiten.add(this.progEditorPane.getActionMap().get("copy-to-clipboard"));
      mntmKopieren.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_copy.gif")));
      mntmKopieren.setAccelerator(KeyStroke.getKeyStroke(67, 2));
      mntmKopieren.setText("Kopieren");
      JMenuItem mntmEinfuegen = mnBearbeiten.add(this.progEditorPane.getActionMap().get("paste-from-clipboard"));
      mntmEinfuegen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_paste.gif")));
      mntmEinfuegen.setAccelerator(KeyStroke.getKeyStroke(86, 2));
      mntmEinfuegen.setText("Einfügen");
      JMenuItem mntmAllesAuswaehlen = mnBearbeiten.add(this.progEditorPane.getActionMap().get("select-all"));
      mntmAllesAuswaehlen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_selectall.gif")));
      mntmAllesAuswaehlen.setAccelerator(KeyStroke.getKeyStroke(65, 2));
      mntmAllesAuswaehlen.setText("Alles auswählen");
      mnBearbeiten.add(new JSeparator());
      JMenuItem mntmGeheZuZeile = mnBearbeiten.add(this.progEditorPane.getActionMap().get("goto-line"));
      mntmGeheZuZeile.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_gotoline.gif")));
      mntmGeheZuZeile.setAccelerator(KeyStroke.getKeyStroke(71, 2));
      mntmGeheZuZeile.setText("Gehe zu Zeile ...");
      mnBearbeiten.add(new JSeparator());
      JMenuItem mntmFormatieren = mnBearbeiten.add(this.progEditorPane.getActionMap().get("formatieren"));
      mntmFormatieren.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_format.gif")));
      mntmFormatieren.setAccelerator(KeyStroke.getKeyStroke(70, 2));
      mntmFormatieren.setText("Formatieren");
      JMenu mnSuchen = new JMenu("Suchen");
      menuBar.add(mnSuchen);
      JMenuItem mntmSuchen = mnSuchen.add(this.progEditorPane.getActionMap().get("find"));
      mntmSuchen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_search.gif")));
      mntmSuchen.setAccelerator(KeyStroke.getKeyStroke(85, 2));
      mntmSuchen.setText("Suchen ...");
      JMenuItem mntmErsetzen = mnSuchen.add(this.progEditorPane.getActionMap().get("replace"));
      mntmErsetzen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_search_replace.gif")));
      mntmErsetzen.setAccelerator(KeyStroke.getKeyStroke(69, 2));
      mntmErsetzen.setText("Ersetzen ...");
      JMenuItem mntmWeitersuchen = mnSuchen.add(this.progEditorPane.getActionMap().get("find-next"));
      mntmWeitersuchen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_search_next.gif")));
      mntmWeitersuchen.setAccelerator(KeyStroke.getKeyStroke(114, 0));
      mntmWeitersuchen.setText("Weitersuchen");
      JMenu mnStruktogramm = new JMenu("Struktogramm");
      menuBar.add(mnStruktogramm);
      this.mntmAnzeigenStrukto = new JMenuItem("Anzeigen");
      this.mntmAnzeigenStrukto.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_diagram.gif")));
      mnStruktogramm.add(this.mntmAnzeigenStrukto);
      this.mntmSpeichernStrukto = new JMenuItem("Speichern");
      this.mntmSpeichernStrukto.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_savepicture.gif")));
      this.mntmSpeichernStrukto.setEnabled(false);
      mnStruktogramm.add(this.mntmSpeichernStrukto);
      this.mntmKopierenStrukto = new JMenuItem("Kopieren");
      this.mntmKopierenStrukto.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_copy.gif")));
      this.mntmKopierenStrukto.setEnabled(false);
      mnStruktogramm.add(this.mntmKopierenStrukto);
      this.mntmDruckenStrukto = new JMenuItem("Drucken");
      this.mntmDruckenStrukto.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_printer.gif")));
      this.mntmDruckenStrukto.setEnabled(false);
      mnStruktogramm.add(this.mntmDruckenStrukto);
      this.mnWelt = new JMenu("Welt");
      menuBar.add(this.mnWelt);
      this.mntmNeueWelt = new JMenuItem("Neue Welt");
      this.mntmNeueWelt.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_new.gif")));
      this.mnWelt.add(this.mntmNeueWelt);
      this.mntmOeffnenWelt = new JMenuItem("Öffnen Welt ...");
      this.mntmOeffnenWelt.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_open.gif")));
      this.mnWelt.add(this.mntmOeffnenWelt);
      this.mntmWeltSpeichern = new JMenuItem("Welt speichern");
      this.mntmWeltSpeichern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_save.gif")));
      this.mnWelt.add(this.mntmWeltSpeichern);
      this.mntmWeltSpeichernUnter = new JMenuItem("Welt speichern unter ...");
      this.mntmWeltSpeichernUnter.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_save_as.gif")));
      this.mnWelt.add(this.mntmWeltSpeichernUnter);
      this.mnWelt.add(new JSeparator());
      this.mntmAlsBildSpeichern = new JMenuItem("Als Bild speichern");
      this.mntmAlsBildSpeichern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_savepicture.gif")));
      this.mnWelt.add(this.mntmAlsBildSpeichern);
      this.mntmAlsBildKopieren = new JMenuItem("Als Bild kopieren");
      this.mntmAlsBildKopieren.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_copy.gif")));
      this.mnWelt.add(this.mntmAlsBildKopieren);
      this.mntmAlsBildDrucken = new JMenuItem("Als Bild drucken");
      this.mntmAlsBildDrucken.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_printer.gif")));
      this.mnWelt.add(this.mntmAlsBildDrucken);
      this.mnWelt.add(new JSeparator());
      this.mntmGrafikVergroessern = new JMenuItem("Bild vergrößern");
      this.mntmGrafikVergroessern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_zoom_gr.gif")));
      this.mntmGrafikVergroessern.setAccelerator(KeyStroke.getKeyStroke(82, 2));
      this.mnWelt.add(this.mntmGrafikVergroessern);
      this.mntmGrafikVerkleinern = new JMenuItem("Bild verkleinern");
      this.mntmGrafikVerkleinern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_zoom_kl.gif")));
      this.mntmGrafikVerkleinern.setAccelerator(KeyStroke.getKeyStroke(75, 2));
      this.mnWelt.add(this.mntmGrafikVerkleinern);
      this.mnWelt.add(new JSeparator());
      this.mntmWeltLoeschen = new JMenuItem("Welt löschen");
      this.mntmWeltLoeschen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_delete_world.gif")));
      this.mnWelt.add(this.mntmWeltLoeschen);
      this.mnWelt.add(new JSeparator());
      this.mntmDarstellung = new JMenuItem("2D Darstellung");
      this.mntmDarstellung.setAccelerator(KeyStroke.getKeyStroke(113, 0));
      this.mnWelt.add(this.mntmDarstellung);
      this.mntmWeltDirektFestlegen = new JMenuItem("Welt direkt festlegen");
      this.mntmWeltDirektFestlegen.setAccelerator(KeyStroke.getKeyStroke(117, 0));
      this.mnWelt.add(this.mntmWeltDirektFestlegen);
      this.mnAblauf = new JMenu("Ablauf");
      menuBar.add(this.mnAblauf);
      this.mntmStart = new JMenuItem("Start");
      this.mntmStart.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_go.gif")));
      this.mntmStart.setAccelerator(KeyStroke.getKeyStroke(120, 0));
      this.mnAblauf.add(this.mntmStart);
      this.mntmSchnelllauf = new JMenuItem("Schnelllauf");
      this.mntmSchnelllauf.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_go_fast.gif")));
      this.mntmSchnelllauf.setAccelerator(KeyStroke.getKeyStroke(120, 1));
      this.mnAblauf.add(this.mntmSchnelllauf);
      this.mntmEinzelschritt = new JMenuItem("Einzelschritt");
      this.mntmEinzelschritt.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_trace1.gif")));
      this.mntmEinzelschritt.setAccelerator(KeyStroke.getKeyStroke(118, 0));
      this.mnAblauf.add(this.mntmEinzelschritt);
      this.mntmPause = new JMenuItem("Pause");
      this.mntmPause.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_pause.gif")));
      this.mntmPause.setAccelerator(KeyStroke.getKeyStroke(113, 0));
      this.mnAblauf.add(this.mntmPause);
      this.mntmStopp = new JMenuItem("Stopp");
      this.mntmStopp.setAccelerator(KeyStroke.getKeyStroke(113, 2));
      this.mntmStopp.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_stop.gif")));
      this.mnAblauf.add(this.mntmStopp);
      this.mntmStopppunkt = new JMenuItem("Stopp-Punkt");
      this.mntmStopppunkt.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_break.gif")));
      this.mntmStopppunkt.setAccelerator(KeyStroke.getKeyStroke(66, 2));
      this.mnAblauf.add(this.mntmStopppunkt);
      this.mntmSyntaxpruefung = new JMenuItem("Syntaxprüfung");
      this.mntmSyntaxpruefung.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_check.gif")));
      this.mnAblauf.add(this.mntmSyntaxpruefung);
      JMenu mnEinstellungen = new JMenu("Einstellungen");
      menuBar.add(mnEinstellungen);
      this.mntmBereicheAnordnen = new JMenuItem("Bereiche anordnen");
      this.mntmBereicheAnordnen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_anordnen.gif")));
      mnEinstellungen.add(this.mntmBereicheAnordnen);
      mnEinstellungen.add(new JSeparator());
      this.mntmKarol = new JMenuItem("Karol");
      this.mntmKarol.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_karol.gif")));
      mnEinstellungen.add(this.mntmKarol);
      this.mntmEditor = new JMenuItem("Editor");
      this.mntmEditor.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_editor.gif")));
      mnEinstellungen.add(this.mntmEditor);
      mnEinstellungen.add(new JSeparator());
      this.mntmFigurWechseln = new JMenuItem("Figur wechseln");
      this.mntmFigurWechseln.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_wechsel.gif")));
      mnEinstellungen.add(this.mntmFigurWechseln);
      JMenu mnHilfe = new JMenu("Hilfe");
      menuBar.add(mnHilfe);
      this.mntmInhalt = new JMenuItem("Inhalt");
      this.mntmInhalt.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_help.gif")));
      this.mntmInhalt.setAccelerator(KeyStroke.getKeyStroke(112, 0));
      mnHilfe.add(this.mntmInhalt);
      this.mntmTipps = new JMenuItem("Tipps");
      this.mntmTipps.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_tips.gif")));
      mnHilfe.add(this.mntmTipps);
      this.mntmProgramminfo = new JMenuItem("Programminfo");
      this.mntmProgramminfo.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_about.gif")));
      mnHilfe.add(this.mntmProgramminfo);
   }

   private void GUIAnlegen() {
      this.getContentPane().setLayout(new BorderLayout(0, 0));
      this.panelSplitterGesamt = new JSplitPane();
      this.panelSplitterGesamt.setMinimumSize(new Dimension(400, 250));
      this.panelSplitterGesamt.setPreferredSize(new Dimension(940, 650));
      this.panelSplitterGesamt.setDividerSize(3);
      this.panelSplitterGesamt.setOrientation(0);
      this.getContentPane().add(this.panelSplitterGesamt, "Center");
      this.panelSplitterTop = new JSplitPane();
      this.panelSplitterTop.setResizeWeight(0.6);
      this.panelSplitterTop.setBorder(new BevelBorder(1, null, null, null, null));
      this.panelSplitterTop.setMinimumSize(new Dimension(400, 185));
      this.panelSplitterTop.setPreferredSize(new Dimension(940, 440));
      this.panelSplitterGesamt.setLeftComponent(this.panelSplitterTop);
      this.panelProgramm = new JPanel();
      this.panelProgramm.setMinimumSize(new Dimension(164, 185));
      this.panelProgramm.setPreferredSize(new Dimension(390, 440));
      this.panelProgramm.setLayout(new BorderLayout(0, 0));
      this.panelSplitterTop.setLeftComponent(this.panelProgramm);
      this.scrollBoxProgramm = new JScrollPane();
      this.panelProgramm.add(this.scrollBoxProgramm, "Center");
      this.progEditorPane = new JTextPane() {
         @Override
         public boolean getScrollableTracksViewportWidth() {
            return this.getUI().getPreferredSize(this).width <= this.getParent().getSize().width;
         }
      };
      this.progEditorPane.setEditorKitForContentType("text/Karol", new KEdtEditorKit(this));
      this.progEditorPane.setContentType("text/Karol");
      this.scrollBoxProgramm.setViewportView(this.progEditorPane);
      this.progRandspalte = new KEdtGutter(this.progEditorPane);
      this.scrollBoxProgramm.setRowHeaderView(this.progRandspalte);
      JPanel panelProgrammNavigator = new JPanel();
      panelProgrammNavigator.setToolTipText("");
      panelProgrammNavigator.setName("");
      panelProgrammNavigator.setBorder(new BevelBorder(0, null, null, null, null));
      panelProgrammNavigator.setPreferredSize(new Dimension(390, 30));
      panelProgrammNavigator.setAlignmentX(0.0F);
      panelProgrammNavigator.setAlignmentY(1.0F);
      this.panelProgramm.add(panelProgrammNavigator, "South");
      panelProgrammNavigator.setLayout(new BoxLayout(panelProgrammNavigator, 0));
      this.btnProgNeu = new JButton("");
      this.btnProgNeu.setContentAreaFilled(false);
      this.btnProgNeu.setBorderPainted(false);
      this.btnProgNeu.setBorder(null);
      this.btnProgNeu.setToolTipText("Neues Programm");
      this.btnProgNeu.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_new.gif")));
      this.btnProgNeu.setMaximumSize(new Dimension(22, 24));
      this.btnProgNeu.setMinimumSize(new Dimension(22, 24));
      this.btnProgNeu.setPreferredSize(new Dimension(22, 24));
      panelProgrammNavigator.add(this.btnProgNeu);
      this.btnProgOeffnen = new JButton("");
      this.btnProgOeffnen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_open.gif")));
      this.btnProgOeffnen.setToolTipText("Programm öffnen");
      this.btnProgOeffnen.setPreferredSize(new Dimension(22, 24));
      this.btnProgOeffnen.setMinimumSize(new Dimension(22, 24));
      this.btnProgOeffnen.setMaximumSize(new Dimension(22, 24));
      this.btnProgOeffnen.setContentAreaFilled(false);
      this.btnProgOeffnen.setBorderPainted(false);
      this.btnProgOeffnen.setBorder(null);
      panelProgrammNavigator.add(this.btnProgOeffnen);
      this.btnProgSpeichern = new JButton("");
      this.btnProgSpeichern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_save.gif")));
      this.btnProgSpeichern.setToolTipText("Programm speichern");
      this.btnProgSpeichern.setPreferredSize(new Dimension(22, 24));
      this.btnProgSpeichern.setMinimumSize(new Dimension(22, 24));
      this.btnProgSpeichern.setMaximumSize(new Dimension(22, 24));
      this.btnProgSpeichern.setContentAreaFilled(false);
      this.btnProgSpeichern.setBorderPainted(false);
      this.btnProgSpeichern.setBorder(null);
      panelProgrammNavigator.add(this.btnProgSpeichern);
      JSeparator separator_10 = new JSeparator();
      separator_10.setPreferredSize(new Dimension(3, 24));
      separator_10.setMinimumSize(new Dimension(3, 24));
      separator_10.setBorder(new BevelBorder(1, null, null, null, null));
      separator_10.setMaximumSize(new Dimension(3, 32767));
      panelProgrammNavigator.add(separator_10);
      this.btnSyntax = new JButton("");
      this.btnSyntax.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_check.gif")));
      this.btnSyntax.setToolTipText("Syntaxprüfung");
      this.btnSyntax.setPreferredSize(new Dimension(22, 24));
      this.btnSyntax.setMinimumSize(new Dimension(22, 24));
      this.btnSyntax.setMaximumSize(new Dimension(22, 24));
      this.btnSyntax.setContentAreaFilled(false);
      this.btnSyntax.setBorderPainted(false);
      this.btnSyntax.setBorder(null);
      panelProgrammNavigator.add(this.btnSyntax);
      JSeparator separator_11 = new JSeparator();
      separator_11.setPreferredSize(new Dimension(3, 24));
      separator_11.setMinimumSize(new Dimension(3, 24));
      separator_11.setMaximumSize(new Dimension(3, 24));
      separator_11.setBorder(new BevelBorder(1, null, null, null, null));
      panelProgrammNavigator.add(separator_11);
      this.btnAblaufNormal = new JButton("");
      this.btnAblaufNormal.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_go.gif")));
      this.btnAblaufNormal.setToolTipText("Programmstart");
      this.btnAblaufNormal.setPreferredSize(new Dimension(22, 24));
      this.btnAblaufNormal.setMinimumSize(new Dimension(22, 24));
      this.btnAblaufNormal.setMaximumSize(new Dimension(22, 24));
      this.btnAblaufNormal.setContentAreaFilled(false);
      this.btnAblaufNormal.setBorderPainted(false);
      this.btnAblaufNormal.setBorder(null);
      panelProgrammNavigator.add(this.btnAblaufNormal);
      this.btnAblaufSchnell = new JButton("");
      this.btnAblaufSchnell.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_go_fast.gif")));
      this.btnAblaufSchnell.setToolTipText("Schnelldurchlauf");
      this.btnAblaufSchnell.setPreferredSize(new Dimension(22, 24));
      this.btnAblaufSchnell.setMinimumSize(new Dimension(22, 24));
      this.btnAblaufSchnell.setMaximumSize(new Dimension(22, 24));
      this.btnAblaufSchnell.setContentAreaFilled(false);
      this.btnAblaufSchnell.setBorderPainted(false);
      this.btnAblaufSchnell.setBorder(null);
      panelProgrammNavigator.add(this.btnAblaufSchnell);
      this.btnAblaufEinzeln = new JButton("");
      this.btnAblaufEinzeln.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_trace1.gif")));
      this.btnAblaufEinzeln.setToolTipText("Einzelschritt");
      this.btnAblaufEinzeln.setPreferredSize(new Dimension(22, 24));
      this.btnAblaufEinzeln.setMinimumSize(new Dimension(22, 24));
      this.btnAblaufEinzeln.setMaximumSize(new Dimension(22, 24));
      this.btnAblaufEinzeln.setContentAreaFilled(false);
      this.btnAblaufEinzeln.setBorderPainted(false);
      this.btnAblaufEinzeln.setBorder(null);
      panelProgrammNavigator.add(this.btnAblaufEinzeln);
      this.btnAblaufPause = new JButton("");
      this.btnAblaufPause.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_pause.gif")));
      this.btnAblaufPause.setToolTipText("Pause");
      this.btnAblaufPause.setPreferredSize(new Dimension(22, 24));
      this.btnAblaufPause.setMinimumSize(new Dimension(22, 24));
      this.btnAblaufPause.setMaximumSize(new Dimension(22, 24));
      this.btnAblaufPause.setContentAreaFilled(false);
      this.btnAblaufPause.setBorderPainted(false);
      this.btnAblaufPause.setBorder(null);
      panelProgrammNavigator.add(this.btnAblaufPause);
      this.btnAblaufStopp = new JButton("");
      this.btnAblaufStopp.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_stop.gif")));
      this.btnAblaufStopp.setToolTipText("Abrechen");
      this.btnAblaufStopp.setPreferredSize(new Dimension(22, 24));
      this.btnAblaufStopp.setMinimumSize(new Dimension(22, 24));
      this.btnAblaufStopp.setMaximumSize(new Dimension(22, 24));
      this.btnAblaufStopp.setContentAreaFilled(false);
      this.btnAblaufStopp.setBorderPainted(false);
      this.btnAblaufStopp.setBorder(null);
      panelProgrammNavigator.add(this.btnAblaufStopp);
      JSeparator separator_12 = new JSeparator();
      separator_12.setPreferredSize(new Dimension(3, 24));
      separator_12.setMinimumSize(new Dimension(3, 24));
      separator_12.setMaximumSize(new Dimension(3, 24));
      separator_12.setBorder(new BevelBorder(1, null, null, null, null));
      panelProgrammNavigator.add(separator_12);
      this.btnProgLampe = new JButton("");
      this.btnProgLampe.setIcon(this.progLampeIcon[0]);
      this.btnProgLampe.setToolTipText("Programmstatus Anzeige");
      this.btnProgLampe.setPreferredSize(new Dimension(22, 24));
      this.btnProgLampe.setMinimumSize(new Dimension(22, 24));
      this.btnProgLampe.setMaximumSize(new Dimension(22, 24));
      this.btnProgLampe.setContentAreaFilled(false);
      this.btnProgLampe.setBorderPainted(false);
      this.btnProgLampe.setBorder(null);
      panelProgrammNavigator.add(this.btnProgLampe);
      Component horizontalGlue = Box.createHorizontalGlue();
      horizontalGlue.setMaximumSize(new Dimension(32767, 24));
      panelProgrammNavigator.add(horizontalGlue);
      JLabel lblProgramm = new JLabel("  Programm  ");
      lblProgramm.setMaximumSize(new Dimension(60, 24));
      lblProgramm.setFont(new Font("Tahoma", 1, 11));
      lblProgramm.setForeground(new Color(255, 69, 0));
      lblProgramm.setHorizontalTextPosition(4);
      lblProgramm.setHorizontalAlignment(0);
      lblProgramm.setAlignmentX(1.0F);
      panelProgrammNavigator.add(lblProgramm);
      this.panelKarol = new JPanel();
      this.panelKarol.setMinimumSize(new Dimension(219, 185));
      this.panelKarol.setPreferredSize(new Dimension(520, 440));
      this.panelKarol.setLayout(new BorderLayout(0, 0));
      this.panelSplitterTop.setRightComponent(this.panelKarol);
      this.panelNavigator = new JPanel();
      this.panelNavigator.setMinimumSize(new Dimension(219, 30));
      this.panelNavigator.setPreferredSize(new Dimension(520, 30));
      this.panelKarol.add(this.panelNavigator, "South");
      this.panelNavigator.setLayout(new CardLayout(0, 0));
      JPanel panelKarolNavigator = new JPanel();
      this.panelNavigator.add(panelKarolNavigator, "KarolNavig");
      panelKarolNavigator.setBorder(new BevelBorder(0, null, null, null, null));
      panelKarolNavigator.setPreferredSize(new Dimension(520, 30));
      panelKarolNavigator.setMinimumSize(new Dimension(219, 30));
      panelKarolNavigator.setLayout(new BoxLayout(panelKarolNavigator, 0));
      this.btnWeltNeu = new JButton("");
      this.btnWeltNeu.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_new.gif")));
      this.btnWeltNeu.setToolTipText("Neue Welt");
      this.btnWeltNeu.setPreferredSize(new Dimension(22, 24));
      this.btnWeltNeu.setMinimumSize(new Dimension(22, 24));
      this.btnWeltNeu.setMaximumSize(new Dimension(22, 24));
      this.btnWeltNeu.setContentAreaFilled(false);
      this.btnWeltNeu.setBorderPainted(false);
      this.btnWeltNeu.setBorder(null);
      panelKarolNavigator.add(this.btnWeltNeu);
      this.btnWeltOeffnen = new JButton("");
      this.btnWeltOeffnen.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_open.gif")));
      this.btnWeltOeffnen.setToolTipText("Welt öffnen");
      this.btnWeltOeffnen.setPreferredSize(new Dimension(22, 24));
      this.btnWeltOeffnen.setMinimumSize(new Dimension(22, 24));
      this.btnWeltOeffnen.setMaximumSize(new Dimension(22, 24));
      this.btnWeltOeffnen.setContentAreaFilled(false);
      this.btnWeltOeffnen.setBorderPainted(false);
      this.btnWeltOeffnen.setBorder(null);
      panelKarolNavigator.add(this.btnWeltOeffnen);
      this.btnWeltSpeichern = new JButton("");
      this.btnWeltSpeichern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_save.gif")));
      this.btnWeltSpeichern.setToolTipText("Welt speichern");
      this.btnWeltSpeichern.setPreferredSize(new Dimension(22, 24));
      this.btnWeltSpeichern.setMinimumSize(new Dimension(22, 24));
      this.btnWeltSpeichern.setMaximumSize(new Dimension(22, 24));
      this.btnWeltSpeichern.setContentAreaFilled(false);
      this.btnWeltSpeichern.setBorderPainted(false);
      this.btnWeltSpeichern.setBorder(null);
      panelKarolNavigator.add(this.btnWeltSpeichern);
      JSeparator separator_13 = new JSeparator();
      separator_13.setPreferredSize(new Dimension(3, 24));
      separator_13.setMinimumSize(new Dimension(3, 24));
      separator_13.setMaximumSize(new Dimension(3, 32767));
      separator_13.setBorder(new BevelBorder(1, null, null, null, null));
      panelKarolNavigator.add(separator_13);
      this.btnKarolLinks = new JButton("");
      this.btnKarolLinks.setIcon(new ImageIcon(KarolView.class.getResource("/icons/arrow_left.gif")));
      this.btnKarolLinks.setToolTipText("LinksDrehen");
      this.btnKarolLinks.setPreferredSize(new Dimension(22, 24));
      this.btnKarolLinks.setMinimumSize(new Dimension(22, 24));
      this.btnKarolLinks.setMaximumSize(new Dimension(22, 24));
      this.btnKarolLinks.setContentAreaFilled(false);
      this.btnKarolLinks.setBorderPainted(false);
      this.btnKarolLinks.setBorder(null);
      panelKarolNavigator.add(this.btnKarolLinks);
      this.btnKarolVor = new JButton("");
      this.btnKarolVor.setIcon(new ImageIcon(KarolView.class.getResource("/icons/arrow_forward.gif")));
      this.btnKarolVor.setToolTipText("Schritt");
      this.btnKarolVor.setPreferredSize(new Dimension(22, 24));
      this.btnKarolVor.setMinimumSize(new Dimension(22, 24));
      this.btnKarolVor.setMaximumSize(new Dimension(22, 24));
      this.btnKarolVor.setContentAreaFilled(false);
      this.btnKarolVor.setBorderPainted(false);
      this.btnKarolVor.setBorder(null);
      panelKarolNavigator.add(this.btnKarolVor);
      this.btnKarolRechts = new JButton("");
      this.btnKarolRechts.setIcon(new ImageIcon(KarolView.class.getResource("/icons/arrow_right.gif")));
      this.btnKarolRechts.setToolTipText("RechtsDrehen");
      this.btnKarolRechts.setPreferredSize(new Dimension(22, 24));
      this.btnKarolRechts.setMinimumSize(new Dimension(22, 24));
      this.btnKarolRechts.setMaximumSize(new Dimension(22, 24));
      this.btnKarolRechts.setContentAreaFilled(false);
      this.btnKarolRechts.setBorderPainted(false);
      this.btnKarolRechts.setBorder(null);
      panelKarolNavigator.add(this.btnKarolRechts);
      JSeparator separator_14 = new JSeparator();
      separator_14.setPreferredSize(new Dimension(3, 24));
      separator_14.setMinimumSize(new Dimension(3, 24));
      separator_14.setMaximumSize(new Dimension(3, 32767));
      separator_14.setBorder(new BevelBorder(1, null, null, null, null));
      panelKarolNavigator.add(separator_14);
      this.btnKarolHinlegen = new JButton("H");
      this.btnKarolHinlegen.setFont(new Font("Tahoma", 0, 14));
      this.btnKarolHinlegen.setToolTipText("Ziegel hinlegen");
      this.btnKarolHinlegen.setPreferredSize(new Dimension(22, 24));
      this.btnKarolHinlegen.setMinimumSize(new Dimension(22, 24));
      this.btnKarolHinlegen.setMaximumSize(new Dimension(22, 24));
      this.btnKarolHinlegen.setContentAreaFilled(false);
      this.btnKarolHinlegen.setBorderPainted(false);
      this.btnKarolHinlegen.setBorder(null);
      panelKarolNavigator.add(this.btnKarolHinlegen);
      this.btnKarolAufheben = new JButton("A");
      this.btnKarolAufheben.setFont(new Font("Tahoma", 0, 14));
      this.btnKarolAufheben.setToolTipText("Ziegel aufheben");
      this.btnKarolAufheben.setPreferredSize(new Dimension(22, 24));
      this.btnKarolAufheben.setMinimumSize(new Dimension(22, 24));
      this.btnKarolAufheben.setMaximumSize(new Dimension(22, 24));
      this.btnKarolAufheben.setContentAreaFilled(false);
      this.btnKarolAufheben.setBorderPainted(false);
      this.btnKarolAufheben.setBorder(null);
      panelKarolNavigator.add(this.btnKarolAufheben);
      this.btnKarolMarke = new JButton("M");
      this.btnKarolMarke.setFont(new Font("Tahoma", 0, 14));
      this.btnKarolMarke.setToolTipText("Marke setzen/löschen");
      this.btnKarolMarke.setPreferredSize(new Dimension(22, 24));
      this.btnKarolMarke.setMinimumSize(new Dimension(22, 24));
      this.btnKarolMarke.setMaximumSize(new Dimension(22, 24));
      this.btnKarolMarke.setContentAreaFilled(false);
      this.btnKarolMarke.setBorderPainted(false);
      this.btnKarolMarke.setBorder(null);
      panelKarolNavigator.add(this.btnKarolMarke);
      this.btnKarolQuaderAbl = new JButton("Q");
      this.btnKarolQuaderAbl.setFont(new Font("Tahoma", 0, 14));
      this.btnKarolQuaderAbl.setToolTipText("Quader aufstellen");
      this.btnKarolQuaderAbl.setPreferredSize(new Dimension(22, 24));
      this.btnKarolQuaderAbl.setMinimumSize(new Dimension(22, 24));
      this.btnKarolQuaderAbl.setMaximumSize(new Dimension(22, 24));
      this.btnKarolQuaderAbl.setContentAreaFilled(false);
      this.btnKarolQuaderAbl.setBorderPainted(false);
      this.btnKarolQuaderAbl.setBorder(null);
      panelKarolNavigator.add(this.btnKarolQuaderAbl);
      this.btnKarolQuaderEntf = new JButton("E");
      this.btnKarolQuaderEntf.setFont(new Font("Tahoma", 0, 14));
      this.btnKarolQuaderEntf.setToolTipText("Quader entfernen");
      this.btnKarolQuaderEntf.setPreferredSize(new Dimension(22, 24));
      this.btnKarolQuaderEntf.setMinimumSize(new Dimension(22, 24));
      this.btnKarolQuaderEntf.setMaximumSize(new Dimension(22, 24));
      this.btnKarolQuaderEntf.setContentAreaFilled(false);
      this.btnKarolQuaderEntf.setBorderPainted(false);
      this.btnKarolQuaderEntf.setBorder(null);
      panelKarolNavigator.add(this.btnKarolQuaderEntf);
      JSeparator separator_15 = new JSeparator();
      separator_15.setPreferredSize(new Dimension(3, 24));
      separator_15.setMinimumSize(new Dimension(3, 24));
      separator_15.setMaximumSize(new Dimension(3, 32767));
      separator_15.setBorder(new BevelBorder(1, null, null, null, null));
      panelKarolNavigator.add(separator_15);
      this.btnWeltLoeschen = new JButton("L");
      this.btnWeltLoeschen.setFont(new Font("Tahoma", 0, 14));
      this.btnWeltLoeschen.setToolTipText("Welt löschen");
      this.btnWeltLoeschen.setPreferredSize(new Dimension(22, 24));
      this.btnWeltLoeschen.setMinimumSize(new Dimension(22, 24));
      this.btnWeltLoeschen.setMaximumSize(new Dimension(22, 24));
      this.btnWeltLoeschen.setContentAreaFilled(false);
      this.btnWeltLoeschen.setBorderPainted(false);
      this.btnWeltLoeschen.setBorder(null);
      panelKarolNavigator.add(this.btnWeltLoeschen);
      this.btnWeltWiederherstellen = new JButton("W");
      this.btnWeltWiederherstellen.setFont(new Font("Tahoma", 0, 14));
      this.btnWeltWiederherstellen.setToolTipText("Welt wiederherstellen");
      this.btnWeltWiederherstellen.setPreferredSize(new Dimension(22, 24));
      this.btnWeltWiederherstellen.setMinimumSize(new Dimension(22, 24));
      this.btnWeltWiederherstellen.setMaximumSize(new Dimension(22, 24));
      this.btnWeltWiederherstellen.setContentAreaFilled(false);
      this.btnWeltWiederherstellen.setBorderPainted(false);
      this.btnWeltWiederherstellen.setBorder(null);
      panelKarolNavigator.add(this.btnWeltWiederherstellen);
      JSeparator separator_16 = new JSeparator();
      separator_16.setPreferredSize(new Dimension(3, 24));
      separator_16.setMinimumSize(new Dimension(3, 24));
      separator_16.setMaximumSize(new Dimension(3, 32767));
      separator_16.setBorder(new BevelBorder(1, null, null, null, null));
      panelKarolNavigator.add(separator_16);
      this.btnDarstell2D = new JButton("2D");
      this.btnDarstell2D.setFont(new Font("Tahoma", 0, 14));
      this.btnDarstell2D.setToolTipText("2D Darstellung");
      this.btnDarstell2D.setPreferredSize(new Dimension(30, 24));
      this.btnDarstell2D.setMinimumSize(new Dimension(30, 24));
      this.btnDarstell2D.setMaximumSize(new Dimension(30, 24));
      this.btnDarstell2D.setContentAreaFilled(false);
      this.btnDarstell2D.setBorderPainted(false);
      this.btnDarstell2D.setBorder(null);
      panelKarolNavigator.add(this.btnDarstell2D);
      Component horizontalGlue_1 = Box.createHorizontalGlue();
      horizontalGlue_1.setMaximumSize(new Dimension(32767, 24));
      panelKarolNavigator.add(horizontalGlue_1);
      JLabel lblWelt = new JLabel("  Welt  ");
      lblWelt.setMaximumSize(new Dimension(60, 24));
      lblWelt.setHorizontalTextPosition(4);
      lblWelt.setHorizontalAlignment(0);
      lblWelt.setForeground(new Color(255, 69, 0));
      lblWelt.setFont(new Font("Tahoma", 1, 11));
      lblWelt.setAlignmentX(1.0F);
      panelKarolNavigator.add(lblWelt);
      JPanel panelDiagrNavigator = new JPanel();
      panelDiagrNavigator.setPreferredSize(new Dimension(520, 30));
      panelDiagrNavigator.setMinimumSize(new Dimension(219, 30));
      panelDiagrNavigator.setBorder(new BevelBorder(0, null, null, null, null));
      panelDiagrNavigator.setAlignmentY(1.0F);
      panelDiagrNavigator.setAlignmentX(0.0F);
      this.panelNavigator.add(panelDiagrNavigator, "DiagrNavig");
      panelDiagrNavigator.setLayout(new BoxLayout(panelDiagrNavigator, 0));
      this.btnDiagrSpeichern = new JButton("");
      this.btnDiagrSpeichern.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_savepicture.gif")));
      this.btnDiagrSpeichern.setToolTipText("Struktogramm speichern");
      this.btnDiagrSpeichern.setPreferredSize(new Dimension(22, 24));
      this.btnDiagrSpeichern.setMinimumSize(new Dimension(22, 24));
      this.btnDiagrSpeichern.setMaximumSize(new Dimension(22, 24));
      this.btnDiagrSpeichern.setContentAreaFilled(false);
      this.btnDiagrSpeichern.setBorderPainted(false);
      this.btnDiagrSpeichern.setBorder(null);
      panelDiagrNavigator.add(this.btnDiagrSpeichern);
      this.btnDiagrKopieren = new JButton("");
      this.btnDiagrKopieren.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_copy.gif")));
      this.btnDiagrKopieren.setToolTipText("Struktogramm in Zwischenablage kopieren");
      this.btnDiagrKopieren.setPreferredSize(new Dimension(22, 24));
      this.btnDiagrKopieren.setMinimumSize(new Dimension(22, 24));
      this.btnDiagrKopieren.setMaximumSize(new Dimension(22, 24));
      this.btnDiagrKopieren.setContentAreaFilled(false);
      this.btnDiagrKopieren.setBorderPainted(false);
      this.btnDiagrKopieren.setBorder(null);
      panelDiagrNavigator.add(this.btnDiagrKopieren);
      this.btnDiagrDrucken = new JButton("");
      this.btnDiagrDrucken.setIcon(new ImageIcon(KarolView.class.getResource("/icons/menu_printer.gif")));
      this.btnDiagrDrucken.setToolTipText("Struktogramm drucken");
      this.btnDiagrDrucken.setPreferredSize(new Dimension(22, 24));
      this.btnDiagrDrucken.setMinimumSize(new Dimension(22, 24));
      this.btnDiagrDrucken.setMaximumSize(new Dimension(22, 24));
      this.btnDiagrDrucken.setContentAreaFilled(false);
      this.btnDiagrDrucken.setBorderPainted(false);
      this.btnDiagrDrucken.setBorder(null);
      panelDiagrNavigator.add(this.btnDiagrDrucken);
      Component rigidAreaDiagr = Box.createRigidArea(new Dimension(20, 10));
      rigidAreaDiagr.setPreferredSize(new Dimension(20, 10));
      panelDiagrNavigator.add(rigidAreaDiagr);
      this.btnDiagrEnde = new JButton("Struktogramm ausblenden");
      this.btnDiagrEnde.setToolTipText("Struktogramm drucken");
      this.btnDiagrEnde.setPreferredSize(new Dimension(138, 24));
      this.btnDiagrEnde.setMinimumSize(new Dimension(138, 24));
      this.btnDiagrEnde.setMaximumSize(new Dimension(138, 24));
      this.btnDiagrEnde.setContentAreaFilled(false);
      this.btnDiagrEnde.setBorder(new BevelBorder(0, null, null, null, null));
      panelDiagrNavigator.add(this.btnDiagrEnde);
      Component horizontalGlue_2 = Box.createHorizontalGlue();
      horizontalGlue_2.setMaximumSize(new Dimension(32767, 24));
      panelDiagrNavigator.add(horizontalGlue_2);
      JLabel lblStruktogramm = new JLabel("  Struktogramm  ");
      lblStruktogramm.setPreferredSize(new Dimension(100, 14));
      lblStruktogramm.setMinimumSize(new Dimension(100, 14));
      lblStruktogramm.setMaximumSize(new Dimension(100, 24));
      lblStruktogramm.setHorizontalTextPosition(4);
      lblStruktogramm.setHorizontalAlignment(4);
      lblStruktogramm.setForeground(new Color(255, 69, 0));
      lblStruktogramm.setFont(new Font("Tahoma", 1, 11));
      lblStruktogramm.setAlignmentX(1.0F);
      panelDiagrNavigator.add(lblStruktogramm);
      this.scrollBoxKarol = new JScrollPane();
      this.scrollBoxKarol.setMinimumSize(new Dimension(219, 172));
      this.scrollBoxKarol.setEnabled(false);
      this.scrollBoxKarol.setFocusable(false);
      this.scrollBoxKarol.setViewportBorder(null);
      this.scrollBoxKarol.setBorder(null);
      this.scrollBoxKarol.setForeground(Color.WHITE);
      this.scrollBoxKarol.setBackground(Color.WHITE);
      this.scrollBoxKarol.getViewport().setBackground(Color.WHITE);
      this.scrollBoxKarol.setPreferredSize(new Dimension(520, 410));
      this.panelKarol.add(this.scrollBoxKarol);
      this.welt3DPanel = new Karol3D(this.karolWelt);
      this.welt3DPanel.setPreferredSize(new Dimension(510, 400));
      this.welt3DPanel.setMinimumSize(new Dimension(214, 168));
      this.welt3DPanel.setForeground(Color.WHITE);
      this.welt3DPanel.setBackground(Color.WHITE);
      this.welt3DPanel.setToolTipText("Nach Klick auf die Welt ist Karol mit Tasten bewegbar.");
      this.welt2DPanel = new Karol2D(this.karolWelt);
      this.welt2DPanel.setPreferredSize(new Dimension(510, 400));
      this.welt2DPanel.setMinimumSize(new Dimension(214, 168));
      this.welt2DPanel.setForeground(Color.WHITE);
      this.welt2DPanel.setBackground(Color.WHITE);
      this.welt2DPanel.setToolTipText("Nach Klick auf die Welt ist Karol mit Tasten bewegbar.");
      this.struktoPanel = new KarolStruktogramm();
      this.struktoPanel.setPreferredSize(new Dimension(510, 400));
      this.struktoPanel.setMinimumSize(new Dimension(214, 168));
      this.struktoPanel.setForeground(Color.WHITE);
      this.struktoPanel.setBackground(Color.WHITE);
      this.struktoPanel.setToolTipText("");
      this.panelSplitterBottom = new JSplitPane();
      this.panelSplitterBottom.setResizeWeight(0.4);
      this.panelSplitterBottom.setBorder(new BevelBorder(1, null, null, null, null));
      this.panelSplitterBottom.setMinimumSize(new Dimension(395, 80));
      this.panelSplitterBottom.setPreferredSize(new Dimension(940, 190));
      this.panelSplitterGesamt.setRightComponent(this.panelSplitterBottom);
      this.panelCodeView = new JPanel();
      this.panelCodeView.setPreferredSize(new Dimension(300, 190));
      this.panelCodeView.setMinimumSize(new Dimension(105, 80));
      this.panelCodeView.setLayout(new BorderLayout(0, 0));
      this.panelSplitterBottom.setLeftComponent(this.panelCodeView);
      JPanel panelUebersicht = new JPanel();
      panelUebersicht.setMinimumSize(new Dimension(105, 30));
      panelUebersicht.setPreferredSize(new Dimension(250, 30));
      panelUebersicht.setBorder(new BevelBorder(0, null, null, null, null));
      panelUebersicht.setAlignmentY(1.0F);
      panelUebersicht.setAlignmentX(0.0F);
      panelUebersicht.setLayout(new BoxLayout(panelUebersicht, 0));
      this.panelCodeView.add(panelUebersicht, "South");
      this.btnUebersicht = new JButton("");
      this.btnUebersicht.setIcon(new ImageIcon(KarolView.class.getResource("/icons/button_update.gif")));
      this.btnUebersicht.setToolTipText("Übersicht aktualisieren");
      this.btnUebersicht.setPreferredSize(new Dimension(22, 24));
      this.btnUebersicht.setMinimumSize(new Dimension(22, 24));
      this.btnUebersicht.setMaximumSize(new Dimension(22, 24));
      this.btnUebersicht.setContentAreaFilled(false);
      this.btnUebersicht.setBorderPainted(false);
      this.btnUebersicht.setBorder(null);
      panelUebersicht.add(this.btnUebersicht);
      Component horizontalGlue_3 = Box.createHorizontalGlue();
      horizontalGlue_3.setEnabled(false);
      horizontalGlue_3.setMaximumSize(new Dimension(32767, 24));
      panelUebersicht.add(horizontalGlue_3);
      JLabel lblUebersicht = new JLabel("  Übersicht  ");
      lblUebersicht.setPreferredSize(new Dimension(72, 14));
      lblUebersicht.setMaximumSize(new Dimension(72, 24));
      lblUebersicht.setHorizontalTextPosition(4);
      lblUebersicht.setHorizontalAlignment(0);
      lblUebersicht.setForeground(new Color(255, 69, 0));
      lblUebersicht.setFont(new Font("Tahoma", 1, 11));
      lblUebersicht.setAlignmentX(1.0F);
      panelUebersicht.add(lblUebersicht);
      this.scrollBoxCodeView = new JScrollPane();
      this.scrollBoxCodeView.setViewportBorder(new EmptyBorder(5, 5, 5, 5));
      this.scrollBoxCodeView.setBackground(Color.WHITE);
      this.panelCodeView.add(this.scrollBoxCodeView, "Center");
      this.panelError = new JPanel();
      this.panelError.setPreferredSize(new Dimension(600, 190));
      this.panelError.setMinimumSize(new Dimension(277, 80));
      this.panelError.setLayout(new BorderLayout(0, 0));
      this.panelSplitterBottom.setRightComponent(this.panelError);
      JPanel panelInformation = new JPanel();
      panelInformation.setMinimumSize(new Dimension(277, 30));
      panelInformation.setPreferredSize(new Dimension(660, 30));
      panelInformation.setBorder(new BevelBorder(0, null, null, null, null));
      panelInformation.setLayout(new BoxLayout(panelInformation, 0));
      this.panelError.add(panelInformation, "South");
      Component rigidArea_1 = Box.createRigidArea(new Dimension(5, 0));
      panelInformation.add(rigidArea_1);
      rigidArea_1.setPreferredSize(new Dimension(10, 0));
      rigidArea_1.setMinimumSize(new Dimension(10, 0));
      rigidArea_1.setMaximumSize(new Dimension(10, 0));
      JPanel panelX = new JPanel();
      FlowLayout flowLayout = (FlowLayout)panelX.getLayout();
      flowLayout.setHgap(4);
      flowLayout.setVgap(3);
      flowLayout.setAlignment(0);
      panelX.setMaximumSize(new Dimension(80, 22));
      panelX.setMinimumSize(new Dimension(80, 22));
      panelX.setPreferredSize(new Dimension(80, 22));
      panelX.setBorder(new BevelBorder(1, null, null, null, null));
      panelInformation.add(panelX);
      JLabel lblPositionX = new JLabel("PositionX:");
      lblPositionX.setFont(new Font("Tahoma", 0, 11));
      lblPositionX.setHorizontalTextPosition(0);
      lblPositionX.setHorizontalAlignment(2);
      panelX.add(lblPositionX);
      this.lblPositionXWert = new JLabel("999");
      this.lblPositionXWert.setFont(new Font("Tahoma", 0, 11));
      this.lblPositionXWert.setHorizontalTextPosition(4);
      this.lblPositionXWert.setHorizontalAlignment(4);
      panelX.add(this.lblPositionXWert);
      Component rigidArea = Box.createRigidArea(new Dimension(5, 0));
      rigidArea.setPreferredSize(new Dimension(10, 0));
      rigidArea.setMinimumSize(new Dimension(10, 0));
      rigidArea.setMaximumSize(new Dimension(10, 0));
      panelInformation.add(rigidArea);
      JPanel panelY = new JPanel();
      FlowLayout flowLayout_1 = (FlowLayout)panelY.getLayout();
      flowLayout_1.setVgap(3);
      flowLayout_1.setHgap(4);
      panelY.setMaximumSize(new Dimension(80, 22));
      panelY.setLocation(new Point(4, 2));
      panelY.setMinimumSize(new Dimension(80, 2));
      panelY.setPreferredSize(new Dimension(80, 22));
      panelY.setBorder(new BevelBorder(1, null, null, null, null));
      panelInformation.add(panelY);
      JLabel lblPositionY = new JLabel("PositionY:");
      lblPositionY.setFont(new Font("Tahoma", 0, 11));
      lblPositionY.setHorizontalTextPosition(0);
      lblPositionY.setHorizontalAlignment(2);
      panelY.add(lblPositionY);
      this.lblPositionYWert = new JLabel("999");
      this.lblPositionYWert.setFont(new Font("Tahoma", 0, 11));
      this.lblPositionYWert.setHorizontalTextPosition(4);
      this.lblPositionYWert.setHorizontalAlignment(4);
      panelY.add(this.lblPositionYWert);
      Component rigidArea_2 = Box.createRigidArea(new Dimension(5, 0));
      rigidArea_2.setPreferredSize(new Dimension(10, 0));
      rigidArea_2.setMinimumSize(new Dimension(10, 0));
      rigidArea_2.setMaximumSize(new Dimension(10, 0));
      panelInformation.add(rigidArea_2);
      JPanel panelBlick = new JPanel();
      FlowLayout flowLayout_2 = (FlowLayout)panelBlick.getLayout();
      flowLayout_2.setVgap(3);
      flowLayout_2.setHgap(4);
      panelBlick.setMaximumSize(new Dimension(80, 22));
      panelBlick.setLocation(new Point(4, 2));
      panelBlick.setMinimumSize(new Dimension(80, 22));
      panelBlick.setPreferredSize(new Dimension(90, 22));
      panelBlick.setBorder(new BevelBorder(1, null, null, null, null));
      panelInformation.add(panelBlick);
      JLabel lblBlick = new JLabel("Blickrichtung:");
      lblBlick.setFont(new Font("Tahoma", 0, 11));
      lblBlick.setHorizontalTextPosition(0);
      lblBlick.setHorizontalAlignment(2);
      panelBlick.add(lblBlick);
      this.lblBlickWert = new JLabel(" S ");
      this.lblBlickWert.setFont(new Font("Tahoma", 0, 11));
      this.lblBlickWert.setHorizontalTextPosition(4);
      this.lblBlickWert.setHorizontalAlignment(4);
      panelBlick.add(this.lblBlickWert);
      Component rigidArea_3 = Box.createRigidArea(new Dimension(5, 0));
      rigidArea_3.setPreferredSize(new Dimension(10, 0));
      rigidArea_3.setMinimumSize(new Dimension(10, 0));
      rigidArea_3.setMaximumSize(new Dimension(10, 0));
      panelInformation.add(rigidArea_3);
      JPanel panelZiegel = new JPanel();
      FlowLayout flowLayout_3 = (FlowLayout)panelZiegel.getLayout();
      flowLayout_3.setVgap(3);
      flowLayout_3.setHgap(4);
      panelZiegel.setMaximumSize(new Dimension(70, 22));
      panelZiegel.setPreferredSize(new Dimension(70, 22));
      panelZiegel.setBorder(new BevelBorder(1, null, null, null, null));
      panelInformation.add(panelZiegel);
      this.lblZiegel = new JLabel("Ziegel:");
      this.lblZiegel.setFont(new Font("Tahoma", 0, 11));
      this.lblZiegel.setHorizontalTextPosition(0);
      this.lblZiegel.setHorizontalAlignment(2);
      panelZiegel.add(this.lblZiegel);
      this.lblZiegelWert = new JLabel("999");
      this.lblZiegelWert.setFont(new Font("Tahoma", 0, 11));
      this.lblZiegelWert.setHorizontalTextPosition(4);
      this.lblZiegelWert.setHorizontalAlignment(4);
      panelZiegel.add(this.lblZiegelWert);
      Component horizontalGlue_4 = Box.createHorizontalGlue();
      horizontalGlue_4.setMaximumSize(new Dimension(32767, 14));
      panelInformation.add(horizontalGlue_4);
      JLabel lblInformation = new JLabel("  Information  ");
      lblInformation.setPreferredSize(new Dimension(80, 14));
      lblInformation.setMaximumSize(new Dimension(80, 24));
      lblInformation.setHorizontalTextPosition(4);
      lblInformation.setHorizontalAlignment(0);
      lblInformation.setForeground(new Color(255, 69, 0));
      lblInformation.setFont(new Font("Tahoma", 1, 11));
      lblInformation.setAlignmentX(1.0F);
      panelInformation.add(lblInformation);
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setMinimumSize(new Dimension(277, 67));
      scrollPane.setBackground(Color.WHITE);
      scrollPane.getViewport().setBackground(Color.WHITE);
      scrollPane.setPreferredSize(new Dimension(660, 160));
      this.panelError.add(scrollPane, "Center");
      this.fehlerAnzeige = new FehlerAnzeige();
      this.fehlerAnzeige.setMinimumSize(new Dimension(273, 63));
      this.fehlerAnzeige.setPreferredSize(new Dimension(650, 150));
      this.fehlerAnzeige.setBackground(Color.WHITE);
      this.fehlerAnzeige.setEditable(false);
      this.fehlerAnzeige.setLineWrap(true);
      this.fehlerAnzeige.setWrapStyleWord(true);
      this.fehlerAnzeige.setFont(new Font("Arial", 0, 12));
      this.fehlerAnzeige.setMargin(new Insets(5, 5, 5, 5));
      scrollPane.setViewportView(this.fehlerAnzeige);
      JPopupMenu popupAnzeige = new JPopupMenu();
      JMenuItem mntmAnzeigeLoeschen = new JMenuItem("Meldungen löschen");
      mntmAnzeigeLoeschen.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent ae) {
            KarolView.this.InfoBoxLoeschen();
            KarolView.this.ProgrammLampeReset();
         }
      });
      popupAnzeige.add(mntmAnzeigeLoeschen);
      this.fehlerAnzeige.setComponentPopupMenu(popupAnzeige);
   }

   public void registerListener(final KarolController listener) {
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent we) {
            listener.doProgramEnd();
         }
      });
      this.btnKarolLinks.addActionListener(listener);
      this.btnKarolLinks.setActionCommand("btnKarolLinks");
      this.btnKarolVor.addActionListener(listener);
      this.btnKarolVor.setActionCommand("btnKarolVor");
      this.btnKarolRechts.addActionListener(listener);
      this.btnKarolRechts.setActionCommand("btnKarolRechts");
      this.btnKarolHinlegen.addActionListener(listener);
      this.btnKarolHinlegen.setActionCommand("btnKarolHinlegen");
      this.btnKarolAufheben.addActionListener(listener);
      this.btnKarolAufheben.setActionCommand("btnKarolAufheben");
      this.btnKarolMarke.addActionListener(listener);
      this.btnKarolMarke.setActionCommand("btnKarolMarke");
      this.btnKarolQuaderAbl.addActionListener(listener);
      this.btnKarolQuaderAbl.setActionCommand("btnKarolQuaderAbl");
      this.btnKarolQuaderEntf.addActionListener(listener);
      this.btnKarolQuaderEntf.setActionCommand("btnKarolQuaderEntf");
      this.btnProgNeu.addActionListener(listener);
      this.btnProgNeu.setActionCommand("btnProgNeu");
      this.mntmNeu.addActionListener(listener);
      this.mntmNeu.setActionCommand("btnProgNeu");
      this.btnProgOeffnen.addActionListener(listener);
      this.btnProgOeffnen.setActionCommand("btnProgOeffnen");
      this.mntmOeffnen.addActionListener(listener);
      this.mntmOeffnen.setActionCommand("btnProgOeffnen");
      this.btnProgSpeichern.addActionListener(listener);
      this.btnProgSpeichern.setActionCommand("btnProgSpeichern");
      this.mntmSpeichern.addActionListener(listener);
      this.mntmSpeichern.setActionCommand("btnProgSpeichern");
      this.mntmSpeichernUnter.addActionListener(listener);
      this.mntmSpeichernUnter.setActionCommand("btnProgSpeichernUnter");
      this.mntmDrucken.addActionListener(listener);
      this.mntmDrucken.setActionCommand("btnProgDrucken");
      this.mntmBeenden.addActionListener(listener);
      this.mntmBeenden.setActionCommand("btnKarolBeenden");
      this.btnWeltNeu.addActionListener(listener);
      this.btnWeltNeu.setActionCommand("btnWeltNeu");
      this.mntmNeueWelt.addActionListener(listener);
      this.mntmNeueWelt.setActionCommand("btnWeltNeu");
      this.btnWeltOeffnen.addActionListener(listener);
      this.btnWeltOeffnen.setActionCommand("btnWeltÖffnen");
      this.mntmOeffnenWelt.addActionListener(listener);
      this.mntmOeffnenWelt.setActionCommand("btnWeltÖffnen");
      this.btnWeltSpeichern.addActionListener(listener);
      this.btnWeltSpeichern.setActionCommand("btnWeltSpeichern");
      this.mntmWeltSpeichern.addActionListener(listener);
      this.mntmWeltSpeichern.setActionCommand("btnWeltSpeichern");
      this.mntmWeltSpeichernUnter.addActionListener(listener);
      this.mntmWeltSpeichernUnter.setActionCommand("btnWeltSpeichernUnter");
      this.btnWeltLoeschen.addActionListener(listener);
      this.btnWeltLoeschen.setActionCommand("btnWeltLoeschen");
      this.mntmWeltLoeschen.addActionListener(listener);
      this.mntmWeltLoeschen.setActionCommand("btnWeltLoeschen");
      this.btnWeltWiederherstellen.addActionListener(listener);
      this.btnWeltWiederherstellen.setActionCommand("btnWeltWiederherstellen");
      this.mntmAlsBildSpeichern.addActionListener(listener);
      this.mntmAlsBildSpeichern.setActionCommand("btnAlsBildSpeichern");
      this.mntmAlsBildKopieren.addActionListener(listener);
      this.mntmAlsBildKopieren.setActionCommand("btnAlsBildKopieren");
      this.mntmAlsBildDrucken.addActionListener(listener);
      this.mntmAlsBildDrucken.setActionCommand("btnAlsBildDrucken");
      this.mntmGrafikVergroessern.addActionListener(listener);
      this.mntmGrafikVergroessern.setActionCommand("btnGrafikVergrößern");
      this.mntmGrafikVerkleinern.addActionListener(listener);
      this.mntmGrafikVerkleinern.setActionCommand("btnGrafikVerkleinern");
      this.btnDarstell2D.addActionListener(listener);
      this.btnDarstell2D.setActionCommand("btn2D3DUmschalten");
      this.mntmDarstellung.addActionListener(listener);
      this.mntmDarstellung.setActionCommand("btn2D3DUmschalten");
      this.mntmWeltDirektFestlegen.addActionListener(listener);
      this.mntmWeltDirektFestlegen.setActionCommand("btnWeltFestlegen");
      this.welt3DPanel.setFocusable(true);
      this.welt3DPanel.addKeyListener(listener);
      this.welt3DPanel.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent me) {
            KarolView.this.welt3DPanel.requestFocus();
            super.mouseClicked(me);
         }
      });
      this.welt2DPanel.setFocusable(true);
      this.welt2DPanel.addKeyListener(listener);
      this.welt2DPanel.addMouseListener(listener);
      this.btnSyntax.addActionListener(listener);
      this.btnSyntax.setActionCommand("btnSyntax");
      this.mntmSyntaxpruefung.addActionListener(listener);
      this.mntmSyntaxpruefung.setActionCommand("btnSyntax");
      this.btnAblaufNormal.addActionListener(listener);
      this.btnAblaufNormal.setActionCommand("btnAblaufNormal");
      this.mntmStart.addActionListener(listener);
      this.mntmStart.setActionCommand("btnAblaufNormal");
      this.btnAblaufSchnell.addActionListener(listener);
      this.btnAblaufSchnell.setActionCommand("btnAblaufSchnell");
      this.mntmSchnelllauf.addActionListener(listener);
      this.mntmSchnelllauf.setActionCommand("btnAblaufSchnell");
      this.btnAblaufEinzeln.addActionListener(listener);
      this.btnAblaufEinzeln.setActionCommand("btnAblaufEinzeln");
      this.mntmEinzelschritt.addActionListener(listener);
      this.mntmEinzelschritt.setActionCommand("btnAblaufEinzeln");
      this.btnAblaufPause.addActionListener(listener);
      this.btnAblaufPause.setActionCommand("btnAblaufPause");
      this.mntmPause.addActionListener(listener);
      this.mntmPause.setActionCommand("btnAblaufPause");
      this.btnAblaufStopp.addActionListener(listener);
      this.btnAblaufStopp.setActionCommand("btnAblaufStopp");
      this.mntmStopp.addActionListener(listener);
      this.mntmStopp.setActionCommand("btnAblaufStopp");
      this.mntmStopppunkt.addActionListener(listener);
      this.mntmStopppunkt.setActionCommand("btnStopppunkt");
      this.btnDiagrEnde.addActionListener(listener);
      this.btnDiagrEnde.setActionCommand("btnStruktoAnzeigen");
      this.mntmAnzeigenStrukto.addActionListener(listener);
      this.mntmAnzeigenStrukto.setActionCommand("btnStruktoAnzeigen");
      this.btnDiagrSpeichern.addActionListener(listener);
      this.btnDiagrSpeichern.setActionCommand("btnStruktoSpeichern");
      this.mntmSpeichernStrukto.addActionListener(listener);
      this.mntmSpeichernStrukto.setActionCommand("btnStruktoSpeichern");
      this.btnDiagrKopieren.addActionListener(listener);
      this.btnDiagrKopieren.setActionCommand("btnStruktoKopieren");
      this.mntmKopierenStrukto.addActionListener(listener);
      this.mntmKopierenStrukto.setActionCommand("btnStruktoKopieren");
      this.btnDiagrDrucken.addActionListener(listener);
      this.btnDiagrDrucken.setActionCommand("btnStruktoDrucken");
      this.mntmDruckenStrukto.addActionListener(listener);
      this.mntmDruckenStrukto.setActionCommand("btnStruktoDrucken");
      this.btnUebersicht.addActionListener(listener);
      this.btnUebersicht.setActionCommand("btnUebersicht");
      this.mntmBereicheAnordnen.addActionListener(listener);
      this.mntmBereicheAnordnen.setActionCommand("btnBereicheAnordnen");
      this.mntmKarol.addActionListener(listener);
      this.mntmKarol.setActionCommand("btnEinstellungK");
      this.mntmEditor.addActionListener(listener);
      this.mntmEditor.setActionCommand("btnEinstellungE");
      this.mntmFigurWechseln.addActionListener(listener);
      this.mntmFigurWechseln.setActionCommand("btnFigurWechseln");
      this.mntmInhalt.addActionListener(listener);
      this.mntmInhalt.setActionCommand("btnInhalt");
      this.mntmTipps.addActionListener(listener);
      this.mntmTipps.setActionCommand("btnTipps");
      this.mntmProgramminfo.addActionListener(listener);
      this.mntmProgramminfo.setActionCommand("btnProgramminfo");
   }

   private void DialogeAnlegen() {
      this.dlgNeueWelt = new DlgNeueWelt(this);
      this.dlgOpenSave = new DlgOpenSaveFile(this);
      this.dlgWeltFestlegen = new DlgWeltFestlegen(this);
      this.dlgEinstellungK = new DlgEinstellungK(this);
      this.dlgEinstellungE = new DlgEinstellungE(this);
      this.dlgAbout = new DlgAbout(this);
      this.dlgTipps = new DlgTipps(this);
      this.dlgHilfe = new DlgHilfe(this);
   }

   private void ProgIconsLaden() {
      this.progLampeIcon[0] = new ImageIcon(KarolView.class.getResource("/icons/button_lampe.gif"));
      this.progLampeIcon[1] = new ImageIcon(KarolView.class.getResource("/icons/button_lampe_rot.gif"));
      this.progLampeIcon[2] = new ImageIcon(KarolView.class.getResource("/icons/button_lampe_gelb.gif"));
      this.progLampeIcon[3] = new ImageIcon(KarolView.class.getResource("/icons/button_lampe_gruen.gif"));
   }

   public static enum DisplayStatusType {
      VST_UNBESTIMMT,
      VST_3D,
      VST_2D,
      VST_DIAGRAMM;
   }
}
