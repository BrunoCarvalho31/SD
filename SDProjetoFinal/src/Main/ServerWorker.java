package Main;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWorker implements Runnable{
    private Socket socket;
    private StayAway sa;

    public ServerWorker(Socket socket, StayAway sa) {
        this.socket=socket;
        this.sa = sa;
    }


    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            int sum=0;
            int n=0;
            String line;
            while ((line = in.readLine()) != null) {
                try {
                    sum+=Integer.parseInt(line);
                    n++;
                }catch(NumberFormatException e)
                {
                    ;
                }

                out.println(sum);
                out.flush();
            }
            int avg = sum/n;
            out.println("the average was: "+avg);
            out.flush();

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();;
        }
    }
}