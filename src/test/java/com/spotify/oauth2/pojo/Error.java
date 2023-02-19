package com.spotify.oauth2.pojo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Value
@Builder
@Jacksonized

@JsonInclude(JsonInclude.Include.NON_NULL)
    public class Error {

        @JsonProperty("error")
         InnerError error;

    }

