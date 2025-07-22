/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto.postOrder;

import java.io.Serializable;


/***
 * ReleasePointApptPostOrderDto is used to traverse the object hierarchy from the bottom up.  This ReleasePointApptPostOrderDto will contain a reference to the ReleasePointPostOrderDto
 * but the ReleasePointPostOrderDto will not contain a list of these ReleasePointApptPostOrderDto objects.  This helps avoid circular references within the MapStruct mappers when 
 * traversing the hierarchy post order.
 * @author kbrundag
 *
 */
public class ReleasePointApptPostOrderDto implements Serializable{

    /**
     * default version id
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private ReleasePointPostOrderDto releasePoint;
    private EmissionsProcessPostOrderDto emissionsProcess;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public ReleasePointPostOrderDto getReleasePoint() {
    	return releasePoint;
    }
    public void setReleasePoint(ReleasePointPostOrderDto releasePoint) {
    	this.releasePoint = releasePoint;
    }
    
    public EmissionsProcessPostOrderDto getEmissionsProcess() {
    	return emissionsProcess;
    }
    public void setEmissionsProcess(EmissionsProcessPostOrderDto emissionsProcess) {
    	this.emissionsProcess = emissionsProcess;
    }
}
