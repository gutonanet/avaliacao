package br.com.augustolemes.avaliacao.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RespostaDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length=2500)
	private String resposta;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="questao_id")
	private QuestaoDTO questao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResposta() {
		return resposta;
	}
	
	public String getRespostaFormatted(Integer indice) {
		if(resposta == null || "".equals(resposta)) {
			return "";
		}
		String marcador = "";
			switch(indice) {
			case 1: marcador = "a) ";break;
			case 2: marcador = "b) ";break;
			case 3: marcador = "c) ";break;
			case 4: marcador = "d) ";break;
			case 5: marcador = "e) ";break;
			}
			
			return marcador+resposta;
	}
	
	public String getRespostaShort() {
			if(resposta!= null) {
				if(resposta.length() > 100) {
					return resposta.substring(0, 100)+"...";
				}
			}
			return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public QuestaoDTO getQuestao() {
		return questao;
	}

	public void setQuestao(QuestaoDTO questao) {
		this.questao = questao;
	}
	
	

}
