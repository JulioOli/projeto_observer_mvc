package model;

/**
 * Interface Subject que define o comportamento para objetos que desejam
 * notificar os observadores sobre mudanças em seu estado.
 */
public interface Subject {
    /**
     * Registra (inscreve) um observador para receber notificações.
     * @param observer O observador a ser registrado
     */
    void registerObserver(Observer observer);
    
    /**
     * Remove (desinscreve) um observador para que não receba mais notificações.
     * @param observer O observador a ser removido
     */
    void removeObserver(Observer observer);
    
    /**
     * Notifica todos os observadores registrados sobre uma mudança de estado.
     */
    void notifyObservers();
}
