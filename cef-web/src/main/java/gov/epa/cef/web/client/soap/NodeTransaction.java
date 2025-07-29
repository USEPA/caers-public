/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package gov.epa.cef.web.client.soap;

import net.exchangenetwork.schema.node._2.TransactionStatusCode;

public class NodeTransaction {

    private final TransactionStatusCode statusCode;

    private final String statusDetail;

    private final String transactionId;

    public NodeTransaction(String transactionId,
                           TransactionStatusCode statusCode,
                           String statusDetail) {

        super();

        this.transactionId = transactionId;
        this.statusCode = statusCode;
        this.statusDetail = statusDetail;
    }

    public TransactionStatusCode getStatusCode() {

        return statusCode;
    }

    public String getStatusDetail() {

        return statusDetail;
    }

    public String getTransactionId() {

        return transactionId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeTransaction [statusCode=").append(statusCode).append(", statusDetail=").append(statusDetail)
                .append(", transactionId=").append(transactionId).append("]");
        return builder.toString();
    }
}
