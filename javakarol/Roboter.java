package javakarol;

import java.awt.Point;
import java.awt.Toolkit;

public class Roboter {
   private int positionX;
   private int positionY;
   private char blickrichtung = 'S';
   protected final String[] farben = new String[]{"rot", "gelb", "blau", "grün", "schwarz"};
   private Welt meineWelt;
   private int sprunghoehe = 1;
   private int rucksackInhalt = 0;
   private int rucksackMaximum = 0;
   private boolean rucksackPruefen = false;
   private int verzoegerung = 300;
   private int kennung;
   private boolean sichtbar;
   private int startX;
   private int startY;
   private char startBlickrichtung;
   private String fehlerText = "";
   private boolean istJavaKarol;

   public Roboter(int startX, int startY, char startBlickrichtung, Welt inWelt) {
      this.meineWelt = inWelt;
      this.positionX = Math.min(Math.max(startX, 0), this.meineWelt.areaBreite);
      this.positionY = Math.min(Math.max(startY, 0), this.meineWelt.areaLaenge);
      if ("SWNO".indexOf(startBlickrichtung) >= 0) {
         this.blickrichtung = startBlickrichtung;
      }

      this.kennung = this.meineWelt.roboterAnmelden(this, true);
      this.startX = startX;
      this.startY = startY;
      this.startBlickrichtung = startBlickrichtung;
      this.sichtbar = true;
      this.istJavaKarol = true;
      this.fehlerText = "";
      this.Zeichnen();
      this.wartenIntern();
   }

   public Roboter(Welt inWelt) {
      this.meineWelt = inWelt;
      this.positionX = this.meineWelt.roboterDatenAbholen().posX;
      this.positionY = this.meineWelt.roboterDatenAbholen().posY;
      this.blickrichtung = this.meineWelt.roboterDatenAbholen().direct;
      this.kennung = this.meineWelt.roboterAnmelden(this, true);
      this.startX = this.positionX;
      this.startY = this.positionY;
      this.startBlickrichtung = this.blickrichtung;
      this.sichtbar = true;
      this.istJavaKarol = true;
      this.fehlerText = "";
      this.Zeichnen();
      this.wartenIntern();
   }

   @Override
   public String toString() {
      String teil1 = "Roboter" + this.kennung + " an der Stelle (" + this.positionX + ";" + this.positionY + ") mit Blickrichtung " + this.blickrichtung;
      String teil2 = " und Rucksackinhalt " + this.rucksackInhalt;
      return this.rucksackPruefen ? teil1 + teil2 : teil1;
   }

   public void VerzoegerungSetzen(int msec) {
      this.verzoegerung = Math.abs(msec);
   }

   public void SprunghoeheSetzen(int neueHoehe) {
      this.sprunghoehe = Math.min(Math.max(neueHoehe, 1), 20);
   }

   public void RucksackMaximumSetzen(int maxZiegel) {
      this.rucksackMaximum = Math.abs(maxZiegel);
      this.rucksackPruefen = this.rucksackMaximum > 0;
   }

   public void UnsichtbarMachen() {
      if (this.sichtbar) {
         this.meineWelt.roboterAbmelden(this);
         this.sichtbar = false;
         this.Zeichnen();
      }
   }

   public void SichtbarMachen() {
      if (!this.sichtbar) {
         this.meineWelt.roboterAnmelden(this, false);
         this.sichtbar = true;
         this.Zeichnen();
      }
   }

   public void Schritt() {
      int neuX = this.getPositionVorne().x;
      int neuY = this.getPositionVorne().y;
      this.fehlerText = "";
      if (!this.meineWelt.isInside(neuX, neuY)) {
         this.fehlerText = " ist an der Wand angestoßen.";
      } else if (this.meineWelt.isStone(neuX, neuY)) {
         this.fehlerText = " ist am Quader angestoßen.";
      } else if (this.meineWelt.getRobotID(neuX, neuY) > 0) {
         this.fehlerText = " ist an anderem Roboter angestoßen.";
      } else if (Math.abs(this.meineWelt.brickCount(this.positionX, this.positionY) - this.meineWelt.brickCount(neuX, neuY)) > this.sprunghoehe) {
         this.fehlerText = " kann nicht so hoch/tief springen.";
      }

      if (this.fehlerText.isEmpty()) {
         this.meineWelt.setTopInvalid(this.positionX, this.positionY);
         this.positionX = neuX;
         this.positionY = neuY;
         this.meineWelt.setTopInvalid(this.positionX, this.positionY);
         this.Zeichnen();
         this.wartenIntern();
      } else if (this.istJavaKarol) {
         this.meineWelt.fehlerMelden(this.toString() + this.fehlerText);
         throw new RuntimeException("Roboterbewegung nicht moeglich.");
      }
   }

   public void LinksDrehen() {
      this.fehlerText = "";
      switch(this.blickrichtung) {
         case 'N':
            this.blickrichtung = 'W';
            break;
         case 'O':
            this.blickrichtung = 'N';
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'V':
         default:
            break;
         case 'S':
            this.blickrichtung = 'O';
            break;
         case 'W':
            this.blickrichtung = 'S';
      }

      this.meineWelt.setTopInvalid(this.positionX, this.positionY);
      this.Zeichnen();
      this.wartenIntern();
   }

   public void RechtsDrehen() {
      this.fehlerText = "";
      switch(this.blickrichtung) {
         case 'N':
            this.blickrichtung = 'O';
            break;
         case 'O':
            this.blickrichtung = 'S';
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'V':
         default:
            break;
         case 'S':
            this.blickrichtung = 'W';
            break;
         case 'W':
            this.blickrichtung = 'N';
      }

      this.meineWelt.setTopInvalid(this.positionX, this.positionY);
      this.Zeichnen();
      this.wartenIntern();
   }

   public void Hinlegen(String farbeZiegel) {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      this.fehlerText = "";
      if (!this.meineWelt.isInside(vorneX, vorneY)) {
         this.fehlerText = " kann nicht hinlegen, er steht vor der Wand.";
      } else if (this.meineWelt.isStone(vorneX, vorneY)) {
         this.fehlerText = " kann nicht hinlegen, er steht vor einem Quader.";
      } else if (this.meineWelt.getRobotID(vorneX, vorneY) > 0) {
         this.fehlerText = " kann nicht hinlegen, er steht vor einem anderen Roboter.";
      } else if (this.meineWelt.isMaxTop(vorneX, vorneY)) {
         this.fehlerText = " kann nicht hinlegen, die maximale Stapelhöhe ist erreicht.";
      }

      byte farbNr = this.umrechnenFarbeZiegel(farbeZiegel);
      if (farbNr < 0 && this.fehlerText.isEmpty()) {
         this.fehlerText = " kennt die Ziegelfarbe " + farbeZiegel + " nicht.";
      }

      if (this.rucksackPruefen && this.fehlerText.isEmpty()) {
         if (this.rucksackInhalt < 1) {
            this.fehlerText = " hat keine Ziegel mehr zum Hinlegen.";
         } else {
            --this.rucksackInhalt;
         }
      }

      if (this.fehlerText.isEmpty()) {
         this.meineWelt.push(vorneX, vorneY, farbNr);
         this.Zeichnen();
         this.wartenIntern();
      } else if (this.istJavaKarol) {
         this.meineWelt.fehlerMelden(this.toString() + this.fehlerText);
         throw new RuntimeException("Roboter kann nicht hinlegen.");
      }
   }

   public void Hinlegen() {
      this.Hinlegen("rot");
   }

   public void Aufheben() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      this.fehlerText = "";
      if (!this.meineWelt.isInside(vorneX, vorneY)) {
         this.fehlerText = " kann nicht aufheben, er steht vor der Wand.";
      } else if (this.meineWelt.isStone(vorneX, vorneY)) {
         this.fehlerText = " kann nicht aufheben, er steht vor einem Quader.";
      } else if (this.meineWelt.getRobotID(vorneX, vorneY) > 0) {
         this.fehlerText = " kann nicht aufheben, er steht vor einem anderen Roboter.";
      } else if (this.meineWelt.brickCount(vorneX, vorneY) < 1) {
         this.fehlerText = " kann nicht aufheben, vor ihm liegen keine Ziegel.";
      }

      if (this.rucksackPruefen && this.fehlerText.isEmpty()) {
         if (this.rucksackInhalt >= this.rucksackMaximum) {
            this.fehlerText = " kann nicht aufheben, das maximale Tragvermögen ist erreicht.";
         } else {
            ++this.rucksackInhalt;
         }
      }

      if (this.fehlerText.isEmpty()) {
         this.meineWelt.pop(vorneX, vorneY);
         this.Zeichnen();
         this.wartenIntern();
      } else if (this.istJavaKarol) {
         this.meineWelt.fehlerMelden(this.toString() + this.fehlerText);
         throw new RuntimeException("Roboter kann nicht aufheben.");
      }
   }

   public void QuaderAufstellen() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      this.fehlerText = "";
      if (!this.meineWelt.isInside(vorneX, vorneY)) {
         this.fehlerText = " kann Quader nicht aufstellen, er steht vor der Wand.";
      } else if (this.meineWelt.isStone(vorneX, vorneY)) {
         this.fehlerText = " kann Quader nicht aufstellen, er steht vor einem Quader.";
      } else if (this.meineWelt.getRobotID(vorneX, vorneY) > 0) {
         this.fehlerText = " kann Quader nicht aufstellen, er steht vor einem anderen Roboter.";
      } else if (this.meineWelt.brickCount(vorneX, vorneY) > 0) {
         this.fehlerText = " kann Quader nicht aufstellen, vor ihm liegen Ziegel.";
      } else if (this.meineWelt.isMarker(vorneX, vorneY)) {
         this.fehlerText = " kann Quader nicht aufstellen, vor ihm ist eine Marke.";
      }

      if (this.fehlerText.isEmpty()) {
         this.meineWelt.push(vorneX, vorneY, (byte)2);
         this.Zeichnen();
         this.wartenIntern();
      } else if (this.istJavaKarol) {
         this.meineWelt.fehlerMelden(this.toString() + this.fehlerText);
         throw new RuntimeException("Roboter kann Quader nicht aufstellen.");
      }
   }

   public void QuaderEntfernen() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      this.fehlerText = "";
      if (!this.meineWelt.isInside(vorneX, vorneY)) {
         this.fehlerText = " kann Quader nicht entfernen, er steht vor der Wand.";
      } else if (this.meineWelt.getRobotID(vorneX, vorneY) > 0) {
         this.fehlerText = " kann Quader nicht entfernen, er steht vor einem anderen Roboter.";
      } else if (!this.meineWelt.isStone(vorneX, vorneY)) {
         this.fehlerText = " kann Quader nicht entfernen, er steht vor keinem Quader.";
      }

      if (this.fehlerText.isEmpty()) {
         this.meineWelt.pop(vorneX, vorneY);
         this.Zeichnen();
         this.wartenIntern();
      } else if (this.istJavaKarol) {
         this.meineWelt.fehlerMelden(this.toString() + this.fehlerText);
         throw new RuntimeException("Roboter kann Quader nicht entfernen.");
      }
   }

   public void MarkeSetzen(String farbeMarke) {
      this.fehlerText = "";
      byte farbNr = this.umrechnenFarbeMarke(farbeMarke);
      if (farbNr < 0) {
         this.fehlerText = " kennt die Markerfarbe " + farbeMarke + " nicht.";
      }

      if (this.fehlerText.isEmpty()) {
         this.meineWelt.setMarkerColor(this.positionX, this.positionY, farbNr);
         this.Zeichnen();
         this.wartenIntern();
      } else if (this.istJavaKarol) {
         this.meineWelt.fehlerMelden(this.toString() + this.fehlerText);
         throw new RuntimeException("Roboter kann Marke nicht setzen.");
      }
   }

   public void MarkeSetzen() {
      this.MarkeSetzen("gelb");
   }

   public void MarkeLoeschen() {
      this.fehlerText = "";
      this.meineWelt.deleteMarker(this.positionX, this.positionY);
      this.Zeichnen();
      this.wartenIntern();
   }

   public void TonErzeugen() {
      Toolkit.getDefaultToolkit().beep();
   }

   public void Warten(float dauer) {
      try {
         Thread.sleep((long)(dauer * 1000.0F));
      } catch (InterruptedException var3) {
      }
   }

   public void MeldungAusgeben(String was) {
      this.meineWelt.fehlerMelden(this.toString() + " sagt: " + was);
   }

   public boolean IstWand() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      return !this.meineWelt.isInside(vorneX, vorneY) || this.meineWelt.isStone(vorneX, vorneY);
   }

   public boolean IstZiegel() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      return this.meineWelt.isBrick(vorneX, vorneY);
   }

   public boolean IstZiegel(String farbeZiegel) {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      byte farbNr = this.umrechnenFarbeZiegel(farbeZiegel);
      return farbNr > 0 ? this.meineWelt.isBrickColor(vorneX, vorneY, farbNr) : false;
   }

   public boolean IstZiegelLinks() {
      int linksX = this.getPositionLinks().x;
      int linksY = this.getPositionLinks().y;
      return this.meineWelt.isBrick(linksX, linksY);
   }

   public boolean IstZiegelRechts() {
      int rechtsX = this.getPositionRechts().x;
      int rechtsY = this.getPositionRechts().y;
      return this.meineWelt.isBrick(rechtsX, rechtsY);
   }

   public boolean IstMarke() {
      return this.meineWelt.isMarker(this.positionX, this.positionY);
   }

   public boolean IstMarke(String farbeMarke) {
      byte farbNr = this.umrechnenFarbeMarke(farbeMarke);
      return farbNr > 0 ? this.meineWelt.isMarkerColor(this.positionX, this.positionY, farbNr) : false;
   }

   public boolean IstRoboter() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      return this.meineWelt.getRobotID(vorneX, vorneY) > 0;
   }

   public boolean IstRoboterInSicht() {
      return this.meineWelt.isRobotInSight(this.positionX, this.positionY, this.blickrichtung);
   }

   public boolean IstBlickNorden() {
      return this.blickrichtung == 'N';
   }

   public boolean IstBlickSueden() {
      return this.blickrichtung == 'S';
   }

   public boolean IstBlickOsten() {
      return this.blickrichtung == 'O';
   }

   public boolean IstBlickWesten() {
      return this.blickrichtung == 'W';
   }

   public boolean IstRucksackVoll() {
      return this.rucksackInhalt >= this.rucksackMaximum;
   }

   public boolean IstRucksackLeer() {
      return this.rucksackInhalt == 0;
   }

   public boolean HatZiegelImRucksack() {
      return this.rucksackInhalt > 0;
   }

   public int PositionXGeben() {
      return this.positionX;
   }

   public int PositionYGeben() {
      return this.positionY;
   }

   public char BlickrichtungGeben() {
      return this.blickrichtung;
   }

   int getBlickrichtungNr() {
      return "SWNO".indexOf(this.blickrichtung);
   }

   public int SprungshoeheGeben() {
      return this.sprunghoehe;
   }

   public boolean RucksackPruefungGeben() {
      return this.rucksackPruefen;
   }

   public int KennungGeben() {
      return this.kennung;
   }

   public boolean SichtbarkeitGeben() {
      return this.sichtbar;
   }

   public int AnzahlZiegelRucksackGeben() {
      return this.rucksackInhalt;
   }

   public int AnzahlZiegelVorneGeben() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      return this.meineWelt.brickCount(vorneX, vorneY);
   }

   public int RoboterVorneKennungGeben() {
      int vorneX = this.getPositionVorne().x;
      int vorneY = this.getPositionVorne().y;
      return this.meineWelt.getRobotID(vorneX, vorneY);
   }

   public int VerzoegerungGeben() {
      return this.verzoegerung;
   }

   protected void Zeichnen() {
      this.meineWelt.paintWorld();
   }

   protected String getFehlerText() {
      return this.fehlerText;
   }

   protected void clearFehlerText() {
      this.fehlerText = "";
   }

   protected void setNotStandAlone() {
      this.istJavaKarol = false;
   }

   protected int getRucksackMaximum() {
      return this.rucksackMaximum;
   }

   protected void setRucksackAnzahl(int wert) {
      this.rucksackInhalt = Math.min(Math.abs(wert), this.rucksackMaximum);
   }

   protected void setRucksackMaximum(int wert) {
      this.rucksackMaximum = Math.abs(wert);
   }

   protected void setRucksackPruefung(boolean wert) {
      this.rucksackPruefen = wert;
   }

   private void wartenIntern() {
      try {
         Thread.sleep((long)this.verzoegerung);
      } catch (InterruptedException var2) {
      }
   }

   private Point getPositionVorne() {
      Point pos = new Point(this.positionX, this.positionY);
      switch(this.blickrichtung) {
         case 'N':
            --pos.y;
            break;
         case 'O':
            ++pos.x;
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'V':
         default:
            break;
         case 'S':
            ++pos.y;
            break;
         case 'W':
            --pos.x;
      }

      return pos;
   }

   private Point getPositionLinks() {
      Point pos = new Point(this.positionX, this.positionY);
      switch(this.blickrichtung) {
         case 'N':
            --pos.x;
            break;
         case 'O':
            --pos.y;
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'V':
         default:
            break;
         case 'S':
            ++pos.x;
            break;
         case 'W':
            ++pos.y;
      }

      return pos;
   }

   private Point getPositionRechts() {
      Point pos = new Point(this.positionX, this.positionY);
      switch(this.blickrichtung) {
         case 'N':
            ++pos.x;
            break;
         case 'O':
            ++pos.y;
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'V':
         default:
            break;
         case 'S':
            --pos.x;
            break;
         case 'W':
            --pos.y;
      }

      return pos;
   }

   private byte umrechnenFarbeZiegel(String farbStr) {
      boolean gefunden = false;

      for(byte i = 0; i <= 3 && !gefunden; ++i) {
         gefunden = this.farben[i].equals(farbStr.toLowerCase());
         if (gefunden) {
            return (byte)(i + 11);
         }
      }

      return -1;
   }

   private byte umrechnenFarbeMarke(String farbStr) {
      boolean gefunden = false;

      for(byte i = 0; i <= 4 && !gefunden; ++i) {
         gefunden = this.farben[i].equals(farbStr.toLowerCase());
         if (gefunden) {
            return (byte)(i + 21);
         }
      }

      return -1;
   }

   void resetRoboter() {
      boolean nichtGefunden = true;
      int k = this.meineWelt.getRobotID(this.startX, this.startY);
      if (!this.meineWelt.isInside(this.startX, this.startY) || this.meineWelt.isStone(this.startX, this.startY) || k != this.kennung && k != 0) {
         for(int y = 1; y < this.meineWelt.areaLaenge && nichtGefunden; ++y) {
            for(int x = 1; x < this.meineWelt.areaBreite && nichtGefunden; ++x) {
               k = this.meineWelt.getRobotID(x, y);
               if (this.meineWelt.isInside(x, y) && !this.meineWelt.isStone(x, y) && (k == this.kennung || k == 0)) {
                  this.positionX = x;
                  this.positionY = y;
                  this.blickrichtung = this.startBlickrichtung;
                  nichtGefunden = false;
               }
            }
         }
      } else {
         this.positionX = this.startX;
         this.positionY = this.startY;
         this.blickrichtung = this.startBlickrichtung;
      }
   }

   void clearRoboter() {
      boolean nichtGefunden = true;

      for(int y = 1; y < this.meineWelt.areaLaenge && nichtGefunden; ++y) {
         for(int x = 1; x < this.meineWelt.areaBreite && nichtGefunden; ++x) {
            int k = this.meineWelt.getRobotID(x, y);
            if (this.meineWelt.isInside(x, y) && !this.meineWelt.isStone(x, y) && (k == this.kennung || k == 0)) {
               this.positionX = x;
               this.startX = x;
               this.positionY = y;
               this.startY = y;
               this.blickrichtung = 'S';
               this.startBlickrichtung = 'S';
               nichtGefunden = false;
            }
         }
      }
   }

   void setRoboter(int neuesX, int neuesY, char neuerBlick) {
      this.positionX = neuesX;
      this.startX = neuesX;
      this.positionY = neuesY;
      this.startY = neuesY;
      this.blickrichtung = neuerBlick;
      this.startBlickrichtung = neuerBlick;
   }
}
