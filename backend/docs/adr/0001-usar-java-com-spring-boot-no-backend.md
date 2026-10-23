# ADR 0001: Usar Java com Spring Boot no backend

**Status:** aceito

**Contexto:** O projeto acadêmico exige um servidor em Java. O grupo quer manter o sistema em uma única linguagem, orientada a objetos, para que todos consigam trabalhar em qualquer parte do backend. O backend fica em um repositório separado do frontend e precisa expor funcionalidades para o frontend web e acessar o banco de dados.

**Decisão:** Implementar o backend em Java, usando o framework Spring Boot, mantendo todo o código do servidor em orientação a objetos.

**Alternativas consideradas:**
- Java puro, sem framework (por exemplo, servidor HTTP embutido do JDK ou Servlets): descartado porque obrigaria a implementar manualmente roteamento, serialização e acesso a dados, aumentando o esforço sem ganho de aprendizado relevante para o projeto.
- Backend em outra linguagem (Node.js, Python): descartado porque o projeto exige servidor em Java e misturar linguagens dividiria o conhecimento do grupo.
- Outro framework Java (Jakarta EE, Quarkus, Micronaut): descartado por exigir mais configuração ou por serem menos conhecidos pelo grupo do que o Spring Boot.

**Consequências:**
- Positivas: atende ao requisito do servidor em Java; uma única linguagem no backend facilita a divisão de tarefas e a revisão de código; o Spring Boot oferece servidor embutido, injeção de dependências e integração com banco de dados prontos para uso.
- Negativas: o grupo precisa aprender as convenções e anotações do Spring Boot; o framework adiciona "mágica" de configuração que dificulta a depuração no início; o projeto fica mais pesado (memória e tempo de inicialização) do que uma solução mínima; o frontend em outra linguagem (JavaScript) significa que a integração entre os dois lados precisa ser projetada.
