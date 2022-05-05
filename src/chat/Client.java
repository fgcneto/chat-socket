package chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Neto
 * Configurações do Cliente do Server socket
 */
public class Client implements Runnable{

    private Socket conexao;
    private Scanner scanner;
    private ClientSocket clientSocket;

    public Client() {
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException {
        try{
            conexao = new Socket(Server.HOST, Server.PORT);
            clientSocket = new ClientSocket(conexao);
            new Thread(this).start(); // cria a thread
            messageLoop();
        }finally {
            conexao.close();
        }
    }

    @Override
    public void run() {
        String msg;
        while ((msg = clientSocket.getMessage()) != null) {
            System.out.println(msg);
        }
    }

    private void messageLoop() throws IOException {
        String msg;
        do {
            System.out.print("Digite sua mensagem ou /exit para sair quando desejar) \n");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg); // envia  a menssagem
        } while (!msg.equalsIgnoreCase("/exit"));
    }
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (IOException e) {
            System.out.println("Erro no cliente" + e.getMessage());
        }
        System.out.println("Cliente finalizado");
    }
}
