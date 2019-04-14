package br.com.augustolemes.avaliacao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;

@Repository
public interface ProvaRepository extends CrudRepository<ProvaDTO, Long> {
	
	ProvaDTO findById(Long id);
	
	//List<ProvaDTO> findByTurmaAndAnoLetivo(String turma, Integer anoLetivo);
	ProvaDTO findByTurmaAndTipoProvaAndMateria(String turma, Integer tipoProva, MateriaDTO nateria);
}