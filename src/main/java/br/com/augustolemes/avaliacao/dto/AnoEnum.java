package br.com.augustolemes.avaliacao.dto;

public enum AnoEnum {
	
	UM(1),
	DOIS(2),//
	TRES(3),
	QUATRO(4),
	CINCO(5),
	SEIS(6),
	SETE(7),
	OITO(8),
	NOVE(9);
		
	Integer ano;
	
	AnoEnum(Integer ano){
		this.ano = ano;
	}

	
	public AnoEnum[] ensinoMedio() {
		AnoEnum[] ano = {UM, DOIS, TRES};
		return ano;
	}


	public Integer getAno() {
		return ano;
	}
	
	
}
