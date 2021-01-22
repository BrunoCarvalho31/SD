import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Client {


    public static void main(String[] args) throws Exception{
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            boolean quit = false;
            while(!quit)
            {
                System.out.println("1  for  login , 2 sign upm 3 for quit");
                userInput = systemIn.readLine();

                switch(userInput){
                    case '1': //login
                    System.out.println("username:")
                    String username =  systemIn.readLine();
                    System.out.println("pass:")
                    String pass =  systemIn.readLine();
                    out.println("login " + username + " " + pass);
                        String response = in.readLine(); // responde 0 para log in correto, ou 1 para username errado, 2 para pass errada
                        
                        switch(response){
                            case '1':
                                System.out.println("nome incorreto");
                                break;
                            case '2':
                                System.out.println("pass incorreta");
                                break;
                            case '0':
                                //entra no medu de estar loged in
                                boolean quit2=false;
                                while(!quit2)
                                {
                                    System.out.println("1 move, 2 infetado,3 nr pessoas ,0 quit");

                                    userInput = systemIn.readLine();
                                    switch(userInput){
                                        case '1'://move
                                            System.out.println("coord x:")
                                            int coordx = Integer.parseInt( systemIn.readLine() );
                                            System.out.println("coord y:")
                                            int coordy = Integer.parseInt( systemIn.readLine() );
                                            out.println("move " + username + " " + coordx + " " + coordy);
                                            response = in.readLine();
                                            while(response=="0")//resposta: 0: nao pode mover, 1: moveu
                                            {
                                                System.out.println("pfv espera");
                                                response = in.readLine();
                                            }
                                            System.out.println("ja se pode mover");

                                            break;
                                        case '2'://infetado
                                            out.println("infected " + username);
                                            break;
                                        case '3'://nrpessoas
                                            System.out.println("coord x:")
                                            int coordx = Integer.parseInt( systemIn.readLine() );
                                            System.out.println("coord y:")
                                            int coordy = Integer.parseInt( systemIn.readLine() );
                                            out.println("nrpeople " + username + " " + coordx + " " + coordy);
                                            response = in.readLine();
                                            System.out.println(response);

                                            break;
                                        case '0'://quit
                                            quit2=true;
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

                    case '2': //registar
                        System.out.println("username:")
                        String username =  systemIn.readLine();
                        System.out.println("pass:")
                        String pass =  systemIn.readLine();
                        out.println("2 " + username + " " + pass);

                        String response = in.readLine(); // responde 0 para registar correto, ou username ja existe
                        
                        switch(response){
                            case '0':
                                System.out.println("registo com sucesso");
                                break;
                            case '1':
                                System.out.println("username ja existe");
                                break;
                            default:
                                System.out.println("opcao invalida");
                                break;
                        }
                        break;
                    case '0': //quit
                        quit=true;
                        break;
                    default:
                        System.out.println("opcao invalida");
                        break;
                }

            }

            
            while ((userInput = systemIn.readLine()) != null) {
                out.println(userInput);
                out.flush();

                String response = in.readLine();
                System.out.println("Server response: " + response);
            }

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
