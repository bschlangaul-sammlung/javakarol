package robotkarol;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class DlgTipps extends JDialog implements ActionListener {
   private KarolView view;
   private final JTextArea txtTippAnzeige = new JTextArea();
   private int tippNummer = 0;

   public DlgTipps(KarolView owner) {
      super((Frame)owner);
      this.view = owner;
      this.setResizable(false);
      this.setPreferredSize(new Dimension(450, 236));
      this.setTitle("Tipps und Tricks zu RobotKarol");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgNeueWelt.class.getResource("/icons/Karol.gif")));
      this.setModal(true);
      this.setDefaultCloseOperation(0);
      this.GUIAnlegen();
      this.pack();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public void showModal() {
      ++this.tippNummer;
      if (!this.getTippFromFile(this.tippNummer)) {
         this.tippNummer = 0;
      }

      this.setVisible(true);
   }

   private void GUIAnlegen() {
      this.getContentPane().setLayout(new BorderLayout());
      JPanel contentPanel = new JPanel();
      contentPanel.setBorder(null);
      contentPanel.setLayout(new BoxLayout(contentPanel, 1));
      this.getContentPane().add(contentPanel, "Center");
      JLabel lblLampe = new JLabel(new ImageIcon(DlgTipps.class.getResource("/icons/tipps_lampe.gif")));
      this.getContentPane().add(lblLampe, "West");
      JTextArea txtWeisstDu = new JTextArea("Weißt du schon?");
      txtWeisstDu.setPreferredSize(new Dimension(124, 40));
      txtWeisstDu.setBackground(UIManager.getColor("Button.background"));
      txtWeisstDu.setBorder(new EmptyBorder(10, 50, 0, 0));
      txtWeisstDu.setFont(new Font("Tahoma", 1, 18));
      txtWeisstDu.setEditable(false);
      contentPanel.add(txtWeisstDu);
      this.txtTippAnzeige.setPreferredSize(new Dimension(124, 134));
      this.txtTippAnzeige.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.txtTippAnzeige.setFont(new Font("Tahoma", 0, 13));
      this.txtTippAnzeige.setEditable(false);
      this.txtTippAnzeige.setLineWrap(true);
      this.txtTippAnzeige.setWrapStyleWord(true);
      contentPanel.add(this.txtTippAnzeige);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setPreferredSize(new Dimension(100, 32));
      buttonPanel.setEnabled(false);
      buttonPanel.setBorder(null);
      buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
      this.getContentPane().add(buttonPanel, "South");
      Component horizontalGlue = Box.createHorizontalGlue();
      horizontalGlue.setPreferredSize(new Dimension(100, 0));
      horizontalGlue.setFocusable(false);
      horizontalGlue.setEnabled(false);
      buttonPanel.add(horizontalGlue);
      JButton btnTipp = new JButton("nächster Tipp");
      btnTipp.setFont(new Font("Tahoma", 0, 11));
      btnTipp.setPreferredSize(new Dimension(85, 23));
      btnTipp.setMinimumSize(new Dimension(85, 23));
      btnTipp.setMaximumSize(new Dimension(85, 23));
      btnTipp.setContentAreaFilled(false);
      btnTipp.setBorder(new BevelBorder(0, null, null, null, null));
      btnTipp.setActionCommand("Tipp");
      btnTipp.addActionListener(this);
      buttonPanel.add(btnTipp);
      Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 10));
      buttonPanel.add(rigidArea_1);
      JButton btnOk = new JButton("OK");
      btnOk.setFont(new Font("Tahoma", 0, 11));
      btnOk.setPreferredSize(new Dimension(85, 23));
      btnOk.setMinimumSize(new Dimension(85, 23));
      btnOk.setMaximumSize(new Dimension(85, 23));
      btnOk.setContentAreaFilled(false);
      btnOk.setBorder(new BevelBorder(0, null, null, null, null));
      btnOk.setActionCommand("OK");
      btnOk.addActionListener(this);
      buttonPanel.add(btnOk);
      Component rigidArea = Box.createRigidArea(new Dimension(20, 10));
      buttonPanel.add(rigidArea);
   }

   private boolean getTippFromFile(int nr) {
      String zeile = "";
      String tippStr = "";
      boolean gefunden = false;
      int lineNr = 0;
      File file = new File(this.view.getMainPfad() + this.view.getFileSeparator() + "tipps.txt");
      if (!file.exists()) {
         JOptionPane.showMessageDialog(null, "Keine Datei tipps.txt vorhanden", "Tipps anzeigen", 0);
         return false;
      } else {
         try {
            BufferedReader brd = Files.newBufferedReader(Paths.get(file.getAbsolutePath()), StandardCharsets.ISO_8859_1);

            while((zeile = brd.readLine()) != null && !gefunden) {
               if (!zeile.isEmpty() && zeile.charAt(0) == '[') {
                  int pos = zeile.indexOf(93);
                  lineNr = Integer.parseInt(zeile.substring(1, pos));
                  if (lineNr == nr) {
                     tippStr = zeile;
                     gefunden = true;

                     while((zeile = brd.readLine()) != null && !zeile.equals("#")) {
                        tippStr = tippStr + "\n" + zeile;
                     }

                     this.txtTippAnzeige.setText(tippStr);
                  }
               }
            }

            brd.close();
         } catch (IOException var9) {
            var9.printStackTrace();
         }

         return gefunden;
      }
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      if (ae.getActionCommand() == "OK") {
         this.setVisible(false);
      }

      if (ae.getActionCommand() == "Tipp") {
         ++this.tippNummer;
         if (!this.getTippFromFile(this.tippNummer)) {
            this.tippNummer = 0;
         }
      }
   }
}
