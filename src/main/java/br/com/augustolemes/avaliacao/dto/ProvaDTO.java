package br.com.augustolemes.avaliacao.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class ProvaDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7524981542304877371L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String turma;

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="materia_id")
	private MateriaDTO materia;	
	
	//@Column
	//private Integer anoLetivo;
	
	@Column
	private Integer tipoProva;
	
	@Column
	private String frase;

	@Transient
	private List<QuestaoDTO> questoes;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	/*
	public Integer getAnoLetivo() {
		return anoLetivo;
	}

	public void setAnoLetivo(Integer anoLetivo) {
		this.anoLetivo = anoLetivo;
	}
*/
	public MateriaDTO getMateria() {
		return materia;
	}

	public void setMateria(MateriaDTO materia) {
		this.materia = materia;
	}

	public Integer getTipoProva() {
		return tipoProva;
	}

	public void setTipoProva(Integer tipoProva) {
		this.tipoProva = tipoProva;
	}

	public List<QuestaoDTO> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<QuestaoDTO> questoes) {
		this.questoes = questoes;
	}

	public String getFrase() {
		return frase;
	}

	public void setFrase(String frase) {
		this.frase = frase;
	}
	
	

}
