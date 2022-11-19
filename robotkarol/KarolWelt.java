package robotkarol;

import java.io.File;
import javakarol.FehlerAnzeige;
import javakarol.Welt;
import javakarol.WeltAnzeige3D;
import javax.swing.JOptionPane;

public class KarolWelt extends Welt {
   private String defaultWeltPfad = "";
   private String fileSeparator = "";

   public KarolWelt(int breite, int laenge, int hoehe) {
      super(true);
      this.initWelt(breite, laenge, hoehe, true);
   }

   public KarolWelt(String weltdatei) {
      super(true);
      boolean gueltig = false;
      String fName = this.validWorldFile(weltdatei);
      if (fName.length() > 0 && this.loadKarolFile(fName, false, false)) {
         gueltig = true;
      }

      if (!gueltig) {
         this.initWelt(5, 10, 6, true);
      }
   }

   public KarolWelt() {
      super(true);
      this.initWelt(5, 10, 6, true);
   }

   @Override
   protected void initView() {
   }

   @Override
   protected void paintWorld() {
   }

   @Override
   public void setView(WeltAnzeige3D wA, FehlerAnzeige fA) {
      super.setView(wA, fA);
   }

   @Override
   public void setWeltDateiname(String s) {
      super.setWeltDateiname(s);
   }

   @Override
   public String getWeltDateiname() {
      return super.getWeltDateiname();
   }

   public void resizeWelt(int neueBreite, int neueLaenge, int neueHoehe) {
      super.resizeWelt(neueBreite, neueLaenge, neueHoehe, true);
   }

   public boolean loadKarolWelt(String absFileName) {
      return super.loadKarolFile(absFileName, false, true);
   }

   public void saveKarolWelt(String fileName) {
      File f = new File(fileName);
      if (f.exists()) {
         try {
            f.delete();
         } catch (SecurityException var4) {
            JOptionPane.showMessageDialog(null, "Die Datei\n" + fileName + "\n ist nicht änderbar", "Karolwelt speichern", 0);
            fileName = "";
         }
      }

      if (fileName.length() > 0) {
         super.saveKarolFile(fileName, "KarolVersion3.0");
      }
   }

   @Override
   public void manuellSetzen(int x, int y, String was) {
      super.manuellSetzen(x, y, was);
   }

   public void setDefaultWeltPfadSep(String s, String f) {
      this.defaultWeltPfad = s;
      this.fileSeparator = f;
   }

   public void setDefaultWeltPfad(String s) {
      this.defaultWeltPfad = s;
   }

   public String getDefaultWeltPfad() {
      return this.defaultWeltPfad;
   }

   public String getFileSeparator() {
      return this.fileSeparator;
   }

   public String getWeltBildDateiname() {
      String ergeb = "";
      String tempStr = "";
      tempStr = this.getWeltDateiname().toLowerCase();
      if (!tempStr.isEmpty()) {
         int tempIndex = tempStr.lastIndexOf(".kdw");
         if (tempIndex < 0) {
            tempIndex = tempStr.lastIndexOf(".jkw");
         }

         if (tempIndex > 0) {
            ergeb = tempStr.substring(0, tempIndex) + ".png";
         }
      }

      return ergeb;
   }
}
