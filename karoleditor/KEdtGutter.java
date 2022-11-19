package karoleditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;

public class KEdtGutter extends JPanel implements DocumentListener {
   private JTextPane editorPane;
   private KEdtDocument doc;
   private int displayDigits = 3;
   private String numbersFormat = "%3d";
   private boolean mitZeilennummer = true;
   private int breiteMZ;
   private int breiteOZ;
   private int abstand = 4;
   private int abstandMZ = 1;
   private int breiteIcon = 11;
   private int fontDesc = 1;
   private int lastLineCount;
   private KEdtGutter.GutterMark[] gutterMarks = new KEdtGutter.GutterMark[5];
   int gutterMarksAnz = 5;
   int doubleMarkLine = -1;

   public KEdtGutter(JTextComponent editorPaneParam) {
      this.editorPane = (JTextPane)editorPaneParam;
      this.doc = (KEdtDocument)this.editorPane.getDocument();
      this.lastLineCount = this.doc.getLineCount();
      this.doc.addDocumentListener(this);
      this.setBorder(BorderFactory.createLineBorder(new Color(240, 240, 240)));
      this.setForeground(Color.BLACK);
      this.setBackground(new Color(240, 240, 240));
      this.setFontWidth();
      this.gutterMarks[0] = new KEdtGutter.GutterMark("Marker_Trace");
      this.gutterMarks[1] = new KEdtGutter.GutterMark("Marker_Alert");
      this.gutterMarks[2] = new KEdtGutter.GutterMark("Marker_Break");
      this.gutterMarks[3] = new KEdtGutter.GutterMark("Marker_BreakPoint");
      this.gutterMarks[4] = new KEdtGutter.GutterMark("Marker_Trace_BreakPoint");
   }

   public void setFontWidth() {
      Style s = this.doc.getStyle("normal");
      this.setFont(this.doc.getFont(s));
      FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
      this.fontDesc = fontMetrics.getDescent();
      int width = fontMetrics.charWidth('0') * this.displayDigits;
      this.breiteMZ = this.abstand + this.breiteIcon + this.abstandMZ + width + this.abstand;
      this.breiteOZ = this.abstand + this.breiteIcon + this.abstand;
      Dimension d = new Dimension();
      if (this.mitZeilennummer) {
         d.setSize(this.breiteMZ, 2146483647);
      } else {
         d.setSize(this.breiteOZ, 2146483647);
      }

      this.setPreferredSize(d);
      this.setSize(d);
   }

   public void setMarkerToLine(KEdtGutter.MarkerType mt, int line) {
      if (line >= 1 && line <= this.doc.getLineCount() && mt.ordinal() >= 0 && mt.ordinal() <= this.gutterMarksAnz) {
         this.gutterMarks[mt.ordinal()].setLineVisible(line);
         if (this.doubleMarkLine >= 1) {
            this.gutterMarks[KEdtGutter.MarkerType.BREAKPOINT.ordinal()].setVisible();
            this.gutterMarks[KEdtGutter.MarkerType.TRACE_BREAKPOINT.ordinal()].setInvisible();
            this.doubleMarkLine = -1;
         }

         if (mt == KEdtGutter.MarkerType.TRACE && line == this.gutterMarks[KEdtGutter.MarkerType.BREAKPOINT.ordinal()].getLine()) {
            this.gutterMarks[KEdtGutter.MarkerType.BREAKPOINT.ordinal()].setInvisible();
            this.gutterMarks[mt.ordinal()].setInvisible();
            this.gutterMarks[KEdtGutter.MarkerType.TRACE_BREAKPOINT.ordinal()].setLineVisible(line);
            this.doubleMarkLine = line;
         }

         this.repaint();
      }
   }

   public void setMarkerInvisible(KEdtGutter.MarkerType mt) {
      if (mt.ordinal() >= 0 && mt.ordinal() <= this.gutterMarksAnz) {
         this.gutterMarks[mt.ordinal()].setInvisible();
         this.gutterMarks[mt.ordinal()].setLine(0);
         this.repaint();
      }
   }

   public void setAllMarkerInvisible() {
      for(int m = 0; m < this.gutterMarksAnz && m != 3; ++m) {
         this.gutterMarks[m].setInvisible();
         this.gutterMarks[m].setLine(0);
      }

      this.repaint();
   }

   public int getBreakPointLine() {
      return this.gutterMarks[KEdtGutter.MarkerType.BREAKPOINT.ordinal()].getLine();
   }

   public void setBreakPointLine(int line) {
      this.gutterMarks[KEdtGutter.MarkerType.BREAKPOINT.ordinal()].setLineVisible(line);
      this.repaint();
   }

   public int getTraceMarkerLine() {
      return this.gutterMarks[KEdtGutter.MarkerType.TRACE.ordinal()].getLine();
   }

   public void setMitZeilennummer(boolean b) {
      this.mitZeilennummer = b;
      this.setFontWidth();
      this.repaint();
   }

   public boolean getMitZeilennummer() {
      return this.mitZeilennummer;
   }

   @Override
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Rectangle clipRect = g.getClipBounds();
      Rectangle r = new Rectangle();
      int yMax = 0;
      int anzLine = this.doc.getLineCount();
      int topLine = this.doc.getLineNumberAt(this.editorPane.viewToModel(new Point(0, clipRect.y)));
      int bottomLine = this.doc.getLineNumberAt(this.editorPane.viewToModel(new Point(0, clipRect.y + clipRect.height)));
      int lastLine = Math.min(bottomLine, anzLine);
      if (this.mitZeilennummer) {
         for(int line = topLine; line <= lastLine; ++line) {
            String lineNumber = String.format(this.numbersFormat, line);

            try {
               r = this.editorPane.modelToView(this.doc.getBeginOfLine(line));
            } catch (BadLocationException var13) {
            }

            int y = r.y + r.height - this.fontDesc;
            g.drawString(lineNumber, this.abstand + this.breiteIcon + this.abstandMZ, y);
            if (y > yMax) {
               yMax = y;
            }
         }

         if (lastLine < bottomLine && yMax > 0) {
            g.setColor(new Color(240, 240, 240));
            yMax += this.fontDesc;
            g.drawRect(0, yMax, this.breiteMZ, clipRect.height - yMax);
         }
      }

      for(int m = 0; m < this.gutterMarksAnz; ++m) {
         if (topLine <= this.gutterMarks[m].line && this.gutterMarks[m].line <= lastLine && this.gutterMarks[m].visible) {
            try {
               r = this.editorPane.modelToView(this.doc.getBeginOfLine(this.gutterMarks[m].line));
            } catch (BadLocationException var12) {
            }

            int y = r.y + r.height / 2 - 7;
            this.gutterMarks[m].imageIcon.paintIcon(this, g, this.abstand, y);
         }
      }
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      this.documentChanged();
   }

   @Override
   public void insertUpdate(DocumentEvent e) {
      this.documentChanged();
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      this.documentChanged();
   }

   private void documentChanged() {
      int anzLine = this.doc.getLineCount();
      if (this.lastLineCount != anzLine) {
         this.lastLineCount = anzLine;
         this.repaint();
      }
   }

   class GutterMark {
      int line = 0;
      boolean visible = false;
      ImageIcon imageIcon;

      GutterMark(String iconFileName) {
         this.imageIcon = new ImageIcon(KEdtGutter.class.getResource("/icons/" + iconFileName + ".gif"));
      }

      void setLine(int newLine) {
         this.line = newLine;
      }

      void setLineVisible(int newLine) {
         this.line = newLine;
         this.visible = true;
      }

      void setInvisible() {
         this.visible = false;
      }

      int getLine() {
         return this.line;
      }

      void setVisible() {
         this.visible = true;
      }
   }

   public static enum MarkerType {
      TRACE,
      ALERT,
      BREAK,
      BREAKPOINT,
      TRACE_BREAKPOINT;
   }
}
