package javakarol;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WeltAnzeige2D extends JPanel {
   private Welt welt;
   private GraphicsConfiguration gfxConf;
   private BufferedImage zeichenFlaeche;
   private Point weltOrigin2D = new Point(0, 0);
   private Graphics2D gDC;
   private Color hintergrundFarbe = Color.WHITE;
   private int[] fuellFarbe = new int[]{13434880, 14474240, 205, 52480, 3881787};
   private int[] randFarbe = new int[]{16711680, 16776960, 255, 65280, 2105376};
   private final int randLinks = 20;
   private final int randOben = 20;
   private final int randUnten = 10;
   private final int fliese = 30;

   public WeltAnzeige2D(Welt welt) {
      this.welt = welt;
      this.gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
      this.zeichenFlaecheVorbereiten();
      this.setBackground(Color.WHITE);
      this.setBorder(BorderFactory.createLineBorder(Color.green));
      this.setOpaque(true);
      this.setPreferredSize(new Dimension(this.getMinBreite(), this.getMinHoehe()));
   }

   public int getMinBreite() {
      return this.zeichenFlaeche.getWidth() + 10;
   }

   public int getMinHoehe() {
      return this.zeichenFlaeche.getHeight() + 10;
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

   protected BufferedImage getZeichenflaeche() {
      return this.zeichenFlaeche;
   }

   protected void zeichneWelt() {
      int links = Integer.MAX_VALUE;
      int oben = Integer.MAX_VALUE;
      int rechts = Integer.MIN_VALUE;
      int unten = Integer.MIN_VALUE;

      for(int a = 1; a <= this.welt.areaBreite; ++a) {
         for(int b = 1; b <= this.welt.areaLaenge; ++b) {
            boolean inv = false;

            for(int c = 0; c < this.welt.areaHoehe; ++c) {
               if (this.welt.areaStapelInvalid[a][b][c]) {
                  inv = true;
                  this.welt.areaStapelInvalid[a][b][c] = false;
               }
            }

            if (inv) {
               if (links > a) {
                  links = a;
               }

               if (oben > b) {
                  oben = b;
               }

               if (rechts < a) {
                  rechts = a;
               }

               if (unten < b) {
                  unten = b;
               }
            }
         }
      }

      if (links != Integer.MAX_VALUE && links != Integer.MIN_VALUE) {
         links = Math.min(Math.max(1, links), this.welt.areaBreite);
         oben = Math.min(Math.max(1, oben), this.welt.areaLaenge);
         rechts = Math.min(Math.max(1, rechts), this.welt.areaBreite);
         unten = Math.min(Math.max(0, unten), this.welt.areaLaenge);
         this.zeichenWeltRechteck(links, oben, rechts, unten);
         this.revalidate();
         this.repaint();
      }
   }

   protected void zeichneWeltGanz() {
      this.loescheZeichenflaeche();
      this.zeichenWeltRechteck(1, 1, this.welt.areaBreite, this.welt.areaLaenge);

      for(int a = 1; a <= this.welt.areaBreite; ++a) {
         for(int b = 1; b <= this.welt.areaLaenge; ++b) {
            for(int c = 0; c < this.welt.areaHoehe; ++c) {
               this.welt.areaStapelInvalid[a][b][c] = false;
            }
         }
      }

      this.revalidate();
      this.repaint();
   }

   protected void resetWeltAnzeige() {
      this.loescheZeichenflaeche();
      this.paintImmediately(0, 0, this.getMinBreite(), this.getMinHoehe());
      this.zeichenFlaeche.flush();
      this.gDC = null;
      this.zeichenFlaecheVorbereiten();
      this.setBackground(Color.WHITE);
      this.setPreferredSize(new Dimension(this.getMinBreite(), this.getMinHoehe()));
      this.revalidate();
      this.repaint();
   }

   private void loescheZeichenflaeche() {
      Color aktuell = this.gDC.getColor();
      this.gDC.setColor(this.hintergrundFarbe);
      this.gDC.fillRect(0, 0, this.zeichenFlaeche.getWidth(), this.zeichenFlaeche.getHeight());
      this.gDC.setColor(aktuell);
   }

   private void zeichenFlaecheVorbereiten() {
      new Point(0, 0);
      Dimension ergeb = new Dimension(0, 0);
      this.weltOrigin2D.x = 20;
      this.weltOrigin2D.y = 20;
      Point pp1 = this.p2(0, 0);
      Point pp2 = this.p2(this.welt.areaBreite, this.welt.areaLaenge);
      ergeb.width = Math.abs(pp1.x - pp2.x) + 40;
      ergeb.height = Math.abs(pp1.y - pp2.y) + 20 + 10;
      this.zeichenFlaeche = this.gfxConf.createCompatibleImage(ergeb.width, ergeb.height);
      this.gDC = this.zeichenFlaeche.createGraphics();
      this.loescheZeichenflaeche();
   }

   private void zeichenWeltRechteck(int links, int oben, int rechts, int unten) {
      new Point(0, 0);
      int[] polyx = new int[3];
      int[] polyy = new int[3];
      String t = "";
      FontMetrics fm = null;
      Roboter robo = null;
      Point pp1 = this.p2(links - 1, oben - 1);
      Point pp2 = this.p2(rechts, unten);
      this.gDC.setColor(this.hintergrundFarbe);
      this.gDC.fillRect(pp1.x, pp1.y, pp2.x - pp1.x, pp2.y - pp1.y);
      this.gDC.setColor(Color.BLUE);

      for(int a = links; a <= rechts + 1; ++a) {
         pp1 = this.p2(a - 1, oben - 1);
         pp2 = this.p2(a - 1, unten);
         this.gDC.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
      }

      for(int b = oben; b <= unten + 1; ++b) {
         pp1 = this.p2(links - 1, b - 1);
         pp2 = this.p2(rechts, b - 1);
         this.gDC.drawLine(pp1.x, pp1.y, pp2.x, pp2.y);
      }

      for(int a = links; a <= rechts; ++a) {
         for(int b = oben; b <= unten; ++b) {
            pp1 = this.p2(a - 1, b - 1);
            pp2.x = pp1.x + 30;
            pp2.y = pp1.y + 30;
            byte part = this.welt.getPart(a, b, 0);
            if (part == 1 || part >= 11 && part <= 14) {
               byte parto = this.welt.getPart(a, b, this.welt.areaStapelHoehe[a][b] - 1);
               byte ziegelNr;
               if (parto == 1) {
                  ziegelNr = 11;
               } else {
                  ziegelNr = (byte)(parto - 11);
               }

               this.gDC.setColor(new Color(this.fuellFarbe[ziegelNr]));
               this.gDC.fillRect(pp1.x + 1, pp1.y + 1, 28, 28);
               this.gDC.setColor(new Color(this.randFarbe[ziegelNr]));
               this.gDC.drawRect(pp1.x + 1, pp1.y + 1, 28, 28);
               t = Integer.toString(this.welt.brickCount(a, b));
               this.gDC.setFont(new Font("Arial", 0, 15));
               fm = this.gDC.getFontMetrics();
               int tw = (30 - fm.stringWidth(t)) / 2;
               int th = (30 + fm.getAscent()) / 2;
               this.gDC.setColor(Color.WHITE);
               this.gDC.drawString(t, (float)(pp1.x + tw), (float)(pp1.y + th));
            }

            if (part == 2) {
               this.gDC.setColor(new Color(4210752));
               this.gDC.fillRect(pp1.x + 1, pp1.y + 1, 28, 28);
               this.gDC.setColor(new Color(8421504));
               this.gDC.drawRect(pp1.x + 1, pp1.y + 1, 28, 28);
            }

            if (this.welt.isMarker(a, b)) {
               byte mark = this.welt.getMarker(a, b);
               byte markeNr;
               if (mark == 4) {
                  markeNr = 22;
               } else {
                  markeNr = (byte)(mark - 21);
               }

               this.gDC.setColor(new Color(this.randFarbe[markeNr]));
               this.gDC.fillRect(pp1.x + 1, pp1.y + 1, 28, 28);
               this.gDC.setColor(Color.BLACK);
               this.gDC.drawRect(pp1.x + 1, pp1.y + 1, 28, 28);
            }

            int rpos = this.welt.getRobotIndex(a, b);
            if (rpos >= 0) {
               robo = (Roboter)this.welt.alleRoboter.get(rpos);
               switch(robo.BlickrichtungGeben()) {
                  case 'N':
                     polyx[0] = pp2.x - 1;
                     polyy[0] = pp2.y - 1;
                     polyx[1] = pp1.x + 1;
                     polyy[1] = pp2.y - 1;
                     polyx[2] = pp1.x + 15;
                     polyy[2] = pp1.y + 1;
                     break;
                  case 'O':
                     polyx[0] = pp1.x + 1;
                     polyy[0] = pp2.y - 1;
                     polyx[1] = pp1.x + 1;
                     polyy[1] = pp1.y + 1;
                     polyx[2] = pp2.x - 1;
                     polyy[2] = pp1.y + 15;
                  case 'P':
                  case 'Q':
                  case 'R':
                  case 'T':
                  case 'U':
                  case 'V':
                  default:
                     break;
                  case 'S':
                     polyx[0] = pp1.x + 1;
                     polyy[0] = pp1.y + 1;
                     polyx[1] = pp2.x - 1;
                     polyy[1] = pp1.y + 1;
                     polyx[2] = pp1.x + 15;
                     polyy[2] = pp2.y - 1;
                     break;
                  case 'W':
                     polyx[0] = pp2.x - 1;
                     polyy[0] = pp1.y + 1;
                     polyx[1] = pp2.x - 1;
                     polyy[1] = pp2.y - 1;
                     polyx[2] = pp1.x + 1;
                     polyy[2] = pp1.y + 15;
               }

               this.gDC.setColor(Color.BLACK);
               this.gDC.fill(new Polygon(polyx, polyy, 3));
               if (this.welt.alleRoboter.size() > 1) {
                  t = Integer.toString(robo.KennungGeben());
                  this.gDC.setFont(new Font("Arial", 0, 15));
                  fm = this.gDC.getFontMetrics();
                  int tw = (30 - fm.stringWidth(t)) / 2;
                  int th = (30 + fm.getAscent()) / 2;
                  this.gDC.setColor(Color.WHITE);
                  this.gDC.drawString(t, (float)(pp1.x + tw), (float)(pp1.y + th));
               }
            }
         }
      }
   }

   private Point p2(int a, int b) {
      Point ergeb = new Point();
      a = Math.abs(a);
      b = Math.abs(b);
      ergeb.x = Math.round((float)(this.weltOrigin2D.x + 30 * a));
      ergeb.y = Math.round((float)(this.weltOrigin2D.y + 30 * b));
      return ergeb;
   }

   protected Point p2ToWelt(int x, int y) {
      Point ergeb = new Point();
      ergeb.x = (int)Math.floor((double)((float)(x - this.weltOrigin2D.x) / 30.0F)) + 1;
      ergeb.y = (int)Math.floor((double)((float)(y - this.weltOrigin2D.y) / 30.0F)) + 1;
      return ergeb;
   }
}
