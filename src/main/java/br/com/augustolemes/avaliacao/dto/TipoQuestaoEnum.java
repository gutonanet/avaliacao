package br.com.augustolemes.avaliacao.dto;

public enum TipoQuestaoEnum {
	
	MULTIPLA_ESCOLHA(1),
	DESCRITIVA(2);

	private Integer codigo;
	TipoQuestaoEnum(Integer codigo){
		this.codigo = codigo;
	}
	
	public Integer getCodigo() {
		return this.codigo;
	}
}
