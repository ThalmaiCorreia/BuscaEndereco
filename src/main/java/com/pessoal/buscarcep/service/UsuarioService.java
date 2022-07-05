package com.pessoal.buscarcep.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.pessoal.buscarcep.model.Usuario;
import com.pessoal.buscarcep.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) throws IOException{
		
		if (usuarioRepository.findByNome(usuario.getNome()).isPresent())
			return Optional.empty();
		
		/* - Consumindo API pública externa Via Cep*/
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");  //Criando um novo objeto do tipo URL que recebe o endereço que vai ser consumido
		
		URLConnection connection = url.openConnection(); //Abrindo conexão como objeto url
		
		InputStream is = connection.getInputStream(); //Obtendo os dados da requisição
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
		
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		usuario.setCep(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		
		/* - Consumindo API pública externa Via Cep */
		
		return Optional.of(usuarioRepository.save(usuario));
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) throws Exception  {
		if(usuarioRepository.findById(usuario.getId()).isPresent()) {
			
			/* - Consumindo API pública externa Via Cep*/
			URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");  //Criando um novo objeto do tipo URL que recebe o endereço que vai ser consumido
			
			URLConnection connection = url.openConnection(); //Abrindo conexão como objeto url
			
			InputStream is = connection.getInputStream(); //Obtendo os dados da requisição
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
			
			usuario.setCep(userAux.getCep());
			usuario.setLogradouro(userAux.getLogradouro());
			usuario.setComplemento(userAux.getComplemento());
			usuario.setBairro(userAux.getBairro());
			usuario.setLocalidade(userAux.getLocalidade());
			usuario.setUf(userAux.getUf());
			
			/* - Consumindo API pública externa Via Cep */
			
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		
		return Optional.empty();
	}
	
}