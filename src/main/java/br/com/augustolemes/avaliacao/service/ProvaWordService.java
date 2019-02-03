package br.com.augustolemes.avaliacao.service;

import br.com.augustolemes.avaliacao.dto.ProvaDTO;

public interface ProvaWordService {
	
	public void readDocxFile(ProvaDTO prova,String fileName) throws Exception;

}
