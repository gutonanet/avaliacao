package br.com.augustolemes.avaliacao.service;

import br.com.augustolemes.avaliacao.dto.DadosProvaTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;

public interface ProvaService {

	ProvaDTO findProva(DadosProvaTO dados);
	
	ProvaDTO findById(Long id);
	
	ProvaDTO converter(DadosProvaTO dados);
	
	DadosProvaTO converter(ProvaDTO prova);
	
	ProvaDTO findProvaAllData(Long id);
	
}
