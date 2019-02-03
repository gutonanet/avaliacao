package br.com.augustolemes.avaliacao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.DadosQuestaoTO;
import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.repository.QuestaoRepository;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@Service
public class QuestaoServiceImpl implements QuestaoService{
	
	@Autowired
	private QuestaoRepository questaoRepository;
	
	@Autowired
	private RespostaService respostaService;
	
	public List<QuestaoDTO> findByProva(ProvaDTO prova){
		return questaoRepository.findByProva(prova);
		
	}
	
	public QuestaoDTO save(QuestaoDTO questao) {
		return questaoRepository.save(questao);
	}

	public QuestaoDTO findById(Long id) {
		return questaoRepository.findOne(id);
	}
	
	public void deleteQuestao(QuestaoDTO questao) {
		List<RespostaDTO> lista = respostaService.findByQuestao(questao);
		for(RespostaDTO r:lista) {
			respostaService.delete(r);
		}
		
		questaoRepository.delete(questao);
	}
	
	public DadosQuestaoTO converter(QuestaoDTO questao) {
		DadosQuestaoTO dados = new DadosQuestaoTO();
		dados.setIdQuestao(questao.getId());
		dados.setHabilidade(questao.getHabilidade());
		dados.setQuestao(questao.getQuestao());
		dados.setIdProva(questao.getProva().getId());
		dados.setContinuacaoQuestao(questao.getContinuacaoQuestao());
		return dados;
	}
	
	public QuestaoDTO converter(DadosQuestaoTO dados, ProvaDTO prova) {
		QuestaoDTO questao = new QuestaoDTO();
		questao.setId(dados.getIdQuestao());
		questao.setQuestao(dados.getQuestao());
		questao.setHabilidade(dados.getHabilidade());
		questao.setTipoQuestao(dados.getTipoQuestao());
		questao.setContinuacaoQuestao(dados.getContinuacaoQuestao());
		questao.setProva(prova);
		return questao;

	}
	
	public QuestaoRepository getQuestaoRepository() {
		return questaoRepository;
	}

	public void setQuestaoRepository(QuestaoRepository questaoRepository) {
		this.questaoRepository = questaoRepository;
	}

	
	
}
