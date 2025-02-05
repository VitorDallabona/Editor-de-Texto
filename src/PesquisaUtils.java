import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PesquisaUtils {

    public static void pesquisarTexto(JTextField campoPesquisa, JTextPane areaTexto, JLabel rotuloOcorrencias) {
        String textoBusca = campoPesquisa.getText(); // pega o texto do arquivo
        if (!textoBusca.isEmpty()) {
            try {
                Highlighter realce = areaTexto.getHighlighter();
                realce.removeAllHighlights();

                String conteudo = areaTexto.getText();
                // verifica se o texto bate com o que foi pesquisado
                Matcher matcher = Pattern.compile(Pattern.quote(textoBusca), Pattern.CASE_INSENSITIVE).matcher(conteudo);
                int contador = 0;

                // se bate com a pesquisa, destaca e aumenta o contador
                while (matcher.find()) {
                    realce.addHighlight(matcher.start(), matcher.end(), new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW));
                    contador++;
                }

                // atualiza o rótulo de ocorrências
                rotuloOcorrencias.setText("Palavras: " + contador);

                // botão para limpar marcações
                JButton botaoLimpar = new JButton("Limpar Marcações");
                botaoLimpar.addActionListener(e -> limparMarcacoes(areaTexto, rotuloOcorrencias));
                
                JOptionPane.showMessageDialog(null, botaoLimpar, "Resultado da Pesquisa", JOptionPane.PLAIN_MESSAGE);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else {
            limparMarcacoes(areaTexto, rotuloOcorrencias);
        }
    }
    
    public static void limparMarcacoes(JTextPane areaTexto, JLabel rotuloOcorrencias) {
        Highlighter realce = areaTexto.getHighlighter();
        realce.removeAllHighlights(); // remove as marcações
        rotuloOcorrencias.setText("Palavras: 0"); // seta novamente o contador pra zero
    }
}
