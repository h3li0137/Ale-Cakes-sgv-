package br.com.sgv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import br.com.sgv.model.Desconto;
import br.com.sgv.repository.DescontoRepository;

import java.util.List;

@Controller
@RequestMapping("/descontos")
public class DescontoController {

    @Autowired
    private DescontoRepository descontoRepository;

    // Lista de descontos
    @GetMapping
    public String listarDescontos(Model model) {
        // Buscar a lista de descontos no repositório
        List<Desconto> descontos = descontoRepository.findAll();
        
        // Adicionar a lista de descontos ao modelo, para que possa ser usada na view
        model.addAttribute("descontos", descontos);
        
        // Retorna o nome da view para exibir a lista de descontos
        return "gerenciar_desconto";  // Página que lista os descontos
    }

    // Página para criar um novo desconto
    @GetMapping("/novo")
    public String novoDesconto(Model model) {
        // Adiciona um objeto de desconto vazio para o formulário
        model.addAttribute("desconto", new Desconto());  // Novo desconto para o formulário
        return "editar_desconto";  // Nome da view para editar/criar o desconto
    }

    // Endpoint para salvar o desconto
    @PostMapping("/salvar")
    public String salvarDesconto(@ModelAttribute Desconto desconto) {
        // Salva o desconto no repositório
        descontoRepository.save(desconto);
        
        // Redireciona para a lista de descontos após salvar
        return "redirect:/descontos";  // Redireciona para a lista de descontos
    }

    // Página para editar um desconto existente
    @GetMapping("/editar/{id}")
    public String editarDesconto(@PathVariable Long id, Model model) {
        // Busca o desconto pelo id
        Desconto desconto = descontoRepository.findById(id).orElse(null);
        
        // Se o desconto não for encontrado, redireciona para a lista de descontos
        if (desconto == null) {
            return "redirect:/descontos";
        }

        // Adiciona o desconto ao modelo para preenchê-lo no formulário
        model.addAttribute("desconto", desconto);
        
        // Retorna a página de edição
        return "editar_desconto";  // Nome da view para editar um desconto
    }

    // Endpoint para atualizar o desconto via AJAX (sem redirecionamento)
    @PostMapping("/editar")
    @ResponseBody
    public ResponseEntity<Desconto> atualizarDesconto(@RequestParam Long id, @RequestParam String codigo, @RequestParam Double percentual) {
        // Busca o desconto no repositório
        Desconto desconto = descontoRepository.findById(id).orElse(null);
        
        // Verifica se o desconto foi encontrado
        if (desconto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Retorna erro se não encontrado
        }

        // Atualiza os campos do desconto
        desconto.setCodigo(codigo);
        desconto.setPercentual(percentual);

        // Salva o desconto atualizado
        descontoRepository.save(desconto);
        
        // Retorna a resposta com o desconto atualizado
        return ResponseEntity.ok(desconto);  // Retorna o desconto atualizado em formato JSON
    }

    // Endpoint para excluir um desconto

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/excluir/{id}")
    public String excluirDesconto(@PathVariable Long id) {
        // Exclui o desconto pelo id
        descontoRepository.deleteById(id);
        
        // Redireciona para a lista de descontos após a exclusão
        return "redirect:/descontos";
    }
}
