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
import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ReviewerComment} from "../../shared/models/reviewer-comment";
import {EntityType} from "../../shared/enums/entity-type";

@Injectable({
  providedIn: 'root'
})
export class ReviewerCommentService {

    private baseUrl = 'api/reviewer/comment';  // URL to web api

    constructor(private http: HttpClient) { }

    create(comment: ReviewerComment): Observable<ReviewerComment> {
        const url = `${this.baseUrl}`;
        return this.http.post<ReviewerComment>(url, comment);
    }

    update(comment: ReviewerComment): Observable<ReviewerComment> {
        const url = `${this.baseUrl}/${comment.id}`;
        return this.http.put<ReviewerComment>(url, comment);
    }

    retrieve(id: number): Observable<ReviewerComment> {
        const url = `${this.baseUrl}/${id}`;
        return this.http.get<ReviewerComment>(url);
    }

    retrieveActiveForReport(reportId: number): Observable<ReviewerComment[]> {
        const url = `${this.baseUrl}/${reportId}/active`;
        return this.http.get<ReviewerComment[]>(url);
    }

    retrieveActive(entityId: number, entityType: EntityType): Observable<ReviewerComment> {
        const url = `${this.baseUrl}/${entityType}/${entityId}/active`;
        return this.http.get<ReviewerComment>(url);
    }

    retrieveDraft(entityId: number, entityType: EntityType): Observable<ReviewerComment> {
        const url = `${this.baseUrl}/${entityType}/${entityId}/draft`;
        return this.http.get<ReviewerComment>(url);
    }
}
