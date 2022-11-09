# ChatP2P
Projeto de criação de um ChatP2P na linguagem Java que implemente as funções de Broadcasting 

**Desenvolver um aplicativo de troca de mensagens instantâneas (chat) P2P com as seguintes 
funcionalidades:**

* Descoberta de usuários on-line na mesma sub-rede através do algoritmo:
  * Envia datagramas UDP de sonda contendo o seu nome de usuário e status para todos os endereços da sub-rede através do endereço de broadcast da sub-rede. Quando algum endereço enviar também uma pacote de sonda, cadastrar ele na lista de usuários on-line, com o seu devido status;
  * Cada cliente de chat ao receber uma pacote de radar (sonda), deve: 
    * cadastrar o usuário recebido na sua lista de usuários on-line, se ele já não existir;
    * atualizar o status do usuário;
    * ignorar mensagens de radar recebidas de si próprio;
  * Os clientes deverão implementar uma função onde são enviadas mensagens de radar periódicos (cada 5s) em broadcast. Sempre que ficar sem receber mensagens de radar de um usuário por tempo superior a 30s, este usuário deve ser retirado da lista de usuários on-line.
* Para qualquer usuário on-line é possível abrir um diálogo de chat. Este diálogo deve ser confiável e com entrega garantida.
* É possível manter sessões de chat simultâneas com vários usuários através de janelas diferentes.
* Quando houver perda de conexão com o usuário de chat, ou se um dos lados fechar a sua janela, deverá ser dado um aviso ao outro usuário e automaticamente fechada a janela  daquela sessão.
* Para implementação da interface pode ser utilizado o esqueleto em Java-Swing fornecido.
* As mensagens de radar devem ser enviadas pela porta 8080, e as sessões de chat devem  ser abertas na porta 8081.

LAYOUT DAS MENSAGEMS:
* Mensagem de SONDA (apresentação)
{ 
“tipoMensagem”: “sonda”,
“usuário”: “<nomeusuario>”,
“status”: “<status>”
} 

**ATENÇÃO: a entrega/apresentação do trabalho será feita impreterivelmente até 21/11/22**
