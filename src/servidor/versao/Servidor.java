package servidor.versao;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import servidor.versao.ThreadConexao;

public class Servidor {
	public static void main(String[] args) throws IOException{
		ServerSocket servidor = new ServerSocket(8000);
		ExecutorService pool = Executors.newFixedThreadPool(20);
		
		System.out.println("SERVIDOR RODANDO VIOLENTAMENTE NA PORTA 8000\n");
		while(true) {
			pool.execute(new ThreadConexao(servidor.accept()));
		}
	}
}