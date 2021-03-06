package com.unknownbrain.recipeapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document
public class Category {

    @Id
    private String id;
    private String description;
    private List<Recipe> recipes;
}
