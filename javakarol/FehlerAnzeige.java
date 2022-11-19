package javakarol;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class FehlerAnzeige extends JTextArea {
   public FehlerAnzeige() {
      this.setBackground(new Color(224, 224, 224));
      this.setEnabled(false);
      this.setDisabledTextColor(Color.BLACK);
      this.setFont(new Font("Arial", 0, 12));
      this.setText("");
   }

   public void setLineText(int zeile, String s) {
      int zeilenAnz = this.getLineCount();
      if (zeile > zeilenAnz) {
         for(int i = 1; i < zeile - zeilenAnz; ++i) {
            this.append("\n");
         }

         this.append(s);
      } else {
         try {
            this.replaceRange(s, this.getLineStartOffset(zeile - 1), this.getLineEndOffset(zeile - 1));
         } catch (BadLocationException var5) {
         }
      }
   }

   public String getLineText(int zeile) {
      String ergeb = "";
      int zeilenAnz = this.getLineCount();
      if (zeile > zeilenAnz) {
         ergeb = "";
      } else {
         try {
            int zeileStart = this.getLineStartOffset(zeile - 1);
            ergeb = this.getText(zeileStart, this.getLineEndOffset(zeile - 1) - zeileStart);
         } catch (BadLocationException var6) {
         }
      }

      return ergeb;
   }
}
