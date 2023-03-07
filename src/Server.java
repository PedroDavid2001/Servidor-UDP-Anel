package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements Runnable{
    
    //========================================================
    
    private int id;
    private boolean conexao;
    private boolean remetente = false;
    private boolean repassa = true;
    private DatagramSocket server_socket;
    private String dest_ip; //endereço de destino
    private String ip; //ip da máquina
    private int port;
    private int client_port;
    private InetAddress address;
    
    //========================================================
    
    //BOOLEANS DE CONTROLE 
    boolean multi_maquinas = false; // ESTÁ USANDO MAIS DE UM COMPUTADOR
    
    boolean maquina_par = false;    // ESTE COMPUTADOR IRÁ GERENCIAR OS 
                                    // PROCESSOS PARES (OBS.: FALSO SE 
                                    // FOR APENAS UMA MÁQUINA)
    
    //========================================================
    
    public Server( int port, String ip, String dest_ip ){
        this.dest_ip = dest_ip;
        this.ip = ip;
        this.port = port;
        conexao = true;
    }

    @Override
    public void run() {

        try {

            address = InetAddress.getByName(ip);
            InetAddress dest_add = InetAddress.getByName(dest_ip);

            estabelecerPorta();

            if( conexao ){
                System.out.println( "P" + id + " rodando em " +  
                InetAddress.getByName(ip).getHostAddress() + ":" + port);
            }
            
            while( conexao ){

                //arrays de bytes utilizados para armazenar os dados dos datagramas
                byte[] dados_recebidos = new byte [1024];
                byte[] dados_envio;

                DatagramPacket datagrama_recebido = new DatagramPacket( dados_recebidos, dados_recebidos.length);

                //recebe o datagrama e armazena os dados no array de bytes 
                server_socket.receive(datagrama_recebido);
                dados_recebidos = datagrama_recebido.getData();

                //exibição da mensagem
                String valor_recebido = new String(dados_recebidos);
                System.out.println("\nP" + id + " recebeu a mensagem < " + valor_recebido);

                String resposta = criarResposta(valor_recebido, datagrama_recebido.getPort() );
                dados_envio = resposta.getBytes();

                DatagramPacket pacote_envio = null;

                int dest_port = definirPortaDestino();

                if(repassa){
                    pacote_envio = new DatagramPacket(  dados_envio, 
                                                        dados_envio.length, 
                                                        dest_add, 
                                                        dest_port );
                    
                }
                else{
                    remetente = false;
                    repassa = true;
                    pacote_envio = new DatagramPacket(  dados_envio, 
                                                        dados_envio.length, 
                                                        address, 
                                                        client_port );
                }

                server_socket.send(pacote_envio);
                System.out.println("P" + id + " enviou a mensagem > " + resposta);  
            }

            server_socket.close(); 
        } 
        catch (IOException e) {
            System.err.println("Exceção disparada: " + e.getMessage());
        } 
    }

    private String criarResposta( String valor_recebido, int client_port ){
        
        int valor = resgatarValor(valor_recebido, client_port);
        return gerarDatagrama(valor);

    }

    // datagrama = [id_remetente, conteudo]
    private String gerarDatagrama( int conteudo ){
        return "[ " + id + " - " + conteudo + " ]"; 
    }

    // "retira" o valor contido no datagrama recebido 
    private int resgatarValor(String msg, int client_port){

        //verifica se é o número digitado pelo usuário ou um datagrama
        if(msg.charAt(0) != '['){
            this.client_port = client_port;
            remetente = true;
            return (int)Double.parseDouble(msg); 
        }

        String [] simbolos = msg.split(" ");
        int tmp = (int)Double.parseDouble( simbolos[3] );

        /*
        * Recebeu mensagem de outro processo, 
        * agora verifica se deve repassar ou 
        * entregar ao cliente.
        */

        if(remetente){
            repassa = false;
        }
        else{
            repassa = true;
            
            if( id % 2 == 0 ){
                tmp *= 2;
            }
        }
        
        return tmp;
    }

    private int definirPortaDestino(){

        int dest_port;

        if(multi_maquinas){
                    
            if(id == 2){
                dest_port = port + 2;    
            }else if(id == 4){
                dest_port = port - 2;
            }else{
                dest_port = port;
            }

        }else{

            if(id == 4){
                dest_port = 10001;
            }
            else{
                dest_port = port + 1;
            }
        }

        return dest_port;
    }

    private void estabelecerPorta(){
        boolean porta_bloqueada = true;

        while(porta_bloqueada){
            try {
                server_socket = new DatagramSocket( port, address );
                porta_bloqueada = false;   
            } catch (SocketException e) {
                
                if(multi_maquinas){
                    port += 2;
                }else{
                    port++;
                }
                
            }
        }

        if( maquina_par ){
            this.id = (port + 1) - 10000;
        }else{
            this.id = port - 10000;   
        }
    }
    
}
