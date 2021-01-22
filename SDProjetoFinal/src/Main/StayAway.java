package Main;

import Exceptions.NomeExistenteException;
import Exceptions.NomeNaoExisteException;
import Exceptions.PassIncorretaException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


public class StayAway {

    private PrintWriter pw;
    private Map<String,User> users;
    private ReentrantLock lockSA = new ReentrantLock();

    public StayAway(){
        this.users = new HashMap<>();
    }

    public User registar (String nome, String pass) throws NomeExistenteException{
        pw.println("registar " + nome + " " + pass);
        pw.flush();
        try {
            this.lockSA.lock();
            if(users.containsKey(nome))
                throw new NomeExistenteException("Nome já existe!");
            else{
                User u = new User(nome,pass);
                this.users.put(nome,u);
                return u;
            }
        }
        finally {
            this.lockSA.unlock();
        }
    }

    public void login (String nome, String pass) throws NomeNaoExisteException, PassIncorretaException{
        try{
            this.lockSA.lock();
            if(users.get(nome) == null)
                throw new NomeNaoExisteException("Nome nao encontrado.");
            if(users.get(pass) == null)
                throw new PassIncorretaException("Password incorreta.");
        }
        finally {
            this.lockSA.unlock();
        }
    }



}
