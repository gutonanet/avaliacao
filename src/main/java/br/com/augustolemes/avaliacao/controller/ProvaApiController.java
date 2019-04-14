package br.com.augustolemes.avaliacao.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.augustolemes.avaliacao.dto.DadosProvaTO;
import br.com.augustolemes.avaliacao.dto.DadosQuestaoTO;
import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.PosicaoEnum;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.dto.TipoProvaEnum;
import br.com.augustolemes.avaliacao.dto.TipoProvaTO;
import br.com.augustolemes.avaliacao.exception.BusinessException;
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
	private ImagemService imagemService;
	
	@GetMapping("/materias")
	public Iterable<MateriaDTO> listaMaterias(){
		return materiaService.findAll();
	}
	
	@GetMapping("/tiposProva")
	public List<TipoProvaTO> tiposProva(){
		List<TipoProvaTO> lista = new ArrayList<>();
		for(TipoProvaEnum e:TipoProvaEnum.values()) {
			lista.add(e.getTO());
		}
		return lista;
	}
	
	@PostMapping("/findProva")
	public DadosProvaTO findProva(@RequestParam String tipoProva, @RequestParam String materia, @RequestParam String turma, @RequestParam String frase ) throws BusinessException{
		
		TipoProvaEnum tipo = TipoProvaEnum.getNome(tipoProva);
		List<MateriaDTO> materias = materiaService.findByNome(materia);
		DadosProvaTO dados = new DadosProvaTO();
		dados.setFrase(frase);
		dados.setTurma(turma);
		dados.setIdMateria(materias.get(0).getId());
		dados.setIdTipoProva(tipo.getId());
    	
    	String mensagemErro = provaService.validarProva(dados);
    	if(mensagemErro!= null && !"".equals(mensagemErro)) {
    	   	throw new BusinessException(mensagemErro);
    	       		
    	}

    	ProvaDTO prova = provaService.save(dados);
    	dados.setIdProva(prova.getId());
    	
		return dados;
	}
	
	
	@PostMapping("/questaoSalvar")
	public void questaoSalvar(@RequestBody DadosQuestaoTO questao,  @RequestParam("file") MultipartFile file) throws BusinessException{
		ProvaDTO prova = provaService.findById(questao.getIdProva());
    	QuestaoDTO questaoDTO = questaoService.converter(questao, prova);
    	String mensagemErro = questaoService.validarQuestionario(questao);
    	QuestaoDTO q = questaoDTO;
    	if(mensagemErro != null && !"".equals(mensagemErro)) {
    		throw new BusinessException(mensagemErro);
    	}else {
    		q = questaoService.save(questaoDTO);	
    	}
    	try {
        	if(file!= null) {
        		if(!"".equals(file.getOriginalFilename()))
        			mensagemErro += questaoService.singleFileUpload(file, questao.getLegendaImagem(),questao.getPosicaoImagem(), q, mensagemErro);
        	}
    	}catch(Exception e) {
    		mensagemErro += " Erro ao efetuar o upload.";
    	}
    	
    	if((mensagemErro== null || "".equals(mensagemErro)) && questao.getResposta()!= null && !"".equals(questao.getResposta())) {
    		mensagemErro=questaoService.salvarRespostas(mensagemErro, q, questao.getResposta());	
    	}
    	
    	if(mensagemErro!= null && !"".equals(mensagemErro)) {
    		throw new BusinessException(mensagemErro);
    	}
    	
	}
	
    @GetMapping("/questaoDelete/{id}")
    public void questaoDelete(@PathVariable Long id) {
    	QuestaoDTO questao = questaoService.findById(id);
    	questaoService.deleteQuestao(questao);
    	
    }
    
    @GetMapping("/respostaDelete/{id}")
    public void respostaDelete(@PathVariable Long id) {
    	RespostaDTO resposta = respostaService.findById(id);
    	respostaService.delete(resposta);
    }   
    
    @GetMapping("/imagemDelete/{id}")
    public void imagemDelete(@PathVariable Long id) {
    	ImagemDTO imagem = imagemService.findById(id);
    	imagemService.delete(imagem);
    }  
	
	
	
}
