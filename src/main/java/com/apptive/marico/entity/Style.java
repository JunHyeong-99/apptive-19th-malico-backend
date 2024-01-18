package com.apptive.marico.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String image;

    @Column
    private String category;

    @ManyToOne
    @JoinColumn(name = "stylist_id")
    private Stylist stylist;

    @ManyToMany(mappedBy = "preferredStyles")
    private List<Member> members = new ArrayList<>();
}
