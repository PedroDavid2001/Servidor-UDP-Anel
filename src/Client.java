package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client implements Runnable {
    
    Scanner scan = new Scanner(System.in);
    DatagramSocket datagramSocket;
    boolean conectado;
    int port;
    private String ip; //ip da máquina
    

    public Client( int port, String ip ){
        this.ip = ip;
        conectado = true;
        this.port = port;
    }

    @Override
    public void run() {
        try {

            // inicialização do socket de datagrama para adquirir a porta 
            datagramSocket = new DatagramSocket();

            InetAddress address = InetAddress.getByName(ip);
            
            while( conectado ){

                int valor = 0;
                boolean valor_incorreto = true;

                // recebe o valor do usuário e converte em um array de bytes
                System.out.print("Digite o valor > ");
                
                while (valor_incorreto) {
                    try {
                        
                        valor = scan.nextInt();
                        valor_incorreto = false;

                    } catch (InputMismatchException e) {
                        System.out.print("Digite um valor válido > ");
                        scan.nextLine();
                    }
                }

                String msg = "" + valor;
                
                byte [] bufferEnvio = msg.getBytes();

                DatagramPacket datagramEnvio = new DatagramPacket(
                                                bufferEnvio, 
                                                bufferEnvio.length, 
                                                address, 
                                                port );
                
                //prepara a mensagem e envia
                System.out.println("Mensagem sendo enviada. . .");    
                
                datagramSocket.send(datagramEnvio); 
                
                byte[] bufferEntrada = new byte[ 1024 ];

                DatagramPacket datagramEntrada = new DatagramPacket(
                                                bufferEntrada, 
                                                bufferEntrada.length );

                
                //recebe o datagrama e escreve no buffer
                datagramSocket.receive(datagramEntrada);
                bufferEntrada = datagramEntrada.getData();

                String resposta = new String(bufferEntrada);
                System.out.println("\nMensagem recebida do server: " + resposta );
                
            }
            
            System.out.println("\nDesconectado do servidor.");
            datagramSocket.close();

        } catch (IOException e) {
            System.err.println("Exceção disparada: " + e.getMessage());
        }
    }
    
}
