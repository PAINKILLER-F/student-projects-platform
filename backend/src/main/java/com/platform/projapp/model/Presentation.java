package com.platform.projapp.model;

import com.sun.xml.txw2.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.File;
import java.util.Base64;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "presentation_generator")
    @SequenceGenerator(name="presentation_generator", sequenceName = "presentation_seq", allocationSize=50)
    @Column(unique = true)
    private Long id;
    //@ElementCollection
    private Byte[] presentation;
    @OneToOne
    private Sprint sprint;

    public Presentation(Byte[] presentation) {
        this.presentation = presentation;
    }
}
