package br.com.augustolemes.avaliacao.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.repository.ImagemRepository;
import br.com.augustolemes.avaliacao.service.ImagemService;

@Service
public class ImagemServiceImpl implements ImagemService {

	@Autowired
	ImagemRepository dao;
	
	
	public ImagemDTO findById(Long id) {
		return dao.findOne(id);
	}


	@Override
	public List<ImagemDTO> findByQuestao(QuestaoDTO questao) {
		return dao.findByQuestao(questao);
	}


	@Override
	public ImagemDTO save(ImagemDTO imagem) {
		return dao.save(imagem);
	}


	@Override
	public void delete(ImagemDTO imagem) {
		dao.delete(imagem);
		
	}
}
