# coopvote
Sistema para gerenciar sessões de votação de uma cooperativa

## Apresentação

Foi desenvolvida uma API REST com a linguagem Java 11, Maven e framework Spring Boot 2.6.6. Foi escolhida a base de dados PostgreSQL 14.0. A documentação da API foi feita seguindo a especificação OpenAPI 3.0.3. A arquitetura procurou seguir os conceitos da Arquitetura Limpa, objetivando isolar o domínio das demais camadas da aplicação.
- Para testar a API acesse a documentação: https://ga-coopvote.herokuapp.com/api/docs.html

## Fluxo da aplicação
1. Deve-se cadastrar uma nova pauta fornecendo uma descrição para a mesma.

2. Após, deve-se abrir uma sessão de votação para a pauta criada no item 1, fornecendo ou não o tempo de duração da sessão em minutos. Caso não seja fornecido tempo, o padrão é de 1 min.

3. Assim, enquanto a sessão de votação estiver aberta, o associado pode enviar seu voto, fornecendo CPF válido e o valor do voto que pode ser YES ou NO.

4. Pode-se consultar o resultado da votação da pauta criada.

5. No momento do encerramento da sessão de votação, a API envia uma notificação com o resultado da votação para uma API de consumo, na qual pode-se visualizar todos os registros de resultados.

## Bônus

### Integração com sistemas externos
Não houve necessidade de implementar a verificação de CPF no sistema externo, pois a própria aplicação já consegue validar os CPFs dos associados no momento do voto.

### Mensageria e filas
A aplicação utilizou o RabbitMQ para realizar a comunicação entre sistemas. Com o objetivo de verificar o envio da notificação, foi criada uma API de consumo simples. Os detalhes estão na documentação.
- Link do código fonte da API de consumo: https://github.com/gustavogavila/coopvote-consumer
- Para testar a API de consumo, acesse a documentação no link abaixo: https://ga-coopvote-consumer.herokuapp.com/api/docs.html

### Performance
Para que o sistema possa atender à várias requisições ao mesmo tempo, seria interessante subir várias instâncias da aplicação e utilizar balanceadores de carga, escalando horizontalmente.

Para medir a performance da aplicação desenvolvida, pode-se utilizar o Apache JMeter através de testes de carga e investigar possíveis causas de lentidão, como alto custo de consultas ao banco de dados causado por falta de índices nas tabelas, por exemplo. Procurar diminuir as consultas ao banco de dados através de consultas mais inteligentes com JOIN. Adicionar algum provedor de cache como o Redis seria interessante para lidar com dados menos mutáveis da aplicação.

### Versionamento da API
Em relação ao versionamento da API, pode-se fazer através do media type, onde o consumidor da API informa a versão no cabeçalho “Accept” da requisição. Pode-se fazer também adicionando o número da versão na URI dos recursos.
