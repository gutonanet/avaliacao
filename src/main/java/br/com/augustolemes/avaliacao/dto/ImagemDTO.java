package br.com.augustolemes.avaliacao.dto;

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
public class ImagemDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="questao_id")	
	private QuestaoDTO questao;
	
	@Lob
	@Column
	private byte[] imagem;
	
	@Column
	private String legenda;
	
	@Column
	private String nome;
	
	@Column
	private Integer posicao;
	
	@Column
	private String tipoImagem;
	
	@Transient 
	private String url;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QuestaoDTO getQuestao() {
		return questao;
	}

	public void setQuestao(QuestaoDTO questao) {
		this.questao = questao;
	}

	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
		if(nome!= null && nome.toLowerCase().contains("jpg")) {
			setTipoImagem(TipoImagem.JPG.getTipo());
		}
		if(nome!= null && nome.toLowerCase().contains("png")) {
			setTipoImagem(TipoImagem.PNG.getTipo());
		}
		
	}

	public String getTipoImagem() {
		return tipoImagem;
	}

	public void setTipoImagem(String tipoImagem) {
		this.tipoImagem = tipoImagem;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}
	
	public String getPosicaoFormatted() {
		if(posicao == null) {
			return null;
		}
		return PosicaoEnum.INFERIOR.getDescricaoPosicao(posicao);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	

}
