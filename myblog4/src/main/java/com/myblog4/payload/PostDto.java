package com.myblog4.payload;
import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private  long id;
    @NotEmpty
    @Size(min = 10, message = "Post title should have at least 10 characters")
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String content;
}
