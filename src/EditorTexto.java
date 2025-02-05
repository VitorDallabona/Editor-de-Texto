import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.undo.*;
import java.net.URL;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme; // tema

public class EditorTexto extends JFrame {
    private JTextPane areaTexto;
    private JLabel rotuloArquivo;
    private JLabel rotuloContadorPalavras;
    private JTextField campoPesquisa;
    private JLabel rotuloOcorrencias;
    private UndoManager gerenciadorDesfazer;
    private JFileChooser seletorArquivo;
    private Color corDestaquePadrao = Color.ORANGE;
    private JButton botaoCorDestaque;

    public static void main(String[] args) {
        try {
            // tema FlatLaf
            FlatCyanLightIJTheme.setup();
            
            // Personalização
            UIManager.put("Button.arc", 8); // cantos dos botões
            UIManager.put("Component.arc", 8); // cantos dos componentes
            UIManager.put("TextComponent.arc", 8); // canto dos textfields
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    
        SwingUtilities.invokeLater(() -> {
            EditorTexto editor = new EditorTexto();
            editor.setVisible(true);
        });
    }

    public EditorTexto() {
        setTitle("Editor de Texto");
        setSize(880, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Centralizar a janela
        setLocationRelativeTo(null);

        seletorArquivo = new JFileChooser();

        inicializarComponentes();

        gerenciadorDesfazer = new UndoManager(); 
        areaTexto.getDocument().addUndoableEditListener(e -> gerenciadorDesfazer.addEdit(e.getEdit()));

        JScrollPane painelRolagem = new JScrollPane(areaTexto);
        add(painelRolagem, BorderLayout.CENTER);

        configurarAtalhos();

        // menu superior
        JMenuBar barraMenu = criarBarraMenu();
        setJMenuBar(barraMenu);

        // Menu inferior
        JPanel painelInferior = new JPanel(new BorderLayout());
        rotuloArquivo = new JLabel("Nenhum arquivo carregado");
        rotuloContadorPalavras = new JLabel("Palavras: 0");

        painelInferior.add(rotuloArquivo, BorderLayout.WEST); // esquerda
        painelInferior.add(rotuloContadorPalavras, BorderLayout.EAST); // direita
        add(painelInferior, BorderLayout.SOUTH); 

        areaTexto.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                atualizarContadorPalavras();
            }
        });
    }

    private void atualizarContadorPalavras() {
        String texto = areaTexto.getText().trim(); // remove espaços em brnaco
        String[] palavras = texto.split("\\s+");
        rotuloContadorPalavras.setText("Palavras: " + (palavras[0].isEmpty() ? 0 : palavras.length));
    }

    private void inicializarComponentes() {
        areaTexto = new JTextPane();
        areaTexto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        areaTexto.setBackground(Color.WHITE);
        areaTexto.setForeground(Color.BLACK);
        configurarBarraFerramentas();
        configurarIconesAlinhamento();
    }

    private void configurarAtalhos() {
        InputMap inputMap = areaTexto.getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = areaTexto.getActionMap();

        // Atalhos para desfazer e refazer (Ctrl+Z e Ctrl+X)
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "Desfazer");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "Refazer");
        
        actionMap.put("Desfazer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UndoRedoUtils.desfazerAcao(gerenciadorDesfazer);
            }
        });
        actionMap.put("Refazer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UndoRedoUtils.refazerAcao(gerenciadorDesfazer);
            }
        });

        // Atalhos para aumentar e diminuir tamanho da fonte (Ctrl+'+' e Ctrl+'-')
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK), "AumentarFonte");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), "DiminuirFonte");
        
        actionMap.put("AumentarFonte", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TextoUtils.alterarTamanhoFonte(areaTexto, 2);
            }
        });
        actionMap.put("DiminuirFonte", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TextoUtils.alterarTamanhoFonte(areaTexto, -2);
            }
        });
    }

    private void abrirArquivo() {
        ArquivoUtils.abrirArquivo(seletorArquivo, areaTexto, rotuloArquivo);
    }
    
    private void salvarArquivo() {
        ArquivoUtils.salvarArquivo(seletorArquivo, areaTexto, rotuloArquivo);
    }

    private void alterarFonte() {
        String fonteEscolhida = "Serif";
        TextoUtils.alterarFonte(areaTexto, fonteEscolhida);
    }
    

    private JMenuBar criarBarraMenu() {
        JMenuBar barraMenu = new JMenuBar();
    
        // Menu Arquivo
        JMenu menuArquivo = new JMenu("Arquivo");
        menuArquivo.add(UIUtils.criarItemMenu("Abrir", e -> abrirArquivo()));
        menuArquivo.add(UIUtils.criarItemMenu("Salvar", e -> salvarArquivo()));
        barraMenu.add(menuArquivo);
    
        // Menu Editar
        JMenu menuEditar = new JMenu("Editar");
        menuEditar.add(UIUtils.criarItemMenu("Desfazer", e -> UndoRedoUtils.desfazerAcao(gerenciadorDesfazer)));
        menuEditar.add(UIUtils.criarItemMenu("Refazer", e -> UndoRedoUtils.refazerAcao(gerenciadorDesfazer)));
        barraMenu.add(menuEditar);
    
        // Menu Formatar
        JMenu menuFormatar = new JMenu("Formatar");
        menuFormatar.add(UIUtils.criarItemMenu("Alterar Fonte", e -> alterarFonte()));
        barraMenu.add(menuFormatar);
    
        return barraMenu;
    }

    private void configurarBarraFerramentas() {
        JPanel painelBarraFerramentas = new JPanel();
        painelBarraFerramentas.setLayout(new BoxLayout(painelBarraFerramentas, BoxLayout.Y_AXIS));

        // Primeira linha de ferramentas
        JToolBar barraFerramentasPrimeira = new JToolBar("Ferramentas de Texto", JToolBar.HORIZONTAL);
        barraFerramentasPrimeira.setFloatable(false);
        barraFerramentasPrimeira.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Segunda linha de ferramentas
        JToolBar barraFerramentasSegunda = new JToolBar("Ferramentas de Texto", JToolBar.HORIZONTAL);
        barraFerramentasSegunda.setFloatable(false);
        barraFerramentasSegunda.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Primeira linha
        barraFerramentasPrimeira.add(UIUtils.criarBotaoEstilizado(getClass(), "Negrito", "/icones/bold-text-option_icon-icons.com_73656.png", e -> EstiloUtils.aplicarEstiloTexto(areaTexto, StyleConstants.Bold)));
        barraFerramentasPrimeira.add(UIUtils.criarBotaoEstilizado(getClass(), "Itálico", "/icones/italic-text_icon-icons.com_71455.png", e -> EstiloUtils.aplicarEstiloTexto(areaTexto, StyleConstants.Italic)));
        barraFerramentasPrimeira.add(UIUtils.criarBotaoEstilizado(getClass(), "Sublinhado", "/icones/format_underline_icon_124960.png", e -> EstiloUtils.aplicarEstiloTexto(areaTexto, StyleConstants.Underline)));
        barraFerramentasPrimeira.addSeparator();

        // Seletor do tamanho da fonte
        JComboBox<Integer> seletorTamanhoFonte = new JComboBox<>(new Integer[]{10, 12, 14, 16, 18, 20, 24, 28}); // JComboBox --> junta um campo com uma lista do tipo drop-down
        seletorTamanhoFonte.setSelectedItem(12);
        seletorTamanhoFonte.putClientProperty("JComboBox.buttonStyle", "roundRect");
        seletorTamanhoFonte.setMaximumSize(new Dimension(100, 30));
        seletorTamanhoFonte.addActionListener(e -> TextoUtils.definirTamanhoFonte(areaTexto, (Integer) seletorTamanhoFonte.getSelectedItem()));
        
        barraFerramentasPrimeira.add(new JLabel("Tamanho: "));
        barraFerramentasPrimeira.add(seletorTamanhoFonte);
        barraFerramentasPrimeira.addSeparator();

        // Seletor de fonte
        JComboBox<String> seletorFonte = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        seletorFonte.setSelectedItem("Serif");
        seletorFonte.putClientProperty("JComboBox.buttonStyle", "roundRect");
        seletorFonte.setMaximumSize(new Dimension(200, 30));
        seletorFonte.addActionListener(e -> TextoUtils.alterarFonte(areaTexto, (String) seletorFonte.getSelectedItem()));
        barraFerramentasPrimeira.add(new JLabel("Fonte: "));
        barraFerramentasPrimeira.add(seletorFonte);
        barraFerramentasPrimeira.addSeparator();

        // botão de salvamente no arquivo
        barraFerramentasPrimeira.add(UIUtils.criarBotaoEstilizado(getClass(), "Salvar arquivo", "/icones/save_diskette_icon_176745.png", e -> ArquivoUtils.salvarArquivo(new JFileChooser(), areaTexto, rotuloArquivo)));
        barraFerramentasPrimeira.addSeparator();
        // botão de fechar o arquivo atual
        barraFerramentasPrimeira.add(UIUtils.criarBotaoEstilizado(getClass(), "Fechar arquivo", "/icones/4115230-cancel-close-cross-delete_114048.png", e -> ArquivoUtils.fecharArquivo(areaTexto, rotuloArquivo)));

        // Segunda linha
        adicionarBotaoCitacao(barraFerramentasSegunda);

        barraFerramentasSegunda.add(UIUtils.criarBotaoEstilizado(getClass(), "Destacar", "/icones/highlight_icon_237836.png", e -> DestaqueUtils.alternarMarcacao(areaTexto, corDestaquePadrao)));

        // Botão de cor de destaque
        botaoCorDestaque = UIUtils.criarBotaoEstilizado(getClass(), "Cor", "/icones/preferencesdesktopcolor_93547.png", e -> alternarCorDestaque());
        
        // Definir ícone inicial
        botaoCorDestaque.setIcon(UIUtils.criarIconeCorDestaque(corDestaquePadrao));
        
        barraFerramentasSegunda.add(botaoCorDestaque);
        barraFerramentasSegunda.addSeparator();

        adicionarBotoesAlinhamento(barraFerramentasSegunda);

        barraFerramentasSegunda.addSeparator();

        // Campo de pesquisa
        campoPesquisa = new JTextField(20);
        campoPesquisa.putClientProperty("JTextField.placeholderText", "Pesquisar...");
        campoPesquisa.putClientProperty("JTextField.showClearButton", true);
        campoPesquisa.setMaximumSize(new Dimension(340, 30));
        campoPesquisa.addActionListener(e -> PesquisaUtils.pesquisarTexto(campoPesquisa, areaTexto, rotuloOcorrencias));
        
        barraFerramentasSegunda.add(new JLabel("Pesquisar: "));
        barraFerramentasSegunda.add(campoPesquisa);
        
        // ocorrências
        barraFerramentasSegunda.addSeparator();
        rotuloOcorrencias = new JLabel("Palavras: 0");
        barraFerramentasSegunda.add(rotuloOcorrencias);

        // Adiciona a barra de ferramentas ao painel
        painelBarraFerramentas.add(barraFerramentasPrimeira);
        painelBarraFerramentas.add(barraFerramentasSegunda);

        // Adiciona painel de barra de ferramentas ao layout
        add(painelBarraFerramentas, BorderLayout.NORTH);
    }

    private void adicionarBotaoCitacao(JToolBar barraFerramentas) {
        // Botão de citação
        JButton botaoCitacao = criarBotaoEstilizado(
            "Citar", 
            "/icones/quote_icon_154946.png", 
            e -> EstiloUtils.inserirCitação(areaTexto)
        );
        
        // Adicionar à barra de ferramentas
        barraFerramentas.add(botaoCitacao);
    }


    private void adicionarBotoesAlinhamento(JToolBar barraFerramentasSegunda) {
        // Grupo de botões de alinhamento
        JPanel painelAlinhamento = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelAlinhamento.setBorder(BorderFactory.createTitledBorder("Alinhamento"));

        // Botões de alinhamento com ícones redimensionados
        JToggleButton botaoEsquerda = new JToggleButton(redimensionarIcone("/icones/text-align-left_icon-icons.com_48215.png", 24, 24));
        JToggleButton botaoCentro = new JToggleButton(redimensionarIcone("/icones/text-align-center_icon-icons.com_48217.png", 24, 24));
        JToggleButton botaoDireita = new JToggleButton(redimensionarIcone("/icones/text-align-right_icon-icons.com_48214.png", 24, 24));
        JToggleButton botaoJustificado = new JToggleButton(redimensionarIcone("/icones/text-align-justify_icon-icons.com_48216.png", 24, 24));

        // Configurar grupo de botões para seleção única
        ButtonGroup grupoAlinhamento = new ButtonGroup();
        grupoAlinhamento.add(botaoEsquerda);
        grupoAlinhamento.add(botaoCentro);
        grupoAlinhamento.add(botaoDireita);
        grupoAlinhamento.add(botaoJustificado);

        // Definir tooltips
        botaoEsquerda.setToolTipText("Alinhar à Esquerda");
        botaoCentro.setToolTipText("Alinhar ao Centro");
        botaoDireita.setToolTipText("Alinhar à Direita");
        botaoJustificado.setToolTipText("Alinhar Justificado");

        // Configurar ações de alinhamento
        botaoEsquerda.addActionListener(e -> aplicarAlinhamento(StyleConstants.ALIGN_LEFT));
        botaoCentro.addActionListener(e -> aplicarAlinhamento(StyleConstants.ALIGN_CENTER));
        botaoDireita.addActionListener(e -> aplicarAlinhamento(StyleConstants.ALIGN_RIGHT));
        botaoJustificado.addActionListener(e -> aplicarAlinhamento(StyleConstants.ALIGN_JUSTIFIED));

        // Definir alinhamento padrão
        botaoEsquerda.setSelected(true);

        // Personalizar aparência dos botões
        personalizarBotaoAlinhamento(botaoEsquerda);
        personalizarBotaoAlinhamento(botaoCentro);
        personalizarBotaoAlinhamento(botaoDireita);
        personalizarBotaoAlinhamento(botaoJustificado);
        
        // Adicionar botões à barra de ferramentas
        barraFerramentasSegunda.add(new JLabel("Alinhamento: "));
        barraFerramentasSegunda.add(botaoEsquerda);
        barraFerramentasSegunda.add(botaoCentro);
        barraFerramentasSegunda.add(botaoDireita);
        barraFerramentasSegunda.add(botaoJustificado);
    }

    private ImageIcon redimensionarIcone(String caminho, int largura, int altura) {
        try {
            URL url = getClass().getResource(caminho);
            if (url == null) {
                return criarIconeTexto(caminho, largura, altura);
            }
            
            ImageIcon iconOriginal = new ImageIcon(url);
            Image imgEscalada = iconOriginal.getImage().getScaledInstance(
                largura, 
                altura, 
                Image.SCALE_SMOOTH
            );
            return new ImageIcon(imgEscalada);
        } catch (Exception e) {
            return criarIconeTexto(caminho, largura, altura);
        }
    }

    // Método de fallback para ícones de texto
    private ImageIcon criarIconeTexto(String caminho, int largura, int altura) {
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagem.createGraphics();
        
        // Configurações de renderização
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Limpar fundo
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, largura, altura);
        g2d.setComposite(AlphaComposite.SrcOver);
        
        // Definir fonte e cor
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Definir texto baseado no caminho do ícone
        String texto = "";
        if (caminho.contains("left")) texto = "←";
        else if (caminho.contains("center")) texto = "•";
        else if (caminho.contains("right")) texto = "→";
        else if (caminho.contains("justify")) texto = "≡";
        
        // Centralizar texto
        FontMetrics fm = g2d.getFontMetrics();
        int larguraTexto = fm.stringWidth(texto);
        int x = (largura - larguraTexto) / 2;
        int y = altura * 2 / 3;
        
        g2d.drawString(texto, x, y);
        g2d.dispose();
        
        return new ImageIcon(imagem);
    }

    private void personalizarBotaoAlinhamento(JToggleButton botao) {
        botao.setPreferredSize(new Dimension(30, 30));
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setContentAreaFilled(true);
                botao.setBackground(new Color(230, 240, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                botao.setContentAreaFilled(false);
            }
        });
    }

    private void aplicarAlinhamento(int alinhamento) {
        StyledDocument doc = areaTexto.getStyledDocument();
        
        // Verificar se há texto selecionado
        int selecaoInicio = areaTexto.getSelectionStart();
        int selecaoFim = areaTexto.getSelectionEnd();
        
        // Se nenhum texto selecionado, aplicar no parágrafo atual
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

    // adicionar ícones de alinhamento
    private void configurarIconesAlinhamento() {
        try {
            // Definir ícones personalizados
            UIManager.put("ToggleButton.select", new Color(200, 220, 255)); // Cor de fundo quando selecionado
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton criarBotaoEstilizado(String texto, String caminhoIcone, ActionListener acao) {
        JButton botao = new JButton(texto);
        
        try {
            // Carregar ícone do classpath
            URL iconURL = getClass().getResource(caminhoIcone);
            if (iconURL != null) {
                ImageIcon iconeOriginal = new ImageIcon(iconURL);
                
                // Redimensionar ícone
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

    private void alternarCorDestaque() {
        // Cores predefinidas para seleção rápida
        Color[] coresRapidas = {
            Color.YELLOW, 
            Color.GREEN, 
            Color.CYAN, 
            Color.ORANGE, 
            Color.PINK
        };

        // painel personalizado de seleção de cor
        JPanel painelCores = new JPanel(new GridLayout(2, 1));
        
        // Painel de cores rápidas
        JPanel painelCoresRapidas = new JPanel(new FlowLayout());
        painelCoresRapidas.setBorder(BorderFactory.createTitledBorder("Cores Rápidas"));
        
        for (Color cor : coresRapidas) {
            JButton botaoCor = new JButton();
            botaoCor.setBackground(cor);
            botaoCor.setPreferredSize(new Dimension(30, 30));
            botaoCor.addActionListener(e -> {
                corDestaquePadrao = cor;
                JOptionPane.getRootFrame().dispose();
            });
            painelCoresRapidas.add(botaoCor);
        }

        // Botão para seleção de cor personalizada
        JButton botaoCorPersonalizada = new JButton("Escolher Cor");
        botaoCorPersonalizada.addActionListener(e -> {
            Color corSelecionada = JColorChooser.showDialog(
                this, 
                "Selecionar Cor de Destaque", 
                corDestaquePadrao
            );
            
            if (corSelecionada != null) {
                corDestaquePadrao = corSelecionada;
                JOptionPane.getRootFrame().dispose();
            }
        });

        painelCores.add(painelCoresRapidas);
        painelCores.add(botaoCorPersonalizada);

        // Mostrar painel de seleção
        int resultado = JOptionPane.showConfirmDialog(
            this, 
            painelCores, 
            "Selecionar Cor de Destaque", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        // Atualizar botão de destaque com a cor selecionada
        if (resultado == JOptionPane.OK_OPTION) {
            atualizarBotaoCorDestaque();
        }
    }

    // Método para atualizar o ícone do botão de destaque
    private void atualizarBotaoCorDestaque() {
        if (botaoCorDestaque != null) {
            // Cria um ícone com a cor de destaque atual
            botaoCorDestaque.setIcon(UIUtils.criarIconeCorDestaque(corDestaquePadrao));
        }
    }

    public abstract class SimpleDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }
    
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }
    
        public void changedUpdate(DocumentEvent e) {
            update(e);
        }
    
        public abstract void update(DocumentEvent e);
    }
}
