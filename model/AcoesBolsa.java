package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Classe que representa o modelo de ações da bolsa de valores.
 * Implementa a interface Subject para notificar os observadores sobre mudanças nos preços.
 */

public class AcoesBolsa implements Subject {
    private List<Observer> observers;
    private Map<String, Double> precoAcoes;
    private Random random;

    public AcoesBolsa() {
        this.observers = new ArrayList<>();
        this.precoAcoes = new HashMap<>();
        this.random = new Random();
        
        // Inicializa com algumas ações de exemplo
        precoAcoes.put("PETR4", 28.44);
        precoAcoes.put("VALE3", 68.12);
        precoAcoes.put("ITUB4", 32.56);
        precoAcoes.put("BBDC4", 18.77);
        precoAcoes.put("MGLU3", 4.23);
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Map.Entry<String, Double> acao : precoAcoes.entrySet()) {
            for (Observer observer : observers) {
                observer.update(acao.getKey(), acao.getValue());
            }
        }
    }
    
    /**
     * Adiciona uma nova ação para monitoramento.
     * @param codigo Código da ação
     * @param precoInicial Preço inicial da ação
     */
    public void adicionarAcao(String codigo, double precoInicial) {
        precoAcoes.put(codigo, precoInicial);
        for (Observer observer : observers) {
            observer.update(codigo, precoInicial);
        }
    }
    
    /**
     * Remove uma ação do monitoramento.
     * @param codigo Código da ação a ser removida
     */
    public void removerAcao(String codigo) {
        precoAcoes.remove(codigo);
        // Notifica os observadores que a ação foi removida (com preço -1)
        for (Observer observer : observers) {
            observer.update(codigo, -1);
        }
    }
    
    /**
     * Simula uma mudança aleatória nos preços das ações.
     */
    public void atualizarPrecos() {
        for (String codigo : precoAcoes.keySet()) {
            double variacaoPercentual = (random.nextDouble() * 2 - 1) * 5; // Variação entre -5% e +5%
            double precoAtual = precoAcoes.get(codigo);
            double novoPreco = precoAtual * (1 + variacaoPercentual / 100);
            novoPreco = Math.round(novoPreco * 100.0) / 100.0; // Arredonda para 2 casas decimais
            precoAcoes.put(codigo, novoPreco);
        }
        notifyObservers();
    }
    
    /**
     * Retorna o preço atual de uma ação.
     * @param codigo Código da ação
     * @return O preço atual ou -1 se a ação não estiver na lista
     */
    public double getPrecoAcao(String codigo) {
        return precoAcoes.getOrDefault(codigo, -1.0);
    }
    
    /**
     * Retorna um Map com todas as ações e seus preços
     * @return Map com códigos e preços das ações
     */
    public Map<String, Double> getTodasAcoes() {
        return new HashMap<>(precoAcoes);
    }
}
