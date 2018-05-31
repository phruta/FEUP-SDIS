# Distributed Backup Service

In this project you will develop a distributed backup service for a local area network (LAN). The idea is to use the free disk space of the computers in a LAN for backing up files in other computers in the same LAN. The service is provided by servers in an environment that is assumed cooperative (rather than hostile). Nevertheless, each server retains control over its own disks and, if needed, may reclaim the space it made available for backing up other computers' files.

## How to compile
O projeto pode ser compilado usando o comando javac:
javac Client\*.java files\*.java Server\*.java multicastChannels\*java protocol\*.java utils\*.java

Alternativamente pode ser o usado o eclipse para importar o projecto e compilá-lo automaticamente.


## How to run
Após compilar o projecto, abrir o terminal na pasta que contém os ficheiros ".class" (pasta bin no caso do eclipse, junto dos ficheiros java no caso da utilização do comando javac referido).
De seguida criar um ficheiro "\_Stub" para a comunicação rmi com o comando:
rmic "Caminho para a pasta que contém os ficheiros compilados PeerInterfaceImplementation"

No caso de ter compilado com eclipse:
1. Ir para pasta bin
2. escrever o comando rmic Server.PeerInterfaceImplementation

Após criado o "\_Stub" correr o seguinte comando na pasta que contém os ficheiros ".class" (pasta bin no caso do eclipse):
1. Windows: start rmiregistry
2. Linux: rmiregistry &

Depois de todos estes passos podemos correr os programas Peer e TestApp:
### Peer
Para correr um peer usar o comando:

java Server.Peer <version> <ID> <Access Point> <MC address> <MC port> <MDB address> <MDB port> <MDR address> <MDR port>

A falta de um dos argumentos causará um erro

### TestApp
Para correr o TestApp usar um destes comandos:
java Client.TestApp <Access Point> backup <file path> <replication degree>
java Client.TestApp <Access Point> restore <file path>
java Client.TestApp <Access Point> delete <file path>
java Client.TestApp <Access Point> reclaim <amount of space>
java Client.TestApp <Access Point> state
  
## Notas
Pastas como Databases, Chunks e RestoredFiles serão criadas durante a execução destes programas se quisermos dar reset a todas as informações do Peer<ID> é necessário apagar as mesmas.
  
O "File Path" necessários em alguns dos comandos pode ser relativo ou absoluto.

O argumento amount of space no caso do TestApp reclaim é indicador da capacidade/tamanho de disco que queremos que o Peer<ID> tenha.

## Projecto realizado por:
Rúben José da Silva Torres - up201405612


