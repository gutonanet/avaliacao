package br.com.augustolemes.avaliacao.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;

@Repository
public interface RespostaRepository extends CrudRepository<RespostaDTO, Long>{
	
	List<RespostaDTO> findByQuestao(QuestaoDTO questao);

}
