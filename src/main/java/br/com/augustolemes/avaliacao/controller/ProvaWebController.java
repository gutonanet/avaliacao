package br.com.augustolemes.avaliacao.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.augustolemes.avaliacao.dto.AnoEnum;
import br.com.augustolemes.avaliacao.dto.DadosProvaTO;
import br.com.augustolemes.avaliacao.dto.DadosQuestaoTO;
import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.dto.MateriaDTO;
import br.com.augustolemes.avaliacao.dto.PosicaoEnum;
import br.com.augustolemes.avaliacao.dto.ProvaDTO;
import br.com.augustolemes.avaliacao.dto.QuestaoDTO;
import br.com.augustolemes.avaliacao.dto.RespostaDTO;
import br.com.augustolemes.avaliacao.dto.TipoProvaEnum;
import br.com.augustolemes.avaliacao.dto.TurmaEnum;
import br.com.augustolemes.avaliacao.service.ImagemService;
import br.com.augustolemes.avaliacao.service.MateriaService;
import br.com.augustolemes.avaliacao.service.ProvaService;
import br.com.augustolemes.avaliacao.service.ProvaWordService;
import br.com.augustolemes.avaliacao.service.QuestaoService;
import br.com.augustolemes.avaliacao.service.RespostaService;

@Controller
public class ProvaWebController {
	
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
	
    @Value( "${template.arquivo}" )
    private String arquivoTemplate;

    
    @RequestMapping("/")
    public String index(Model model) {
    	//model.addAttribute("mensagem","");
    	model.addAttribute("item", new DadosProvaTO());
    	Iterable<MateriaDTO> listaMaterias = materiaService.findAll();
    	model.addAttribute("materias", listaMaterias);
    	model.addAttribute("tiposProva", TipoProvaEnum.values());
    	model.addAttribute("anos", AnoEnum.values());
    	model.addAttribute("turmas", TurmaEnum.values());
        return "index";
    }
    
    @RequestMapping(value="/listaQuestoes", method = RequestMethod.POST)
    public String listaQuestoes(Model model, DadosProvaTO item) {
    	model.addAttribute("mensagem","");
    	String mensagemErro = validarProva(item);
    	if(mensagemErro!= null && !"".equals(mensagemErro)) {
    	   	model.addAttribute("mensagem",mensagemErro);
    	   	return index(model);
    	       		
    	}
   
    	
    	//model.addAttribute("mensagem","");
    	ProvaDTO prova = provaService.findProva(item);
    	model.addAttribute("prova", prova);
    	//model.addAttribute("lista", lista);
    	model.addAttribute("item", item);
    	model.addAttribute("questoes", prova.getQuestoes());
    	MateriaDTO materia = materiaService.findById(item.getIdMateria());
    	model.addAttribute("materia",materia);
    	TipoProvaEnum tipo = TipoProvaEnum.getId(item.getIdTipoProva());
    	model.addAttribute("tipoProva", tipo);
    	
       // notesService.saveNotes(notes);
        //model.addAttribute("notesList", notesService.findAll());
        return "listaQuestoes";
    }
    
    @RequestMapping(value="/questaoSalvar", method = RequestMethod.POST)
    public String questaoSalvar(Model model, DadosQuestaoTO questao, @RequestParam("file") MultipartFile file) {
    	model.addAttribute("mensagem","");
    	ProvaDTO prova = provaService.findById(questao.getIdProva());
    	
    	QuestaoDTO questaoDTO = questaoService.converter(questao, prova);
    	DadosProvaTO dadosProva = provaService.converter(prova);
    	String mensagemErro = validarQuestionario(questao);
    	QuestaoDTO q = questaoDTO;
    	if(mensagemErro != null && !"".equals(mensagemErro)) {
    		model.addAttribute("mensagem",mensagemErro);
    	}else {
    		q = questaoService.save(questaoDTO);	
    	}
    	
    	DadosQuestaoTO dadosQuestao = questaoService.converter(q);
    	model.addAttribute("questao", dadosQuestao);
    	model.addAttribute("prova", dadosProva);
       	MateriaDTO materia = materiaService.findById(dadosProva.getIdMateria());
    	model.addAttribute("materia",materia);
    	TipoProvaEnum tipo = TipoProvaEnum.getId(dadosProva.getIdTipoProva());
    	model.addAttribute("tipoProva", tipo);
    	model.addAttribute("posicoes", PosicaoEnum.values());

    	
    	try {
        	if(file!= null) {
        		if(!"".equals(file.getOriginalFilename()))
        			singleFileUpload(file, questao.getLegendaImagem(),questao.getPosicaoImagem(), q, mensagemErro, model);
        	}
    	}catch(Exception e) {
    		mensagemErro += " Erro ao efetuar o upload.";
    		model.addAttribute("mensagem",mensagemErro);
    		//e.printStackTrace();
    	}


    	if((mensagemErro== null || "".equals(mensagemErro)) && questao.getResposta()!= null && !"".equals(questao.getResposta())) {
    		mensagemErro=salvarRespostas(mensagemErro,model, q, questao.getResposta());	
    	}
    	if(q!= null && q.getId()!= null) {
        	List<ImagemDTO> imagens = imagemService.findByQuestao(q);
    		model.addAttribute("imagens", imagens); 
    		List<RespostaDTO> respostas = respostaService.findByQuestao(q);
    		model.addAttribute("respostas", respostas);
    	}
    	
    	if(mensagemErro == null || "".equals(mensagemErro)) {
    	   	model.addAttribute("mensagem","Registro salvo com sucesso.");
    	}

        return "editarQuestoes";
    }

    
    private String salvarRespostas(String mensagemErro, Model model, QuestaoDTO questaoDTO, String respostaTexto) {
		List<RespostaDTO> respostas = respostaService.findByQuestao(questaoDTO);
		
		if(respostas.size()>=5) {
			mensagemErro+=" Cada questão só pode ter no máximo 5 respostas.";
	    	return mensagemErro;
			
		}else {
    		RespostaDTO resposta = new RespostaDTO();
    		resposta.setQuestao(questaoDTO);
    		resposta.setResposta(respostaTexto);
    		respostaService.save(resposta);
			
		}

		return mensagemErro;
	}

 
    private String singleFileUpload(MultipartFile file, String legenda, Integer posicao, QuestaoDTO questaoDTO, String mensagemErro, Model model) throws IOException { 	
		List<ImagemDTO> imagens = imagemService.findByQuestao(questaoDTO);
		
		if(imagens.size()>=5) {
			mensagemErro+=" Cada questão só pode ter no máximo 5 imagens.";
	    	return mensagemErro;
		}else {
	    	ImagemDTO img = new ImagemDTO();
	 		byte[] imagem = file.getBytes();
	        img.setImagem(imagem);
	        img.setNome(file.getOriginalFilename());
	        img.setLegenda(legenda);
	        img.setPosicao(posicao);
	        img.setQuestao(questaoDTO);
	        imagemService.save(img);
		}         

		
		
		return mensagemErro;
     }

    
    
    private String validarQuestionario(DadosQuestaoTO questao) {
    	String retorno = null;
    	if(questao.getHabilidade() == null || "".equals(questao.getHabilidade())) {
    		retorno = " O Campo habilidade deve ser preenchido.";
    	}
    	if(questao.getQuestao() == null || "".equals(questao.getQuestao())) {
    		retorno += " O Campo Questão deve ser preenchido.";
    	}
    	return retorno;
    }
    
    private String validarProva(DadosProvaTO prova) {
    	String retorno = "";

    	if(prova.getIdMateria() == null || prova.getIdMateria() == 0 ) {
    		retorno += " O Campo Matéria deve ser selecionado.";
    	}
    	if(prova.getIdTipoProva() == null || prova.getIdTipoProva() == 0) {
    		retorno += " O Campo Tipo de Prova deve ser selecionado.";
    	}
      	if(prova.getTurma() == null || "".equals(prova.getTurma())) {
    		retorno += " O Campo Turma deve ser preenchido.";
    	}
  
    	
    	return retorno;
    }
    
    @RequestMapping(value={"/novaQuestao","/novaQuestao/{id}"}, method = RequestMethod.GET)
    public String novaQuestao(Model model, @PathVariable(required = false, name = "id") Long id) {
    	model.addAttribute("mensagem","");
    	model.addAttribute("posicoes", PosicaoEnum.values());
    	ProvaDTO prova = provaService.findById(id);
    	DadosProvaTO dadosProva = provaService.converter(prova);
    	model.addAttribute("prova", dadosProva);
    	DadosQuestaoTO dadosQuestao = new DadosQuestaoTO();
    	dadosQuestao.setIdProva(prova.getId());
    	model.addAttribute("questao", dadosQuestao);
    	MateriaDTO materia = materiaService.findById(dadosProva.getIdMateria());
    	model.addAttribute("materia",materia);
    	TipoProvaEnum tipo = TipoProvaEnum.getId(prova.getTipoProva());
    	model.addAttribute("tipoProva", tipo);

    	
        return "editarQuestoes";
    }  
    
    @RequestMapping(value={"/editarQuestao","/editarQuestao/{id}"}, method = RequestMethod.GET)
    public String editarQuestao(Model model, @PathVariable(required = false, name = "id") Long id) {
    	model.addAttribute("mensagem","");
    	model.addAttribute("posicoes", PosicaoEnum.values());
    	QuestaoDTO questao = questaoService.findById(id);
    	DadosProvaTO dadosProva = provaService.converter(questao.getProva());
    	model.addAttribute("prova", dadosProva);
    	DadosQuestaoTO dadosQuestao = questaoService.converter(questao);
    	dadosQuestao.setIdProva(dadosProva.getIdProva());
    	model.addAttribute("questao", dadosQuestao);
    	MateriaDTO materia = materiaService.findById(dadosProva.getIdMateria());
    	model.addAttribute("materia",materia);
    	TipoProvaEnum tipo = TipoProvaEnum.getId(dadosProva.getIdTipoProva());
    	model.addAttribute("tipoProva", tipo);
    	List<RespostaDTO> respostas = respostaService.findByQuestao(questao);
		model.addAttribute("respostas", respostas);
    	List<ImagemDTO> imagens = imagemService.findByQuestao(questao);
		model.addAttribute("imagens", imagens);
        return "editarQuestoes";
    }    

    @RequestMapping(value={"/questaoDelete","/questaoDelete/{id}"}, method = RequestMethod.GET)
    public String questaoDelete(Model model, @PathVariable(required = false, name = "id") Long id) {
    	model.addAttribute("mensagem","");
    	QuestaoDTO questao = questaoService.findById(id);
    	ProvaDTO prova = questao.getProva();
    	questaoService.deleteQuestao(questao);
    	DadosProvaTO dados = provaService.converter(prova);
    	return listaQuestoes(model, dados);
    	
    }
    
    @RequestMapping(value={"/respostaDelete","/respostaDelete/{id}"}, method = RequestMethod.GET)
    public String respostaDelete(Model model, @PathVariable(required = false, name = "id") Long id) {
    	model.addAttribute("mensagem","");
    	RespostaDTO resposta = respostaService.findById(id);
    	QuestaoDTO questao = resposta.getQuestao();
    	DadosProvaTO dadosProva = provaService.converter(questao.getProva());
    	model.addAttribute("prova", dadosProva);
    	DadosQuestaoTO dadosQuestao = questaoService.converter(questao);
    	dadosQuestao.setIdProva(dadosProva.getIdProva());
    	model.addAttribute("questao", dadosQuestao);
    	MateriaDTO materia = materiaService.findById(dadosProva.getIdMateria());
    	model.addAttribute("materia",materia);
    	TipoProvaEnum tipo = TipoProvaEnum.getId(dadosProva.getIdTipoProva());
    	model.addAttribute("tipoProva", tipo);
    	respostaService.delete(resposta);
    	List<RespostaDTO> respostas = respostaService.findByQuestao(questao);
		model.addAttribute("respostas", respostas);
    	List<ImagemDTO> imagens = imagemService.findByQuestao(questao);
		model.addAttribute("imagens", imagens);
        return "editarQuestoes";
    }   
    
    @RequestMapping(value={"/imagemDelete","/imagemDelete/{id}"}, method = RequestMethod.GET)
    public String imagemDelete(Model model, @PathVariable(required = false, name = "id") Long id) {
    	model.addAttribute("mensagem","");
    	ImagemDTO imagem = imagemService.findById(id);
    	QuestaoDTO questao = imagem.getQuestao();
    	DadosProvaTO dadosProva = provaService.converter(questao.getProva());
    	model.addAttribute("prova", dadosProva);
    	DadosQuestaoTO dadosQuestao = questaoService.converter(questao);
    	dadosQuestao.setIdProva(dadosProva.getIdProva());
    	model.addAttribute("questao", dadosQuestao);
    	MateriaDTO materia = materiaService.findById(dadosProva.getIdMateria());
    	model.addAttribute("materia",materia);
    	TipoProvaEnum tipo = TipoProvaEnum.getId(dadosProva.getIdTipoProva());
    	model.addAttribute("tipoProva", tipo);
    	imagemService.delete(imagem);
    	List<RespostaDTO> respostas = respostaService.findByQuestao(questao);
		model.addAttribute("respostas", respostas);
    	List<ImagemDTO> imagens = imagemService.findByQuestao(questao);
		model.addAttribute("imagens", imagens);
		
        return "editarQuestoes";
    }   
    
    @RequestMapping(value={"/voltarQuestao","/voltarQuestao/{id}"}, method = RequestMethod.GET)
    public String voltarQuestao(Model model, @PathVariable(required = false, name = "id") Long id) {
    	//model.addAttribute("mensagem","");
    	QuestaoDTO questao = questaoService.findById(id);
    	ProvaDTO prova = questao.getProva();
    	DadosProvaTO dados = provaService.converter(prova);
    	return listaQuestoes(model, dados);
    	
    }
    
    
    
    //Upload
	  //Save the uploaded file to this folder

     
    /*

    public void singleFileUpload(String id, MultipartFile file) throws IOException { 	
    		


            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(caminhoImagem + id+"__"+ file.getOriginalFilename());
            Files.write(path, bytes);


    }
    
    */
    @RequestMapping(value={"/imprimir","/imprimir/{id}"}, method = RequestMethod.GET)
    public String imprimir(Model model, @PathVariable(required = false, name = "id") Long id, HttpServletRequest request, HttpServletResponse response) {
    	model.addAttribute("mensagem","");
    	ProvaDTO prova = provaService.findProvaAllData(id);
    	try {
    		provaWordService.readDocxFile(prova, arquivoTemplate, request, response);
    		model.addAttribute("mensagem","Prova gerada com sucesso");
    	}catch (Exception e) {
    		model.addAttribute("mensagem","Erro ao gerar a prova.");
    		e.printStackTrace();
    	}
    	
    	
    	
        return voltarQuestao(model, prova.getQuestoes().get(0).getId());
    }      


	public MateriaService getMateriaService() {
		return materiaService;
	}

	public void setMateriaService(MateriaService materiaService) {
		this.materiaService = materiaService;
	}

	public ProvaService getProvaService() {
		return provaService;
	}

	public void setProvaService(ProvaService provaService) {
		this.provaService = provaService;
	}

	public QuestaoService getQuestaoService() {
		return questaoService;
	}

	public void setQuestaoService(QuestaoService questaoService) {
		this.questaoService = questaoService;
	}
    
    
    
    

}
