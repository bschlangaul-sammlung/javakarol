package robotkarol;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class DlgHilfe extends JDialog implements ActionListener, HyperlinkListener, TreeSelectionListener {
   private JEditorPane helpHTMLPane;
   private JTree helpTree;
   private ImageIcon[] helpTreeIcon = new ImageIcon[5];

   public DlgHilfe(KarolView owner) {
      super((Frame)owner);
      this.setPreferredSize(new Dimension(740, 620));
      this.setTitle("Hilfe");
      this.setIconImage(Toolkit.getDefaultToolkit().getImage(DlgHilfe.class.getResource("/icons/Karol.gif")));
      this.setModal(true);
      this.setDefaultCloseOperation(1);
      this.GUIAnlegen();
      this.pack();
      Dimension parentFrameSize = owner.getSize();
      Point p = owner.getLocation();
      this.setLocation(p.x + parentFrameSize.width / 2, p.y + parentFrameSize.height / 2);
      this.helpTreeIcon[0] = new ImageIcon(DlgHilfe.class.getResource("/icons/help_robotkarol.gif"));
      this.helpTreeIcon[1] = new ImageIcon(DlgHilfe.class.getResource("/icons/help_javakarol.gif"));
      this.helpTreeIcon[2] = new ImageIcon(DlgHilfe.class.getResource("/icons/help_diverses.gif"));
      this.helpTreeIcon[3] = new ImageIcon(DlgHilfe.class.getResource("/icons/help_blatt.gif"));
      this.helpTreeIcon[4] = new ImageIcon(DlgHilfe.class.getResource("/icons/help_sprache.gif"));
   }

   public void showModal() {
      URL helpIndexUrl = null;

      try {
         helpIndexUrl = new URL(DlgHilfe.class.getResource("/help/index.htm").toString());
      } catch (MalformedURLException var4) {
      }

      try {
         this.helpHTMLPane.setPage(helpIndexUrl);
      } catch (IOException var3) {
      }

      this.helpTree.expandRow(0);
      this.helpTree.collapseRow(0);
      this.helpTree.expandRow(1);
      this.helpTree.collapseRow(1);
      this.helpTree.expandRow(2);
      this.helpTree.collapseRow(2);
      this.setVisible(true);
   }

   private void GUIAnlegen() {
      this.getContentPane().setLayout(new BorderLayout(0, 0));
      JSplitPane panelSplitterHelp = new JSplitPane(1);
      panelSplitterHelp.setDividerSize(3);
      this.getContentPane().add(panelSplitterHelp, "Center");
      JPanel panelHelpTree = new JPanel();
      panelHelpTree.setLayout(new BorderLayout(0, 0));
      panelHelpTree.setMinimumSize(new Dimension(80, 200));
      panelHelpTree.setPreferredSize(new Dimension(200, 570));
      panelSplitterHelp.setLeftComponent(panelHelpTree);
      JScrollPane scrollBoxHelpTree = new JScrollPane();
      scrollBoxHelpTree.setViewportBorder(new EmptyBorder(5, 5, 5, 5));
      scrollBoxHelpTree.setBackground(Color.WHITE);
      panelHelpTree.add(scrollBoxHelpTree, "Center");
      scrollBoxHelpTree.setViewportView(this.buildHelpTree());
      JPanel panelHelpHTML = new JPanel();
      panelHelpHTML.setLayout(new BorderLayout(0, 0));
      panelHelpHTML.setMinimumSize(new Dimension(200, 200));
      panelHelpHTML.setPreferredSize(new Dimension(300, 570));
      panelSplitterHelp.setRightComponent(panelHelpHTML);
      this.helpHTMLPane = new JEditorPane();
      this.helpHTMLPane.setContentType("text/html");
      this.helpHTMLPane.setEditorKit(new HTMLEditorKit());
      this.helpHTMLPane.addHyperlinkListener(this);
      this.helpHTMLPane.setEditable(false);
      JScrollPane scrollPane = new JScrollPane(this.helpHTMLPane);
      panelHelpHTML.add(scrollPane);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setPreferredSize(new Dimension(700, 32));
      buttonPanel.setEnabled(false);
      buttonPanel.setBorder(null);
      buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
      this.getContentPane().add(buttonPanel, "South");
      Component horizontalGlue = Box.createHorizontalGlue();
      horizontalGlue.setPreferredSize(new Dimension(100, 0));
      buttonPanel.add(horizontalGlue);
      JButton button = new JButton("OK");
      button.setFont(new Font("Tahoma", 0, 11));
      button.setPreferredSize(new Dimension(85, 23));
      button.setMinimumSize(new Dimension(85, 23));
      button.setMaximumSize(new Dimension(85, 23));
      button.setContentAreaFilled(false);
      button.setBorder(new BevelBorder(0, null, null, null, null));
      button.setActionCommand("OK");
      button.addActionListener(this);
      buttonPanel.add(button);
      Component rigidArea = Box.createRigidArea(new Dimension(20, 10));
      buttonPanel.add(rigidArea);
   }

   public JTree buildHelpTree() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("HelpView", -1, null));
      this.buildNodes(root);
      this.helpTree = new JTree(root);
      this.helpTree.getSelectionModel().setSelectionMode(1);
      this.helpTree.addTreeSelectionListener(this);
      this.helpTree.setRootVisible(false);
      this.helpTree.setShowsRootHandles(true);
      TreeCellRenderer renderer = new DlgHilfe.HelpTreeCellRenderer();
      this.helpTree.setCellRenderer(renderer);
      this.helpTree.setRowHeight(20);
      return this.helpTree;
   }

   private void buildNodes(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode rkNode = new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("RobotKarol", 0, null));
      top.add(rkNode);
      DefaultMutableTreeNode jkNode = new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("JavaKarol", 10, null));
      top.add(jkNode);
      DefaultMutableTreeNode divNode = new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Diverses", 20, null));
      top.add(divNode);
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Info", 1, "/help/rkarol.htm")));
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Programmierumgebung", 1, "/help/progumgebung.htm")));
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Anleitung Editor", 1, "/help/editor.htm")));
      DefaultMutableTreeNode sprNode = new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Sprache Karol", 5, null));
      sprNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Kontrollstrukturen", 2, "/help/sprache.htm")));
      sprNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Anweisungen", 2, "/help/sprache2.htm")));
      sprNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Bedingungen", 2, "/help/sprache3.htm")));
      rkNode.add(sprNode);
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Programmablauf", 1, "/help/ablauf.htm")));
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Steuerung Karol", 1, "/help/steuerung.htm")));
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Struktogramm", 1, "/help/strukto.htm")));
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Download/Handbuch", 1, "/help/download.htm")));
      rkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Beispiele", 1, "/help/beispiele.htm")));
      jkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Info", 11, "/help/jkarol.htm")));
      jkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Programmierung", 11, "/help/jprog.htm")));
      jkNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Download", 11, "/help/jdownload.htm")));
      divNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Historie", 21, "/help/historie.htm")));
      divNode.add(new DefaultMutableTreeNode(new DlgHilfe.treeKnoten("Kontakt", 21, "/help/kontakt.htm")));
   }

   public void selectNodeRow(URL searchURL) {
      DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)this.helpTree.getModel().getRoot();
      Enumeration e = rootNode.breadthFirstEnumeration();

      while(e.hasMoreElements()) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.nextElement();
         DlgHilfe.treeKnoten t = (DlgHilfe.treeKnoten)node.getUserObject();
         URL nodeURL = t.htmlURL;
         if (nodeURL != null && nodeURL.equals(searchURL)) {
            TreePath tp = new TreePath(node.getPath());
            this.helpTree.setSelectionPath(tp);
            break;
         }
      }
   }

   @Override
   public void hyperlinkUpdate(HyperlinkEvent e) {
      if (e.getEventType() == EventType.ACTIVATED) {
         if (e instanceof HTMLFrameHyperlinkEvent) {
            HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent)e;
            HTMLDocument doc = (HTMLDocument)this.helpHTMLPane.getDocument();
            doc.processHTMLFrameHyperlinkEvent(evt);
         } else {
            this.selectNodeRow(e.getURL());

            try {
               this.helpHTMLPane.setPage(e.getURL());
            } catch (Throwable var4) {
            }
         }
      }
   }

   @Override
   public void valueChanged(TreeSelectionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.helpTree.getLastSelectedPathComponent();
      if (node != null) {
         DlgHilfe.treeKnoten nodeObj = (DlgHilfe.treeKnoten)node.getUserObject();

         try {
            if (nodeObj.htmlURL != null) {
               this.helpHTMLPane.setPage(nodeObj.htmlURL);
            }
         } catch (IOException var5) {
         }
      }
   }

   @Override
   public void actionPerformed(ActionEvent ae) {
      ae.getActionCommand();
      this.setVisible(false);
   }

   class HelpTreeCellRenderer extends DefaultTreeCellRenderer {
      public HelpTreeCellRenderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
         DlgHilfe.treeKnoten nodeObj = (DlgHilfe.treeKnoten)node.getUserObject();
         int nr = nodeObj.levelNr;
         this.setLeafIcon(DlgHilfe.this.helpTreeIcon[3]);
         if (nr == 0) {
            this.setIcon(DlgHilfe.this.helpTreeIcon[0]);
         }

         if (nr == 10) {
            this.setIcon(DlgHilfe.this.helpTreeIcon[1]);
         }

         if (nr == 20) {
            this.setIcon(DlgHilfe.this.helpTreeIcon[2]);
         }

         if (nr == 5) {
            this.setIcon(DlgHilfe.this.helpTreeIcon[4]);
         }

         return this;
      }
   }

   private class treeKnoten {
      String textStr = "";
      int levelNr = 0;
      URL htmlURL;

      treeKnoten(String name, int art, String filename) {
         this.textStr = name;
         this.levelNr = art;
         if (filename == null) {
            this.htmlURL = null;
         } else {
            this.htmlURL = DlgHilfe.class.getResource(filename);
         }
      }

      @Override
      public String toString() {
         if (this.levelNr == 0) {
            return "<html><font color = #FFCE63 size = +2>RobotKarol</font></html>";
         } else if (this.levelNr == 10) {
            return "<html><font color = #8DB5E3 size = +2>JavaKarol</font></html>";
         } else {
            return this.levelNr == 20 ? "<html><font color = #228B22 size = +2>Diverses</font></html>" : this.textStr;
         }
      }
   }
}
