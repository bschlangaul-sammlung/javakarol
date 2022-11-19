package karoleditor;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class KEdtEditorKit extends StyledEditorKit {
   private JFrame frame;
   private JTextPane kitEditorPane;
   private KEdtDocument styledDoc;
   public static Font DEFAULT_FONT;
   public int TAB_LEN = 2;
   private DlgFindReplace dlgFindReplace;
   private DlgGeheZu dlgGeheZu;
   private DlgComplete dlgComplete;
   protected UndoManager undoManager = new UndoManager();
   protected KEdtEditorKit.UndoAction undoAction = new KEdtEditorKit.UndoAction();
   protected KEdtEditorKit.RedoAction redoAction = new KEdtEditorKit.RedoAction();

   public KEdtEditorKit(JFrame mainFrame) {
      this.frame = mainFrame;
      this.dlgFindReplace = new DlgFindReplace(this.frame);
      this.dlgGeheZu = new DlgGeheZu(this.frame);
   }

   @Override
   public void install(JEditorPane editorPane) {
      super.install(editorPane);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      String[] fonts = ge.getAvailableFontFamilyNames();
      Arrays.sort((Object[])fonts);
      if (Arrays.binarySearch(fonts, "Courier New") >= 0) {
         DEFAULT_FONT = new Font("Courier New", 0, 13);
      } else if (Arrays.binarySearch(fonts, "Courier") >= 0) {
         DEFAULT_FONT = new Font("Courier", 0, 13);
      } else if (Arrays.binarySearch(fonts, "Monospaced") >= 0) {
         DEFAULT_FONT = new Font("Monospaced", 0, 13);
      }

      editorPane.setFont(DEFAULT_FONT);
      editorPane.setCaretColor(Color.BLACK);
      editorPane.setSelectionColor(new Color(10079487));
      this.addActions(editorPane);
      this.kitEditorPane = (JTextPane)editorPane;
      this.dlgComplete = new DlgComplete(this.frame, this.kitEditorPane);
      this.kitEditorPane.setComponentPopupMenu(new KEdtPopUp(this.kitEditorPane, this.TAB_LEN));
   }

   @Override
   public String getContentType() {
      return "text/Karol";
   }

   @Override
   public Document createDefaultDocument() {
      this.styledDoc = new KEdtDocument();
      this.styledDoc.addUndoableEditListener(new KEdtEditorKit.MyUndoableEditListener());
      this.undoManager.setLimit(20);
      return this.styledDoc;
   }

   public void setTAB_LEN(int wert) {
      this.TAB_LEN = wert;
   }

   public int getTAB_LEN() {
      return this.TAB_LEN;
   }

   public void addActions(JEditorPane editorPane) {
      InputMap imap = new InputMap();
      imap.setParent(editorPane.getInputMap());
      ActionMap amap = new ActionMap();
      amap.setParent(editorPane.getActionMap());
      amap.put("undo", this.undoAction);
      amap.put("redo", this.redoAction);
      amap.put("find", new KEdtEditorKit.FindAction());
      amap.put("replace", new KEdtEditorKit.ReplaceAction());
      amap.put("find-next", new KEdtEditorKit.FindNextAction());
      amap.put("goto-line", new KEdtEditorKit.GotoLineAction());
      amap.put("doTab", new KEdtEditorKit.DoTabAction());
      amap.put("complete", new KEdtEditorKit.CompleteAction());
      amap.put("formatieren", new KEdtEditorKit.FormatAction());
      KeyStroke key = KeyStroke.getKeyStroke(9, 0);
      imap.put(key, "doTab");
      KeyStroke key2 = KeyStroke.getKeyStroke(32, 2);
      imap.put(key2, "complete");
      editorPane.setActionMap(amap);
      editorPane.setInputMap(0, imap);
   }

   class CompleteAction extends AbstractAction {
      public CompleteAction() {
         super("complete");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         int aktPos = KEdtEditorKit.this.kitEditorPane.getSelectionStart();
         if (KEdtEditorKit.this.styledDoc != null && KEdtEditorKit.this.dlgComplete != null) {
            KEdtToken token = KEdtEditorKit.this.styledDoc.getTokenAt(aktPos);
            String abk = new String("");
            if (token != null) {
               abk = KEdtEditorKit.this.styledDoc.getString(token);
               abk = abk.substring(0, aktPos - token.getStart());
            }

            KEdtEditorKit.this.kitEditorPane.setSelectionEnd(aktPos);
            KEdtEditorKit.this.dlgComplete.showDialog(abk);
         }
      }
   }

   class DoTabAction extends AbstractAction {
      public DoTabAction() {
         super("doTab");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         int pos = KEdtEditorKit.this.kitEditorPane.getCaret().getDot();
         String maxBlank = "                ";

         try {
            KEdtEditorKit.this.styledDoc.insertString(pos, maxBlank.substring(0, KEdtEditorKit.this.TAB_LEN), null);
            KEdtEditorKit.this.kitEditorPane.setCaretPosition(pos + KEdtEditorKit.this.TAB_LEN);
         } catch (BadLocationException var5) {
         }
      }
   }

   class FindAction extends AbstractAction {
      public FindAction() {
         super("find");
         this.putValue("ShortDescription", "Suchen");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         if (KEdtEditorKit.this.dlgFindReplace != null && KEdtEditorKit.this.dlgFindReplace.showModal(true)) {
            DlgFindReplace d = KEdtEditorKit.this.dlgFindReplace;
            if (d.getNurMarkiertOption()) {
               KEdtEditorKit.this.styledDoc
                  .doFindSelected(d.getSearchText(), d.getGrossOption(), d.getWortOption(), d.getVorwaertsOption(), KEdtEditorKit.this.kitEditorPane);
            } else if (d.getAbCursorOption()) {
               KEdtEditorKit.this.styledDoc
                  .doFindFromCursor(d.getSearchText(), d.getGrossOption(), d.getWortOption(), d.getVorwaertsOption(), KEdtEditorKit.this.kitEditorPane);
            } else {
               KEdtEditorKit.this.styledDoc
                  .doFindAll(d.getSearchText(), d.getGrossOption(), d.getWortOption(), d.getVorwaertsOption(), KEdtEditorKit.this.kitEditorPane);
            }
         }
      }
   }

   class FindNextAction extends AbstractAction {
      public FindNextAction() {
         super("find-next");
         this.putValue("ShortDescription", "Weitersuchen");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         KEdtEditorKit.this.styledDoc.doFindNext(KEdtEditorKit.this.kitEditorPane);
      }
   }

   class FormatAction extends AbstractAction {
      public FormatAction() {
         super("formatieren");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         KEdtEditorKit.this.styledDoc.formatieren(KEdtEditorKit.this.kitEditorPane, KEdtEditorKit.this.TAB_LEN);
      }
   }

   class GotoLineAction extends AbstractAction {
      public GotoLineAction() {
         super("goto-line");
         this.putValue("ShortDescription", "Gehe zu Zeile");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         int max = KEdtEditorKit.this.styledDoc.getLineCount();
         int akt = KEdtEditorKit.this.styledDoc.getLineNumberAt(KEdtEditorKit.this.kitEditorPane.getCaret().getDot());
         if (KEdtEditorKit.this.dlgGeheZu != null && KEdtEditorKit.this.dlgGeheZu.showModal(akt, max)) {
            int pos = KEdtEditorKit.this.styledDoc.getBeginOfLine(KEdtEditorKit.this.dlgGeheZu.getNeueZeile());
            KEdtEditorKit.this.kitEditorPane.setCaretPosition(pos);
         }
      }
   }

   protected class MyUndoableEditListener implements UndoableEditListener {
      @Override
      public void undoableEditHappened(UndoableEditEvent ue) {
         if (!ue.getEdit().getPresentationName().equals("Formatvorlagenänderung")) {
            KEdtEditorKit.this.undoManager.addEdit(ue.getEdit());
         }

         KEdtEditorKit.this.undoAction.updateState();
         KEdtEditorKit.this.redoAction.updateState();
      }
   }

   class RedoAction extends AbstractAction {
      public RedoAction() {
         super("redo");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         try {
            KEdtEditorKit.this.undoManager.redo();
         } catch (CannotUndoException var3) {
         }

         this.updateState();
         KEdtEditorKit.this.undoAction.updateState();
      }

      void updateState() {
         if (KEdtEditorKit.this.undoManager.canRedo()) {
            this.setEnabled(true);
         } else {
            this.setEnabled(false);
         }
      }
   }

   class ReplaceAction extends AbstractAction {
      public ReplaceAction() {
         super("replace");
         this.putValue("ShortDescription", "Ersetzen");
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         if (KEdtEditorKit.this.dlgFindReplace != null && KEdtEditorKit.this.dlgFindReplace.showModal(false)) {
            DlgFindReplace d = KEdtEditorKit.this.dlgFindReplace;
            if (d.getNurMarkiertOption()) {
               KEdtEditorKit.this.styledDoc
                  .doReplaceSelected(d.getSearchText(), d.getReplaceText(), d.getGrossOption(), d.getWortOption(), KEdtEditorKit.this.kitEditorPane);
            } else if (d.getAbCursorOption()) {
               KEdtEditorKit.this.styledDoc
                  .doReplaceFromCursor(d.getSearchText(), d.getReplaceText(), d.getGrossOption(), d.getWortOption(), KEdtEditorKit.this.kitEditorPane);
            } else {
               KEdtEditorKit.this.styledDoc.doReplaceAll(d.getSearchText(), d.getReplaceText(), d.getGrossOption(), d.getWortOption());
            }
         }
      }
   }

   class UndoAction extends AbstractAction {
      public UndoAction() {
         super("undo");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent ae) {
         try {
            KEdtEditorKit.this.undoManager.undo();
         } catch (CannotUndoException var3) {
         }

         this.updateState();
         KEdtEditorKit.this.redoAction.updateState();
      }

      void updateState() {
         if (KEdtEditorKit.this.undoManager.canUndo()) {
            this.setEnabled(true);
         } else {
            this.setEnabled(false);
         }
      }
   }
}
