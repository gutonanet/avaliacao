package br.com.augustolemes.avaliacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import br.com.augustolemes.avaliacao.service.ImagemService;
import br.com.augustolemes.avaliacao.service.MateriaService;
import br.com.augustolemes.avaliacao.service.ProvaService;
import br.com.augustolemes.avaliacao.service.ProvaWordService;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@RestController
public class ProvaAppController {

	@Autowired
	private MateriaService materiaService;
	
	@Autowired
	private ProvaService provaService;

	@Autowired 
	private QuestaoService questaoService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Autowired
	private ProvaWordService provaWordService;
	
	@Autowired
	private ImagemService imagemService;
	
	
}
