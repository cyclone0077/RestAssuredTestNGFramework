
package com.spotify.oauth2.pojo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Getter @Setter
@Value
@Builder
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Playlist {

    @JsonProperty("collaborative")
     Boolean collaborative;
    @JsonProperty("description")
     String description;
    @JsonProperty("external_urls")
     ExternalUrls externalUrls;
    @JsonProperty("followers")
     Followers followers;
    @JsonProperty("href")
     String href;
    @JsonProperty("id")
     String id;
    @JsonProperty("images")
     List<Object> images;
    @JsonProperty("name")
     String name;
    @JsonProperty("owner")
     Owner owner;
    @JsonProperty("primary_color")
     Object primaryColor;
    @JsonProperty("public")
     Boolean _public;
    @JsonProperty("snapshot_id")
     String snapshotId;
    @JsonProperty("tracks")
     Tracks tracks;
    @JsonProperty("type")
     String type;
    @JsonProperty("uri")
     String uri;

}