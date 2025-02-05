import javax.swing.*;
import javax.swing.text.*;

public class EstiloUtils {

    public static void aplicarAlinhamento(JTextPane areaTexto, int alinhamento) {
        StyledDocument doc = areaTexto.getStyledDocument();
        
        // Verifica se há texto selecionado
        int selecaoInicio = areaTexto.getSelectionStart();
        int selecaoFim = areaTexto.getSelectionEnd();
        
        // Se nenhum texto foi selecionado, aplica no parágrafo atual
        if (selecaoInicio == selecaoFim) {
            Element paragrafo = doc.getParagraphElement(areaTexto.getCaretPosition());
            selecaoInicio = paragrafo.getStartOffset();
            selecaoFim = paragrafo.getEndOffset();
        }
        
        // Criar estilo de parágrafo
        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attrs, alinhamento);
        
        // Aplicar alinhamento
        doc.setParagraphAttributes(selecaoInicio, selecaoFim - selecaoInicio, attrs, false);
    }

    public static void inserirCitação(JTextPane areaTexto) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int selecaoInicio = areaTexto.getSelectionStart();
        int selecaoFim = areaTexto.getSelectionEnd();
    
        // Verifica se há texto selecionado
        if (selecaoInicio == selecaoFim) {
            JOptionPane.showMessageDialog(
                null, 
                "Selecione o texto que deseja citar.", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
    
        try {
            // Extrair texto selecionado
            String textoSelecionado = doc.getText(selecaoInicio, selecaoFim - selecaoInicio);
            // Criar estilos para citação
            Style estiloCitacao = doc.addStyle("Citacao", null);
            // Define a fonte pra itálico
            StyleConstants.setItalic(estiloCitacao, true);
            // Define um recuo à esquerda
            SimpleAttributeSet atributosParagrafo = new SimpleAttributeSet();
            StyleConstants.setLeftIndent(atributosParagrafo, 20f);
            StyleConstants.setRightIndent(atributosParagrafo, 20f);
            
            // Remover seleção atual
            doc.remove(selecaoInicio, selecaoFim - selecaoInicio);
            // Inserir aspas de citação
            doc.insertString(selecaoInicio, "\"", null);
            // Inserir texto com estilo de citação
            doc.insertString(selecaoInicio + 1, textoSelecionado, estiloCitacao);
            // Inserir fechamento de aspas
            doc.insertString(selecaoInicio + 1 + textoSelecionado.length(), "\"", null);
            // Aplicar recuo ao parágrafo
            doc.setParagraphAttributes(selecaoInicio, textoSelecionado.length() + 2, atributosParagrafo, false);
        } catch (BadLocationException e) { // acesso de uma localização indevida
            e.printStackTrace();
        }
    }

    public static void aplicarEstiloTexto(JTextPane areaTexto, Object estilo) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fim = areaTexto.getSelectionEnd();

        if (inicio != fim) {
            Style estiloAplicar = areaTexto.addStyle("", null);
            StyleConstants.setBold(estiloAplicar, StyleConstants.Bold.equals(estilo));
            StyleConstants.setItalic(estiloAplicar, StyleConstants.Italic.equals(estilo));
            StyleConstants.setUnderline(estiloAplicar, StyleConstants.Underline.equals(estilo));

            doc.setCharacterAttributes(inicio, fim - inicio, estiloAplicar, false);
        }
    }

}
