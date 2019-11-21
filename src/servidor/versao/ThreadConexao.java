package servidor.versao;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import servidor.versao.RequisicaoHTTP;
import servidor.versao.RespostaHTTP;

public class ThreadConexao implements Runnable{
	private final Socket cliente;
	private boolean conectado;
	
	public ThreadConexao(Socket cliente) {
		this.cliente = cliente;
	}
	
	@Override
	public void run() {
		conectado = true;
		System.out.println("Endereco do cliente: " + cliente.getInetAddress());
		while(conectado) {
			try {
				RequisicaoHTTP requisicao = RequisicaoHTTP.lerRequisicao(cliente.getInputStream());
				
				if(requisicao.isManterViva()) {
					cliente.setKeepAlive(true);
					cliente.setSoTimeout((int)requisicao.getTempoLimite());
				}else {
					cliente.setSoTimeout(300);
				}
				
				if(requisicao.getRecurso().equals("/")) {
					requisicao.setRecurso("src\\servidor\\versao\\index.html");
				}else if(requisicao.getRecurso().equals("/curso")){
					requisicao.setRecurso("src\\servidor\\versao\\curso.html");
				}else if(requisicao.getRecurso().equals("/violencia")){
					requisicao.setRecurso("src\\servidor\\versao\\violencia.html");
				}
				File arquivo = new File(requisicao.getRecurso().replaceFirst("/", ""));
				RespostaHTTP resposta;
				if(arquivo.exists()) {
					resposta = new RespostaHTTP(requisicao.getProtocolo(), 200, "OK");
				}else {
					resposta = new RespostaHTTP(requisicao.getProtocolo(), 404, "Not Found");
					arquivo = new File("src\\servidor\\versao\\404.html");
				}
				
				resposta.setConteudo(Files.readAllBytes(arquivo.toPath()));
				String dataFormatada = new Date().toString();
				resposta.setCabecalhos("Location", "https://localhost:8000/");
				resposta.setCabecalhos("Date", dataFormatada);
				resposta.setCabecalhos("Server", "MeuServidor/1.0");
				resposta.setCabecalhos("Content-Type", "text/html");
				resposta.setCabecalhos("Content-Length", resposta.getTamanhoResposta());
				resposta.setSaida(cliente.getOutputStream());
				resposta.envia();
			}catch(IOException e) {
				if(e instanceof SocketTimeoutException) {
					try {
						conectado = false;
						cliente.close();
					}catch(IOException ex) {
						Logger.getLogger(ThreadConexao.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}
	}
}
