import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Client {


    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            FramedConnection fc = new FramedConnection(socket);

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            try{
                Menus.inicial(socket, fc, systemIn);
            }finally{
                System.out.println("conexao perdida");
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
