import Exceptions.NomeExistenteException;
import Exceptions.NomeNaoExisteException;
import Exceptions.PassIncorretaException;
import Exceptions.UtilizadorInfetadoException;

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

    public ReadWriteLock getReadWriteLock(){
        return this.lockSA;
    }

    public void register (String nome, String pass) throws NomeExistenteException{
        //pw.println("registar " + nome + " " + pass);
        //pw.flush();
        try {
            this.lockSA.writeLock().lock();
            if(users.containsKey(nome))
                throw new NomeExistenteException("Nome já existe!");
            else{
                User u = new User(nome,pass,0,0,false);
                this.users.put(nome,u);
            }
        }
        finally {
            this.lockSA.writeLock().unlock();
        }
    }

    public void login (String nome, String pass) throws NomeNaoExisteException, PassIncorretaException, UtilizadorInfetadoException {
        try{
            this.lockSA.readLock().lock();
            if(users.get(nome) == null)
                throw new NomeNaoExisteException("Nome nao encontrado.");
            else if(users.get(pass) == null)
                throw new PassIncorretaException("Password incorreta.");
            else if(users.get(nome).isDoente() == true)
                throw new UtilizadorInfetadoException("Encontra-se infetado");
        }
        finally {
            this.lockSA.readLock().unlock();
        }
    }

    public int numeroPessoasLocalizacao(int x, int y){
        int count = 0;
        for(User u: this.users.values()){
            if (u.getX() == x && u.getY() == y)
                count++;
        }
        return count;
    }

    public void novaLocalizacaoAtual(int x, int y, String nome){
        User u = this.users.get(nome);
        u.setX(x);
        u.setY(y);
    }

    // dar intencao de se mover para a posicao x,y, receve notificacao de quando estiver vazia
    public void goTo(int x , int y, String nome){
    }

    public void getMapa(){
    }

    public boolean isVIP(String username) {
        return this.users.get(username).VIP();
    }


    public static void tornarVIP(String username){
        this.users.get(username).setVIP(true);
    }

    public void move(String user, int x, int y) {
        if(numeroPessoasLocalizacao(x,y) > 0){
            //wait
        }else{
            novaLocalizacaoAtual(x,y,user);
        }

    }

    public void logOut(String user) {
    }

    public void infected(String user) {
        //notificar de infeçao
    }
}
