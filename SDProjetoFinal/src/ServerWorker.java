import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWorker implements Runnable{
    private Socket socket;
    private StayAway sa;
    private String user;
    private ReadWriteLock rwlock;

    public ServerWorker(Socket socket, StayAway sa) {
        this.socket=socket;
        this.sa = sa;
        this.rwlock = sa.getReadWriteLock();
    }


    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String line;

            //main cicle
            while ((line = in.readLine()) != null) {
                String[] args = line.split("\s");   // em principeo isto separa a string em string novas com o delimitador de espaco
                                                    // é possivel que seja melhor em vez do espaco usar um simbolo como delimitador ou ser mesmo fancy e meter tipo \e (corresponde ao caracter do escape)
                switch (args[0]){
                    case "login":
                        login(args[1],args[2],out);
                        break;
                    case "register":
                        register(args[1],args[2],out);
                        break;
                    case "move":
                        move(args[1],args[2],args[3],out);
                        break;
                    case  "infected":
                        infected(args[1],out);
                        break;
                    case "nrpeople":
                        nrpeople(args[1],args[2],out);
                        break;
                    default :
                        break;
                }
            }

            this.sa.logOut(this.user);

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();;
        }
    }

    private void login(String username, String pass, PrintWriter out){
        try{
            boolean vip=sa.login(username,pass);
            if(vip)
            {
                out.print("0");    
            }
            else if(!vip)
            {
               out.print("4"); 
            }
            
        } catch(PassIncorretaException e) {
            System.out.println("1");
        
        } catch(NomeNaoExisteException e) {
            System.out.println("2");
        
        } catch(UtilizadorInfetadoException e) {
            System.out.println("3");
        }
    }

    private void register(String usarname, String pass, PrintWriter out){
        try{
            this.sa.register(username,pass);
            out.print("0");

        } catch(NomeExistenteException e) {
            System.out.println("1");
        
        }
    }

    private void move(String user, String x ,String y, PrintWriter out)
    {   // o move devolde um bool, se falso faz wait, se true faz signal all
        this.sa.move(username,Integer.parseInt(x),Integer.parseInt(y));
        out.print("0");
    }

    private void infected(String user,PrintWriter out)
    {
        this.sa.infected(username);
    }

    private void nrpeople(){
        this.sa.nrpeople();

    }



}