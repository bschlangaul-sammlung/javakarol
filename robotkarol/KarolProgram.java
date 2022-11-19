package robotkarol;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import karoleditor.KEdtDocument;
import karoleditor.KEdtGutter;
import karoleditor.KEdtRegexLexer;
import karoleditor.KEdtToken;

public class KarolProgram {
   private KarolWelt welt;
   private KarolRoboter karol;
   private KarolView view;
   private String defaultProgPfad = "";
   private String fileSeparator = "";
   private String progDateiname = "";
   private KEdtDocument doc;
   private Map<String, Integer> KeyMap;
   public Map<Integer, String> TokenMap;
   private List<KEdtToken> GesTokens;
   private StringBuilder GesProgramm;
   public List<KarolProgram.KarolProgBlock> blockArray = new ArrayList<>(20);
   public List<KarolProgram.KarolProgAnw> anwArray = new ArrayList<>(100);
   private Stack<KarolProgram.KarolProgStack> anwStack = new Stack<>();
   public final String[] farbParameter = new String[]{"(rot)", "(gelb)", "(blau)", "(grün)", "(schwarz)"};
   private KarolProgram.ProgStatusType progStatus;
   private KarolProgram.ProgStatusType pauseProgStatus;
   private boolean cError = false;
   private boolean isStopped = false;
   private boolean isFast = false;
   private boolean showOnError = false;
   private boolean stopOnError = true;
   private boolean scrollOnStep = true;
   private int breakLine;
   private int progVerzoegerung = 100;
   private float progScrollPerLine = 0.0F;
   private KarolProgram.ProgThread progThread;

   public KarolProgram(KarolWelt w, KarolRoboter r, KarolView v) {
      this.welt = w;
      this.karol = r;
      this.view = v;
      this.doc = this.view.getProgDocument();
      this.progDateiname = "";
      this.KeyMap = new HashMap<String, Integer>() {
         {
            this.put("programm", Integer.valueOf(1));
            this.put("wiederhole", Integer.valueOf(2));
            this.put("wenn", Integer.valueOf(6));
            this.put("sonst", Integer.valueOf(8));
            this.put("anweisung", Integer.valueOf(9));
            this.put("methode", Integer.valueOf(9));
            this.put("bedingung", Integer.valueOf(10));
            this.put("wahr", Integer.valueOf(11));
            this.put("falsch", Integer.valueOf(12));
            this.put("einfügen", Integer.valueOf(14));
            this.put("endewiederhole", Integer.valueOf(50));
            this.put("*wiederhole", Integer.valueOf(50));
            this.put("endewenn", Integer.valueOf(51));
            this.put("*wenn", Integer.valueOf(51));
            this.put("endeanweisung", Integer.valueOf(52));
            this.put("*anweisung", Integer.valueOf(52));
            this.put("endebedingung", Integer.valueOf(53));
            this.put("*bedingung", Integer.valueOf(53));
            this.put("endemethode", Integer.valueOf(52));
            this.put("*methode", Integer.valueOf(52));
            this.put("endeprogramm", Integer.valueOf(54));
            this.put("*programm", Integer.valueOf(54));
            this.put("endeeinfügen", Integer.valueOf(55));
            this.put("*einfügen", Integer.valueOf(55));
            this.put("schritt", Integer.valueOf(101));
            this.put("linksdrehen", Integer.valueOf(102));
            this.put("rechtsdrehen", Integer.valueOf(103));
            this.put("hinlegen", Integer.valueOf(104));
            this.put("aufheben", Integer.valueOf(105));
            this.put("markesetzen", Integer.valueOf(106));
            this.put("markelöschen", Integer.valueOf(107));
            this.put("warten", Integer.valueOf(108));
            this.put("ton", Integer.valueOf(109));
            this.put("langsam", Integer.valueOf(110));
            this.put("schnell", Integer.valueOf(111));
            this.put("beenden", Integer.valueOf(112));
            this.put("istwand", Integer.valueOf(201));
            this.put("nichtistwand", Integer.valueOf(202));
            this.put("istziegel", Integer.valueOf(203));
            this.put("nichtistziegel", Integer.valueOf(204));
            this.put("istmarke", Integer.valueOf(205));
            this.put("nichtistmarke", Integer.valueOf(206));
            this.put("istnorden", Integer.valueOf(207));
            this.put("istosten", Integer.valueOf(208));
            this.put("istsüden", Integer.valueOf(209));
            this.put("istwesten", Integer.valueOf(210));
            this.put("istvoll", Integer.valueOf(211));
            this.put("nichtistvoll", Integer.valueOf(212));
            this.put("istleer", Integer.valueOf(213));
            this.put("nichtistleer", Integer.valueOf(214));
            this.put("hatziegel", Integer.valueOf(215));
         }
      };
      this.TokenMap = new HashMap<Integer, String>() {
         {
            this.put(Integer.valueOf(1), "Programm");
            this.put(Integer.valueOf(2), "wiederhole");
            this.put(Integer.valueOf(3), "mal");
            this.put(Integer.valueOf(4), "solange");
            this.put(Integer.valueOf(6), "wenn");
            this.put(Integer.valueOf(7), "dann");
            this.put(Integer.valueOf(8), "sonst");
            this.put(Integer.valueOf(9), "Anweisung");
            this.put(Integer.valueOf(10), "Bedingung");
            this.put(Integer.valueOf(11), "wahr");
            this.put(Integer.valueOf(12), "falsch");
            this.put(Integer.valueOf(13), "nicht");
            this.put(Integer.valueOf(14), "Einfügen");
            this.put(Integer.valueOf(15), "bis");
            this.put(Integer.valueOf(16), "immer");
            this.put(Integer.valueOf(50), "*wiederhole");
            this.put(Integer.valueOf(51), "*wenn");
            this.put(Integer.valueOf(52), "*Anweisung");
            this.put(Integer.valueOf(53), "*Bedingung");
            this.put(Integer.valueOf(54), "*Programm");
            this.put(Integer.valueOf(55), "*Einfügen");
            this.put(Integer.valueOf(101), "Schritt");
            this.put(Integer.valueOf(102), "LinksDrehen");
            this.put(Integer.valueOf(103), "RechtsDrehen");
            this.put(Integer.valueOf(104), "Hinlegen");
            this.put(Integer.valueOf(105), "Aufheben");
            this.put(Integer.valueOf(106), "MarkeSetzen");
            this.put(Integer.valueOf(107), "MarkeLöschen");
            this.put(Integer.valueOf(108), "Warten");
            this.put(Integer.valueOf(109), "Ton");
            this.put(Integer.valueOf(110), "Langsam");
            this.put(Integer.valueOf(111), "Schnell");
            this.put(Integer.valueOf(112), "Beenden");
            this.put(Integer.valueOf(201), "IstWand");
            this.put(Integer.valueOf(202), "NichtIstWand");
            this.put(Integer.valueOf(203), "IstZiegel");
            this.put(Integer.valueOf(204), "NichtIstZiegel");
            this.put(Integer.valueOf(205), "IstMarke");
            this.put(Integer.valueOf(206), "NichtIstMarke");
            this.put(Integer.valueOf(207), "IstNorden");
            this.put(Integer.valueOf(208), "IstOsten");
            this.put(Integer.valueOf(209), "IstSüden");
            this.put(Integer.valueOf(210), "IstWesten");
            this.put(Integer.valueOf(211), "IstVoll");
            this.put(Integer.valueOf(212), "NichtIstVoll");
            this.put(Integer.valueOf(213), "IstLeer");
            this.put(Integer.valueOf(214), "NichtIstLeer");
            this.put(Integer.valueOf(215), "HatZiegel");
         }
      };
      this.view.getStruktoPanel().setProgram(this);
      this.cError = false;
      this.GesTokens = new ArrayList<>(700);
      this.GesProgramm = new StringBuilder(1000);
      this.progStatus = KarolProgram.ProgStatusType.PStNothing;
      this.pauseProgStatus = null;
   }

   public void setDefaultProgPfadSep(String s, String f) {
      this.defaultProgPfad = s;
      this.fileSeparator = f;
   }

   public void setDefaultProgPfad(String s) {
      this.defaultProgPfad = s;
      this.welt.setDefaultWeltPfad(s);
   }

   public String getDefaultProgPfad() {
      return this.defaultProgPfad;
   }

   public String getFileSeparator() {
      return this.fileSeparator;
   }

   public String getProgDateiname() {
      return this.progDateiname;
   }

   public int getProgVerzoegerung() {
      return this.progVerzoegerung;
   }

   public void setProgVerzoegerung(int vz) {
      this.progVerzoegerung = vz;
   }

   public KarolProgram.ProgStatusType getProgStatus() {
      return this.progStatus;
   }

   public boolean getStopOnError() {
      return this.stopOnError;
   }

   public boolean getShowOnError() {
      return this.showOnError;
   }

   public void setOnError(boolean show, boolean stop) {
      this.showOnError = show;
      this.stopOnError = stop;
   }

   public boolean getScrollOnStep() {
      return this.scrollOnStep;
   }

   public void setScrollOnStep(boolean scroll) {
      this.scrollOnStep = scroll;
   }

   public boolean newProgFile() {
      if (this.view.getProgDocument().getModified()
         && JOptionPane.showConfirmDialog(
               null,
               "Das aktuelle Programm wurde geändert.\nOhne Speichern gehen die Änderungen verloren.\n\nTrotzdem neues Programm anlegen?",
               "Bestätigung",
               0,
               2
            )
            == 1) {
         return false;
      } else {
         this.view.getGutterPanel().setAllMarkerInvisible();
         this.view.getProgTextPanel().setText("");
         this.view.getProgDocument().resetModified();
         this.progDateiname = "";
         this.view.InfoBoxSetProgrammName(this.progDateiname);
         this.view.getProgTextPanel().setCaretPosition(0);
         return true;
      }
   }

   public boolean openProgFile(String fileNameProg, boolean mitWelt) {
      String fileNameWelt = "";
      String content = "";
      String vorschlag = "";
      if (this.view.getProgDocument().getModified()
         && JOptionPane.showConfirmDialog(
               null,
               "Das aktuelle Programm wurde geändert.\nOhne Speichern gehen die Änderungen verloren.\n\nTrotzdem neues Programm öffnen?",
               "Bestätigung",
               0,
               2
            )
            == 1) {
         return false;
      } else {
         if (fileNameProg.isEmpty()) {
            vorschlag = this.defaultProgPfad;
            if (!this.progDateiname.isEmpty()) {
               vorschlag = Paths.get(this.progDateiname).getParent().toString();
            }

            fileNameProg = this.view.dlgOpenSave.dateiVorhanden(this.view.dlgOpenSave.dateiOeffnen(vorschlag, 'p'), 'p');
         } else {
            fileNameProg = this.view.dlgOpenSave.dateiVorhanden(fileNameProg, 'p');
         }

         if (fileNameProg.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Keine Karolprogramm-Datei ausgewählt", "KarolProgramm öffnen", 0);
            return false;
         } else {
            this.view.getGutterPanel().setAllMarkerInvisible();

            try {
               content = new String(Files.readAllBytes(Paths.get(fileNameProg)), StandardCharsets.ISO_8859_1);
            } catch (IOException var7) {
               return false;
            }

            this.view.getProgTextPanel().setText(content);
            this.progDateiname = fileNameProg;
            this.view.InfoBoxSetProgrammName(this.progDateiname);
            this.view.getProgDocument().resetModified();
            this.setDefaultProgPfad(Paths.get(this.progDateiname).getParent().toString());
            fileNameWelt = fileNameProg.substring(0, fileNameProg.lastIndexOf(".")) + ".kdw";
            File f = new File(fileNameWelt);
            if (f.exists() && f.isFile() && mitWelt) {
               this.welt.loadKarolWelt(fileNameWelt);
               if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_3D) {
                  this.view.getWelt3DPanel().resetWeltAnzeige();
               }

               if (this.view.getViewStatus() == KarolView.DisplayStatusType.VST_2D) {
                  this.view.getWelt2DPanel().resetWeltAnzeige();
               }

               this.view.InfoBoxSetWeltName(fileNameWelt);
            }

            this.view.getProgTextPanel().requestFocus();
            this.view.getProgTextPanel().setCaretPosition(0);
            return true;
         }
      }
   }

   public void saveProgFile() {
      if (this.progDateiname.isEmpty()) {
         this.saveAsProgFile();
      }

      String content = this.view.getProgTextPanel().getText();

      try {
         Files.write(Paths.get(this.progDateiname), content.getBytes(StandardCharsets.ISO_8859_1));
      } catch (IOException var3) {
         return;
      }

      this.view.getProgDocument().resetModified();
   }

   public boolean saveAsProgFile() {
      String fileNameProg = "";
      String vorschlag = "";
      if (!this.progDateiname.isEmpty()) {
         vorschlag = this.progDateiname;
      } else {
         vorschlag = this.defaultProgPfad;
      }

      fileNameProg = this.view.dlgOpenSave.dateiSpeichernUnter(vorschlag, 'p');
      if (fileNameProg.isEmpty()) {
         return false;
      } else {
         String content = this.view.getProgTextPanel().getText();

         try {
            Files.write(Paths.get(fileNameProg), content.getBytes(StandardCharsets.ISO_8859_1));
         } catch (IOException var5) {
            return false;
         }

         this.view.getProgDocument().resetModified();
         this.progDateiname = fileNameProg;
         this.view.InfoBoxSetProgrammName(this.progDateiname);
         this.view.getProgTextPanel().setCaretPosition(0);
         this.setDefaultProgPfad(Paths.get(this.progDateiname).getParent().toString());
         return true;
      }
   }

   public void printProg() {
      File f = new File(this.progDateiname);
      String strDate = MessageFormat.format("{0, date,long}", new Date());
      MessageFormat header = new MessageFormat("KarolProgramm " + f.getName());
      MessageFormat footer = new MessageFormat("gedruckt am " + strDate);

      try {
         this.view.getProgTextPanel().print(header, footer);
      } catch (PrinterException var6) {
      }
   }

   private int searchBlockArray(String bez, byte t) {
      for(int i = 0; i < this.blockArray.size(); ++i) {
         if (this.blockArray.get(i).bezeichner.equalsIgnoreCase(bez) && t == this.blockArray.get(i).typ) {
            return i;
         }
      }

      return -1;
   }

   private int getBedingung(int tokPos, KarolProgram.KarolProgAnw progAnw, int fehlerNr) {
      int ergeb = -1;
      int blockNr = 0;
      KEdtToken tok = null;
      Integer[] nichtZahl = new Integer[]{201, 202, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214};
      Integer[] nichtFarbe = new Integer[]{201, 202, 207, 208, 209, 210, 211, 212, 213, 214, 215};
      progAnw.paramI = 0;
      progAnw.paramB = false;
      progAnw.bedNr = 0;
      if (tokPos >= this.GesTokens.size()) {
         this.CErrorProc(fehlerNr, progAnw.lineNum);
         return -1;
      } else {
         tok = this.GesTokens.get(tokPos);
         if (tok.getTyp() == KEdtToken.TokenType.KEY && this.getStringLower(tok).equals("nicht")) {
            progAnw.paramB = true;
            if (++tokPos >= this.GesTokens.size()) {
               this.CErrorProc(fehlerNr, progAnw.lineNum);
               return -1;
            }

            tok = this.GesTokens.get(tokPos);
         }

         if (tok.getTyp() == KEdtToken.TokenType.KAROL) {
            if (++tokPos >= this.GesTokens.size()) {
               this.CErrorProc(fehlerNr, progAnw.lineNum);
               return -1;
            }

            tok = this.GesTokens.get(tokPos);
         }

         if (tok.getTyp() == KEdtToken.TokenType.KEYBED) {
            progAnw.bedNr = this.KeyMap.get(this.getStringLower(tok));
         }

         if (tok.getTyp() == KEdtToken.TokenType.IDENTIFIER) {
            blockNr = this.searchBlockArray(this.getStringLower(tok), (byte)2);
            if (blockNr < 0) {
               this.CErrorProc(11, progAnw.lineNum);
               return -1;
            }

            progAnw.bedNr = 400 + blockNr;
         }

         if (progAnw.bedNr == 0) {
            this.CErrorProc(fehlerNr, progAnw.lineNum);
            return -1;
         } else if (++tokPos >= this.GesTokens.size()) {
            return tokPos - 1;
         } else {
            tok = this.GesTokens.get(tokPos);
            if (tok.getTyp() == KEdtToken.TokenType.PEMPTY) {
               return tokPos;
            } else if (tok.getTyp() == KEdtToken.TokenType.PNUMBER) {
               String tokStr = this.getStringLower(tok);
               ergeb = Integer.valueOf(tokStr.substring(1, tokStr.length() - 1));
               if (ergeb <= 0) {
                  this.CErrorProc(13, progAnw.lineNum);
                  return -1;
               } else if (Arrays.binarySearch(nichtZahl, Integer.valueOf(progAnw.bedNr)) < 0 && progAnw.bedNr < 400) {
                  progAnw.paramI = ergeb;
                  return tokPos;
               } else {
                  this.CErrorProc(54, progAnw.lineNum);
                  return -1;
               }
            } else if (tok.getTyp() == KEdtToken.TokenType.PCOLOR) {
               String tokStr = this.getStringLower(tok);
               ergeb = Arrays.asList(this.farbParameter).indexOf(tokStr) + 1;
               if (ergeb <= 0) {
                  this.CErrorProc(38, progAnw.lineNum);
                  return -1;
               } else if (Arrays.binarySearch(nichtFarbe, Integer.valueOf(progAnw.bedNr)) < 0 && progAnw.bedNr < 400) {
                  progAnw.paramI = -ergeb;
                  return tokPos;
               } else {
                  this.CErrorProc(55, progAnw.lineNum);
                  return -1;
               }
            } else {
               return tokPos - 1;
            }
         }
      }
   }

   private int getParameter(int tokPos, KarolProgram.KarolProgAnw progAnw) {
      int ergeb = -1;
      KEdtToken tok = null;
      Integer[] nichtZahl = new Integer[]{9, 10, 102, 103, 106, 107, 109, 110, 111, 112};
      Integer[] nichtFarbe = new Integer[]{9, 10, 101, 102, 103, 105, 107, 108, 109, 110, 111, 112};
      progAnw.paramI = 1;
      if (progAnw.schluesselNr == 108) {
         progAnw.paramI = 1000;
      }

      progAnw.paramB = false;
      progAnw.bedNr = 0;
      if (tokPos >= this.GesTokens.size()) {
         return tokPos - 1;
      } else {
         tok = this.GesTokens.get(tokPos);
         if (tok.getTyp() == KEdtToken.TokenType.PEMPTY) {
            return tokPos;
         } else if (tok.getTyp() == KEdtToken.TokenType.PNUMBER) {
            String tokStr = this.getStringLower(tok);
            ergeb = Integer.valueOf(tokStr.substring(1, tokStr.length() - 1));
            if (ergeb <= 0) {
               this.CErrorProc(13, progAnw.lineNum);
               return -1;
            } else if (Arrays.binarySearch(nichtZahl, Integer.valueOf(progAnw.schluesselNr)) < 0 && progAnw.schluesselNr < 300) {
               progAnw.paramI = ergeb;
               return tokPos;
            } else {
               this.CErrorProc(56, progAnw.lineNum);
               return -1;
            }
         } else if (tok.getTyp() == KEdtToken.TokenType.PCOLOR) {
            String tokStr = this.getStringLower(tok);
            ergeb = Arrays.asList(this.farbParameter).indexOf(tokStr) + 1;
            if (ergeb <= 0) {
               this.CErrorProc(38, progAnw.lineNum);
               return -1;
            } else if (Arrays.binarySearch(nichtFarbe, Integer.valueOf(progAnw.schluesselNr)) < 0 && progAnw.schluesselNr < 300) {
               progAnw.paramI = -ergeb;
               return tokPos;
            } else {
               this.CErrorProc(57, progAnw.lineNum);
               return -1;
            }
         } else {
            return tokPos - 1;
         }
      }
   }

   private boolean between(int x, int a, int b) {
      return a <= x && x <= b;
   }

   boolean compileProgramm() {
      if (this.progStatus != KarolProgram.ProgStatusType.PStNothing) {
         return false;
      } else {
         this.view.InfoBoxLoeschen();
         this.view.getGutterPanel().setAllMarkerInvisible();
         this.view.ProgrammLampeSchalten(KarolProgram.ProgStatusType.PStNothing);
         this.anwArray.clear();
         this.blockArray.clear();
         this.anwStack.clear();
         this.cError = false;
         if (!this.prepareProgramm()) {
            return false;
         } else {
            int maxGesTokens = this.GesTokens.size();
            KEdtToken aktTok = null;
            KEdtToken nextTok = null;
            KEdtToken nextNextTok = null;
            KEdtToken.TokenType aktTyp = KEdtToken.TokenType.DEFAULT;
            KEdtToken.TokenType nextTyp = KEdtToken.TokenType.DEFAULT;
            String nextStr = "";
            KarolProgram.KarolProgAnw tempProgAnw = null;
            int keyNr = 0;
            int nextKeyNr = 0;
            int aktLine = 0;
            int paramI = 0;
            int anwNr = 0;
            int nextPos = 0;
            int tempInt = 0;
            Integer tempIntValue = null;
            boolean inAnwDef = false;
            boolean inBedDef = false;
            boolean inProgDef = false;
            boolean hatWahrFalsch = false;
            this.blockArray.add(new KarolProgram.KarolProgBlock((byte)0, "KarolProgramm", 1, 0));
            this.anwArray.add(new KarolProgram.KarolProgAnw(1, 0, 0, false, 0, 1));

            for(int aktPos = 0; aktPos < maxGesTokens && !this.cError; ++aktPos) {
               aktTok = this.GesTokens.get(aktPos);
               aktTyp = aktTok.getTyp();
               aktLine = aktTok.getDocLineNr();
               keyNr = 0;
               switch(aktTyp) {
                  case COMMENT:
                  case DEFAULT:
                     continue;
                  case KEY:
                  case KEYEND:
                  case LIB:
                  case LIBEND:
                  case KEYANW:
                  case KEYBED:
                     tempIntValue = this.KeyMap.get(this.getStringLower(aktTok));
                     if (tempIntValue == null) {
                        this.CErrorProc(67, aktLine);
                        continue;
                     }

                     keyNr = tempIntValue;
                     break;
                  case NUMBER:
                  case PNUMBER:
                  case PCOLOR:
                  case PEMPTY:
                     this.CErrorProc(35, aktLine);
                     continue;
                  case KAROL:
                     keyNr = 500;
                     break;
                  case FILENAME:
                     this.CErrorProc(61, aktLine);
                     continue;
                  case IDENTIFIER:
                     keyNr = 300;
               }

               if (keyNr == 0) {
                  this.CErrorProc(10, aktLine);
               } else {
                  if (!inAnwDef && !inBedDef && !inProgDef && (keyNr == 2 || keyNr == 6 || this.between(keyNr, 101, 112) || this.between(keyNr, 300, 399))) {
                     KarolProgram.KarolProgBlock bl = this.blockArray.get(0);
                     bl.beginn = this.anwArray.size();
                     this.anwStack.push(new KarolProgram.KarolProgStack(1, 0));
                     this.anwArray.add(new KarolProgram.KarolProgAnw(1, 0, 0, false, 0, aktLine));
                     inProgDef = true;
                  }

                  if (keyNr == 1) {
                     if (inAnwDef || inBedDef) {
                        this.CErrorProc(21, aktLine);
                        continue;
                     }

                     KarolProgram.KarolProgBlock bl = this.blockArray.get(0);
                     bl.beginn = this.anwArray.size();
                     this.anwStack.push(new KarolProgram.KarolProgStack(1, 0));
                     this.anwArray.add(new KarolProgram.KarolProgAnw(1, 0, 0, false, 0, aktLine));
                     inProgDef = true;
                  }

                  if (keyNr == 300) {
                     anwNr = this.searchBlockArray(this.getStringLower(aktTok), (byte)1);
                     if (anwNr < 0) {
                        this.CErrorProc(10, aktLine);
                     } else {
                        tempProgAnw = new KarolProgram.KarolProgAnw(300 + anwNr, 0, 0, false, this.blockArray.get(anwNr).beginn, aktLine);
                        nextPos = this.getParameter(aktPos + 1, tempProgAnw);
                        if (nextPos >= 0 && !this.cError) {
                           aktPos = nextPos;
                           tempProgAnw.paramI = 0;
                           this.anwArray.add(tempProgAnw);
                        }
                     }
                  } else if (this.between(keyNr, 101, 112)) {
                     tempProgAnw = new KarolProgram.KarolProgAnw(keyNr, 0, 0, false, 0, aktLine);
                     nextPos = this.getParameter(aktPos + 1, tempProgAnw);
                     if (nextPos >= 0 && !this.cError) {
                        if (keyNr == 104 && tempProgAnw.paramI == -5) {
                           this.CErrorProc(34, tempProgAnw.lineNum);
                        } else {
                           aktPos = nextPos;
                           this.anwArray.add(tempProgAnw);
                        }
                     }
                  } else if (keyNr == 2) {
                     if (aktPos + 1 < maxGesTokens) {
                        nextTok = this.GesTokens.get(aktPos + 1);
                        nextTyp = nextTok.getTyp();
                        nextStr = this.getStringLower(nextTok);
                        if (nextTyp == KEdtToken.TokenType.KEY) {
                           if (nextStr.equals("immer")) {
                              ++aktPos;
                              this.anwStack.push(new KarolProgram.KarolProgStack(5, this.anwArray.size()));
                              this.anwArray.add(new KarolProgram.KarolProgAnw(5, 0, Integer.MAX_VALUE, false, 0, aktLine));
                              continue;
                           }

                           if (nextStr.equals("solange")) {
                              ++aktPos;
                              tempProgAnw = new KarolProgram.KarolProgAnw(4, 0, 0, false, 0, aktLine);
                              nextPos = this.getBedingung(aktPos + 1, tempProgAnw, 15);
                              if (nextPos >= 0 && !this.cError) {
                                 aktPos = nextPos;
                                 this.anwStack.push(new KarolProgram.KarolProgStack(4, this.anwArray.size()));
                                 this.anwArray.add(tempProgAnw);
                              }
                              continue;
                           }

                           if (nextStr.equals("mal")) {
                              this.CErrorProc(19, aktLine);
                              continue;
                           }
                        }

                        if (nextTyp == KEdtToken.TokenType.NUMBER) {
                           if (++aktPos + 1 >= maxGesTokens) {
                              this.CErrorProc(4, aktLine);
                           } else {
                              nextNextTok = this.GesTokens.get(aktPos + 1);
                              if (!this.getStringLower(nextNextTok).equals("mal")) {
                                 this.CErrorProc(4, aktLine);
                              } else {
                                 paramI = Integer.valueOf(nextStr);
                                 if (paramI <= 0) {
                                    this.CErrorProc(13, aktLine);
                                 } else {
                                    ++aktPos;
                                    this.anwStack.push(new KarolProgram.KarolProgStack(3, this.anwArray.size()));
                                    this.anwArray.add(new KarolProgram.KarolProgAnw(3, 0, paramI, false, 0, aktLine));
                                 }
                              }
                           }
                           continue;
                        }
                     }

                     this.anwStack.push(new KarolProgram.KarolProgStack(2, this.anwArray.size()));
                     this.anwArray.add(new KarolProgram.KarolProgAnw(2, 0, 0, false, 0, aktLine));
                  } else if (keyNr == 6) {
                     tempProgAnw = new KarolProgram.KarolProgAnw(6, 0, 0, false, 0, aktLine);
                     nextPos = this.getBedingung(aktPos + 1, tempProgAnw, 2);
                     if (nextPos >= 0 && !this.cError) {
                        aktPos = nextPos;
                        if (nextPos + 1 >= maxGesTokens) {
                           this.CErrorProc(2, aktLine);
                        } else {
                           nextTok = this.GesTokens.get(nextPos + 1);
                           nextTyp = nextTok.getTyp();
                           nextStr = this.getStringLower(nextTok);
                           if (nextTyp == KEdtToken.TokenType.KEY && nextStr.equals("dann")) {
                              aktPos = nextPos + 1;
                              this.anwStack.push(new KarolProgram.KarolProgStack(6, this.anwArray.size()));
                              this.anwArray.add(tempProgAnw);
                           } else {
                              this.CErrorProc(2, aktLine);
                           }
                        }
                     }
                  } else if (keyNr == 9) {
                     if (inAnwDef) {
                        this.CErrorProc(22, aktLine);
                     } else if (inProgDef) {
                        this.CErrorProc(29, aktLine);
                     } else if (aktPos + 1 >= maxGesTokens) {
                        this.CErrorProc(42, aktLine);
                     } else {
                        nextTok = this.GesTokens.get(aktPos + 1);
                        nextTyp = nextTok.getTyp();
                        nextStr = this.getString(nextTok);
                        if (nextTyp != KEdtToken.TokenType.IDENTIFIER) {
                           this.CErrorProc(42, aktLine);
                        } else {
                           anwNr = this.searchBlockArray(nextStr, (byte)1);
                           if (anwNr >= 0) {
                              this.CErrorProc(43, aktLine);
                           } else {
                              ++aktPos;
                              anwNr = this.blockArray.size();
                              inAnwDef = true;
                              this.anwStack.push(new KarolProgram.KarolProgStack(9, this.blockArray.size()));
                              this.blockArray.add(new KarolProgram.KarolProgBlock((byte)1, nextStr, this.anwArray.size(), 0));
                              tempProgAnw = new KarolProgram.KarolProgAnw(9, 0, 0, false, 0, aktLine);
                              nextPos = this.getParameter(aktPos + 1, tempProgAnw);
                              if (nextPos >= 0 && !this.cError) {
                                 aktPos = nextPos;
                                 tempProgAnw.paramI = 0;
                                 this.anwArray.add(tempProgAnw);
                              }
                           }
                        }
                     }
                  } else if (keyNr == 10) {
                     if (inBedDef) {
                        this.CErrorProc(23, aktLine);
                     } else if (aktPos + 1 >= maxGesTokens) {
                        this.CErrorProc(44, aktLine);
                     } else {
                        nextTok = this.GesTokens.get(aktPos + 1);
                        nextTyp = nextTok.getTyp();
                        nextStr = this.getString(nextTok);
                        if (nextTyp != KEdtToken.TokenType.IDENTIFIER) {
                           this.CErrorProc(44, aktLine);
                        } else {
                           anwNr = this.searchBlockArray(nextStr, (byte)2);
                           if (anwNr >= 0) {
                              this.CErrorProc(45, aktLine);
                           } else {
                              ++aktPos;
                              anwNr = this.blockArray.size();
                              inBedDef = true;
                              hatWahrFalsch = false;
                              this.anwStack.push(new KarolProgram.KarolProgStack(10, this.blockArray.size()));
                              this.blockArray.add(new KarolProgram.KarolProgBlock((byte)2, nextStr, this.anwArray.size(), 0));
                              tempProgAnw = new KarolProgram.KarolProgAnw(10, 0, 0, false, 0, aktLine);
                              nextPos = this.getParameter(aktPos + 1, tempProgAnw);
                              if (nextPos >= 0 && !this.cError) {
                                 aktPos = nextPos;
                                 tempProgAnw.paramI = 0;
                                 this.anwArray.add(tempProgAnw);
                              }
                           }
                        }
                     }
                  } else if (keyNr == 14) {
                     if (inAnwDef) {
                        this.CErrorProc(58, aktLine);
                     } else if (inProgDef) {
                        this.CErrorProc(59, aktLine);
                     } else if (aktPos + 1 >= maxGesTokens) {
                        this.CErrorProc(60, aktLine);
                     } else {
                        nextTok = this.GesTokens.get(aktPos + 1);
                        nextTyp = nextTok.getTyp();
                        nextStr = this.getString(nextTok);
                        if (nextTyp != KEdtToken.TokenType.FILENAME) {
                           this.CErrorProc(60, aktLine);
                        } else {
                           ++aktPos;
                        }
                     }
                  } else if (keyNr == 8) {
                     KarolProgram.KarolProgStack wennStack = this.anwStack.peek();
                     if (wennStack.schluesselNr != 6) {
                        this.CErrorProc(3, aktLine);
                     } else {
                        KarolProgram.KarolProgAnw wennAnw = this.anwArray.get(wennStack.anwArrayIndex);
                        wennAnw.geheZu = this.anwArray.size();
                        wennAnw.schluesselNr = 7;
                        this.anwStack.push(new KarolProgram.KarolProgStack(8, this.anwArray.size()));
                        this.anwArray.add(new KarolProgram.KarolProgAnw(8, 0, 0, false, 0, aktLine));
                     }
                  } else if (keyNr != 11 && keyNr != 12) {
                     if (keyNr == 500) {
                        if (aktPos + 1 >= maxGesTokens) {
                           this.CErrorProc(28, aktLine);
                        } else {
                           nextTok = this.GesTokens.get(aktPos + 1);
                           nextTyp = nextTok.getTyp();
                           if (nextTyp != KEdtToken.TokenType.KEYANW && nextTyp != KEdtToken.TokenType.KEYBED && nextTyp != KEdtToken.TokenType.IDENTIFIER) {
                              this.CErrorProc(28, aktLine);
                           }
                        }
                     } else if (keyNr == 50) {
                        if (aktPos + 1 < maxGesTokens) {
                           nextTok = this.GesTokens.get(aktPos + 1);
                           nextTyp = nextTok.getTyp();
                           nextStr = this.getStringLower(nextTok);
                           if (nextTyp == KEdtToken.TokenType.KEY && (nextStr.equals("solange") || nextStr.equals("bis"))) {
                              byte var55;
                              byte var69;
                              if (nextStr.equals("solange")) {
                                 var55 = 60;
                                 var69 = 18;
                              } else {
                                 var55 = 61;
                                 var69 = 17;
                              }

                              ++aktPos;
                              tempProgAnw = new KarolProgram.KarolProgAnw(var55, 0, 0, false, 0, aktLine);
                              nextPos = this.getBedingung(aktPos + 1, tempProgAnw, var69);
                              if (nextPos >= 0 && !this.cError) {
                                 KarolProgram.KarolProgStack wdhStack = this.anwStack.pop();
                                 if (wdhStack.schluesselNr != 2) {
                                    this.CErrorProc(47, aktLine);
                                 } else {
                                    KarolProgram.KarolProgAnw wdhAnw = this.anwArray.get(wdhStack.anwArrayIndex);
                                    wdhAnw.geheZu = this.anwArray.size();
                                    tempProgAnw.geheZu = wdhStack.anwArrayIndex;
                                    this.anwArray.add(tempProgAnw);
                                 }
                              }
                              continue;
                           }
                        }

                        KarolProgram.KarolProgStack wdhStack = this.anwStack.pop();
                        if (wdhStack.schluesselNr != 3 && wdhStack.schluesselNr != 4 && wdhStack.schluesselNr != 5) {
                           this.CErrorProc(48, aktLine);
                        } else {
                           KarolProgram.KarolProgAnw wdhAnw = this.anwArray.get(wdhStack.anwArrayIndex);
                           wdhAnw.geheZu = this.anwArray.size();
                           this.anwArray.add(new KarolProgram.KarolProgAnw(50, 0, 0, false, wdhStack.anwArrayIndex, aktLine));
                        }
                     } else {
                        if (keyNr == 51) {
                           KarolProgram.KarolProgStack wennStack = this.anwStack.pop();
                           if (wennStack.schluesselNr != 6 && wennStack.schluesselNr != 8) {
                              this.CErrorProc(49, aktLine);
                              continue;
                           }

                           if (wennStack.schluesselNr == 6) {
                              KarolProgram.KarolProgAnw wennAnw = this.anwArray.get(wennStack.anwArrayIndex);
                              wennAnw.geheZu = this.anwArray.size();
                              this.anwArray.add(new KarolProgram.KarolProgAnw(51, 0, 0, false, 0, aktLine));
                              continue;
                           }

                           if (wennStack.schluesselNr == 8) {
                              KarolProgram.KarolProgAnw wennAnw = this.anwArray.get(wennStack.anwArrayIndex);
                              wennAnw.geheZu = this.anwArray.size();
                              this.anwStack.pop();
                              this.anwArray.add(new KarolProgram.KarolProgAnw(51, 0, 0, false, 0, aktLine));
                              continue;
                           }
                        }

                        if (keyNr == 52) {
                           if (!inAnwDef) {
                              this.CErrorProc(50, aktLine);
                           } else {
                              KarolProgram.KarolProgStack defStack = this.anwStack.pop();
                              if (defStack.schluesselNr != 9) {
                                 this.CErrorProc(50, aktLine);
                              } else {
                                 KarolProgram.KarolProgBlock defBlock = this.blockArray.get(defStack.anwArrayIndex);
                                 defBlock.ende = this.anwArray.size();
                                 this.anwArray.add(new KarolProgram.KarolProgAnw(52, 0, 0, false, 0, aktLine));
                                 inAnwDef = false;
                              }
                           }
                        } else if (keyNr == 53) {
                           if (!inBedDef) {
                              this.CErrorProc(51, aktLine);
                           } else if (!hatWahrFalsch) {
                              this.CErrorProc(12, aktLine);
                           } else {
                              KarolProgram.KarolProgStack defStack = this.anwStack.pop();
                              if (defStack.schluesselNr != 10) {
                                 this.CErrorProc(51, aktLine);
                              } else {
                                 KarolProgram.KarolProgBlock defBlock = this.blockArray.get(defStack.anwArrayIndex);
                                 defBlock.ende = this.anwArray.size();
                                 this.anwArray.add(new KarolProgram.KarolProgAnw(53, 0, 0, false, 0, aktLine));
                                 inBedDef = false;
                              }
                           }
                        } else if (keyNr != 55 && keyNr == 54) {
                           if (!inProgDef) {
                              this.CErrorProc(52, aktLine);
                           } else {
                              KarolProgram.KarolProgStack defStack = this.anwStack.pop();
                              if (defStack.schluesselNr != 1) {
                                 this.CErrorProc(52, aktLine);
                              } else {
                                 KarolProgram.KarolProgBlock defBlock = this.blockArray.get(0);
                                 defBlock.ende = this.anwArray.size();
                                 this.anwArray.add(new KarolProgram.KarolProgAnw(54, 0, 0, false, 0, aktLine));
                                 inProgDef = false;
                                 aktPos = maxGesTokens;
                              }
                           }
                        }
                     }
                  } else if (!inBedDef) {
                     this.CErrorProc(46, aktLine);
                  } else {
                     hatWahrFalsch = true;
                     this.anwArray.add(new KarolProgram.KarolProgAnw(keyNr, 0, 0, false, 0, aktLine));
                  }
               }
            }

            if ((inAnwDef || inBedDef) && !this.cError) {
               this.CErrorProc(53, aktLine);
            }

            if (!this.cError && inProgDef) {
               if (!this.anwStack.empty()) {
                  KarolProgram.KarolProgStack defStack = this.anwStack.pop();
                  if (defStack.schluesselNr != 1) {
                     this.CErrorProc(53, aktLine);
                  }
               }

               KarolProgram.KarolProgBlock defBlock = this.blockArray.get(0);
               defBlock.ende = this.anwArray.size();
               this.anwArray.add(new KarolProgram.KarolProgAnw(54, 0, 0, false, 0, aktLine));
               inProgDef = false;
            }

            if (this.cError) {
               this.view.ProgrammLampeSchalten(KarolProgram.ProgStatusType.PStStop);
            } else {
               this.view.InfoBoxHinzufuegen(6, "Kein Syntaxfehler\n");
            }

            return !this.cError;
         }
      }
   }

   public String getString(KEdtToken token) {
      String result = "";
      if (token != null) {
         result = this.GesProgramm.substring(token.start, token.start + token.length);
      }

      return result;
   }

   public String getStringLower(KEdtToken token) {
      return this.getString(token).toLowerCase();
   }

   public void showAllTokens() {
      if (this.GesTokens != null && !this.GesTokens.isEmpty()) {
         for(KEdtToken t : this.GesTokens) {
            JOptionPane.showMessageDialog(null, t.toString());
         }
      }
   }

   public void showAllTokens(List<KEdtToken> tokens) {
      if (tokens != null && !tokens.isEmpty()) {
         for(KEdtToken t : tokens) {
            JOptionPane.showMessageDialog(null, t.toString());
         }
      }
   }

   boolean prepareProgramm() {
      KEdtToken merkFileToken = null;
      KEdtToken merkNextToken = null;
      String libFileName = "";
      String libPfad = "";
      String progPfad = "";
      String libContent = "";
      this.GesTokens.clear();
      this.doc.getNewTokenListMitLine(this.GesTokens);
      if (this.GesTokens != null && !this.GesTokens.isEmpty() && this.GesTokens.size() != 0) {
         if (this.doc.getLength() == 0) {
            return false;
         } else {
            try {
               this.GesProgramm.setLength(0);
               this.GesProgramm.append(this.doc.getText(0, this.doc.getLength()));
            } catch (BadLocationException var22) {
               return false;
            }

            boolean fertig = false;
            int suchPos = 0;

            while(!fertig) {
               boolean gefunden = false;
               ListIterator<KEdtToken> ti = this.GesTokens.listIterator(suchPos);

               while(ti.hasNext() && !this.cError && !gefunden) {
                  KEdtToken t = ti.next();
                  if (t.getTyp() == KEdtToken.TokenType.LIB) {
                     if (!ti.hasNext()) {
                        this.CErrorProc(60, t.getDocLineNr());
                     } else {
                        t = ti.next();
                        if (t.getTyp() != KEdtToken.TokenType.FILENAME) {
                           this.CErrorProc(60, t.getDocLineNr());
                        } else {
                           merkFileToken = t;
                           libFileName = this.getString(t);
                           if (!ti.hasNext()) {
                              this.CErrorProc(62, t.getDocLineNr());
                           } else {
                              t = ti.next();
                              if (t.getTyp() != KEdtToken.TokenType.LIBEND) {
                                 this.CErrorProc(62, t.getDocLineNr());
                              } else if (!ti.hasNext()) {
                                 this.CErrorProc(66, t.getDocLineNr());
                              } else {
                                 merkNextToken = ti.next();
                                 t = ti.previous();
                                 gefunden = true;
                              }
                           }
                        }
                     }
                  }
               }

               if (!gefunden) {
                  fertig = true;
               } else {
                  if (!this.progDateiname.isEmpty()) {
                     Path p = Paths.get(this.progDateiname);
                     progPfad = p.getParent().toString();
                  }

                  libPfad = "";
                  File f = new File(libFileName);

                  try {
                     libPfad = f.getCanonicalPath();
                  } catch (IOException var24) {
                     this.CErrorProc(65, merkFileToken.getDocLineNr());
                     fertig = true;
                     continue;
                  }

                  if (!libPfad.equals(libFileName) && !progPfad.isEmpty()) {
                     f = new File(progPfad, libFileName);

                     try {
                        libPfad = f.getCanonicalPath();
                     } catch (IOException var23) {
                        this.CErrorProc(65, merkFileToken.getDocLineNr());
                        fertig = true;
                        continue;
                     }
                  }

                  if (f.exists() && f.isFile() && !libPfad.isEmpty()) {
                     libContent = "";

                     try {
                        libContent = new String(Files.readAllBytes(Paths.get(libPfad)));
                     } catch (IOException var21) {
                     }

                     if (libContent.isEmpty()) {
                        this.CErrorProc(64, merkFileToken.getDocLineNr());
                        fertig = true;
                     } else {
                        List<KEdtToken> LibTokens = new ArrayList<>(200);
                        KEdtRegexLexer lexer = new KEdtRegexLexer();
                        lexer.parse(libContent, 0, LibTokens);
                        int anzZeichen = libContent.length();
                        int abZeichenPos = merkNextToken.getStart();
                        this.GesProgramm.insert(abZeichenPos, libContent);
                        int lnr = merkFileToken.getDocLineNr();

                        for(KEdtToken t : LibTokens) {
                           t.setDocLineNr(lnr);
                           t.setStart(t.getStart() + abZeichenPos);
                        }

                        int abTokPos = this.GesTokens.indexOf(merkNextToken);

                        for(KEdtToken t : this.GesTokens) {
                           t.setStart(t.getStart() + anzZeichen);
                        }

                        this.GesTokens.addAll(abTokPos, LibTokens);
                        suchPos = abTokPos + LibTokens.size();
                        if (suchPos >= this.GesTokens.size()) {
                           fertig = true;
                        }
                     }
                  } else {
                     this.CErrorProc(63, merkFileToken.getDocLineNr());
                     this.CErrorProcZus("ungültiger Dateipfad: " + libPfad);
                     fertig = true;
                  }
               }
            }

            return !this.cError;
         }
      } else {
         return false;
      }
   }

   public void CErrorProc(int fnr, int lnr) {
      String errorStr = "Compiler Fehler";
      switch(fnr) {
         case 1:
            errorStr = "bed. Anweisung wenn: unbekannte oder fehlende Bedingung.";
            break;
         case 2:
            errorStr = "bed. Anweisung wenn hat die Form: wenn {bedingung} dann.";
            break;
         case 3:
            errorStr = "zweiseitige bed. Anweisung wenn: zum sonst fehlt passendes wenn. Verschachtelungsfehler!";
            break;
         case 4:
            errorStr = "Wiederholung mit fester Anzahl: nach der Zahl für die Wiederholungsanzahl muss das Wort mal folgen.";
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 14:
         case 16:
         case 24:
         case 25:
         case 26:
         case 27:
         case 30:
         case 31:
         case 32:
         case 33:
         default:
            break;
         case 10:
            errorStr = "Unbekannte Anweisung/Methode. Schreibweise kontrollieren!";
            break;
         case 11:
            errorStr = "Unbekannte Bedingung. Schreibweise kontrollieren!";
            break;
         case 12:
            errorStr = "Der Bezeichner wahr oder falsch muss mindestens einmal in der selbstdef. Bedingung vorkommen.";
            break;
         case 13:
            errorStr = "Der Parameter muss eine positive Ganzzahl sein.";
            break;
         case 15:
            errorStr = "bed.Wiederholung wiederhole solange: unbekannte oder fehlende Bedingung.";
            break;
         case 17:
            errorStr = "bed.Wiederholung wiederhole  *wiederhole bis : unbekannte oder fehlende Bedingung.";
            break;
         case 18:
            errorStr = "bed.Wiederholung wiederhole  *wiederhole solange : unbekannte oder fehlende Bedingung.";
            break;
         case 19:
            errorStr = "Wiederholung mit fester Anzahl: fehlt Zahl für die Anzahl an Wiederholungen";
            break;
         case 20:
            errorStr = "Fehler in der Kontrollstuktur wiederhole.";
            break;
         case 21:
            errorStr = "programm ist nicht innerhalb von Anweisung/Bedingung möglich.";
            break;
         case 22:
            errorStr = "fehlt Ende der selbstdef. Anweisung/Methode.";
            break;
         case 23:
            errorStr = "fehlt Ende der selbstdef. Bedingung.";
            break;
         case 28:
            errorStr = "Ist keine Methode des Objekts karol.";
            break;
         case 29:
            errorStr = "Anweisung/Methode und Bedingung dürfen nur vor dem Programm definiert werden.";
            break;
         case 34:
            errorStr = "Schwarze Ziegel sind nicht möglich.";
            break;
         case 35:
            errorStr = "Unerlaubte Verwendung von Parameter (Zahl oder Farbe).";
            break;
         case 36:
            errorStr = "Fehlt Klammer zu.";
            break;
         case 37:
            errorStr = "Bei dieser Anweisungen ist kein Parameter möglich.";
            break;
         case 38:
            errorStr = "Diese Farbe ist unbekannt.";
            break;
         case 39:
            errorStr = "Für diese Anweisung ist kein Parameter möglich.";
            break;
         case 40:
            errorStr = "Als Farben sind nur rot, gelb, blau, grün möglich.";
            break;
         case 41:
            errorStr = "Als Parameter sind nur Zahlen oder Farben möglich.";
            break;
         case 42:
            errorStr = "Fehlt Bezeichner für die selbstdefinierte Anweisung/Methode.";
            break;
         case 43:
            errorStr = "Eine Anweisung/Methode mit diesem Namen gibt es bereits.";
            break;
         case 44:
            errorStr = "Fehlt Bezeichner für die selbstdefinierte Bedingung.";
            break;
         case 45:
            errorStr = "Eine Bedingung mit diesem Namen gibt es bereits.";
            break;
         case 46:
            errorStr = "Die Bezeichner wahr und falsch können nur innerhalb einer selbstdef. Bedingung verwendet werden.";
            break;
         case 47:
            errorStr = "Ende einer Wiederholung mit EndBedingung: es fehlt passendes wiederhole";
            break;
         case 48:
            errorStr = "Ende einer Wiederholung mit AnfangsBedingung: es fehlt passendes wiederhole";
            break;
         case 49:
            errorStr = "Ende bed. Anweisung *wenn: es fehlt passendes wenn bzw. sonst";
            break;
         case 50:
            errorStr = "Ende der Definition einer Anweisung/Methode ohne Kennwort anweisung/methode als Beginn. Evtl. Verschachtelungsfehler.";
            break;
         case 51:
            errorStr = "Ende der Definition einer Bedingung ohne Kennwort bedingung als Beginn. Evtl. Verschachtelungsfehler.";
            break;
         case 52:
            errorStr = "Programmende *programm ohne programm als Kennung für Programmbeginn. Evtl. Verschachtelungsfehler.";
            break;
         case 53:
            errorStr = "selbstdefinierte Anweisung/Methode/Bedingung wurde nicht beendet.";
            break;
         case 54:
            errorStr = "Für diese Bedingung ist kein Zahlenparameter möglich.";
            break;
         case 55:
            errorStr = "Für diese Bedingung ist kein Farbenparameter möglich.";
            break;
         case 56:
            errorStr = "Für diese Anweisung/Methode ist kein Zahlenparameter möglich.";
            break;
         case 57:
            errorStr = "Für diese Anweisung/Methode ist kein Farbenparameter möglich.";
            break;
         case 58:
            errorStr = "Einfügen einer Bibliothek innerhalb einer Anweisung/Methode nicht möglich.";
            break;
         case 59:
            errorStr = "Einfügen einer Bibliothek nur vor dem Hauptprogramm möglich.";
            break;
         case 60:
            errorStr = "Bei einfügen einer Bibliothek fehlt Dateiname der Bibliothek.";
            break;
         case 61:
            errorStr = "Dateinamen ohne einfügen sind nicht zulässig.";
            break;
         case 62:
            errorStr = "Bei einfügen fehlt der Abschluss durch *einfügen bzw. endeeinfügen.";
            break;
         case 63:
            errorStr = "Die bei einfügen angeführte Datei existiert nicht.";
            break;
         case 64:
            errorStr = "Die angegebene Bibliotheksdatei ist leer.";
            break;
         case 65:
            errorStr = "Ungültige Pfadangabe für die Bibliotheksdatei.";
            break;
         case 66:
            errorStr = "Nach dem *einfügen bzw. endeeinfügen fehlt ein Hauptprogramm";
            break;
         case 67:
            errorStr = "Schlüsselwort der Sprache Karol in falschem Zusammenhang verwendet.";
      }

      this.cError = true;
      errorStr = "[Fehlernr " + String.valueOf(fnr) + "][Zeile " + lnr + "] " + errorStr + "\n";
      this.view.InfoBoxHinzufuegen(6, errorStr);
      this.view.getGutterPanel().setMarkerToLine(KEdtGutter.MarkerType.ALERT, lnr);
   }

   public void CErrorProcZus(String errorStr) {
      if (errorStr.isEmpty()) {
         errorStr = "Compiler Fehler";
      }

      this.cError = true;
      this.view.InfoBoxHinzufuegen(7, errorStr + "\n");
   }

   public void showAllProg() {
      int zaehler = 0;
      JOptionPane.showMessageDialog(null, "Karol Programm");
      JOptionPane.showMessageDialog(null, "AnweisungsAnzahl: " + Integer.toString(this.anwArray.size()));
      if (this.anwArray != null && !this.anwArray.isEmpty()) {
         for(KarolProgram.KarolProgAnw p : this.anwArray) {
            JOptionPane.showMessageDialog(null, "Anweisung " + Integer.toString(++zaehler) + "\n" + p.toString());
         }
      }
   }

   public void showAllStack() {
      JOptionPane.showMessageDialog(null, "Karol Stack");
      if (this.anwStack != null && !this.anwStack.isEmpty()) {
         for(KarolProgram.KarolProgStack s : this.anwStack) {
            JOptionPane.showMessageDialog(null, s.toString());
         }
      }
   }

   public void runProgramm(KarolProgram.ProgStatusType prgStat) {
      if (this.progThread == null || !this.progThread.isAlive()) {
         if (this.compileProgramm()) {
            this.progStatus = prgStat;
            this.view.getGutterPanel().setAllMarkerInvisible();
            this.view.ProgrammLampeSchalten(this.progStatus);
            this.isStopped = false;
            this.isFast = false;
            this.karol.VerzoegerungSetzen(0);
            this.karol.clearFehlerText();
            this.karol.setRucksackAnzahl(this.karol.getRucksackStart());
            this.breakLine = this.view.getGutterPanel().getBreakPointLine();
            this.progThread = new KarolProgram.ProgThread();
            this.progThread.start();
         }
      }
   }

   private void setRunMarker(int line) {
      if (this.progStatus != KarolProgram.ProgStatusType.PStRunningFast) {
         this.view.getGutterPanel().setMarkerToLine(KEdtGutter.MarkerType.TRACE, line);
      }
   }

   private void doDelay() {
      if (this.progStatus == KarolProgram.ProgStatusType.PStRunning && !this.isFast) {
         try {
            Thread.sleep((long)this.progVerzoegerung);
         } catch (InterruptedException var2) {
         }
      }
   }

   private void doWait() {
      if (this.progStatus == KarolProgram.ProgStatusType.PStTracing) {
         if (this.scrollOnStep) {
            this.scrollProgrammText();
         }

         synchronized(this.progThread) {
            try {
               this.progThread.wait();
            } catch (InterruptedException var3) {
            }
         }
      }
   }

   private boolean runBedingung(KarolProgram.KarolProgAnw bedAnw) {
      boolean ergeb = true;
      KarolProgram.KarolProgBlock bedBlock = null;
      switch(bedAnw.bedNr) {
         case 201:
            ergeb = this.karol.IstWand();
            break;
         case 202:
            ergeb = !this.karol.IstWand();
            break;
         case 203:
            ergeb = this.karol.IstZiegel(bedAnw.paramI);
            break;
         case 204:
            ergeb = !this.karol.IstZiegel(bedAnw.paramI);
            break;
         case 205:
            ergeb = this.karol.IstMarke(bedAnw.paramI);
            break;
         case 206:
            ergeb = !this.karol.IstMarke(bedAnw.paramI);
            break;
         case 207:
            ergeb = this.karol.IstBlickNorden();
            break;
         case 208:
            ergeb = this.karol.IstBlickOsten();
            break;
         case 209:
            ergeb = this.karol.IstBlickSueden();
            break;
         case 210:
            ergeb = this.karol.IstBlickWesten();
            break;
         case 211:
            ergeb = this.karol.IstRucksackVoll();
            break;
         case 212:
            ergeb = !this.karol.IstRucksackVoll();
            break;
         case 213:
            ergeb = this.karol.IstRucksackLeer();
            break;
         case 214:
            ergeb = !this.karol.IstRucksackLeer();
            break;
         case 215:
            ergeb = this.karol.HatZiegel(bedAnw.paramI);
      }

      if (bedAnw.bedNr > 400 && bedAnw.bedNr < 500) {
         bedBlock = this.blockArray.get(bedAnw.bedNr - 400);
         this.runBereich(bedBlock.beginn + 1, bedBlock.ende - 1, bedAnw.bedNr - 400);
         ergeb = bedBlock.result;
      }

      if (!this.karol.getFehlerText().isEmpty() && (this.stopOnError || this.showOnError)) {
         this.RunErrorProc(bedAnw.lineNum, this.stopOnError);
         this.isStopped = this.stopOnError;
      }

      if (this.progThread.isInterrupted()) {
         this.isStopped = true;
      }

      if (bedAnw.paramB) {
         ergeb = !ergeb;
      }

      return ergeb;
   }

   private void boolEintragen(boolean wert, int blockNr) {
      if (blockNr < this.blockArray.size() && this.blockArray.get(blockNr).typ == 2) {
         this.blockArray.get(blockNr).result = wert;
      }
   }

   private void runBereich(int startAnw, int endAnw, int blockNr) {
      int posAnw = startAnw;
      KarolProgram.KarolProgAnw aktAnw = null;
      KarolProgram.KarolProgBlock anwBlock = null;
      boolean bTemp = false;

      while(posAnw <= endAnw && posAnw < this.anwArray.size() && !this.isStopped) {
         if (this.anwArray.get(posAnw).schluesselNr == 1) {
            ++posAnw;
         } else if (this.anwArray.get(posAnw).schluesselNr == 54) {
            ++posAnw;
         } else {
            aktAnw = this.anwArray.get(posAnw);
            int aktParamI = aktAnw.paramI;
            if (this.breakLine == aktAnw.lineNum) {
               this.progStatus = KarolProgram.ProgStatusType.PStTracing;
               this.view.ProgrammLampeSchalten(this.progStatus);
            }

            this.setRunMarker(aktAnw.lineNum);
            this.doWait();
            switch(aktAnw.schluesselNr) {
               case 2:
               case 8:
               case 50:
               case 51:
               default:
                  break;
               case 3:
               case 5:
                  for(int w = 1; w <= aktParamI; ++w) {
                     this.runBereich(posAnw + 1, aktAnw.geheZu, blockNr);
                     this.setRunMarker(aktAnw.lineNum);
                     this.doWait();
                     if (this.isStopped) {
                        break;
                     }
                  }

                  posAnw = aktAnw.geheZu;
                  break;
               case 4:
                  while(this.runBedingung(aktAnw) && !this.isStopped) {
                     this.runBereich(posAnw + 1, aktAnw.geheZu, blockNr);
                     this.setRunMarker(aktAnw.lineNum);
                     this.doWait();
                  }

                  posAnw = aktAnw.geheZu;
                  break;
               case 6:
                  if (!this.runBedingung(aktAnw) && !this.isStopped) {
                     posAnw = aktAnw.geheZu;
                  }
                  break;
               case 7:
                  bTemp = this.runBedingung(aktAnw);
                  if (!bTemp && !this.isStopped) {
                     posAnw = aktAnw.geheZu;
                  }

                  if (bTemp && !this.isStopped) {
                     this.runBereich(posAnw + 1, aktAnw.geheZu - 1, blockNr);
                     posAnw = this.anwArray.get(aktAnw.geheZu).geheZu;
                  }
                  break;
               case 11:
                  this.boolEintragen(true, blockNr);
                  break;
               case 12:
                  this.boolEintragen(false, blockNr);
                  break;
               case 60:
                  if (this.runBedingung(aktAnw) && !this.isStopped) {
                     posAnw = aktAnw.geheZu - 1;
                  }
                  break;
               case 61:
                  if (!this.runBedingung(aktAnw) && !this.isStopped) {
                     posAnw = aktAnw.geheZu - 1;
                  }
                  break;
               case 101:
                  this.karol.Schritt(aktParamI);
                  break;
               case 102:
                  this.karol.LinksDrehen();
                  break;
               case 103:
                  this.karol.RechtsDrehen();
                  break;
               case 104:
                  this.karol.Hinlegen(aktParamI);
                  break;
               case 105:
                  this.karol.Aufheben(aktParamI);
                  break;
               case 106:
                  this.karol.MarkeSetzen(aktParamI);
                  break;
               case 107:
                  this.karol.MarkeLoeschen();
                  break;
               case 108:
                  this.karol.Warten(aktParamI);
                  break;
               case 109:
                  this.karol.TonErzeugen();
                  break;
               case 110:
                  this.isFast = false;
                  break;
               case 111:
                  this.isFast = true;
                  break;
               case 112:
                  this.isStopped = true;
                  this.isFast = false;
            }

            if (aktAnw.schluesselNr > 300 && aktAnw.schluesselNr < 400) {
               anwBlock = this.blockArray.get(aktAnw.schluesselNr - 300);
               this.runBereich(anwBlock.beginn + 1, anwBlock.ende - 1, aktAnw.schluesselNr - 300);
            }

            if (!this.karol.getFehlerText().isEmpty() && (this.stopOnError || this.showOnError)) {
               this.RunErrorProc(aktAnw.lineNum, this.stopOnError);
               this.isStopped = this.stopOnError;
            }

            if (!this.isFast) {
               this.view.ansichtAktualisieren(this.karol);
            }

            this.doDelay();
            if (this.progThread.isInterrupted()) {
               this.isStopped = true;
            }

            ++posAnw;
         }
      }
   }

   public void doProgRunNormal() {
      if (this.progStatus == KarolProgram.ProgStatusType.PStNothing) {
         this.pauseProgStatus = null;
         this.runProgramm(KarolProgram.ProgStatusType.PStRunning);
      }

      if (this.progStatus == KarolProgram.ProgStatusType.PStTracing) {
         this.progStatus = KarolProgram.ProgStatusType.PStRunning;
         this.pauseProgStatus = null;
         this.view.ProgrammLampeSchalten(this.progStatus);
         if (this.progThread != null && this.progThread.isAlive()) {
            synchronized(this.progThread) {
               this.progThread.notify();
            }
         }
      }
   }

   public void doProgRunFast() {
      if (this.progStatus == KarolProgram.ProgStatusType.PStNothing) {
         this.pauseProgStatus = null;
         this.runProgramm(KarolProgram.ProgStatusType.PStRunningFast);
      }

      if (this.progStatus == KarolProgram.ProgStatusType.PStTracing) {
         this.progStatus = KarolProgram.ProgStatusType.PStRunningFast;
         this.pauseProgStatus = null;
         this.view.ProgrammLampeSchalten(this.progStatus);
         if (this.progThread != null && this.progThread.isAlive()) {
            synchronized(this.progThread) {
               this.progThread.notify();
            }
         }
      }
   }

   private void scrollProgrammVorbereiten() {
      JScrollBar vSB = this.view.getScrollBoxProgramm().getVerticalScrollBar();
      this.progScrollPerLine = (float)(vSB.getMaximum() - vSB.getVisibleAmount()) / (float)this.doc.getLineCount();
   }

   private void scrollProgrammText() {
      if (this.progScrollPerLine > 0.0F) {
         int tmLine = this.view.getGutterPanel().getTraceMarkerLine();
         JViewport vPort = this.view.getScrollBoxProgramm().getViewport();
         int tmpScrollMode = vPort.getScrollMode();
         vPort.setScrollMode(0);
         this.view.getScrollBoxProgramm().getVerticalScrollBar().setValue(Math.round(this.progScrollPerLine * (float)tmLine));
         vPort.setScrollMode(tmpScrollMode);
      }
   }

   public void doProgStepByStep() {
      if (this.progStatus == KarolProgram.ProgStatusType.PStNothing) {
         this.scrollProgrammVorbereiten();
         this.runProgramm(KarolProgram.ProgStatusType.PStTracing);
      } else {
         if (this.progStatus == KarolProgram.ProgStatusType.PStTracing && this.progThread != null && this.progThread.isAlive()) {
            synchronized(this.progThread) {
               this.progThread.notify();
            }
         }
      }
   }

   public void doProgPause() {
      if (this.pauseProgStatus == null) {
         if (this.progStatus == KarolProgram.ProgStatusType.PStRunning || this.progStatus == KarolProgram.ProgStatusType.PStRunningFast) {
            this.pauseProgStatus = this.progStatus;
            this.progStatus = KarolProgram.ProgStatusType.PStTracing;
            this.view.ProgrammLampeSchalten(this.progStatus);
         }
      } else {
         this.progStatus = this.pauseProgStatus;
         this.pauseProgStatus = null;
         this.view.ProgrammLampeSchalten(this.progStatus);
         if (this.progThread != null && this.progThread.isAlive()) {
            synchronized(this.progThread) {
               this.progThread.notify();
            }
         }
      }
   }

   public void doProgStopp() {
      this.isStopped = true;
      this.isFast = false;
      if (this.progThread != null && this.progThread.isAlive()) {
         this.progThread.interrupt();
      }

      this.progStatus = KarolProgram.ProgStatusType.PStNothing;
   }

   public void RunErrorProc(int lnr, boolean stop) {
      String errorStr = null;
      if (stop) {
         errorStr = "[Abbruch][Zeile " + String.valueOf(lnr) + "]: Karol " + this.karol.getFehlerText() + "\n";
         this.view.InfoBoxHinzufuegen(5, errorStr);
         this.view.getGutterPanel().setMarkerToLine(KEdtGutter.MarkerType.BREAK, lnr);
      } else {
         errorStr = "[Hinweis]: Karol " + this.karol.getFehlerText() + "\n";
         this.view.InfoBoxHinzufuegen(errorStr);
      }

      this.karol.clearFehlerText();
   }

   class KarolProgAnw {
      int schluesselNr;
      int bedNr;
      int paramI;
      boolean paramB;
      int geheZu;
      int lineNum;

      public KarolProgAnw() {
         this.schluesselNr = 0;
         this.bedNr = 0;
         this.paramI = 0;
         this.paramB = false;
         this.geheZu = 0;
         this.lineNum = 0;
      }

      public KarolProgAnw(int snr, int bnr, int pi, boolean pb, int gz, int ln) {
         this.schluesselNr = snr;
         this.bedNr = bnr;
         this.paramI = pi;
         this.paramB = pb;
         this.geheZu = gz;
         this.lineNum = ln;
      }

      public void reset() {
         this.schluesselNr = 0;
         this.bedNr = 0;
         this.paramI = 0;
         this.paramB = false;
         this.geheZu = 0;
         this.lineNum = 0;
      }

      @Override
      public String toString() {
         String ergeb = null;
         return "Key "
            + this.schluesselNr
            + "; Bed "
            + this.bedNr
            + "; ParI "
            + this.paramI
            + "; ParB "
            + this.paramB
            + "; Gehe "
            + this.geheZu
            + " ;Zeile "
            + this.lineNum;
      }
   }

   class KarolProgBlock {
      byte typ;
      String bezeichner;
      int beginn;
      int ende;
      boolean result;

      public KarolProgBlock() {
         this.typ = 0;
         this.bezeichner = "";
         this.beginn = 0;
         this.ende = 0;
         this.result = false;
      }

      public KarolProgBlock(byte ty, String bz, int be, int en) {
         this.typ = ty;
         this.bezeichner = bz;
         this.beginn = be;
         this.ende = en;
         this.result = false;
      }
   }

   class KarolProgStack {
      int schluesselNr;
      int anwArrayIndex;

      public KarolProgStack() {
         this.schluesselNr = 0;
         this.anwArrayIndex = 0;
      }

      public KarolProgStack(int snr, int ind) {
         this.schluesselNr = snr;
         this.anwArrayIndex = ind;
      }

      @Override
      public String toString() {
         String ergeb = null;
         return "Key " + this.schluesselNr + "; anwArrayIndex " + this.anwArrayIndex;
      }
   }

   public static enum ProgStatusType {
      PStNothing,
      PStRunning,
      PStRunningFast,
      PStTracing,
      PStStop;
   }

   class ProgThread extends Thread {
      KarolProgram.ProgStatusType prgStat;

      public ProgThread() {
         super("KarolProgramm");
      }

      @Override
      public void run() {
         int startAnw = KarolProgram.this.blockArray.get(0).beginn;
         int endAnw = KarolProgram.this.blockArray.get(0).ende;
         KarolProgram.this.runBereich(startAnw, endAnw, 0);
         if (KarolProgram.this.isStopped) {
            KarolProgram.this.progStatus = KarolProgram.ProgStatusType.PStNothing;
            KarolProgram.this.view.ProgrammLampeSchalten(KarolProgram.ProgStatusType.PStNothing);
            KarolProgram.this.view.getGutterPanel().setMarkerInvisible(KEdtGutter.MarkerType.TRACE);
            KarolProgram.this.view.InfoBoxHinzufuegen(6, "Programmabbruch\n");
         } else {
            KarolProgram.this.view.ProgrammLampeSchalten(KarolProgram.ProgStatusType.PStNothing);
            KarolProgram.this.view.getGutterPanel().setAllMarkerInvisible();
            KarolProgram.this.view.InfoBoxLoeschen();
            KarolProgram.this.view.InfoBoxHinzufuegen(6, "Programmende\n");
            KarolProgram.this.progStatus = KarolProgram.ProgStatusType.PStNothing;
         }
      }
   }
}
