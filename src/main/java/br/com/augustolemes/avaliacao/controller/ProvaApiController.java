package br.com.augustolemes.avaliacao.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.TipoProvaEnum;
import br.com.augustolemes.avaliacao.service.ImagemService;
import br.com.augustolemes.avaliacao.service.MateriaService;
import br.com.augustolemes.avaliacao.service.ProvaService;
import br.com.augustolemes.avaliacao.service.ProvaWordService;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@RestController
@RequestMapping("/api")
public class ProvaApiController {

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
	
	@GetMapping("/materias")
	public Iterable<MateriaDTO> listaMaterias(){
		return materiaService.findAll();
	}
	
	@GetMapping("/tiposProva")
	public List<TipoProvaEnum> tiposProva(){
		return Arrays.asList(TipoProvaEnum.values());
	}
	
	

	
	
	
	
}
