package br.com.sgv.repository;

import br.com.sgv.model.Desconto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescontoRepository extends JpaRepository<Desconto, Long> {
    // O Spring Data JPA já provê métodos como save(), findById(), findAll(), delete(), etc.
}
