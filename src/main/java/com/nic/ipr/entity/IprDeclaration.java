package com.nic.ipr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "ipr_declaration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IprDeclaration {

    @Id
    @SequenceGenerator(name = "ipr_declaration_sequence", sequenceName = "ipr_declaration_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipr_declaration_sequence")
    private Long id;

    @OneToOne
    @JoinColumn(name = "ipr_return_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_declaration_ipr_return"))
    private IprReturn iprReturn;

    @Column(columnDefinition = "TEXT")
    private String declarationText;

    private Boolean agreed;
    private LocalDate declarationDate;
    private String place;
    private String employeeSignature;
}