public class User {

    private String nome;
    private String pass;
    //fazer getters e setters;
    private int x;
    private int y;
    private boolean doente;
    private boolean VIP;

    public User (String nome, String pass){
        this.nome = nome;
        this.pass = pass;
    }

    public String getNome() {
        return nome;
    }

    public String getPass() {
        return pass;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
