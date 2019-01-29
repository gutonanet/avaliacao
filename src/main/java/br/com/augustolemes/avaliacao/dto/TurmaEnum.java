package br.com.augustolemes.avaliacao.dto;

public enum TurmaEnum {
	A("A"),
	B("B"),
	C("C");

	private String turma;
	TurmaEnum(String turma){
		this.turma = turma;
	}
	public String getTurma() {
		return turma;
	}



}
