import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class UIUtils {
    
    // método pra criar um item de menu
    public static JMenuItem criarItemMenu(String texto, ActionListener acao) {
        JMenuItem itemMenu = new JMenuItem(texto);
        itemMenu.addActionListener(acao);
        return itemMenu;
    }

    public static JButton criarBotaoEstilizado(Class<?> clazz, String texto, String caminhoIcone, ActionListener acao) {
        JButton botao = new JButton(texto);

        try {
            // Carrega o ícone do classpath usando a classe fornecida
            URL iconURL = clazz.getResource(caminhoIcone);
            if (iconURL != null) {
                ImageIcon iconeOriginal = new ImageIcon(iconURL);

                // Redimensiona o ícone
                Image imagemRedimensionada = iconeOriginal.getImage().getScaledInstance(
                    16,  // largura
                    16,  // altura
                    Image.SCALE_SMOOTH  // suavização
                );

                // Criar novo ícone redimensionado
                ImageIcon iconeRedimensionado = new ImageIcon(imagemRedimensionada);

                // Definir ícone no botão
                botao.setIcon(iconeRedimensionado);
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + e.getMessage());
        }

        // Configurações de estilo do botão
        botao.putClientProperty("JButton.buttonType", "roundRect");
        botao.putClientProperty("JButton.minimumWidth", 30);
        botao.putClientProperty("JButton.cornerRadius", 8);

        botao.setFocusPainted(false);
        botao.addActionListener(acao);

        return botao;
    }

    public static Icon criarIconeCorDestaque(Color cor) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(cor);
                g.fillRect(x, y, getIconWidth(), getIconHeight());
                g.setColor(Color.BLACK);
                g.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
            }

            @Override
            public int getIconWidth() {
                return 20;
            }

            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }
}
