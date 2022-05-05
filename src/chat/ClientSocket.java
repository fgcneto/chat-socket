package chat;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

/**
 * @author Neto
 * Configurações do Cliente do Server socket
 */
public class ClientSocket {

    private final Socket conexao;
    private final BufferedReader in;
    private final PrintWriter out;

    public ClientSocket(Socket conexao) throws IOException { // armazenar o socket
        this.conexao = conexao;
        this.in = new BufferedReader(new InputStreamReader(conexao.getInputStream())); //servidor
        this.out = new PrintWriter(conexao.getOutputStream(), true); // cliente
        System.out.println("Cliente conectou ao servidor "+ conexao.getRemoteSocketAddress());
    }

    // retorna a identificação do usuário (ip)
    public SocketAddress getRemoteSocketAddress() {
        return conexao.getRemoteSocketAddress();
    }

    public String getMessage(){
        try {
            return in.readLine(); // método de leitura da msg
        } catch (IOException e) {
            return null;
        }
    }

    // envio da msg
    public boolean sendMsg(String msg){
        out.println(msg);
        return !out.checkError(); // confirmação do envio
    }

    //fecha todos os canais abertos
    public void close(){
        try {
            in.close();
            out.close();
            conexao.close();
        } catch (IOException e) {
            System.out.println("Erro close");
        }
    }
}
