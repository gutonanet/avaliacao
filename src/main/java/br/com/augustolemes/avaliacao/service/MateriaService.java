package br.com.augustolemes.avaliacao.service;

import java.util.List;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;

public interface MateriaService {
	Iterable<MateriaDTO> findAll();
	
	MateriaDTO findById(Long id);
	
	List<MateriaDTO> findByNome(String nome);

}
