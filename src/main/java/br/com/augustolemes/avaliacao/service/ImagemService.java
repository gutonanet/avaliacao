package br.com.augustolemes.avaliacao.service;

import java.util.List;

import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;

public interface ImagemService {
	
	ImagemDTO findById(Long id);
	
	List<ImagemDTO> findByQuestao(QuestaoDTO questao);
	
	ImagemDTO save(ImagemDTO imagem);
	
	void delete(ImagemDTO imagem);
	

}
