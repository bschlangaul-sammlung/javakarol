package robotkarol;

import javakarol.Roboter;

public class KarolRoboter extends Roboter {
   private int rucksackStart = 0;

   public KarolRoboter(int startX, int startY, char startBlickrichtung, KarolWelt inWelt) {
      super(startX, startY, startBlickrichtung, inWelt);
      this.setNotStandAlone();
   }

   public KarolRoboter(KarolWelt inWelt) {
      super(inWelt);
      this.setNotStandAlone();
   }

   @Override
   public String toString() {
      return "Karol";
   }

   @Override
   protected void Zeichnen() {
   }

   @Override
   public int getRucksackMaximum() {
      return super.getRucksackMaximum();
   }

   @Override
   public void setRucksackAnzahl(int wert) {
      super.setRucksackAnzahl(wert);
   }

   @Override
   public void setRucksackPruefung(boolean wert) {
      super.setRucksackPruefung(wert);
   }

   @Override
   public void setRucksackMaximum(int wert) {
      super.setRucksackMaximum(wert);
   }

   @Override
   public String getFehlerText() {
      return super.getFehlerText();
   }

   @Override
   public void clearFehlerText() {
      super.clearFehlerText();
   }

   public void Schritt(int n) {
      int i = 1;
      if (n <= 0) {
         n = 1;
      }

      while(this.getFehlerText().isEmpty() && i <= n) {
         super.Schritt();
         ++i;
      }
   }

   public void Hinlegen(int nf) {
      int i = 1;
      if (nf == 0) {
         nf = 1;
      }

      if (nf > 0) {
         while(this.getFehlerText().isEmpty() && i <= nf) {
            super.Hinlegen();
            ++i;
         }
      }

      if (nf < 0 && nf > -5) {
         super.Hinlegen(super.farben[-nf - 1]);
      }
   }

   public void Aufheben(int n) {
      int i = 1;
      if (n <= 0) {
         n = 1;
      }

      while(this.getFehlerText().isEmpty() && i <= n) {
         super.Aufheben();
         ++i;
      }
   }

   public void MarkeSetzen(int nf) {
      if (nf >= 0) {
         super.MarkeSetzen();
      }

      if (nf < 0 && nf > -6) {
         super.MarkeSetzen(super.farben[-nf - 1]);
      }
   }

   public void Warten(int zeit) {
      if (zeit <= 0) {
         zeit = 1000;
      }

      float dauer = (float)(0.0 + (double)zeit / 1000.0);
      super.Warten(dauer);
   }

   public boolean IstZiegel(int nf) {
      boolean ergeb = false;
      if (nf == 0) {
         ergeb = super.IstZiegel();
      }

      if (nf > 0 && this.AnzahlZiegelVorneGeben() == nf) {
         ergeb = true;
      }

      if (nf < 0 && nf > -5) {
         ergeb = super.IstZiegel(super.farben[-nf - 1]);
      }

      return ergeb;
   }

   public boolean IstMarke(int nf) {
      boolean ergeb = false;
      if (nf >= 0) {
         ergeb = super.IstMarke();
      }

      if (nf < 0 && nf > -5) {
         ergeb = super.IstMarke(super.farben[-nf - 1]);
      }

      return ergeb;
   }

   public boolean HatZiegel(int n) {
      boolean ergeb = false;
      if (n <= 0) {
         ergeb = super.HatZiegelImRucksack();
      }

      if (n > 0 && this.AnzahlZiegelRucksackGeben() == n) {
         ergeb = true;
      }

      return ergeb;
   }

   public int getRucksackStart() {
      return this.rucksackStart;
   }

   public void setRucksackStart(int wert) {
      this.rucksackStart = Math.min(Math.abs(wert), this.getRucksackMaximum());
   }

   @Override
   public void SprunghoeheSetzen(int neueHoehe) {
      super.SprunghoeheSetzen(Math.min(neueHoehe, 10));
   }
}
