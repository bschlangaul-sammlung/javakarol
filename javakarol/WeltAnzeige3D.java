package javakarol;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class WeltAnzeige3D extends JPanel {
   private Welt welt;
   private GraphicsConfiguration gfxConf;
   private BufferedImage zeichenFlaeche;
   private Point weltOrigin3D = new Point(0, 0);
   private Graphics2D gDC;
   protected BufferedImage quaderImg = null;
   protected BufferedImage[] ziegelImg = new BufferedImage[4];
   protected BufferedImage[] markeImg = new BufferedImage[5];
   private final int maxRobotImages = 9;
   protected BufferedImage[][] karolImg = new BufferedImage[4][9];
   private BufferedImage weltFlaeche = null;
   protected int maxImageHoehe = 10;
   private Color hintergrundFarbe = Color.WHITE;
   private final int randLinks = 40;
   private final int randOben = 40;
   private final int randUnten = 30;
   private final int weltOben = 20;
   private int zoomWert = 0;
   private double[] zoomFaktor = new double[]{0.6, 0.7, 0.8, 1.0, 1.5, 2.0, 2.5};

   public WeltAnzeige3D(Welt welt) {
      this.welt = welt;
      this.gfxConf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
      this.zeichenFlaecheVorbereiten();
      this.weltFlaecheVorbereiten();
      this.imagesLaden();
      this.setBackground(Color.WHITE);
      this.setBorder(BorderFactory.createLineBorder(Color.red));
      this.setOpaque(true);
      this.zoomWert = 0;
      this.setPreferredSize(new Dimension(this.getMinBreite(), this.getMinHoehe()));
   }

   public int getMinBreite() {
      return this.zoomWert == 0
         ? this.zeichenFlaeche.getWidth() + 10
         : (int)(this.zoomFaktor[this.zoomWert + 3] * (double)this.zeichenFlaeche.getWidth() + 10.0);
   }

   public int getMinHoehe() {
      return this.zoomWert == 0
         ? this.zeichenFlaeche.getHeight() + 10
         : (int)(this.zoomFaktor[this.zoomWert + 3] * (double)this.zeichenFlaeche.getHeight() + 10.0);
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (this.zoomWert == 0) {
         g.drawImage(this.zeichenFlaeche, 0, 0, null);
      } else {
         Graphics2D g2 = (Graphics2D)g;
         g2.scale(this.zoomFaktor[this.zoomWert + 3], this.zoomFaktor[this.zoomWert + 3]);
         g2.drawImage(this.zeichenFlaeche, 0, 0, null);
      }
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

   private void loescheZeichenflaeche() {
      Color aktuell = this.gDC.getColor();
      this.gDC.setColor(this.hintergrundFarbe);
      this.gDC.fillRect(0, 0, this.zeichenFlaeche.getWidth(), this.zeichenFlaeche.getHeight());
      this.gDC.setColor(aktuell);
   }

   protected void zeichneWelt() {
      int links = Integer.MAX_VALUE;
      int oben = Integer.MAX_VALUE;
      int rechts = Integer.MIN_VALUE;
      int unten = Integer.MIN_VALUE;
      new Point(0, 0);
      new Point(0, 0);

      for(int a = 1; a <= this.welt.areaBreite; ++a) {
         for(int b = 1; b <= this.welt.areaLaenge; ++b) {
            for(int c = 0; c < this.welt.areaHoehe; ++c) {
               if (this.welt.areaStapelInvalid[a][b][c]) {
                  Point p1 = this.p3((float)(a - 1), (float)b, (float)c);
                  Point p2 = this.p3((float)a, (float)(b - 1), (float)c);
                  if (links > p1.x) {
                     links = p1.x;
                  }

                  if (oben > p2.y - this.maxImageHoehe) {
                     oben = p2.y - this.maxImageHoehe;
                  }

                  if (rechts < p2.x) {
                     rechts = p2.x;
                  }

                  if (unten < p1.y) {
                     unten = p1.y;
                  }

                  this.welt.areaStapelInvalid[a][b][c] = false;
               }
            }
         }
      }

      if (links != Integer.MAX_VALUE && links != Integer.MIN_VALUE) {
         links = Math.max(0, links - 10);
         oben = Math.max(0, oben - 10);
         rechts = Math.min(this.weltFlaeche.getWidth(), rechts + 10);
         unten = Math.min(this.weltFlaeche.getHeight(), unten + 10);
         this.zeichenWeltRechteck(links, oben, rechts, unten);
         this.revalidate();
         this.repaint();
      }
   }

   protected void zeichneWeltGanz() {
      this.loescheZeichenflaeche();
      this.zeichenWeltRechteck(0, 0, this.weltFlaeche.getWidth(), this.weltFlaeche.getHeight());

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

   private void zeichenWeltRechteck(int x1, int y1, int x2, int y2) {
      new Point(0, 0);
      Rectangle clipRect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
      this.gDC.setClip(x1 + 40, y1 + 40, x2 - x1, y2 - y1);
      this.gDC.drawImage(this.weltFlaeche, x1 + 40, y1 + 40, x2 + 40, y2 + 40, x1, y1, x2, y2, this);

      for(int a = 1; a <= this.welt.areaBreite; ++a) {
         for(int b = 1; b <= this.welt.areaLaenge; ++b) {
            for(int c = 0; c <= this.welt.areaStapelHoehe[a][b]; ++c) {
               byte part = this.welt.getPart(a, b, c);
               if (part == 1 || part >= 11 && part <= 14) {
                  byte ziegelNr;
                  if (part == 1) {
                     ziegelNr = 11;
                  } else {
                     ziegelNr = (byte)(part - 11);
                  }

                  Point p1 = this.p3((float)(a - 1), (float)b, (float)c);
                  p1.y -= this.ziegelImg[ziegelNr].getHeight();
                  if (clipRect.intersects(new Rectangle(p1.x, p1.y, this.ziegelImg[ziegelNr].getWidth(), this.ziegelImg[ziegelNr].getHeight()))) {
                     this.gDC.drawImage(this.ziegelImg[ziegelNr], p1.x + 40, p1.y + 40, this);
                  }
               }

               if (part == 2) {
                  Point var19 = this.p3((float)(a - 1), (float)b, 0.0F);
                  var19.y -= this.quaderImg.getHeight();
                  if (clipRect.intersects(new Rectangle(var19.x, var19.y, this.quaderImg.getWidth(), this.quaderImg.getHeight()))) {
                     this.gDC.drawImage(this.quaderImg, var19.x + 40, var19.y + 40, this);
                  }
               }

               if (this.welt.isMarker(a, b) && this.welt.brickCount(a, b) == c) {
                  byte mark = this.welt.getMarker(a, b);
                  byte markeNr;
                  if (mark == 4) {
                     markeNr = 22;
                  } else {
                     markeNr = (byte)(mark - 21);
                  }

                  Point var20 = this.p3((float)(a - 1), (float)b, (float)c);
                  var20.y -= this.markeImg[markeNr].getHeight();
                  if (clipRect.intersects(new Rectangle(var20.x, var20.y, this.markeImg[markeNr].getWidth(), this.markeImg[markeNr].getHeight()))) {
                     this.gDC.drawImage(this.markeImg[markeNr], var20.x + 40, var20.y + 40, this);
                  }
               }

               int anzRoboter = this.welt.alleRoboter.size();

               for(int i = 0; i < anzRoboter; ++i) {
                  Roboter robo = (Roboter)this.welt.alleRoboter.get(i);
                  if (a == robo.PositionXGeben() && b == robo.PositionYGeben() && c == this.welt.areaStapelHoehe[a][b] && robo.SichtbarkeitGeben()) {
                     Point var21 = this.p3((float)a - 0.9F, (float)b - 0.3F, (float)c);
                     int richtung = robo.getBlickrichtungNr();
                     int knr = Math.min(Math.max(robo.KennungGeben() - 1, 0), 8);
                     var21.x -= (this.karolImg[richtung][knr].getWidth() - 30) / 2;
                     var21.y -= this.karolImg[richtung][knr].getHeight();
                     if (clipRect.intersects(new Rectangle(var21.x, var21.y, this.karolImg[richtung][knr].getWidth(), this.karolImg[richtung][knr].getHeight()))
                        )
                      {
                        this.gDC.drawImage(this.karolImg[richtung][knr], var21.x + 40, var21.y + 40, this);
                     }
                  }
               }
            }
         }
      }
   }

   private void weltFlaecheVorbereiten() {
      new Point(0, 0);
      new Point(0, 0);
      float[] dash_array = new float[]{10.0F, 5.0F, 5.0F, 5.0F};
      BasicStroke gestrichelt = new BasicStroke(1.0F, 0, 2, 1.0F, dash_array, 0.0F);
      BasicStroke durchgehend = new BasicStroke();
      this.weltFlaeche = this.gfxConf.createCompatibleImage(this.zeichenFlaeche.getWidth() - 80 + 1, this.zeichenFlaeche.getHeight() - 40 - 30 + 1);
      Graphics2D g = this.weltFlaeche.createGraphics();
      g.setColor(this.hintergrundFarbe);
      g.fillRect(0, 0, this.weltFlaeche.getWidth(), this.weltFlaeche.getHeight());
      g.setColor(Color.BLUE);
      g.setStroke(durchgehend);

      for(int i = 0; i <= this.welt.areaLaenge; ++i) {
         Point p1 = this.p3(0.0F, (float)i, 0.0F);
         Point p2 = this.p3((float)this.welt.areaBreite, (float)i, 0.0F);
         g.drawLine(p1.x, p1.y, p2.x, p2.y);
      }

      for(int i = 0; i <= this.welt.areaBreite; ++i) {
         Point var8 = this.p3((float)i, 0.0F, 0.0F);
         Point var14 = this.p3((float)i, (float)this.welt.areaLaenge, 0.0F);
         g.drawLine(var8.x, var8.y, var14.x, var14.y);
      }

      g.setStroke(gestrichelt);

      for(int i = 0; i <= this.welt.areaBreite; ++i) {
         Point var9 = this.p3((float)i, 0.0F, 0.0F);
         Point var15 = this.p3((float)i, 0.0F, (float)this.welt.areaHoehe);
         g.drawLine(var9.x, var9.y, var15.x, var15.y);
      }

      Point var10 = this.p3((float)this.welt.areaBreite, 0.0F, (float)this.welt.areaHoehe);
      Point var16 = this.p3(0.0F, 0.0F, (float)this.welt.areaHoehe);
      g.drawLine(var10.x, var10.y, var16.x, var16.y);

      for(int i = 0; i <= this.welt.areaLaenge; ++i) {
         var10 = this.p3(0.0F, (float)i, 0.0F);
         var16 = this.p3(0.0F, (float)i, (float)this.welt.areaHoehe);
         g.drawLine(var10.x, var10.y, var16.x, var16.y);
      }

      var10 = this.p3(0.0F, (float)this.welt.areaLaenge, (float)this.welt.areaHoehe);
      var16 = this.p3(0.0F, 0.0F, (float)this.welt.areaHoehe);
      g.drawLine(var10.x, var10.y, var16.x, var16.y);
      g.setStroke(durchgehend);
      var10 = this.p3(-2.0F, 0.0F, (float)this.welt.areaHoehe);
      var16 = this.p3(-2.0F, 2.0F, (float)this.welt.areaHoehe);
      g.drawLine(var10.x, var10.y, var16.x, var16.y);
      g.drawLine(var10.x - 10, var10.y + 5, var10.x, var10.y);
      g.drawLine(var10.x - 5, var10.y + 10, var10.x, var10.y);
      g.setFont(new Font("Arial", 0, 14));
      g.drawString("N", var10.x + 4, var10.y);
   }

   protected BufferedImage imageLadenName(String name) {
      BufferedImage bi = null;
      URL u = ClassLoader.getSystemResource("imgs/" + name + ".gif");
      if (u == null) {
         u = WeltAnzeige3D.class.getResource("imgs/" + name + ".gif");
      }

      try {
         bi = ImageIO.read(u);
      } catch (IOException var5) {
         System.out.println("Ein noetiges Image für Ziegel/Quader/Roboter kann nicht geladen werden.");
         throw new RuntimeException("Fehler beim Laden eines Images.");
      }

      if (bi.getHeight() > this.maxImageHoehe) {
         this.maxImageHoehe = bi.getHeight();
      }

      return bi;
   }

   protected void imagesLaden() {
      this.ziegelImg[0] = this.imageLadenName("Ziegel_rot");
      this.ziegelImg[1] = this.imageLadenName("Ziegel_gelb");
      this.ziegelImg[2] = this.imageLadenName("Ziegel_blau");
      this.ziegelImg[3] = this.imageLadenName("Ziegel_gruen");
      this.markeImg[0] = this.imageLadenName("Marke_rot");
      this.markeImg[1] = this.imageLadenName("Marke_gelb");
      this.markeImg[2] = this.imageLadenName("Marke_blau");
      this.markeImg[3] = this.imageLadenName("Marke_gruen");
      this.markeImg[4] = this.imageLadenName("Marke_schwarz");
      this.quaderImg = this.imageLadenName("Quader");

      for(int i = 1; i <= 9; ++i) {
         this.karolImg[0][i - 1] = this.imageLadenName("robotS" + i);
         this.karolImg[1][i - 1] = this.imageLadenName("robotW" + i);
         this.karolImg[2][i - 1] = this.imageLadenName("robotN" + i);
         this.karolImg[3][i - 1] = this.imageLadenName("robotO" + i);
      }
   }

   private Point p3(float x, float y, float z) {
      Point ergeb = new Point();
      ergeb.x = Math.round((float)this.weltOrigin3D.x + 30.0F * x - 15.0F * y);
      ergeb.y = Math.round((float)this.weltOrigin3D.y + 15.0F * y - 15.0F * z);
      return ergeb;
   }

   private void zeichenFlaecheVorbereiten() {
      Dimension ergeb = new Dimension(0, 0);
      Point[] punkte = new Point[8];
      int links = Integer.MAX_VALUE;
      int oben = Integer.MAX_VALUE;
      int rechts = Integer.MIN_VALUE;
      int unten = Integer.MIN_VALUE;
      punkte[0] = this.p3(0.0F, 0.0F, 0.0F);
      punkte[1] = this.p3(0.0F, 0.0F, (float)this.welt.areaHoehe);
      punkte[2] = this.p3(0.0F, (float)this.welt.areaLaenge, 0.0F);
      punkte[3] = this.p3(0.0F, (float)this.welt.areaLaenge, (float)this.welt.areaHoehe);
      punkte[4] = this.p3((float)this.welt.areaBreite, 0.0F, 0.0F);
      punkte[5] = this.p3((float)this.welt.areaBreite, 0.0F, (float)this.welt.areaHoehe);
      punkte[6] = this.p3((float)this.welt.areaBreite, (float)this.welt.areaLaenge, 0.0F);
      punkte[7] = this.p3((float)this.welt.areaBreite, (float)this.welt.areaLaenge, (float)this.welt.areaHoehe);

      for(int i = 0; i < 7; ++i) {
         if (punkte[i].x > rechts) {
            rechts = punkte[i].x;
         }

         if (punkte[i].y > unten) {
            unten = punkte[i].y;
         }

         if (punkte[i].x < links) {
            links = punkte[i].x;
         }

         if (punkte[i].y < oben) {
            oben = punkte[i].y;
         }
      }

      ergeb.width = Math.abs(rechts - links) + 80;
      ergeb.height = Math.abs(unten - oben) + 40 + 30 + 20;
      this.weltOrigin3D.x = Math.abs(punkte[0].x - links);
      this.weltOrigin3D.y = Math.abs(punkte[0].y - oben) + 20;
      this.zeichenFlaeche = this.gfxConf.createCompatibleImage(ergeb.width, ergeb.height);
      this.gDC = this.zeichenFlaeche.createGraphics();
      this.loescheZeichenflaeche();
   }

   protected void resetWeltAnzeige() {
      this.loescheZeichenflaeche();
      this.paintImmediately(0, 0, this.getMinBreite(), this.getMinHoehe());
      this.zoomWert = 0;
      this.zeichenFlaeche.flush();
      this.weltFlaeche.flush();
      this.gDC = null;
      this.zeichenFlaecheVorbereiten();
      this.weltFlaecheVorbereiten();
      this.setBackground(Color.WHITE);
      this.setPreferredSize(new Dimension(this.getMinBreite(), this.getMinHoehe()));
      this.revalidate();
      this.repaint();
   }

   public int getZoomWert() {
      return this.zoomWert;
   }

   private void zoomSetzen(int neuerWert) {
      if (this.zoomWert != neuerWert) {
         this.loescheZeichenflaeche();
         this.paintImmediately(0, 0, this.getMinBreite(), this.getMinHoehe());
         this.zoomWert = neuerWert;
         this.setBackground(Color.WHITE);
         this.setPreferredSize(new Dimension(this.getMinBreite(), this.getMinHoehe()));
         this.revalidate();
         this.repaint();
      }
   }

   public boolean zoomen(boolean hinein) {
      int neuerWert = this.zoomWert;
      boolean ergeb = false;
      if (hinein && this.zoomWert < 3) {
         ++neuerWert;
      }

      if (!hinein && this.zoomWert > -3) {
         --neuerWert;
      }

      if (neuerWert != this.zoomWert) {
         this.zoomSetzen(neuerWert);
         ergeb = true;
      }

      return ergeb;
   }

   public void zoomZuruecksetzen() {
      if (this.zoomWert != 0) {
         this.zoomSetzen(0);
      }
   }
}
