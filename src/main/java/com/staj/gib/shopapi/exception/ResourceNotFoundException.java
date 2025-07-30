package com.staj.gib.shopapi.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(resourceType + " not found: " + resourceId);
        this.resourceType = resourceType;
        this.resourceId   = resourceId.toString();
    }

    public String getResourceType() { return resourceType; }
    public String getResourceId()   { return resourceId;   }
}
