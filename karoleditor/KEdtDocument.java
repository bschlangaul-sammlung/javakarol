package karoleditor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class KEdtDocument extends DefaultStyledDocument {
   private Pattern searchPattern = null;
   private KEdtRegexLexer lexer = new KEdtRegexLexer();
   private List<KEdtToken> tokens;
   private boolean mitParsen = true;
   private boolean mitSchreibweise = true;
   private boolean istGeaendert = false;

   public KEdtDocument() {
      super(new KEdtStyleContext());
   }

   @Override
   protected void fireInsertUpdate(DocumentEvent e) {
      super.fireInsertUpdate(e);
      this.istGeaendert = true;
      if (this.mitParsen) {
         this.parse();
         this.highLight();
      }
   }

   @Override
   protected void fireRemoveUpdate(DocumentEvent e) {
      super.fireRemoveUpdate(e);
      this.istGeaendert = true;
      if (this.mitParsen) {
         this.parse();
         this.highLight();
      }
   }

   public void setMitParsen(boolean status) {
      this.mitParsen = status;
   }

   public void setMitSchreibweise(boolean status) {
      this.mitSchreibweise = status;
   }

   public boolean getMitSchreibweise() {
      return this.mitSchreibweise;
   }

   public void resetModified() {
      this.istGeaendert = false;
   }

   public boolean getModified() {
      return this.istGeaendert;
   }

   public void setStyleColor(KEdtToken.TokenType typ, int hexColor) {
      Style s = this.getStyle(KEdtStyleContext.styleName[typ.ordinal()]);
      StyleConstants.setForeground(s, new Color(hexColor));
   }

   public int getStyleColor(KEdtToken.TokenType typ) {
      Style s = this.getStyle(KEdtStyleContext.styleName[typ.ordinal()]);
      return StyleConstants.getForeground(s).getRGB();
   }

   public void setFontsizeNormal(int pt) {
      Style s = this.getStyle("normal");
      StyleConstants.setFontSize(s, pt);
      Style def = StyleContext.getDefaultStyleContext().getStyle("default");
      StyleConstants.setFontSize(def, pt);
   }

   public int getFontsizeNormal() {
      Style s = this.getStyle("normal");
      return StyleConstants.getFontSize(s);
   }

   private void parse() {
      int len = this.getLength();
      if (this.lexer == null) {
         this.tokens = null;
      } else {
         List<KEdtToken> toks = new ArrayList<>(len / 5);

         try {
            Segment seg = new Segment();
            this.getText(0, len, seg);
            this.lexer.parse(seg, 0, toks);
         } catch (BadLocationException var4) {
         }

         this.tokens = toks;
      }
   }

   private void highLight() {
      int z = 0;
      int maxToken = 700;
      int klammer = 0;
      if (this.lexer != null && this.tokens != null && !this.tokens.isEmpty()) {
         this.setCharacterAttributes(0, this.getLength(), this.getStyle("leer"), true);

         for(ListIterator<KEdtToken> ti = this.tokens.listIterator(); ti.hasNext() && z < maxToken; ++z) {
            KEdtToken t = ti.next();
            byte var6;
            if (t.getTyp() != KEdtToken.TokenType.PNUMBER && t.getTyp() != KEdtToken.TokenType.PCOLOR) {
               var6 = 0;
            } else {
               var6 = 1;
            }

            this.setCharacterAttributes(t.getStart() + var6, t.getLength() - 2 * var6, this.getStyle(t.getStyle()), false);
         }
      }
   }

   public void replaceToken(KEdtToken token, String replacement) {
      try {
         this.replace(token.start, token.length, replacement, null);
      } catch (BadLocationException var4) {
      }
   }

   public void showAllTokens() {
      if (this.lexer != null && this.tokens != null && !this.tokens.isEmpty()) {
         for(KEdtToken t : this.tokens) {
            JOptionPane.showMessageDialog(null, t.toString());
         }
      }
   }

   public KEdtToken getTokenAt(int pos) {
      if (this.tokens != null && !this.tokens.isEmpty() && pos <= this.getLength()) {
         KEdtToken tok = null;
         int ndx = Collections.binarySearch(this.tokens, new KEdtToken(KEdtToken.TokenType.DEFAULT, pos, 1));
         if (ndx < 0) {
            ndx = -ndx - 1 - 1 < 0 ? 0 : -ndx - 1 - 1;
            KEdtToken t = this.tokens.get(ndx);
            if (t.start <= pos && pos <= t.getEnd()) {
               tok = t;
            }
         } else {
            tok = this.tokens.get(ndx);
         }

         return tok;
      } else {
         return null;
      }
   }

   public String getString(KEdtToken token) {
      String result = "";
      if (token != null) {
         try {
            result = this.getText(token.start, token.length);
         } catch (BadLocationException var4) {
            return result;
         }
      }

      return result;
   }

   public String getStringLower(KEdtToken token) {
      return this.getString(token).toLowerCase();
   }

   public KEdtToken getNextToken(KEdtToken tok) {
      int n = this.tokens.indexOf(tok);
      return n >= 0 && n < this.tokens.size() - 1 ? this.tokens.get(n + 1) : null;
   }

   public KEdtToken getPrevToken(KEdtToken tok) {
      int n = this.tokens.indexOf(tok);
      return n > 0 && !this.tokens.isEmpty() ? this.tokens.get(n - 1) : null;
   }

   public List<KEdtToken> getTokenList() {
      return this.tokens;
   }

   public void getNewTokenListMitLine(List<KEdtToken> tok) {
      tok.clear();
      if (this.tokens != null && !this.tokens.isEmpty()) {
         for(KEdtToken t : this.tokens) {
            tok.add(new KEdtToken(t.type, t.start, t.length, this.getLineNumberToken(t)));
         }
      }
   }

   public Pattern getPattern() {
      return this.searchPattern;
   }

   private Pattern createPattern(String suchStr, boolean regExp, boolean grossKlein, boolean nurGanzeWoerter) {
      int flag = 0;
      if (suchStr != null && suchStr.length() > 0) {
         if (regExp) {
            flag |= 16;
         }

         if (!grossKlein) {
            flag |= 2;
         }

         if (nurGanzeWoerter) {
            suchStr = "[\\b\\s\\.,;!?]" + suchStr + "[\\b\\s\\.,;!?]";
         }

         return Pattern.compile(suchStr, flag);
      } else {
         return null;
      }
   }

   private Matcher createMatcher(Pattern pattern, int start, int laenge) {
      Matcher matcher = null;
      if (pattern == null) {
         return null;
      } else if (this.getLength() == 0) {
         return null;
      } else if (start >= this.getLength()) {
         return null;
      } else {
         if (start < 0) {
            start = 0;
         }

         if (start + laenge > this.getLength()) {
            laenge = this.getLength() - start;
         }

         try {
            Segment seg = new Segment();
            this.getText(start, laenge, seg);
            matcher = pattern.matcher(seg);
         } catch (BadLocationException var6) {
            matcher = null;
         }

         return matcher;
      }
   }

   private void doReplace(String suchStr, String ersetzStr, int start, int laenge, boolean grossKlein, boolean nurGanzeWoerter) {
      if (ersetzStr == null) {
         ersetzStr = "";
      }

      this.searchPattern = this.createPattern(suchStr, true, grossKlein, nurGanzeWoerter);
      if (this.searchPattern != null) {
         Matcher matcher = this.createMatcher(this.searchPattern, start, laenge);
         String newText = matcher.replaceAll(ersetzStr);

         try {
            this.replace(start, laenge, newText, null);
         } catch (BadLocationException var10) {
         }
      }
   }

   public void doReplaceAll(String suchStr, String ersetzStr, boolean grossKlein, boolean nurGanzeWoerter) {
      this.doReplace(suchStr, ersetzStr, 0, this.getLength(), grossKlein, nurGanzeWoerter);
   }

   public void doReplaceFromCursor(String suchStr, String ersetzStr, boolean grossKlein, boolean nurGanzeWoerter, JEditorPane editorPane) {
      int start = editorPane.getCaretPosition();
      this.doReplace(suchStr, ersetzStr, start, this.getLength() - start, grossKlein, nurGanzeWoerter);
   }

   public void doReplaceSelected(String suchStr, String ersetzStr, boolean grossKlein, boolean nurGanzeWoerter, JEditorPane editorPane) {
      int start = editorPane.getSelectionStart();
      int laenge = editorPane.getSelectionEnd() - start;
      if (laenge > 0 && start > 0) {
         this.doReplace(suchStr, ersetzStr, start, laenge, grossKlein, nurGanzeWoerter);
      }
   }

   private boolean doFindRange(Pattern pattern, int start, int laenge, JEditorPane editorPane) {
      Matcher matcher = this.createMatcher(pattern, start, laenge);
      if (matcher != null && matcher.find()) {
         editorPane.select(matcher.start() + start, matcher.end() + start);
         return true;
      } else {
         return false;
      }
   }

   private boolean doFindRangePrev(Pattern pattern, int start, int laenge, JEditorPane editorPane) {
      int dot = start + laenge;
      if (dot >= start + this.getLength()) {
         dot = start + this.getLength() - 1;
      }

      Matcher matcher = this.createMatcher(pattern, start, laenge);
      if (matcher == null) {
         return false;
      } else {
         int lastBegin = -1;

         int lastEnd;
         for(lastEnd = -1; matcher.find() && matcher.end() < dot; lastEnd = matcher.end()) {
            lastBegin = matcher.start();
         }

         if (lastEnd > 0) {
            editorPane.select(lastBegin + start, lastEnd + start);
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean doFindNext(JEditorPane editorPane) {
      if (this.searchPattern == null) {
         return false;
      } else {
         int start = editorPane.getSelectionEnd();
         if (editorPane.getSelectionEnd() == editorPane.getSelectionStart()) {
            ++start;
         }

         if (start >= this.getLength()) {
            start = this.getLength();
         }

         if (this.doFindRange(this.searchPattern, start, this.getLength() - start, editorPane)) {
            return true;
         } else {
            return this.doFindRange(this.searchPattern, 0, this.getLength(), editorPane);
         }
      }
   }

   public boolean doFindAll(String suchStr, boolean grossKlein, boolean nurGanzeWoerter, boolean vorwaerts, JEditorPane editorPane) {
      if (suchStr != null && !suchStr.isEmpty()) {
         this.searchPattern = this.createPattern(suchStr, true, grossKlein, nurGanzeWoerter);
         if (this.searchPattern == null) {
            return false;
         } else {
            return vorwaerts
               ? this.doFindRange(this.searchPattern, 0, this.getLength(), editorPane)
               : this.doFindRangePrev(this.searchPattern, 0, this.getLength(), editorPane);
         }
      } else {
         return false;
      }
   }

   public boolean doFindFromCursor(String suchStr, boolean grossKlein, boolean nurGanzeWoerter, boolean vorwaerts, JEditorPane editorPane) {
      if (suchStr != null && !suchStr.isEmpty()) {
         this.searchPattern = this.createPattern(suchStr, true, grossKlein, nurGanzeWoerter);
         if (this.searchPattern == null) {
            return false;
         } else {
            int start = editorPane.getCaretPosition();
            return vorwaerts
               ? this.doFindRange(this.searchPattern, start, this.getLength() - start, editorPane)
               : this.doFindRangePrev(this.searchPattern, start, this.getLength() - start, editorPane);
         }
      } else {
         return false;
      }
   }

   public boolean doFindSelected(String suchStr, boolean grossKlein, boolean nurGanzeWoerter, boolean vorwaerts, JEditorPane editorPane) {
      if (suchStr != null && !suchStr.isEmpty()) {
         this.searchPattern = this.createPattern(suchStr, true, grossKlein, nurGanzeWoerter);
         if (this.searchPattern == null) {
            return false;
         } else {
            int start = editorPane.getSelectionStart();
            int laenge = editorPane.getSelectionEnd() - start;
            return vorwaerts
               ? this.doFindRange(this.searchPattern, start, laenge, editorPane)
               : this.doFindRangePrev(this.searchPattern, start, laenge, editorPane);
         }
      } else {
         return false;
      }
   }

   public int getLineCount() {
      Element e = this.getDefaultRootElement();
      return e.getElementCount();
   }

   public int getLineNumberAt(int pos) {
      int lineNr = this.getDefaultRootElement().getElementIndex(pos);
      return lineNr + 1;
   }

   public int getLineNumberToken(KEdtToken t) {
      return this.getLineNumberAt(t.getStart());
   }

   public int getLineNumberCaret(JEditorPane editorPane) {
      return this.getLineNumberAt(editorPane.getCaretPosition());
   }

   public int getBeginOfLine(int lineNr) {
      return this.getDefaultRootElement().getElement(lineNr - 1).getStartOffset();
   }

   public int getEndOfLine(int lineNr) {
      return this.getDefaultRootElement().getElement(lineNr - 1).getEndOffset();
   }

   public String getLineStr(int lineNr) {
      String result = "";
      int start = this.getBeginOfLine(lineNr);

      try {
         return this.getText(start, this.getEndOfLine(lineNr) - start);
      } catch (BadLocationException var5) {
         return result;
      }
   }

   public int getBeginOfLinePos(int aktPos) {
      int lineNr = this.getLineNumberAt(aktPos);
      return this.getDefaultRootElement().getElement(lineNr - 1).getStartOffset();
   }

   public void formatieren(JEditorPane editorPane, int tabLen) {
      this.setMitParsen(false);
      int anz = this.getLineCount();
      String tabStr = null;
      String zeileStr = null;
      String testStr = null;
      String keyStr = null;
      int level = 0;

      for(int z = 1; z <= anz; ++z) {
         int startLine = this.getBeginOfLine(z);
         int endLine = this.getEndOfLine(z) - 1;
         int lenLine = endLine - startLine;
         if (lenLine > 0) {
            try {
               zeileStr = this.getText(startLine, lenLine).toLowerCase();
            } catch (BadLocationException var24) {
            }

            int p1 = 0;

            while(p1 < lenLine && zeileStr.charAt(p1) == ' ') {
               ++p1;
            }

            int p2 = p1 + 1;

            while(p2 < lenLine && zeileStr.charAt(p2) != ' ') {
               ++p2;
            }

            if (p1 == lenLine) {
               keyStr = "";
            } else {
               keyStr = zeileStr.substring(p1, p2);
            }

            testStr = "|" + keyStr + "|";
            if ("|*wenn|endewenn|*wiederhole|endewiederhole|sonst|*anweisung|endeanweisung|*methode|endemethode|*bedingung|endebedingung|*programm|endeprogramm|*einfügen|endeeinfügen|"
               .contains(testStr)) {
               --level;
            }

            tabStr = "                                     ".substring(0, level * tabLen);

            try {
               this.remove(startLine, p1);
               this.insertString(startLine, tabStr, null);
            } catch (BadLocationException var25) {
               break;
            }

            if ("|wenn|wiederhole|sonst|anweisung|methode|bedingung|programm|einfügen|".contains(testStr)) {
               ++level;
            }

            if (zeileStr.contains("*" + keyStr) || zeileStr.contains("ende" + keyStr)) {
               --level;
            }
         }
      }

      if (this.mitSchreibweise) {
         this.parse();
         this.highLight();
         String suchStr = null;
         String suchStrLower = null;
         String ersatzStr = null;
         String karolAllKeys = "wiederhole|mal|immer|solange|bis|falsch|wahr|nicht|wenn|dann|sonst|langsam|schnell|warten|Bedingung|Anweisung|Methode|Programm|\\*wiederhole|endewiederhole|\\*wenn|endewenn|\\*Anweisung|endeAnweisung|\\*Methode|endeMethode|\\*Bedingung|endeBedingung|\\*Programm|endeProgramm|Einfügen|\\*Einfügen|endeEinfügen|Beenden|Ton|Aufheben|Hinlegen|Schritt|LinksDrehen|RechtsDrehen|MarkeSetzen|MarkeLöschen|IstLeer|IstWand|IstMarke|IstVoll|IstZiegel|IstNorden|IstOsten|IstSüden|IstWesten|HatZiegel|NichtIstLeer|NichtIstWand|NichtIstMarke|NichtIstVoll|NichtIstZiegel";
         String karolAllKeysLower = karolAllKeys.toLowerCase();
         int pos = 0;
         int suchLen = 0;

         for(KEdtToken t : this.tokens) {
            KEdtToken.TokenType tt = t.getTyp();
            if (tt == KEdtToken.TokenType.KEY || tt == KEdtToken.TokenType.KEYEND || tt == KEdtToken.TokenType.KEYANW || tt == KEdtToken.TokenType.KEYBED) {
               suchStr = this.getString(t);
               suchStrLower = suchStr.toLowerCase();
               suchLen = suchStr.length();
               pos = karolAllKeysLower.indexOf(suchStrLower);
               if (pos >= 0) {
                  ersatzStr = karolAllKeys.substring(pos, pos + suchLen);
                  this.replaceToken(t, ersatzStr);
               }
            }
         }

         this.parse();
         this.highLight();
      }

      this.setMitParsen(true);
   }
}
