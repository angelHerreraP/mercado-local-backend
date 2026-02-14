package com.example.aws_final.roles.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Soloe s un identificador
@Table(name ="roles") //Ahorro de recursos en buscar
@NoArgsConstructor //Constructor Vacio, necesario para JPA
@Data //Getters y Setters
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    //Aunque ya tienes el noArgs, necesitas uno que entregue el name sin entregar el id
    public Role(String name){
        this.name = name;
    }

    //Constructor vacio, getters y setters, ya creados
}
