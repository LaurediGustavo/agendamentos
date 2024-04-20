package br.com.tcc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "consulta_estendida")
public class ConsultaEstendida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultaestendidade_id", referencedColumnName = "id")
    private Consulta consultaEstendidaDe;

    @ManyToOne
    @JoinColumn(name = "consultaestendidapara_id", referencedColumnName = "id")
    private Consulta consultaEstendidaPara;

}
