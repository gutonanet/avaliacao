package br.com.augustolemes.avaliacao.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.augustolemes.avaliacao.dto.ImagemDTO;
import br.com.augustolemes.avaliacao.service.ImagemService;



@Controller
@RequestMapping(value = "/dbresources/images")
public class ImageController {

	@Autowired
	private ImagemService service;
	
	@RequestMapping(value = "/{id}")
	public void writePicture(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
	        try{
	            ImagemDTO img = service.findById(id);
	            //response.setContent(img.getImagem());
	            response.setContentLength(img.getImagem().length);
	            response.setContentType(img.getTipoImagem());

	            //additionally, you should add the mime-type and the last
	            //change date (to allow the browsers to use the cache) if these info are available

	            response.getOutputStream().write(img.getImagem());
	            response.setStatus(HttpServletResponse.SC_OK);
	        }
	        catch(Exception e){
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404. Add specific catch for specific errors
	        }
	}
	
}
