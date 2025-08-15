package controller;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import model.AcoesBolsa;
import view.AcoesView;

/**
 * Classe que representa o controlador do padrão MVC.
 * Responsável por coordenar a interação entre o modelo e a view.
 */
public class AcoesController {
    private final AcoesBolsa modelo;
    private final AcoesView view;
    private Timer timer;
    
    public AcoesController(AcoesBolsa modelo, AcoesView view) {
        this.modelo = modelo;
        this.view = view;
        
        // Registra a view como observadora do modelo
        modelo.registerObserver(view);
        
        // Inicializa os preços com os valores atuais
        modelo.notifyObservers();
        
        configurarEventos();
        iniciarAtualizacaoAutomatica();
    }
    
    private void configurarEventos() {
        // Evento do botão Adicionar Ação
        view.getBtnAdicionarAcao().addActionListener(e -> {
            String codigo = JOptionPane.showInputDialog(view, "Digite o código da ação:");
            if (codigo != null && !codigo.trim().isEmpty()) {
                codigo = codigo.toUpperCase();
                try {
                    String precoStr = JOptionPane.showInputDialog(view, "Digite o preço inicial da ação:");
                    if (precoStr != null && !precoStr.trim().isEmpty()) {
                        double preco = Double.parseDouble(precoStr.replace(",", "."));
                        modelo.adicionarAcao(codigo, preco);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Preço inválido. Use o formato: 99.99",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Evento do botão Remover Ação
        view.getBtnRemoverAcao().addActionListener(e -> {
            String acaoSelecionada = view.getAcaoSelecionada();
            if (acaoSelecionada != null) {
                modelo.removerAcao(acaoSelecionada);
            } else {
                JOptionPane.showMessageDialog(view, "Selecione uma ação para remover",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // Evento do botão Atualizar Preços
        view.getBtnAtualizarPrecos().addActionListener(e -> modelo.atualizarPrecos());
    }
    
    private void iniciarAtualizacaoAutomatica() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> modelo.atualizarPrecos());
            }
        }, 5000, 5000); // Atualiza a cada 5 segundos
    }
    
    public void finalizar() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
