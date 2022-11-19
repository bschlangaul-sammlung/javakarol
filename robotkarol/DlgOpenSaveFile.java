package robotkarol;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DlgOpenSaveFile {
   private KarolView view;

   public DlgOpenSaveFile(KarolView owner) {
      this.view = owner;
   }

   public String dateiVorhanden(String fileName, char fileTyp) {
      if (fileName.length() > 0) {
         File f = new File(fileName);
         boolean check = false;

         try {
            if (f.isFile()) {
               if (fileTyp == 'w' && (f.getName().toLowerCase().endsWith(".kdw") || f.getName().toLowerCase().endsWith(".jkw"))) {
                  check = true;
               }

               if (fileTyp == 'k' && f.getName().toLowerCase().endsWith(".kdw")) {
                  check = true;
               }

               if (fileTyp == 'p' && f.getName().toLowerCase().endsWith(".kdp")) {
                  check = true;
               }
            }
         } catch (SecurityException var7) {
         }

         if (check) {
            try {
               fileName = f.getCanonicalPath();
            } catch (IOException var6) {
            }
         } else {
            fileName = "";
         }
      }

      return fileName;
   }

   public String dateiOeffnen(String pathName, char fileTyp) {
      FileNameExtensionFilter filter = null;
      String ergeb = "";
      JFileChooser d = new JFileChooser();
      if (pathName.length() > 0) {
         d.setCurrentDirectory(new File(pathName));
      }

      if (fileTyp == 'w') {
         filter = new FileNameExtensionFilter("Karolwelt *.kdw; *.jkw", "kdw", "jkw");
         d.setDialogTitle("Karolwelt laden");
      }

      if (fileTyp == 'p') {
         filter = new FileNameExtensionFilter("Karolprogramm *.kdp", "kdp");
         d.setDialogTitle("Karolprogramm laden");
      }

      if (fileTyp == 'g') {
         filter = new FileNameExtensionFilter("Karolfiguren *.gif", "gif");
         d.setDialogTitle("Karolfigur laden");
      }

      d.setFileFilter(filter);
      int status = d.showOpenDialog(null);
      File f = d.getSelectedFile();
      if (f != null && status == 0) {
         try {
            ergeb = f.getCanonicalPath();
         } catch (IOException var9) {
            ergeb = "";
         }

         return ergeb;
      } else {
         return "";
      }
   }

   public String dateiSpeichernUnter(String fileName, char fileTyp) {
      File f = null;
      FileNameExtensionFilter filter = null;
      JFileChooser d = new JFileChooser();
      if (fileTyp == 'k') {
         filter = new FileNameExtensionFilter("Karolwelt *.kdw", "kdw");
         d.setDialogTitle("Karolwelt speichern");
      }

      if (fileTyp == 'p') {
         filter = new FileNameExtensionFilter("Karolprogramm *.kdp", "kdp");
         d.setDialogTitle("Karolprogramm speichern");
      }

      if (fileTyp == 'g') {
         filter = new FileNameExtensionFilter("Bilder *.png *.jpg *.bmp", "png", "jpg", "bmp");
         d.setDialogTitle("3DWelt als Bild speichern");
      }

      if (fileTyp == 's') {
         filter = new FileNameExtensionFilter("Bilder *.png *.jpg *.bmp", "png", "jpg", "bmp");
         d.setDialogTitle("Struktogramm als Bild speichern");
      }

      d.setFileFilter(filter);
      d.setSelectedFile(new File(fileName));
      int status = d.showSaveDialog(null);
      if (status == 0) {
         f = d.getSelectedFile();

         try {
            fileName = f.getCanonicalPath();
         } catch (IOException var9) {
         }

         if (fileName.length() > 0) {
            if (fileTyp == 'k' && !fileName.toLowerCase().endsWith(".kdw")) {
               fileName = fileName + ".kdw";
            }

            if (fileTyp == 'p' && !fileName.toLowerCase().endsWith(".kdp")) {
               fileName = fileName + ".kdp";
            }

            if ((fileTyp == 'g' || fileTyp == 's')
               && !fileName.toLowerCase().endsWith(".bmp")
               && !fileName.toLowerCase().endsWith(".jpg")
               && !fileName.toLowerCase().endsWith(".png")) {
               fileName = fileName + ".png";
            }
         }

         if (f.exists()) {
            if (fileTyp == 'k') {
               status = JOptionPane.showConfirmDialog(null, "Die vorhandene Datei\n" + f.getName() + "\nüberschreiben?", "KarolWelt speichern", 0);
            }

            if (fileTyp == 'p') {
               status = JOptionPane.showConfirmDialog(null, "Die vorhandene Datei\n" + f.getName() + "\nüberschreiben?", "KarolProgramm speichern", 0);
            }

            if (fileTyp == 'g') {
               status = JOptionPane.showConfirmDialog(null, "Die vorhandene Datei\n" + f.getName() + "\nüberschreiben?", "3DWelt als Bild speichern", 0);
            }

            if (fileTyp == 's') {
               status = JOptionPane.showConfirmDialog(null, "Die vorhandene Datei\n" + f.getName() + "\nüberschreiben?", "Struktogramm als Bild speichern", 0);
            }

            if (status == 0) {
               try {
                  f.delete();
               } catch (SecurityException var8) {
                  fileName = "";
               }
            } else {
               fileName = "";
            }
         }
      } else {
         fileName = "";
      }

      return fileName;
   }

   private boolean fileExist(String fileName) {
      File f = new File(fileName);
      return f.exists() && f.isFile();
   }

   public String pfadNeuerFiguren() {
      String defaultFileName = "";
      String fileName = "";
      String fileSeparator = "";
      File f = null;
      fileSeparator = this.view.getFileSeparator();
      defaultFileName = this.view.getMainPfad() + fileSeparator + "Figuren";
      fileName = this.dateiOeffnen(defaultFileName, 'g');
      f = new File(fileName);
      if (!f.getName().toLowerCase().equals("robot0.gif")) {
         JOptionPane.showMessageDialog(null, "Es muss eine Bilddatei mit dem Namen\n robot0.gif ausgewählt werden", "Figur wechseln", 0);
         return "";
      } else if (this.fileExist(f.getParent() + fileSeparator + "robot1.gif")
         && this.fileExist(f.getParent() + fileSeparator + "robot2.gif")
         && this.fileExist(f.getParent() + fileSeparator + "robot3.gif")) {
         return f.getParent();
      } else {
         JOptionPane.showMessageDialog(
            null, "Es müssen die vier Bilddateien\nrobot0.gif, robot1.gif, robot2.gif und robot3.gif\nim selben Verzeichnis vorliegen.", "", 0
         );
         return "";
      }
   }

   public void copyImage(Image img) {
      DlgOpenSaveFile.ImageTransferable it = new DlgOpenSaveFile.ImageTransferable(img);
      Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
      clip.setContents(it, it);
   }

   public void printImage(BufferedImage img, JPanel jPanel) {
      PrinterJob prnJob = PrinterJob.getPrinterJob();
      prnJob.defaultPage().setOrientation(1);
      prnJob.setPrintable(new DlgOpenSaveFile.ImagePrintable(img));
      if (prnJob.printDialog()) {
         try {
            jPanel.setCursor(Cursor.getPredefinedCursor(3));
            prnJob.print();
            jPanel.setCursor(Cursor.getPredefinedCursor(0));
         } catch (PrinterException var5) {
            var5.printStackTrace();
            JOptionPane.showMessageDialog(null, "Drucken nicht möglich.\n Druckfehler: " + var5.toString(), "", 0);
         }
      }
   }

   class ImagePrintable implements Printable {
      int pageMax = 1;
      BufferedImage img;
      final int rand = 20;

      public ImagePrintable(BufferedImage img) {
         this.img = img;
      }

      @Override
      public int print(Graphics pg, PageFormat pageFormat, int pageIndex) {
         if (pageIndex < this.pageMax && this.img != null) {
            int wPage = (int)pageFormat.getImageableWidth() - 40;
            int hPage = (int)pageFormat.getImageableHeight() - 40;
            int w = this.img.getWidth();
            int h = this.img.getHeight();
            if (w != 0 && h != 0) {
               int nCol = Math.max((int)Math.ceil((double)w / (double)wPage), 1);
               int nRow = Math.max((int)Math.ceil((double)h / (double)hPage), 1);
               this.pageMax = nCol * nRow;
               int iCol = pageIndex % nCol;
               int iRow = pageIndex / nCol;
               int x = iCol * wPage;
               int y = iRow * hPage;
               int wImage = Math.min(wPage, w - x);
               int hImage = Math.min(hPage, h - y);
               pg.drawImage(this.img, 20, 20, wImage, hImage, x, y, x + wImage, y + hImage, null);
               System.gc();
               return 0;
            } else {
               return 1;
            }
         } else {
            return 1;
         }
      }
   }

   static class ImageTransferable implements Transferable, ClipboardOwner {
      Image image;

      ImageTransferable(Image img) {
         this.image = img;
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{DataFlavor.imageFlavor};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
         return DataFlavor.imageFlavor.equals(flavor);
      }

      @Override
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
         return this.image;
      }

      @Override
      public void lostOwnership(Clipboard clipboard, Transferable contents) {
      }
   }
}
