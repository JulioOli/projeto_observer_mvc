package view;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Observer;

/**
 * Classe que representa a view para exibir os preços das ações em uma tabela.
 * Implementa a interface Observer para receber atualizações do modelo.
 */
public class AcoesView extends JFrame implements Observer {
    private DefaultTableModel tableModel;
    private JTable tabela;
    private Map<String, Double> precoAnterior;
    private JButton btnAdicionarAcao;
    private JButton btnRemoverAcao;
    private JButton btnAtualizarPrecos;
    // Mapas para controlar as células que devem ser destacadas
    private Map<Integer, Color> linhasCores;
    private Timer flashTimer;

    public AcoesView() {
        super("Monitor de Ações - Bolsa de Valores");
        precoAnterior = new HashMap<>();
        linhasCores = new HashMap<>();

        // Configura a tabela
        String[] colunas = {"Código", "Preço Atual", "Variação"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(tableModel);
        tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(2).setPreferredWidth(100);
        
        // Configurar o renderizador personalizado
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                // Se a linha está no mapa de cores, aplica a cor
                if (linhasCores.containsKey(row)) {
                    c.setBackground(linhasCores.get(row));
                    // Ajusta a cor do texto para melhor contraste
                    if (linhasCores.get(row).equals(Color.RED) || 
                        linhasCores.get(row).equals(Color.GREEN)) {
                        c.setForeground(Color.WHITE);
                    }
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                    c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                }
                
                return c;
            }
        });
        
        // Configura o timer de animação (piscar)
        flashTimer = new Timer(500, e -> {
            for (Integer row : linhasCores.keySet()) {
                Color cor = linhasCores.get(row);
                // Alterna entre a cor e a cor padrão para criar efeito piscante
                if (cor.equals(Color.GREEN) || cor.equals(Color.RED)) {
                    linhasCores.put(row, new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 100));
                } else {
                    // Restaura a cor original com base na última cor (verde ou vermelho com transparência)
                    if (cor.getGreen() > cor.getRed()) {
                        linhasCores.put(row, Color.GREEN);
                    } else {
                        linhasCores.put(row, Color.RED);
                    }
                }
            }
            tabela.repaint(); // Força a tabela a redesenhar
        });
        flashTimer.start();

        // Configura a janela
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Adiciona a tabela em um JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        // Painel para os botões
        JPanel painelBotoes = new JPanel();
        btnAdicionarAcao = new JButton("Adicionar Ação");
        btnRemoverAcao = new JButton("Remover Ação");
        btnAtualizarPrecos = new JButton("Atualizar Preços");
        
        painelBotoes.add(btnAdicionarAcao);
        painelBotoes.add(btnRemoverAcao);
        painelBotoes.add(btnAtualizarPrecos);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void update(String acao, double preco) {
        SwingUtilities.invokeLater(() -> {
            // Verifica se a ação já está na tabela
            int row = findRowByCode(acao);
            
            if (preco == -1) {
                // Se o preço é -1, significa que a ação foi removida
                if (row != -1) {
                    tableModel.removeRow(row);
                    precoAnterior.remove(acao);
                    linhasCores.remove(row);
                    
                    // Atualiza os índices das cores para linhas que foram deslocadas
                    Map<Integer, Color> novasCores = new HashMap<>();
                    for (Map.Entry<Integer, Color> entry : linhasCores.entrySet()) {
                        int indice = entry.getKey();
                        if (indice > row) {
                            novasCores.put(indice - 1, entry.getValue());
                        } else {
                            novasCores.put(indice, entry.getValue());
                        }
                    }
                    linhasCores = novasCores;
                }
                return;
            }
            
            // Calcula a variação percentual
            Double precoAnt = precoAnterior.getOrDefault(acao, preco);
            double variacao = 0.0;
            if (precoAnt > 0) {
                variacao = ((preco - precoAnt) / precoAnt) * 100;
            }
            String variacaoStr = String.format("%.2f%%", variacao);
            
            // Se já existe na tabela, atualiza
            if (row != -1) {
                tableModel.setValueAt(String.format("R$ %.2f", preco), row, 1);
                tableModel.setValueAt(variacaoStr, row, 2);
                
                // Define a cor da linha com base na variação
                if (preco > precoAnt) {
                    // Preço subiu - verde
                    linhasCores.put(row, Color.GREEN);
                } else if (preco < precoAnt) {
                    // Preço desceu - vermelho
                    linhasCores.put(row, Color.RED);
                }
                
                // Inicia o efeito de piscar por 3 segundos
                Timer linhaPiscaTimer = new Timer(3000, evt -> {
                    linhasCores.remove(row);
                    tabela.repaint();
                    ((Timer)evt.getSource()).stop(); // Para o timer após executar
                });
                linhaPiscaTimer.setRepeats(false);
                linhaPiscaTimer.start();
                
            } else {
                // Caso contrário, adiciona nova linha
                Object[] novaLinha = {acao, String.format("R$ %.2f", preco), variacaoStr};
                tableModel.addRow(novaLinha);
                // Linha nova não tem cor especial
            }
            
            // Atualiza o preço anterior
            precoAnterior.put(acao, preco);
        });
    }
    
    private int findRowByCode(String codigo) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(codigo)) {
                return i;
            }
        }
        return -1;
    }
    
    public JButton getBtnAdicionarAcao() {
        return btnAdicionarAcao;
    }
    
    public JButton getBtnRemoverAcao() {
        return btnRemoverAcao;
    }
    
    public JButton getBtnAtualizarPrecos() {
        return btnAtualizarPrecos;
    }
    
    public String getAcaoSelecionada() {
        int rowIndex = tabela.getSelectedRow();
        if (rowIndex != -1) {
            return (String) tableModel.getValueAt(rowIndex, 0);
        }
        return null;
    }
}
