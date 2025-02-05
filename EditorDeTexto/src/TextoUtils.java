import javax.swing.*;
import javax.swing.text.*;

public class TextoUtils {

    public static void alterarFonte(JTextPane areaTexto, String fonteEscolhida) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fim = areaTexto.getSelectionEnd();
    
        if (inicio != fim) {
            Style estilo = areaTexto.addStyle("", null);
            StyleConstants.setFontFamily(estilo, fonteEscolhida);
            doc.setCharacterAttributes(inicio, fim - inicio, estilo, false);
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

    public static void definirTamanhoFonte(JTextPane areaTexto, int tamanho) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fim = areaTexto.getSelectionEnd();

        if (inicio != fim) {
            Style estiloAplicar = areaTexto.addStyle("", null);
            StyleConstants.setFontSize(estiloAplicar, tamanho);
            doc.setCharacterAttributes(inicio, fim - inicio, estiloAplicar, false);
        }
    }

    public static void alterarTamanhoFonte(JTextPane areaTexto, int incremento) {
        StyledDocument doc = areaTexto.getStyledDocument();
        int inicio = areaTexto.getSelectionStart();
        int fim = areaTexto.getSelectionEnd();

        if (inicio != fim) {
            Element elemento = doc.getCharacterElement(inicio);
            AttributeSet atributos = elemento.getAttributes();
            int tamanhoAtual = StyleConstants.getFontSize(atributos);
            definirTamanhoFonte(areaTexto, tamanhoAtual + incremento);
        }
    }
}