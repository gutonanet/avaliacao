package br.com.augustolemes.avaliacao.dto;

public enum PosicaoEnum {
	
	SUPERIOR(0,"Superior"),
	INFERIOR(1,"Inferior");
	
	private Integer id;
	
	private String descricao;
	
	PosicaoEnum(Integer id, String descricao){
		this.id = id;
		this.descricao = descricao;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricaoPosicao(Integer posicao) {
		if(id == 0) {
			return SUPERIOR.descricao;
		}else {
			return INFERIOR.descricao;
		}
	}
	
}
