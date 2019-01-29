package br.com.augustolemes.avaliacao.service;

import java.util.List;

import br.com.augustolemes.avaliacao.dto.DadosQuestaoTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;

public interface QuestaoService {

	List<QuestaoDTO> findByProva(ProvaDTO prova);
	
	QuestaoDTO save(QuestaoDTO questao);
	
	QuestaoDTO findById(Long id);
	
	DadosQuestaoTO converter(QuestaoDTO questao);
	
	QuestaoDTO converter(DadosQuestaoTO dados, ProvaDTO prova);
	
	void deleteQuestao(QuestaoDTO questao);
	
}
