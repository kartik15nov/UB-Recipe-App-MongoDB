package com.unknownbrain.recipeapp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notes {
    private String id;
    private String recipeNotes;
    private Recipe recipe;
}
