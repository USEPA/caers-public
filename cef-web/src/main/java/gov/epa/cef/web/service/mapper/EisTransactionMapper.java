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
package gov.epa.cef.web.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import gov.epa.cef.web.domain.EisTransactionAttachment;
import gov.epa.cef.web.domain.EisTransactionHistory;
import gov.epa.cef.web.service.dto.EisTransactionAttachmentDto;
import gov.epa.cef.web.service.dto.EisTransactionHistoryDto;

@Mapper(componentModel = "spring")
public interface EisTransactionMapper {

    EisTransactionHistoryDto historyToDto(EisTransactionHistory source);

    List<EisTransactionHistoryDto> historyToDtoList(List<EisTransactionHistory> source);

    @Mapping(source="transactionHistoryId", target="transactionHistory.id")
    @Mapping(target="attachment", ignore = true)
    EisTransactionAttachment attachmentFromDto(EisTransactionAttachmentDto source);

    @Mapping(source="transactionHistory.id", target="transactionHistoryId")
    @Mapping(target="attachment", ignore = true)
    EisTransactionAttachmentDto attachmentToDto(EisTransactionAttachment source);

    List<EisTransactionAttachmentDto> attachmentToDtoList(List<EisTransactionAttachment> source);

}
