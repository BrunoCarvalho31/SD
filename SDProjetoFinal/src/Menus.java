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


    static void inicial() {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            String response;
            boolean quit = false;
            int cx;
            int cy;

            menu();
            while (!quit) {
                // System.out.println("1  for  login , 2 sign upm 3 for quit");
                menu();
                userInput = systemIn.readLine();

                switch (userInput) {
                    case "1": //login
                        System.out.println("username:");
                        String username = systemIn.readLine();
                        System.out.println("pass:");
                        String pass = systemIn.readLine();
                        out.println("login " + username + " " + pass);
                        response = in.readLine(); // responde 0 para log in correto, ou 1 para username errado, 2 para pass errada

                        switch (response) {
                            case "1":
                                System.out.println("nome incorreto");
                                break;
                            case "2":
                                System.out.println("pass incorreta");
                                break;
                            case "0":
                                //entra no medu de estar loged in
                                boolean quit2 = false;
                                while (!quit2) {
                                    // System.out.println("1 move, 2 infetado,3 nr pessoas ,0 quit");
                                    menuLogin();
                                    userInput = systemIn.readLine();
                                    switch (userInput) {
                                        case "1"://move
                                            System.out.println("coord x:");
                                            cx = Integer.parseInt(systemIn.readLine());
                                            System.out.println("coord y:");
                                            cy = Integer.parseInt(systemIn.readLine());
                                            out.println("move " + username + " " + cx + " " + cy);
                                            response = in.readLine();
                                            while (response.equals("0"))//resposta: 0: nao pode mover, 1: moveu
                                            {
                                                System.out.println("pfv espera");
                                                response = in.readLine();
                                            }
                                            System.out.println("ja se pode mover");

                                            break;
                                        case "2"://infetado
                                            out.println("infected " + username);
                                            break;
                                        case "3"://nrpessoas
                                            System.out.println("coord x:");
                                            cx = Integer.parseInt(systemIn.readLine());
                                            System.out.println("coord y:");
                                            cy = Integer.parseInt(systemIn.readLine());
                                            out.println("nrpeople " + username + " " + cx + " " + cy);
                                            response = in.readLine();
                                            System.out.println(response);

                                            break;
                                        case "0"://quit
                                            quit2 = true;
                                            break;
                                        default:
                                            System.out.println("opcao invalida");
                                            break;
                                    }
                                }
                                break;
                            default:
                                System.out.println("opcao invalida");
                                break;
                        }
                        break;

                    case "2": //registar
                        System.out.println("username:");
                        username = systemIn.readLine();
                        System.out.println("pass:");
                        pass = systemIn.readLine();
                        out.println("2 " + username + " " + pass);

                        response = in.readLine(); // responde 0 para registar correto, ou username ja existe

                        switch (response) {
                            case "0":
                                System.out.println("registo com sucesso");
                                break;
                            case "1":
                                System.out.println("username ja existe");
                                break;
                            default:
                                System.out.println("opcao invalida");
                                break;
                        }
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
}
