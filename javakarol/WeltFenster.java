package javakarol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class WeltFenster {
   private Welt welt;
   private JFrame fenster;
   private WeltAnzeige3D anzeigeWelt;
   private FehlerAnzeige fehlerText;

   WeltFenster(Welt welt) {
      this.welt = welt;
      this.fenster = new JFrame("JavaKarol");
      this.fenster.setLocation(50, 50);
      this.fenster.setResizable(true);
      this.fenster.setMinimumSize(new Dimension(200, 200));
      this.fenster.setPreferredSize(new Dimension(500, 500));
      this.fenster.setDefaultCloseOperation(3);
      JPanel contentPane = (JPanel)this.fenster.getContentPane();
      contentPane.setBorder(new EmptyBorder(6, 6, 6, 6));
      contentPane.setLayout(new BorderLayout(6, 6));
      this.anzeigeWelt = new WeltAnzeige3D(this.welt);
      JScrollPane scrollPane = new JScrollPane(this.anzeigeWelt);
      scrollPane.setBackground(Color.WHITE);
      scrollPane.getViewport().setBackground(Color.WHITE);
      scrollPane.setPreferredSize(new Dimension(200, 200));
      contentPane.add(scrollPane, "Center");
      this.fehlerText = new FehlerAnzeige();
      this.fehlerText.setBorder(new EtchedBorder());
      this.fehlerText.setBackground(new Color(224, 224, 224));
      this.fehlerText.setPreferredSize(new Dimension(200, 50));
      this.fehlerText.setLineWrap(true);
      this.fehlerText.setWrapStyleWord(true);
      contentPane.add(this.fehlerText, "South");
      this.fenster.pack();
      this.fenster.setVisible(true);
   }

   WeltAnzeige3D getWeltAnzeige() {
      return this.anzeigeWelt;
   }

   FehlerAnzeige getFehlerAnzeige() {
      return this.fehlerText;
   }

   void fensterNachVorne() {
      this.fenster.toFront();
   }
}
