package model;

/**
 * Interface Observer que define o comportamento para objetos que desejam
 * ser notificados sobre mudanças em um Subject (Assunto).
 */
public interface Observer {
    /**
     * Método chamado quando o estado do Subject observado é alterado.
     * @param acao Nome da ação que teve alteração
     * @param preco Novo preço da ação
     */
    void update(String acao, double preco);
}
