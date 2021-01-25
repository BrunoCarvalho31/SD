import java.net.Socket;
import Exceptions.*;


public class ServerWorker implements Runnable{
    private final Socket socket;
    private final StayAway sa;


    private static final int N =10;

    public ServerWorker(Socket socket, StayAway sa) {
        this.socket=socket;
        this.sa = sa;
    }


    public void run() {
        try {
            FramedConnection fc = new FramedConnection(socket);
            String line= new String ( fc.receive() ) ;

            //main cicle
            while (line!=null) {
                String[] args = line.split(" ");   

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
                        break;
                    case "mapa":
                        getMapas(args[1],fc);
                        break;
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
                    fc.send("loginVIP".getBytes());
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

        try{
            while( !this.sa.move(Integer.parseInt(x),Integer.parseInt(y),user) ){
                fc.send("move 0".getBytes());
                Thread.sleep(500);
                //cond.await();
            }
            fc.send("move 1".getBytes());
            //cond.signalAll();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
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
                for (int i=0;i<N ;i++ ) {
                    for (int j=0;j<N ;j++ ) {
                        fc.send( (infec[i][j]+"").getBytes() );
                    }
                }

                int [][] vis = this.sa.getMapaVisitantes(nome);
                for (int i=0;i<N ;i++ ) {
                    for (int j=0;j<N ;j++ ) {
                        fc.send( (vis[i][j]+"").getBytes() );
                    }
                }

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