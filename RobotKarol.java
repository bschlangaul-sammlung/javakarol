import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import robotkarol.KarolController;
import robotkarol.KarolProgram;
import robotkarol.KarolRoboter;
import robotkarol.KarolView;
import robotkarol.KarolWelt;

public class RobotKarol {
   private KarolWelt welt;
   private KarolRoboter karol;
   private KarolView mainFrame;
   private KarolProgram prog;
   private KarolController control;
   private String programPath = "";
   private String fileSeparator = "";
   private String homePath = "";
   static String propStr = "";
   static String progStr = "";
   static String weltStr = "";

   public RobotKarol() {
      this.welt = new KarolWelt(5, 10, 6);
      this.karol = new KarolRoboter(this.welt);
      this.mainFrame = new KarolView(this.welt);
      this.prog = new KarolProgram(this.welt, this.karol, this.mainFrame);
      this.control = new KarolController(this.welt, this.karol, this.mainFrame, this.prog);
      this.mainFrame.registerListener(this.control);
      this.programPath = System.getProperty("user.dir");
      this.fileSeparator = System.getProperty("file.separator");
      this.homePath = System.getProperty("user.home");
      this.welt.setDefaultWeltPfadSep(this.programPath, this.fileSeparator);
      this.prog.setDefaultProgPfadSep(this.programPath, this.fileSeparator);
      this.mainFrame.setMainPfad(this.programPath, this.fileSeparator);
      this.control.startProperties(propStr, progStr, weltStr, this.programPath, this.homePath, this.fileSeparator);
      this.mainFrame.pack();
      this.mainFrame.setVisible(true);
   }

   public static void main(String[] args) {
      for(String s : args) {
         if (!s.isEmpty() && s.startsWith("/I")) {
            propStr = s.substring(2, s.length());
         }

         if (!s.isEmpty() && s.startsWith("/P")) {
            progStr = s.substring(2, s.length());
         }

         if (!s.isEmpty() && s.endsWith(".kdp") && progStr.isEmpty()) {
            progStr = s.substring(0, s.length());
         }

         if (!s.isEmpty() && s.startsWith("/W")) {
            weltStr = s.substring(2, s.length());
         }
      }

      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception var2) {
            }

            new RobotKarol();
         }
      });
   }
}
