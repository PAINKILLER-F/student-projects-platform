package com.platform.projapp.model;

import com.sun.xml.txw2.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "presentation_generator")
    @SequenceGenerator(name="presentation_generator", sequenceName = "presentation_seq1", allocationSize=1)
    @Column(unique = true)
    private Long id;
    private byte[] presentation;

    public Presentation(byte[] presentation) {
        this.presentation = presentation;
    }
}