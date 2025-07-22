package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import java.util.List;

public class SideNavItemDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private Long id;
    private String identifier;
    private String description;
    private String url;
    private List<SideNavItemDto> children;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<SideNavItemDto> getChildren() {
		return children;
	}
	public void setChildren(List<SideNavItemDto> children) {
		this.children = children;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
