package Questao1;

public class RunClient {

    public RunClient( int port ){
        rodar( port );
    }

    void rodar( int port ){
        try {
            
            //Altere a String com o ip da sua máquina
            Client cliente = new Client(port, "IP-da-maquina");
            
            System.out.println("Cliente enviando datagramas à porta: " + port);

            //Instancia uma thread para o cliente e inicia
            Thread thread = new Thread(cliente);
            thread.start();

        } catch (Exception e) {
            System.err.println("Exceção disparada: " + e.getMessage() );
        }
    }

    public static void main(String[] args){
        /*
        * altere a porta para o valor que o terminal 
        * mostrar quando criar o servidor deste cliente
        */
        new RunClient(10001);
    }
}
