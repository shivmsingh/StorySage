package com.major.bookcatalog.RecommendationUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private String username;
    private Long bookId;
    private String method;
}
