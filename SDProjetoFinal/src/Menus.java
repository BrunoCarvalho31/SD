import java.io.BufferedReader;
import java.net.Socket;



public class Menus {
    static String codigovip="12345";

    private static final int N=10;

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



    public static void inicial(Socket socket, FramedConnection fc, BufferedReader systemIn) {
        try {
            String userInput;
            boolean quit = false;
            while (!quit) {
                menu();
                userInput = systemIn.readLine();
                switch (userInput) {
                    case "1": //login
                        caseLogin(fc,systemIn);
                        break;
                    case "2": //registar
                        caseSignUp(fc,systemIn);
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


    public static void caseLogin(FramedConnection fc, BufferedReader systemIn){
        try{
            String response;
            System.out.println("username:");
            String username = systemIn.readLine();
            System.out.println("pass:");
            String pass = systemIn.readLine();

            fc.send(("login " + username + " " + pass).getBytes() );

            byte[] b1 = fc.receive();
            response = new String(b1);
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
                case "loginVIP":
                    afterLoginVIP(fc, systemIn,username);
                    break;
                case "login 0":
                    afterLogin(fc, systemIn, username);
                    break;
                default:
                    System.out.println("opcao invalida" + response);
                    break;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void caseSignUp( FramedConnection fc, BufferedReader systemIn){
        try {
            System.out.println("username:");
            String username = systemIn.readLine();
            System.out.println("pass:");
            String pass = systemIn.readLine();

            fc.send(("register " + username + " " + pass).getBytes() );

            byte[] b1 = fc.receive();
            String response = new String(b1);


            switch (response) {
                case "register 0":
                    System.out.println("registo com sucesso");
                    System.out.println("E VIP ? (y or n)");
                    String isvip = systemIn.readLine();
                    if(isvip.equals("y") ){
                        System.out.println("Digite o codigo");
                        String code = systemIn.readLine();
                        if (code.equals(codigovip) ){
  
                            fc.send( ("makeVIP " + username ).getBytes() );

                            System.out.println("codigo correto");
                        }
                        else{
                            System.out.println("codigo incorreto, a a conta foi criada como utilizador normal");
                        }
                    }
                    else if(!isvip.equals("n")){
                        System.out.println("opcao invalida, a a conta foi criada como utilizador normal");
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


    public static void move(FramedConnection fc,BufferedReader systemIn,String username ){
        try {
            System.out.println("coord x:");
            int cx = Integer.parseInt(systemIn.readLine());
            System.out.println("coord y:");
            int cy = Integer.parseInt(systemIn.readLine());

            fc.send( ("move "+username+" "+ cx + " " + cy).getBytes() );
            while( (new String ( fc.receive() ) ).equals("move 0") )//resposta: 0: nao pode mover, 1: moveu
            {
                ;
            }
            System.out.println("ja se pode mover");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void nrPessoas(FramedConnection fc,BufferedReader systemIn,String username ){
        try {
            System.out.println("coord x:");
            int cx = Integer.parseInt(systemIn.readLine());
            System.out.println("coord y:");
            int cy = Integer.parseInt(systemIn.readLine());

            fc.send(("nrpeople "+username+" "+ cx + " " + cy).getBytes() );

            String response = (new String ( fc.receive() ) );
            System.out.println(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void afterLogin(FramedConnection fc,BufferedReader systemIn, String username){
        try {
            boolean quit = false;
            while (!quit) {
                menuLogin();
                String userInput = systemIn.readLine();
                switch (userInput) {
                    case "1"://move
                        move(fc, systemIn,username);
                        break;
                    case "2"://infetado
                        fc.send(("infected " + username).getBytes() );
                        quit = true;
                        break;
                    case "3"://nrpessoas
                        nrPessoas(fc, systemIn, username);
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


    public static void afterLoginVIP(FramedConnection fc,BufferedReader systemIn,String username){
        try {
            boolean quit = false;
            while (!quit) {
                menuLoginVIP();
                String userInput = systemIn.readLine();
                switch (userInput) {
                    case "1"://move
                        move(fc, systemIn, username);
                        break;
                    case "2"://infetado
                        fc.send(("infected "+ username).getBytes() );

                        quit = true;
                        break;
                    case "3"://nrpessoas
                        nrPessoas(fc, systemIn, username);
                        break;
                    case "4": // mostra o mapa

                        fc.send(("mapa "+username).getBytes() );

                        int [][] infec = new int[N][N];
                        System.out.println("infetados");
                        for (int i=0;i<N ;i++ ) {
                            for (int j=0;j<N ;j++ ) {
                                infec[i][j]=Integer.parseInt(new String(fc.receive()));
                                System.out.print(infec[i][j]+" ");
                            }
                            System.out.print("\n");
                        }
                        System.out.println("Visitados");
                        int [][] vis = new int[N][N];
                        for (int i=0;i<N ;i++ ) {
                            for (int j=0;j<N ;j++ ) {
                                vis[i][j]=Integer.parseInt(new String(fc.receive()));
                                System.out.print(vis[i][j]+" ");
                            }
                            System.out.print("\n");
                        }

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

