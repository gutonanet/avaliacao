package br.com.augustolemes.avaliacao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.repository.RespostaRepository;
import br.com.augustolemes.avaliacao.service.RespostaService;

@Service
public class RespostaServiceImpl implements RespostaService{
	
	@Autowired
	private RespostaRepository respostaRepository; 

	@Override
	public List<RespostaDTO> findByQuestao(QuestaoDTO questao) {
		return respostaRepository.findByQuestao(questao);
	}

	@Override
	public RespostaDTO save(RespostaDTO resposta) {
		return respostaRepository.save(resposta);
	}
	
	@Override
	public void delete(RespostaDTO resposta) {
		respostaRepository.delete(resposta);
	}
	
	public RespostaDTO findById(Long id) {
		return respostaRepository.findOne(id);
	}
	
	
}
