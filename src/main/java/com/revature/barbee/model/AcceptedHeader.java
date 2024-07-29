package com.revature.barbee.model;

import java.util.List;
import java.util.Optional;

public class AcceptedHeader {
    public final HTTPMIMEType type;
    public final double quality;

    public AcceptedHeader(HTTPMIMEType type, double quality) {
        this.type = type;
        this.quality = quality;
    }

    public AcceptedHeader(HTTPMIMEType type) {
        this.type = type;
        this.quality = 1.0;
    }

    public static Optional<HTTPMIMEType> firstSupported(List<AcceptedHeader> acceptedHeaders, List<HTTPMIMEType> supported) {
        for (AcceptedHeader accepted : acceptedHeaders) {
            if (supported.contains(accepted.type)) {
                return Optional.of(accepted.type);
            }
        }
        return Optional.empty();
    }
}
