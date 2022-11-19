package karoleditor;

import java.io.Serializable;
import java.util.Comparator;

public class KEdtToken implements Serializable, Comparable<KEdtToken>, Comparator<KEdtToken> {
   public final KEdtToken.TokenType type;
   public int start;
   public int length;
   public int docLineNr;

   public KEdtToken(KEdtToken.TokenType type, int start, int length) {
      this.type = type;
      this.start = start;
      this.length = length;
      this.docLineNr = 0;
   }

   public KEdtToken(KEdtToken.TokenType t, int st, int le, int lnr) {
      this.type = t;
      this.start = st;
      this.length = le;
      this.docLineNr = lnr;
   }

   public int getStart() {
      return this.start;
   }

   public void setStart(int s) {
      this.start = s;
   }

   public int getLength() {
      return this.length;
   }

   public KEdtToken.TokenType getTyp() {
      return this.type;
   }

   public int getEnd() {
      return this.start + this.length;
   }

   public String getStyle() {
      return KEdtStyleContext.styleName[this.type.ordinal()];
   }

   public void setDocLineNr(int ln) {
      this.docLineNr = ln;
   }

   public int getDocLineNr() {
      return this.docLineNr;
   }

   public boolean equals(KEdtToken t) {
      return this.start == t.start && this.length == t.length && this.type.equals(t.type);
   }

   @Override
   public int hashCode() {
      return this.start;
   }

   @Override
   public String toString() {
      return String.format("%s (%d, %d, %d)", this.type, this.start, this.length, this.docLineNr);
   }

   public int compareTo(KEdtToken t) {
      if (this.start != t.start) {
         return this.start - t.start;
      } else {
         return this.length != t.length ? this.length - t.length : this.type.compareTo(t.type);
      }
   }

   public int compare(KEdtToken t1, KEdtToken t2) {
      if (t1.start != t2.start) {
         return t1.start - t2.start;
      } else {
         return t1.length != t2.length ? t1.length - t2.length : t1.type.compareTo(t2.type);
      }
   }

   public static enum TokenType {
      COMMENT,
      KEY,
      KEYEND,
      LIB,
      LIBEND,
      KEYANW,
      KEYBED,
      NUMBER,
      PNUMBER,
      PCOLOR,
      PEMPTY,
      KAROL,
      FILENAME,
      IDENTIFIER,
      DEFAULT;
   }
}
