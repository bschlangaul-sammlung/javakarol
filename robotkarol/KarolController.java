package robotkarol;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JOptionPane;
import karoleditor.KEdtGutter;

public class KarolController implements ActionListener, KeyListener, MouseListener {
   private KarolWelt welt;
   private KarolRoboter karol;
   private KarolView view;
   private KarolProgram prog;
   public KarolTreeView treeView;
   private KarolProperties prop;
   String ergeb = "";
   private boolean restoreWelt = false;

   public KarolController(KarolWelt w, KarolRoboter r, KarolView v, KarolProgram p) {
      this.welt = w;
      this.karol = r;
      this.view = v;
      this.prog = p;
      this.view.ansichtGanzZeichnen(this.karol);
      this.treeView = new KarolTreeView(this.prog);
      this.view.setCodeViewTree(this.treeView.buildCodeTree(false));
   }

   public boolean getRestoreWelt() {
      return this.restoreWelt;
   }

   public void setRestoreWelt(boolean neuerWert) {
      this.restoreWelt = neuerWert;
   }

   public void startProperties(String propStr, String progStr, String weltStr, String progPath, String homePath, String fileSep) {
      boolean fromFile = false;
      this.prop = new KarolProperties(this.prog, this.karol, this, this.view);
      fromFile = this.prop.setPropFileName(progPath, homePath, propStr, fileSep);
      if (fromFile) {
         fromFile = this.prop.getProperties();
      }

      if (!fromFile) {
         JOptionPane.showMessageDialog(null, "Die Datei Karol.prop ist nicht vorhanden.\nStandardeinstellungen werden vorgenommen.", "Robot Karol Starten", 1);
         this.prop.defaultProperties();
      }

      if (!progStr.isEmpty() && this.prog.openProgFile(progStr, true)) {
         this.view.ansichtGanzZeichnen(this.karol);
      }

      if (!weltStr.isEmpty()) {
         weltStr = this.view.dlgOpenSave.dateiVorhanden(weltStr, 'w');
         if (weltStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Keine Karolwelt-Datei ausgewählt", "Karolwelt öffnen", 0);
         } else {
            this.welt.loadKarolWelt(weltStr);
            if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
               this.view.getWelt3DPanel().resetWeltAnzeige();
            }

            this.view.InfoBoxSetWeltName(weltStr);
            this.view.ansichtGanzZeichnen(this.karol);
         }
      }
   }

   public void doProgramEnd() {
      if (this.prog.getProgStatus() != KarolProgram.ProgStatusType.PStNothing) {
         JOptionPane.showMessageDialog(
            null, "Programm ist noch am Laufen.\n\nErst das Programm stoppen, dann kann Karol beendet werden.", "Robot Karol beenden", 2
         );
      } else {
         if (this.view.getProgDocument().getModified()) {
            int answer = JOptionPane.showConfirmDialog(
               null, "Das Programm wurde geändert.\nOhne Speichern gehen die Änderungen verloren.\n\nProgramm speichern?", "Robot Karol beenden", 1, 2
            );
            if (answer == 2) {
               return;
            }

            if (answer == 0) {
               this.prog.saveProgFile();
            }
         }

         if (!this.prop.setProperties()) {
            JOptionPane.showMessageDialog(
               null,
               "Die Programmeinstellungen können nicht\nin der Datei karol.prop gespeichert werden.\n  Dateipfad: " + this.prop.getPropFileNameSave(),
               "Robot Karol beenden",
               1
            );
         }

         System.exit(0);
      }
   }

   private boolean doRestoreWelt() {
      if (this.prog.getProgStatus() == KarolProgram.ProgStatusType.PStNothing && this.restoreWelt) {
         if (this.welt.getWeltDateiname().isEmpty() || this.view.dlgOpenSave.dateiVorhanden(this.welt.getWeltDateiname(), 'w').isEmpty()) {
            JOptionPane.showMessageDialog(null, "Die Welt muss vor dem Programmstart erst gespeichert werden.", "Programm starten", 0);
            return false;
         }

         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
            this.view.getWelt3DPanel().zoomZuruecksetzen();
         }

         this.welt.ZurueckSetzen();
         this.view.ansichtGanzZeichnen(this.karol);
      }

      return true;
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      String wer = ae.getActionCommand();
      if (wer == "btnKarolLinks") {
         this.karol.LinksDrehen();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolVor") {
         this.karol.Schritt();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolRechts") {
         this.karol.RechtsDrehen();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolHinlegen") {
         this.karol.Hinlegen();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolAufheben") {
         this.karol.Aufheben();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolMarke") {
         if (this.karol.IstMarke()) {
            this.karol.MarkeLoeschen();
         } else {
            this.karol.MarkeSetzen();
         }

         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolQuaderAbl") {
         this.karol.QuaderAufstellen();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnKarolQuaderEntf") {
         this.karol.QuaderEntfernen();
         this.view.ansichtAktualisieren(this.karol);
      }

      if (wer == "btnProgNeu") {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_DIAGRAMM) {
            this.view.Umschalten3DStrukto(false);
         }

         if (this.prog.newProgFile()) {
            this.view.InfoBoxLoeschen();
         }
      }

      if (wer == "btnProgOeffnen") {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_DIAGRAMM) {
            this.view.Umschalten3DStrukto(false);
         }

         if (this.prog.openProgFile("", true)) {
            this.view.ansichtGanzZeichnen(this.karol);
         }
      }

      if (wer == "btnProgSpeichern") {
         this.prog.saveProgFile();
      }

      if (wer == "btnProgSpeichernUnter" && this.prog.saveAsProgFile()) {
         this.view.InfoBoxLoeschen();
      }

      if (wer == "btnProgDrucken") {
         this.prog.printProg();
      }

      if (wer == "btnKarolBeenden") {
         this.doProgramEnd();
      }

      if (wer == "btnWeltNeu") {
         boolean mdErgeb = this.view.dlgNeueWelt.showModal(this.welt.getWeltBreite(), this.welt.getWeltLaenge(), this.welt.getWeltHoehe());
         if (mdErgeb) {
            this.welt.resizeWelt(this.view.dlgNeueWelt.getNeueBreite(), this.view.dlgNeueWelt.getNeueLaenge(), this.view.dlgNeueWelt.getNeueHoehe());
            if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
               this.view.getWelt3DPanel().resetWeltAnzeige();
            }

            if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
               this.view.getWelt2DPanel().resetWeltAnzeige();
            }

            this.view.InfoBoxSetWeltName("");
            this.view.ansichtGanzZeichnen(this.karol);
         }
      }

      if (wer == "btnWeltÖffnen") {
         this.ergeb = this.view.dlgOpenSave.dateiVorhanden(this.view.dlgOpenSave.dateiOeffnen(this.welt.getDefaultWeltPfad(), 'w'), 'w');
         if (this.ergeb.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Keine Karolwelt-Datei ausgewählt", "Karolwelt öffnen", 0);
         } else {
            this.welt.loadKarolWelt(this.ergeb);
            if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
               this.view.getWelt3DPanel().resetWeltAnzeige();
            }

            if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
               this.view.getWelt2DPanel().resetWeltAnzeige();
            }

            this.view.InfoBoxSetWeltName(this.ergeb);
            this.view.ansichtGanzZeichnen(this.karol);
         }
      }

      if (wer == "btnWeltSpeichern") {
         this.ergeb = this.welt.getWeltDateiname();
         if (this.ergeb.length() > 0) {
            if (JOptionPane.showConfirmDialog(null, "Die Karolwelt unter\n" + this.ergeb + "\nspeichern?", "Karolwelt speichern", 0) == 0) {
               this.welt.saveKarolWelt(this.ergeb);
            }
         } else {
            String vorschlag = this.welt.getDefaultWeltPfad();
            this.ergeb = this.view.dlgOpenSave.dateiSpeichernUnter(vorschlag, 'k');
            if (this.ergeb.length() > 0) {
               this.welt.saveKarolWelt(this.ergeb);
               this.welt.setWeltDateiname(this.ergeb);
               this.view.InfoBoxSetWeltName(this.ergeb);
               this.view.InfoBoxLoeschen();
            }
         }
      }

      if (wer == "btnWeltSpeichernUnter") {
         String vorschlag = this.welt.getWeltDateiname();
         if (vorschlag.isEmpty()) {
            vorschlag = this.welt.getDefaultWeltPfad();
         }

         this.ergeb = this.view.dlgOpenSave.dateiSpeichernUnter(vorschlag, 'k');
         if (this.ergeb.length() > 0) {
            this.welt.saveKarolWelt(this.ergeb);
            this.welt.setWeltDateiname(this.ergeb);
            this.view.InfoBoxSetWeltName(this.ergeb);
            this.view.InfoBoxLoeschen();
         }
      }

      if (wer == "btnWeltLoeschen"
         && JOptionPane.showConfirmDialog(
               null, "Wirklich gesamte Welt löschen?\nNichtgespeicherte Änderungen an der Welt gehen verloren.\n\n", "Bestätigung", 0, 2
            )
            == 0) {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
            this.view.getWelt3DPanel().zoomZuruecksetzen();
         }

         this.welt.Loeschen();
         this.view.InfoBoxSetWeltName("");
         this.view.ansichtGanzZeichnen(this.karol);
      }

      if (wer == "btnWeltWiederherstellen") {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
            this.view.getWelt3DPanel().zoomZuruecksetzen();
         }

         this.welt.ZurueckSetzen();
         this.view.ansichtGanzZeichnen(this.karol);
      }

      if (wer == "btnAlsBildSpeichern" && this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
         String vorschlag = this.welt.getWeltBildDateiname();
         if (vorschlag.isEmpty()) {
            vorschlag = this.welt.getDefaultWeltPfad();
         }

         this.ergeb = this.view.dlgOpenSave.dateiSpeichernUnter(vorschlag, 'g');
         if (this.ergeb.length() > 0) {
            this.ergeb = this.ergeb.toLowerCase();
            if (this.ergeb.endsWith(".png")) {
               this.view.welt3DPanel.paintToFile(new File(this.ergeb), "png");
            }

            if (this.ergeb.endsWith(".jpg")) {
               this.view.welt3DPanel.paintToFile(new File(this.ergeb), "jpg");
            }

            if (this.ergeb.endsWith(".bmp")) {
               this.view.welt3DPanel.paintToFile(new File(this.ergeb), "bmp");
            }
         }
      }

      if (wer == "btnAlsBildKopieren") {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
            this.view.dlgOpenSave.copyImage(this.view.welt3DPanel.getZeichenflaeche());
         }

         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
            this.view.dlgOpenSave.copyImage(this.view.welt2DPanel.getZeichenflaeche());
         }
      }

      if (wer == "btnAlsBildDrucken") {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
            this.view.dlgOpenSave.printImage(this.view.welt3DPanel.getZeichenflaeche(), this.view.welt3DPanel);
         }

         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
            this.view.dlgOpenSave.printImage(this.view.welt2DPanel.getZeichenflaeche(), this.view.welt2DPanel);
         }
      }

      if (wer == "btnGrafikVergrößern" && this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
         if (this.view.welt3DPanel.zoomen(true)) {
            this.view.UmschaltenGrKl(true, true);
            this.view.ansichtGanzZeichnen(this.karol);
         } else {
            this.view.UmschaltenGrKl(false, true);
         }
      }

      if (wer == "btnGrafikVerkleinern" && this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
         if (this.view.welt3DPanel.zoomen(false)) {
            this.view.UmschaltenGrKl(true, true);
            this.view.ansichtGanzZeichnen(this.karol);
         } else {
            this.view.UmschaltenGrKl(true, false);
         }
      }

      if (wer == "btn2D3DUmschalten") {
         if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
            this.view.Umschalten3D2D(true);
         } else if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
            this.view.Umschalten3D2D(false);
         }
      }

      if (wer == "btnWeltFestlegen") {
         this.view.dlgWeltFestlegen.showNonModal();
      }

      if (wer == "btnSyntax") {
         this.prog.compileProgramm();
      }

      if (wer == "btnAblaufNormal" && this.doRestoreWelt()) {
         this.prog.doProgRunNormal();
      }

      if (wer == "btnAblaufSchnell" && this.doRestoreWelt()) {
         this.prog.doProgRunFast();
      }

      if (wer == "btnAblaufEinzeln" && this.doRestoreWelt()) {
         this.prog.doProgStepByStep();
      }

      if (wer == "btnAblaufPause") {
         this.prog.doProgPause();
      }

      if (wer == "btnAblaufStopp") {
         this.prog.doProgStopp();
      }

      if (wer == "btnStopppunkt") {
         if (this.view.getGutterPanel().getBreakPointLine() > 0) {
            this.view.getGutterPanel().setMarkerInvisible(KEdtGutter.MarkerType.BREAKPOINT);
         } else {
            int bLine = this.view.getProgDocument().getLineNumberCaret(this.view.getProgTextPanel());
            this.view.getGutterPanel().setBreakPointLine(bLine);
         }
      }

      if (wer == "btnStruktoAnzeigen") {
         if (this.view.getViewStatus() != KarolView.DisplayStatusType.VST_3D && this.view.getViewStatus() != KarolView.DisplayStatusType.VST_2D) {
            this.view.Umschalten3DStrukto(false);
         } else if (this.prog.compileProgramm()) {
            this.view.Umschalten3DStrukto(true);
         }
      }

      if (wer == "btnStruktoSpeichern" && this.view.getViewStatus() == KarolView.DisplayStatusType.VST_DIAGRAMM) {
         String vorschlag = this.view.getStruktoPanel().getStruktoBildDateiname();
         if (vorschlag.isEmpty()) {
            vorschlag = this.prog.getDefaultProgPfad();
         }

         this.ergeb = this.view.dlgOpenSave.dateiSpeichernUnter(vorschlag, 's');
         if (this.ergeb.length() > 0) {
            this.ergeb = this.ergeb.toLowerCase();
            if (this.ergeb.endsWith(".png")) {
               this.view.getStruktoPanel().paintToFile(new File(this.ergeb), "png");
            }

            if (this.ergeb.endsWith(".jpg")) {
               this.view.getStruktoPanel().paintToFile(new File(this.ergeb), "jpg");
            }

            if (this.ergeb.endsWith(".bmp")) {
               this.view.getStruktoPanel().paintToFile(new File(this.ergeb), "bmp");
            }
         }
      }

      if (wer == "btnStruktoKopieren") {
         this.view.dlgOpenSave.copyImage(this.view.getStruktoPanel().getZeichenflaeche());
      }

      if (wer == "btnStruktoDrucken") {
         this.view.dlgOpenSave.printImage(this.view.getStruktoPanel().getZeichenflaeche(), this.view.getStruktoPanel());
      }

      if (wer == "btnUebersicht" && this.prog.compileProgramm()) {
         this.view.setCodeViewTree(this.treeView.buildCodeTree(true));
      }

      if (wer == "btnBereicheAnordnen") {
         this.view.BereicheAnordnen();
      }

      if (wer == "btnEinstellungK") {
         this.view.dlgEinstellungK.showModal(this.prog, this.karol, this);
      }

      if (wer == "btnEinstellungE") {
         this.view.dlgEinstellungE.showModal(this.view);
      }

      if (wer == "btnFigurWechseln") {
         String figurPfad = this.view.dlgOpenSave.pfadNeuerFiguren();
         if (!figurPfad.isEmpty()) {
            this.view.getWelt3DPanel().neueFigurenLaden(figurPfad);
         }
      }

      if (wer == "btnInhalt") {
         this.view.dlgHilfe.showModal();
      }

      if (wer == "btnTipps") {
         this.view.dlgTipps.showModal();
      }

      if (wer == "btnProgramminfo") {
         this.view.dlgAbout.showModal();
      }
   }

   @Override
   public void keyPressed(KeyEvent ke) {
      int Key = ke.getKeyCode();
      switch(Key) {
         case 37:
            this.karol.LinksDrehen();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 38:
            this.karol.Schritt();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 39:
            this.karol.RechtsDrehen();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 40:
            int verzoeger = this.karol.VerzoegerungGeben();
            this.karol.VerzoegerungSetzen(0);
            this.karol.LinksDrehen();
            this.karol.LinksDrehen();
            this.karol.Schritt();
            this.karol.LinksDrehen();
            this.karol.VerzoegerungSetzen(verzoeger);
            this.karol.LinksDrehen();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 65:
            this.karol.Aufheben();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 69:
            this.karol.QuaderEntfernen();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 72:
            this.karol.Hinlegen();
            this.view.ansichtAktualisieren(this.karol);
            break;
         case 77:
            if (this.karol.IstMarke()) {
               this.karol.MarkeLoeschen();
            } else {
               this.karol.MarkeSetzen();
            }

            this.view.ansichtAktualisieren(this.karol);
            break;
         case 81:
            this.karol.QuaderAufstellen();
            this.view.ansichtAktualisieren(this.karol);
      }
   }

   @Override
   public void keyReleased(KeyEvent ke) {
   }

   @Override
   public void keyTyped(KeyEvent ke) {
   }

   @Override
   public void mouseClicked(MouseEvent me) {
      new Point(0, 0);
      String ergebWas = null;
      this.view.getWelt2DPanel().requestFocus();
      Point ergebPunkt = this.view.getWelt2DPanel().p2ToWelt(me.getX(), me.getY());
      ergebWas = this.view.dlgWeltFestlegen.getAuswahl();
      if (ergebWas.length() > 0) {
         this.welt.manuellSetzen(ergebPunkt.x, ergebPunkt.y, ergebWas);
         this.view.ansichtAktualisieren(this.karol);
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
}
