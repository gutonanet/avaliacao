package br.com.augustolemes.avaliacao.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	
	String validarQuestionario(DadosQuestaoTO questao);
	
	String singleFileUpload(MultipartFile file, String legenda, Integer posicao, QuestaoDTO questaoDTO, String mensagemErro) throws IOException;
	
	String salvarRespostas(String mensagemErro, QuestaoDTO questaoDTO, String respostaTexto);
	
	String validarQuestionario(QuestaoDTO questao);

}
//