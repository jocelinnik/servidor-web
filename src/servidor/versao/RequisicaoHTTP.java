package servidor.versao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RequisicaoHTTP {
	private String protocolo;
	private String recurso;
	private String metodo;
	private boolean manterViva = true;
	private long tempoLimite = 3000;
	private Map<String, List> cabecalhos;
	
	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public boolean isManterViva() {
		return manterViva;
	}

	public void setManterViva(boolean manterViva) {
		this.manterViva = manterViva;
	}

	public long getTempoLimite() {
		return tempoLimite;
	}

	public void setTempoLimite(long tempoLimite) {
		this.tempoLimite = tempoLimite;
	}
	
	public Map<String, List> getCabecalhos(){
		return cabecalhos;
	}
	
	public void setCabecalho(String chave, String... valores) { 
		if (cabecalhos == null) { 
			cabecalhos = new TreeMap<>(); 
		} 
		cabecalhos.put(chave, Arrays.asList(valores)); 
	}
	
	public static RequisicaoHTTP lerRequisicao(InputStream entrada) throws IOException{
		RequisicaoHTTP req = new RequisicaoHTTP();
		BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
		
		System.out.println("Requisicao: ");
		String linha = buffer.readLine();
		String[] dadosreq = linha.split(" ");
		
		req.setMetodo(dadosreq[0]);
		req.setRecurso(dadosreq[1]);
		req.setProtocolo(dadosreq[2]);
		System.out.println(dadosreq[2]);
		String header = buffer.readLine();
		while(header!=null&&!header.isEmpty()) {
			System.out.println(header);
			String[] linhaCabecalho = header.split(":");
			req.setCabecalho(linhaCabecalho[0], linhaCabecalho[1].trim().split(","));
			header = buffer.readLine();
		}
		
		if(req.getCabecalhos().containsKey("Connection")) {
			req.setManterViva(req.getCabecalhos().get("Connection").get(0).equals("keep-alive"));
		}
		
		return req;
	}
}
