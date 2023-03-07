package Questao1;

public class RunServer {
    int port = 10001;
    
    public RunServer() {
        this.rodar();
    }

    private void rodar() {
        try {

            // Cria uma thread do servidor para tratar a conexão
            Server servidor = new Server( port, "192.168.1.102", "192.168.1.102" ); ///10.70.1.x ou localhost
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