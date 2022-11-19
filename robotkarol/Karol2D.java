package robotkarol;

import java.awt.Point;
import java.awt.image.BufferedImage;
import javakarol.WeltAnzeige2D;

public class Karol2D extends WeltAnzeige2D {
   public Karol2D(KarolWelt welt) {
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
   public Point p2ToWelt(int x, int y) {
      return super.p2ToWelt(x, y);
   }

   @Override
   public BufferedImage getZeichenflaeche() {
      return super.getZeichenflaeche();
   }
}
