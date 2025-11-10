package io.cucumber.cienvironment;

import java.net.URI;
import java.net.URISyntaxException;

final class RemoveUserInfo {

    private RemoveUserInfo(){
        // Utility class
    }
    
    static String fromUrl(String value) {
        try {
            URI uri = URI.create(value);
            return new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()).toASCIIString();
        } catch (URISyntaxException | IllegalArgumentException e) {
            return value;
        }
    }

}
