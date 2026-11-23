# ADR 0003: Usar Docker para executar o backend e o banco de dados

**Status:** aceito

**Contexto:** O backend em Java com Spring Boot depende de um banco PostgreSQL, e o projeto é desenvolvido por um grupo em máquinas diferentes. Sem padronização, cada integrante precisaria instalar e configurar o Java, o PostgreSQL e suas versões por conta própria, o que gera diferenças de ambiente e o clássico “na minha máquina funciona”. O grupo também precisa de uma forma reproduzível de iniciar os serviços para testes e apresentação.

**Decisão:** Usar Docker para empacotar e executar o backend e o PostgreSQL em contêineres, orquestrados localmente com Docker Compose. O frontend também é executado em contêiner próprio, em repositório separado, e se integra ao backend pela rede Docker compartilhada configurada para o projeto.

**Alternativas consideradas:**
- Instalar Java e PostgreSQL diretamente em cada máquina: descartado porque as versões e configurações poderiam divergir entre os integrantes e o passo a passo de instalação seria mais longo.
- Máquina virtual (por exemplo, VirtualBox): descartada por ser mais pesada, mais lenta para iniciar e mais difícil de versionar do que arquivos de configuração de contêiner.
- Banco hospedado na nuvem compartilhado entre o grupo: não adotado como configuração padrão local, pois todos dependeriam da mesma instância e da conexão com a internet.

**Consequências:**
- **Positivas:** os serviços são executados em ambientes conteinerizados; as configurações de execução ficam versionadas; a inicialização e a integração entre os componentes ficam documentadas e podem ser reproduzidas em outras máquinas.
- **Negativas:** todos precisam instalar o Docker, que consome memória e disco; o grupo precisa conhecer Dockerfile, Compose, volumes e redes; a depuração dentro de contêineres pode ser menos direta; é necessário configurar volumes para preservar os dados do banco.
- **Operacionais:** backend e frontend são iniciados em etapas separadas, pois estão em repositórios distintos. Antes de iniciar o frontend, é necessário garantir a existência da rede Docker externa `finduhouse-net`, conforme descrito no README.
