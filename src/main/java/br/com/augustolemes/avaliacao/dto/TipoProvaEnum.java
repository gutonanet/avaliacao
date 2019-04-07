package br.com.augustolemes.avaliacao.dto;

public enum TipoProvaEnum {
	
	TIPO_EF(1, "Ensino Fundamental"),
	TIPO_EM(2, "Ensino Médio");
	
	private Integer id;
	
	private String nome;
	
	TipoProvaEnum(Integer id, String nome){
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static TipoProvaEnum getId(Integer id) {
		if(id == null) {
			return null;
		}
		for(TipoProvaEnum tipo:TipoProvaEnum.values()) {
			if(tipo.getId().equals(id)) {
				return tipo;
			}
		}
		return null;
	}
	
	public TipoProvaTO getTO() {
		TipoProvaTO t = new TipoProvaTO();
		t.setId(getId());
		t.setNome(getNome());
		
		return t;
	}
	
	
}
