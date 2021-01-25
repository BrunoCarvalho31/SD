import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//final?

public class Server{

    public static void main (String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12345);
        StayAway sa = new StayAway();

        while(true)
        {
            System.out.println("inicio do loop");
            Socket socket = ss.accept();
            System.out.println("socket aceite");
            Thread worker = new Thread(new ServerWorker(socket, sa));
            worker.start();
            System.out.println("worker started end of loop");

        }
    }
}