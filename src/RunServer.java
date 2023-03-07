package Questao1;

public class RunServer {
    //O código de servidor já altera a porta automaticamente quando necessário
    int port = 10001;
    
    public RunServer() {
        this.rodar();
    }

    private void rodar() {
        try {

            // Nos argumentos, você altera as String de da sua máquina e ip da máquina destino
            Server servidor = new Server( port, "IP-da-maquina", "IP-Dest" );
            Thread serverThread = new Thread(servidor);

            serverThread.start();

        } catch (Exception e) {
            System.err.println("Exceção disparada: " + e.getMessage() );
        }
    }

    public static void main(String[] args) throws Exception {
        new RunServer();
    }
}
