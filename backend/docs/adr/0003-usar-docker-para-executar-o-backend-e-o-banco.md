# ADR 0003: Usar Docker para executar o backend e o banco de dados

**Status:** aceito

**Contexto:** O backend em Java com Spring Boot depende de um banco PostgreSQL, e o projeto é desenvolvido por um grupo em máquinas diferentes. Sem padronização, cada integrante precisaria instalar e configurar o Java, o PostgreSQL e suas versões por conta própria, o que gera diferenças de ambiente e o clássico "na minha máquina funciona". O grupo também precisa de uma forma simples de subir o sistema completo para testes e apresentação.

**Decisão:** Usar o Docker para empacotar e executar o backend e o PostgreSQL em contêineres, orquestrados localmente com Docker Compose.

**Alternativas consideradas:**
- Instalar Java e PostgreSQL diretamente em cada máquina: descartado porque as versões e configurações divergem entre os integrantes e o passo a passo de instalação é longo e propenso a erro.
- Máquina virtual (por exemplo, VirtualBox): descartada por ser mais pesada, mais lenta para iniciar e mais difícil de versionar do que arquivos de configuração de contêiner.
- Banco hospedado na nuvem compartilhado entre o grupo: descartado porque todos passariam a depender da mesma instância, com risco de um integrante sobrescrever os dados do outro e de dependência de internet.

**Consequências:**
- Positivas: todos executam o mesmo ambiente (mesma versão de Java e de PostgreSQL) a partir de arquivos versionados no repositório; subir o sistema completo passa a ser um único comando (`docker compose up`); um integrante novo começa a trabalhar sem instalar o banco manualmente.
- Negativas: todos precisam instalar o Docker, que consome memória e disco e pode dar problemas em máquinas mais fracas ou em alguns sistemas operacionais; o grupo precisa aprender Dockerfile, Compose, volumes e redes; depurar dentro de contêineres é menos direto; é preciso configurar volumes para que os dados do banco não se percam ao recriar o contêiner.
