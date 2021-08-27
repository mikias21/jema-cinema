package com.jema.cinema.comment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class CommentGetResponse {
    private String userName;
    private String comment;
    private int rating;
    private String commentedDateTime;
}
