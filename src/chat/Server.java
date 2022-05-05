package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Neto
 * Configurações do Servidor Socket
 */
public class Server {

    public static final int PORT = 7000;
    public static  final String HOST = "127.0.0.1";
    private ServerSocket serverSocket;
    // armazena os vários clientes
    private final List<ClientSocket> clients = new LinkedList<>();

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciando na porta: " + PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        while (true) {
            ClientSocket clientSocket = new ClientSocket(serverSocket.accept()); //retorna o socket
            clients.add(clientSocket);
            sendMsgToAll(clientSocket, "Entrou no chat");
            new Thread(() -> clientMessageLoop(clientSocket)).start();
        }
    }

    private void clientMessageLoop(ClientSocket clientSocket) {
        String msg;
        try {
            while ((msg = clientSocket.getMessage()) != null) { // atribui o valor a variável msg e tbm verifica se o valor n foi lido -- null
                if ("/exit".equalsIgnoreCase(msg)) { // verifica se o cliente quer finalizar a sessão
                    sendMsgToAll(clientSocket, ": Saiu" );
                    return;
                }
                System.out.println("Msg recebida do cliente " + clientSocket.getRemoteSocketAddress() + " : " + msg);
                sendMsgToAll(clientSocket, msg); //envia a msg para todos os clientes ativos
            }
        } finally {
            clientSocket.close(); // garante que todos os canais sejam fechados
        }
    }

    //envia a msg para todos os clientes
    private void sendMsgToAll(ClientSocket sender, String msg) {
        Iterator<ClientSocket> iterator = clients.iterator(); // percorre enquanto tiver elementos
        while (iterator.hasNext()) {
            ClientSocket clientSocket = iterator.next(); // obtem os elementos
            if (!sender.equals(clientSocket)) { // evita q a msg seja enviada para o remetente
                if (!clientSocket.sendMsg("["+ sender.getRemoteSocketAddress() + "]"+ " = " + msg)) {
                    iterator.remove(); // remove se nao foi possível enviar a msg
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start();
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o Servidor :)" + e.getMessage());
        }
        System.out.println("Servidor Parou ");
    }
}
