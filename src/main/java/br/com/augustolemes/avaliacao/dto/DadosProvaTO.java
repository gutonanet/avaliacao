package br.com.augustolemes.avaliacao.dto;

public class DadosProvaTO {

	public Long getIdMateria() {
		return idMateria;
	}

	public void setIdMateria(Long idMateria) {
		this.idMateria = idMateria;
	}

	private Integer idTipoProva;
	
	private Long idProva;
	
	private Long idMateria;
	
	private Integer anoLetivo;
	
	private String turma;
	
	private String frase;

	public Integer getIdTipoProva() {
		return idTipoProva;
	}

	public void setIdTipoProva(Integer idTipoProva) {
		this.idTipoProva = idTipoProva;
	}

	public Long getIdProva() {
		return idProva;
	}

	public void setIdProva(Long idProva) {
		this.idProva = idProva;
	}

	public String getTurma() {
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Integer getAnoLetivo() {
		return anoLetivo;
	}

	public void setAnoLetivo(Integer anoLetivo) {
		this.anoLetivo = anoLetivo;
	}

	public String getFrase() {
		return frase;
	}

	public void setFrase(String frase) {
		this.frase = frase;
	}
	
	
	
	
}
