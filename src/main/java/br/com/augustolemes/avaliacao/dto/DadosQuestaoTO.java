package br.com.augustolemes.avaliacao.dto;

public class DadosQuestaoTO {
	
	private Long idQuestao;
	
	private String habilidade;
	
	private String questao;
	
	private String resposta;
	
	private Long idProva;
	
	private Integer tipoQuestao;

	public Long getIdQuestao() {
		return idQuestao;
	}

	public void setIdQuestao(Long idQuestao) {
		this.idQuestao = idQuestao;
	}

	public String getQuestao() {
		return questao;
	}

	public void setQuestao(String questao) {
		this.questao = questao;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public Long getIdProva() {
		return idProva;
	}

	public void setIdProva(Long idProva) {
		this.idProva = idProva;
	}

	public Integer getTipoQuestao() {
		return tipoQuestao;
	}

	public void setTipoQuestao(Integer tipoQuestao) {
		this.tipoQuestao = tipoQuestao;
	}

	public String getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(String habilidade) {
		this.habilidade = habilidade;
	}
	
	
	

}
