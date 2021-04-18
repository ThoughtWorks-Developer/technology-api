package com.tw.techops.dx;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "TECHNOLOGY")
@NoArgsConstructor
@RestResource
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Technology {

    @JsonIgnore
    @Id
    @Column(name = "ID")
    private String id = UUID.randomUUID().toString();

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IMAGE")
    private String image;

    @Column(name = "TAGS")
    private String tags;
}
