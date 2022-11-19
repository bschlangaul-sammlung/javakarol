package javakarol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class Welt {
   private WeltFenster weltFenster = null;
   private WeltAnzeige3D weltAnzeige = null;
   private FehlerAnzeige fehlerAnzeige = null;
   private final int maxHoehe = 31;
   final byte st_Nichts = 0;
   final byte st_Ziegel = 1;
   final byte st_Quader = 2;
   final byte st_Roboter = 3;
   final byte st_Marke = 4;
   final byte st_Ziegel_rot = 11;
   final byte st_Ziegel_gelb = 12;
   final byte st_Ziegel_blau = 13;
   final byte st_Ziegel_gruen = 14;
   final byte st_Marke_rot = 21;
   final byte st_Marke_gelb = 22;
   final byte st_Marke_blau = 23;
   final byte st_Marke_gruen = 24;
   final byte st_Marke_schwarz = 25;
   int areaBreite;
   int areaLaenge;
   int areaHoehe;
   byte[][] areaMarkiert;
   int[][] areaStapelHoehe;
   byte[][][] areaStapelInhalt;
   boolean[][][] areaStapelInvalid;
   private final int maxRoboter = 9;
   private int nextRoboter = 1;
   List<Object> alleRoboter;
   private List<Object> geladeneRoboter;
   private String weltDateiname;
   private boolean istJavaKarol = true;
   private String fehlerText;

   public Welt(int breite, int laenge, int hoehe) {
      this.initWelt(breite, laenge, hoehe, true);
      this.weltDateiname = "";

      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var5) {
      }

      this.istJavaKarol = true;
      this.initView();
   }

   public Welt(String weltdatei) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var3) {
      }

      this.weltDateiname = "";
      this.loadKarolFile(this.validWorldFile(weltdatei), false, false);
      this.istJavaKarol = true;
      this.initView();
   }

   public Welt() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var2) {
      }

      this.weltDateiname = "";
      this.loadKarolFile(this.validWorldFile(""), false, false);
      this.istJavaKarol = true;
      this.initView();
   }

   protected Welt(boolean b) {
      this.weltDateiname = "";
      this.istJavaKarol = false;
   }

   public void Speichern(String dateiname) {
      String dn = "";
      dn = this.saveAsKarolFile(dateiname);
      if (!dn.isEmpty()) {
         this.saveKarolFile(dn, "JavaKarolVersion3.0");
         this.weltDateiname = dn;
      }
   }

   public void BildSpeichern(String dateiname) {
      this.saveWorldImage(dateiname);
   }

   public void ZurueckSetzen() {
      this.clearWelt();
      if (this.weltDateiname.length() > 0) {
         this.loadKarolFile(this.weltDateiname, true, false);
      }

      for(int i = 0; i < this.alleRoboter.size(); ++i) {
         Roboter robo = (Roboter)this.alleRoboter.get(i);
         robo.resetRoboter();
      }

      if (this.istJavaKarol) {
         this.weltAnzeige.zeichneWeltGanz();
         this.fehlerAnzeige.setText("");
         if (this.weltFenster != null) {
            this.weltFenster.fensterNachVorne();
         }
      }
   }

   public void Loeschen() {
      this.clearWelt();
      this.weltDateiname = "";

      for(int i = 0; i < this.alleRoboter.size(); ++i) {
         Roboter robo = (Roboter)this.alleRoboter.get(i);
         robo.clearRoboter();
      }

      if (this.istJavaKarol) {
         this.weltAnzeige.zeichneWeltGanz();
         this.fehlerAnzeige.setText("");
         if (this.weltFenster != null) {
            this.weltFenster.fensterNachVorne();
         }
      }
   }

   public void ZiegelVerstreuen(int anzahlZiegel, int maxStapelhoehe, boolean mitFarbe) {
      Random zufall = new Random();
      anzahlZiegel = Math.min(anzahlZiegel, this.areaBreite * this.areaLaenge * this.areaHoehe);
      maxStapelhoehe = Math.min(Math.max(maxStapelhoehe, 1), this.areaHoehe);
      int i = 0;

      for(int j = 0; i < anzahlZiegel && j < 3 * anzahlZiegel; ++j) {
         int x = zufall.nextInt(this.areaBreite) + 1;
         int y = zufall.nextInt(this.areaLaenge) + 1;
         if (this.areaStapelHoehe[x][y] < maxStapelhoehe && (this.areaStapelHoehe[x][y] == 0 || this.istZiegelTyp(this.areaStapelInhalt[x][y][0]))) {
            if (mitFarbe) {
               byte f = (byte)((byte)zufall.nextInt(4) + 11);
               this.push(x, y, f);
            } else {
               this.push(x, y, (byte)11);
            }
         } else {
            --i;
         }

         ++i;
      }

      if (this.istJavaKarol) {
         this.weltAnzeige.zeichneWeltGanz();
         if (this.weltFenster != null) {
            this.weltFenster.fensterNachVorne();
         }
      }
   }

   public void ZiegelVerstreuen(int anzahlZiegel, int maxStapelhoehe) {
      this.ZiegelVerstreuen(anzahlZiegel, maxStapelhoehe, false);
   }

   public String getWeltDateiname() {
      return this.weltDateiname;
   }

   public int getWeltBreite() {
      return this.areaBreite;
   }

   public int getWeltLaenge() {
      return this.areaLaenge;
   }

   public int getWeltHoehe() {
      return this.areaHoehe;
   }

   public int getAnzahlRoboter() {
      return this.alleRoboter.size();
   }

   protected void setRobotKarol() {
      this.istJavaKarol = false;
   }

   protected void setView(WeltAnzeige3D wA, FehlerAnzeige fA) {
      this.weltAnzeige = wA;
      this.fehlerAnzeige = fA;
   }

   protected String getFehlerText() {
      return this.fehlerText;
   }

   protected void clearFehlerText() {
      this.fehlerText = "";
   }

   protected void setWeltDateiname(String s) {
      this.weltDateiname = s;
   }

   private boolean istZiegelTyp(byte what) {
      return what == 1 || what >= 11 && what <= 14;
   }

   protected void initWelt(int breite, int laenge, int hoehe, boolean mitRoboter) {
      this.areaBreite = Math.max(breite, 1);
      this.areaLaenge = Math.max(laenge, 1);
      this.areaHoehe = Math.max(Math.min(hoehe, 31), 1);
      this.areaStapelHoehe = new int[this.areaBreite + 1][this.areaLaenge + 1];
      this.areaMarkiert = new byte[this.areaBreite + 1][this.areaLaenge + 1];
      this.areaStapelInhalt = new byte[this.areaBreite + 1][this.areaLaenge + 1][this.areaHoehe];
      this.areaStapelInvalid = new boolean[this.areaBreite + 1][this.areaLaenge + 1][this.areaHoehe];
      this.clearWelt();
      if (mitRoboter) {
         this.alleRoboter = new ArrayList<>();
         this.geladeneRoboter = new ArrayList<>();
      }
   }

   protected void initView() {
      if (this.istJavaKarol) {
         this.weltFenster = new WeltFenster(this);
         this.weltAnzeige = this.weltFenster.getWeltAnzeige();
         this.fehlerAnzeige = this.weltFenster.getFehlerAnzeige();
         this.weltAnzeige.zeichneWeltGanz();
         this.weltFenster.fensterNachVorne();
      }
   }

   private void clearWelt() {
      for(int x = 0; x <= this.areaBreite; ++x) {
         Arrays.fill(this.areaStapelHoehe[x], 0);
      }

      for(int x = 0; x <= this.areaBreite; ++x) {
         Arrays.fill(this.areaMarkiert[x], (byte)0);
      }

      for(int x = 0; x <= this.areaBreite; ++x) {
         for(int y = 0; y <= this.areaLaenge; ++y) {
            Arrays.fill(this.areaStapelInhalt[x][y], (byte)0);
         }
      }

      for(int x = 0; x <= this.areaBreite; ++x) {
         for(int y = 0; y <= this.areaLaenge; ++y) {
            Arrays.fill(this.areaStapelInvalid[x][y], true);
         }
      }
   }

   protected void resizeWelt(int neueBreite, int neueLaenge, int neueHoehe, boolean mitRoboter) {
      this.areaStapelHoehe = null;
      this.areaMarkiert = null;
      this.areaStapelInhalt = null;
      this.areaStapelInvalid = null;
      this.initWelt(neueBreite, neueLaenge, neueHoehe, false);
      this.weltDateiname = "";
      if (mitRoboter) {
         for(int i = 0; i < this.alleRoboter.size(); ++i) {
            Roboter robo = (Roboter)this.alleRoboter.get(i);
            robo.clearRoboter();
         }
      }

      if (this.istJavaKarol) {
         this.weltAnzeige.zeichneWeltGanz();
         this.fehlerAnzeige.setText("");
         if (this.weltFenster != null) {
            this.weltFenster.fensterNachVorne();
         }
      }
   }

   protected String validWorldFile(String fileName) {
      String ergeb = "";
      if (fileName.length() > 0) {
         File f = new File(fileName);
         boolean check = false;

         try {
            if (f.isFile() && (f.getName().toLowerCase().endsWith(".kdw") || f.getName().toLowerCase().endsWith(".jkw"))) {
               check = true;
            }
         } catch (SecurityException var10) {
         }

         if (check) {
            try {
               ergeb = f.getCanonicalPath();
            } catch (IOException var9) {
            }
         } else {
            fileName = "";
         }
      }

      if (fileName.length() == 0) {
         JFileChooser d = new JFileChooser();
         d.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
               return f.isDirectory() || f.getName().toLowerCase().endsWith(".kdw") || f.getName().toLowerCase().endsWith(".jkw");
            }

            @Override
            public String getDescription() {
               return "Karolwelt *.kdw; *.jkw";
            }
         });
         d.setDialogTitle("Karolwelt laden");
         int status = d.showOpenDialog(null);
         File f = d.getSelectedFile();
         if (f == null || status != 0) {
            ergeb = "";
            if (this.istJavaKarol) {
               throw new RuntimeException("Keine Karolwelt-Datei ausgewaehlt.");
            }
         }

         try {
            ergeb = f.getCanonicalPath();
         } catch (IOException var8) {
         }
      }

      return ergeb;
   }

   protected boolean loadKarolFile(String absFileName, boolean reload, boolean newload) {
      File f = new File(absFileName);
      this.fehlerText = "";

      try {
         Scanner s = new Scanner(f, StandardCharsets.ISO_8859_1.name());
         String version = s.next();
         if (!version.equals("KarolVersion1Deutsch")
            && !version.equals("KarolVersion2Deutsch")
            && !version.equals("JavaKarolVersion1")
            && !version.equals("JavaKarolVersion1.1")
            && !version.equals("KarolVersion3.0")
            && !version.equals("JavaKarolVersion3.0")) {
            s.close();
            if (this.istJavaKarol) {
               throw new RuntimeException("Die Datei ist keine Karolwelt-Datei.");
            } else {
               this.fehlerText = "Die Datei ist keine Karolwelt-Datei.";
               JOptionPane.showMessageDialog(null, this.fehlerText, "Karolwelt laden", 0);
               return false;
            }
         } else {
            int breite = s.nextInt();
            int laenge = s.nextInt();
            int hoehe = s.nextInt();
            if (!reload || breite == this.areaBreite && laenge == this.areaLaenge && hoehe == this.areaHoehe) {
               if (newload) {
                  this.resizeWelt(breite, laenge, hoehe, false);
                  this.weltDateiname = absFileName;
               }

               if (!reload && !newload) {
                  this.initWelt(breite, laenge, hoehe, true);
                  this.weltDateiname = absFileName;
               }

               this.geladeneRoboter.clear();
               int posx = s.nextInt() + 1;
               int posy = s.nextInt() + 1;
               char direct = "SWNO".charAt(s.nextInt());
               this.geladeneRoboter.add(new Welt.RoboDat(posx, posy, direct));

               for(int a = 1; a <= this.areaBreite; ++a) {
                  for(int b = 1; b <= this.areaLaenge; ++b) {
                     for(int c = 0; c < this.areaHoehe; ++c) {
                        char partChar = s.next().charAt(0);
                        switch(partChar) {
                           case 'A':
                              this.push(a, b, (byte)11);
                              break;
                           case 'B':
                              this.push(a, b, (byte)12);
                              break;
                           case 'C':
                              this.push(a, b, (byte)13);
                              break;
                           case 'D':
                              this.push(a, b, (byte)14);
                              break;
                           case 'q':
                              this.push(a, b, (byte)2);
                              break;
                           case 'z':
                              this.push(a, b, (byte)11);
                        }
                     }

                     char markChar = s.next().charAt(0);
                     switch(markChar) {
                        case 'K':
                           this.setMarkerColor(a, b, (byte)21);
                           break;
                        case 'L':
                           this.setMarkerColor(a, b, (byte)22);
                           break;
                        case 'M':
                           this.setMarkerColor(a, b, (byte)23);
                           break;
                        case 'N':
                           this.setMarkerColor(a, b, (byte)24);
                           break;
                        case 'O':
                           this.setMarkerColor(a, b, (byte)25);
                           break;
                        case 'm':
                           this.setMarkerColor(a, b, (byte)22);
                     }
                  }
               }

               if (version.equals("JavaKarolVersion1") || version.equals("JavaKarolVersion1.1") || version.equals("JavaKarolVersion3.0")) {
                  this.geladeneRoboter.clear();

                  while(s.hasNextInt()) {
                     posx = s.nextInt();
                     posy = s.nextInt();
                     direct = s.next().charAt(0);
                     this.geladeneRoboter.add(new Welt.RoboDat(posx, posy, direct));
                  }
               }

               s.close();
               if (newload || reload && !this.istJavaKarol) {
                  this.roboterSetzen();
               }

               return true;
            } else {
               s.close();
               if (this.istJavaKarol) {
                  throw new RuntimeException("Die Karolwelt-Datei wurde geaendert. Zuruecksetzen nicht moeglich.");
               } else {
                  this.fehlerText = "Die Karolwelt-Datei wurde geändert. Zurücksetzen nicht möglich.";
                  JOptionPane.showMessageDialog(null, this.fehlerText, "Karolwelt laden", 0);
                  return false;
               }
            }
         }
      } catch (FileNotFoundException var18) {
         if (this.istJavaKarol) {
            throw new RuntimeException("Die Datei ist keine Karolwelt-Datei.");
         } else {
            this.fehlerText = "Datei wurde nicht gefunden.";
            JOptionPane.showMessageDialog(null, this.fehlerText, "Karolwelt laden", 0);
            return false;
         }
      }
   }

   private String saveAsKarolFile(String fileName) {
      File f = null;
      JFileChooser d = new JFileChooser();
      d.setFileFilter(new FileFilter() {
         @Override
         public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jkw");
         }

         @Override
         public String getDescription() {
            return "JavaKarolwelt *.jkw";
         }
      });
      d.setDialogTitle("JavaKarol Welt speichern");
      if (!fileName.toLowerCase().endsWith(".jkw")) {
         fileName = fileName + ".jkw";
      }

      f = new File(fileName);
      d.setSelectedFile(f);
      int status = d.showSaveDialog(null);
      if (status == 0) {
         f = d.getSelectedFile();

         try {
            fileName = f.getCanonicalPath();
         } catch (IOException var7) {
         }

         if (f.exists()) {
            status = JOptionPane.showConfirmDialog(null, "Die vorhandene Datei\n" + f.getName() + "\nüberschreiben?", "JavaKarol Welt speichern", 0);
            if (status == 0) {
               try {
                  f.delete();
               } catch (SecurityException var6) {
                  fileName = "";
               }
            } else {
               fileName = "";
            }
         }
      } else {
         fileName = "";
      }

      return fileName;
   }

   protected void saveKarolFile(String fileName, String version) {
      File f = null;
      StringBuilder strB = new StringBuilder(2000);
      String strOutPut = "";
      f = new File(fileName);
      if (!f.exists()) {
         strB.append(version + " ");
         strB.append(String.valueOf(this.areaBreite) + " ");
         strB.append(String.valueOf(this.areaLaenge) + " ");
         strB.append(String.valueOf(this.areaHoehe) + " ");
         if (this.alleRoboter.size() > 0) {
            Roboter robo = (Roboter)this.alleRoboter.get(0);
            strB.append(String.valueOf(robo.PositionXGeben() - 1) + " ");
            strB.append(String.valueOf(robo.PositionYGeben() - 1) + " ");
            strB.append(String.valueOf("SWNO".indexOf(robo.BlickrichtungGeben())) + " ");
         } else {
            strB.append("0 ");
            strB.append("0 ");
            strB.append("0 ");
         }

         for(int a = 1; a <= this.areaBreite; ++a) {
            for(int b = 1; b <= this.areaLaenge; ++b) {
               for(int c = 0; c < this.areaHoehe; ++c) {
                  switch(this.getPart(a, b, c)) {
                     case 0:
                        strB.append("n ");
                        break;
                     case 1:
                        strB.append("z ");
                        break;
                     case 2:
                        strB.append("q ");
                        break;
                     case 3:
                     case 4:
                     case 5:
                     case 6:
                     case 7:
                     case 8:
                     case 9:
                     case 10:
                     default:
                        strB.append("x ");
                        break;
                     case 11:
                        strB.append("A ");
                        break;
                     case 12:
                        strB.append("B ");
                        break;
                     case 13:
                        strB.append("C ");
                        break;
                     case 14:
                        strB.append("D ");
                  }
               }

               switch(this.getMarker(a, b)) {
                  case 0:
                     strB.append("o ");
                     break;
                  case 4:
                     strB.append("m ");
                     break;
                  case 21:
                     strB.append("K ");
                     break;
                  case 22:
                     strB.append("L ");
                     break;
                  case 23:
                     strB.append("M ");
                     break;
                  case 24:
                     strB.append("N ");
                     break;
                  case 25:
                     strB.append("O ");
                     break;
                  default:
                     strB.append("x ");
               }
            }
         }

         for(int i = 0; i < this.alleRoboter.size(); ++i) {
            Roboter robo = (Roboter)this.alleRoboter.get(i);
            strB.append(String.valueOf(robo.PositionXGeben()) + " ");
            strB.append(String.valueOf(robo.PositionYGeben()) + " ");
            strB.append(String.valueOf(robo.BlickrichtungGeben()) + " ");
         }

         strB.append("\n");
         strOutPut = strB.toString();

         try {
            Files.write(Paths.get(f.getAbsolutePath()), strOutPut.getBytes(StandardCharsets.ISO_8859_1));
         } catch (IOException var10) {
            return;
         }
      }
   }

   private void saveWorldImage(String fileName) {
      File f = null;
      JFileChooser d = new JFileChooser();
      d.setFileFilter(
         new FileFilter() {
            @Override
            public boolean accept(File f) {
               return f.isDirectory()
                  || f.getName().toLowerCase().endsWith(".bmp")
                  || f.getName().toLowerCase().endsWith(".jpg")
                  || f.getName().toLowerCase().endsWith(".png");
            }
   
            @Override
            public String getDescription() {
               return "Bilder *.bmp; *.jpg; *.png";
            }
         }
      );
      d.setDialogTitle("Bild der Welt speichern");
      if (!fileName.toLowerCase().endsWith(".bmp") && !fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".png")) {
         fileName = fileName + ".png";
      }

      f = new File(fileName);
      d.setSelectedFile(f);
      int status = d.showSaveDialog(null);
      if (status == 0) {
         f = d.getSelectedFile();
         if (f.exists()) {
            status = JOptionPane.showConfirmDialog(null, "Die vorhandene Datei\n" + f.getName() + "\nüberschreiben?", "Bild der Welt speichern", 0);
            if (status == 0) {
               try {
                  f.delete();
               } catch (SecurityException var7) {
               }
            }
         }

         if (!f.exists()) {
            String fn = f.getName().toLowerCase();
            if (!fn.endsWith(".jpg") && !fn.endsWith(".bmp") && !fn.endsWith(".png")) {
               JOptionPane.showMessageDialog(null, "Es werden nur die Grafikformate \njpg, bmp unf png unterstützt.", "Bild der Welt speichern", 0);
            } else {
               if (fn.endsWith(".jpg")) {
                  this.weltAnzeige.paintToFile(f, "jpg");
               }

               if (fn.endsWith(".bmp")) {
                  this.weltAnzeige.paintToFile(f, "bmp");
               }

               if (fn.endsWith(".png")) {
                  this.weltAnzeige.paintToFile(f, "png");
               }
            }
         }
      }
   }

   int roboterAnmelden(Roboter anmeldeRoboter, boolean neu) {
      int neueKennung = 0;
      this.fehlerText = "";
      if (neu) {
         if (this.nextRoboter > 9) {
            this.fehlerText = "Maximal 9 Roboter erlaubt.";
            if (this.istJavaKarol) {
               throw new RuntimeException(this.fehlerText);
            }

            return 0;
         }

         neueKennung = this.nextRoboter;
      } else {
         neueKennung = anmeldeRoboter.KennungGeben();
      }

      int posX = anmeldeRoboter.PositionXGeben();
      int posY = anmeldeRoboter.PositionYGeben();
      if (this.getRobotID(posX, posY) > 0) {
         this.fehlerText = "An dieser Stelle steht schon ein Roboter.";
         if (this.istJavaKarol) {
            throw new RuntimeException(this.fehlerText);
         } else {
            return 0;
         }
      } else if (this.isStone(posX, posY)) {
         this.fehlerText = "An dieser Stelle steht ein Quader.";
         if (this.istJavaKarol) {
            throw new RuntimeException(this.fehlerText);
         } else {
            return 0;
         }
      } else {
         if (neu) {
            ++this.nextRoboter;
         }

         this.alleRoboter.add(anmeldeRoboter);
         this.areaStapelInvalid[posX][posY][Math.max(this.areaStapelHoehe[posX][posY] - 1, 0)] = true;
         return neueKennung;
      }
   }

   void roboterAbmelden(Roboter abmeldeRoboter) {
      int posX = abmeldeRoboter.PositionXGeben();
      int posY = abmeldeRoboter.PositionYGeben();
      this.areaStapelInvalid[posX][posY][Math.max(this.areaStapelHoehe[posX][posY] - 1, 0)] = true;
      this.alleRoboter.remove(abmeldeRoboter);
   }

   Welt.RoboDat roboterDatenAbholen() {
      Welt.RoboDat robodat = new Welt.RoboDat(1, 1, 'S');
      int anzRoboter = this.alleRoboter.size();
      int anzRoboDaten = this.geladeneRoboter.size();
      if (anzRoboter < anzRoboDaten) {
         robodat = (Welt.RoboDat)this.geladeneRoboter.get(anzRoboter);
      }

      return robodat;
   }

   void roboterSetzen() {
      List<Object> zuLoeschen = new ArrayList<>();
      zuLoeschen.clear();

      for(int r = 0; r < this.alleRoboter.size(); ++r) {
         Roboter robo = (Roboter)this.alleRoboter.get(r);
         int kennung = robo.KennungGeben();
         if (kennung <= this.geladeneRoboter.size()) {
            Welt.RoboDat robodat = (Welt.RoboDat)this.geladeneRoboter.get(kennung - 1);
            robo.setRoboter(robodat.posX, robodat.posY, robodat.direct);
         } else {
            zuLoeschen.add(robo);
         }
      }

      if (zuLoeschen.size() > 0) {
         this.alleRoboter.removeAll(zuLoeschen);
      }
   }

   void fehlerMelden(String was) {
      if (this.istJavaKarol) {
         this.fehlerAnzeige.setText(was);
         if (this.weltFenster != null) {
            this.weltFenster.fensterNachVorne();
         }
      }
   }

   protected void paintWorld() {
      if (this.istJavaKarol) {
         this.weltAnzeige.zeichneWelt();
      }
   }

   boolean isInside(int x, int y) {
      return x >= 1 && x <= this.areaBreite && y >= 1 && y <= this.areaLaenge;
   }

   void push(int x, int y, byte what) {
      if (this.istZiegelTyp(what)
         && this.areaStapelHoehe[x][y] < this.areaHoehe
         && (this.areaStapelHoehe[x][y] == 0 || this.istZiegelTyp(this.areaStapelInhalt[x][y][0]))) {
         this.areaStapelInhalt[x][y][this.areaStapelHoehe[x][y]] = what;
         this.areaStapelInvalid[x][y][this.areaStapelHoehe[x][y]] = true;
         this.areaStapelHoehe[x][y]++;
      }

      if (what == 2 && this.areaStapelHoehe[x][y] == 0 && this.areaMarkiert[x][y] == 0) {
         this.areaStapelHoehe[x][y] = 2;
         this.areaStapelInhalt[x][y][0] = 2;
         this.areaStapelInhalt[x][y][1] = 2;
         this.areaStapelInvalid[x][y][0] = true;
         this.areaStapelInvalid[x][y][1] = true;
      }
   }

   void pop(int x, int y) {
      if (this.areaStapelHoehe[x][y] > 0) {
         if (this.istZiegelTyp(this.areaStapelInhalt[x][y][0])) {
            this.areaStapelInhalt[x][y][this.areaStapelHoehe[x][y] - 1] = 0;
            this.areaStapelInvalid[x][y][this.areaStapelHoehe[x][y] - 1] = true;
            this.areaStapelHoehe[x][y]--;
         }

         if (this.areaStapelInhalt[x][y][0] == 2) {
            this.areaStapelHoehe[x][y] = 0;
            this.areaStapelInhalt[x][y][0] = 0;
            this.areaStapelInhalt[x][y][1] = 0;
            this.areaStapelInvalid[x][y][0] = true;
            this.areaStapelInvalid[x][y][1] = true;
         }
      }
   }

   byte getPart(int x, int y, int z) {
      byte ergeb;
      if (x > 0 && x <= this.areaBreite && y > 0 && y <= this.areaLaenge) {
         if (this.areaStapelHoehe[x][y] > 0 && this.areaStapelHoehe[x][y] > z) {
            ergeb = this.areaStapelInhalt[x][y][z];
         } else {
            ergeb = 0;
         }
      } else {
         ergeb = 0;
      }

      return ergeb;
   }

   byte getMarker(int x, int y) {
      byte ergeb;
      if (x > 0 && x <= this.areaBreite && y > 0 && y <= this.areaLaenge) {
         ergeb = this.areaMarkiert[x][y];
      } else {
         ergeb = 0;
      }

      return ergeb;
   }

   int brickCount(int x, int y) {
      int ergeb = 0;
      if (this.istZiegelTyp(this.getPart(x, y, 0))) {
         ergeb = this.areaStapelHoehe[x][y];
      }

      return ergeb;
   }

   boolean isMaxTop(int x, int y) {
      if (x > 0 && x <= this.areaBreite && y > 0 && y <= this.areaLaenge) {
         return this.areaStapelHoehe[x][y] >= this.areaHoehe;
      } else {
         return true;
      }
   }

   boolean isStone(int x, int y) {
      return this.getPart(x, y, 0) == 2;
   }

   boolean isBrick(int x, int y) {
      return this.istZiegelTyp(this.getPart(x, y, 0));
   }

   boolean isBrickColor(int x, int y, byte what) {
      if (what >= 11 && what <= 14) {
         for(int anzahl = this.brickCount(x, y); anzahl >= 1; --anzahl) {
            if (this.getPart(x, y, anzahl - 1) == what) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   void setMarkerColor(int x, int y, byte what) {
      if (this.areaMarkiert[x][y] == 0 && what >= 21 && what <= 25) {
         this.areaMarkiert[x][y] = what;
         this.areaStapelInvalid[x][y][this.areaStapelHoehe[x][y]] = true;
      }
   }

   void setMarker(int x, int y) {
      this.setMarkerColor(x, y, (byte)22);
   }

   void deleteMarker(int x, int y) {
      if (this.areaMarkiert[x][y] != 0) {
         this.areaMarkiert[x][y] = 0;
         this.areaStapelInvalid[x][y][this.areaStapelHoehe[x][y]] = true;
      }
   }

   boolean isMarkerColor(int x, int y, byte what) {
      if (what >= 21 && what <= 25) {
         return this.areaMarkiert[x][y] == what;
      } else {
         return false;
      }
   }

   boolean isMarker(int x, int y) {
      return this.areaMarkiert[x][y] != 0;
   }

   int getRobotID(int x, int y) {
      int ergeb = 0;
      int anzRoboter = this.alleRoboter.size();
      Roboter robo = null;

      for(int i = 0; i < anzRoboter; ++i) {
         robo = (Roboter)this.alleRoboter.get(i);
         if (robo.PositionXGeben() == x && robo.PositionYGeben() == y) {
            ergeb = robo.KennungGeben();
         }
      }

      return ergeb;
   }

   int getRobotIndex(int x, int y) {
      int ergeb = -1;
      int anzRoboter = this.alleRoboter.size();
      Roboter robo = null;

      for(int i = 0; i < anzRoboter; ++i) {
         robo = (Roboter)this.alleRoboter.get(i);
         if (robo.PositionXGeben() == x && robo.PositionYGeben() == y) {
            ergeb = i;
         }
      }

      return ergeb;
   }

   boolean isRobotInSight(int x, int y, char blickrichtung) {
      boolean ergeb = false;
      boolean abbruch = false;
      int richtung = "SWNO".indexOf(blickrichtung);

      while(!ergeb && !abbruch && this.isInside(x, y)) {
         switch(richtung) {
            case 0:
               ++y;
               break;
            case 1:
               --x;
               break;
            case 2:
               --y;
               break;
            case 3:
               ++x;
               break;
            default:
               ++y;
         }

         if (this.isInside(x, y)) {
            if (this.getRobotID(x, y) > 0) {
               ergeb = true;
            } else {
               abbruch = this.isBrick(x, y) || this.isStone(x, y);
            }
         }
      }

      return ergeb;
   }

   void setTopInvalid(int x, int y) {
      this.areaStapelInvalid[x][y][Math.max(this.areaStapelHoehe[x][y] - 1, 0)] = true;
   }

   protected void manuellSetzen(int x, int y, String was) {
      String Objekte = "zro/zge/zbl/zgr/zsc/zxx/mro/mge/mbl/mgr/msc/mxx/qua/qxx/kar/";
      int pos = 0;
      x = Math.min(Math.max(x, 0), this.areaBreite);
      y = Math.min(Math.max(y, 0), this.areaLaenge);
      pos = "zro/zge/zbl/zgr/zsc/zxx/mro/mge/mbl/mgr/msc/mxx/qua/qxx/kar/".indexOf(was + "/");
      switch(pos) {
         case 0:
            this.push(x, y, (byte)11);
            break;
         case 4:
            this.push(x, y, (byte)12);
            break;
         case 8:
            this.push(x, y, (byte)13);
            break;
         case 12:
            this.push(x, y, (byte)14);
            break;
         case 16:
            this.push(x, y, (byte)11);
            break;
         case 20:
            if (this.isBrick(x, y)) {
               this.pop(x, y);
            }
            break;
         case 24:
            this.setMarkerColor(x, y, (byte)21);
            break;
         case 28:
            this.setMarkerColor(x, y, (byte)22);
            break;
         case 32:
            this.setMarkerColor(x, y, (byte)23);
            break;
         case 36:
            this.setMarkerColor(x, y, (byte)24);
            break;
         case 40:
            this.setMarkerColor(x, y, (byte)25);
            break;
         case 44:
            this.deleteMarker(x, y);
            break;
         case 48:
            this.push(x, y, (byte)2);
            break;
         case 52:
            if (this.isStone(x, y)) {
               this.pop(x, y);
            }
            break;
         case 56:
            if (!this.isStone(x, y) && this.getRobotID(x, y) == 0 && this.alleRoboter.size() > 0) {
               Roboter robo = (Roboter)this.alleRoboter.get(0);
               this.setTopInvalid(robo.PositionXGeben(), robo.PositionYGeben());
               robo.setRoboter(x, y, robo.BlickrichtungGeben());
               this.setTopInvalid(x, y);
            }
      }
   }

   class RoboDat {
      int posX = 1;
      int posY = 1;
      char direct = 'S';

      RoboDat(int x, int y, char b) {
         this.posX = x;
         this.posY = y;
         this.direct = b;
      }
   }
}
