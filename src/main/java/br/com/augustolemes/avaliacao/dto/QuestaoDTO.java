package br.com.augustolemes.avaliacao.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class QuestaoDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length=2500)
	private String questao;
	
	@Column
	private String habilidade;
	
	@Column
	private Integer tipoQuestao;
	
	@Column(length=2500)
	private String resposta;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="prova_id")	
	private ProvaDTO prova;
	
	@Transient
	private List<RespostaDTO> respostas = new ArrayList<>();
	
	@Transient
	private List<ImagemDTO> imagens= new ArrayList<>();

	
	public String getRespostaShort() {
		if(resposta!= null) {
			if(resposta.length() > 100) {
				return resposta.substring(0, 100)+"...";
			}
		}
		return resposta;
	}
	
	public String getQuestaoShort() {
		if(questao!= null) {
			if(questao.length() > 100) {
				return questao.substring(0, 100)+"...";
			}
		}
		return questao;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestao() {
		return questao;
	}

	public void setQuestao(String questao) {
		this.questao = questao;
	}

	public Integer getTipoQuestao() {
		return tipoQuestao;
	}

	public void setTipoQuestao(Integer tipoQuestao) {
		this.tipoQuestao = tipoQuestao;
	}

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public ProvaDTO getProva() {
		return prova;
	}

	public void setProva(ProvaDTO prova) {
		this.prova = prova;
	}

	public List<RespostaDTO> getRespostas() {
		return respostas;
	}

	
	public List<RespostaDTO> getRespostasFormatted(){
		List<RespostaDTO> lista5 = new ArrayList<>();
		List<RespostaDTO> listaRespostas = new ArrayList<>();
		if(respostas != null && !respostas.isEmpty()) {
			lista5.addAll(respostas);
			listaRespostas.addAll(respostas);
		}
		
		if(lista5.size() < 5) {
			for(int i = listaRespostas.size() -1; i < 5; i++) {
				RespostaDTO dto = new RespostaDTO();
				dto.setResposta("");
				lista5.add(dto);
			}
		}
		
		return lista5;
	}
	
	
	public void setRespostas(List<RespostaDTO> respostas) {
		this.respostas = respostas;
	}

	public String getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(String habilidade) {
		this.habilidade = habilidade;
	}

	public List<ImagemDTO> getImagens() {
		return imagens;
	}

	public void setImagens(List<ImagemDTO> imagens) {
		this.imagens = imagens;
	}	
	
	
	

}
