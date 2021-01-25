import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Menus {
    static String codigovip="12345";

    private static void menu() {
        System.out.println("\n\n###    Menu Inicial   ###");
        System.out.println("\n  =========================");
        System.out.println("    |     1 - Login         |");
        System.out.println("    |     2 - Registar      |");
        System.out.println("    |     0 - Sair          |");
        System.out.println("    =========================\n");
    }


    private static void menuLogin() {
        System.out.println("\n\n###     Menu Login    ###");
        System.out.println("\n  =========================");
        System.out.println("    |     1 - Move          |");
        System.out.println("    |     2 - Infetado      |");
        System.out.println("    |     3 - Nr Pessoas    |");
        System.out.println("    |     0 - Sair          |");
        System.out.println("    =========================\n");
    }

    private static void menuLoginVIP() {
        System.out.println("\n\n###     Menu Login    ###");
        System.out.println("\n  =========================");
        System.out.println("    |     1 - Move           |");
        System.out.println("    |     2 - Infetado       |");
        System.out.println("    |     3 - Nr Pessoas     |");
        System.out.println("    |     4 - Mapa infetados |");
        System.out.println("    |     0 - Sair           |");
        System.out.println("    =========================\n");
    }



    public static void inicial(Socket socket, BufferedReader in, PrintWriter out, BufferedReader systemIn) {
        try {
            String userInput;
            boolean quit = false;
            while (!quit) {
                menu();
                userInput = systemIn.readLine();
                switch (userInput) {
                    case "1": //login
                        caseLogin(in,out,systemIn);
                        break;
                    case "2": //registar
                        caseSignUp(in,out,systemIn);
                        break;
                    case "0": //quit
                        quit = true;
                        break;
                    default:
                        System.out.println("opcao invalida");
                        break;
                }
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void caseLogin(BufferedReader in, PrintWriter out, BufferedReader systemIn){
        try{
            String response;
            System.out.println("username:");
            String username = systemIn.readLine();
            System.out.println("pass:");
            String pass = systemIn.readLine();
            out.println("login " + username + " " + pass);
            out.flush();
            System.out.println("client antes da resposta");
            response = in.readLine(); // responde 0 para log in correto, ou 1 para username errado, 2 para pass errada, 3 infetado, 4 vip
            System.out.println("client depois da resposta" + response);
            switch (response) {
                case "login 1":
                    System.out.println("pass incorreta");
                    break;
                case "login 2":
                    System.out.println("nome inixistente");
                    break;
                case "login 3":
                    System.out.println("infetado");
                    break;
                case "login 4":
                    afterLoginVIP(in, out, systemIn,username);
                    break;
                case "login 0":
                    afterLogin(in, out, systemIn, username);
                    break;
                default:
                    System.out.println("opcao invalida" + response);
                    break;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void caseSignUp( BufferedReader in, PrintWriter out, BufferedReader systemIn){
        try {
            System.out.println("username:");
            String username = systemIn.readLine();
            System.out.println("pass:");
            String pass = systemIn.readLine();

            out.println("register " + username + " " + pass);
            out.flush();

            String response = in.readLine(); // responde 0 para registar correto, ou username ja existe

            switch (response) {
                case "register 0":
                    System.out.println("registo com sucesso");
                    System.out.println("E VIP ? (y or n)");
                    String isvip = systemIn.readLine();
                    if(isvip.equals("y") ){
                        System.out.println("Digite o codigo");
                        String code = systemIn.readLine();
                        if (code.equals(codigovip) ){
                            //StayAway.tornarVIP(username);
                            out.println("makeVIP " + username);
                            out.flush();
                            System.out.println("codigo correto");
                        }
                        else{
                            System.out.println("codigo incorreto, a a conta foi criada como utilizador normal");
                        }
                    }
                    else if(!isvip.equals("n")){
                        System.out.println("opcap invalida, a a conta foi criada como utilizador normal");
                    }
                    break;
                case "register 1":
                    System.out.println("username ja existe");
                    break;
                default:
                    System.out.println("opcao invalida");
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void move(BufferedReader in,PrintWriter out,BufferedReader systemIn,String username ){
        try {
            System.out.println("coord x:");
            int cx = Integer.parseInt(systemIn.readLine());
            System.out.println("coord y:");
            int cy = Integer.parseInt(systemIn.readLine());
            out.println("move " + username + " " + cx + " " + cy);
            out.flush();
            System.out.println("antes do while do move");
            while( in.readLine().equals("move 0") )//resposta: 0: nao pode mover, 1: moveu
            {
                System.out.println("pfv espera");
            }
            System.out.println("ja se pode mover");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void nrPessoas(BufferedReader in,PrintWriter out,BufferedReader systemIn,String username ){
        try {
            System.out.println("coord x:");
            int cx = Integer.parseInt(systemIn.readLine());
            System.out.println("coord y:");
            int cy = Integer.parseInt(systemIn.readLine());
            out.println("nrpeople " + username + " " + cx + " " + cy);
            out.flush();
            String response = in.readLine();
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void afterLogin(BufferedReader in,PrintWriter out,BufferedReader systemIn, String username){
        try {
            boolean quit = false;
            while (!quit) {
                menuLogin();
                String userInput = systemIn.readLine();
                switch (userInput) {
                    case "1"://move
                        move(in, out, systemIn,username);
                        break;
                    case "2"://infetado
                        out.println("infected " + username);
                        quit = true;
                        break;
                    case "3"://nrpessoas
                        nrPessoas(in, out, systemIn, username);
                        break;
                    case "0"://quit
                        quit = true;
                        break;
                    default:
                        System.out.println("opcao invalida");
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void afterLoginVIP(BufferedReader in,PrintWriter out,BufferedReader systemIn,String username){
        try {
            boolean quit = false;
            while (!quit) {
                menuLoginVIP();
                String userInput = systemIn.readLine();
                switch (userInput) {
                    case "1"://move
                        move(in, out, systemIn, username);
                        break;
                    case "2"://infetado
                        out.println("infected " + username);
                        quit = true;
                        break;
                    case "3"://nrpessoas
                        nrPessoas(in, out, systemIn, username);
                        break;
                    case "4": // mostra o mapa
                        out.println("mapa " + username);
                        String response = in.readLine();
                        System.out.println("infetados" + response);
                        response = in.readLine();
                        System.out.println("visitantes" + response);

                        break;
                    case "0"://quit
                        quit = true;
                        break;
                    default:
                        System.out.println("opcao invalida");
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }




}

