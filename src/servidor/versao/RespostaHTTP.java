package servidor.versao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RespostaHTTP {
	private String protocolo;
	private int codigo;
	private String mensagem;
	private byte[] conteudo;
	private Map<String, List> cabecalhos;
	private OutputStream saida;
	
	public RespostaHTTP() {
		
	}
	
	public RespostaHTTP(String protocolo, int codigo, String mensagem) {
		this.setProtocolo(protocolo);
		this.setCodigo(codigo);
		this.setMensagem(mensagem);
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public Map<String, List> getCabecalhos() {
		return cabecalhos;
	}

	public OutputStream getSaida() {
		return saida;
	}

	public void setSaida(OutputStream saida) {
		this.saida = saida;
	}
	
	/**
	 * Envia os dados da resposta ao cliente
	 * 
	 * @throws IOException
	 */
	public void envia() throws IOException{
		this.getSaida().write(montaCabecalho());
		this.getSaida().write(this.getConteudo());
		this.getSaida().flush();
	}
	
	/**
	 * Insere um item de cabecalho no mapa
	 * 
	 * @param chave
	 * @param valores lista com um ou mais valores para esta chave
	 */
	public void setCabecalhos(String chave, String... valores) {
		if(this.cabecalhos==null) {
			this.cabecalhos = new TreeMap<>();
		}
		
		this.cabecalhos.put(chave, Arrays.asList(valores));
	}
	
	/**
	 * pega o tamanho da resposta em bytes
	 * 
	 * @return retorna o valor em bytes do tamanho do conteudo da resposta convertida em string
	 */
	public String getTamanhoResposta() {
		return this.getConteudo().length + "";
	}
	
	/**
	 * converte o cabecalho em string
	 * 
	 * @return retorna o cabecalho em bytes
	 */
	private byte[] montaCabecalho() {
		return this.toString().getBytes();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getProtocolo()).append(" ").append(this.getCodigo()).append(" ").append(this.getMensagem()).append("\r\n");
		
		for(Map.Entry<String, List> entry : this.getCabecalhos().entrySet()) {
			str.append(entry.getKey());
			String corrigida = Arrays.toString(entry.getValue().toArray()).replace("[", "").replace("]", "");
			str.append(": ").append(corrigida).append("\r\n");
		}
		
		str.append("\r\n");
		return str.toString();
	}
}