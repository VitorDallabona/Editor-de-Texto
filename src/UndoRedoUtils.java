import javax.swing.undo.UndoManager;

public class UndoRedoUtils {

    // Método para desfazer a ação
    public static void desfazerAcao(UndoManager gerenciadorDesfazer) {
        if (gerenciadorDesfazer.canUndo()) { // se possível desfazer
            gerenciadorDesfazer.undo(); // desfaz
        }
    }
    
    // Método para refazer a ação
    public static void refazerAcao(UndoManager gerenciadorDesfazer) {
        if (gerenciadorDesfazer.canRedo()) { // se possível refazer
            gerenciadorDesfazer.redo(); // refaz
        }
    }
}
