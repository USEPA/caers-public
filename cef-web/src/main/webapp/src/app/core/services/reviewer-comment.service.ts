/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
