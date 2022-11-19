package karoleditor;

import java.awt.Color;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class KEdtStyleContext extends StyleContext {
   public static String[] styleName = new String[]{
      "comment", "key", "keyend", "lib", "libend", "keyanw", "keybed", "number", "pnumber", "pcolor", "pempty", "karol", "filename", "indentifier", "normal"
   };
   public static int[] styleColor = new int[]{7171437, 0, 0, 16728319, 16728319, 255, 255, 16711680, 12582912, 12582912, 0, 16747520, 16728319, 32768, 0};

   public KEdtStyleContext() {
      Style def = getDefaultStyleContext().getStyle("default");
      this.addStyle("leer", def);
      Style normal = this.addStyle(styleName[14], def);
      StyleConstants.setFontFamily(normal, KEdtEditorKit.DEFAULT_FONT.getFamily());
      StyleConstants.setFontSize(normal, KEdtEditorKit.DEFAULT_FONT.getSize());
      StyleConstants.setForeground(normal, new Color(styleColor[11]));
      StyleConstants.setBold(normal, false);
      StyleConstants.setItalic(normal, false);
      Style s = this.addStyle(styleName[0], normal);
      StyleConstants.setForeground(s, new Color(styleColor[0]));
      StyleConstants.setItalic(s, true);

      for(int i = 1; i <= 4; ++i) {
         s = this.addStyle(styleName[i], normal);
         StyleConstants.setForeground(s, new Color(styleColor[i]));
         StyleConstants.setBold(s, true);
      }

      for(int i = 5; i <= 13; ++i) {
         s = this.addStyle(styleName[i], normal);
         StyleConstants.setForeground(s, new Color(styleColor[i]));
      }
   }

   public static int getStandardStyleColor(KEdtToken.TokenType typ) {
      return styleColor[typ.ordinal()];
   }
}
