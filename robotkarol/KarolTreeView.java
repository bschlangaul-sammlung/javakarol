package robotkarol;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class KarolTreeView {
   public JTree tree = null;
   private KarolProgram prog = null;
   private ImageIcon[] treeImage = new ImageIcon[8];

   public KarolTreeView(KarolProgram pro) {
      this.prog = pro;
      this.treeImage[0] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_pfeil.gif"));
      this.treeImage[1] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_grau.gif"));
      this.treeImage[2] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_sdef.gif"));
      this.treeImage[3] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_prog.gif"));
      this.treeImage[4] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_lila.gif"));
      this.treeImage[5] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_blau.gif"));
      this.treeImage[6] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_gruen.gif"));
      this.treeImage[7] = new ImageIcon(KarolTreeView.class.getResource("/icons/code_view_empty.gif"));
   }

   private boolean between(int x, int a, int b) {
      return a <= x && x <= b;
   }

   private void buildControls(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Kontrollstrukturen", 0));
      top.add(pNode);
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wiederhole immer ANW endewiederhole", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wiederhole n mal ANW endewiederhole", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wiederhole solange BED endewiederhole", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wiederhole ANW endewiederhole bis BED", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wiederhole ANW endewiederhole solange BED", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wenn BED dann ANW endewenn", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("wenn BED dann ANW sonst ANW endewenn", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Anweisung ANW endeAnweisung", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Methode ANW endeMethode", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Bedingung ANW endeBedingung", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Programm ANW endeProgramm", 1)));
   }

   private void buildCommands(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Vordefinierte Anweisungen", 0));
      top.add(pNode);
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Schritt", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Schritt(ANZAHL)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("LinksDrehen", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("RechtsDrehen", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Hinlegen", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Hinlegen(ANZAHL)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Hinlegen(FARBE)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Aufheben", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Aufheben(ANZAHL)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("MarkeSetzen", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("MarkeSetzen(FARBE)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("MarkeLöschen", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Warten", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Warten(mSEK)", 1)));

      for(int i = 109; i <= 112; ++i) {
         pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(this.prog.TokenMap.get(i), 1)));
      }

      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("FARBE=rot,gelb,blau,grün", 1)));
   }

   private void buildConditions(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Vordefinierte Bedingungen", 0));
      top.add(pNode);
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("IstWand", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("NichtIstWand", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("IstZiegel", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("IstZiegel(ANZAHL)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("IstZiegel(FARBE)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("NichtIstZiegel", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("NichtIstZiegel(ANZAHL)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("NichtIstZiegel(FARBE)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("IstMarke", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("IstMarke(FARBE)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("NichtIstMarke", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("NichtIstMarke(FARBE)", 1)));

      for(int i = 207; i <= 215; ++i) {
         pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(this.prog.TokenMap.get(i), 1)));
      }

      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("HatZiegel(ANZAHL)", 1)));
      pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("FARBE=rot,gelb,blau,grün", 1)));
   }

   private void buildBlocks(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Eigene Anweisungen/Bedingungen", 2));
      top.add(pNode);

      for(int b = 1; b < this.prog.blockArray.size(); ++b) {
         KarolProgram.KarolProgBlock aktBlock = this.prog.blockArray.get(b);
         pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(aktBlock.bezeichner + "()", 1)));
      }

      if (this.prog.blockArray.size() <= 1) {
         pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("keine", 1)));
      }
   }

   private String bedingungStr(KarolProgram.KarolProgAnw aktAnw) {
      String ergebStr = "";
      String tempStr = "";
      if (aktAnw.paramB) {
         ergebStr = (String)this.prog.TokenMap.get(13) + " ";
      }

      if (this.between(aktAnw.bedNr, 201, 215)) {
         if (aktAnw.paramI > 1) {
            tempStr = "(" + Integer.toString(aktAnw.paramI) + ")";
         }

         if (aktAnw.paramI < 0) {
            tempStr = this.prog.farbParameter[Math.abs(aktAnw.paramI) - 1];
         }

         ergebStr = ergebStr + (String)this.prog.TokenMap.get(aktAnw.bedNr) + tempStr;
      }

      if (this.between(aktAnw.bedNr, 401, 499)) {
         ergebStr = ergebStr + this.prog.blockArray.get(aktAnw.bedNr - 400).bezeichner + "()";
      }

      return ergebStr;
   }

   private void buildBereich(int startAnw, int endAnw, DefaultMutableTreeNode top) {
      int posAnw = startAnw;
      KarolProgram.KarolProgAnw aktAnw = null;
      KarolProgram.KarolProgAnw sonstAnw = null;
      KarolProgram.KarolProgAnw nextAnw = null;

      for(String tempStr = ""; posAnw <= endAnw && posAnw < this.prog.anwArray.size(); ++posAnw) {
         aktAnw = this.prog.anwArray.get(posAnw);
         if (this.between(aktAnw.schluesselNr, 101, 112)) {
            tempStr = "";
            if (aktAnw.paramI > 1) {
               tempStr = "(" + Integer.toString(aktAnw.paramI) + ")";
            }

            if (aktAnw.paramI < 0) {
               tempStr = this.prog.farbParameter[Math.abs(aktAnw.paramI) - 1];
            }

            tempStr = (String)this.prog.TokenMap.get(aktAnw.schluesselNr) + tempStr;
            top.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 5)));
         }

         if (this.between(aktAnw.schluesselNr, 11, 12)) {
            tempStr = this.prog.TokenMap.get(aktAnw.schluesselNr);
            top.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 5)));
         }

         if (this.between(aktAnw.schluesselNr, 301, 399)) {
            tempStr = this.prog.blockArray.get(aktAnw.schluesselNr - 300).bezeichner;
            top.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr + "()", 6)));
         }

         if (this.between(aktAnw.schluesselNr, 3, 5)) {
            tempStr = (String)this.prog.TokenMap.get(2) + " ";
            if (aktAnw.schluesselNr == 3) {
               tempStr = tempStr + Integer.toString(aktAnw.paramI) + " " + (String)this.prog.TokenMap.get(3);
            }

            if (aktAnw.schluesselNr == 5) {
               tempStr = tempStr + (String)this.prog.TokenMap.get(16);
            }

            if (aktAnw.schluesselNr == 4) {
               tempStr = tempStr + (String)this.prog.TokenMap.get(4) + " " + this.bedingungStr(aktAnw);
            }

            DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 1));
            top.add(pNode);
            this.buildBereich(posAnw + 1, aktAnw.geheZu, pNode);
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 2) {
            DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(this.prog.TokenMap.get(4), 1));
            top.add(pNode);
            this.buildBereich(posAnw + 1, aktAnw.geheZu, pNode);
            nextAnw = this.prog.anwArray.get(aktAnw.geheZu);
            tempStr = (String)this.prog.TokenMap.get(2) + " ";
            if (nextAnw.schluesselNr == 60) {
               tempStr = tempStr + (String)this.prog.TokenMap.get(4) + this.bedingungStr(nextAnw);
            }

            if (nextAnw.schluesselNr == 61) {
               tempStr = tempStr + (String)this.prog.TokenMap.get(4) + this.bedingungStr(nextAnw);
            }

            pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 7)));
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 6) {
            tempStr = (String)this.prog.TokenMap.get(6) + " " + this.bedingungStr(aktAnw) + " " + (String)this.prog.TokenMap.get(7);
            DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 1));
            top.add(pNode);
            this.buildBereich(posAnw + 1, aktAnw.geheZu, pNode);
            posAnw = aktAnw.geheZu;
         }

         if (aktAnw.schluesselNr == 7) {
            tempStr = (String)this.prog.TokenMap.get(6) + " " + this.bedingungStr(aktAnw) + " " + (String)this.prog.TokenMap.get(7);
            DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 1));
            top.add(pNode);
            this.buildBereich(posAnw + 1, aktAnw.geheZu, pNode);
            posAnw = aktAnw.geheZu;
            sonstAnw = this.prog.anwArray.get(posAnw);
            tempStr = this.prog.TokenMap.get(8);
            DefaultMutableTreeNode p2Node = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(tempStr, 1));
            top.add(p2Node);
            this.buildBereich(posAnw + 1, sonstAnw.geheZu, p2Node);
            posAnw = sonstAnw.geheZu;
         }
      }
   }

   private void buildProgram(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Dieses Programm", 3));
      top.add(pNode);

      for(int b = 0; b < this.prog.blockArray.size(); ++b) {
         KarolProgram.KarolProgBlock aktBlock = this.prog.blockArray.get(b);
         DefaultMutableTreeNode p2Node;
         if (b == 0) {
            p2Node = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("Programm()", 4));
         } else {
            p2Node = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten(aktBlock.bezeichner + "()", 4));
         }

         pNode.add(p2Node);
         this.buildBereich(this.prog.blockArray.get(b).beginn, this.prog.blockArray.get(b).ende, p2Node);
      }

      if (this.prog.blockArray.size() <= 0) {
         pNode.add(new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("kein Programm", 1)));
      }
   }

   public JTree buildCodeTree(boolean mitProg) {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode(new KarolTreeView.treeKnoten("CodeView", -1));
      this.buildControls(root);
      this.buildCommands(root);
      this.buildConditions(root);
      if (mitProg) {
         this.buildBlocks(root);
         this.buildProgram(root);
      }

      this.tree = new JTree(root);
      this.tree.setRootVisible(false);
      this.tree.setShowsRootHandles(true);
      this.tree.setFont(new Font("Arial", 0, 12));
      TreeCellRenderer renderer = new KarolTreeView.KarolTreeCellRenderer();
      this.tree.setCellRenderer(renderer);
      return this.tree;
   }

   class KarolTreeCellRenderer extends DefaultTreeCellRenderer {
      public KarolTreeCellRenderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
         super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
         KarolTreeView.treeKnoten nodeObj = (KarolTreeView.treeKnoten)node.getUserObject();
         int nr = nodeObj.iconNr;
         if (KarolTreeView.this.between(nr, 0, 7)) {
            this.setIcon(KarolTreeView.this.treeImage[nr]);
         }

         return this;
      }
   }

   private class treeKnoten {
      String textStr = "";
      int iconNr = 0;

      treeKnoten(String name, int icon) {
         this.textStr = name;
         this.iconNr = icon;
      }

      @Override
      public String toString() {
         return this.textStr;
      }
   }
}
