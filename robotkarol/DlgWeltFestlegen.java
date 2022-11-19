package robotkarol;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class DlgWeltFestlegen extends JDialog implements ActionListener {
   private JPanel contentPanel = new JPanel();
   private ButtonGroup gruppe;
   private JToggleButton btnSchliessen;

   public DlgWeltFestlegen(KarolView owner) {
      super((Frame)owner);
      this.setResizable(false);
      this.setPreferredSize(new Dimension(414, 230));
      this.setTitle("Festlegen");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgWeltFestlegen.class.getResource("/icons/Karol.gif")));
      this.setModal(false);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      this.pack();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
   }

   public void showNonModal() {
      this.setVisible(true);
   }

   public void hideNonModal() {
      this.btnSchliessen.setSelected(true);
      this.setVisible(false);
   }

   public String getAuswahl() {
      String was = null;
      was = this.gruppe.getSelection().getActionCommand();
      return was != "Close" ? was : "";
   }

   private void GUIAnlegen() {
      this.getContentPane().setLayout(new BorderLayout());
      this.getContentPane().add(this.contentPanel, "Center");
      this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      this.contentPanel.setLayout(new GridLayout(3, 6, 4, 4));
      JToggleButton btnZiegelRot = new JToggleButton("");
      btnZiegelRot.setContentAreaFilled(false);
      btnZiegelRot.setFocusPainted(false);
      btnZiegelRot.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Ziegel_rot.gif")));
      btnZiegelRot.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnZiegelRot.setToolTipText("roten Ziegel setzen");
      btnZiegelRot.setActionCommand("zro");
      btnZiegelRot.setSelected(true);
      this.contentPanel.add(btnZiegelRot);
      JToggleButton btnZiegelGelb = new JToggleButton("");
      btnZiegelGelb.setContentAreaFilled(false);
      btnZiegelGelb.setFocusPainted(false);
      btnZiegelGelb.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Ziegel_gelb.gif")));
      btnZiegelGelb.setToolTipText("gelben Ziegel setzen");
      btnZiegelGelb.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnZiegelGelb.setActionCommand("zge");
      this.contentPanel.add(btnZiegelGelb);
      JToggleButton btnZiegelBlau = new JToggleButton("");
      btnZiegelBlau.setFocusPainted(false);
      btnZiegelBlau.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Ziegel_blau.gif")));
      btnZiegelBlau.setToolTipText("blauen Ziegel setzen");
      btnZiegelBlau.setContentAreaFilled(false);
      btnZiegelBlau.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnZiegelBlau.setActionCommand("zbl");
      this.contentPanel.add(btnZiegelBlau);
      JToggleButton btnZiegelGruen = new JToggleButton("");
      btnZiegelGruen.setFocusPainted(false);
      btnZiegelGruen.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Ziegel_gruen.gif")));
      btnZiegelGruen.setToolTipText("grünen Ziegel setzen");
      btnZiegelGruen.setContentAreaFilled(false);
      btnZiegelGruen.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnZiegelGruen.setActionCommand("zgr");
      this.contentPanel.add(btnZiegelGruen);
      JToggleButton btnZiegelSchwarz = new JToggleButton("");
      btnZiegelSchwarz.setEnabled(false);
      btnZiegelSchwarz.setFocusPainted(false);
      btnZiegelSchwarz.setIcon(null);
      btnZiegelSchwarz.setToolTipText("");
      btnZiegelSchwarz.setContentAreaFilled(false);
      btnZiegelSchwarz.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnZiegelSchwarz.setActionCommand("zsc");
      this.contentPanel.add(btnZiegelSchwarz);
      JToggleButton btnZiegelLoeschen = new JToggleButton("");
      btnZiegelLoeschen.setFocusPainted(false);
      btnZiegelLoeschen.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/icons/button_ziegel_delete.gif")));
      btnZiegelLoeschen.setToolTipText("Ziegel entfernen");
      btnZiegelLoeschen.setContentAreaFilled(false);
      btnZiegelLoeschen.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnZiegelLoeschen.setActionCommand("zxx");
      this.contentPanel.add(btnZiegelLoeschen);
      JToggleButton btnMarkeRot = new JToggleButton("");
      btnMarkeRot.setFocusPainted(false);
      btnMarkeRot.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Marke_rot.gif")));
      btnMarkeRot.setToolTipText("rote Marke setzen");
      btnMarkeRot.setContentAreaFilled(false);
      btnMarkeRot.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnMarkeRot.setActionCommand("mro");
      this.contentPanel.add(btnMarkeRot);
      JToggleButton btnMarkeGelb = new JToggleButton("");
      btnMarkeGelb.setFocusPainted(false);
      btnMarkeGelb.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Marke_gelb.gif")));
      btnMarkeGelb.setToolTipText("gelbe Marke setzen");
      btnMarkeGelb.setContentAreaFilled(false);
      btnMarkeGelb.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnMarkeGelb.setActionCommand("mge");
      this.contentPanel.add(btnMarkeGelb);
      JToggleButton btnMarkeBlau = new JToggleButton("");
      btnMarkeBlau.setFocusPainted(false);
      btnMarkeBlau.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Marke_blau.gif")));
      btnMarkeBlau.setToolTipText("blaue Marke setzen");
      btnMarkeBlau.setContentAreaFilled(false);
      btnMarkeBlau.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnMarkeBlau.setActionCommand("mbl");
      this.contentPanel.add(btnMarkeBlau);
      JToggleButton btnMarkeGruen = new JToggleButton("");
      btnMarkeGruen.setFocusPainted(false);
      btnMarkeGruen.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Marke_gruen.gif")));
      btnMarkeGruen.setToolTipText("grüne Marke setzen");
      btnMarkeGruen.setContentAreaFilled(false);
      btnMarkeGruen.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnMarkeGruen.setActionCommand("mgr");
      this.contentPanel.add(btnMarkeGruen);
      JToggleButton btnMarkeSchwarz = new JToggleButton("");
      btnMarkeSchwarz.setFocusPainted(false);
      btnMarkeSchwarz.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Marke_schwarz.gif")));
      btnMarkeSchwarz.setToolTipText("schwarze Marke setzen");
      btnMarkeSchwarz.setContentAreaFilled(false);
      btnMarkeSchwarz.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnMarkeSchwarz.setActionCommand("msc");
      this.contentPanel.add(btnMarkeSchwarz);
      JToggleButton btnMarkeLoeschen = new JToggleButton("");
      btnMarkeLoeschen.setFocusPainted(false);
      btnMarkeLoeschen.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/icons/button_marke_delete.gif")));
      btnMarkeLoeschen.setToolTipText("Marke entfernen");
      btnMarkeLoeschen.setContentAreaFilled(false);
      btnMarkeLoeschen.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnMarkeLoeschen.setActionCommand("mxx");
      this.contentPanel.add(btnMarkeLoeschen);
      JToggleButton btnQuader = new JToggleButton("");
      btnQuader.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/imgs/Quader.gif")));
      btnQuader.setToolTipText("Quader setzen");
      btnQuader.setFocusPainted(false);
      btnQuader.setContentAreaFilled(false);
      btnQuader.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnQuader.setActionCommand("qua");
      this.contentPanel.add(btnQuader);
      JToggleButton btnQuaderLoeschen = new JToggleButton("");
      btnQuaderLoeschen.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/icons/button_quader_delete.gif")));
      btnQuaderLoeschen.setToolTipText("Quader entfernen");
      btnQuaderLoeschen.setFocusPainted(false);
      btnQuaderLoeschen.setContentAreaFilled(false);
      btnQuaderLoeschen.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnQuaderLoeschen.setActionCommand("qxx");
      this.contentPanel.add(btnQuaderLoeschen);
      Component horizontalStrut = Box.createHorizontalStrut(20);
      this.contentPanel.add(horizontalStrut);
      JToggleButton btnKarol = new JToggleButton("");
      btnKarol.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/icons/button_karol3.gif")));
      btnKarol.setToolTipText("Karol setzen");
      btnKarol.setFocusPainted(false);
      btnKarol.setContentAreaFilled(false);
      btnKarol.setBorder(UIManager.getBorder("ToggleButton.border"));
      btnKarol.setActionCommand("kar");
      this.contentPanel.add(btnKarol);
      Component horizontalStrut_1 = Box.createHorizontalStrut(20);
      this.contentPanel.add(horizontalStrut_1);
      this.btnSchliessen = new JToggleButton("");
      this.btnSchliessen.setIcon(new ImageIcon(DlgWeltFestlegen.class.getResource("/icons/button_ok.gif")));
      this.btnSchliessen.setToolTipText("Toolbox schließen");
      this.btnSchliessen.setFocusPainted(false);
      this.btnSchliessen.setContentAreaFilled(false);
      this.btnSchliessen.setBorder(UIManager.getBorder("ToggleButton.border"));
      this.btnSchliessen.addActionListener(this);
      this.btnSchliessen.setActionCommand("Close");
      this.contentPanel.add(this.btnSchliessen);
      this.gruppe = new ButtonGroup();
      this.gruppe.add(btnZiegelRot);
      this.gruppe.add(btnZiegelGelb);
      this.gruppe.add(btnZiegelBlau);
      this.gruppe.add(btnZiegelGruen);
      this.gruppe.add(btnZiegelSchwarz);
      this.gruppe.add(btnZiegelLoeschen);
      this.gruppe.add(btnMarkeRot);
      this.gruppe.add(btnMarkeGelb);
      this.gruppe.add(btnMarkeBlau);
      this.gruppe.add(btnMarkeGruen);
      this.gruppe.add(btnMarkeSchwarz);
      this.gruppe.add(btnMarkeLoeschen);
      this.gruppe.add(btnQuader);
      this.gruppe.add(btnQuaderLoeschen);
      this.gruppe.add(btnKarol);
      this.gruppe.add(this.btnSchliessen);
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      if (ae.getActionCommand() == "Close") {
         this.setVisible(false);
      }
   }
}
