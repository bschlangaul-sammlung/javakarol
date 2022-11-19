package karoleditor;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KEdtRegexLexer {
   Map<KEdtToken.TokenType, Pattern> patterns = new HashMap<>();
   public static final Comparator<KEdtToken> LONGEST_FIRST = new KEdtRegexLexer.LongestFirst();
   public static final Comparator<KEdtToken> SHORTEST_FIRST = new KEdtRegexLexer.ShortestFirst();
   public static final String karolKeys = "wiederhole|mal|immer|solange|bis|falsch|wahr|nicht|wenn|dann|sonst|langsam|schnell|warten|Bedingung|Anweisung|Methode|Programm";
   public static final String karolKeysEnd = "\\*wiederhole|endewiederhole|\\*wenn|endewenn|\\*Anweisung|endeAnweisung|\\*Methode|endeMethode|\\*Bedingung|endeBedingung|\\*Programm|endeProgramm";
   public static final String karolLib = "Einfügen";
   public static final String karolLibEnd = "\\*Einfügen|endeEinfügen";
   public static final String karolKeysAnw = "Beenden|Ton|Aufheben|Hinlegen|Schritt|LinksDrehen|RechtsDrehen|MarkeSetzen|MarkeLöschen";
   public static final String karolKeysBed = "IstLeer|IstWand|IstMarke|IstVoll|IstZiegel|IstNorden|IstOsten|IstSüden|IstWesten|HatZiegel|NichtIstLeer|NichtIstWand|NichtIstMarke|NichtIstVoll|NichtIstZiegel";
   public static final String karolColor = "rot|gelb|blau|grün|schwarz";
   public static final String karolObject = "karol\\.";

   public KEdtRegexLexer() {
      String pt = "\\{(.|[\\n\\r\\t\\f])*?\\}";
      this.patterns.put(KEdtToken.TokenType.COMMENT, Pattern.compile(pt));
      this.patterns
         .put(
            KEdtToken.TokenType.KEY,
            Pattern.compile("wiederhole|mal|immer|solange|bis|falsch|wahr|nicht|wenn|dann|sonst|langsam|schnell|warten|Bedingung|Anweisung|Methode|Programm", 2)
         );
      this.patterns
         .put(
            KEdtToken.TokenType.KEYEND,
            Pattern.compile(
               "\\*wiederhole|endewiederhole|\\*wenn|endewenn|\\*Anweisung|endeAnweisung|\\*Methode|endeMethode|\\*Bedingung|endeBedingung|\\*Programm|endeProgramm",
               2
            )
         );
      this.patterns.put(KEdtToken.TokenType.LIB, Pattern.compile("Einfügen", 2));
      this.patterns.put(KEdtToken.TokenType.LIBEND, Pattern.compile("\\*Einfügen|endeEinfügen", 2));
      this.patterns
         .put(KEdtToken.TokenType.KEYANW, Pattern.compile("Beenden|Ton|Aufheben|Hinlegen|Schritt|LinksDrehen|RechtsDrehen|MarkeSetzen|MarkeLöschen", 2));
      this.patterns
         .put(
            KEdtToken.TokenType.KEYBED,
            Pattern.compile(
               "IstLeer|IstWand|IstMarke|IstVoll|IstZiegel|IstNorden|IstOsten|IstSüden|IstWesten|HatZiegel|NichtIstLeer|NichtIstWand|NichtIstMarke|NichtIstVoll|NichtIstZiegel",
               2
            )
         );
      pt = "[0-9]+";
      this.patterns.put(KEdtToken.TokenType.NUMBER, Pattern.compile(pt));
      pt = "\\(([0-9]+)\\)";
      this.patterns.put(KEdtToken.TokenType.PNUMBER, Pattern.compile(pt));
      this.patterns.put(KEdtToken.TokenType.PCOLOR, Pattern.compile("\\((rot|gelb|blau|grün|schwarz)\\)", 2));
      pt = "\\(\\)";
      this.patterns.put(KEdtToken.TokenType.PEMPTY, Pattern.compile(pt));
      this.patterns.put(KEdtToken.TokenType.KAROL, Pattern.compile("karol\\.", 2));
      pt = "[a-zA-Z0-9_:/\\u002e\\u005c]+\\.kdp";
      this.patterns.put(KEdtToken.TokenType.FILENAME, Pattern.compile(pt, 2));
      pt = "[a-zA-Z\\u00e4\\u00f6\\u00fc\\u00c4\\u00d6\\u00dc\\u00df][a-zA-Z_0-9\\u00e4\\u00f6\\u00fc\\u00c4\\u00d6\\u00dc\\u00df]*";
      this.patterns.put(KEdtToken.TokenType.IDENTIFIER, Pattern.compile(pt));
   }

   public void parse(CharSequence segment, int ofst, List<KEdtToken> tokens) {
      TreeSet<KEdtToken> allMatches = new TreeSet<>(LONGEST_FIRST);

      for(Entry<KEdtToken.TokenType, Pattern> e : this.patterns.entrySet()) {
         Matcher m = e.getValue().matcher(segment);

         while(m.find()) {
            KEdtToken t = new KEdtToken(e.getKey(), m.start() + ofst, m.end() - m.start());
            allMatches.add(t);
         }
      }

      int end = -1;

      for(KEdtToken t : allMatches) {
         if (t.start >= end) {
            tokens.add(t);
            end = t.getEnd();
         }
      }
   }

   private static class LongestFirst implements Comparator<KEdtToken>, Serializable {
      private LongestFirst() {
      }

      public int compare(KEdtToken t1, KEdtToken t2) {
         if (t1.start != t2.start) {
            return t1.start - t2.start;
         } else {
            return t1.length != t2.length ? t2.length - t1.length : t1.type.compareTo(t2.type);
         }
      }
   }

   private static class ShortestFirst implements Comparator<KEdtToken>, Serializable {
      private ShortestFirst() {
      }

      public int compare(KEdtToken t1, KEdtToken t2) {
         if (t1.start != t2.start) {
            return t1.start - t2.start;
         } else {
            return t1.length != t2.length ? t1.length - t2.length : t1.type.compareTo(t2.type);
         }
      }
   }
}
