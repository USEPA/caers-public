/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
export enum ReportAction {
  CREATED = 'Created New',
  COPIED_FWD = 'Copied Forward',
  UPLOADED = 'Report Uploaded',
  REOPENED = 'Reopened',
  ACCEPTED = 'Accepted',
  REJECTED = 'Rejected',
  SEMIANNUAL_SUBMITTED = 'Semiannual Submitted',
  SEMIANNUAL_REJECTED = 'Semiannual Rejected',
  READY_TO_CERTIFY = 'Report Ready to Certify',
  SUBMITTED = 'Submitted',
  ATTACHMENT = 'Uploaded Attachment',
  ATTACHMENT_DELETED = 'Attachment Deleted',
  ADVANCED_QA = 'Advanced QA',
  OPTED_OUT = 'Opted Out',
  DELETED = 'Deleted',
  MARKED_ONRE = "Marked ONRE"
}
