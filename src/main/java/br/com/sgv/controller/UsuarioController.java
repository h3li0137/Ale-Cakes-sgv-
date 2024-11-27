package br.com.sgv.controller;

import br.com.sgv.model.Usuario;
import br.com.sgv.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller de Usuários
 * Permite visualizar, criar, editar e excluir usuários.
 */
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Exibe a lista de usuários
     * @param model
     * @return 
     */
    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("listaUsuarios", usuarioRepository.findAll());
        return "gerenciar_usuarios";
    }

    /**
     * Exibe o formulário para criar um novo usuário
     * @param model
     * @return 
     */
    @GetMapping("/usuarios/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "editar_usuario";
    }

    /**
     * Exibe o formulário para editar um usuário existente
     * @param id
     * @param model
     * @return 
     */
    @GetMapping("/usuarios/{id}")
    public String editar(@PathVariable("id") long id, Model model) {
        // Verifica se o usuário existe no banco antes de exibir
        return usuarioRepository.findById(id)
            .map(usuario -> {
                model.addAttribute("usuario", usuario);
                return "editar_usuario";
            })
            .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o ID: " + id));
    }

    /**
     * Salva um novo usuário ou atualiza um existente
     * @param usuario
     * @param result
     * @return 
     */
    @PostMapping("/usuarios")
    public String salvar(@Valid Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return "editar_usuario";
        }

        // Criptografa a senha antes de salvar no banco
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // Salva o usuário no banco
        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }

    /**
     * Exclui um usuário pelo ID
     * @param id
     * @return 
     */
    @PostMapping("/usuarios/{id}")
    public String excluir(@PathVariable("id") long id) {
        // Verifica se o usuário existe antes de excluir
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado com o ID: " + id);
        }
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }
}
