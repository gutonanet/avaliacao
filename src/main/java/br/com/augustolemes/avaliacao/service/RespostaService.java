package br.com.augustolemes.avaliacao.service;

import java.util.List;

import br.com.augustolemes.avaliacao.dto.DadosRespostaTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;

public interface RespostaService {
	
	List<RespostaDTO> findByQuestao(QuestaoDTO questao);
	
	RespostaDTO save(RespostaDTO resposta);
	
	void delete(RespostaDTO resposta);
	
	RespostaDTO findById(Long id);
	
	DadosRespostaTO converter(RespostaDTO resposta);
	
	String validarResposta(RespostaDTO resposta);

}
