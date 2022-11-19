package karoleditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

public class DlgComplete extends JDialog {
   private final String[] itemsArray = new String[]{
      "wiederhole mal",
      "wiederhole immer",
      "wiederhole solange",
      "wenn dann",
      "Anweisung",
      "Methode",
      "Bedingung",
      "Schritt",
      "LinksDrehen",
      "RechtsDrehen",
      "Hinlegen",
      "Aufheben",
      "MarkeSetzen",
      "MarkeLöschen",
      "Warten",
      "Ton",
      "Langsam",
      "Schnell",
      "IstWand",
      "NichtIstWand",
      "IstZiegel",
      "NichtIstZiegel",
      "IstMarke",
      "NichtIstMarke",
      "IstNorden",
      "IstSüden",
      "IstWesten",
      "IstVoll",
      "NichtIstVoll",
      "IstLeer",
      "NichtIstLeer"
   };
   private JTextComponent target;
   private JList<String> lstItems;
   private String aktEingabe = "";
   private int startPos;
   private int endPos;

   public DlgComplete(Frame owner, JTextComponent targetComp) {
      super(owner, ModalityType.APPLICATION_MODAL);
      this.target = targetComp;
      this.setPreferredSize(new Dimension(190, 100));
      this.GUIAnlegen();
      this.pack();
   }

   public void showDialog(String eingabe) {
      try {
         Window window = SwingUtilities.getWindowAncestor(this.target);
         Rectangle rt = this.target.modelToView(this.target.getSelectionStart());
         int fh = this.target.getFontMetrics(this.target.getFont()).getHeight();
         Point loc = new Point(rt.x, rt.y + fh);
         this.setLocationRelativeTo(window);
         loc = SwingUtilities.convertPoint(this.target, loc, window);
         SwingUtilities.convertPointToScreen(loc, window);
         this.setLocation(loc);
      } catch (BadLocationException var6) {
      }

      this.aktEingabe = eingabe;
      this.filterList(this.aktEingabe);
      this.startPos = this.target.getSelectionStart() - eingabe.length();
      if (this.startPos < 0) {
         this.startPos = 0;
      }

      this.setVisible(true);
      this.lstItems.requestFocus();
   }

   private void GUIAnlegen() {
      this.setResizable(false);
      this.setModal(false);
      this.setDefaultCloseOperation(2);
      this.setUndecorated(true);
      this.getContentPane().setLayout(new BorderLayout(0, 0));
      this.lstItems = new JList<>();
      this.lstItems.setSelectionMode(0);
      this.lstItems.setBackground(new Color(255, 255, 224));
      this.lstItems.setFont(this.target.getFont());
      this.lstItems.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent me) {
            DlgComplete.this.lstItemsMouseClicked(me);
         }
      });
      this.lstItems.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent ke) {
            DlgComplete.this.lstItemsKeyPressed(ke);
         }
      });
      this.lstItems.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent fe) {
         }

         @Override
         public void focusLost(FocusEvent fe) {
            DlgComplete.this.setVisible(false);
         }
      });
      JScrollPane scrPane = new JScrollPane();
      scrPane.setViewportView(this.lstItems);
      this.getContentPane().add(scrPane);
   }

   private void lstItemsKeyPressed(KeyEvent ke) {
      int sel = this.lstItems.getSelectedIndex();
      switch(ke.getKeyCode()) {
         case 10:
            String result = this.lstItems.getSelectedValue().toString();
            this.endPos = this.target.getCaretPosition();
            this.target.setSelectionStart(this.startPos);
            this.target.setSelectionEnd(this.endPos);
            this.target.replaceSelection("");
            this.target.replaceSelection(result);
            this.setVisible(false);
            break;
         case 27:
            this.setVisible(false);
            return;
         case 38:
            if (sel > 0) {
               --sel;
            }

            this.lstItems.setSelectedIndex(sel);
            this.lstItems.ensureIndexIsVisible(sel);
            break;
         case 40:
            if (sel < this.lstItems.getModel().getSize() - 1) {
               ++sel;
            }

            this.lstItems.setSelectedIndex(sel);
            this.lstItems.ensureIndexIsVisible(sel);
            break;
         default:
            Character pressed = new Character(ke.getKeyChar());
            this.aktEingabe = this.aktEingabe + pressed.toString();
            this.filterList(this.aktEingabe);
            this.target.replaceSelection(pressed.toString());
      }
   }

   private void lstItemsMouseClicked(MouseEvent me) {
      if (me.getClickCount() == 2) {
         String result = this.lstItems.getSelectedValue().toString();
         this.endPos = this.target.getCaretPosition();
         this.target.setSelectionStart(this.startPos);
         this.target.setSelectionEnd(this.endPos);
         this.target.replaceSelection("");
         this.target.replaceSelection(result);
         this.setVisible(false);
      }
   }

   public void filterList(String eingabe) {
      Vector<String> filter = new Vector<>();
      Object selected = this.lstItems.getSelectedValue();
      eingabe = eingabe.toLowerCase();
      if (eingabe.equals(" ")) {
         eingabe = "";
      }

      for(int i = 0; i < this.itemsArray.length; ++i) {
         if (this.itemsArray[i].toLowerCase().startsWith(eingabe)) {
            filter.add(this.itemsArray[i]);
         }
      }

      this.lstItems.setListData(filter);
      if (selected != null && filter.contains(selected)) {
         this.lstItems.setSelectedValue(selected, true);
      } else {
         this.lstItems.setSelectedIndex(0);
      }
   }
}
