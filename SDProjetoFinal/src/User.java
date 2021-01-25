import java.util.ArrayList;
import java.util.List;

public class User {

    private String nome;
    private String pass;
    private int x;
    private int y;
    private boolean doente;
    private boolean vip;
    private List <Integer> caminhoX;
    private List <Integer> caminhoY;

    public User (String nome,String pass,int x, int y,boolean vip){
        this.nome = nome;
        this.pass = pass;
        this.x = x;
        this.y = y;
        this.doente = false;
        this.vip = vip;
        this.caminhoX = new ArrayList<Integer>();
        this.caminhoY = new ArrayList<Integer>();
    }

    public boolean checkPass(String  pass){
        return this.pass.equals(pass);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean VIP() {
        return vip;
    }

    public void setVIP(boolean vip) {
        this.vip = vip;
    }

    public boolean isDoente() {
        return doente;
    }

    public void setDoente(boolean doente) {
        this.doente = doente;
    }

    public List<Integer> getCaminhoX() {
        return caminhoX;
    }

    public void setCaminhoX(List<Integer> caminhoX) {
        this.caminhoX = caminhoX;
    }

    public List<Integer> getCaminhoY() {
        return caminhoY;
    }

    public void setCaminhoY(List<Integer> caminhoY) {
        this.caminhoY = caminhoY;
    }

    public boolean isIn(int xx,  int yy){
        System.out.println("oq ue foi ao users " + xx +" " + yy +"%%" +this.x + " " + this.y + (xx==this.x && yy==this.y));
        return xx==this.x && yy==this.y;
    }

    public void move(int x, int y){
        this.caminhoX.add(x);
        this.caminhoY.add(y);
        this.x=x;
        this.y=y;
    }
}
