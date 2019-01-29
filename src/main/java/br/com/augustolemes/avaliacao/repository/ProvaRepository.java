package br.com.augustolemes.avaliacao.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;

@Repository
public interface ProvaRepository extends CrudRepository<ProvaDTO, Long> {
	
	ProvaDTO findById(Long id);
	
	//List<ProvaDTO> findByTurmaAndAnoLetivo(String turma, Integer anoLetivo);
	List<ProvaDTO> findByTurma(String turma);
}