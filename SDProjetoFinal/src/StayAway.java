import Exceptions.NomeExistenteException;
import Exceptions.NomeNaoExisteException;
import Exceptions.PassIncorretaException;
import Exceptions.UtilizadorInfetadoException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.*;

public class StayAway {

    private static final int N = 10;
    private final Map<String,User> users;
    private final ReadWriteLock lockSA = new ReentrantReadWriteLock();


    public StayAway(){
        this.users = new HashMap<>();
    }

    public void register (String nome, String pass) throws NomeExistenteException{
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

            if( users.get(nome)==null ){
                throw new NomeNaoExisteException("Nome nao encontrado.");
            }
            else if( !(users.get(nome).checkPass(pass) ) ){
                throw new PassIncorretaException("Password incorreta.");
            }
            else if(users.get(nome).isDoente()){
                throw new UtilizadorInfetadoException("Encontra-se infetado");
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }
    }

    public int numeroPessoasLocalizacao(String user, int x, int y) throws UtilizadorInfetadoException {
        int count;

        try{
            this.lockSA.readLock().lock();
            if(this.users.get(user).isDoente())
            {
                throw(new UtilizadorInfetadoException("") );
            }
            count = 0;
            for(User u: this.users.values() ){
                if (u.isIn(x,y))
                {
                    count++;
                }
                    
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }
        return count;
    }

    public void novaLocalizacaoAtual(int x, int y, String nome) throws UtilizadorInfetadoException {
        try{
                this.lockSA.writeLock().lock();
                if( this.users.get(nome).isDoente())  {
                    throw (new UtilizadorInfetadoException("") );
                }
                this.users.get(nome).move(x,y);
        }
        finally {
            this.lockSA.writeLock().unlock();
        }
    }

    // dar intencao de se mover para a posicao x,y, receve notificacao de quando estiver vazia
    public boolean move(int x , int y, String nome) throws UtilizadorInfetadoException {
        boolean r;
        try{
            this.lockSA.readLock().lock();
            if( this.users.get(nome).isDoente()) {
                throw (new UtilizadorInfetadoException("") );
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }

        if(numeroPessoasLocalizacao(nome,x,y)==0 )
        {
            novaLocalizacaoAtual(x,y,nome);
            r = true;
        }
        else{
            r = false;
        }
        
        return r;
    }

    public int[][] getMapaDoentes(String nome) throws UtilizadorInfetadoException{
        int[][] matrizDoentes = new int[N][N];

        try{
            this.lockSA.readLock().lock();
            if( this.users.get(nome).isDoente()) {
                throw (new UtilizadorInfetadoException("") );
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }

        for(User u : this.users.values())
        {
            for(int i = 0;i<u.getCaminhoX().size();i++ )
            {
                if(u.isDoente())
                {
                    matrizDoentes[u.getCaminhoX().get(i)][u.getCaminhoY().get(i)]++;
                }
            } 
        }
        return matrizDoentes;
    }

    public int[][] getMapaVisitantes(String nome) throws UtilizadorInfetadoException{
        int[][] matrizVisitantes = new int[N][N];

        try{
            this.lockSA.readLock().lock();
            if( this.users.get(nome).isDoente()) {
                throw (new UtilizadorInfetadoException("") );
            }
        }
        finally {
            this.lockSA.readLock().unlock();
        }

        for(User u : this.users.values())
        {
            for(int i = 0;i<u.getCaminhoX().size();i++ )
            {
                matrizVisitantes[u.getCaminhoX().get(i)][u.getCaminhoY().get(i)]++;
            } 
        }
        return matrizVisitantes;
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
