package robotkarol;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class KarolStruktogramm extends JPanel {
   private KarolProgram prog = null;
   private GraphicsConfiguration gfxConf;
   private BufferedImage zeichenFlaeche;
   private Graphics2D gDC;
   private Color hintergrundFarbe = Color.WHITE;
   private final int randLinks = 20;
   private final int randOben = 20;
   private final int randUnten = 10;
   private final int blockAbstand = 20;
   private final int ueberSchriftBreite = 400;
   private final int sw = 110;
   private final int se = 40;
   private final int sh = 18;
   private Font blockFont = new Font("Arial", 0, 18);
   private Font normalFont = new Font("Arial", 0, 11);
   private Font fettFont = new Font("Arial", 1, 11);

   public KarolStruktogramm() {
      this.gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
      this.zeichenFlaeche = this.gfxConf.createCompatibleImage(300, 400);
      this.gDC = this.zeichenFlaeche.createGraphics();
      this.loescheZeichenflaeche();
      this.setBackground(this.hintergrundFarbe);
      this.setBorder(BorderFactory.createLineBorder(Color.blue));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(this.zeichenFlaeche.getWidth() + 10, this.zeichenFlaeche.getHeight() + 10));
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(this.zeichenFlaeche, 0, 0, null);
   }

   public void paintToFile(File f, String fileTyp) {
      try {
         ImageIO.write(this.zeichenFlaeche, fileTyp, f);
      } catch (IOException var4) {
      }
   }

   public String getStruktoBildDateiname() {
      String ergeb = "";
      String tempStr = "";
      tempStr = this.prog.getProgDateiname().toLowerCase();
      if (!tempStr.isEmpty()) {
         int tempIndex = tempStr.lastIndexOf(".kdp");
         if (tempIndex < 0) {
            tempIndex = tempStr.lastIndexOf(".jkp");
         }

         if (tempIndex > 0) {
            ergeb = tempStr.substring(0, tempIndex) + "_strukto" + ".png";
         }
      }

      return ergeb;
   }

   public BufferedImage getZeichenflaeche() {
      return this.zeichenFlaeche;
   }

   public void setProgram(KarolProgram prog) {
      this.prog = prog;
   }

   private void loescheZeichenflaeche() {
      Color aktuell = this.gDC.getColor();
      this.gDC.setColor(this.hintergrundFarbe);
      this.gDC.fillRect(0, 0, this.zeichenFlaeche.getWidth(), this.zeichenFlaeche.getHeight());
      this.gDC.setColor(aktuell);
   }

   private boolean between(int x, int a, int b) {
      return a <= x && x <= b;
   }

   private int calcMaxBreiteBereich(int startAnw, int endAnw, int linkeBreite, int maxBreite) {
      int posAnw = startAnw;
      KarolProgram.KarolProgAnw aktAnw = null;

      for(KarolProgram.KarolProgAnw sonstAnw = null; posAnw <= endAnw && posAnw < this.prog.anwArray.size(); ++posAnw) {
         aktAnw = this.prog.anwArray.get(posAnw);
         if (this.between(aktAnw.schluesselNr, 101, 112) || this.between(aktAnw.schluesselNr, 11, 12)) {
            maxBreite = Math.max(maxBreite, linkeBreite + 110);
         }

         if (this.between(aktAnw.schluesselNr, 301, 399)) {
            maxBreite = Math.max(maxBreite, linkeBreite + 110);
         }

         if (this.between(aktAnw.schluesselNr, 2, 5)) {
            maxBreite = Math.max(maxBreite, linkeBreite + 110 + 40);
            maxBreite = this.calcMaxBreiteBereich(posAnw + 1, aktAnw.geheZu, linkeBreite + 40, maxBreite);
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 6) {
            maxBreite = Math.max(maxBreite, linkeBreite + 110);
            int tempBreite = this.calcMaxBreiteBereich(posAnw + 1, aktAnw.geheZu, linkeBreite, maxBreite);
            maxBreite = tempBreite + 40;
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 7) {
            maxBreite = Math.max(maxBreite, linkeBreite + 110);
            int tempBreite = this.calcMaxBreiteBereich(posAnw + 1, aktAnw.geheZu, linkeBreite, maxBreite);
            posAnw = aktAnw.geheZu;
            sonstAnw = this.prog.anwArray.get(posAnw);
            maxBreite = this.calcMaxBreiteBereich(posAnw + 1, sonstAnw.geheZu, tempBreite, tempBreite);
            posAnw = sonstAnw.geheZu;
         }
      }

      return maxBreite;
   }

   private int calcMaxHoeheBereich(int startAnw, int endAnw, int obereHoehe, int maxHoehe) {
      int posAnw = startAnw;
      KarolProgram.KarolProgAnw aktAnw = null;

      for(KarolProgram.KarolProgAnw sonstAnw = null; posAnw <= endAnw && posAnw < this.prog.anwArray.size(); ++posAnw) {
         aktAnw = this.prog.anwArray.get(posAnw);
         if (this.between(aktAnw.schluesselNr, 101, 112) || this.between(aktAnw.schluesselNr, 11, 12)) {
            obereHoehe += 18;
            maxHoehe = Math.max(maxHoehe, obereHoehe);
         }

         if (this.between(aktAnw.schluesselNr, 301, 399)) {
            obereHoehe += 18;
            maxHoehe = Math.max(maxHoehe, obereHoehe);
         }

         if (this.between(aktAnw.schluesselNr, 3, 5)) {
            obereHoehe += 18;
            int var12 = Math.max(maxHoehe, obereHoehe);
            maxHoehe = this.calcMaxHoeheBereich(posAnw + 1, aktAnw.geheZu, obereHoehe, var12);
            obereHoehe = maxHoehe;
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 2) {
            int var13 = Math.max(maxHoehe, obereHoehe);
            int var14 = this.calcMaxHoeheBereich(posAnw + 1, aktAnw.geheZu, obereHoehe, var13);
            maxHoehe = var14 + 18;
            obereHoehe = maxHoehe;
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 6) {
            obereHoehe += 36;
            maxHoehe = Math.max(maxHoehe, obereHoehe);
            int tempHoehe = this.calcMaxHoeheBereich(posAnw + 1, aktAnw.geheZu, obereHoehe, maxHoehe);
            maxHoehe = tempHoehe;
            obereHoehe = tempHoehe;
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 7) {
            obereHoehe += 36;
            int var16 = Math.max(maxHoehe, obereHoehe);
            int tempHoehe = this.calcMaxHoeheBereich(posAnw + 1, aktAnw.geheZu, obereHoehe, var16);
            posAnw = aktAnw.geheZu;
            sonstAnw = this.prog.anwArray.get(posAnw);
            int var17 = this.calcMaxHoeheBereich(posAnw + 1, sonstAnw.geheZu, obereHoehe, tempHoehe);
            maxHoehe = Math.max(var17, tempHoehe);
            obereHoehe = maxHoehe;
            posAnw = sonstAnw.geheZu;
         }
      }

      return maxHoehe;
   }

   private void zeichenFlaecheVorbereiten() {
      int maxBreite = 0;
      int breite = 0;
      int summeHoehe = 0;
      int hoehe = 0;

      for(int b = 0; b < this.prog.blockArray.size(); ++b) {
         breite = this.calcMaxBreiteBereich(this.prog.blockArray.get(b).beginn, this.prog.blockArray.get(b).ende, 0, 0);
         if (breite > maxBreite) {
            maxBreite = breite;
         }
      }

      for(int b = 0; b < this.prog.blockArray.size(); ++b) {
         hoehe = this.calcMaxHoeheBereich(this.prog.blockArray.get(b).beginn, this.prog.blockArray.get(b).ende, 0, 0);
         summeHoehe += hoehe;
      }

      this.loescheZeichenflaeche();
      this.paintImmediately(0, 0, this.zeichenFlaeche.getWidth(), this.zeichenFlaeche.getHeight());
      this.zeichenFlaeche.flush();
      this.gDC = null;
      Dimension ergeb = new Dimension(0, 0);
      ergeb.width = Math.max(maxBreite + 40, 400);
      ergeb.height = summeHoehe + this.prog.blockArray.size() * 56 + 20 + 10;
      this.zeichenFlaeche = this.gfxConf.createCompatibleImage(ergeb.width, ergeb.height);
      this.gDC = this.zeichenFlaeche.createGraphics();
      this.loescheZeichenflaeche();
      this.gDC.setColor(Color.BLACK);
      this.gDC.setStroke(new BasicStroke(1.0F));
      this.setPreferredSize(new Dimension(this.zeichenFlaeche.getWidth() + 10, this.zeichenFlaeche.getHeight() + 10));
      this.revalidate();
      this.repaint();
   }

   private int drawAnweisung(int xPos, int yPos, int rechts, String str) {
      this.gDC.drawRect(xPos, yPos, rechts - xPos, 18);
      int asc = this.gDC.getFontMetrics().getAscent();
      this.gDC.drawString(str, xPos + 3, yPos + asc + 2);
      return yPos + 18;
   }

   private int drawWiederholung(int xPos, int yPos, int rechts, int unten, String str1, String str2, String str3) {
      this.gDC.drawRect(xPos, yPos, rechts - xPos, unten - yPos);
      int asc = this.gDC.getFontMetrics().getAscent();
      this.gDC.setFont(this.fettFont);
      this.gDC.drawString(str1, xPos + 3, yPos + asc + 2);
      int tw1 = this.gDC.getFontMetrics().stringWidth(str1 + " ");
      this.gDC.setFont(this.normalFont);
      this.gDC.drawString(str2, xPos + tw1 + 3, yPos + asc + 2);
      int tw2 = this.gDC.getFontMetrics().stringWidth(str2 + " ");
      this.gDC.setFont(this.fettFont);
      this.gDC.drawString(str3, xPos + tw1 + tw2 + 3, yPos + asc + 2);
      this.gDC.setFont(this.normalFont);
      return unten;
   }

   private int drawWiederholungEndbedingung(int xPos, int yPos, int rechts, int unten, String str1, String str2, String str3) {
      this.gDC.drawRect(xPos, yPos, rechts - xPos, unten - yPos);
      int asc = this.gDC.getFontMetrics().getAscent();
      this.gDC.setFont(this.fettFont);
      this.gDC.drawString(str1, xPos + 3, unten - 18 + asc + 2);
      int tw1 = this.gDC.getFontMetrics().stringWidth(str1 + " ");
      this.gDC.setFont(this.normalFont);
      this.gDC.drawString(str2, xPos + tw1 + 3, unten - 18 + asc + 2);
      int tw2 = this.gDC.getFontMetrics().stringWidth(str2 + " ");
      this.gDC.setFont(this.fettFont);
      this.gDC.drawString(str3, xPos + tw1 + tw2 + 3, unten - 18 + asc + 2);
      this.gDC.setFont(this.normalFont);
      return unten;
   }

   private int drawAuswahl(int xPos, int yPos, int rechts, int unten, int mitte, String str1, boolean einseitig) {
      this.gDC.drawRect(xPos, yPos, rechts - xPos, unten - yPos);
      this.gDC.drawLine(xPos, yPos + 36, rechts - 1, yPos + 36);
      this.gDC.drawLine(xPos, yPos, mitte, yPos + 36);
      this.gDC.drawLine(rechts - 1, yPos, mitte, yPos + 36);
      int asc = this.gDC.getFontMetrics().getAscent();
      int dsc = this.gDC.getFontMetrics().getDescent();
      this.gDC.setFont(this.fettFont);
      int tw1 = this.gDC.getFontMetrics().stringWidth(str1 + "?");
      int tw2 = this.gDC.getFontMetrics().stringWidth("f");
      if (einseitig) {
         mitte = (rechts - xPos) / 2 + xPos + 13;
      }

      this.gDC.drawString(str1 + "?", mitte - tw1 / 2, yPos + asc + 2);
      this.gDC.drawString("w", xPos + 3, yPos + 36 - dsc);
      this.gDC.drawString("f", rechts - tw2 - 3, yPos + 36 - dsc);
      this.gDC.setFont(this.normalFont);
      return unten;
   }

   private String bedingungStr(KarolProgram.KarolProgAnw aktAnw) {
      String ergebStr = "";
      String tempStr = "";
      if (aktAnw.paramB) {
         ergebStr = (String)this.prog.TokenMap.get(13) + " ";
      }

      if (this.between(aktAnw.bedNr, 201, 215)) {
         if (aktAnw.paramI > 1) {
            tempStr = "(" + Integer.toString(aktAnw.paramI) + ")";
         }

         if (aktAnw.paramI < 0) {
            tempStr = this.prog.farbParameter[Math.abs(aktAnw.paramI) - 1];
         }

         ergebStr = ergebStr + (String)this.prog.TokenMap.get(aktAnw.bedNr) + tempStr;
      }

      if (this.between(aktAnw.bedNr, 401, 499)) {
         ergebStr = ergebStr + this.prog.blockArray.get(aktAnw.bedNr - 400).bezeichner + "()";
      }

      return ergebStr;
   }

   private int zeichneBereich(int startAnw, int endAnw, int xPos, int yPos, int maxXPos, int maxYPos) {
      int posAnw = startAnw;
      KarolProgram.KarolProgAnw aktAnw = null;
      KarolProgram.KarolProgAnw sonstAnw = null;
      KarolProgram.KarolProgAnw nextAnw = null;

      for(String tempStr = ""; posAnw <= endAnw && posAnw < this.prog.anwArray.size(); ++posAnw) {
         aktAnw = this.prog.anwArray.get(posAnw);
         if (this.between(aktAnw.schluesselNr, 101, 112)) {
            maxYPos = Math.max(maxYPos, yPos + 18);
            tempStr = "";
            if (aktAnw.paramI > 1) {
               tempStr = "(" + Integer.toString(aktAnw.paramI) + ")";
            }

            if (aktAnw.paramI < 0) {
               tempStr = this.prog.farbParameter[Math.abs(aktAnw.paramI) - 1];
            }

            tempStr = (String)this.prog.TokenMap.get(aktAnw.schluesselNr) + tempStr;
            yPos = this.drawAnweisung(xPos, yPos, maxXPos, tempStr);
         }

         if (this.between(aktAnw.schluesselNr, 11, 12)) {
            maxYPos = Math.max(maxYPos, yPos + 18);
            this.gDC.setFont(this.fettFont);
            tempStr = this.prog.TokenMap.get(aktAnw.schluesselNr);
            yPos = this.drawAnweisung(xPos, yPos, maxXPos, tempStr);
            this.gDC.setFont(this.normalFont);
         }

         if (this.between(aktAnw.schluesselNr, 301, 399)) {
            maxYPos = Math.max(maxYPos, yPos + 18);
            tempStr = this.prog.blockArray.get(aktAnw.schluesselNr - 300).bezeichner;
            yPos = this.drawAnweisung(xPos, yPos, maxXPos, tempStr + "()");
         }

         if (this.between(aktAnw.schluesselNr, 3, 5)) {
            maxYPos = Math.max(maxYPos, yPos + 18);
            maxYPos = this.zeichneBereich(posAnw + 1, aktAnw.geheZu, xPos + 40, yPos + 18, maxXPos, maxYPos);
            if (aktAnw.schluesselNr == 3) {
               yPos = this.drawWiederholung(xPos, yPos, maxXPos, maxYPos, this.prog.TokenMap.get(2), Integer.toString(aktAnw.paramI), this.prog.TokenMap.get(3));
            }

            if (aktAnw.schluesselNr == 5) {
               yPos = this.drawWiederholung(xPos, yPos, maxXPos, maxYPos, (String)this.prog.TokenMap.get(2) + " " + (String)this.prog.TokenMap.get(16), "", "");
            }

            if (aktAnw.schluesselNr == 4) {
               yPos = this.drawWiederholung(xPos, yPos, maxXPos, maxYPos, "wdh. " + (String)this.prog.TokenMap.get(4), this.bedingungStr(aktAnw), "");
            }

            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 2) {
            maxYPos = Math.max(maxYPos, yPos);
            maxYPos = this.zeichneBereich(posAnw + 1, aktAnw.geheZu, xPos + 40, yPos, maxXPos, maxYPos);
            maxYPos += 18;
            nextAnw = this.prog.anwArray.get(aktAnw.geheZu);
            if (nextAnw.schluesselNr == 60) {
               yPos = this.drawWiederholungEndbedingung(
                  xPos, yPos, maxXPos, maxYPos, "wdh. " + (String)this.prog.TokenMap.get(4), this.bedingungStr(nextAnw), ""
               );
            }

            if (nextAnw.schluesselNr == 61) {
               yPos = this.drawWiederholungEndbedingung(
                  xPos, yPos, maxXPos, maxYPos, "wdh. " + (String)this.prog.TokenMap.get(15), this.bedingungStr(nextAnw), ""
               );
            }

            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 6) {
            int var17 = Math.max(maxYPos, yPos + 36);
            int merkXPos = this.calcMaxBreiteBereich(posAnw + 1, aktAnw.geheZu, xPos, xPos);
            maxYPos = this.zeichneBereich(posAnw + 1, aktAnw.geheZu, xPos, yPos + 36, merkXPos, var17);
            yPos = this.drawAuswahl(xPos, yPos, maxXPos, maxYPos, merkXPos, this.bedingungStr(aktAnw), true);
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 7) {
            int var18 = Math.max(maxYPos, yPos + 36);
            int merkXPos = this.calcMaxBreiteBereich(posAnw + 1, aktAnw.geheZu, xPos, xPos);
            int var19 = this.zeichneBereich(posAnw + 1, aktAnw.geheZu, xPos, yPos + 36, merkXPos, var18);
            posAnw = aktAnw.geheZu;
            sonstAnw = this.prog.anwArray.get(posAnw);
            int var20 = this.zeichneBereich(posAnw + 1, sonstAnw.geheZu, merkXPos, yPos + 36, maxXPos, var19);
            maxYPos = Math.max(var20, var19);
            yPos = this.drawAuswahl(xPos, yPos, maxXPos, maxYPos, merkXPos, this.bedingungStr(aktAnw), false);
            posAnw = sonstAnw.geheZu;
         }
      }

      return maxYPos;
   }

   protected void zeichneStruktoGanz() {
      String ueberSchrift = "";
      int tempBreite = 0;
      this.loescheZeichenflaeche();
      if (this.prog != null) {
         if (this.prog.anwArray != null && !this.prog.anwArray.isEmpty()) {
            if (this.prog.blockArray != null && !this.prog.blockArray.isEmpty()) {
               this.zeichenFlaecheVorbereiten();
               int untenY = 20;

               for(int b = 0; b < this.prog.blockArray.size(); ++b) {
                  KarolProgram.KarolProgBlock aktBlock = this.prog.blockArray.get(b);
                  if (aktBlock.typ == 0) {
                     ueberSchrift = "Hauptprogramm";
                  }

                  if (aktBlock.typ == 1) {
                     ueberSchrift = "Anweisung/Methode: " + aktBlock.bezeichner + "()";
                  }

                  if (aktBlock.typ == 2) {
                     ueberSchrift = "Bedingung: " + aktBlock.bezeichner + "()";
                  }

                  this.gDC.setFont(this.blockFont);
                  this.gDC.drawString(ueberSchrift, 20, untenY + this.gDC.getFontMetrics().getAscent());
                  this.gDC.setFont(this.normalFont);
                  untenY += 36;
                  tempBreite = this.calcMaxBreiteBereich(this.prog.blockArray.get(b).beginn, this.prog.blockArray.get(b).ende, 20, 0);
                  int tempY = this.zeichneBereich(this.prog.blockArray.get(b).beginn, this.prog.blockArray.get(b).ende, 20, untenY, tempBreite, untenY);
                  untenY = Math.max(untenY, tempY + 20);
               }

               this.revalidate();
               this.repaint();
            }
         }
      }
   }
}
