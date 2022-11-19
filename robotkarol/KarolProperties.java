package robotkarol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import karoleditor.KEdtDocument;
import karoleditor.KEdtEditorKit;
import karoleditor.KEdtStyleContext;
import karoleditor.KEdtToken;

public class KarolProperties {
   String propFileNameLoad = "";
   String propFileNameSave = "";
   String fixOpenPath = "";
   boolean fixProperties = false;
   KarolProgram prog;
   KarolRoboter karol;
   KarolController cont;
   KarolView view;

   public KarolProperties(KarolProgram prog, KarolRoboter karol, KarolController cont, KarolView view) {
      this.prog = prog;
      this.karol = karol;
      this.cont = cont;
      this.view = view;
   }

   public String getPropFileNameSave() {
      return this.propFileNameSave;
   }

   public boolean setPropFileName(String systemPropPath, String userPropPath, String startParamPropPath, String pathSeperator) {
      this.propFileNameLoad = "";
      this.propFileNameSave = "";
      if (!systemPropPath.isEmpty() && !userPropPath.isEmpty()) {
         if (!startParamPropPath.isEmpty()) {
            String testFileName = startParamPropPath + pathSeperator + "karol.prop";
            File f1 = new File(testFileName);
            if (f1.exists() && f1.isFile()) {
               this.propFileNameLoad = testFileName;
               this.propFileNameSave = testFileName;
               return true;
            }
         }

         String testFileName;
         if (System.getProperty("os.name").startsWith("Mac")) {
            testFileName = userPropPath + pathSeperator + "Library" + pathSeperator + "Preferences" + pathSeperator + "karol.prop";
         } else {
            testFileName = userPropPath + pathSeperator + "RobotKarol3" + pathSeperator + "karol.prop";
         }

         this.propFileNameSave = testFileName;
         File f2 = new File(testFileName);
         if (f2.exists() && f2.isFile()) {
            this.propFileNameLoad = testFileName;
            return true;
         } else {
            testFileName = systemPropPath + pathSeperator + "karol.prop";
            File f3 = new File(testFileName);
            if (f3.exists() && f3.isFile()) {
               this.propFileNameLoad = testFileName;
               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public void defaultProperties() {
      this.prog.setProgVerzoegerung(100);
      this.karol.SprunghoeheSetzen(1);
      this.karol.setRucksackMaximum(0);
      this.karol.setRucksackStart(0);
      this.karol.setRucksackPruefung(false);
      this.prog.setOnError(false, true);
      this.prog.setScrollOnStep(false);
      this.cont.setRestoreWelt(false);
      this.fixProperties = false;
      KEdtDocument doc = this.view.getProgDocument();
      KEdtEditorKit kit = (KEdtEditorKit)this.view.getProgTextPanel().getEditorKit();
      kit.setTAB_LEN(2);
      doc.setFontsizeNormal(13);
      this.view.getGutterPanel().setFontWidth();
      this.view.getGutterPanel().repaint();
      doc.setMitSchreibweise(true);
      this.view.getGutterPanel().setMitZeilennummer(false);
      this.fixOpenPath = "";
   }

   public boolean getProperties() {
      if (this.propFileNameLoad.isEmpty()) {
         return false;
      } else {
         File f = new File(this.propFileNameLoad);
         if (f.exists() && f.isFile()) {
            Properties prop = new Properties();
            InputStream input = null;

            try {
               input = new FileInputStream(f);
            } catch (FileNotFoundException var11) {
               return false;
            }

            try {
               prop.load(input);
            } catch (IOException var10) {
               return false;
            }

            this.prog.setProgVerzoegerung(Integer.valueOf(prop.getProperty("Karol.Delay", "100")));
            this.karol.SprunghoeheSetzen(Integer.valueOf(prop.getProperty("Karol.MaxJump", "1")));
            this.karol.setRucksackMaximum(Integer.valueOf(prop.getProperty("Karol.MaxBrick", "0")));
            this.karol.setRucksackStart(Integer.valueOf(prop.getProperty("Karol.BrickStart", "0")));
            this.karol.setRucksackPruefung(Boolean.valueOf(prop.getProperty("Karol.BrickControl", "false")));
            this.prog
               .setOnError(Boolean.valueOf(prop.getProperty("Program.HintOnError", "false")), Boolean.valueOf(prop.getProperty("Program.StopOnError", "true")));
            this.prog.setScrollOnStep(Boolean.valueOf(prop.getProperty("Program.ScrollOnStep", "false")));
            this.cont.setRestoreWelt(Boolean.valueOf(prop.getProperty("Program.RestoreWorld", "false")));
            this.fixProperties = Boolean.valueOf(prop.getProperty("Program.FixProperties", "false"));
            KEdtDocument doc = this.view.getProgDocument();
            KEdtEditorKit kit = (KEdtEditorKit)this.view.getProgTextPanel().getEditorKit();
            kit.setTAB_LEN(Integer.valueOf(prop.getProperty("Editor.TabLen", "2")));
            doc.setFontsizeNormal(Integer.valueOf(prop.getProperty("Editor.FontSize", "13")));
            this.view.getGutterPanel().setFontWidth();
            this.view.getGutterPanel().repaint();
            String defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.COMMENT));
            int colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorComment", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.COMMENT, colorNr);
            defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.KEY));
            colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorKeyWord", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.KEY, colorNr);
            doc.setStyleColor(KEdtToken.TokenType.KEYEND, colorNr);
            defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.KEYANW));
            colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorAnwBed", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.KEYANW, colorNr);
            doc.setStyleColor(KEdtToken.TokenType.KEYBED, colorNr);
            defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.IDENTIFIER));
            colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorIdent", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.IDENTIFIER, colorNr);
            defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.NUMBER));
            colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorNum", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.NUMBER, colorNr);
            defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.PNUMBER));
            colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorParam", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.PNUMBER, colorNr);
            doc.setStyleColor(KEdtToken.TokenType.PCOLOR, colorNr);
            defaultColor = Integer.toHexString(KEdtStyleContext.getStandardStyleColor(KEdtToken.TokenType.KAROL));
            colorNr = (int)Long.parseLong(prop.getProperty("Editor.ColorKarol", defaultColor), 16);
            doc.setStyleColor(KEdtToken.TokenType.KAROL, colorNr);
            doc.setMitSchreibweise(Boolean.valueOf(prop.getProperty("Editor.FormatToken", "true")));
            this.view.getGutterPanel().setMitZeilennummer(Boolean.valueOf(prop.getProperty("Editor.LineNumbers", "false")));
            this.prog.setDefaultProgPfad(prop.getProperty("Program.OpenPath", this.prog.getDefaultProgPfad()));
            this.fixOpenPath = prop.getProperty("Program.FixOpenPath");
            if (this.fixOpenPath == null) {
               this.fixOpenPath = "";
            } else {
               this.prog.setDefaultProgPfad(this.fixOpenPath);
            }

            try {
               input.close();
            } catch (IOException var9) {
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean setProperties() {
      if (this.propFileNameSave.isEmpty()) {
         return false;
      } else if (this.fixProperties) {
         return true;
      } else {
         File f = new File(this.propFileNameSave);
         if (!f.exists()) {
            try {
               if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
                  return false;
               }

               if (!f.createNewFile()) {
                  return false;
               }
            } catch (IOException var9) {
               return false;
            }
         }

         Properties prop = new Properties();
         OutputStream output = null;

         try {
            output = new FileOutputStream(f);
         } catch (FileNotFoundException var8) {
            return false;
         }

         prop.setProperty("Version", "3.0");
         prop.setProperty("Karol.Delay", Integer.toString(this.prog.getProgVerzoegerung()));
         prop.setProperty("Karol.MaxJump", Integer.toString(this.karol.SprungshoeheGeben()));
         prop.setProperty("Karol.MaxBrick", Integer.toString(this.karol.getRucksackMaximum()));
         prop.setProperty("Karol.BrickStart", Integer.toString(this.karol.getRucksackStart()));
         prop.setProperty("Karol.BrickControl", Boolean.toString(this.karol.RucksackPruefungGeben()));
         prop.setProperty("Program.HintOnError", Boolean.toString(this.prog.getShowOnError()));
         prop.setProperty("Program.StopOnError", Boolean.toString(this.prog.getStopOnError()));
         prop.setProperty("Program.RestoreWorld", Boolean.toString(this.cont.getRestoreWelt()));
         prop.setProperty("Program.ScrollOnStep", Boolean.toString(this.prog.getScrollOnStep()));
         prop.setProperty("Program.FixProperties", Boolean.toString(this.fixProperties));
         KEdtDocument doc = this.view.getProgDocument();
         KEdtEditorKit kit = (KEdtEditorKit)this.view.getProgTextPanel().getEditorKit();
         prop.setProperty("Editor.TabLen", Integer.toString(kit.getTAB_LEN()));
         prop.setProperty("Editor.FontSize", Integer.toString(doc.getFontsizeNormal()));
         prop.setProperty("Editor.ColorComment", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.COMMENT)));
         prop.setProperty("Editor.ColorKeyWord", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.KEY)));
         prop.setProperty("Editor.ColorAnwBed", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.KEYANW)));
         prop.setProperty("Editor.ColorIdent", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.IDENTIFIER)));
         prop.setProperty("Editor.ColorNum", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.NUMBER)));
         prop.setProperty("Editor.ColorParam", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.PNUMBER)));
         prop.setProperty("Editor.ColorKarol", Integer.toHexString(doc.getStyleColor(KEdtToken.TokenType.KAROL)));
         prop.setProperty("Editor.FormatToken", Boolean.toString(doc.getMitSchreibweise()));
         prop.setProperty("Editor.LineNumbers", Boolean.toString(this.view.getGutterPanel().getMitZeilennummer()));
         prop.setProperty("Program.OpenPath", this.prog.getDefaultProgPfad());
         if (!this.fixOpenPath.isEmpty()) {
            prop.setProperty("Program.FixOpenPath", this.fixOpenPath);
         }

         try {
            prop.store(output, "Einstellungen RobotKarol Version3.0");
            output.close();
            return true;
         } catch (IOException var7) {
            return false;
         }
      }
   }
}
