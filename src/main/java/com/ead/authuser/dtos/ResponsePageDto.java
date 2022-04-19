package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ResponsePageDto<T> extends PageImpl<T> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageDto(@JsonProperty("content") final List<T> content,
                           @JsonProperty("number") final int number,
                           @JsonProperty("size") final int size,
                           @JsonProperty("totalElements") final Long totalElements,
                           @JsonProperty("pageable") final JsonNode pageable,
                           @JsonProperty("last") final boolean last,
                           @JsonProperty("totalPages") final int totalPages,
                           @JsonProperty("sort") final JsonNode sort,
                           @JsonProperty("first") final boolean first,
                           @JsonProperty("empty") final boolean empty){

        super(content, PageRequest.of(number,size), totalElements);
    }

    public ResponsePageDto(final List<T> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    public ResponsePageDto(final List<T> content) {
        super(content);
    }
}
