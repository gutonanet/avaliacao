package br.com.augustolemes.avaliacao.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;

@Repository
public interface QuestaoRepository extends CrudRepository<QuestaoDTO, Long>{
	
	List<QuestaoDTO> findByProva(ProvaDTO prova);

}
