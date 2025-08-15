# Monitor de Ações - Padrão Observer com MVC

Este projeto implementa um sistema de monitoramento de preços de ações utilizando o padrão de projeto Observer em conjunto com a arquitetura MVC (Model-View-Controller).

## Estrutura do Projeto

O projeto está organizado seguindo a arquitetura MVC:

### Model
- `Subject.java`: Interface que define o comportamento para objetos que notificam observadores.
- `Observer.java`: Interface que define o comportamento para objetos que desejam ser notificados.
- `AcoesBolsa.java`: Implementação concreta do modelo que mantém os dados das ações.

### View
- `AcoesView.java`: Implementa a interface gráfica e também atua como Observer, atualizando a tela conforme as mudanças no modelo.

### Controller
- `AcoesController.java`: Gerencia a interação entre o modelo e a view, tratando eventos da UI.

### Principal
- `MonitorAcoes.java`: Classe que inicializa o sistema.

## Funcionalidades

1. **Adicionar Ação**: Permite adicionar uma nova ação para monitoramento.
2. **Remover Ação**: Remove uma ação da lista de monitoramento.
3. **Atualizar Preços**: Atualiza os preços das ações manualmente.
4. **Atualização Automática**: Os preços são atualizados automaticamente a cada 5 segundos.

## Padrão Observer

O padrão Observer é implementado através das interfaces `Subject` e `Observer`. A classe `AcoesBolsa` (Subject) notifica a `AcoesView` (Observer) sempre que há alterações nos preços das ações.

## Como Executar

Para executar o programa, compile as classes e execute a classe `MonitorAcoes`:

```bash
javac -d out *.java model/*.java view/*.java controller/*.java
java -cp out MonitorAcoes
```

## Demonstração do Padrão Observer

1. **Inscrição (Subscription)**: Quando uma ação é adicionada, ela é inscrita no sistema de observação.
2. **Desinscrição (Unsubscription)**: Quando uma ação é removida, ela é desinscrita do sistema.
3. **Notificação (Notification)**: Quando os preços mudam, todos os observadores são notificados.
