import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class DestaqueUtils {
    // trocar a marcação do texto
    public static void alternarMarcacao(JTextPane areaTexto, Color corDestaquePadrao) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int selecaoInicio = areaTexto.getSelectionStart();
        int selecaoFim = areaTexto.getSelectionEnd();

        if (selecaoInicio == selecaoFim) { // se inicio == fim --> nenhum texto selecionado
            JOptionPane.showMessageDialog(
                null, 
                "Selecione o texto que deseja destacar.", 
                "Aviso", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        // Usa a cor de destaque passada como argumento
        Style estiloDestaque = doc.addStyle("Destaque", null);
        StyleConstants.setBackground(estiloDestaque, corDestaquePadrao);

        try {
            // Verificar se já está destacado
            boolean jaDestacado = verificarSeJaDestacado(doc, selecaoInicio, selecaoFim);

            if (jaDestacado) {
                // Remove destaque
                removerDestaque(doc, selecaoInicio, selecaoFim);
            } else {
                // Aplica o destaque com a cor atual
                doc.setCharacterAttributes(selecaoInicio, selecaoFim - selecaoInicio, estiloDestaque, false);
            }
        } catch (Exception e) {
            e.printStackTrace(); // diagnóstico do erro
        }
    }

    // remover o destaque no texto selecionado
    public static void removerDestaque(StyledDocument doc, int inicio, int fim) {
        SimpleAttributeSet atributosNormais = new SimpleAttributeSet();
        StyleConstants.setBackground(atributosNormais, Color.WHITE); // seta novamente o fundo pra branco
        doc.setCharacterAttributes(inicio, fim - inicio, atributosNormais, false);
    }

    public static boolean verificarSeJaDestacado(StyledDocument doc, int inicio, int fim) {
        for (int i = inicio; i < fim; i++) { // pra cada parte do texto selecionado
            Element elemento = doc.getCharacterElement(i);
            AttributeSet atributos = elemento.getAttributes();
            
            if (StyleConstants.getBackground(atributos) == Color.YELLOW) {
                return true;
            }
        }
        return false;
    }
}