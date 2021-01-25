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
            if( users.containsKey(nome) )
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

            if(users.get(nome).equals(null) ){
                throw new NomeNaoExisteException("Nome nao encontrado.");
            }
            else if( !(users.get(nome).checkPass(pass) ) ){
                throw new PassIncorretaException("Password incorreta.");
            }
            else if(users.get(nome).isDoente() == true){
                throw new UtilizadorInfetadoException("Encontra-se infetado");
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }
    }

    public int numeroPessoasLocalizacao(String user, int x, int y) throws UtilizadorInfetadoException {
        int count;

        System.out.println("inicio do nrpessoas loc");
        try{
            this.lockSA.readLock().lock();
            if(this.users.get(user).isDoente())
            {
                throw(new UtilizadorInfetadoException("") );
            }
            count = 0;
            for(User u: this.users.values() ){
                if (u.isIn(x,y))
                    System.out.println(x +" " + y +"%%" +u.getX() + " " + u.getY() );
                    count++;
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }
        System.out.println("fim do nrpessoas loc");
        return count;
    }

    public void novaLocalizacaoAtual(int x, int y, String nome) throws UtilizadorInfetadoException {
        try{
                this.lockSA.writeLock().lock();
                if( this.users.get(nome).isDoente())  {
                    throw (new UtilizadorInfetadoException("") );
                }
                this.users.get(nome).move(x,y);
                notifyAll();
        }
        finally {
            this.lockSA.writeLock().unlock();
        }
    }

    // dar intencao de se mover para a posicao x,y, receve notificacao de quando estiver vazia
    public boolean move(int x , int y, String nome) throws UtilizadorInfetadoException {
        boolean r;
        System.out.println("inicio do move");
        try{
            this.lockSA.readLock().lock();
            if( this.users.get(nome).isDoente()) {
                throw (new UtilizadorInfetadoException("") );
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }


        if(numeroPessoasLocalizacao(nome,x,y)==0)
        {
            System.out.println("dentro do if do move");
            novaLocalizacaoAtual(x,y,nome);
            r = true;
        }
        else{
            r = false;
        }
        
        System.out.println("fim do move");
        return r;
    }

    public void getMapa(){

    }

    public boolean isVIP(String username) {
        return this.users.get(username).VIP();
    }

    public void tornarVIP(String username){
        this.users.get(username).setVIP(true);
    }

    public void infected(String user) {
        this.users.get(user).setDoente(true);
    }

    public boolean checkInfected(String user)
    {
        return this.users.get(user).isDoente();
    }
}
