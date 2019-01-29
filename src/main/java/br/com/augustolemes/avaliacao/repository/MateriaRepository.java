package br.com.augustolemes.avaliacao.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;

@Repository
public interface MateriaRepository extends CrudRepository<MateriaDTO, Long>{
	
	MateriaDTO findById(Long id);
	
	List<MateriaDTO> findByNome(String nome);
	
}
