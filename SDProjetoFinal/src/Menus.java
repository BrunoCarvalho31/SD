import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Menus {

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
        System.out.println("    |     3 - Mapa infetados |");
        System.out.println("    |     0 - Sair           |");
        System.out.println("    =========================\n");
    }



    public static void inicial(Socket socket, BufferedReader in, PrintWriter out, BufferedReader systemIn) {
        try {
            String userInput;
            boolean quit = false;
            int cx;
            int cy;
            menu();
            while (!quit) {
                menu();
                userInput = systemIn.readLine();
                switch (userInput) {
                    case "1": //login
                        caseLogin(socket,in,out,systemIn);
                        break;
                    case "2": //registar
                        caseSignUp(socket,in,out,systemIn);
                        break;
                    case "0": //quit
                        quit = true;
                        break;
                    default:
                        System.out.println("opcao invalida");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void caseLogin(Socket socket, BufferedReader in, PrintWriter out, BufferedReader systemIn){
        try{
            String response;
            int cx = 0;
            int cy = 0;

            System.out.println("username:");
            String username = systemIn.readLine();
            System.out.println("pass:");
            String pass = systemIn.readLine();
            out.println("login " + username + " " + pass);
            response = in.readLine(); // responde 0 para log in correto, ou 1 para username errado, 2 para pass errada, 3 infetado, 4 vip

            switch (response) {
                case "1":
                    System.out.println("nome incorreto");
                    break;
                case "2":
                    System.out.println("pass incorreta");
                    break;
                case "3":
                    System.out.println("infetado");
                    break;
                case "4":
                    afterLoginVIP(socket, in, out, systemIn,username);
                    break;
                case "0":
                    afterLogin(socket, in, out, systemIn, username);
                    break;
                default:
                    System.out.println("opcao invalida");
                    break;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void caseSignUp(Socket socket, BufferedReader in, PrintWriter out, BufferedReader systemIn){
        try {
            System.out.println("username:");
            String username = systemIn.readLine();
            System.out.println("pass:");
            String pass = systemIn.readLine();
            out.println("register " + username + " " + pass);

            String response = in.readLine(); // responde 0 para registar correto, ou username ja existe

            switch (response) {
                case "1":
                    System.out.println("registo com sucesso");
                    System.out.println("E VIP ? (y or n)");
                    String isvip = systemIn.readLine();
                    if(isvip == "y"){
                        System.out.println("Digite o codigo");
                        String code = systemIn.readLine();
                        String codigovip = "12345";
                        if (code == codigovip){
                            StayAway.tornarVIP(username);
                        }
                    }
                    break;
                case "2":
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


    public static void move(Socket socket,BufferedReader in,PrintWriter out,BufferedReader systemIn,String username ){
        try {
            System.out.println("coord x:");
            int cx = Integer.parseInt(systemIn.readLine());
            System.out.println("coord y:");
            int cy = Integer.parseInt(systemIn.readLine());
            out.println("move " + username + " " + cx + " " + cy);
            String response = in.readLine();
            while (response.equals("0"))//resposta: 0: nao pode mover, 1: moveu
            {
                System.out.println("pfv espera");
                response = in.readLine();
            }
            System.out.println("ja se pode mover");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void nrPessoas(Socket socket,BufferedReader in,PrintWriter out,BufferedReader systemIn,String username ){
        try {
            System.out.println("coord x:");
            int cx = Integer.parseInt(systemIn.readLine());
            System.out.println("coord y:");
            int cy = Integer.parseInt(systemIn.readLine());
            out.println("nrpeople " + username + " " + cx + " " + cy);
            String response = in.readLine();
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void afterLogin(Socket socket,BufferedReader in,PrintWriter out,BufferedReader systemIn, String username){
        try {
            boolean quit = false;
            while (!quit) {
                menuLogin();
                String userInput = systemIn.readLine();
                switch (userInput) {
                    case "1"://move
                        move(socket, in, out, systemIn,username);
                        break;
                    case "2"://infetado
                        out.println("infected " + username);
                        quit = true;
                        break;
                    case "3"://nrpessoas
                        nrPessoas(socket, in, out, systemIn, username);
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


    public static void afterLoginVIP(Socket socket,BufferedReader in,PrintWriter out,BufferedReader systemIn,String username){
        try {
            boolean quit = false;
            while (!quit) {
                menuLoginVIP();
                String userInput = systemIn.readLine();
                switch (userInput) {
                    case "1"://move
                        move(socket, in, out, systemIn, username);
                        break;
                    case "2"://infetado
                        out.println("infected " + username);
                        quit = true;
                        break;
                    case "3"://nrpessoas
                        nrPessoas(socket, in, out, systemIn, username);
                        break;
                    case "4": // mostra o mapa
                        out.println("mapa");
                        String response = in.readLine();
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

