package robotkarol;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javakarol.WeltAnzeige3D;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Karol3D extends WeltAnzeige3D {
   public Karol3D(KarolWelt welt) {
      super(welt);
   }

   @Override
   public void zeichneWelt() {
      super.zeichneWelt();
   }

   @Override
   public void zeichneWeltGanz() {
      super.zeichneWeltGanz();
   }

   @Override
   public void resetWeltAnzeige() {
      super.resetWeltAnzeige();
   }

   @Override
   public BufferedImage getZeichenflaeche() {
      return super.getZeichenflaeche();
   }

   @Override
   protected void imagesLaden() {
      this.ziegelImg[0] = this.imageLadenName("Ziegel_rot");
      this.ziegelImg[1] = this.imageLadenName("Ziegel_gelb");
      this.ziegelImg[2] = this.imageLadenName("Ziegel_blau");
      this.ziegelImg[3] = this.imageLadenName("Ziegel_gruen");
      this.markeImg[0] = this.imageLadenName("Marke_rot");
      this.markeImg[1] = this.imageLadenName("Marke_gelb");
      this.markeImg[2] = this.imageLadenName("Marke_blau");
      this.markeImg[3] = this.imageLadenName("Marke_gruen");
      this.markeImg[4] = this.imageLadenName("Marke_schwarz");
      this.quaderImg = this.imageLadenName("Quader");
      this.karolImg[0][0] = this.imageLadenName("robot0");
      this.karolImg[1][0] = this.imageLadenName("robot1");
      this.karolImg[2][0] = this.imageLadenName("robot2");
      this.karolImg[3][0] = this.imageLadenName("robot3");
   }

   public void neueFigurenLaden(String figurDateiPfad) {
      BufferedImage bi = null;

      for(int i = 0; i <= 3; ++i) {
         File f = new File(figurDateiPfad + "\\robot" + i + ".gif");

         try {
            bi = ImageIO.read(f);
         } catch (IOException var6) {
            JOptionPane.showMessageDialog(null, "Ein nötiges Image für die Figur kann nicht geladen werden.", "Wechsel der Roboter-Figur", 0);
            return;
         }

         if (bi.getHeight() > this.maxImageHoehe) {
            this.maxImageHoehe = bi.getHeight();
         }

         this.karolImg[i][0] = bi;
      }

      this.zeichneWeltGanz();
   }
}
