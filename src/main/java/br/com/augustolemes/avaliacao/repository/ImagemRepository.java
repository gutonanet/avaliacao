package br.com.augustolemes.avaliacao.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;

@Repository
public interface ImagemRepository  extends CrudRepository<ImagemDTO, Long> {
	
	List<ImagemDTO> findByQuestao(QuestaoDTO questao);

}
