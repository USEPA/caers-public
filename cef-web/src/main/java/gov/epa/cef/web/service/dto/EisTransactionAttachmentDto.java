/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.service.dto;

import java.io.Serializable;
import gov.epa.cef.web.util.TempFile;

public class EisTransactionAttachmentDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long transactionHistoryId;
	private String fileName;
	private TempFile attachment;
	private String fileType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransactionHistoryId() {
        return transactionHistoryId;
    }

    public void setTransactionHistoryId(Long transactionHistoryId) {
        this.transactionHistoryId = transactionHistoryId;
    }

    public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public TempFile getAttachment() {
		return attachment;
	}

	public void setAttachment(TempFile attachment) {
		this.attachment = attachment;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EisHistoryAttachmentDto [id=").append(id).append(", transactionHistoryId=")
                .append(transactionHistoryId).append(", fileName=").append(fileName).append(", attachment=")
                .append(attachment).append(", fileType=").append(fileType).append("]");
        return builder.toString();
    }

}
