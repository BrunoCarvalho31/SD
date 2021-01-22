public class User {

    private String nome;
    private String pass;
    private int x;
    private int y;
    private boolean doente;
    private boolean vip;

    public User (String nome, String pass, int x, int y, boolean doente, boolean vip){
        this.nome = nome;
        this.pass = pass;
        this.x = x;
        this.y = y;
        this.doente = doente;
        this.vip = vip;
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

    public boolean isVIP() {
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




}
