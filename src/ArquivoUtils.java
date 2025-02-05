import javax.swing.*;
import java.io.*;

public class ArquivoUtils {

    private static File arquivoAtual;

    public static void abrirArquivo(JFileChooser seletorArquivo, JTextPane areaTexto, JLabel rotuloArquivo) {
        int retorno = seletorArquivo.showOpenDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            arquivoAtual = seletorArquivo.getSelectedFile();
            rotuloArquivo.setText("Aberto: " + arquivoAtual.getName());
            
            try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoAtual))) {
                StringBuilder conteudo = new StringBuilder();
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    conteudo.append(linha).append("\n");
                }
                areaTexto.setText(conteudo.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao abrir o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void salvarArquivo(JFileChooser seletorArquivo, JTextPane areaTexto, JLabel rotuloArquivo) {
        if (arquivoAtual == null) {
            int retorno = seletorArquivo.showSaveDialog(null);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                arquivoAtual = seletorArquivo.getSelectedFile();
                rotuloArquivo.setText("Salvo: " + arquivoAtual.getName());
            } else {
                return;
            }
        }
        
        try (FileWriter escritor = new FileWriter(arquivoAtual)) {
            escritor.write(areaTexto.getText());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void salvarNovamente(JTextPane areaTexto, JLabel rotuloArquivo) {
        if (arquivoAtual != null) {
            try (FileWriter escritor = new FileWriter(arquivoAtual)) {
                escritor.write(areaTexto.getText());
                rotuloArquivo.setText("Salvo: " + arquivoAtual.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum arquivo aberto.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void fecharArquivo(JTextPane areaTexto, JLabel rotuloArquivo) {
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja salvar as alterações antes de fechar?", "Fechar Arquivo", JOptionPane.YES_NO_CANCEL_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {
            salvarNovamente(areaTexto, rotuloArquivo); // Salva as alterações
        } else if (resposta == JOptionPane.CANCEL_OPTION) {
            return; // Cancela o fechamento
        }
        
        arquivoAtual = null; // Fecha o arquivo
        rotuloArquivo.setText("Nenhum arquivo aberto.");
        areaTexto.setText(""); // Limpa a área de texto
    }
}