import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Exceptions.*;

import java.util.concurrent.locks.*;

public class ServerWorker implements Runnable{
    private final Socket socket;
    private final StayAway sa;
    private String user;

    public ServerWorker(Socket socket, StayAway sa) {
        this.socket=socket;
        this.sa = sa;
    }


    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            System.out.println("sou o novo thread");
            String line;

            //main cicle
            while ((line = in.readLine()) != null) {
                System.out.println("\n" + line + " esta foi a linha recebida");
                String[] args = line.split(" ");   // em principio isto separa a string em string novas com o delimitador de espaco
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
                        nrpeople(args[1],args[2],args[3],out);
                        break;
                    case "makeVIP":
                        this.sa.tornarVIP(args[1]);
                    case "mapa":
                        getMapas(args[1],out);
                    default :
                        break;
                }
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void login(String username, String pass, PrintWriter out){
        try{
            sa.login(username,pass);
            boolean vip = sa.isVIP(username);
            if(vip)
            {
                out.println("login 4");
                System.out.println("dentro do log4");
                out.flush();
            }
            else
            {
               out.println("login 0");
               System.out.println("dentro do log0");
               out.flush();
            }

        } catch(PassIncorretaException e) {
            out.println("login 1");
            System.out.println("dentro do log1");
            out.flush();
        
        } catch(NomeNaoExisteException e) {
            out.println("login 2");
            System.out.println("dentro do log2");
            out.flush();

        } catch(UtilizadorInfetadoException e) {
            out.println("login 3");
            System.out.println("dentro do log3");
            out.flush();
        }
    }

    private void register(String username, String pass, PrintWriter out){
        try{
            this.sa.register(username,pass);
            out.println("register 0");
            out.flush();

        } catch(NomeExistenteException e) {
            out.println("register 1");
            out.flush();
        
        }
    }

    private void move(String user, String x ,String y, PrintWriter out)
    {   // o move devolde um bool, se falso faz wait, se true faz signal all

        Condition cond = this.sa.getCond();
        Lock l = this.sa.getLock();
        l.lock();
        try{
            while( !this.sa.move(Integer.parseInt(x),Integer.parseInt(y),user) ){
                out.println("move 0"); //isto acontece se NAO se puder mover
                out.flush();
                System.out.println("antes do  wait");
                cond.await();
            }
            out.println("move 1");
            out.flush();
            cond.signalAll();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally
        {
             l.unlock();
        }
        
    }

    private void infected(String user,PrintWriter out)
    {
        this.sa.infected(user);
    }
    
    private void nrpeople(String user, String cx, String cy, PrintWriter out){
        try {
            int n = this.sa.numeroPessoasLocalizacao(user,Integer.parseInt(cx),Integer.parseInt(cy));
            out.println("exitem " + n + " pessoas nessa localizacao");
            out.flush();
        }catch(UtilizadorInfetadoException e) {
            out.println("utilizador infetado");
            out.flush();
        }
    }

    private void getMapas(String nome, PrintWriter out)
    {   
        try{
            int [][] infec = this.sa.getMapaDoentes(nome);
            int [][] vis = this.sa.getMapaVisitantes(nome);
            out.println(infec);
            out.flush();
            out.println(vis);
            out.flush();    
        }catch(UtilizadorInfetadoException e)
        {
            out.println("utilizador infetado");
            out.flush();
            out.println("");
            out.flush();
        }
        
    }
}