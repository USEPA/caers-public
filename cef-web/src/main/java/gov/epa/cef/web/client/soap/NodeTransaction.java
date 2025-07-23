/*
 * © Copyright 2019 EPA CAERS Project Team
 *
 * This file is part of the Common Air Emissions Reporting System (CAERS).
 *
 * CAERS is free software: you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License as published by the Free Software Foundation, 
 * either version 3 of the License, or (at your option) any later version.
 *
 * CAERS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with CAERS.  If 
 * not, see <https://www.gnu.org/licenses/>.
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
