package br.com.tcc.repository;

import br.com.tcc.entity.Role;
import br.com.tcc.enumerador.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(RoleNameEnum roleNeme);

}
