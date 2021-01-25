import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Exceptions.*;
import java.io.*; 
import java.util.*;

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
            FramedConnection fc = new FramedConnection(socket);

            System.out.println("sou o novo thread");
            String line= new String ( fc.receive() ) ;

            //main cicle
            while (line!=null) {
                System.out.println("\n" + line + " esta foi a linha recebida");
                String[] args = line.split(" ");   // em principio isto separa a string em string novas com o delimitador de espaco
                                                    // é possivel que seja melhor em vez do espaco usar um simbolo como delimitador ou ser mesmo fancy e meter tipo \e (corresponde ao caracter do escape)
                
                switch (args[0]){
                    case "login":
                        login(args[1],args[2],fc);
                        break;
                    case "register":
                        register(args[1],args[2],fc);
                        break;
                    case "move":
                        move(args[1],args[2],args[3],fc);
                        break;
                    case  "infected":
                        infected(args[1],fc);
                        break;
                    case "nrpeople":
                        nrpeople(args[1],args[2],args[3],fc);
                        break;
                    case "makeVIP":
                        this.sa.tornarVIP(args[1]);
                    case "mapa":
                        getMapas(args[1],fc);
                    default :
                        break;
                }
                line= new String ( fc.receive() ) ;
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void login(String username, String pass, FramedConnection fc){
        try{
            try{
                sa.login(username,pass);
                boolean vip = sa.isVIP(username);
                if(vip)
                {
                    //out.println("login 4");
                    fc.send("login 4".getBytes());
                    //System.out.println("dentro do log4");
                    //out.flush();
                }
                else
                {
                    fc.send("login 0".getBytes());
                }

            } catch(PassIncorretaException e) {
               fc.send("login 1".getBytes());

            } catch(NomeNaoExisteException e) {
               fc.send("login 2".getBytes());

            } catch(UtilizadorInfetadoException e) {
               fc.send("login 3".getBytes());
            }
        } catch(Exception e){
            ;
        }
    }

    private void register(String username, String pass, FramedConnection fc){
        try{
            try{
                this.sa.register(username,pass);
                fc.send("register 0".getBytes());

            } catch(NomeExistenteException e) {
                fc.send("register 1".getBytes());
            } 
        }catch(Exception e)
        {
            ;
        }
    }

    private void move(String user, String x ,String y, FramedConnection fc)
    {   // o move devolde um bool, se falso faz wait, se true faz signal all

        //Condition cond = this.sa.getCond();
        //Lock l = this.sa.getLock();
        //l.lock();
        try{
            while( !this.sa.move(Integer.parseInt(x),Integer.parseInt(y),user) ){
                fc.send("move 0".getBytes());
                //out.println("move 0"); //isto acontece se NAO se puder mover
                //out.flush();
                System.out.println("antes do  wait");
                //cond.await();
                Thread.sleep(500);
            }
            fc.send("move 1".getBytes());
            //cond.signalAll();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }//finally
        //{
        //     l.unlock();
        //}
        
    }

    private void infected(String user,FramedConnection fc)
    {
        this.sa.infected(user);
    }
    
    private void nrpeople(String user, String cx, String cy, FramedConnection fc){
        try{
            try {
                int n = this.sa.numeroPessoasLocalizacao(user,Integer.parseInt(cx),Integer.parseInt(cy));
                fc.send(("exitem " + n + " pessoas nessa localizacao").getBytes()  );
            }catch(UtilizadorInfetadoException e) {
                fc.send("utilizador infetado".getBytes());
            //out.println("utilizador infetado");
            //out.flush();
            }
            
        }catch(Exception e){
            ;
        }
    }

    private void getMapas(String nome, FramedConnection fc)
    {   
        try{
            try{
                int [][] infec = this.sa.getMapaDoentes(nome);
                int [][] vis = this.sa.getMapaVisitantes(nome);
                fc.send( Arrays.toString(infec).getBytes());
                fc.send( Arrays.toString(vis).getBytes());

            }catch(UtilizadorInfetadoException e)
            {
                fc.send( "utilizador infetado".getBytes());
                fc.send( "".getBytes());
            }

        }catch(Exception e)
        {
            ;
        }
        
    }
}