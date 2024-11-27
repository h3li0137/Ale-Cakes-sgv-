package br.com.sgv.repository;

import br.com.sgv.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// Alteração: Mudando o método para buscar por 'login' em vez de 'email'
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Modifiquei de findByEmail para findByLogin
    Usuario findByLogin(String login);
}
