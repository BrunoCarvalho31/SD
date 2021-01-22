import Exceptions.NomeExistenteException;
import Exceptions.NomeNaoExisteException;
import Exceptions.PassIncorretaException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StayAway {

    // quase a certeza que os PW nao sao precisos por isso ta tudo comentado e depois apaga-se
    //private PrintWriter pw;
    private Map<String,User> users;
    private ReadWriteLock lockSA = new ReentrantReadWriteLock();

    public StayAway(){
        this.users = new HashMap<>();
    }

    public void register (String nome, String pass) throws NomeExistenteException{
        //pw.println("registar " + nome + " " + pass);
        //pw.flush();
        try {
            this.lockSA.writeLock().lock();
            if(users.containsKey(nome))
                throw new NomeExistenteException("Nome já existe!");
            else{
                User u = new User(nome,pass,0,0,false,false);
                this.users.put(nome,u);
            }
        }
        finally {
            this.lockSA.writeLock().unlock();
        }
    }

    public void login (String nome, String pass) throws NomeNaoExisteException, PassIncorretaException{
        try{
            this.lockSA.readLock().lock();
            if(users.get(nome) == null)
                throw new NomeNaoExisteException("Nome nao encontrado.");
            else if(users.get(pass) == null)
                throw new PassIncorretaException("Password incorreta.");
        }
        finally {
            this.lockSA.readLock().unlock();
        }
    }

    public int numeroPessoasLocalizacao(int x, int y){
        //
        int count = 0;
        return count;
    }

    public void novaLocalizacaoAtual(int x, int y, User user){
        //
    }

    // dar intencao de se mover para a posicao x,y, receve notificacao de quando estiver vazia
    public void goTo(int x , int y, User user){

    }

    //
    public void notificarInfecao()
    {

    }

    public void getMapa(){

    }


}
