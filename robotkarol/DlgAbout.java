package robotkarol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class DlgAbout extends JDialog implements ActionListener {
   private final JPanel contentPanel = new JPanel();
   private final JPanel buttonPanel = new JPanel();
   private final JScrollPane scrLizenz = new JScrollPane();
   private final String strVersion = "3.0.4, 21.September 2019";
   private Timer lizenzTimer;

   public DlgAbout(KarolView owner) {
      super((Frame)owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(475, 470));
      this.setTitle("Programminfo");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgAbout.class.getResource("/icons/Karol.gif")));
      this.setModal(true);
      this.setDefaultCloseOperation(0);
      this.GUIAnlegen();
      this.pack();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public void showModal() {
      this.scrLizenz.getVerticalScrollBar().setValue(0);
      this.lizenzTimer.start();
      this.setVisible(true);
   }

   private void GUIAnlegen() {
      this.getContentPane().setLayout(new BorderLayout());
      this.contentPanel.setBorder(null);
      GridBagLayout gbl_contentPanel = new GridBagLayout();
      gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0};
      this.contentPanel.setLayout(gbl_contentPanel);
      this.getContentPane().add(this.contentPanel, "Center");
      JLabel lblKarolIcon1 = new JLabel(new ImageIcon(DlgAbout.class.getResource("/icons/Karol_about.gif")));
      GridBagConstraints gbc_lblIcon1 = new GridBagConstraints();
      gbc_lblIcon1.insets = new Insets(5, 0, 0, 0);
      gbc_lblIcon1.anchor = 16;
      gbc_lblIcon1.gridx = 0;
      gbc_lblIcon1.gridy = 0;
      lblKarolIcon1.setPreferredSize(new Dimension(36, 35));
      lblKarolIcon1.setSize(new Dimension(36, 36));
      this.contentPanel.add(lblKarolIcon1, gbc_lblIcon1);
      JLabel lblVersionsPict = new JLabel(new ImageIcon(DlgAbout.class.getResource("/icons/Karol_version.gif")));
      GridBagConstraints gbc_lblVersionPict = new GridBagConstraints();
      gbc_lblVersionPict.insets = new Insets(5, 0, 0, 0);
      gbc_lblVersionPict.gridwidth = 2;
      gbc_lblVersionPict.gridx = 1;
      gbc_lblVersionPict.gridy = 0;
      lblVersionsPict.setPreferredSize(new Dimension(365, 36));
      lblVersionsPict.setSize(new Dimension(365, 36));
      this.contentPanel.add(lblVersionsPict, gbc_lblVersionPict);
      JLabel lblKarolIcon2 = new JLabel(new ImageIcon(DlgAbout.class.getResource("/icons/Karol_about.gif")));
      GridBagConstraints gbc_lblIcon2 = new GridBagConstraints();
      gbc_lblIcon2.insets = new Insets(5, 0, 0, 0);
      gbc_lblIcon2.anchor = 14;
      gbc_lblIcon2.gridx = 3;
      gbc_lblIcon2.gridy = 0;
      lblKarolIcon2.setPreferredSize(new Dimension(36, 35));
      lblKarolIcon2.setSize(new Dimension(36, 36));
      this.contentPanel.add(lblKarolIcon2, gbc_lblIcon2);
      JTextArea txtErklaerung = new JTextArea(
         "RobotKarol ist eine Programmiersprache, die für Schülerinnen und Schüler zum Erlernen des Programmierens und zur Einführung in die Algorithmik gedacht ist."
      );
      txtErklaerung.setBorder(new EmptyBorder(2, 5, 2, 5));
      GridBagConstraints gbc_txtSprache = new GridBagConstraints();
      gbc_txtSprache.fill = 2;
      gbc_txtSprache.gridwidth = 4;
      gbc_txtSprache.gridx = 0;
      gbc_txtSprache.gridy = 1;
      gbc_txtSprache.insets = new Insets(0, 0, 5, 0);
      txtErklaerung.setFont(new Font("Tahoma", 0, 13));
      txtErklaerung.setEditable(false);
      txtErklaerung.setLineWrap(true);
      txtErklaerung.setWrapStyleWord(true);
      txtErklaerung.setForeground(Color.BLUE);
      txtErklaerung.setBackground(new Color(255, 255, 102));
      txtErklaerung.setSize(new Dimension(365, 36));
      this.contentPanel.add(txtErklaerung, gbc_txtSprache);
      JLabel lblVersionText = new JLabel("Version");
      lblVersionText.setFont(new Font("Tahoma", 0, 13));
      GridBagConstraints gbc_lblVersionText = new GridBagConstraints();
      gbc_lblVersionText.anchor = 17;
      gbc_lblVersionText.gridwidth = 2;
      gbc_lblVersionText.gridx = 0;
      gbc_lblVersionText.gridy = 2;
      gbc_lblVersionText.insets = new Insets(0, 5, 0, 5);
      this.contentPanel.add(lblVersionText, gbc_lblVersionText);
      JTextArea txtVersionText = new JTextArea("3.0.4, 21.September 2019");
      txtVersionText.setEditable(false);
      txtVersionText.setBorder(new EmptyBorder(0, 5, 0, 5));
      GridBagConstraints gbc_txtVersionText = new GridBagConstraints();
      gbc_txtVersionText.gridwidth = 2;
      gbc_txtVersionText.gridx = 1;
      gbc_txtVersionText.gridy = 3;
      gbc_txtVersionText.insets = new Insets(0, 0, 5, 5);
      gbc_txtVersionText.fill = 2;
      txtVersionText.setFont(new Font("Tahoma", 0, 13));
      txtVersionText.setBackground(new Color(240, 240, 240));
      txtVersionText.setWrapStyleWord(true);
      txtVersionText.setLineWrap(true);
      this.contentPanel.add(txtVersionText, gbc_txtVersionText);
      JLabel lblCopyright = new JLabel("Copyright");
      lblCopyright.setFont(new Font("Tahoma", 0, 13));
      GridBagConstraints gbc_lblCopyright = new GridBagConstraints();
      gbc_lblCopyright.anchor = 17;
      gbc_lblCopyright.gridwidth = 2;
      gbc_lblCopyright.gridx = 0;
      gbc_lblCopyright.gridy = 4;
      gbc_lblCopyright.insets = new Insets(0, 5, 0, 5);
      this.contentPanel.add(lblCopyright, gbc_lblCopyright);
      JTextArea txtCopyright = new JTextArea("Ulli Freiberger, Dezember 2016 - September 2019  \r\nauf der Basis von Java Version 8");
      txtCopyright.setEditable(false);
      txtCopyright.setBorder(new EmptyBorder(0, 5, 0, 5));
      GridBagConstraints gbc_txtVersion3 = new GridBagConstraints();
      gbc_txtVersion3.gridwidth = 2;
      gbc_txtVersion3.gridx = 1;
      gbc_txtVersion3.gridy = 5;
      gbc_txtVersion3.insets = new Insets(0, 0, 5, 5);
      gbc_txtVersion3.fill = 2;
      txtCopyright.setFont(new Font("Tahoma", 0, 13));
      txtCopyright.setBackground(new Color(240, 240, 240));
      txtCopyright.setWrapStyleWord(true);
      txtCopyright.setLineWrap(true);
      this.contentPanel.add(txtCopyright, gbc_txtVersion3);
      JLabel lblIdee = new JLabel("Ideen");
      GridBagConstraints gbc_lblIdee = new GridBagConstraints();
      gbc_lblIdee.anchor = 17;
      gbc_lblIdee.gridx = 0;
      gbc_lblIdee.gridy = 6;
      gbc_lblIdee.gridwidth = 2;
      gbc_lblIdee.insets = new Insets(0, 5, 0, 5);
      lblIdee.setFont(new Font("Tahoma", 0, 13));
      this.contentPanel.add(lblIdee, gbc_lblIdee);
      JTextArea txtIdee1 = new JTextArea("Richard E. Pattis, \"Karel the Robot\" A Gentle Introduction to the Art of Programming, 1995");
      txtIdee1.setEditable(false);
      txtIdee1.setBorder(new EmptyBorder(0, 5, 0, 5));
      GridBagConstraints gbc_txtIdee1 = new GridBagConstraints();
      gbc_txtIdee1.fill = 1;
      gbc_txtIdee1.gridx = 1;
      gbc_txtIdee1.gridy = 7;
      gbc_txtIdee1.gridwidth = 2;
      gbc_txtIdee1.insets = new Insets(0, 0, 5, 5);
      txtIdee1.setFont(new Font("Tahoma", 0, 13));
      txtIdee1.setWrapStyleWord(true);
      txtIdee1.setLineWrap(true);
      txtIdee1.setBackground(new Color(240, 240, 240));
      this.contentPanel.add(txtIdee1, gbc_txtIdee1);
      JTextArea txtIdee2 = new JTextArea("Ondrej Krsko, Robot Karol slowakisch, Version 1.0, Mai 2001");
      txtIdee2.setEditable(false);
      txtIdee2.setBorder(new EmptyBorder(0, 5, 0, 5));
      GridBagConstraints gbc_txtIdee2 = new GridBagConstraints();
      gbc_txtIdee2.fill = 1;
      gbc_txtIdee2.gridx = 1;
      gbc_txtIdee2.gridy = 8;
      gbc_txtIdee2.gridwidth = 2;
      gbc_txtIdee2.insets = new Insets(0, 0, 5, 5);
      txtIdee2.setWrapStyleWord(true);
      txtIdee2.setLineWrap(true);
      txtIdee2.setFont(new Font("Tahoma", 0, 13));
      txtIdee2.setBackground(new Color(240, 240, 240));
      this.contentPanel.add(txtIdee2, gbc_txtIdee2);
      JTextArea txtIdee3 = new JTextArea("Ulli Freiberger, Robot Karol, auf der Basis von Delphi 4.0, letzte Version 2.3, April 2013");
      txtIdee3.setEditable(false);
      txtIdee3.setBorder(new EmptyBorder(0, 5, 0, 5));
      GridBagConstraints gbc_txtIdee3 = new GridBagConstraints();
      gbc_txtIdee3.fill = 1;
      gbc_txtIdee3.gridx = 1;
      gbc_txtIdee3.gridy = 9;
      gbc_txtIdee3.gridwidth = 2;
      gbc_txtIdee3.insets = new Insets(0, 0, 5, 5);
      txtIdee3.setWrapStyleWord(true);
      txtIdee3.setLineWrap(true);
      txtIdee3.setFont(new Font("Tahoma", 0, 13));
      txtIdee3.setBackground(new Color(240, 240, 240));
      this.contentPanel.add(txtIdee3, gbc_txtIdee3);
      JLabel lblLizenz = new JLabel("Lizenz");
      GridBagConstraints gbc_lblLizenz = new GridBagConstraints();
      gbc_lblLizenz.anchor = 17;
      gbc_lblLizenz.insets = new Insets(0, 5, 0, 5);
      gbc_lblLizenz.gridx = 0;
      gbc_lblLizenz.gridy = 10;
      gbc_lblLizenz.gridwidth = 2;
      lblLizenz.setFont(new Font("Tahoma", 0, 13));
      this.contentPanel.add(lblLizenz, gbc_lblLizenz);
      JTextArea txtLizenz = new JTextArea();
      txtLizenz.setSize(new Dimension(0, 100));
      txtLizenz.setBorder(new EmptyBorder(0, 5, 0, 5));
      txtLizenz.setText(
         "Das Programm \"Robot Karol\" steht für den Bildungsbereich zur freien Nutzung zur Verfügung. Das heißt, Schüler, Lehrer und Studenten dürfen das Programm uneingeschränkt und kostenlos nutzen, es beliebig oft installieren und kopieren.\r\nDas Programm darf frei weitergegeben werden, sofern es nicht verändert wird und das Originalarchiv mit allen enthaltenen Dateien erhalten bleibt. Für die Weitergabe darf keine Gebühr erhoben werden mit Ausnahme der Abgaben, die nötig sind um die Kosten für das Vertriebsmedium zu decken.\r\nEin Decomplieren der Software ist ebenso verboten, wie auch die Verwendung einzelner Programmteile in anderen Softwareprodukten.\r\nDiese Software wird vertrieben als \"wie-es-ist\", ohne jegliche ausgesprochene oder implizite Garantie. In keiner Weise ist der Autor haftbar für Schäden, die durch die Nutzung dieser Software entstehen.\r\n\r\nCopyright (c) Ulli Freiberger September 2019\r\n (ca. 990h Arbeit)\r\nAlle weitervertriebenen Versionen des Programms müssen die Copyright-Hinweise und Lizenz-Angaben in der Originalform beinhalten."
      );
      txtLizenz.setEditable(false);
      txtLizenz.setWrapStyleWord(true);
      txtLizenz.setLineWrap(true);
      txtLizenz.setFont(new Font("Tahoma", 0, 13));
      txtLizenz.setBackground(new Color(240, 240, 240));
      this.scrLizenz.setMinimumSize(new Dimension(23, 50));
      GridBagConstraints gbc_scrLizenz = new GridBagConstraints();
      gbc_scrLizenz.fill = 1;
      gbc_scrLizenz.gridx = 1;
      gbc_scrLizenz.gridy = 11;
      gbc_scrLizenz.gridwidth = 2;
      gbc_scrLizenz.insets = new Insets(0, 0, 5, 5);
      this.contentPanel.add(this.scrLizenz, gbc_scrLizenz);
      this.scrLizenz.setViewportView(txtLizenz);
      Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
      rigidArea_1.setMaximumSize(new Dimension(150, 10));
      rigidArea_1.setSize(new Dimension(150, 5));
      rigidArea_1.setMinimumSize(new Dimension(150, 5));
      rigidArea_1.setPreferredSize(new Dimension(150, 5));
      GridBagConstraints gbc_rigidArea_1 = new GridBagConstraints();
      gbc_rigidArea_1.insets = new Insets(0, 0, 0, 5);
      gbc_rigidArea_1.gridx = 1;
      gbc_rigidArea_1.gridy = 12;
      this.contentPanel.add(rigidArea_1, gbc_rigidArea_1);
      Component rigidArea_2 = Box.createRigidArea(new Dimension(20, 20));
      rigidArea_2.setMaximumSize(new Dimension(150, 5));
      rigidArea_2.setMinimumSize(new Dimension(150, 5));
      rigidArea_2.setSize(new Dimension(150, 5));
      rigidArea_2.setPreferredSize(new Dimension(150, 10));
      GridBagConstraints gbc_rigidArea_2 = new GridBagConstraints();
      gbc_rigidArea_2.insets = new Insets(0, 0, 0, 5);
      gbc_rigidArea_2.gridx = 2;
      gbc_rigidArea_2.gridy = 12;
      this.contentPanel.add(rigidArea_2, gbc_rigidArea_2);
      this.buttonPanel.setPreferredSize(new Dimension(100, 32));
      this.buttonPanel.setEnabled(false);
      this.buttonPanel.setBorder(null);
      this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 0));
      this.getContentPane().add(this.buttonPanel, "South");
      Component horizontalGlue = Box.createHorizontalGlue();
      horizontalGlue.setPreferredSize(new Dimension(100, 0));
      horizontalGlue.setFocusable(false);
      horizontalGlue.setEnabled(false);
      this.buttonPanel.add(horizontalGlue);
      JButton button = new JButton("OK");
      button.setFont(new Font("Tahoma", 0, 11));
      button.setPreferredSize(new Dimension(85, 23));
      button.setMinimumSize(new Dimension(85, 23));
      button.setMaximumSize(new Dimension(85, 23));
      button.setContentAreaFilled(false);
      button.setBorder(new BevelBorder(0, null, null, null, null));
      button.setActionCommand("OK");
      button.addActionListener(this);
      this.buttonPanel.add(button);
      Component rigidArea = Box.createRigidArea(new Dimension(20, 10));
      this.buttonPanel.add(rigidArea);
      this.lizenzTimer = new Timer(1000, this);
      this.lizenzTimer.setActionCommand("Timer");
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      if (ae.getActionCommand() == "OK") {
         this.lizenzTimer.stop();
         this.setVisible(false);
      }

      if (ae.getActionCommand() == "Timer") {
         if (this.lizenzTimer.getDelay() > 100) {
            this.lizenzTimer.setDelay(100);
         }

         int value = this.scrLizenz.getVerticalScrollBar().getValue();
         if (value <= this.scrLizenz.getVerticalScrollBar().getMaximum()) {
            this.scrLizenz.getVerticalScrollBar().setValue(++value);
         }
      }
   }
}
