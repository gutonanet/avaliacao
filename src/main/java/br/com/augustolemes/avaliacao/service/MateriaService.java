package br.com.augustolemes.avaliacao.service;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;

public interface MateriaService {
	Iterable<MateriaDTO> findAll();
	
	MateriaDTO findById(Long id);

}
