package karoleditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class KEdtPopUp extends JPopupMenu implements ActionListener {
   private JEditorPane editorPane;
   private int tabLen;
   private final String[] itemWdh = new String[]{"wiederhole immer", "wiederhole mal", "wiederhole solange", "*wiederhole bis"};
   private final String[] itemWenn = new String[]{"wenn dann", "wenn dann sonst"};
   private final String[] itemUsw1 = new String[]{"Anweisung", "Methode", "Bedingung", "Einfügen", "wahr", "falsch", "nicht"};
   private final String[] itemMeth = new String[]{"Schritt", "LinksDrehen", "RechtsDrehen", "Hinlegen", "Aufheben", "MarkeSetzen", "MarkeLöschen"};
   private final String[] itemUsw2 = new String[]{"Warten", "Ton", "Langsam", "Schnell", "Beenden"};
   private final String[] itemBed = new String[]{"IstWand", "NichtIstWand", "IstZiegel", "NichtIstZiegel", "IstMarke", "NichtIstMarke"};
   private final String[] itemUsw3 = new String[]{
      "IstNorden", "IstOsten", "IstSüden", "IstWesten", "IstVoll", "NichtIstVoll", "IstLeer", "NichtIstLeer", "HatZiegel"
   };

   public KEdtPopUp(JEditorPane edtPane, int tabLen) {
      this.editorPane = edtPane;
      this.tabLen = tabLen;
      JMenu mnWiederholung = new JMenu("Wiederholungen");
      this.add(mnWiederholung);

      for(int i = 0; i < this.itemWdh.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemWdh[i]);
         mnWiederholung.add(eintrag);
         eintrag.addActionListener(this);
      }

      for(int i = 0; i < this.itemWenn.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemWenn[i]);
         this.add(eintrag);
         eintrag.addActionListener(this);
      }

      JMenu mnUsw1 = new JMenu("usw.");
      this.add(mnUsw1);

      for(int i = 0; i < this.itemUsw1.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemUsw1[i]);
         mnUsw1.add(eintrag);
         eintrag.addActionListener(this);
      }

      this.addSeparator();

      for(int i = 0; i < this.itemMeth.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemMeth[i]);
         this.add(eintrag);
         eintrag.addActionListener(this);
      }

      JMenu mnUsw2 = new JMenu("usw.");
      this.add(mnUsw2);

      for(int i = 0; i < this.itemUsw2.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemUsw2[i]);
         mnUsw2.add(eintrag);
         eintrag.addActionListener(this);
      }

      this.addSeparator();

      for(int i = 0; i < this.itemBed.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemBed[i]);
         this.add(eintrag);
         eintrag.addActionListener(this);
      }

      JMenu mnUsw3 = new JMenu("usw.");
      this.add(mnUsw3);

      for(int i = 0; i < this.itemUsw3.length; ++i) {
         JMenuItem eintrag = new JMenuItem(this.itemUsw3[i]);
         mnUsw3.add(eintrag);
         eintrag.addActionListener(this);
      }
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      KEdtDocument editorDoc = (KEdtDocument)this.editorPane.getDocument();
      String wer = new String(ae.getActionCommand().replaceAll(" ", ""));
      String str1 = "";
      String str2 = "";
      String str12 = "";
      String ausgabe = "";
      Boolean ohneNewLine = false;
      if (wer.equals("wiederholeimmer")) {
         str1 = "wiederhole immer";
         str2 = "endewiederhole";
      }

      if (wer.equals("wiederholemal")) {
         str1 = "wiederhole   mal";
         str2 = "endewiederhole";
      }

      if (wer.equals("wiederholesolange")) {
         str1 = "wiederhole solange ";
         str2 = "endewiederhole";
      }

      if (wer.equals("*wiederholebis")) {
         str1 = "wiederhole";
         str2 = "endewiederhole bis ";
      }

      if (wer.equals("wenndann")) {
         str1 = "wenn  dann";
         str2 = "endewenn";
      }

      if (wer.equals("wenndannsonst")) {
         str1 = "wenn  dann";
         str12 = "sonst";
         str2 = "endewenn";
      }

      if (wer.equals("Anweisung") || wer.equals("Methode") || wer.equals("Bedingung") || wer.equals("Einfügen")) {
         str1 = wer;
         str2 = "ende" + wer;
      }

      if (wer.equals("wahr") || wer.equals("falsch") || wer.equals("nicht")) {
         str1 = wer;
      }

      for(int i = 0; i < this.itemMeth.length; ++i) {
         if (wer.equals(this.itemMeth[i])) {
            str1 = wer;
         }
      }

      for(int i = 0; i < this.itemUsw2.length; ++i) {
         if (wer.equals(this.itemUsw2[i])) {
            str1 = wer;
         }
      }

      for(int i = 0; i < this.itemBed.length; ++i) {
         if (wer.equals(this.itemBed[i])) {
            str1 = wer;
            ohneNewLine = true;
         }
      }

      for(int i = 0; i < this.itemUsw3.length; ++i) {
         if (wer.equals(this.itemUsw3[i])) {
            str1 = wer;
            ohneNewLine = true;
         }
      }

      if (!str1.isEmpty()) {
         int aktPos = this.editorPane.getCaretPosition();
         int aktLineBegin = editorDoc.getBeginOfLinePos(aktPos);
         String einrueckStr = "                                                                     ".substring(0, aktPos - aktLineBegin);
         String tabStr = "               ".substring(0, this.tabLen);
         if (str2.isEmpty()) {
            if (ohneNewLine) {
               ausgabe = str1;
            } else {
               ausgabe = str1 + "\n";
            }
         } else if (str12.isEmpty()) {
            ausgabe = str1 + "\n" + einrueckStr + tabStr + "\n" + einrueckStr + str2 + "\n";
         } else {
            ausgabe = str1 + "\n" + einrueckStr + tabStr + "\n" + einrueckStr + str12 + "\n" + einrueckStr + tabStr + "\n" + einrueckStr + str2 + "\n";
         }

         this.editorPane.replaceSelection(ausgabe);
      }
   }
}
