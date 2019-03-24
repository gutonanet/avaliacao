package br.com.augustolemes.avaliacao.dto;

public enum TipoImagem {
	
	//image/x-png,image/jpeg

	PNG("image/x-png"),
	JPG("image/jpeg");
	
	private String tipo;
	
	TipoImagem(String tipo){
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
